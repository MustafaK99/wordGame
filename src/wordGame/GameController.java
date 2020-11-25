package wordGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.HashSet;
import java.util.Scanner;
/**
 * implementation of controller interface
 * 
 * @author RAJA KHALID
 * @author MOHAHMMED KHAN
 * @author MUBASHIR AHMAD
 * @author AADIL MOHAMMED
 */


public class GameController implements Controller {
     //Creates all the necessary variables to start the game
	private Rack rack;
	private Board board;
	private Letters letters;
	private HashSet<String> wordList;
	private HashSet<String> alphabet; //HashSet to check for single letters on the board 
	private Scanner wordListFile;
	private ArrayList<String> correctWords; //all the correct words within a given play
	private ArrayList<String> incorrectWords; //all incorrect words within a given play
	
	/**
	 * Constructs the interface controller with all required objects and fields
	 */

	public GameController() throws FileNotFoundException {
		  this.correctWords = new ArrayList<String>();
		  this.incorrectWords = new ArrayList<String>();
		
	
		this.board = new Board();
		this.rack = new Rack();
		letters = new Letters();
		wordList = new HashSet<String>(104854);
		wordListFile = new Scanner(new File("Assets/wordlist.txt"));
		while(wordListFile.hasNext()) {
			wordList.add(wordListFile.nextLine());
		}
		wordListFile.close();
		alphabet = new HashSet<String>(26);
		alphabet.add("A");
		alphabet.add("B");
		alphabet.add("C");
		alphabet.add("D");
		alphabet.add("E");
		alphabet.add("F");
		alphabet.add("G");
		alphabet.add("H");
		alphabet.add("I");
		alphabet.add("J");
		alphabet.add("K");
		alphabet.add("L");
		alphabet.add("M");
		alphabet.add("O");
		alphabet.add("P");
		alphabet.add("Q");
		alphabet.add("R");
		alphabet.add("S");
		alphabet.add("T");
		alphabet.add("U");
		alphabet.add("V");
		alphabet.add("W");
		alphabet.add("X");
		alphabet.add("Y");
		alphabet.add("Z");
	}

	@Override
	public String refillRack() {
		//adds random set of letters to the rack when refillRack is called from the array
	 String[] alphabet = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	 Random r=new Random();
	  for(int i=0; i < 5; i++) {
		  int randomNumber=r.nextInt(alphabet.length);
		  int randomIndex = r.nextInt(4);
		  rack.AddCharacter(alphabet[randomNumber],randomIndex);
	  }
	  return rack.toString();
	}

	
	@Override
	public String gameState() {
		
		StringBuilder GameString = new StringBuilder();
		GameString.append("Board:\n" + board.toString() + "\n");
		GameString.append("Tiles: " + "\n");
	    GameString.append(rack.toString());
		return GameString.toString();
	}

	@Override
	public String play(Play play) {
		
	    //gets Coordinates from play and assigns them to variables
		int[] givenCoordinates = calculatingCoordinates(play);
		int row = givenCoordinates[0];
		int column = givenCoordinates[1];
		
		//gets the position of values within the rack to be placed
		int[] rackPositions = getRackPositions(play);
		
		// gets all the rack values and adds them to an ArrayList ready for placement 
        ArrayList<String> currentCharacters = new  ArrayList<String>();
        for(int i =0; i < rackPositions.length; i++) {
        	 currentCharacters.add(rack.getCharacter(rackPositions[i]))  ;	   
        }
        //switch statement that checks the Enum direction of a play then places it
		switch(play.dir()){
			case ACROSS:
				for(int i=0; i< currentCharacters.size();i++) {
					
					if( row<0 || row>9 || column < 0 || column > 9 ){
						return "Your sequence is invalid because you are trying to place letters outside the board";
					}
					else if(!board.getTile(row, column).equals(" ") && !board.getTile(row, column).equals("+")) {
						return "Your sequence is invalid because some cells are occupied";
					}
					else {
						board.setTileOnBoard(row,column,currentCharacters.get(i));
						refillRack();
						column++;
						}	 
				}
				break;
			case DOWN:
				for(int i=0; i < currentCharacters.size();i++) {	
					if( row<0 || row>9 || column < 0 || column > 9 ){	
						return "Your sequence is invalid because you are trying to place letters outside the board";
					}
					else if(!board.getTile(row, column).equals(" ") && !board.getTile(row, column).equals("+")) {
						return "Your sequence is invalid because some cells are occupied";
					}
					else {
						board.setTileOnBoard(row,column,currentCharacters.get(i));
						refillRack();
						row++;
					}
				}
				
		       break;
		}
		return board.toString();
	}

	
	@Override
	public String calculateScore(Play play) {
		 
		int[] givenCoordinates = calculatingCoordinates(play);
		int row = givenCoordinates[0];
		int column = givenCoordinates[1];
		// stores the total score for the given play
		int total = 0;
		StringBuilder Score = new StringBuilder();
		
		int[] rackPositions = getRackPositions(play);
		
		//gets all characters within given play
        ArrayList<String> currentCharacters = new  ArrayList<String>();
        for(int i =0; i < rackPositions.length; i++) {
        	 currentCharacters.add(rack.getCharacter(rackPositions[i]))  ;	
        }
        //calculates score for each character multiplying by two if it's a +
        switch(play.dir()){
		case ACROSS:
			for(int i=0; i< currentCharacters.size();i++) {
				if(board.getTile(row, column).equals("+")){
					//gets the value of a letter from the letter class hashmap which contains them all
					total += letters.getLetterValue(currentCharacters.get(i))*2;
				}
				else {
					total += letters.getLetterValue(currentCharacters.get(i));
				}
			
				column++;
			}
			break;
		case DOWN:
			for(int i=0; i < currentCharacters.size();i++) {
				if(board.getTile(row, column).equals("+")){
					total += letters.getLetterValue(currentCharacters.get(i))*2;
				}
				else {
					total += letters.getLetterValue(currentCharacters.get(i));
				}	
				row++;	
			}
		     break;
        }
        // returns the score for the character
		Score.append("the possible score for this play is: "+ total);
		return Score.toString();
        
	}

	@Override
	public String checkValidity(Play play) {
		
		StringBuilder words = new StringBuilder();
	   switch(play.dir()){
		case DOWN: 
			//checks the adjacency for down direction plays
			if(checkAdjDown(play).equals("Valid")){
				//checks that the words are correct for down direction plays
				if(checkWordsDown(play).equals("Valid")) {
					for(int i=0; i < correctWords.size();i++) {
						words.append("This is a valid sequence within the play: " + correctWords.get(i));
						words.append("\n");

					}	
				}
				else {
					for(int i=0; i < incorrectWords.size();i++) {
						words.append("This is a invalid sequence within the play: " + incorrectWords.get(i));
						words.append("\n");	
					}	
				}	
			}
			else {
				words.append("The adjacency for this play is invalid");
			}
			break;
		case ACROSS:
			if(checkAdjAcross(play).equals("Valid")) {
				//checks the adjacency for across direction plays
				words.append("This a valid sequence");
			}
			else {
				words.append("The adjacency for this play is invalid");
			}
		    break;
	  }
	
	   return    words.toString(); 
	}
	
  /**Method that calculates the first letter coordinates of a given play value
 * @param play
 * @return an array with the calculate coordinates 
 */
public int[] calculatingCoordinates( Play play) {
	  
	  int col = 0;
	  int row = 0;
	  int[] coordinates = new int[2];
	  String[] startingCell = play.cell().split("");

	  if(startingCell.length==2) {
		   Character letter = startingCell[0].charAt(0);
		   Character number = startingCell[1].charAt(0);
		   col = letter - 65  ; //converts the column value to a number
		   row = number - '0';  // converts the row value to a number
		   row = row - 1;  // minus one from row value to get index for array operations
	  }
	  //else if statements check for value 10th row cells e.g. A10 because it has 3 values
	  else if(startingCell.length==3) {
		   Character letter = startingCell[0].charAt(0);
		   col = letter - 65  ;
		   row = 9;
	  }
	  
	  coordinates[0] = row;
	  coordinates[1] = col;
	  
	  
      return coordinates;
  }	
  
 
 
/** gets the actual position of the letters in a rack for a given play
 * @param play
 * @return int array containing all the index values
 */
public int[] getRackPositions(Play play) {
	    // gets the rack index inputed by the player
	     String[] rackPositions =  play.letterPositionsInRack().split("");
	     int[] rackIndexValues = new int[rackPositions.length];
	     for(int i=0; i < rackPositions.length; i++) {
	    	int currentIndex =  Integer.parseInt(rackPositions[i])   ;  
	    	currentIndex -- ;
	    	rackIndexValues[i]=currentIndex;
	     }
	   return rackIndexValues;
  }
  
  
  /** checks all down plays do not add any invalid words
 * @param play
 * @return  returns a string with either valid or invalid
 */
public String checkWordsDown(Play play) {
	    // this is the current word on a given board
	    String currentString ="";
	    // this list contains all the current sequences which a play has induced
	    ArrayList<String> allCurrentSequences = new ArrayList<String>();
	    if(checkBoardValidity(play, board.getBoard())==null){
	    	return "Please try again";
	    }
	    else {
	  	String[][] currentBoard = checkBoardValidity(play, board.getBoard());
	     int[]startingCoords = calculatingCoordinates(play);
	     int length = getRackPositions(play).length;
	     // checks what the current column is
	     if(startingCoords[1]==0) {
	    	 if(startingCoords[0]==0) {
	    		 int rowAcross = startingCoords[0];
	    	     int columnAcross = startingCoords[1];
	    		 for(int i=0; i < length;i++) {
	    			 boolean stop = false;
	    	         while(stop == false) {
	    	        	 // checks if the column value is greater than 9 to avoid out of exception error
	    	        	 if(columnAcross > 9) {
	    	        		 allCurrentSequences.add(currentString);
	    	        		 currentString="";
	    	        		 stop = false;
	    	        	 }
	    	        	 //if the letter contains a letter it is added to currentString
	    	        	if(!currentBoard[rowAcross][columnAcross].equals(" ") &&!currentBoard[rowAcross][columnAcross].equals("+") ) {
	    	        	    currentString+= currentBoard[rowAcross][columnAcross];
	    	        	    columnAcross++;
	    	        	   
	    	        	}
	    	        	else {
	    	        		//once empty space is detected the currentString is added to all current characters list
	    	        		allCurrentSequences.add(currentString);
	    	        		currentString="";
	    	        		stop = true;
	    	        	}	
	    	        }
	    	        rowAcross++;
	    	        columnAcross=startingCoords[1];
	    		 }
	    		 //this checks for all values below the current letter 
	    		 int rowDown = startingCoords[0];
	    		 int columnDown = startingCoords[0];
	    		 for(int i=0; i < 9; i++) {
	    			 if(rowDown >9) {
	    				 allCurrentSequences.add(currentString);
	    				 currentString ="";
	    				 break;
	    			 }
	    			 if(!currentBoard[rowDown][columnDown].equals(" ") &&!currentBoard[rowAcross][columnAcross].equals("+") ) {
	    	        	    currentString+= currentBoard[rowDown][columnDown];
	    	        	    rowDown++;
	    			 }
	    			 else {
	    				 allCurrentSequences.add(currentString);
	    				 currentString="";
	    				 break;
	    			 }
	    		 }
	    		for(int i=0; i < allCurrentSequences.size();i++) {
	    			//if the current sequence is word within the word list or is within the alphabet
	    			if(wordList.contains(allCurrentSequences.get(i).toLowerCase())||alphabet.contains(allCurrentSequences.get(i).toLowerCase())) {
	    				correctWords.add(allCurrentSequences.get(i));
	    			}
	    			else {
	    				incorrectWords.add(allCurrentSequences.get(i));
	    			}
	    		} 
	    		if(incorrectWords.size()>0) {
	    			return "Invalid";
	    		}
	    		else {
	    			return "Valid";
	    		}
	    	 }
	    	 //this checks for row values which are greater than zero 
	    	 if(startingCoords[0]>0 && startingCoords[0]<9) {
	    		 int rowAcross = startingCoords[0];
	    		 int columnAcross = startingCoords[1];
	    		 for(int i=0; i < length;i++) {
	    			 boolean stop = false;
	    	         while(stop == false) {
	    	        	 if(rowAcross>9) {
	    	        		 allCurrentSequences.add(currentString);
	 	    				 currentString="";
	    	        		 stop=false;
	    	        	 }
	    	        	if(!currentBoard[rowAcross][columnAcross].equals(" ") &&!currentBoard[rowAcross][columnAcross].equals("+") ) {
	    	        	    currentString+= currentBoard[rowAcross][columnAcross];
	    	        	    columnAcross++;
	    	        	}
	    	        	else {
	    	        		allCurrentSequences.add(currentString);
	    	        		currentString="";
	    	        		stop = true;
	    	        	}	
	    	        }
	    	        rowAcross++;
	    	        columnAcross=startingCoords[1];
	    		 }
	    		 //calculate up is used because horizontally there may be letters
	    		int[] newCoords=  calculateUp(startingCoords, currentBoard);
	    		int rowDown = newCoords[0];
	    		int columnDown = newCoords[1];
	    		for(int i=0;i < 9; i++) {
	    			if(rowDown > 9) {
	    				allCurrentSequences.add(currentString);
	    				currentString="";
    			    	break;
    			    }
	    			if(!currentBoard[rowDown][columnDown].equals(" ")&&!currentBoard[rowDown][columnDown].equals("+")) {
	    				currentString += currentBoard[rowDown][columnDown]; 
	    				rowDown++;
	    				
	    			}
	    			else {
	    				 allCurrentSequences.add(currentString);
	    				 currentString="";
	    				 break;
	    			}
	    		}
	    		for(int i=0; i < allCurrentSequences.size();i++) {
	    			if(wordList.contains(allCurrentSequences.get(i).toLowerCase()) || alphabet.contains(allCurrentSequences.get(i))) {
	    				correctWords.add(allCurrentSequences.get(i));
	    			}
	    			else {
	    				incorrectWords.add(allCurrentSequences.get(i).toLowerCase());
	    			}
	    		} 
	    		if(incorrectWords.size()>0) {
	    			return "Invalid";
	    		}
	    		else {
	    			return "Valid";
	    		}
	    	 }
	    	 
	    	 if(startingCoords[0]==9 && length < 2) {
	    		 int rowAcross = startingCoords[0];
	    		 int columnAcross = startingCoords[1];
	    		 for(int i=0; i < length;i++) {
	    			 boolean stop = false;
	    	         while(stop == false) {
	    	        	 if(columnAcross > 9) {
	    	        		allCurrentSequences.add(currentString);
	    	        		currentString="";
	    	        		break;
	    	        	 }
	    	        	if(!currentBoard[rowAcross][columnAcross].equals(" ") &&!currentBoard[rowAcross][columnAcross].equals("+") ) {
	    	        	    currentString+= currentBoard[rowAcross][columnAcross];
	    	        	    columnAcross++;
	    	        	}
	    	        	else {
	    	        		allCurrentSequences.add(currentString);
	    	        		currentString="";
	    	        		stop = true;
	    	        	}	
	    	        }
	    	        rowAcross++;
	    	        columnAcross=startingCoords[1];
	    		 }
	    		 int[] newCoords=  calculateUp(startingCoords, currentBoard);
	    		 int rowDown = newCoords[0];
	    		 int columnDown = newCoords[1];
	    		 for(int i=0;i < 9; i++) {
	    			    if(rowDown > 9) {
	    			    	allCurrentSequences.add(currentString);
	    			    	currentString="";
	    			    	break;
	    			    }
		    			if(!currentBoard[rowDown][columnDown].equals(" ")&&!currentBoard[rowDown][columnDown].equals("+")) {
		    				currentString += currentBoard[rowDown][columnDown]; 
		    				rowDown++;
		    				
		    			}
		    			else {
		    				 allCurrentSequences.add(currentString);
		    				 currentString="";
		    				 break;
		    			}
		    		}
	    		     
		    		for(int i=0; i < allCurrentSequences.size();i++) {
		    			if(wordList.contains(allCurrentSequences.get(i).toLowerCase()) || alphabet.contains(allCurrentSequences.get(i))) {
		    				correctWords.add(allCurrentSequences.get(i));
		    			}
		    			else {
		    				incorrectWords.add(allCurrentSequences.get(i).toLowerCase());
		    			}
		    		} 
		    		//if there are any incorrect words the play is invalid be default
		    		if(incorrectWords.size()>0) {
		    			return "Invalid";
		    		}
		    		else {
		    			return "Valid";
		    		}
	    	 }	 
	     }
	     if(startingCoords[1]>0 && startingCoords[1]<9) {
	    	 if(startingCoords[0]==0) {
	    		 //calculate across is used because there may be values across
	    		 int[]newCoordsAcross = calculateAcross(startingCoords,currentBoard);
	    		 int rowAcross = newCoordsAcross[0];
	    		 int columnAcross = newCoordsAcross[1];
	    		 for(int i=0; i < length;i++) {
	    			 boolean stop = false;
	    	         while(stop == false) {
	    	        	 if(columnAcross > 9) {
	    	        		allCurrentSequences.add(currentString);
	    	        		currentString="";
	    	        		break;
	    	        	 }
	    	        	if(!currentBoard[rowAcross][columnAcross].equals(" ") &&!currentBoard[rowAcross][columnAcross].equals("+") ) {
	    	        	    currentString+= currentBoard[rowAcross][columnAcross];
	    	        	    columnAcross++;
	    	        	}
	    	        	else {
	    	        		allCurrentSequences.add(currentString);
	    	        		currentString="";
	    	        		stop = true;
	    	        	}	
	    	        }
	    	        rowAcross++;
	    	        columnAcross=startingCoords[1];
	    		 }
	    		 int[] newCoordsUp=  calculateUp(startingCoords, currentBoard);
	    		 int rowDown = newCoordsUp[0];
	    		 int columnDown = newCoordsUp[1];
	    		 for(int i=0;i < 9; i++) {
	    			    if(rowDown > 9) {
	    			    	allCurrentSequences.add(currentString);
	    			    	currentString="";
	    			    	break;
	    			    }
		    			if(!currentBoard[rowDown][columnDown].equals(" ")&&!currentBoard[rowDown][columnDown].equals("+")) {
		    				currentString += currentBoard[rowDown][columnDown]; 
		    				rowDown++;
		    				
		    			}
		    			else {
		    				 allCurrentSequences.add(currentString);
		    				 currentString="";
		    				 break;
		    			}
		    		}
	    		 for(int i=0; i < allCurrentSequences.size();i++) {
		    			if(wordList.contains(allCurrentSequences.get(i).toLowerCase()) || alphabet.contains(allCurrentSequences.get(i))) {
		    				correctWords.add(allCurrentSequences.get(i));
		    			}
		    			else {
		    				incorrectWords.add(allCurrentSequences.get(i).toLowerCase());
		    			}
		    		} 
		    		if(incorrectWords.size()>0) {
		    			return "Invalid";
		    		}
		    		else {
		    			return "Valid";
		    		} 
	    	 }
	    	 if(startingCoords[0]>0 && startingCoords[0]< 9) {
	    		 int[]newCoordsAcross = calculateAcross(startingCoords,currentBoard);
	    		 int rowAcross = newCoordsAcross[0];
	    		 int columnAcross = newCoordsAcross[1];
	    		 for(int i=0; i < length;i++) {
	    			 boolean stop = false;
	    	         while(stop == false) {
	    	        	 if(columnAcross > 9) {
	    	        		allCurrentSequences.add(currentString);
	    	        		currentString="";
	    	        		break;
	    	        	 }
	    	        	if(!currentBoard[rowAcross][columnAcross].equals(" ") &&!currentBoard[rowAcross][columnAcross].equals("+") ) {
	    	        	    currentString+= currentBoard[rowAcross][columnAcross];
	    	        	    columnAcross++;
	    	        	}
	    	        	else {
	    	        		allCurrentSequences.add(currentString);
	    	        		currentString="";
	    	        		stop = true;
	    	        	}	
	    	        }
	    	        rowAcross++;
	    	        columnAcross= startingCoords[1];
	    		 }
	    		 int[] newCoordsUp = calculateUp(startingCoords, currentBoard);
	    		 int rowDown = newCoordsUp[0];
	    		 int columnDown = newCoordsUp[1];
	    		 for(int i=0;i < 9; i++) {
	    			    if(rowDown > 9) {
	    			    	allCurrentSequences.add(currentString);
	    			    	break;
	    			    }
		    			if(!currentBoard[rowDown][columnDown].equals(" ")&&!currentBoard[rowDown][columnDown].equals("+")) {
		    				currentString += currentBoard[rowDown][columnDown]; 
		    				rowDown++;
		    				
		    			}
		    			else {
		    				 allCurrentSequences.add(currentString);
		    				 currentString="";
		    				 break;
		    			}
		    		}
	    		 for(int i=0; i < allCurrentSequences.size();i++) {
		    			if(wordList.contains(allCurrentSequences.get(i).toLowerCase()) || alphabet.contains(allCurrentSequences.get(i))) {
		    				correctWords.add(allCurrentSequences.get(i));
		    			}
		    			else {
		    				incorrectWords.add(allCurrentSequences.get(i).toLowerCase());
		    			}
		    		} 
		    		if(incorrectWords.size()>0) {
		    			return "Invalid";
		    		}
		    		else {
		    			return "Valid";
		    		} 	  
	    	 }
	    	 if(startingCoords[0]==9) {
	    		 int[]newCoordsAcross = calculateAcross(startingCoords,currentBoard);
	    		 int rowAcross = newCoordsAcross[0];
	    		 int columnAcross = newCoordsAcross[1];
	    		 for(int i=0; i < length;i++) {
	    			 boolean stop = false;
	    	         while(stop == false) {
	    	        	 if(columnAcross > 9) {
	    	        		allCurrentSequences.add(currentString);
	    	        		currentString="";
	    	        		break;
	    	        	 }
	    	        	if(!currentBoard[rowAcross][columnAcross].equals(" ") &&!currentBoard[rowAcross][columnAcross].equals("+") ) {
	    	        	    currentString+= currentBoard[rowAcross][columnAcross];
	    	        	    columnAcross++;
	    	        	}
	    	        	else {
	    	        		allCurrentSequences.add(currentString);
	    	        		currentString="";
	    	        		stop = true;
	    	        	}	
	    	        }
	    	        rowAcross++;
	    	        columnAcross=startingCoords[1];
	    		 }
	    		 int[] newCoordsUp = calculateUp(startingCoords, currentBoard);
	    		 int rowDown = newCoordsUp[0];
	    		 int columnDown = newCoordsUp[1];
	    		 for(int i=0;i < 9; i++) {
	    			    if(rowDown > 9) {
	    			    	allCurrentSequences.add(currentString);
	    			    	break;
	    			    }
		    			if(!currentBoard[rowDown][columnDown].equals(" ")&&!currentBoard[rowDown][columnDown].equals("+")) {
		    				currentString += currentBoard[rowDown][columnDown]; 
		    				rowDown++;
		    				
		    			}
		    			else {
		    				 allCurrentSequences.add(currentString);
		    				 currentString="";
		    				 break;
		    			}
		    		}
	    		 for(int i=0; i < allCurrentSequences.size();i++) {
		    			if(wordList.contains(allCurrentSequences.get(i).toLowerCase()) || alphabet.contains(allCurrentSequences.get(i))) {
		    				correctWords.add(allCurrentSequences.get(i));
		    			}
		    			else {
		    				incorrectWords.add(allCurrentSequences.get(i).toLowerCase());
		    			}
		    		} 
		    		if(incorrectWords.size()>0) {
		    			return "Invalid";
		    		}
		    		else {
		    			return "Valid";
		    		} 	   
	    	 } 
	     }
	     if(startingCoords[1]==9) {
	    	 if(startingCoords[0]==0) {
	    		 int rowAcross = startingCoords[0];
	    	     int columnAcross = startingCoords[1];
	    		 for(int i=0; i < length;i++) {
	    			 boolean stop = false;
	    	         while(stop == false) {
	    	        	 if(columnAcross < 0) {
	    	        		 allCurrentSequences.add(currentString);
	    	        		 currentString="";
	    	        		 stop = false;
	    	        	 }
	    	        	if(!currentBoard[rowAcross][columnAcross].equals(" ") &&!currentBoard[rowAcross][columnAcross].equals("+") ) {
	    	        	    currentString= currentBoard[rowAcross][columnAcross] + currentString;
	    	        	    columnAcross--;
	    	        	}
	    	        	else {
	    	        		allCurrentSequences.add(currentString);
	    	        		currentString="";
	    	        		stop = true;
	    	        	}	
	    	        }
	    	        rowAcross++;
	    	        columnAcross=startingCoords[1];   
	    		 }
	               
		    		int rowDown = startingCoords[0];
		    		int columnDown = startingCoords[1];
		    		for(int i=0;i < 9; i++) {
		    			if(rowDown > 9) {
		    				allCurrentSequences.add(currentString);
		    				currentString="";
	    			    	break;
	    			    }
		    			if(!currentBoard[rowDown][columnDown].equals(" ")&&!currentBoard[rowDown][columnDown].equals("+")) {
		    				currentString += currentBoard[rowDown][columnDown]; 
		    				rowDown++;
		    				
		    			}
		    			else {
		    				 allCurrentSequences.add(currentString);
		    				 currentString="";
		    				 break;
		    			}
		    		}
		    		 for(int i=0; i < allCurrentSequences.size();i++) {
			    			if(wordList.contains(allCurrentSequences.get(i).toLowerCase()) || alphabet.contains(allCurrentSequences.get(i))) {
			    				correctWords.add(allCurrentSequences.get(i));
			    			}
			    			else {
			    				incorrectWords.add(allCurrentSequences.get(i).toLowerCase());
			    			}
			    		} 
			    		if(incorrectWords.size()>0) {
			    			return "Invalid";
			    		}
			    		else {
			    			return "Valid";
			    		}
	    	 	}
	    	 if(startingCoords[0]>0 && startingCoords[0]<9) {
	    		 int[] newCoords = calculateAcross(startingCoords,currentBoard);
	    		 int rowAcross = newCoords[0];
	    	     int columnAcross = newCoords[1];
	    		 for(int i=0; i < length;i++) {
	    			 boolean stop = false;
	    	         while(stop == false) {
	    	        	 if(columnAcross < 0 || columnAcross > 9) {
	    	        		 allCurrentSequences.add(currentString);
	    	        		 currentString="";
	    	        		 break;
	    	        	 }
	    	        	if(!currentBoard[rowAcross][columnAcross].equals(" ") &&!currentBoard[rowAcross][columnAcross].equals("+") ) {
	    	        	    currentString += currentBoard[rowAcross][columnAcross];
	    	        	    columnAcross++;
	    	        	}
	    	        	else {
	    	        		allCurrentSequences.add(currentString);
	    	        		currentString="";
	    	        		stop = true;
	    	        	}	
	    	        }
	    	        rowAcross++;
	    	        columnAcross= startingCoords[1];   
	    		 }
	    		
	    		 int[] newCoordsUp= calculateUp(startingCoords, currentBoard);
	    		 int rowDown = newCoordsUp[0];
	    		 int columnDown = newCoordsUp[1];
	    		 for(int i=0;i < 9; i++) {
	    			    if(rowDown > 9) {
	    			    	allCurrentSequences.add(currentString);
	    			    	break;
	    			    }
		    			if(!currentBoard[rowDown][columnDown].equals(" ")&&!currentBoard[rowDown][columnDown].equals("+")) {
		    				currentString += currentBoard[rowDown][columnDown]; 
		    				rowDown++;
		    				
		    			}
		    			else {
		    				 allCurrentSequences.add(currentString);
		    				 currentString="";
		    				 break;
		    			}
		    		}
	    		 for(int i=0; i < allCurrentSequences.size();i++) {
		    			if(wordList.contains(allCurrentSequences.get(i).toLowerCase()) || alphabet.contains(allCurrentSequences.get(i))) {
		    				correctWords.add(allCurrentSequences.get(i));
		    			}
		    			else {
		    				incorrectWords.add(allCurrentSequences.get(i).toLowerCase());
		    			}
		    		} 
		    		if(incorrectWords.size()>0) {
		    			return "Invalid";
		    		}
		    		else {
		    			return "Valid";
		    		}
	 
	    	 }
	     }
	     
	
	  }
	    
	 return "There seems to be an error with this input check you have the correct size and location";
  }
  /** this method checks each cell vertically above each letter play and returns the coordinates for the farthest cell
   * containing a letter
 * @param givenCoords
 * @param givenBoard
 * @return int array values of the calculate coordinates to start from
 */
public int[] calculateUp(int[] givenCoords, String[][] givenBoard) {
	  //get the starting coordinates for a play and clone them to avoid pass-by reference in Java
	  int[] newCoords = givenCoords.clone();
	  int row = newCoords[0];
	  int column = newCoords[1];
	  boolean stop = false;
	  while(stop== false) {
		  if(!givenBoard[row][column].equals(" ") &&!givenBoard[row][column].equals("+") && row!=0) {
  	        	 row--;
  	        	 newCoords[0] = row;
		  }
  	        else {
  	        	 stop = true;
  	         }
		  }
	  if(givenBoard[row][column].equals(" ")||givenBoard[row][column].equals("+")) {
		  // to make sure you return the first letter from that column
		  newCoords[0] = row + 1;
	  }

	  return newCoords;
  }
  
  /**this method checks each cell horizontally from a given cell coordinate and returns the coordinates
   * for the farthest cell containing a letter
 * @param givenCoords
 * @param givenBoard
 * @return
 */
public int[] calculateAcross(int[] givenCoords, String[][]givenBoard) {
	  int[]newCoords = givenCoords.clone();
	  int row = newCoords[0];
	  int column = newCoords[1];
	  boolean stop = false;
	  while(stop== false) {
		  if(!givenBoard[row][column].equals(" ") &&!givenBoard[row][column].equals("+") && row!=0) {
  	        	 column--;
  	        	newCoords[1] = column;
		  }
  	        else {
  	        	 stop = true;
  	         }
		  }
	  if(givenBoard[row][column].equals(" ")||givenBoard[row][column].equals("+")) {
		// to make sure you return the first letter from that row
		  newCoords[1] = column + 1;
	  }

	  return newCoords;
	  
  }
 
  
  /** checks adjacency of all down plays
 * @param play
 * @return String value confirming validity or invalidity of a play
 */
public String checkAdjDown(Play play) {
	  int[] startingCoordinates = calculatingCoordinates(play);
	  int[] rackPositions = getRackPositions(play);
	  // counter of all the surrounding empty cells
	  int emptyCellCounter = 0;
	  //gets all surrounding values of a play
	  if(startingCoordinates[0]== 0 && startingCoordinates[1]== 0) {
		  for(int i=0;i < rackPositions.length;i++) {
			  //checks all neighbour values to the right of given play
			 String currentTile =	board.getTile(startingCoordinates[0], startingCoordinates[1]+1);
			   if(currentTile.equals(" ")) {
				   emptyCellCounter++;
				   
			   }
			   startingCoordinates[0] =  startingCoordinates[0]+1;
		  }
          //checks the values directly below the given play
		  String lastChar =  board.getTile(rackPositions.length,startingCoordinates[1]);
		  if(lastChar ==" " ) {
			  emptyCellCounter++;
		   }
			 
		  //if there is a certain number of empty cells the play is invalid
		  if(emptyCellCounter == rackPositions.length+1) {
			  return "Invalid";
		  }
		 
		}
	  // checks for down values that could have row greater than zero but less than 5
	  if(startingCoordinates[0]> 0 &&  startingCoordinates[0] < 5 && startingCoordinates[1]== 0) {
		   int topVal = startingCoordinates[0] -1;
		   if(board.getTile(topVal, startingCoordinates[1]).equals(" ")){
			   emptyCellCounter++;
		   }
		   for(int i=0;i < rackPositions.length;i++) {
				 String currentTile =	board.getTile(startingCoordinates[0], startingCoordinates[1]+1);
				   if(currentTile.equals(" ")) {
					   emptyCellCounter++;
				   }
				   startingCoordinates[0] =  startingCoordinates[0]+1;
			  }
		   String lastChar =  board.getTile(rackPositions.length,startingCoordinates[1]);
			  if(lastChar ==" ") {
				  emptyCellCounter++;
			   }
			  if(emptyCellCounter == rackPositions.length+2) {
				  return "Invalid";
			  }
			
	  }
	  //checks down values for rows greater than or equal to 5
	  if(startingCoordinates[0]>=5 && startingCoordinates[1]==0) {
			  int topVal = startingCoordinates[0] -1;
			   if(board.getTile(topVal, startingCoordinates[1]).equals(" ")){
				   emptyCellCounter++;
			   }
			   for(int i=0;i < rackPositions.length;i++) {
					 String currentTile =	board.getTile(startingCoordinates[0], startingCoordinates[1]+1);
					   if(currentTile.equals(" ")) {
						   emptyCellCounter++;
					   }
					   startingCoordinates[0] =  startingCoordinates[0]+1;	   
		  }
			   if(rackPositions.length==5) {
			   if(emptyCellCounter == rackPositions.length+1) {
				   return "Invalid";
				  }
				
		  } else{
				   String lastChar =  board.getTile(rackPositions.length,startingCoordinates[1]);
					  if(lastChar ==" ") {
						  emptyCellCounter++;
					   }
					  if(emptyCellCounter == rackPositions.length+2) {
						  return "Invalid";
					  }
				   
		  }
  	}   
	  //checks columns greater than zero
	  if(startingCoordinates[1]> 0 && startingCoordinates[1]< 9) {
		  if(startingCoordinates[0]==0) {
			  for(int i=0;i < rackPositions.length;i++) {
				     //checks tiles to the right and left now "+" character also considered
					 String currentTileRightSide =board.getTile(startingCoordinates[0], startingCoordinates[1]+1);
					 String currentTileLeftSide = board.getTile(startingCoordinates[0], startingCoordinates[1]-1);
					   if(currentTileRightSide.equals(" ") || currentTileRightSide.equals("+")) {
						   emptyCellCounter++;
					   }
					   if(currentTileLeftSide.equals(" ")|| currentTileLeftSide.equals("+")) {
						   emptyCellCounter++;
					   }
					   startingCoordinates[0] =  startingCoordinates[0]+1;
				  }
			  String lastChar =  board.getTile(rackPositions.length,startingCoordinates[1]);
			  if(lastChar.equals(" ")|| lastChar.equals("+")) {
				  emptyCellCounter++;
			   }
			  if(emptyCellCounter == (rackPositions.length*2)+1) {
				  return "Invalid";
			  }
		
			  
		  }
		  if(startingCoordinates[0]>0 && startingCoordinates[0]<5) {
			   int topVal = startingCoordinates[0] -1;
			   if(board.getTile(topVal, startingCoordinates[1]).equals(" ")){
				   emptyCellCounter++;
			   }
			   for(int i=0;i < rackPositions.length;i++) {
					 String currentTileRightSide =board.getTile(startingCoordinates[0], startingCoordinates[1]+1);
					 String currentTileLeftSide = board.getTile(startingCoordinates[0], startingCoordinates[1]-1);
					   if(currentTileRightSide.equals(" ")|| currentTileRightSide.equals("+")) {
						   emptyCellCounter++;
					   }
					   if(currentTileLeftSide.equals(" ")|| currentTileRightSide.equals("+")) {
						   emptyCellCounter++;
					   }
					   startingCoordinates[0] =  startingCoordinates[0]+1;
				  }
			  String lastChar =  board.getTile(rackPositions.length,startingCoordinates[1]);
			  if(lastChar.equals(" ")|| lastChar.equals("+")) {
				  emptyCellCounter++;
			   }
			  if(emptyCellCounter == (rackPositions.length*2)+2) {
				  return "Invalid";
			  }
			
		  }   
		  if(startingCoordinates[0]>=5) {
			  int topVal = startingCoordinates[0] -1;
			   if(board.getTile(topVal, startingCoordinates[1]).equals(" ")){
				   emptyCellCounter++;
			   }
			   for(int i=0;i < rackPositions.length;i++) {
					 String currentTileRightSide =board.getTile(startingCoordinates[0], startingCoordinates[1]+1);
					 String currentTileLeftSide = board.getTile(startingCoordinates[0], startingCoordinates[1]-1);
					   if(currentTileRightSide.equals(" ")||currentTileRightSide.equals("+") ) {
						   emptyCellCounter++;
					   }
					   if(currentTileLeftSide.equals(" ")||currentTileLeftSide.equals("+")) {
						   emptyCellCounter++;
					   }
					   startingCoordinates[0] =  startingCoordinates[0]+1;
				  }
			   
			  if(rackPositions.length==5) {
				  if(emptyCellCounter == rackPositions.length+1) {
					  return "Invalid";
					  }
			  }
			  else {
				  String lastChar =  board.getTile(rackPositions.length,startingCoordinates[1]);
				  if(lastChar.equals(" ")|| lastChar.equals("+")) {
					  emptyCellCounter++;
				   }
				  if(emptyCellCounter == rackPositions.length+2) {
					  return "Invalid";
				  }	  
			  }
			  
		  }
		    
	  }
	  //checks the farthest column this only has adjacent column to its left side 
	  if(startingCoordinates[1]==9) {
		  if(startingCoordinates[0]==0) {
			  for(int i=0;i < rackPositions.length;i++) {
					 String currentTile =	board.getTile(startingCoordinates[0], startingCoordinates[1]-1);
					   if(currentTile.equals(" ")) {
						   emptyCellCounter++;
					   }
					   startingCoordinates[0] =  startingCoordinates[0]+1;
				  }
			  String lastChar =  board.getTile(rackPositions.length,startingCoordinates[1]);
			  if(lastChar ==" ") {
				  emptyCellCounter++;
			   }
			  
			  if(emptyCellCounter == rackPositions.length+1) {
				  return "Invalid";
			  } 
		  }
		if(startingCoordinates[0]>0 && startingCoordinates[0]<5) {
			 int topVal = startingCoordinates[0] -1;
			   if(board.getTile(topVal, startingCoordinates[1]).equals(" ")){
				   emptyCellCounter++;
			   }
			   for(int i=0;i < rackPositions.length;i++) {
					 String currentTile =	board.getTile(startingCoordinates[0], startingCoordinates[1]-1);
					   if(currentTile.equals(" ")) {
						   emptyCellCounter++;
					   }
					   startingCoordinates[0] =  startingCoordinates[0]+1;
				  }
			  String lastChar =  board.getTile(rackPositions.length,startingCoordinates[1]);
			  if(lastChar ==" ") {
				  emptyCellCounter++;
			   }
			  
			  if(emptyCellCounter == rackPositions.length+2) {
				  return "Invalid";
			  } 		
		}
		if(startingCoordinates[0]>=5) {
			int topVal = startingCoordinates[0] -1;
			   if(board.getTile(topVal, startingCoordinates[1]).equals(" ")){
				   emptyCellCounter++;
			   }
			   for(int i=0;i < rackPositions.length;i++) {
					 String currentTile =	board.getTile(startingCoordinates[0], startingCoordinates[1]-1);
					   if(currentTile.equals(" ")) {
						   emptyCellCounter++;
					   }
					   startingCoordinates[0] =  startingCoordinates[0]+1;
				  }
			   if(rackPositions.length==5) {
					  if(emptyCellCounter == rackPositions.length+1) {
						  return "Invalid";
						  }
				  }
				  else {
					  String lastChar =  board.getTile(rackPositions.length,startingCoordinates[1]);
					  if(lastChar.equals(" ")|| lastChar.equals("+")) {
						  emptyCellCounter++;
					   }
					  if(emptyCellCounter == rackPositions.length+2) {
						  return "Invalid";
					  }	 				  
				  }	
		}
	  } 
	  return "Valid";
  }
  
  /** checks adjacency of all across plays
 * @param play
 * @return String value confirming validity or invalidity of a play
 */ 
public String checkAdjAcross(Play play) {
	  /*the following code functions similar to checkAdjDown but checks neighbouring cells across so
	   * the rows directly above or below and to the right and left of a given play
	   */
	  int[] startingCoordinates = calculatingCoordinates(play);
	  int[] rackPositions = getRackPositions(play);
	  int emptyCellCounter = 0;
	  if(startingCoordinates[0]==0) {
		  if(startingCoordinates[1]==0){
			  for(int i=0; i < rackPositions.length;i++) {
				 String currentTile =  board.getTile(startingCoordinates[0]+1,startingCoordinates[1]);
				 if(currentTile.equals(" ")|| currentTile.equals("+")) {
				     emptyCellCounter++;
				 }
				  startingCoordinates[1] =  startingCoordinates[1] + 1;
			  }
			  String lastChar =  board.getTile(startingCoordinates[0],rackPositions.length);
			  if(lastChar.equals(" ")|| lastChar.equals("+")) {
				  emptyCellCounter++;
			   }
			 if(emptyCellCounter==rackPositions.length+1) {
				 return "Invalid";
			 }
			  
		  }
		  if(startingCoordinates[1]>0 && startingCoordinates[1]<5) {
			  int leftSide = startingCoordinates[1] -1;
			   if(board.getTile(startingCoordinates[0], leftSide).equals(" ")){
				   emptyCellCounter++;
			   }
			   for(int i=0; i < rackPositions.length;i++) {
					 String currentTile =  board.getTile(startingCoordinates[0]+1,startingCoordinates[1]);
					 if(currentTile.equals(" ")|| currentTile.equals("+")) {
					     emptyCellCounter++;
					 }
					  startingCoordinates[1] =  startingCoordinates[1] + 1;
				  }
			   String lastChar =  board.getTile(startingCoordinates[0],rackPositions.length);
				  if(lastChar.equals(" ")) {
					  emptyCellCounter++;
				   }
				 if(emptyCellCounter==rackPositions.length+2) {
					 return "Invalid";
				 }  
		  }
		  if(startingCoordinates[1]>=5) {
			  int leftSide = startingCoordinates[1] - 1;
			  if(board.getTile(startingCoordinates[0], leftSide).equals(" ")){
				  emptyCellCounter++;
			  }
			  
			  for(int i=0; i< rackPositions.length;i++) {
				  String currentTile =  board.getTile(startingCoordinates[0]+1,startingCoordinates[1]);
					 if(currentTile.equals(" ")|| currentTile.equals("+")) {
					     emptyCellCounter++;
					 }
					  startingCoordinates[1] =  startingCoordinates[1] + 1;
			  }
			  if(rackPositions.length==5) {
				  if(emptyCellCounter== rackPositions.length+1) {
					  return "Invalid";
				  }	  
			  }
			  else {
				  String lastChar =  board.getTile(startingCoordinates[0],rackPositions.length);
				  if(lastChar.equals(" ")||lastChar.equals("+")) {
					  emptyCellCounter++;
				   }
				 if(emptyCellCounter==rackPositions.length+2) {
					 return "Invalid";
				 }  	
			 }
		  }
	  }
	  if(startingCoordinates[0]>0 && startingCoordinates[0]<9) {
		  if(startingCoordinates[1]==0) {
			  
			  for(int i=0; i< rackPositions.length;i++) {
				  String currentTileBelow =  board.getTile(startingCoordinates[0]+1,startingCoordinates[1]);
				  String currentTileAbove = board.getTile(startingCoordinates[0]-1, startingCoordinates[1]);
					 if(currentTileBelow.equals(" ")|| currentTileBelow.equals("+")) {
					     emptyCellCounter++;
					 }
					 if(currentTileAbove.equals(" ")|| currentTileAbove.equals("+")) {
						 emptyCellCounter++;
					 }
					  startingCoordinates[1] =  startingCoordinates[1] + 1;
			  }
			  String lastChar =  board.getTile(startingCoordinates[0],rackPositions.length);
			  if(lastChar.equals(" ")||lastChar.equals("+")) {
				  emptyCellCounter++;
			   }
			  if(emptyCellCounter==(rackPositions.length*2)+1) {
				  return "Invalid";
				 }
			  
		  }
		  if(startingCoordinates[1]>0 && startingCoordinates[1]<5) {
			  int leftSide = startingCoordinates[1] - 1;
			  if(board.getTile(startingCoordinates[0], leftSide).equals(" ")){
				  emptyCellCounter++;
			  }
			  for(int i=0; i< rackPositions.length;i++) {
				  String currentTileBelow =  board.getTile(startingCoordinates[0]+1,startingCoordinates[1]);
				  String currentTileAbove = board.getTile(startingCoordinates[0]-1, startingCoordinates[1]);
					 if(currentTileBelow.equals(" ")|| currentTileBelow.equals("+")) {
					     emptyCellCounter++;
					 }
					 if(currentTileAbove.equals(" ")|| currentTileAbove.equals("+")) {
						 emptyCellCounter++;
					 }
					  startingCoordinates[1] =  startingCoordinates[1] + 1;
			  }
			  String lastChar =  board.getTile(startingCoordinates[0],rackPositions.length);
			  if(lastChar.equals(" ")||lastChar.equals("+")) {
				  emptyCellCounter++;
			   }
			  if(emptyCellCounter==(rackPositions.length*2)+2) {
				  return "Invalid";
				 }
		  }
		  if(startingCoordinates[1]>=5) {
			  int leftSide = startingCoordinates[1] - 1;
			  if(board.getTile(startingCoordinates[0], leftSide).equals(" ")){
				  emptyCellCounter++;
			  }
			  for(int i=0; i< rackPositions.length;i++) {
				  String currentTileBelow =  board.getTile(startingCoordinates[0]+1,startingCoordinates[1]);
				  String currentTileAbove = board.getTile(startingCoordinates[0]-1, startingCoordinates[1]);
					 if(currentTileBelow.equals(" ")|| currentTileBelow.equals("+")) {
					     emptyCellCounter++;
					 }
					 if(currentTileAbove.equals(" ")|| currentTileAbove.equals("+")) {
						 emptyCellCounter++;
					 }
					  startingCoordinates[1] =  startingCoordinates[1] + 1;
			  }
			  if(rackPositions.length==5) {
				  if(emptyCellCounter== (rackPositions.length*2)+1) {
					  return "Invalid";
				  }	  
			  }
			  else {
				  String lastChar =  board.getTile(startingCoordinates[0],rackPositions.length);
				  if(lastChar.equals(" ")||lastChar.equals("+")) {
					  emptyCellCounter++;
				   }
				 if(emptyCellCounter==(rackPositions.length*2)+2) {
					 return "Invalid";
				 }  	
			 }	  	  
		  }
	  }
	  if(startingCoordinates[0]==9) {
		  if(startingCoordinates[1]==0) {
			  for(int i=0; i < rackPositions.length;i++) {
					 String currentTile =  board.getTile(startingCoordinates[0]-1,startingCoordinates[1]);
					 if(currentTile.equals(" ")|| currentTile.equals("+")) {
					     emptyCellCounter++;
					 }
					  startingCoordinates[1] =  startingCoordinates[1] + 1;
				  }
			  String lastChar =  board.getTile(startingCoordinates[0],rackPositions.length);
			  if(lastChar.equals(" ")||lastChar.equals("+")) {
				  emptyCellCounter++;
			   }
			  if(emptyCellCounter==rackPositions.length+1) {
				  return "Invalid";
				 }  	 
		  }  
		  if(startingCoordinates[1]>0 && startingCoordinates[1]<5) {
			  int leftSide = startingCoordinates[1] - 1;
			  if(board.getTile(startingCoordinates[0], leftSide).equals(" ")){
				  emptyCellCounter++;
			  }
			  for(int i=0; i < rackPositions.length;i++) {
					 String currentTile =  board.getTile(startingCoordinates[0]-1,startingCoordinates[1]);
					 if(currentTile.equals(" ")|| currentTile.equals("+")) {
					     emptyCellCounter++;
					 }
					  startingCoordinates[1] =  startingCoordinates[1] + 1;
				  }
			  String lastChar =  board.getTile(startingCoordinates[0],rackPositions.length);
			  if(lastChar.equals(" ")||lastChar.equals("+")) {
				  emptyCellCounter++;
			   }
			  if(emptyCellCounter==rackPositions.length+2) {
				  return "Invalid";
				 }  	 
	  
		  }
		  if(startingCoordinates[1]>=5) {
			  int leftSide = startingCoordinates[1] - 1;
			  if(board.getTile(startingCoordinates[0], leftSide).equals(" ")){
				  emptyCellCounter++;
			  }
			  for(int i=0; i < rackPositions.length;i++) {
					 String currentTile =  board.getTile(startingCoordinates[0]-1,startingCoordinates[1]);
					 if(currentTile.equals(" ")|| currentTile.equals("+")) {
					     emptyCellCounter++;
					 }
					  startingCoordinates[1] =  startingCoordinates[1] + 1;
				  }
			  if(rackPositions.length==5) {
				  if(emptyCellCounter== (rackPositions.length)+1) {
					  return "Invalid";
				  }	  
			  }
			  else {
				  String lastChar =  board.getTile(startingCoordinates[0],rackPositions.length);
				  if(lastChar.equals(" ")||lastChar.equals("+")) {
					  emptyCellCounter++;
				   }
				 if(emptyCellCounter==(rackPositions.length)+2) {
					 return "Invalid";
				 }  	
			 }	
  
		  }  
	 }
	 return "Valid";
  }
  
  /** this is used to place values on the board for the check validity function without actually effecting the board
 * @param play
 * @param givenBoard
 * @return 2D String array containing a copy of the board with letters placed on it
 */
public String[][] checkBoardValidity(Play play, String[][] givenBoard){
	    //a clone array of the real board with intended play to be validated
	    String[][] currentBoard = givenBoard;
	    int[] givenCoordinates = calculatingCoordinates(play);
		int row = givenCoordinates[0];
		int column = givenCoordinates[1];

		
		int[] rackPositions = getRackPositions(play);
		
      ArrayList<String> currentCharacters = new  ArrayList<String>();
      for(int i =0; i < rackPositions.length; i++) {
      	 currentCharacters.add(rack.getCharacter(rackPositions[i]))  ;	   
      }
		switch(play.dir()){
			case ACROSS:
				for(int i=0; i< currentCharacters.size();i++) {
					//the play is placed on the board checking if the cell is occupied at the same time
					if(givenBoard[row][column].equals(" ") ||givenBoard[row][column].equals("+")) {
						givenBoard[row][column] = currentCharacters.get(i);
						column++;
					}
					else {
						//error message and null value returned 
						System.out.println("This play is invalid because a following cell is occupied");
						return null;
					}

				 }
					 
				break;
			case DOWN:
				for(int i=0; i < currentCharacters.size();i++) {
					if(!givenBoard[row][column].equals(" ") || !givenBoard[row][column].equals("+")) {
						givenBoard[row][column] = currentCharacters.get(i);
						row++;
					}
					else {
						System.out.println("This play is invalid because a following cell is occupied");
						return null;
					}
				}
		       break;
			
		}
      //returns the current board
	  return currentBoard;
  }
}
	

