package RocketTracker.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.ssplugins.rlstats.entities.Playlist;

import RocketTracker.data.MatchInfo;
import RocketTracker.data.Session;

/**
 *  Helper functions for saving/loading Sessions to the file system
 *
 */
public class FileIO {
	// Where Sessions are stored
	public static String sessionDir = "";
	
	public static int sessionID = -1;
	
	// Returns an array list in the for (standard, doubles, solo standard, duels)
	public static ArrayList<Session> loadSession(String filename) {
		// There should always be at least one 
		Session standard = new Session(Playlist.RANKED_STANDARD);
		Session doubles = new Session(Playlist.RANKED_DOUBLES);
		Session soloStd = new Session(Playlist.RANKED_SOLO_STANDARD);
		Session duels = new Session(Playlist.RANKED_DUEL);
		//Session unranked;
		
		try {
			File file = new File(filename);
			
			Scanner scanner = new Scanner(file);
			
			long time = scanner.nextLong();
			int sessionID = scanner.nextInt();
			
			while(scanner.hasNextInt()) {
				//long timeUpdated = scanner.nextLong();
				
				int playlist = scanner.nextInt();
				int currentMMR = scanner.nextInt();
				int mmrChange = scanner.nextInt();
				
				int goals = scanner.nextInt();
				int assists = scanner.nextInt();
				int saves = scanner.nextInt();
				int shots = scanner.nextInt();
				
				scanner.nextLine(); // End the line
				
				MatchInfo info = new MatchInfo(playlist, currentMMR, mmrChange, 0);
				info.setGoals(goals);
				info.setAssists(assists);
				info.setSaves(saves);
				info.setShots(shots);
				
				if(goals == -1)
					info.startMatch = true;
				
				System.out.println(info);
				
				switch(playlist) {
				case Playlist.RANKED_DUEL: // Ranked_Duel
					duels.addMatch(info);
					break;
				case Playlist.RANKED_DOUBLES: // Ranked_Doubles
					doubles.addMatch(info);
					break;
				case Playlist.RANKED_SOLO_STANDARD: // Ranked_Solo_Standard
					soloStd.addMatch(info);	
					break; // Ranked_Standard
				case Playlist.RANKED_STANDARD:
					standard.addMatch(info);
					break;
				default:
					System.out.println("Unknown playlist when loading session " + playlist);
					break;
				}
			}
			
			ArrayList<Session> list = new ArrayList<Session>();
			list.add(standard); list.add(doubles); list.add(duels); list.add(soloStd); 
			
			return list;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
			return null;
		}
	}
	
	public static void saveSession(Session standard, Session doubles, Session soloStd, Session duels) {
		String filename = "session1.txt";
		
		String data = "";
		
		//data += System.currentTimeMillis() + " ";
		//data += 0 + "\n"; // Session ID
		
		List<String> lines = new ArrayList<String>();
		lines.add(System.currentTimeMillis() + " " + 0);
		
		// Matches should be sorted by time
		for(MatchInfo info : standard.matches) {
			data += info.getPlaylist() + " ";
			data += info.getCurrentMMR() + " ";
			data += info.getMMRChange() + " ";
			data += info.getGoals() + " ";
			data += info.getAssists() + " ";
			data += info.getSaves() + " ";
			data += info.getShots() + "\n";
			
			lines.add(data);
			data = "";
		}
		
		// Matches should be sorted by time
		for(MatchInfo info : doubles.matches) {
			data += info.getPlaylist() + " ";
			data += info.getCurrentMMR() + " ";
			data += info.getMMRChange() + " ";
			data += info.getGoals() + " ";
			data += info.getAssists() + " ";
			data += info.getSaves() + " ";
			data += info.getShots() + "\n";
			
			lines.add(data);
			data = "";
		}
		
		// Matches should be sorted by time
		for(MatchInfo info : soloStd.matches) {
			data += info.getPlaylist() + " ";
			data += info.getCurrentMMR() + " ";
			data += info.getMMRChange() + " ";
			data += info.getGoals() + " ";
			data += info.getAssists() + " ";
			data += info.getSaves() + " ";
			data += info.getShots() + "\n";
			
			lines.add(data);
			data = "";
		}
		
		// Matches should be sorted by time
		for(MatchInfo info : duels.matches) {
			data += info.getPlaylist() + " ";
			data += info.getCurrentMMR() + " ";
			data += info.getMMRChange() + " ";
			data += info.getGoals() + " ";
			data += info.getAssists() + " ";
			data += info.getSaves() + " ";
			data += info.getShots() + "\n";
			
			lines.add(data);
			data = "";
		}
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
			
			for(String line : lines) {
				bw.write(line);
				bw.newLine();
			}
			
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static int getNewSessionID() {
		// Go through all of the sessions (text files) in the directory
		
		int maxID = -1;
		
		File folder = new File("C:/Users/Gannon/eclipse-workspace/RocketTracker");
		File[] listOfFiles = folder.listFiles();
		
		for(File file : listOfFiles) {
			if(file.isFile()) {
				String filename = file.getName();
				//System.out.print(filename);
				
				String comp = filename.substring(0, 7);
				
				if(comp.equals("session")) {
					int id = Integer.parseInt(filename.substring(7, 8));
					
					if(id > maxID)
						maxID = id;
					
					//System.out.println(" ID: " + maxID + "\n");
				}
			}
		}
		
		return maxID + 1;
	}
}
