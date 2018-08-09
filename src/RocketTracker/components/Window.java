package RocketTracker.components;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JMenuItem;

import RocketTracker.data.MatchInfo;
import RocketTracker.data.Session;
import RocketTracker.data.StatManager;
import RocketTracker.utils.FileIO;
 	
/**
 *  Contains all of the window data and objects,
 *  Contains no functionality, only creates the window
 */
public class Window extends JFrame implements Runnable {
	static int width = (int) (1600 / 1.5);
	static int height = (int) (900 / 1.5) ;
	
	List<Point> points = new ArrayList<Point>();
	
	// Try to force synchronization
	List<SessionTab> tabs;
	
	public Window(List<Point> points) {
		this.points = points;
		
		tabs = new ArrayList<SessionTab>();
		
		// Create 
		this.setTitle("Rocket Tracker");
		initComponents();
		
		try {
			StatManager.updatePlayer();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		
		// Start a read to update the player after a certain delay
		ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
		exec.scheduleAtFixedRate(this, 0, 45, TimeUnit.SECONDS);
		
		//new Thread(this).start();
	}
	
	public void addSession(int playlist) {
		// Initialize session and sessiontab to be added to the window
		Session session = new Session(playlist);
		SessionTab tab = new SessionTab(session);

		// Get the actual playlist name from its ID
		String playlistName = StatManager.getPlaylistName(playlist);
		
		// Add the newly created tab to the tabbed pane, titled with the corresponding playlists
		jTabbedPane1.add(playlistName, tab);
		tabs.add(tab);
		
		this.repaint();
	}
	
	@Override
	public void run() {
		//System.out.println("Executing");
		
		// After a certain amount of time, attempt to poll the server for stat changes
		// If there are stat changes, append them to the corresponding playlist matchinfo's
		synchronized (tabs) {
			try {
				StatManager.updatePlayer();
				
				// .updatePlayer returns true if the player was successfully updated
				// False if the last update was under a predefined amount of seconds ago(usually 60)
				// Iterate over every session tab currently open
				for (int i = 0; i < tabs.size(); i++) {
					SessionTab tab = tabs.get(i);
		
					// Attempt to generate a match in the playlist
					MatchInfo match = tab.session.generateMatch();
					if (match.getMMRChange() != 0) {
						tab.graphPanel.addMatch(match);
						tab.repaint();
						System.out.println(match);
					} else {
						System.out.println("No change in playlist: " + StatManager.getPlaylistName(match.getPlaylist()) + " MMR: " + StatManager.getMMR(match.getPlaylist()));
					}
					
				}
				
				System.out.println("");
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
	}
	
    private void initComponents() {
    	//graphPanel = new javax.swing.JPanel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        //jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        JMenuItem newMenuItem = new JMenuItem("New session");
        JMenuItem openMenuItem = new JMenuItem("Open session");
        JMenuItem saveMenuItem = new JMenuItem("Save session");
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1200, 630));

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);
        jMenu1.add(newMenuItem);
        jMenu1.add(openMenuItem);
        jMenu1.add(saveMenuItem);
        
        newMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Prompt the user, if there's data already plotted, if they're sure they want to make a new session
				
				// Clear Session(could initialize new ones per SessionTab)
				// Clear GraphPanel
				
				FileIO.sessionID = FileIO.getNewSessionID();
			}
        });
        
        openMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Open the file explorer to search for a session
				// Also have an open recent menu item
				
				// For now, just open one session

				// SessionID will be the first/earliest session in the file
				FileIO.sessionID = -1;
				
				ArrayList<Session> sessions = FileIO.loadSession("session1.txt");
				Session std = sessions.get(0);
				Session doubles = sessions.get(1);
				Session duels = sessions.get(2);
				Session soloStd = sessions.get(3);
				
				// Need to clear these
				tabs.get(0).session = std;
				tabs.get(1).session = doubles;
				tabs.get(2).session = duels;
				tabs.get(3).session = soloStd;
				
				tabs.get(0).graphPanel.clear();
				for(MatchInfo match : std.matches) {
					tabs.get(0).graphPanel.addMatch(match);
				}
				
				tabs.get(1).graphPanel.clear();
				for(MatchInfo match : doubles.matches) {
					tabs.get(1).graphPanel.addMatch(match);
				}
				
				tabs.get(2).graphPanel.clear();
				for(MatchInfo match : duels.matches) {
					tabs.get(2).graphPanel.addMatch(match);
				}
				
				tabs.get(3).graphPanel.clear();
				for(MatchInfo match : soloStd.matches) {
					tabs.get(3).graphPanel.addMatch(match);
				}
				
				repaint();
			}
        });
        
        saveMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Saves the current session to the hard drive
				
				Session std = tabs.get(0).session;
				Session doubles = tabs.get(1).session;
				Session duels = tabs.get(2).session;
				Session soloStd = tabs.get(3).session;
				
				FileIO.saveSession(std, doubles, soloStd, duels);
				
				//System.out.println("Saving Session");
			}
        });
        
        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        jMenu3.setText("Stats");
        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        pack();
    }// </editor-fold>                        

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
    	
    } 
    
    private javax.swing.JButton jButton1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    //private javax.swing.JPanel jPanel3;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JTabbedPane jTabbedPane1;
}
