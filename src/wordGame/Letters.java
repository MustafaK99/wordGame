package wordGame;
import java.util.HashMap;

/**simple class with HashMap containing all the letters within the game and their points values
 * @author AADIL MOHAMMED
 *
 */
public class Letters {
	private HashMap<String,Integer> letters;
	
	/** simple constructor for letters
	 *  
	 */
	public Letters() {
		this.letters = new HashMap<String,Integer>();
		letters.put("A", 1);
		letters.put("B", 2);
		letters.put("C", 1);
		letters.put("D",1);
		letters.put("E", 1);
		letters.put("F", 1);
		letters.put("G", 2);
		letters.put("H",1);
		letters.put("I", 1);
		letters.put("J", 2);
		letters.put("K", 2);
		letters.put("L", 1);
		letters.put("M", 2);
		letters.put("N", 2);
		letters.put("O", 1);
		letters.put("P", 1);
		letters.put("Q", 3);
		letters.put("R", 1);
		letters.put("S", 1);
		letters.put("T", 1);
		letters.put("U", 1);
		letters.put("V", 1);
		letters.put("W", 1);
		letters.put("X", 3);
		letters.put("Y", 3);
		letters.put("Z", 3);
	}
	
	/** returns interger value of points for that letter
	 * @param givenLetter
	 * @return int value of points of a given letter
	 */
	public int getLetterValue(String givenLetter) {
		return letters.get(givenLetter);
	}
	

}
