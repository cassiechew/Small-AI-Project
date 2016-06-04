/* Ryan Chew - rchew1, Primaniaga Suryadi - psuryadi */
package aiproj.exception;


/**
 * <h1>Invalid Board Size Exception</h1>
 * An exception written to be thrown when the board inputed is of the wrong
 * Dimensions. Meaning the height and width of the board is not of the 
 * size
 * 
 * 
 * @author	Ryan Chew
 * @version	0.2 (amended)
 * @since	2016-03-28
 */
/*
 * CHANGELOG:
 * 
 * MAR28 - Ryan - Created InvalidBoardSizeException (0.1)
 * MAR29 - Ryan - Added documentation
 * MAR29 - Ryan - Completed and Finalized InvalidBoardSizeException
 * MAR31 - Ryan - ReOpened InvalidBoardSizeException to change error message
 * MAR31 - Ryan - Added overloaded methods for clearer error messages (0.2)
 * 
 */
public class InvalidBoardSizeException extends Exception{

	/**
	 * The Serial ID of the exception
	 */
	private static final long serialVersionUID = 1L;
	
	public InvalidBoardSizeException() {
		super("Invelid BoardSize was entered. Exiting.");
	}
	
	public InvalidBoardSizeException(String attribute, Integer location, Integer length, Integer correction) {
		super("Invalid " + attribute + " length at row " + (location) + ". Length: " 
				+ length + ". When should be: " + correction);
	}
	public InvalidBoardSizeException(String attribute, Integer length, Integer correction) {
		super("Invalid " + attribute + " length. Length: " 
				+ length + ". When should be: " + correction);
	}
	
}
