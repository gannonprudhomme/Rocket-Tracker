package RocketTracker.components;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

import RocketTracker.data.MatchInfo;
import RocketTracker.data.Session;
import RocketTracker.data.StatManager;
import RocketTracker.utils.GraphPanel2;

public class SessionTab extends JPanel {
	Session session;
	
	private JButton jButton1;
	public GraphPanel graphPanel;
	
	int matchCount = 0;
	
	public SessionTab(Session session) {
		super();
		this.session = session;
		
		this.initComponents();
		
		// Only do this if it's a new Session?
		MatchInfo initial = session.generateStartMatch();
		
		graphPanel.addMatch(initial);
		
		// Add intial info
		
		// Shouldn't need this for now
		/*
		jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if(!session.testing) 
                	session.update();
                	
                MatchInfo info = session.generateMatch();
                
                // Check if a match was played
                if(info.getMMRChange() == 0) {
                	System.out.println("No mmr change detected!");
                } else {
                	graphPanel.addMatch(info);
                	
                }
            }
        }); */
	}
	
	private void initComponents() {
		graphPanel = new GraphPanel();
		
		jButton1 = new JButton();
		jButton1.setText("Add Match");
        
        graphPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        graphPanel.setRequestFocusEnabled(false);
        
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(this);
        this.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(graphPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 1011, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(graphPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 529, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        
	}
}
