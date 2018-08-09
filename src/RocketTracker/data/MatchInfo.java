package RocketTracker.data;

import java.util.Random;

import com.ssplugins.rlstats.entities.Playlist;

// A container file for a match
public class MatchInfo {
	boolean win; // Whether this match was a win or a loss
	int mmrChange; // should this be mmrDelta? The sign determines if it was a win or a loss
	
	int playlist; // Which playlist this match was in
	
	int currentStreak; // How many wins (if negative, then how many losses)
	
	int goals;
	int assists;
	int saves;
	int shots;
	boolean mvp;
	
	public boolean startMatch = false; // If this is the first point of the session, not an actual match
	
	public int currentMMR;
	
	/*
	public MatchInfo(boolean testing) {
		Random random = new Random();
		
		this.playlist = Playlist.DOUBLES;
		this.playerMMR = mmrNext;
		this.mmrChange = (random.nextInt(1) == 1 ? -1 : 1) * (random.nextInt(11));
		this.currentStreak = -2;
		
		this.goals = 4;
		this.assists = 2;
		this.saves = 1;
		this.mvp = true;
		
		mmrNext += mmrChange;
	} */
	
	public MatchInfo(int playlist, int playerMMR, int mmrChange, int currentStreak) {
		this.playlist = playlist;
		this.currentMMR = playerMMR;
		this.mmrChange = mmrChange;
		this.currentStreak = currentStreak;
	}
	
	public String toStringHTML() {
		String ret = "<html>";
		
		// If this is the first match of the session, it's not a "real" match
		if(startMatch) {
			ret += "Starting MMR: " + currentMMR + "<br>";
			
		} else {
			ret += "MMR Change: " + (mmrChange > 0 ? "+" : "") + this.mmrChange + "<br>";
			ret += "Current MMR: " + currentMMR + "<br>";
			ret += "Goals: " + goals +"<br>";
			ret += "Assists: " + assists + "<br>";
			ret += "Saves: " + saves + "<br>";
			ret += "Shots: " + shots + "<br>";
			ret += (mvp ? "MVP!" : "");
		}
		
		return ret + "</html>";
	}
	
	@Override
	public String toString() {
		String ret = "";
		
		if(startMatch) {
			ret += "Starting MMR: " + currentMMR + "\n";
			
		} else {
			ret += "Playlist: " + StatManager.getPlaylistName(playlist) + "\n";
			ret += "MMR Change" + (mmrChange > 0 ? "+" : "") + this.mmrChange + "\n";
			ret += "Goals: " + goals +"\n";
			ret += "Assists: " + assists + "\n";
			ret += "Saves: " + saves + "\n";
			ret += "Shots: " + shots + "\n";
			ret += (mvp ? "MVP!\n" : "");
		}
		
		return ret;
	}

	// Getters and setters
	
	public boolean isWin() {
		// If the player had a positive mmrChange then they must have won
		return mmrChange > 0;
	}

	public int getCurrentMMR() {
		return currentMMR;
	}

	public int getMMRChange() {
		return mmrChange;
	}


	public int getPlaylist() {
		return playlist;
	}


	public int getCurrentStreak() {
		return currentStreak;
	}


	public int getGoals() {
		return goals;
	}


	public int getAssists() {
		return assists;
	}


	public int getSaves() {
		return saves;
	}
	
	public int getShots() {
		return shots;
	}
	

	public void setMMRChange(int mmrChange) {
		this.mmrChange = mmrChange;
	}


	public void setPlaylist(int playlist) {
		this.playlist = playlist;
	}


	public void setCurrentStreak(int currentStreak) {
		this.currentStreak = currentStreak;
	}


	public void setGoals(int goals) {
		this.goals = goals;
	}


	public void setAssists(int assists) {
		this.assists = assists;
	}


	public void setSaves(int saves) {
		this.saves = saves;
	}
	
	public void setMVP(boolean mvp) {
		this.mvp = mvp;
	}
	
	public void setShots(int shots) {
		this.shots = shots;
	}
}
