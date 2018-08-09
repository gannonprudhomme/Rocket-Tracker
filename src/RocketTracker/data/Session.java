package RocketTracker.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import com.ssplugins.rlstats.entities.Stat;

/**
 * Basically one ("grind") session of RL, although could be any length of time
 * Starts and stops depending on what the user determines
 *
 * Should probably be renamed to PlaylistSession
 */
public class Session {
	public Date startDate;
	public Date stopDate;
	
	public int playlist;
	
	// Stores the data retrieved every game
	// Currently has no use? Basically just for the future
	// Likewise, we're double storing matchinfo, as GraphPanel has as list as well
	public List<MatchInfo> matches;

	// mmr is per Playlist, all other stats are global
	private int lastMMR;
	private static int lastGoals;
	private static int lastAssists;
	private static int lastMVPs;
	private static int lastSaves;
	private static int lastShots;
	
	public boolean testing = false;
	private boolean running = true;
	
	public Session(int playlist) {
		this.playlist = playlist;
		
		// Start the session by getting the current date/time
		startDate = new Date();
		matches = new ArrayList<MatchInfo>();
	}
	
	// Get all of the stats and compare them to last game
	// Creation of a "Match" is under the assumption that this
	// was called in the time only a single match could be played
	// If there was no change in mmr (&& stats), then no match will be added
	public MatchInfo generateMatch() {
		MatchInfo info = new MatchInfo(playlist, 0, 0, 0);
		//MatchInfo info = new MatchInfo(true);
		
		if(!testing) {
			//testing = true; // Get initial data (for testing)
			
			// Check if there was an mmr change
			int newPlayerMMR = StatManager.getMMR(playlist);
			if(newPlayerMMR - lastMMR != 0) { // Can be negative or positive, 
				if(newPlayerMMR - lastMMR > 20)
					System.out.println("idk what's wrong with the mmr");
				
				int goals = StatManager.getPlayerStat(Stat.GOALS);
				int assists = StatManager.getPlayerStat(Stat.ASSISTS);
				int saves = StatManager.getPlayerStat(Stat.SAVES);
				int mvps = StatManager.getPlayerStat(Stat.MVP);
				int shots = StatManager.getPlayerStat(Stat.SHOTS);
				
				info.currentMMR = newPlayerMMR;

				info.setGoals(goals - lastGoals);
				info.setAssists(assists - lastAssists);
				info.setSaves(saves - lastSaves);
				info.setMVP((lastMVPs - mvps) == 1);
				info.setShots(shots - lastShots);
				info.setMMRChange(newPlayerMMR - lastMMR);
				
				matches.add(info);
				
				lastMMR = newPlayerMMR;
				lastGoals = goals;
				lastAssists = assists;
				lastSaves = saves;
				lastMVPs = mvps;
				lastShots = shots;
			} else {
				// Notify that there's no mmr change(debugging)
				// System.out.println("No mmr change detected in playlist: " + StatManager.getPlaylistName(playlist));
			}
			
		} else {
			int rand = new Random().nextInt(5);
			
			// When testing, only want to add a fake match rarely
			if(rand <= 1) {
				int mmrChange = StatManager.getFakeMMRChange();

				lastMMR += mmrChange;
				info.currentMMR = lastMMR;
				
				info.setMMRChange(mmrChange);
				info.setGoals(StatManager.getFakePlayerStat());
				info.setAssists(StatManager.getFakePlayerStat());
				info.setSaves(StatManager.getFakePlayerStat());
				info.setMVP(false);
				info.setShots(StatManager.getFakePlayerStat());
				
				matches.add(info);
				//System.out.println(rand);
			} else {
				
			}
		}
		
		return info;
	}
	
	// Called by the SessionTab (parent) upon construction, the starting point of the graph
	public MatchInfo generateStartMatch() {
		MatchInfo info = new MatchInfo(playlist, 0, 0, 0);
		info.startMatch = true;
		
		info.currentMMR = StatManager.getMMR(playlist);
		lastMMR = info.currentMMR;
		
		// Set all the starting values
		lastMMR = StatManager.getMMR(playlist);
		lastGoals = StatManager.getPlayerStat(Stat.GOALS);
		lastAssists = StatManager.getPlayerStat(Stat.ASSISTS);
		lastSaves = StatManager.getPlayerStat(Stat.SAVES);
		lastMVPs = StatManager.getPlayerStat(Stat.MVP);
		lastShots = StatManager.getPlayerStat(Stat.SHOTS);
		
		// "Nullify" all the other stats for this match, to show it's not real match
		info.setGoals(-1);
		info.setAssists(-1);
		info.setSaves(-1);
		info.setMVP(false);
		info.setShots(-1);
		
		matches.add(info);
		
		return info;
	}
	
	public void addMatch(MatchInfo info) {
		matches.add(info);
		
		lastMMR = info.currentMMR;
	}
	
	// Polls the servers
	// Returns true if the update went through
	/*public boolean update() {
		try {
			return StatManager.updatePlayer();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			return false;
		}
	} */
	
	public void endSession() {
		stopDate = new Date();
	}
}
