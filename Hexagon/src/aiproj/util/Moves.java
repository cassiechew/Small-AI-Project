package aiproj.util;

import java.util.ArrayList;

import aiproj.hexifence.Piece;



/**
 * <h1>Moves</h1>
 * 
 * A generic class that extends ArrayList<BoardMovePair> {@link BoardMovePair}
 * to show the future states of the board.
 * 
 * 
 * @author 	ryanchew
 * @version 0.1
 * @since	2016-04-27
 */
/*
 * CHANGELOG:
 * 
 * APR 27 - Ryan - Created Moves class w/ constructor
 * APR 27 - Ryan - Added getAvaliableMoves Method
 */
public class Moves extends ArrayList<BoardMovePair>{

	/**
	 * The serial ID of the class
	 */
	
	private static final long serialVersionUID = 1L;
	@SuppressWarnings(value = { "unused" })
	private static final Character DASH = '-';
	private static final Character PLUS = '+';

	private int numberOfMoves;
	/*************************************************************************/

	
	public Moves() {
		
	}
	/*************************************************************************/

	
	/**
	 * Creates an instance of the class. It generates and stores the 
	 * available moves and the move coordinates in a BoardMovePair and adds it
	 * to itself.
	 * 	
	 * 
	 * @param boardState
	 * @param player
	 */
	public Moves (BoardState boardState, Integer player) {
		this.numberOfMoves = 0;
		getAvaliableMoves(boardState, player);
	}
	/*************************************************************************/

	
	/**
	 * returns the variable numberOfMoves
	 * @return
	 */
	public int getNumberOfMoves() {
		return numberOfMoves;
	}
	
	
	/**
	 * Generates the available moves and populates this with them.
	 * 
	 * 
	 * @param boardState
	 * @param player
	 */
	private void getAvaliableMoves(BoardState boardState, Integer player) {

		for (int row = 0; row < boardState.size(); row++) {
			for (int col = 0; col < boardState.size(); col++) {
				if (boardState.getCharAt(row, col) == PLUS) {
					BoardState newBoard = new BoardState(boardState);
					newBoard.setCharAt(row,col,(player==Piece.BLUE) ? 'B':'R');
					this.add(new BoardMovePair(newBoard, 
							new Move(row,col,player)));
					this.numberOfMoves += 1;
				}
			}
		}
	}
	
	/*public BoardMovePair getBestMove() {
		
		BoardMovePair best = this.get(0);
		
		for (BoardMovePair move : this) {
			if (move.getScore() > best.getScore()) {
				best = move;
			}
		}
		
		return best;
	}*/
	
}
