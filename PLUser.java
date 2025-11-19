import java.util.*;

/**
 *  This class is an implementation of a user interface for a Playlist
 *  that uses the console to display the menu of command choices.
 */


public class PLUser {
	
	private PlayList songlist;
	private Scanner scan;
	
	public PLUser(PlayList pl) {
		songlist = pl;
		scan = new Scanner(System.in);
	}
	
	public void processCommands() {
		 String[] commands = {"Play Song",
	 			  "Remove Artist",
	 			  "Display Play List",
	 			  "Save Play List",
	 			  "Exit"};  // must be last option

		 int choice;
		 
		 do {
			 for (int i = 0; i < commands.length; i++) {
				 System.out.println("Select " + i + ": " + commands[i]);
			 }
			 try {
				 choice = scan.nextInt();
				 scan.nextLine();
				 
				 if (choice == 0) { 
					 doPlaySong(); 
				 }
				 else if (choice == 1)  {
					 doRemoveArtist();
				 }
				 else if (choice == 2)	{
					 doDisplayPlayList();
				 }
				 else if (choice == 3) {
					 doSave();
				 }
				 else if (choice == 4) { // Exit
					 doSave(); 
					 System.out.println("Bye");
				 } else {
				 	System.out.println("INVALID CHOICE - Enter number 0-4");
				 }
			 } catch (InputMismatchException e) {
				 System.out.println("INVALID CHOICE - Enter number 0-4");
				 scan.nextLine();
				 choice = -1;
			 }	 
		 } while (choice != commands.length-1); // Chose Exit
		 System.exit(0);
	}

	private void doPlaySong() {

		// Request the title
		System.out.println("Enter title");
		String title = scan.nextLine();
		if (title.equals("")) {
			return;		// input was cancelled
		}
		title = title.toUpperCase();
		
		// Request the artist
		System.out.println("Enter artist");
		String artist = scan.nextLine();
		if (artist.equals("")) {
			return;		// input was cancelled
		}
		artist = artist.toUpperCase();
		
		// Insert or change the number
		int numberOfPlays = songlist.playSong(title, artist);
		
		// Prepare confirmation message for console
		String message = null;
		if (numberOfPlays == 1) {
			message = title + " / " + artist + " was added to the playlist"
						+ "\nNumber of Plays: 1";
		} else {
			message = title + " / " + artist
						+ "\nNumber of Plays: " + numberOfPlays;
		}

		// Display confirmation message
		System.out.println(message);
		
	}
	
	private void doRemoveArtist() {

		// Request the artist
		System.out.println("Enter artist");
		String artist = scan.nextLine();
		if (artist.equals("")) {
			return;		// dialog was cancelled
		}
		artist = artist.toUpperCase();
		
		// Remove all songs by the given artist, returns number of songs removed
		int numberOfSongs = songlist.removeArtist(artist);
		
		// Prepare confirmation message for dialog box
		String message = numberOfSongs + " song(s) have been removed.";

		// Display confirmation message
		System.out.println(message);
			
	}

	private void doDisplayPlayList() {

		String message = songlist.getPlayList();
		System.out.println(message);		

	}
	
	private void doSave() {
		songlist.save();
	}	
	
	public static void main(String[] args) {
		PlayList pl = new PlayList("songdata.txt");
		PLUser user = new PLUser(pl);
		user.processCommands();
	}	
}
