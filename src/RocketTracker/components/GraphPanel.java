package RocketTracker.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import RocketTracker.data.MatchInfo;

/*
 * Custom JPanel that renders the inputted points
 * Also, given MatchInfo, converts it to a GraphPoint
 * 
 * Based on: From https://gist.github.com/roooodcastro/6325153
 */
public class GraphPanel extends JPanel implements MouseMotionListener {
	private int padding = 25;
	private int labelPadding = padding;
	
	//private Color lineColor = new Color(219, 100, 36, 250);
	//private Color pointColor = new Color(10, 97, 184, 250);
	private Color lineColor = new Color(10, 180, 140, 250);
	private Color pointColor = new Color(70, 220, 24, 250);
	private Color gridColor = new Color(160, 160, 160, 200);
	
	private Stroke pointStroke = new BasicStroke(2f);
	
	private int numberYDivisions = 10;
	
	List<MatchInfo> matches;
	List<GraphPoint> points;

	// The starting mmr of the session, before any matches have been played(in the reference frame of this program0
	// y-intercept of the graph
	int startY = 0; 
	int startMMR = 0;
	int xScale = 40;
	
	public GraphPanel() {
		this.addMouseMotionListener(this);
		
		matches = new ArrayList<MatchInfo>();
		points = new ArrayList<GraphPoint>();
		
		int c = 90;
		this.setBackground(new Color(c, c, c));
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		int width = this.getParent().getWidth() - (padding - labelPadding - 13) / 2;
		int height = this.getParent().getHeight() - (padding - labelPadding / 2) / 2;
		this.setSize(width, height);
		
		Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		// Could(Should?) be based on how many points there are in the first place
		// 	could be only slightly based on how many points there are, keep around the same general
		//  size, unless there were a large amount of points
		//int xScale = (getWidth() - (2 * padding)) / (points.size() > 1 ? points.size() - 1 : 1); // (How much space(range) the points have to be in) / (how many points there are)

        if(points.size() > 10) {
        	//xScale /= points.size() - 10;
        }
        
        double h = getHeight();
		double max = getMaxMMR();
		double min = getMinMMR();

		//System.out.println(h + " " + max + " " + min);
		
        // Only slightly based on the range of mmr-values there are
		double  mmrDiff = max - min;
		double yScale = 1; // Default value, (Range of space to be split up) / (difference in max and min mmr's)
		if(points.size() > 0) {
			yScale = (h) / (max - min); // pixels per mmr difference?
			System.out.println(yScale + "h " + h + " diff " + (max - min) + " max: " + max + " min: " + min);
		}
			
		// mmrDiff shouldn't be used in y-values
		// points.size() shouldn't be used in x-values
		// only should if there are a lot of points
		
		// Depends if I want to scale or not
		for(int i = 0; i < points.size(); i++) {
			GraphPoint graphPoint = points.get(i);
			
			Point point = graphPoint.graphLocation; // (x, y) = (match #, mmr)
			
			int x = (int) (point.x * xScale) + padding + labelPadding + 13;
			//int y = (int) (getMaxMMR() - point.y) * yScale + 2;
			double y = h - ((h / (max - min))) * (point.y - min) - padding - labelPadding / 2;
			
			// Update the graphPoint's location
			graphPoint.windowLocation = new Point(x, (int) y);
			graphPoint.tooltip.setLocation(graphPoint.windowLocation);
			
			if(x > getWidth())
				xScale -= 1;
			
			//System.out.println(graphPoint.graphLocation.x + ", " + graphPoint.graphLocation.y);
		}
		
		// Render the labels
		
		// Render the grid-lines
		// create hatch marks and grid lines for y axis.
		drawHashAndGrid(g2);
		//drawOldHashAndGridLines(g2);
		
        /*
        // and for x axis
        for (int i = 0; i < points.size(); i++) {
            if (points.size() > 1) {
                int x0 = i * (getWidth() - padding * 2 - labelPadding) / (points.size() - 1) + padding + labelPadding;
                int x1 = x0;
                int y0 = getHeight() - padding - labelPadding;
                int y1 = y0 - GraphPoint.pointRadius;
                if ((i % ((int) ((points.size() / 20.0)) + 1)) == 0) {
                    g2.setColor(gridColor);
                    g2.drawLine(x0, getHeight() - padding - labelPadding - 1 - GraphPoint.pointRadius, x1, padding);
                    g2.setColor(Color.BLACK);
                    String xLabel = i + "";
                    FontMetrics metrics = g2.getFontMetrics();
                    int labelWidth = metrics.stringWidth(xLabel);
                    g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
                }
                g2.drawLine(x0, y0, x1, y1);
            }
        } */
        
        // Draw the mmr change between the two points, with +/- mmr gained
        g2.setColor(pointColor);
        for(int i = 0; i < points.size() - 1; i++) {
        	int mmrGain = matches.get(i + 1).getMMRChange();
        	
        	String drawString = (mmrGain >= 0 ? "+" : "") + (mmrGain + "");
        	
        	Point p1 = points.get(i).windowLocation;
        	Point p2 = points.get(i + 1).windowLocation;

        	// Flip p1.y & p2.y b/c y values increase from top to bottom
        	double slope = ((double) (p1.y - p2.y) / (double) (p2.x - p1.x));
        	
        	double distSqrd = Math.sqrt(Math.pow(p2.y - p1.y, 2) + Math.pow(p2.x - p1.x, 2));
        	double halfDist = distSqrd / 2.0;

        	double angle = Math.atan(slope);
        	
        	if(xScale < 20) {
        		Font font = g2.getFont();
        		
        		g2.setFont(new Font(font.getName(), font.getStyle(), 10));
        	}
        	
        	int stringWidth = g.getFontMetrics().stringWidth(drawString);
        	double stringHeight = g.getFontMetrics().getStringBounds(drawString, g).getHeight();
        	
        	int x1 = 0, y1 = 0;
        	
        	x1 = (int) (p1.x + (Math.cos(angle) * halfDist)) - (stringWidth / 2);
        	y1 = (int) (p1.y - (Math.sin(angle) * halfDist)) - (int) (stringHeight / 2);
        	
        	// Draw the rotated text
        	AffineTransform orig = g2.getTransform();
        	g2.rotate(-angle, x1 + (stringWidth / 2), y1);
        	g2.drawString(drawString, x1, y1);
        	g2.setTransform(orig);
        }
		
		// Render the axes
		g2.setColor(new Color(50, 50, 50));
		
		// GraphPoint.pointRadius is the width of the hash mark
		g2.drawLine(padding + labelPadding + GraphPoint.pointRadius, getHeight() - padding - labelPadding, getWidth() - padding, getHeight() - padding - labelPadding);
		
		// Render the points & lines
		
		Stroke oldStroke = g2.getStroke();
		
		g2.setColor(lineColor);
		g2.setStroke(pointStroke);	
		for(int i = 0; i < points.size() - 1; i++) {
			Point currPoint = points.get(i).windowLocation;
			Point destinationPoint = points.get(i + 1).windowLocation;
			
			g2.drawLine(currPoint.x, currPoint.y, destinationPoint.x, destinationPoint.y);
		}
		
		// Could just do these two loops at once
		
		g2.setColor(pointColor);
		g2.setStroke(oldStroke);
		for(int i = 0; i < points.size(); i++) {
			Point p = points.get(i).windowLocation;
			int x = p.x - GraphPoint.pointRadius / 2;
			int y = p.y - GraphPoint.pointRadius / 2;
			
			g2.fillOval(x, y, GraphPoint.pointRadius, GraphPoint.pointRadius);
		}
	}	
	
	private void drawHashAndGrid(Graphics2D g2) {
		// Draw max, start, and min hashes(top mid bottom)
		int x0 = padding + labelPadding;
		int x1 = GraphPoint.pointRadius + padding + labelPadding; // why pointRadius?
		int y0 = -1;
		int y1 = -1;
		
		List<Integer> yValues = new ArrayList<Integer>();
		
		double diff = ((double) (getMaxMMR() - getMinMMR()) / (double) (numberYDivisions));
		double sum = 0.0;
		for(int i = 1; i < numberYDivisions; i++, sum += diff) {
			yValues.add((int) ((numberYDivisions - i) * diff));
		}
		
		// Linearly space the hashes between the min and max mmr/y-coordinate
		for(int val : yValues) {
			// Space the hash's window coordinates
			int h = getHeight();
			int max = getMaxMMR();
			int min = getMinMMR();
			
			// Convert the mmr value for this hash into the y-coordinate
			y0 = h - ((h / (max - min))) * (val) - padding - labelPadding / 2;
			y1 = y0;
			
			//System.out.println("Value: " + (val + getMinMMR()) + " Coord: " + y0);
			quickDrawHash(g2, x0, y0, x1, y1, (val + getMinMMR()) + "");
		}
	}
	
	// Helper method for drawHashAndGrid
	private void quickDrawHash(Graphics2D g2, int x0, int y0, int x1, int y1, String text) {
		FontMetrics metrics = g2.getFontMetrics();
        int labelWidth = metrics.stringWidth(text);
        g2.drawString(text, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
       
        // draw hashmark
        g2.setColor(Color.black);
        g2.drawLine(x0, y0, x1, y1);
        
       // Draw gridline
        g2.setColor(gridColor);
        g2.drawLine(padding + labelPadding + 1 + GraphPoint.pointRadius, y0, getWidth() - padding, y1);
	}
	
	// Converts the MatchInfo to a GraphPoint, then adds it to the list to be displayed
	// Likewise, does the math for converting the mmr to y-values, and the match count to x-values
	public void addMatch(MatchInfo match) {
		matches.add(match);
		
		GraphPoint graphPoint = new GraphPoint(new Point(-1, -1), new Point(points.size(), match.currentMMR));

		// Set the start point as the original
		if(points.size() == 0) {
			startMMR = graphPoint.graphLocation.y;
			
			// Set the min and max mmr values, determines the hash marks and where points are located
			//minMMR = startMMR - 50;
			//maxMMR = startMMR + 50;
		}
		
		// If the y-value of graphPoint in window coordinates is approaching near the middle value, 
		int graphY = graphPoint.graphLocation.y;
		if(getMaxMMR() - graphY < 10) {
			//maxY = graphY + 50;
		}
			
		points.add(graphPoint);
		
		Point topleft = PointTooltip.shiftLocation(graphPoint.windowLocation);
		
		PointTooltip tooltip = new PointTooltip(match, graphPoint.windowLocation);
		tooltip.setBounds(topleft.x, topleft.y, tooltip.getWidth(), tooltip.getHeight());
		tooltip.addMouseListener(tooltip.mouseHandler);
		this.add(tooltip);
		graphPoint.tooltip = tooltip;

		this.repaint();
	}
	
	// Everytime the mouse is moved, check if the cursor is over a GraphPoint
	// If so, display tooltip information
	// The point is actually the area of a circle
 	@Override
	public void mouseMoved(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
			
		for(GraphPoint point : points) {
			int pointX = point.windowLocation.x;
			int pointY = point.windowLocation.y;
			
			// Distance would be r*sqrt(2)
			double sqrt2 = 1.41421356237; // = sqrt(2)
			
			// The hitbox is estimated to be a square, so the distance to each corner would be half the width(aka radius) times sqrt(2)
			// b/c 45-45-90 triangle rule
			double dist = GraphPoint.pointRadius * sqrt2;
			
			// Get x-values of point hitbox
			int xMin = pointX - (int) dist; 
			int xMax = pointX + (int) dist;
			
			// Get y-values of point hitbox
			int yMin = pointY - (int) dist;
			int yMax = pointY + (int) dist;
			
			if(x > xMin && x < xMax) {
				if(y > yMin && y < yMax) {
					//System.out.println("In bounds of point: \n   " + point);
					
					// Display the tooltip
					//point.tooltip.show();
				}
			} else {
				// Out of bounds
				// If there's a tooltip displayed, hide it
			}
		}
	}
 	
 	private void drawOldHashAndGridLines(Graphics2D g2) {
 	// create hatch marks and grid lines for y axis.
        for (int i = 0; i < numberYDivisions + 1; i++) {
            int x0 = padding + labelPadding;
            int x1 = GraphPoint.pointRadius + padding + labelPadding;
            int y0 = getHeight() - ((i * (getHeight() - padding * 2 - labelPadding)) / numberYDivisions + padding + labelPadding);
            int y1 = y0;
            if (points.size() > 0) {
                g2.setColor(gridColor);
                g2.drawLine(padding + labelPadding + 1 +  GraphPoint.pointRadius, y0, getWidth() - padding, y1);
                g2.setColor(Color.BLACK);
                String yLabel = ((int) ((getMinMMR() + (getMaxMMR() - getMinMMR()) * ((i * 1) / numberYDivisions)) * 100)) / 100.0 + "";
                FontMetrics metrics = g2.getFontMetrics();
                int labelWidth = metrics.stringWidth(yLabel);
                g2.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
            }
            g2.drawLine(x0, y0, x1, y1);
        }
 	}
 	
 	public void clear() {
 		matches.clear();
 		points.clear();
 	}

 	public int getMaxMMR() {
 		int startMax = startMMR + 50;
 		
 		int max = 0;
 		for(MatchInfo inf : matches) {
 			int mmr = inf.currentMMR;
 			
 			if(mmr > max)
 				max = mmr;
 		}
 		
 		if(max > startMax)
 			startMax = max + 15;
 		
 		//System.out.println("max: " + startMax);
 		
 		return startMax;
 	}
 	
 	public int getMinMMR() {
 		int startMin = startMMR - 50;
 		
 		int min = 2500;
 		for(MatchInfo inf : matches) {
 			int mmr = inf.currentMMR;
 			
 			if(mmr < min)
 				min = mmr;
 		}
 		
 		if(min < startMin)
 			startMin = min;
 		
 		return startMin;
 	}
 	
 	// Helper function for rounding to the tenths digit
 	// Might need to change for lower than 4 digit mmr values?
 	private int shaveOnesDigit(int mmr) {
 		return ((mmr / 10) * 10) + (mmr % 10 >= 5 ? 10 : 0);
 	}
 	
	// Unused
	@Override
	public void mouseDragged(MouseEvent e) {}
}
