package wordGame;



/** a board class that stores instance of the board
 * @author RAJA MUSTAFA KHALID
 *
 */
public class Board {

	
	private String[][] board;
   

	public Board() {
		board = new String[][] {
			{" "," "," "," "," "," "," "," "," "," "},
			{" "," "," "," ","+","+"," "," "," "," "},
			{" "," "," "," "," "," "," "," "," "," "},
			{" "," "," ","+"," "," ","+"," "," "," "},
			{" "," "," "," "," "," "," "," "," "," "},
			{" "," "," "," "," "," "," "," "," "," "},
			{" "," "," ","+"," "," ","+"," "," "," "},
			{" "," "," "," "," "," "," "," "," "," "},
			{" "," "," "," ","+","+"," "," "," "," "},
			{" "," "," "," "," "," "," "," "," "," "}
		};

	}

	/** gets the board and returns it
	 * @return 2D string array
	 */
	public String[][] getBoard(){
		String[][] boardCopy = new String[board.length][];
		for (int i = 0; i < board.length; i++)
			//this only clones the board so the actual board is always protected unless option 3 is called
		    boardCopy[i] = board[i].clone();
		
		return boardCopy;
	}
	

	/** sets a tile on the board
	 * @param x
	 * @param y
	 * @param givenCharacter
	 */
	public void setTileOnBoard(int x, int y, String givenCharacter) {
		board[x][y] = givenCharacter;
	}
	/** gets a tile from the board
	 * @param x
	 * @param y
	 * @return
	 */
	public String getTile(int x, int y) {
		String wantedCharacter = board[x][y];
		return wantedCharacter;
	}
	
	
	

	public String toString() {
		StringBuilder boardString = new StringBuilder();
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				boardString.append(board[i][j] + ".");
			}
			boardString.append("\n");
		}
		return boardString.toString();
	}
	


}
