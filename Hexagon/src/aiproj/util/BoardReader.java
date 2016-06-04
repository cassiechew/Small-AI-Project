/* Ryan Chew - rchew1, Primaniaga Suryadi - psuryadi */
package aiproj.util;

import java.util.Scanner;

import aiproj.exception.InvalidBoardSizeException;


/********************************************************************************/
/**
 * <h1>BoardReader</h1>
 * This BoardReader program is an implementation of an application that reads text
 * from stdin (System.in) and prints information about the board from an analyzer
 * {@link Analyzer}.
 * 	- Number of possible moves
 * 	- Maximum number of hexagons that can be captured in a single move
 *  - Number of hexagonal cells available for capture in a single move
 * <p>
 * Before processing the board, the board is first verified through the 
 * {@link BoardVerifier}.
 * 
 * 
 * @author 	Ryan Chew
 * @version	0.3
 * @see		Analyzer 
 * @see 	BoardVerifier
 * @since	2016-03-28
 */
/*
 * CHANGELOG:
 * 
 * MAR28 - Ryan - Created BoardReader
 * MAR28 - Ryan - Added boardState, boardSize and main
 * MAR28 - Ryan - Tested CLI to program. Failed. Changed to opening files for now
 * MAR28 - Ryan - Added and completed generateBoard and boardSizeVerification
 * MAR28 - Ryan - Added, completed and finalized printCurrentBoard
 * MAR29 - Ryan - Changed format of code in main, now it is held by single try (0.2)
 * MAR29 - Ryan - Moved BoardSizeVerification to BoardVerifier class
 * MAR30 - Ryan - Added additional documentation, amended method name
 * MAR31 - Ryan - Removed legacy boardSizeVerification function (0.3)
 * MAR31 - Ryan - Removed references to BoardSize and changed to access functions in BoardState
 * MAR31 - Ryan - Updated comments in reference to BoardSize
 * 
 */
public class BoardReader {
	
	/****************************************************************************/
	/** The state of the board */
	private static BoardState boardState;
	
	
	private static final Integer EXIT_ERROR = 1;
	/********************************************************************************/

	
	public static void main(String args[]) {
		
		/** Instantiates the main class */
		BoardReader main = new BoardReader();
		
		/****************************************************************************/
		/* Admin Data */
		//System.out.println("Working Directory = "+System.getProperty("user.dir"));
		//File file = new File("testfiles/testfile2");
		/****************************************************************************/
		
		
		/* Generates a reader for the input board */
		Scanner sc = null;

		try {
			//Creates a new Scanner that reads from System.in (stdin)
			sc = new Scanner(System.in); 
			//Populates the BoardState
			main.generateBoard(sc);
			//Closes the scanner instance
			sc.close();
			//Retrieves the verifier singleton
			BoardVerifier verifier = BoardVerifier.getVerifier();
			//Sets the verifiers board state to the current set
			verifier.setBoardState(boardState);
			//Calls the verifier to verify the board
			verifier.verifyBoard();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(EXIT_ERROR);
		}
		
		//Retrieves the analyzer singleton
		Analyzer analyzer = Analyzer.getAnalyzer();
		//Sets the analyzers board state to the current state
		analyzer.setBoardState(boardState);
		//Calls the analyzer to analyze the board
		analyzer.analyzeState();
		
	}
	/********************************************************************************/
	
	
	/**
	 * Assigns the BoardState and BoardSize variables that can then be used in the
	 * {@link Analyzer}. The scanner argument must be the scanner that 
	 * contains information on the input board.
	 * <p>
	 * The function does not return. The function assigns the class variables
	 * BoardState and BoardSize, then checks for invalid input before populating
	 * BoardState with the whitespace stripped input from the Scanner argument.
	 * The BoardState is the input for the analysis tools that gives information on
	 * the current state of the board.
	 * 
	 * 
	 * @param 	sc The reader of the input file
	 * @throws 	InvalidBoardSizeException 
	 * @see		BoardState
	 */
	private void generateBoard(Scanner sc) throws InvalidBoardSizeException {
		
		boardState = new BoardState(new Integer(sc.nextLine()));
		
		System.out.println("Reading board passed in...");

	
		
		while(sc.hasNextLine()) {
			
			String toAdd = sc.nextLine().replaceAll("\\s+", "");
			System.out.println(toAdd);			
	    	boardState.add(toAdd);
	    }
		//boardState.remove(boardState.size() - 1);
		
	}
	/********************************************************************************/

		
	/**
	 * A function used in development that will loop the current BoardState and print
	 * each element. This is used to check the BoardState after changes made in the
	 * code.
	 * 
	 * For development purposes only
	 */
	@SuppressWarnings("unused")
	private void printCurrentBoard() {
		for(int i = 0; i < boardState.size(); i++) {
			System.out.println(boardState.get(i));
		}
	}
}
