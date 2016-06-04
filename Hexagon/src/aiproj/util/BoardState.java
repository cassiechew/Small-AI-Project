/* Ryan Chew - rchew1, Primaniaga Suryadi - psuryadi */
package aiproj.util;

import java.util.ArrayList;


/**
 * <h1>BoardState</h1>
 * A generic class that extends ArrayList<String> that is being used to
 * represent the board state.
 * 
 * 
 * @author 	Ryan Chew
 * @version 0.2
 * @since	2016-03-29
 */
/*
 * CHANGELOG:
 * 
 * MAR29 - Ryan - Created BoardState class
 * MAR29 - Ryan - Added Documentation
 * MAR29 - Ryan - Added getCharAt method
 * MAR31 - Ryan - Added boardSize variable
 * MAR31 - Ryan - Added getBoardSize method
 * MAR31 - Ryan - Added Documentation
 * MAR31 - Ryan - Added getRowLength
 * 
 */
public class BoardState extends ArrayList<String>{

	/**
	 * The serial ID of the data type
	 */
	private static final long serialVersionUID = 1L;
	
	/** The size of the board */
	private Integer boardSize;
	
	/**
	 * Creates an instance of the BoardState object. It stores the BoardSize in a 
	 * local attribute.
	 * 
	 * 
	 * @param boardSize
	 */
	public BoardState (int boardSize) {
		this.boardSize = boardSize;
		
	}
	
	public BoardState (BoardState boardState) {
		
		for (String s : boardState) {
			this.add(s);
		}
		
		this.boardSize = boardState.boardSize;
		
	}
	/********************************************************************************/

	
	/**
	 * Returns the locally stored BoardSize for this board.
	 * 
	 * 
	 * @return the boardSize
	 */
	public Integer getBoardSize() {
		return this.boardSize;
	}
	/********************************************************************************/

	
	/**
	 * Returns the length or the row supplied
	 * 
	 * 
	 * @param row The row of which the length is required
	 * @return The length of the row
	 */
	public Integer getRowLength(int row) {
		return this.get(row).length();
	}
	/********************************************************************************/

	
	/**
	 * Goes into the board and gets the string at row, and returns the character at 
	 * col.
	 * 
	 * 
	 * @param row The row to search in
	 * @param col The column to search in
	 * @return The character at the specified coordinates
	 */
	public char getCharAt(int row, int col) {
		return this.get(row).charAt(col);
	}
	/********************************************************************************/

	
	/**
	 * Sets the character representation to a new character inputted at (row,col)
	 * @param row
	 * @param col
	 * @param newChar
	 */
	public void setCharAt(int row, int col, char newChar) {
		
		StringBuilder newRow = new StringBuilder(this.get(row));
		newRow.setCharAt(col, newChar);
		
		this.set(row, newRow.toString());
		
	}
	/********************************************************************************/
	
	
	/**
	 * 
	 */
	public static void generateBlankBoard(BoardState board) {
		//A check to whether the current line is an alternating line
		boolean isAlternate = false;
		//A check to whether the current line is past the no starting dash section
		boolean pastStartingLength = false;
		//A check to whether the '+', A, B section is over
		boolean sectionOver = false;

		//The starting number of non-dashes in the first and second line
		int startingLength = board.boardSize * 2;
		int alternateStartingLength = (board.boardSize * 2) - (board.boardSize - 1);
		//System.out.println(board.boardSize + "GGGGGGGGGGGGGGG");
		//System.out.println(startingLength + " " + alternateStartingLength);
		
		//The number of dashes before reaching the non-dash section
		int forwardSpace = 0;
		
		//An incremental counter to increase or decrease the number of non-dashes in 
		//the subsequent lines
		int counter = 0;
		

		
		//Iterates over the rows of the board
		for(int row = 0; row < (4*board.boardSize-1); row++) {
			
			String newRow = "";
			//Resets the sectionOver for each new row
			sectionOver = false;
			//Detects whether the current row is a row with alternate non-dash symbols
			isAlternate = (row % 2 != 0);
			
			//Calculates whether to add or subtract or do nothing to the counter based on 
			//how many rows have passed and what the current row is as the number of non
			//dash symbols increases, stays, then decreases
			/////////////////////////////// might be 1 row late
			counter += (row % 2 == 0 && row != 0 && row != ((4*board.boardSize-1)/2 + 1) ) ? ((row < startingLength) ? 1 : -1) : 0;
			
			//Calculates whether the current row is past the no starting dash section
			pastStartingLength = (row > ((4*board.boardSize-1) / 2));
			
			//Adds 1 to forwardSpace for each row after the non starting dash section
			if (pastStartingLength) {
				forwardSpace++;
			}
			
			//Iterates over the columns
			for(int col =  0; col < (4*board.boardSize-1);) {
				
				//Assigns the value of forwardSpace to currentEdge as to not change
				//forwardSpace
				int currentEdges = forwardSpace;
				//The number of non-dash symbols to be printed
				int edgeCounter = 0;
				
				//Checks if the current column is in the dash area, before or after the 
				//non-dash section and checks for any non-dashes in the area
				while(sectionOver == true || currentEdges > 0) {
					
					//Will break out of loop once it has reached the end of the board
					if (col >= (4*board.boardSize-1)) break;
					//refer to verify dash
					newRow = newRow + "-";
					col++;
					//Subtracts from current edge as col moves closer to non-dash section
					currentEdges -= (currentEdges > 0) ? 1 : 0;

				}
				
				//Checks if row is an alternating row
				if(!isAlternate) {
					//Assigns edgeCounter the number of consecutive non-dash symbols to
					//be read
					edgeCounter = startingLength + (2 * counter);
					//Loops through the non-dash section and checks if there are any dashes
					while(edgeCounter > 0) {
						//Will break out of loop if it has reached the end of the board
						if (col >= (4*board.boardSize-1)) break; 
						newRow = newRow + "+";
						col++;
						edgeCounter--;
					}
				}
				else {
					//Assigns the number of nun-dash symbols required to be read
					edgeCounter = alternateStartingLength + (counter);
					//System.out.println("alt plus = " + edgeCounter);
					//A boolean switch to tell whether the current column is a dash
					Boolean isDash = new Boolean(false);
					//Loops over the alternating area and checks if there are and symbols
					//out of place in the row
					while(edgeCounter > 0) {
						//Will break out of loop if it has reached the end of the board
						if (col >= (4*board.boardSize-1)) break;
						if (isDash) {
							newRow = newRow + "-";
						}
						else if (!isDash) {
							newRow = newRow + "+";
							edgeCounter--;
						}
						
						isDash = !isDash;
						col++;
						//edgeCounter--;

					}
				}
				//Lets the loop know that the non-dash section is over
				sectionOver = true;
			}
			//new row
			board.add(newRow);
		}
	}
	
}
