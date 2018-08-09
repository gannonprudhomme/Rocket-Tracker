package RocketTracker.data;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.ssplugins.rlstats.RLStats;
import com.ssplugins.rlstats.RLStatsAPI;
import com.ssplugins.rlstats.entities.Platform;
import com.ssplugins.rlstats.entities.Player;
import com.ssplugins.rlstats.entities.Playlist;
import com.ssplugins.rlstats.entities.PlaylistInfo;
import com.ssplugins.rlstats.entities.Stat;

/*
 * Doesn't really need to be a container like originally intended
 * Should do the analysis of matches, like goals per game and such
 * Would take in a set of match data and create points with data attached to it
 * Also gets the stats to begin with
 * 
 * Everytime updatePlayer() is called, there'll be a chance that the program pauses
 * This should not happen frequently, 
 */
public class StatManager {
	static String key = "12AZJ7HWAWRS5A33GYOBFCLKDKJFMELK";
	static String steamID = "Lodence";
	
	static Player player; // The data of the current player that was retrieved at the timeOfLastUpdate 
	//static long timeOfLastUpdate; // The time since the player was last updated, in seconds
	
	//private static int updateDelay = 30; // Min time in seconds that must be elapsed between updates

	// Returns true if the update went through, false if an update was recently.
	public static void updatePlayer() throws InterruptedException, ExecutionException {
		// If it's been less than a minute since the last update, don't update
		RLStatsAPI api = RLStats.getAPI(key);
		
		Future<Player> future = api.getPlayer(steamID, Platform.STEAM);
		
		// .. Do something to wait until API request is finished.
		while(future == null && future.get() == null) {
			System.out.println("Waiting for RLStats API");
		}
		
		player = future.get();
		
		// Wait til the player is loaded, it might be immediate
		while(player == null) {
			System.out.println("Waiting for player");
		}
		
		// What's the point of waiting to set this anyways?
		
		// Then set the time of last update to the current time
		
		System.out.println("Updated: " + steamID);
	}
	
	public static int getMMR(int playlist) {
		PlaylistInfo info = player.getSeasonInfo(7).getPlaylistInfo(playlist);
		
		// Wait until the value is corr	ect
		while(info == null) {
			System.out.println("Waiting for playlist");
		}
		
		return info.getRankPoints();
	}
	
	// Should return instantly, as the player it's loading from should be the expected player
	public static int getPlayerStat(Stat stat) {
		return player.getStats().getStat(stat);
	}
	
	public static String getPlaylistName(int playlist) {
		String ret = "";
		
		switch(playlist) {
		case Playlist.RANKED_STANDARD:
			ret = "Standard";
			break;
		case Playlist.RANKED_DOUBLES:
			ret = "Doubles";
			break;
		case Playlist.RANKED_SOLO_STANDARD:
			ret = "Solo Standard";
			break;
		case Playlist.RANKED_DUEL:
			ret = "Duel";
			break;
		default:
			ret = "unknown";
			break;
		}
		
		return ret;
	}
	
	public static int getFakeMMRChange() {
		Random random = new Random();
		int change = random.nextInt(5) + 7; // [5, 12) -> [5, 11]
		int sign = random.nextInt(2); // [0, 2) -> [0, 1]
		
		if(sign == 0) {
			change -= (change * 2); // make change negative
		}
		
		return change;
	}
	
	// For testing
	public static int getFakePlayerStat() {
		Random random = new Random();
		return random.nextInt(5);
	}
}
