package wordGame;

import java.io.FileNotFoundException;

/**
 * @author RAJA MUSTAFA KHALID
 * @author MUBASHIR AHMAD
 */
public class Main {
	

	/** main method that launches the game
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {
		GameController controller = new GameController();
		@SuppressWarnings("unused")
		TUI tui = new TUI(controller);
		
		
	}

}
