/* Ryan Chew - rchew1, Primaniaga Suryadi - psuryadi */
package aiproj.util;

import java.util.ArrayList;

import aiproj.hexifence.Piece;


/**
 * <h1>Analyzer</h1>
 * This is a utility tool used to analyze a game state of Hexifence. It 
 * implements tools to find data on the game state. Usage of this class is only 
 * calling analyzeState which will output the required data.
 * 
 * 
 * @author 	Ryan Chew
 * @version	0.3
 * @since	2016-03-29
 */
/*
 * CHANGELOG:
 * 
 * MAR29 - Ryan - Created Analyzer (0.1)
 * MAR29 - Ryan - Added AnalyzeState, findNumberOfPossibleMoves, 
 * 					findAtariHexagons findMaxCapturableSingleMove
 * MAR29 - Ryan - Completed AnalyzeState, Added and Completed isHexagon
 * MAR29 - Ryan - Completed findNumberOfPossibleMoves
 * MAR29 - Ryan - Finalized AnalyzeState and isHexagon
 * MAR29 - PNS  - Amended AnalyzeState, 
 * MAR29 - PNS  - Added makeSimpleBoard, findMaxSingleCapture, new 
 * 					findAtariHexagons (0.2)
 * MAR29 - Ryan - Removed findMaxCapturableSingleMove, Deprecated original 
 * 					findAtariHexagons
 * MAR30 - Ryan - Amended method name
 * MAR31 - Ryan - Changed analyzer to a singleton design pattern
 * MAR31 - Ryan - Removed legacy findAtariHexagons (0.3)
 * MAR31 - Ryan - Added documentation
 * APR01 - PNS	- Created Amalgamated [A combination of findMaxSingleCapture 
 * 					and findAtariHexagons]
 * APR01 - PNS	- Changed result output to use return value from Amalgamated
 * APR01 - Ryan - Deprecated findMaxSingleCapture and findAtariHexagons
 * APR02 - Ryan - Deprecated findNumberOfPossibleMoves
 * APR02 - Ryan - Added functionality of findNumberOfPossibleMoves to 
 * 					makeSimpleBoard
 * APR28 - Ryan - Added _analyzeState
 * 
 */
public class Analyzer {

	/** The Current state of the board as was passed in on creation of the 
	 * class */
	private BoardState boardState;
	
	/** The instance of the analyzer */
	private static Analyzer instance = null;
	
	
	/**
	 * 
	 * @author ryanchew
	 *
	 */
	public static class AnalysisData extends ArrayList<Object>{

		/**
		 * type serial number
		 */
		private static final long serialVersionUID = 7820864589243009784L;
		
		public static final int TOTALMOVES = 0;
		public static final int SINGLEMOVECAPTUREHEX = 1;
		public static final int MAXCAPSINGLEMOVE = 2;
		public static final int AVAILABLEHEX = 3;
		public static final int CAPTUREDHEX = 4;
		public static final int SAFE = 5;
		
	}
	
	
	/**
	 * A private constructor to prevent external instantiation of the singleton
	 */
	private Analyzer () {
	}
	/*************************************************************************/

	
	/**
	 * A static method to return the instance of the analyzer singleton. If an 
	 * instance does not exist, it will create an instance and return it.
	 * <p>
	 * This allows a single instance of the analyzer for the system.
	 * 
	 * 
	 * @return The instantiated analyzer
	 */
	public static Analyzer getAnalyzer() {
		if (instance == null) {
			instance = new Analyzer();
		}
		return instance;
	}
	/*************************************************************************/

	
	/**
	 * This method updates BoardState to the new state passed in the argument.
	 * 
	 * 
	 * @param boardState The new BoardState to analyze
	 */
	public void setBoardState(BoardState boardState) {
		this.boardState = boardState;
	}
	/*************************************************************************/

	
	/**
	 * Generates a simplified board and uses the simple board in its analysis. 
	 * Once analysis is complete, it prints the information to System.out 
	 * (stdout) in a formatted form.
	 * 
	 */
	protected void analyzeState () {
		ArrayList<HexCoordinates> hexState = makeSimpleBoard();
		int[] atariPossible = Amalgamated(hexState);
		System.out.println("Analyzing state...");
		
		System.out.println("============== Analysis COMPLETE ==============");
		System.out.println("Number of possible moves: " + 
				findNumberOfPossibleMoves());
		/*System.out.println("Number of hexagons capturable in one move: "+
				findMaxSingleCapture(hexState));
		System.out.println("Maximum hexagons capturable in one move: "+
				findAtariHexagons(hexState));*/
		System.out.println("Number of hexagons capturable in one move: "+
				(Integer)atariPossible[0]);
		System.out.println("Maximum hexagons capturable in one move: "+
				(Integer)atariPossible[1]);
		System.out.println("===============================================");

	}
	/*************************************************************************/
	
	
	/**
	 * Does the same calculations as analyzeState() excepts returns the 
	 * information
	 * 
	 * 
	 * @return An arraylist of the data
	 */
	public AnalysisData _analyzeState() {
		
		ArrayList<HexCoordinates> hexState = makeSimpleBoard();
		int[] atariPossible = Amalgamated(hexState);
		AnalysisData data = new AnalysisData();
		Integer availableHexagons = hexState.size();
		Integer capturedHexagons = 0;
		
		data.add(findNumberOfPossibleMoves());
		data.add(atariPossible[0]);
		data.add(atariPossible[1]);
		
		for (HexCoordinates coordinate : hexState) {
			if (coordinate.getIsCaptured()) {
				availableHexagons--;
				capturedHexagons++;
			}
		}
		
		data.add(availableHexagons);
		data.add(capturedHexagons);

		return data;
	}
	
	public int[] analyzeEndGame() {
		int[] RBHex = {0,0};
		ArrayList<HexCoordinates> hexes = makeSimpleBoard();
		
		
		for (HexCoordinates hex : hexes) {

			if (hex.getOwner() == Piece.RED) {
				RBHex[0]++;
			}
			else if (hex.getOwner() == Piece.BLUE) {
				RBHex[1]++;
			}
			
		}
		return RBHex;
		
	}
	/*************************************************************************/

	
	/**
	 * Loops through the current BoardState and searches for the number of 
	 * possible moves, as denoted by a '+' symbol.
	 * <p>
	 * The function returns the total number of '+' symbols found. Which will 
	 * be presented in an Executive Summary.
	 * 
	 * 
	 * @see		BoardState
	 * @return 	The number of possible moves to do
	 */
	private Integer findNumberOfPossibleMoves() {
		
		//A local counter to count the number of available moves
		Integer freeMoves = 0;
		
		//Iterates through the rows
		for (int row = 0; row < boardState.size(); row++) {
			//Iterates through the columns
			for (int col = 0; col < boardState.size(); col++) {
			//Checks if the current position is a '+', if it is add 1 else, 0
				freeMoves += (boardState.getCharAt(row, col) == '+') ? 1 : 0;
			}
		}
		return freeMoves;
	}
	
	
	/*************************************************************************/

	
	/**
	 * A function that generates a smaller board containing the coordinates of 
	 * the hexagons in the main board. Also counts the number of free liberties
	 * on the map.
	 * <p>
	 * The function loops through the board and checks if there is a hexagon in
	 * that position using {@link #isHexagon(int, int)} and adds the 
	 * coordinates to a list if it is found to be a hexagon.
	 * 
	 * 
	 * @see 	HexCoordinates
	 * @return 	The list of coordinates
	 */
	private ArrayList<HexCoordinates> makeSimpleBoard(){
		
		//The list containing the coordinates of the hexagons
		ArrayList<HexCoordinates>coordinates = new ArrayList<HexCoordinates>();
		
		//Iterates through the rows
		for (int row = 0; row < boardState.size(); row++) {
			//System.out.print(boardState.size());
			//System.out.println("row = " +  row);
			//Iterates through the columns
			for (int col = 0; col < boardState.size(); col++) {
				//System.out.println("col = " + col);
				if(isHexagon(row,col)) {
					HexCoordinates coordinate = new HexCoordinates(row, col);
					coordinates.add(coordinate);
					
					if (checkLiberties(row, col).size() == 0) {
						coordinate.setIsCaptured(Boolean.TRUE);
						coordinate.setOwner((boardState.getCharAt(row,col) == 
								'r') ? Piece.RED : Piece.BLUE );
					}
				}
			}
		}
		return coordinates;
	}
	/*************************************************************************/

	
	/**
	 * Loops through the hexagons and finds hexagons with a single liberty. It 
	 * then compares hexagon common points and checks whether there is a shared
	 * single liberty that is the last liberty of both hexagons.
	 * <p>
	 * It returns the maximum number of hexagons that can be captured in a 
	 * single move. The information will be used in the formatted printing of 
	 * the analysis.
	 * 
	 * 
	 * @param hexState 	The coordinates of the hexagons 
	 * @see 			#makeSimpleBoard(), #checkLiberties(int, int), 
	 * 					HexCoordinates
	 * @deprecated
	 * @return 			The max number of hexagons that can be captured in a 
	 * 					single move
	 */
	@Deprecated @SuppressWarnings(value = { "unused" })
	private Integer findMaxSingleCapture(ArrayList<HexCoordinates> hexState) {
		
		//The maximum number of hexagons that can be captured in one move
		int maxCap = 0;
		//A list of the connecting hexagons with a single common point
		ArrayList<HexCoordinates> commonPoint=new ArrayList<HexCoordinates>();
		
		//Iterates through the hexagons
		for(HexCoordinates hex : hexState){
			//A placeholder for the liberties of a hexagon
			ArrayList<HexCoordinates> liberties;
			
			//A coordinate array to hold the coordinates of the current hexagon
			int [] posArray = hex.getCoordinates();
			//populate the array of liberties with the number of open liberties
			liberties = checkLiberties(posArray[0],posArray[1]);
			
			//If there is a single liberty open on the current hexagon, it sets
			//maxCap
			//to 1
			if (liberties.size() == 1) {
				if (maxCap == 0){
					maxCap = 1;
				}
				
				//Iterates through the list of liberties coordinates and checks
				//if it is shared with the current hexagon
				for (HexCoordinates point : commonPoint){
					//Compares the coordinates for equality
					if (HexCoordinates.hexCoordComp(liberties.get(0), point)) {
						maxCap = 2;
					}
				}
				//Adds the single liberty to the common points array
				commonPoint.add(liberties.get(0));
			}
		}
		return maxCap;
	}
	/*************************************************************************/


	/**
	 * Loops through the hexagons and checks the liberties and adds them to a 
	 * place-holder variable. It then checks the length of the variable for 
	 * whether the hexagon contains more than one liberty. If it contains a 
	 * single liberty, the count for the number of atari'd hexagons increases.
	 * <p> 
	 * The function returns the  number of threatened hexagons to be used in 
	 * the formatted results of the analysis.
	 * 
	 * 
	 * @param	hexState
	 * @see		#makeSimpleBoard(), #checkLiberties(int, int), HexCoordinates
	 * @deprecated
	 * @return 	The number hexagons that are threatened to be captured
	 */
	@Deprecated @SuppressWarnings(value = { "unused" })
	private Integer findAtariHexagons(ArrayList<HexCoordinates> hexState) {
		
		//A counter for the number of threatened hexagons
		Integer atariHexagons = 0;
		//Iterates through all the hexagons
		for(HexCoordinates hex : hexState){
			//A place-holder variable for the liberties of each hexagon
			ArrayList<HexCoordinates> liberties;
			//The coordinate array of the current hexagons coordinates
			int[] posArray = hex.getCoordinates();
			//Populates the liberties array with the number of free spaces 
			//around the hexagon
			liberties = checkLiberties(posArray[0],posArray[1]);
			if (liberties.size() > 1) {
				continue;
			}
			else if (liberties.size() == 1) {
				atariHexagons++;
				continue;
			}
		}
		return atariHexagons;
	}
	/*************************************************************************/

	
	/**
	 * Checks the number of liberties a hexagon has from being captured. It checks
	 * each side of the hexagon to whether it is a free space, and adds it to
	 * liberties if it is a '+'.
	 * <p>
	 * The information would then be used to check if at each side, if that space is
	 * also the only free liberty in the adjacent hexagon.
	 * 
	 * 
	 * @param row The row coordinate
	 * @param col The col coordinate
	 * @return The collection of available liberties of a hexagon
	 */
	public ArrayList<HexCoordinates> checkLiberties(int row, int col) {
		
		
		
		ArrayList<HexCoordinates> liberties = new ArrayList<HexCoordinates>();
		
		
		
		if (boardState.getCharAt(row-1, col-1) == '+') {
			liberties.add(new HexCoordinates(row-1, col-1));
		}
		if (boardState.getCharAt(row, col-1) == '+') {
			liberties.add(new HexCoordinates(row,col-1));
		}
		if (boardState.getCharAt(row-1, col) == '+') {
			liberties.add(new HexCoordinates(row-1,col));
		}
		if (boardState.getCharAt(row+1, col+1) == '+') {
			liberties.add(new HexCoordinates(row+1,col+1));
		}
		if (boardState.getCharAt(row, col+1) == '+') {
			liberties.add(new HexCoordinates(row,col+1));
		}
		if (boardState.getCharAt(row+1, col) == '+') {
			liberties.add(new HexCoordinates(row+1,col));
		}
		return liberties;
	}
	/********************************************************************************/

	
	/**
	 * A function to check if the current position is surrounded by a hexagon, denoting
	 * the current position as a hexagon coordinate.
	 * <p>
	 * It is used in the process of finding how many liberties a hexagon has by
	 * finding the position of the center of the hexagon.
	 * 
	 * 
	 * @param row The row position in the BoardState
	 * @param col The column position in the BoardState
	 * @return Whether the current space is a hexagon or not
	 */
	public boolean isHexagon(int row, int col) {
		
		String reference = "rb-";
		
		if (row <= 0 || col <= 0 || row >= boardState.size()-1 || col >= boardState.size()-1) { return false; };
		
		
		if (reference.indexOf(boardState.getCharAt(row-1, col-1)) >= 0) {
			return false;
		}
		if (reference.indexOf(boardState.getCharAt(row+1, col)) >= 0) {
			return false;
		}
		if (reference.indexOf(boardState.getCharAt(row-1, col)) >= 0) {
			return false;
		}
		if (reference.indexOf(boardState.getCharAt(row+1, col+1)) >= 0) {
			return false;
		}
		if (reference.indexOf(boardState.getCharAt(row, col+1)) >= 0) {
			return false;
		}
		if (reference.indexOf(boardState.getCharAt(row, col-1)) >= 0) {
			return false;
		}
		return true;
	}
	/********************************************************************************/

	
	/**
	 * A new updated amalgamation between the two previous legacy functions {@link #findAtariHexagons(ArrayList)}
	 * and {@link #findMaxSingleCapture(ArrayList)}. Due to similarities between the
	 * functions they were combined. The operations are the same as the other two.
	 * The return value is an integer array containing the results.
	 * 
	 * 
	 * @param hexState The simple board of hexagons
	 * @see 	#findAtariHexagons(ArrayList) {@link #findMaxSingleCapture(ArrayList)} {@link #makeSimpleBoard()}
	 * @return an array of the the atari and maxSingleCapture
	 */
	private int[] Amalgamated(ArrayList<HexCoordinates> hexState) {
		
		//The maximum number of hexagons that can be captured in one move
		Integer maxCap = 0;
		//A counter for the number of threatened hexagons
		Integer atariHexagons = 0;
		//A list of the connecting hexagons with a single common point
		ArrayList<HexCoordinates> libertyPoints = new ArrayList<HexCoordinates>();
		
		//Iterates through the hexagons
		for(HexCoordinates hex : hexState){
			//A placeholder for the liberties of a hexagon
			ArrayList<HexCoordinates> liberties;
			
			//A coordinate array to hold the coordinates of the current hexagon
			int [] posArray = hex.getCoordinates();
			//populates the array of liberties with the number of open liberties
			liberties = checkLiberties(posArray[0],posArray[1]);

			if (liberties.size() == 1) {
				atariHexagons++;
				//If there is a single liberty open on a hexagon, that means
				//there is at least 1 capturable hex and maxCap must at least be 1
				if (maxCap == 0){
					maxCap = 1;
				}
				
				if (maxCap == 2) {continue;};
				//Iterates through the list of liberties coordinates and checks if it is 
				//shared with the current hexagon
				for (HexCoordinates point : libertyPoints){
					//Compares the coordinates for equality

					if (HexCoordinates.hexCoordComp(liberties.get(0), point)) {
						maxCap = 2;
					}
				}
				//Adds the single liberty to the liberty points array
				libertyPoints.add(liberties.get(0));
			}
		
			
		}		
		
		int[] results = {maxCap, atariHexagons};
		return results;
	}
	/********************************************************************************/
	
	
}
