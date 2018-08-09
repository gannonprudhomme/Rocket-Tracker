package RocketTracker;

import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Basically just what shows up when Rocket League isn't running(like bakkes mod)
 * Once Rocket League is launched, this window will be destroyed
 * When Rocket League is closed, this will be constructed
 */
public class InactiveWindow extends JFrame {
	static int width = 240;
	static int height = 120;
	
	public InactiveWindow() {
		this.setTitle("Rocket Tracker");
		
		this.setSize(width, height);
		
		JLabel label = new JLabel("Inactive while Rocket League is not running");
		this.add(label);
		
		this.setVisible(true);
	}
}
