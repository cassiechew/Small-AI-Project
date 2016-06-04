/* Ryan Chew - rchew1, Primaniaga Suryadi - psuryadi */
package aiproj.util;

import aiproj.exception.InvalidBoardSizeException;
import aiproj.exception.InvalidBoardStateException;

/**
 * <h1>Board Verifier</h1>
 * A utility tool to verify that an input board is correctly formatted.
 * 
 * @author 	Ryan Chew
 * @version	0.2
 * @since	2016-03-29
 */
/*
 * CHANGELOG: 
 * 
 * MAR29 - Ryan - Created BoardVerifier
 * MAR29 - Ryan - Moved boardSizeVerification from BoardReader to here
 * MAR29 - Ryan - Added verifyBoard and boardSyntaxVerification
 * MAR29 - Ryan - Merging boardSyntaxVerification and boardSizeVerification
 * MAR31 - Ryan - Changed BoardVerifier to singleton design pattern (0.2)
 * MAR31 - Ryan - Added documentation
 * MAR31 - Ryan - Added verifyDash function with documentation
 * MAR31 - Ryan - Added documentation, comments
 * MAR31 - Ryan - Added dashVerifierRouter
 * 
 */
public class BoardVerifier {
	
	/** The board State to verify */
	private BoardState boardState;
	/** The size of the board */
	private Integer boardSize;
	
	/** The instance of the BoardVerifier */
	private static BoardVerifier instance = null;
	
	
	/**
	 * A private constructor to prevent external instantiation of the singleton
	 */
	private BoardVerifier(){
	}
	/********************************************************************************/

	
	 /** 
	  * A static method to return the instance of the BoardVerifier singleton. If an 
	 * instance does not exist, it will create an instance and return it.
	 * <p>
	 * This allows a single instance of the BoardVerifier for the system.
	 * 
	 * 
	 * @return The instantiated BoardVerifier
	 */
	public static BoardVerifier getVerifier() {
		if (instance == null) {
			instance = new BoardVerifier();
		}
		return instance;
	}
	/********************************************************************************/

	
	/**
	 * This method updates the BoardState to the new state passed in the argument.
	 * 
	 * 
	 * @param boardState
	 */
	protected void setBoardState(BoardState boardState) {
		this.boardState = boardState;
		this.boardSize = boardState.getBoardSize();
	}
	/********************************************************************************/


	/**
	 * Calls the verification tools to verify the specified board. It will throw 
	 * an InvalidBoardStateException and an InvalidBoardSizeException if any of the 
	 * board is wrongly formatted and the program will stop.
	 * <p>
	 * There is no return, however it is used to ensure that any further input will be
	 * correctly formatted so that the program and make an accurate analysis.
	 * 
	 * 
	 * @throws InvalidBoardStateException
	 * @throws InvalidBoardSizeException
	 */
	protected void verifyBoard() throws InvalidBoardStateException, InvalidBoardSizeException{
		
		boardSizeVerification();
		boardSyntaxVerification();
		
	}
	/********************************************************************************/

	
	/**
	 * A tool to check the syntax of the board. It will run through each of the lines
	 * of the board and check if it is what it is supposed to be. The tool exploits 
	 * the fact that each of the boards will be symmetrical and uses that fact to 
	 * easily predict which symbol should be at a specific place.
	 * <p>
	 * It will predict based on the BoardSize how many lines have no dashes prefacing
	 * them. Since the board is symmetrical, it knows that the number of '+' in the
	 * top row will also be the number of the '+' symbol in the first position in the 
	 * next rows. Once it has passed the section where there are no rows that begin
	 * with dashes. It will begin a count as it knows that for each line after that
	 * section, there will be an additional space.
	 * <p>
	 * Next it will check for alternating or non alternating lines. A correctly 
	 * formatted board will contain lines that contain alternating dashes and non
	 * alternating dashes. eg.
	 * 
	 * 								B R B R - - -
	 *								R - B - B - -
	 *			
	 * <p>
	 * After that it checks for full dashes until the end of the line.
	 * <p>
	 * If any of the checks fail they will throw an InvalidBoardStateException and 
	 * the program will stop.
	 * 
	 * 
	 * @throws InvalidBoardStateException
	 */
	private void boardSyntaxVerification() throws InvalidBoardStateException{
		
		//A check to whether the current line is an alternating line
		boolean isAlternate = false;
		//A check to whether the current line is past the no starting dash section
		boolean pastStartingLength = false;
		//A check to whether the '+', A, B section is over
		boolean sectionOver = false;

		//The starting number of non-dashes in the first and second line
		int startingLength = boardSize * 2;
		int alternateStartingLength = (boardSize * 2) - (boardSize - 1);
		
		//The number of dashes before reaching the non-dash section
		int forwardSpace = 0;
		
		//An incremental counter to increase or decrease the number of non-dashes in 
		//the subsequent lines
		int counter = 0;

		
		//Iterates over the rows of the board
		for(int row = 0; row < boardState.size(); row++) {
			
			//Resets the sectionOver for each new row
			sectionOver = false;
			//Detects whether the current row is a row with alternate non-dash symbols
			isAlternate = (row % 2 != 0);
			
			//Calculates whether to add or subtract or do nothing to the counter based on 
			//how many rows have passed and what the current row is as the number of non
			//dash symbols increases, stays, then decreases
			counter += (row % 2 == 0 && row != 0 && row != (boardState.size()/2 + 1)) ? 
					((row < startingLength) ? 1 : -1) : 0;
			
			//Calculates whether the current row is past the no starting dash section
			pastStartingLength = (row > (boardState.size() / 2));
			
			//Adds 1 to forwardSpace for each row after the non starting dash section
			if (pastStartingLength) {
				forwardSpace++;
			}
			
			//Iterates over the columns
			for(int col =  0; col < boardState.size(); col++) {
				
				//Assigns the value of forwardSpace to currentEdge as to not change
				//forwardSpace
				int currentEdges = forwardSpace;
				//The number of non-dash symbols to be read
				int edgeCounter = 0;
				
				//Checks if the current column is in the dash area, before or after the 
				//non-dash section and checks for any non-dashes in the area
				while(sectionOver == true || currentEdges > 0) {
					
					//Will break out of loop once it has reached the end of the board
					if (col >= boardState.size()) break; 
					//refer to verify dash
					verifyDash(row, col, false);
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
						if (col >= boardState.size()) break; 
						verifyDash(row, col, true);
						col++;
						edgeCounter--;
					}
				}
				else {
					//Assigns the number of nun-dash symbols required to be read
					edgeCounter = alternateStartingLength + (counter);
					//A boolean switch to tell whether the current column is a dash
					Boolean isDash = new Boolean(false);
					//Loops over the alternating area and checks if there are and symbols
					//out of place in the row
					while(edgeCounter > 0) {
						//Will break out of loop if it has reached the end of the board
						if (col >= boardState.size()) break;
						//Makes a call to add or subtract an edge based on the routers response
						edgeCounter -= (dashVerifierRouter(isDash, row, col)) ? 0 : 1;
						
						isDash = !isDash;
						col++;

					}
				}
				//Lets the loop know that the non-dash section is over
				sectionOver = true;
			}
		}
	}
	/********************************************************************************/

	
	/**
	 * A router function that, based on the first argument, to verify whether the 
	 * current position as denoted by (row, col) is a dash symbol.
	 * <p>
	 * It returns a boolean to tell the parent function whether to subtract from 
	 * the edgeCounter variable {@link #verifyBoard()}
	 * 
	 * 
	 * @param 	isDash
	 * @param 	row
	 * @param 	col
	 * @see		#verifyBoard()
	 * @return	a boolean for the parent function to handle
	 * @throws 	InvalidBoardStateException
	 */
	private boolean dashVerifierRouter(boolean isDash, int row, int col) 
			throws InvalidBoardStateException {
		
		if (!isDash) {
			verifyDash(row, col, true);
			return false;
		}
		else {
			verifyDash(row, col, false);
			return true;
		}
		
	}
	/********************************************************************************/

	
	/**
	 * This verifies whether the given coordinates is a dash or not. The function
	 * does a verification based on whether the input of toConfirm is true or false.
	 * When false it will confirm if the coordinates are a dash and if they are not a
	 * dash if toConfirm is true.
	 * <p>
	 * The function does not return anything but it is a essential function in the
	 * verification of the board as it allows the program to easily check each 
	 * coordinates without having many if statements.
	 * 
	 * 
	 * @param row 			The row of the board to check
	 * @param col			The column of the board to check
	 * @param toConfirm 	A switch on whether to confirm if it is a dash or not a dash
	 * @throws InvalidBoardStateException
	 */
	private void verifyDash(int row, int col, boolean toConfirm)
			throws InvalidBoardStateException{

		Character toCompare = boardState.getCharAt(row, col);
		String reference = "RB+";
		
		if (!toConfirm && toCompare != '-') {
			throw new InvalidBoardStateException(toCompare, row, col);
		}
		//Checks if the character at the current position is in the list of legal characters
		else if (toConfirm && reference.indexOf(toCompare) <= 0) {
			throw new InvalidBoardStateException(toCompare, row, col);
		}
		
	}
	/********************************************************************************/

	
	/**
	 * A function to verify that the board is of the correct size height and width. 
	 * It loops through the rows checking the length of each row.
	 * 
	 * @throws InvalidBoardStateException 
	 */
	private void boardSizeVerification() throws InvalidBoardSizeException {
		
		Integer verifiedLength = (boardSize*4) - 1;
		
		//Checks the number of rows if its correct
		if (boardState.size() != verifiedLength) 
			throw new InvalidBoardSizeException("column", boardState.size(), verifiedLength);
		
		//Loops over each row to check whether the number of elements is correct
		for(int row = 0; row < boardState.size(); row++) {
			if (boardState.getRowLength(row) != verifiedLength) {
				throw new InvalidBoardSizeException("row", row, boardState.getRowLength(row), boardState.size());
			}
		}
	}
	
}
