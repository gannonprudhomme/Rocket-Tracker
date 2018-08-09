package RocketTracker.components;

import java.awt.Point;
import java.util.HashMap;

import javax.swing.JLabel;

/*
 * Kinda like an extension to the Point class
 * 
 * Just a point on the graph with specific data attached to it
 * Would have one set of data(left column of tooltip) that had what the info on the right was
 * and another set of data representing the right side, aka the data
 * Set of enum objects?
 * i.e.
 * _____________________
 * | MMR Gained:    +8 |
 * | Goals Scored:   3 |
 * |___________________|
 *        \  /
 *         \/
 *         O(<- point on graph)
 * 
 */
public class GraphPoint {
	Point windowLocation; // The location of this point on the graph
	Point graphLocation; // The location in graph coordinates, in form (match number, mmr)
	
	HashMap<DataType, Integer> tooltipDataMap; // Map of all the data for the tooltips
	
	PointTooltip tooltip;
	
	public static int pointRadius = 8;
	
	enum DataType {
		MMR_GAINED,
		GOALS_GAINED,
		ASSISTS_GAINED,
		SAVES_GAINED,
		NUMBER_WINS,
		WIN_STREAK_COUNT
	}
	
	public GraphPoint(Point windowLocation, Point graphLocation) {
		this.windowLocation = windowLocation;
		this.graphLocation = graphLocation;
		//this.tooltipDataMap = dataMap;
	}
	
	public int getWindowX() {
		return 0;
	}
	
	public int getWindowY() {
		return 0;
	}
	
	@Override
	public String toString() {
		String output = "";
		
		output += "Graph Location: (" + graphLocation.getX() + ", " + graphLocation.getY() + ")\n";
		
		for(DataType type : tooltipDataMap.keySet()) {
			int value = tooltipDataMap.get(type);
			
			output += type.name() + ": " + value + "\n";
		}
		
		return output;
	}
}

