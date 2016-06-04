/* Ryan Chew - rchew1, Primaniaga Suryadi - psuryadi */
package aiproj.util;

import aiproj.hexifence.Piece;


/**
 * A generic class to contain the coordinates of the hexagons and contains some
 * methods in the manipulation of the coordinates
 * 
 * @author 	PNS
 * @version	0.1
 * @since	2016-03-30
 */
/*
 * CHANGELOG:
 * 
 * MAR30 - PNS  - Created HexCoordinates
 * MAR30 - PNS  - Added getCoordinates
 * MAR30 - Ryan - Added documentation
 * MAR30 - Ryan - Added hexCoordComp method
 * MAR31 - Ryan - Changed hexCoordComp
 * MAR31 - Ryan - Added documentation
 * APR01 - PNS 	- Removed Ternary statement in hexCoordComp
 * 
 */
public class HexCoordinates{
	
	/** The x coordinate */
	private int x;
	/** The y coordinate */
	private int y;
	
	private boolean isCaptured;
	private Integer owner;

	private Integer ajacentHexes;
	
	/**
	 * Creates an instance of the HexCoordinates object, assigning the row and column
	 * to the x and y variables in the object.
	 * 
	 * 
	 * @param row The row value of the hexagon
	 * @param col The column value of the hexagon
	 */
	public HexCoordinates(int row, int col){
		this.x = row;
		this.y = col;
		this.isCaptured = false;
		this.owner = Piece.EMPTY;
		this.ajacentHexes = 0;
	}
	/********************************************************************************/

	
	/**
	 * Creates an integer array and assigns the x and y coordinates to the array.
	 * It then returns the array.
	 * 
	 * 
	 * @return The coordinate array
	 */
	public int[] getCoordinates(){
		int[] coordinates = {x,y};
		return coordinates;
	}
	/********************************************************************************/

	
	/**
	 * A comparison function to compare two different instances of HexCoordinates
	 * 
	 * 
	 * @param coord1 The first coordinate
	 * @param coord2 The second coordinate
	 * @return a boolean denoting whether the comparision is equal or not
	 */
	public static boolean hexCoordComp(HexCoordinates coord1, HexCoordinates coord2) {
		
		return (coord1.x == coord2.x && coord1.y == coord2.y);
		
	}
	/********************************************************************************/

	
	public int checkAjacency(BoardState board) {
		
		Analyzer analyzer = Analyzer.getAnalyzer();
		analyzer.setBoardState(board);
		
		if (board.getCharAt(this.x + 1, this.y + 1) == '+' && analyzer.isHexagon(this.x + 2, this.y + 2)) {
			this.ajacentHexes += 1;
		}
		if (board.getCharAt(this.x + 1, this.y) == '+' && analyzer.isHexagon(this.x + 2, this.y)) {
			this.ajacentHexes += 1;
		}
		if (board.getCharAt(this.x - 1, this.y) == '+' && analyzer.isHexagon(this.x - 2, this.y)) {
			this.ajacentHexes += 1;
		}
		if (board.getCharAt(this.x, this.y + 1) == '+' && analyzer.isHexagon(this.x, this.y + 2)) {
			this.ajacentHexes += 1;
		}
		if (board.getCharAt(this.x, this.y - 1) == '+' && analyzer.isHexagon(this.x, this.y - 2)) {
			this.ajacentHexes += 1;
		}
		if (board.getCharAt(this.x - 1, this.y - 1) == '+' && analyzer.isHexagon(this.x - 2, this.y - 2)) {
			this.ajacentHexes += 1;		
		}
		
		return this.ajacentHexes;
		
	}
	
	public void setIsCaptured(boolean captured) {
		this.isCaptured = captured;
	}
	
	public boolean getIsCaptured() {
		return isCaptured;
	}
	
	public Integer getOwner() {
		return owner;
	}
	
	public void setOwner(Integer owner) {
		this.owner = owner;
	}
	
	public void setAjacentHexes(Integer number) {
		this.ajacentHexes = number;
	}
	
	public Integer getAjacentHexes() {
		return this.ajacentHexes;
	}
}
