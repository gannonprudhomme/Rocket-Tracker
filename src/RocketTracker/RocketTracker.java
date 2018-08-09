package RocketTracker;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.ssplugins.rlstats.entities.Playlist;

import RocketTracker.components.Window;
import RocketTracker.data.Session;
import RocketTracker.utils.FileIO;

public class RocketTracker {
	Session session;
	Window window;
	
	static RocketTracker tracker;
	
	public RocketTracker() {
		List<Point> points = new ArrayList<Point>();
		
		// Start the initial session
		// Will actually have multiple sessions per 
		
		// Initialize the window
		window = new Window(points);
		window.setVisible(true);
		
		addSession(Playlist.RANKED_STANDARD);
		addSession(Playlist.RANKED_DOUBLES);
		addSession(Playlist.RANKED_DUEL);
		addSession(Playlist.RANKED_SOLO_STANDARD);
	}
	
	// Create a new sesion and a new Ranked/Graph tab
	public void addSession(int playlist) {
		window.addSession(playlist);
	}
	
	public static void main(String[] args) {
		tracker =  new RocketTracker();
	}
}
