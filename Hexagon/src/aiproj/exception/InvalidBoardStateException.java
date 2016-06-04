/* Ryan Chew - rchew1, Primaniaga Suryadi - psuryadi */
package aiproj.exception;


/**
 * <h1>Invalid Board State Exception</h1>
 * An exception to be thrown when the game state inputed is invalid, 
 * ie. it is not a square.
 * 
 * 
 * @author 	Ryan Chew
 * @version	0.2 (amended)
 * @since	2016-03-29
 *
 */
/*
 * CHANGELOG:
 * 
 * MAR29 - Ryan - Created InvalidBoardStateException (0.1)
 * MAR29 - Ryan - Added documentation
 * MAR29 - Ryan - Completed and Finalized InvalidBoardStateException
 * MAR31 - Ryan - ReOpened InvalidBoardStateException to change error message
 * MAR31 - Ryan - Added overloaded methods for clearer error messages (0.2)
 * 
 */
public class InvalidBoardStateException extends Exception {

	/**
	 * The Serial ID of the exception
	 */
	private static final long serialVersionUID = 1L;
	
	public InvalidBoardStateException() {
		super("Board State entered was invalid. Exiting...");
	}
	
	public InvalidBoardStateException(char invalidCharacter, int row, int col) {
		super("Illegal character \"" + invalidCharacter + "\" was " +
				"found at row: " + (row) + ", col: " + (col));
	}
	
}
