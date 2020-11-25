package wordGame;

import java.util.Arrays;

/** class that acts as an instance of a rack for a game
 * @author RAJA MUSTAFA KHALID
 * @author MOHAMMED KHAN
 */
public class Rack {
	private String[] rack;
	
	
	public Rack() {
		rack = new String[5];
		rack[0] = "M";
		rack[1] = "U";
		rack[2] = "I";
		rack[3] = "R";
		rack[4] = "W";
		
	}
	
	/** gets a character from the rack
	 * @param index
	 * @return String
	 */
	public String getCharacter(int index) {
		return rack[index];
		
	}
	
	/** adds a character to a given index
	 * @param givenChar
	 * @param givenIndex
	 */
	public void AddCharacter(String givenChar, int givenIndex) {
		rack[givenIndex]=givenChar;
		
	}
	/**  gets the current rack
	 * @return String[] array of the rack
	 */
	public String[] getRack() {
		return rack;
	}
	
	public String toString() {
		
		return Arrays.toString(rack);
	}
	/** gets the length of the current rack
	 * @return int value of current rack length
	 */
	public int getRackLength() {
		
		return rack.length;
	}
	
	
	

}
