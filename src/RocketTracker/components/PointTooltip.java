package RocketTracker.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

import javax.swing.JLabel;
import javax.swing.event.MouseInputAdapter;

import RocketTracker.data.MatchInfo;

/**
 *  Based off the code from the second answer from this SO question
 *  https://stackoverflow.com/questions/5957241/text-mouseover-popups-over-a-swing-jtextarea
 * 
 *  Differs in the sense that this is hidden behind a point on the graph, but is not the original displayed "point" on the graph
 *  like in the example
 * 
 *  Tooltip that is displayed when hovering over a point on the graph
 *  Displays all of the match information
 *  
 */
public class PointTooltip extends JLabel {
	MatchInfo info;
	String text;
	public MouseInputAdapter mouseHandler;
	
	public PointTooltip(MatchInfo info, Point windowLocation) {
		super();
	
		text = info.toStringHTML();
		
		this.setText(text);
		
		setFocusable(true);
		setBackground(new Color(0, 0, 0, 0));
		
		showTooltip();
		hideTooltip();
		
		//System.out.println("Point tooltip created " + this.getBounds());		
		
		mouseHandler = new MouseInputAdapter() {
			@Override
			public void mouseEntered(final MouseEvent e) {
				//System.out.println(getBounds() + "");
				
				showTooltip();
			}
			
			@Override
			public void mouseExited(final MouseEvent e) {
				//PointTooltip.this.setOpaque(false); // How does this work wot
				
				hideTooltip();
				
				//System.out.println("mouse exited point " + getBounds());
			}
		};
		
		addMouseListener(mouseHandler);
	}
	
	public void showTooltip() {
		setText(text);
		
		PointTooltip.this.setOpaque(true); // How does this work wot
		
		Font font = getFont();
		FontRenderContext fontRenderContext = getFontMetrics(font).getFontRenderContext();
		
		Rectangle bounds = getBounds();
		Rectangle2D stringBounds = font.getStringBounds(text, fontRenderContext);
		
		bounds.width = (int) stringBounds.getWidth();
		bounds.height = (int) 100;
		setBounds(bounds);
		
		this.setForeground(new Color(200, 200, 210));
	}
	
	public void hideTooltip() {
		setText("   ");
		
		PointTooltip.this.setOpaque(false);
		
		setBounds(new Rectangle(GraphPoint.pointRadius + 2, GraphPoint.pointRadius + 2));
	}
	
	public static Point shiftLocation(Point graphPoint) {
		Point p = graphPoint;
		
		int radius = GraphPoint.pointRadius;
		
		p.x = p.x - (radius);
		p.y = p.y - (radius);
		
		//System.out.println(p + "");
		
		return p;
	}
}
