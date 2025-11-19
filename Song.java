/*
 * Class representing an individual Song object
 * Look at the documentation for more details.
 */
public class Song {

	// FIELDS
	private String title;
	private String artist;
	private int numberOfPlays;

	// CONSTRUCTOR
	/*
	 * Creates a new Song with the specified title, artist and number of plays. 
	 * (Sets the number of plays to 1 instead if the value supplied in the parameter is less than 1.)
	 */
	public Song(String songTitle, String songArtist, int numberOfPlays) {
		this.title = songTitle;
		this.artist = songArtist;
		
		// Set plays to 1 if parameter is less than 1
		if (numberOfPlays < 1) {
			this.numberOfPlays = 1;
		} else {
			this.numberOfPlays = numberOfPlays;
		}
	}
	

	// METHODS
	
	/*
	 * Adds 1 to the number of plays for this song.
	 */
	public void addOnePlay() {
		this.numberOfPlays++;
	}

	/*
	 * Retrieves the artist of this song.
	 * @return the artist as a String
	 */
	public String getArtist() {
		return this.artist;
	}
	

	/*
	 *  Retrieves the number of plays for this song.
	 * @return the number of plays as an int
	 */
	public int getNumberOfPlays() {
		return this.numberOfPlays;
	}


	/*
	 *  Retrieves the title of this song.
	 * @return the title as a String
	 */
	public String getTitle() {
		return this.title;
	}



	
    public String toString() {
        return title + " | " + artist + " | Number of plays: " + numberOfPlays;
     }



	public boolean equals(Song other) {
    if (other == null) return false;
    
    return title.equals(other.title) && artist.equals(other.artist);
}
}
