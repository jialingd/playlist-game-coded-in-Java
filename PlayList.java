import java.io.*;
import java.util.*;

/*
 * Class that maintains a list of songs
 */
public class PlayList {

	// FIELDS

	/** The current number of songs in the array */
	private int numSongs;

	/** The array to contain the songs */
	private Song[] songArray;

	/** The name of the data file that contains the initial song data */
	private String fileName;

	/**
	 * Boolean flag to indicate whether the playlist was modified since it was
	 * either loaded or saved.
	 * Note: You should change this field in your code whenever the playlist is modified
	 * after the first load or save.
	 */
	private boolean modified;


	// CONSTRUCTOR 
	/**
	 * Constructs an empty directory as an array with an initial capacity of 4.
	 */
	public PlayList(String sourceName) {
		numSongs = 0;
		songArray = new Song[4];
		modified=false;//initialized to false 
		loadData(sourceName);
		
	}


	//METHODS
	/**
	 * Initializes the play list using the songs found in the file given by the
	 * sourceName given in the parameter. If the file is not found, the playlist
	 * is initially empty. If the file has an invalid value or is corrupt, then
	 * the playlist is initialized with all valid songs up to, but not
	 * including, the invalid entry in the text file.
	 * @param sourceName The name of the text file containing playlist data.
	 * This method has been written for you
	 */
	private void loadData(String sourceName) {

		this.fileName = sourceName;
		Scanner in;
		try {
			// Create a Scanner for the file
			in = new Scanner(new FileReader(sourceName));
		} catch (FileNotFoundException e) {
			// Do nothing - no data to load
			return;
		}

		boolean error = false;

		// Read each song entry (3 lines) and add the entry to the array
		while (in.hasNextLine() && !error) {
			String title = in.nextLine().toUpperCase();
			if (in.hasNextLine()) {
				String artist = in.nextLine().toUpperCase();
				if (in.hasNextInt()) {
					int numberOfPlays = in.nextInt();
					if (in.hasNextLine()) // in case missing newline at end of file
						in.nextLine(); // clear rest of line
					error = (numSongs >= 1 && numberOfPlays > 
						songArray[numSongs - 1].getNumberOfPlays());
					// invalid number of plays (more plays than previous song),
					// exit loop
					if (!error) {
						// Add an entry for this song
						add(title, artist, numberOfPlays);
					}
				} else {
					error = true; // not an int numberOfPlays
				}
			}
		}

		// Close the file
		in.close();
	}
	
	/**
	 * Returns a single string containing the entire playlist from most played 
	 * to least played.
	 * @return The entire playlist as a single string.
	 */	
	public String getPlayList() {
	    String result = "";
	    
	    for (int i = 0; i < numSongs; i++) {
	        Song song = songArray[i];
	        result += song.getTitle() + " | " + 
	                 song.getArtist() + " | Number of plays: " + 
	                 song.getNumberOfPlays();
	        
	        
	            result += "\n"; //every song is in a single line
	        
	    }
	    
	    return result;
	}

	/**
	 * Adds 1 play to the song (given its title and artist) on the playlist if 
	 * the song is already in the playlist. Otherwise, this method inserts the 
	 * song into the playlist with a number of plays of 1.
	 * @param title The title of the song in uppercase.
	 * @param artist The artist of the song in uppercase.
	 * @return The number of plays for this song after the playlist is updated.
	 */
	
	public int playSong(String title, String artist) {
			// find if the song already in the list
			for (int i = 0; i < numSongs; i++) {
				Song currSong = songArray[i];
				if (currSong.getTitle().equals(title) && currSong.getArtist().equals(artist)) {
					// found, play numbers + 1
					currSong.addOnePlay();
					modified = true;

					int newPlayCount = currSong.getNumberOfPlays();

					// remove the song from current position
					Song temp = songArray[i];
					for (int j = i + 1; j < numSongs; j++) {
						songArray[j - 1] = songArray[j];
					}
					numSongs--;
					songArray[numSongs] = null;//allow garbage collection

					// find the new position to insert
					int insertPos = 0;
					while (insertPos < numSongs &&
							songArray[insertPos].getNumberOfPlays() > newPlayCount) { 
						insertPos++; // stay in the loop if the new play count has is smaller than existing songs
					} //if equal, insert in the beginning

					// move other songs 1 pos right 
					for (int j = numSongs; j > insertPos; j--) {
						songArray[j] = songArray[j - 1];
					}

					// insert the song back
					songArray[insertPos] = temp;
					numSongs++;

					return newPlayCount;
				}
			}

			// Not found, add the new song
			growIfFull();
			Song newSong = new Song(title, artist, 1);

			// find the new insert position (skipping all the songs where number of plays >1)
			int insertPos = 0;
			while (insertPos < numSongs &&
					songArray[insertPos].getNumberOfPlays() > 1) {
				insertPos++;
			}

			
			for (int j = numSongs; j > insertPos; j--) {
				songArray[j] = songArray[j - 1];//repeat the sample moving process because for new song with 1 play, it should be place ahead of old songs with 1 play
			}

			
			songArray[insertPos] = newSong;
			numSongs++;
			modified = true;

			return 1;//only single play because it is a new song
		}
	
	/**
	 * Removes all songs by the specified artist from the playlist, if any.
	 * @param artist The name of the artist to remove, in uppercase.
	 * @return The entire playlist as a single string.
	 */
	public int removeArtist(String artist) {

	    int removedCount = 0;
	    for (int index = numSongs - 1; index >= 0; index--) {
	        if (songArray[index].getArtist().equals(artist)) {
	            // Found a song by this artist, remove it
	            for (int j = index + 1; j < numSongs; j++) {
	                songArray[j - 1] = songArray[j];
	            }
	            numSongs--;
	            removedCount++;
	            modified = true;
	            songArray[numSongs] = null;
	        }
	    }
	    return removedCount;
	}
		
		
		
				


	/**
	 * Saves the current playlist into the file specified when the program began
	 * using the standard format (each song stored as title, artist and number
	 * of plays on separate lines).
	 */
	public void save() {
		if (modified) {
			try {
				// Create PrintWriter for the file
				PrintWriter out = new PrintWriter(new FileWriter(fileName));

				// Write each song to the file
				for (int i = 0; i < numSongs; i++) {
					out.println(songArray[i].getTitle());
					out.println(songArray[i].getArtist());
					out.println(songArray[i].getNumberOfPlays());
				}

				// Close the file and reset modified
				out.close();
				modified = false;
			} catch (Exception e) {
				System.err.println("Save of playlist failed");
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

	/**
	 * Inserts new song into songArray in the next available position, doubling
	 * the size of the array if necessary (if the array is full)
	 */

public void add(String title, String artist, int numberOfPlays) {
    growIfFull();//dynamical growing
    Song s = new Song(title, artist, numberOfPlays);
    songArray[numSongs] = s;  
    numSongs++;
    
}


		
		
	
	
	private void growIfFull() {
	    if (numSongs == songArray.length) {
	        Song[] newArray = new Song[songArray.length * 2]; 
	        
	        // iterate over the songs and copy them over to the new array
	        for (int i = 0; i < numSongs; i++) {
	            newArray[i] = songArray[i];//copy 
	        }

	        songArray = newArray;
	    }
	}


/////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////
///////////////////// FOR THE AUTOGRADER - DO NOT MODIFY
/////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////

	/**
	 * Returns songArray for testing
	 */
	public Song[] getSongArray() {
		return songArray;
	}

	/**
	 * Returns modified for testing
	 */
	public boolean getModified() {
		return modified;
	}

	/**
	 * Returns numSongs for testing
	 */
	public int getNumSongs() {
		return numSongs;
	}
}
