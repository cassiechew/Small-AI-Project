package aiproj.boredGames;

import java.io.PrintStream;
import java.util.Random;

import machineLearning.core.Learner;
import machineLearning.core.Sentience;
import machineLearning.util.MoveCatalogue;
import aiproj.hexifence.Piece;
import aiproj.hexifence.Player;
import aiproj.util.Analyzer;
import aiproj.util.BoardState;
import aiproj.util.Move;
import aiproj.util.Moves;
import aiproj.util.Analyzer.AnalysisData;
import aiproj.util.BoardMovePair;


/**
 * <h1>SuryadiAI</h1>
 * 
 * An AI that plays Hexifence
 * 
 * 
 * @author 	ryanchew
 * @version	0.1
 * @since 	2016-04-27
 */
/*
 * CHANGELOG:
 * 
 * APR27 - Ryan - Created SuryadiAI class
 * APR27 - Ryan - Added interface methods
 * APR27 - Ryan - Implemented init method
 * APR27 - Ryan - Added make move and analyzeBetterMove
 * APR29 - Ryan - Added BetterMove Analyzers and evaluation
 * APR29 - Ryan - Added Gaussian random to move
 * MAY07 - Ryan - Added documentation
 * 
 */
public class SuryadiAI implements Player, Piece {

	/** The initial search depth for checking a good move */
	private static final Integer DEFAULTSEARCHDEPTH = 2;
	/** The players board */
	private BoardState playerBoard;
	/** The turn of the player 1/2 */
	private int playerSide;
	/** An arraylist of the moves that can be taken from the current position*/
	private Moves availableMoves;
	private Integer fullBoardSize;
	/** The analysis data of the board */
	private AnalysisData boardData;
	private Integer searchDepth;
	private Random randomGenerator;
	private Character playerPiece;
	private Character opponentPiece;
	private static final String DATABASE = 
			"../DataFiles/textbook.readmeifyoucan";
	private Learner learner;
	private Sentience brain;
	
	/*************************************************************************/


	/**
	 * The method to initialize an instance of the AI. It takes the size of the
	 * board and the type of player it is. 
	 * <p>
	 * It returns 0 on successful initialization
	 *  
	 * @param n The size of the board
	 * @param p The type of piece to assign to the player
	 * @return 0 on success
	 */
	public int init(int n, int p) {
		
		Analyzer analyzer = Analyzer.getAnalyzer();
		
		this.playerBoard = new BoardState(n);
		BoardState.generateBlankBoard(playerBoard);
		
		analyzer.setBoardState(playerBoard);
		this.boardData = analyzer._analyzeState();
		
		this.playerSide = p;
		this.availableMoves = new Moves(playerBoard, playerSide);
		this.fullBoardSize = this.availableMoves.size();
		
		this.searchDepth = DEFAULTSEARCHDEPTH;
		
		randomGenerator = new Random();
		playerPiece = (p == Piece.BLUE) ? 'B' : 'R';
		opponentPiece = (p == Piece.BLUE) ? 'R' : 'B';
		
		learner = new Learner();
		brain = new Sentience();
		brain.init(SuryadiAI.DATABASE);
		
		return 0;
	}
	/*************************************************************************/
	

	/**
	 * An evaluation function that finds the overall spread of the pieces on 
	 * the board. It finds each piece and compares the distances between this
	 * piece and other pieces that have not had their distances calculated.
	 * <p>
	 * This returns an integer that represents the average distance between all
	 * pieces on the board. The data will be used to evaluate the value of the 
	 * move.
	 * 
	 * 
	 * @param boardPair
	 * @return
	 */
	private Integer evaluation(BoardMovePair boardPair) {
		Integer hueristic = 0;
		BoardState board = boardPair.getBoard();
		
		Integer totalDistance = 0;
		Integer totalFilled = 0;
		
		for (int row = 0; row < board.size(); row++) {
			for (int col = 0; col < board.size(); col++) {
				if (board.getCharAt(row, col) != '+' && 
						board.getCharAt(row,col)!='-'){
					totalFilled += 1;
					for (int rowcomp = row; rowcomp < board.size(); rowcomp++){
						for (int colcomp=col;colcomp<board.size();colcomp++) {
							if (board.getCharAt(rowcomp, colcomp) != '+' && 
									board.getCharAt(rowcomp, colcomp) != '-') {
								if (row != rowcomp && col != colcomp) {
									totalDistance += Math.abs(row-rowcomp) + 
											Math.abs(col-colcomp);
								}
							}
						}
					}
				}
			}
			
		}
		//hueristic = (totalDistance) / totalFilled;
		hueristic = totalDistance;
		
		return hueristic;
	}

	
	/**
	 * A function that provides a platform to scan the better moves and finds
	 * the best move to take.
	 * 
	 * 
	 * @param betterMoves
	 * @return
	 */
	private BoardMovePair analyzeBetterMovesRouter(Moves betterMoves) {
		
		Integer turn = 0;
		Moves moves = new Moves();
		Moves currentAvailableMoves;
		Moves bestMove = new Moves();
		
		if (betterMoves.size() == 0) return null;
		
		for (BoardMovePair betterMove : betterMoves) {
			betterMove.setScore(0.0);
			currentAvailableMoves = new Moves(betterMove.getBoard(), 
					(turn % 2 == 0) ? (playerSide == Piece.BLUE) ? Piece.RED : 
						Piece.BLUE : (playerSide == Piece.BLUE) ? Piece.BLUE : 
							Piece.RED);
			betterMove.setScore(analyzeBetterMovesMKII(betterMove, 
					currentAvailableMoves, 0));
			moves.add(betterMove);
		}
		
		bestMove.add(moves.get(0));
		
		for (int i = 1; i < moves.size(); i++) {
			
			double t = moves.get(i).getScore();
			double v = bestMove.get(0).getScore();

			if ( Double.compare(t, v) > 0) {
				bestMove.clear();
				bestMove.add(moves.get(i));
			}
			else if ( Double.compare(t, v) == 0) {
				bestMove.add(moves.get(i));
			}
		}
		
		if (bestMove.size() > 1 ){
			BoardMovePair best = new BoardMovePair(null, null);
			for (BoardMovePair move : bestMove) {
				move.setScore(this.brain.reference(move));
				if (move.getScore() > best.getScore()) {
					best = move;
				}
			}
			return best;
			
		}
		return bestMove.get(0);
	}
	
	
	/**
	 * An implementation of a variation of a min-max tree. 
	 * 
	 * 
	 * @param currentM
	 * @param betterMoves
	 * @param turn
	 * @return
	 */
	private Double analyzeBetterMovesMKII(BoardMovePair currentM, 
			Moves betterMoves, Integer turn) {
		
		AnalysisData currentMoveData;
		 
		Moves currentAvailableMoves;
		
		//the goodness of the node
		Double nodeScore = 0.;
		
		if (betterMoves.size() == 0) return 0.;
		
		Analyzer analyzer = Analyzer.getAnalyzer();
		//print("LLL");
		analyzer.setBoardState(currentM.getBoard());
		currentMoveData = analyzer._analyzeState();
		AnalysisData comparingData = null;
		int numberCaptured = 0;
		
		/* looks through all the available moves passed in and looks
		 * for moves that produce hexes
		 */
		if ((turn+1) <= this.searchDepth) {
			/* generates the next better moves from current node
			 * 
			 */
			for (BoardMovePair currentMove : betterMoves) {
				
				analyzer.setBoardState(currentMove.getBoard());
				comparingData = analyzer._analyzeState();
				numberCaptured = (
			   (Integer)currentMoveData.get(Analyzer.AnalysisData.CAPTUREDHEX)
			   -(Integer)comparingData.get(Analyzer.AnalysisData.CAPTUREDHEX));
				
				if (numberCaptured > 0) {
					currentMove.setWasCaptured(true);
				}
				nodeScore += numberCaptured - evaluation(currentMove);

				/* generates the next better moves from current node */
				currentAvailableMoves = new Moves(currentMove.getBoard(), 
						(turn % 2 == 0) ? (playerSide == Piece.BLUE) ? 
						Piece.RED : Piece.BLUE : 
						(playerSide == Piece.BLUE) ? Piece.BLUE : Piece.RED);
				Moves betterMovesFromCurrent = prune(currentAvailableMoves, 
						currentMoveData);
				
				//adds scores upwards on the 'tree'
				nodeScore += analyzeBetterMovesMKII(currentMove, 
						betterMovesFromCurrent, turn+1);//(currentMove.getWasCaptured() ? 
								//turn : turn+1));
			}
		}
		//calculates the goodness of the moves based on how many point you can 
		//capture
		return (turn % 2 == 0) ? -nodeScore : nodeScore;
	}
	
	
	/**
	 * A function to prune the irrelevant moves from the moves where one can 
	 * capture a hex
	 * 
	 * 
	 * @param currentAvailableMoves
	 * @param currentMoveData
	 * @return
	 */
	private Moves prune(Moves currentAvailableMoves, 
			AnalysisData currentMoveData) {
		
		Analyzer analyzer = Analyzer.getAnalyzer();
		Moves betterMovesFromCurrent = new Moves();
		AnalysisData comparingData;
		
		//Prunes the moves, based on whether there is anything to gain.
		for (int move = 0; move < currentAvailableMoves.size(); move++) {
			
			//sets analyzer to analyze the next possible move in available 
			//moves
			analyzer.setBoardState(currentAvailableMoves.get(move).getBoard());
			comparingData = analyzer._analyzeState();
			
			//assumes that both players will do a 'good' move
			/*if ((Integer)comparingData.get(Analyzer.AnalysisData.CAPTUREDHEX) > 
			(Integer)currentMoveData.get(Analyzer.AnalysisData.CAPTUREDHEX)) {
				betterMovesFromCurrent.add(currentAvailableMoves.get(move));
			}*/
			if ((Integer)comparingData.get(Analyzer.AnalysisData.SINGLEMOVECAPTUREHEX) > 
			(Integer)currentMoveData.get(Analyzer.AnalysisData.SINGLEMOVECAPTUREHEX)) {
				continue;
			}
			betterMovesFromCurrent.add(currentAvailableMoves.get(move));

			
		}
		
		return betterMovesFromCurrent;
	}
	
	
	/**
	 * A method to make the next move of the AI
	 */
	public Move makeMove() {
		
		Analyzer analyzer = Analyzer.getAnalyzer();
		analyzer.setBoardState(playerBoard);
		AnalysisData currentStateData = analyzer._analyzeState();
		Moves betterMoves = prune(availableMoves, currentStateData);
		BoardMovePair moveToDo = null;
		
		Moves topMoves = new Moves();
		
		for (BoardMovePair move : betterMoves) {
			updateTopMoves(topMoves,move);
		}
		
		if ((double)availableMoves.size()/this.fullBoardSize >= 0.30 &&
				topMoves.size() > 0) {
			int num = randomGenerator.nextInt(topMoves.size());
			if (num < 0) {
				num = 0;
			}
			else if (num >= topMoves.size()) {
				num = topMoves.size() - 1;
			}
			moveToDo = betterMoves.get(num);
		}
		
		else {
			moveToDo = analyzeBetterMovesRouter(topMoves);
		}
		
		if (moveToDo == null || moveToDo.getMove() == null) {
			int num = randomGenerator.nextInt(availableMoves.size());
			if (num < 0) {
				num = 0;
			}
			else if (num >= availableMoves.size()) {
				num = availableMoves.size() - 1;
			}
			moveToDo = availableMoves.get(num);			
		}	
		
		
		analyzer.setBoardState(playerBoard);
		this.boardData = analyzer._analyzeState();
		
		availableMoves.remove(moveToDo);
		playerBoard.setCharAt(moveToDo.getMove().Row, moveToDo.getMove().Col, 
				playerPiece);
		checkHexCaptured(moveToDo.getMove(), playerSide);
		this.availableMoves = new Moves(playerBoard, playerSide);
		analyzer.setBoardState(playerBoard);
		this.boardData = analyzer._analyzeState();
		
		// LEARNER PHASE ////////////////
		learner.betaFence(moveToDo);
		////////////////////////////////
		
		return moveToDo.getMove();
	}
	
	public void updateTopMoves(Moves topMoves, BoardMovePair move) {
		
		double worstScore = 0;
		double tempScore = 0;
		int worstLocation = topMoves.size();
		int count = 0;
		
		
		// if topMoves is not fully populated yet
		if (topMoves.size() < 4 ) {
			topMoves.add(move);
			return;
		}
		
		worstScore = this.brain.reference(move);
		
		for (BoardMovePair top : topMoves) {
			tempScore = this.brain.reference(top);
			if (worstScore > tempScore) {
				worstScore = tempScore;
				worstLocation = count;
			}
			count++;
		}
		
		if (worstLocation < topMoves.size()) {
			topMoves.remove(worstLocation);
			topMoves.add(move);
		}
		return;
	}
	
	/**
	 * A useful print function
	 * @param o
	 */
	public static void print(Object o) {
		System.out.println(o);
	}
	/*************************************************************************/
	
	
	/**
	 * Checks if a hexagon at a coordinate  has been captured.
	 * 
	 * 
	 * @param move
	 * @param player
	 * @return
	 */
	private Boolean checkHexCaptured(Move move, int player) {
		
		Analyzer analyzer = Analyzer.getAnalyzer();
		Boolean captured = new Boolean(Boolean.FALSE);
		
		//print(move.Row+" " + move.Col);
		if (analyzer.isHexagon(move.Row + 1, move.Col + 1)) {
			if (analyzer.checkLiberties(move.Row + 1, move.Col + 1).size()==0){
				playerBoard.setCharAt(move.Row + 1, move.Col + 1, 
						(player == Piece.BLUE) ? 'b' : 'r');
				
				captured = Boolean.TRUE;
			}
		}
		if (analyzer.isHexagon(move.Row + 1, move.Col)) {
			if (analyzer.checkLiberties(move.Row + 1, move.Col).size() == 0) {
				playerBoard.setCharAt(move.Row + 1, move.Col, 
						(player == Piece.BLUE) ? 'b' : 'r');
				captured = Boolean.TRUE;
			}
		}
		if (analyzer.isHexagon(move.Row - 1, move.Col)) {
			if (analyzer.checkLiberties(move.Row - 1, move.Col).size() == 0) {
				playerBoard.setCharAt(move.Row - 1, move.Col, 
						(player == Piece.BLUE) ? 'b' : 'r');
				captured = Boolean.TRUE;
			}
		}
		if (analyzer.isHexagon(move.Row, move.Col + 1)) {
			if (analyzer.checkLiberties(move.Row, move.Col + 1).size() == 0) {
				playerBoard.setCharAt(move.Row, move.Col + 1, 
						(player == Piece.BLUE) ? 'b' : 'r');
				captured = Boolean.TRUE;
			}
		}
		if (analyzer.isHexagon(move.Row, move.Col - 1)) {
			if (analyzer.checkLiberties(move.Row, move.Col - 1).size() == 0) {
				playerBoard.setCharAt(move.Row, move.Col - 1, 
						(player == Piece.BLUE) ? 'b' : 'r');
				captured = Boolean.TRUE;
			}
		}
		if (analyzer.isHexagon(move.Row - 1, move.Col - 1)) {
			if (analyzer.checkLiberties(move.Row - 1, move.Col - 1).size()==0){
				playerBoard.setCharAt(move.Row - 1, move.Col - 1, 
						(player == Piece.BLUE) ? 'b' : 'r');
				captured = Boolean.TRUE;
			}
		}
		return captured;
	}

	
	/**
	 * This method plays the opponents move onto the local board and checks if
	 * it is a valid move. by checking if the proposed move is 'free' meaning
	 * a '+'. It returns -1 if it is invalid, 0 if it was valid and 1 if the 
	 * move also captured a hexagon.
	 * 
	 * 
	 * @param m The move to make
	 */
	public int opponentMove(Move m) {
		
		//if (m.Row == -1 && m.Col == -1) return 0;
		
		if (playerBoard.getCharAt(m.Row, m.Col) != '+') {
			return -1;
		}
		else {
			Integer oldAvailableHex = 
			   (Integer)this.boardData.get(Analyzer.AnalysisData.AVAILABLEHEX);
			AnalysisData opponentBoardData;
			Analyzer analyzer = Analyzer.getAnalyzer();
			BoardState opponentBoard = playerBoard;
			
			opponentBoard.setCharAt(m.Row, m.Col, opponentPiece);
			//
			this.availableMoves = new Moves(playerBoard, playerSide);

			analyzer.setBoardState(opponentBoard);
			opponentBoardData = analyzer._analyzeState();
			
			checkHexCaptured(m, m.P);
			
			analyzer.setBoardState(playerBoard);
			this.boardData = analyzer._analyzeState();
			playerBoard.setCharAt(m.Row, m.Col, opponentPiece);
			
			
			if ((Integer)opponentBoardData.get
					(Analyzer.AnalysisData.AVAILABLEHEX) < oldAvailableHex) {
				return 1;
			}
			else {
				return 0;
			}
			
		}
	}
	/*************************************************************************/


	/**
	 * A method that checks the board if it is in a terminal state. If it is, 
	 * the method will return who the winner is, based on the local copy of the
	 * board. It will then record the findings from the game into the brain
	 * file.
	 * <p>
	 * The method just checks if there are no more pieces to be played.
	 */
	public int getWinner() {
		int[] RBHex;
		
		// if there are still available hexes
		Analyzer analyzer = Analyzer.getAnalyzer();
		AnalysisData thisBoardData;
		analyzer.setBoardState(playerBoard); 
		thisBoardData = analyzer._analyzeState();
		RBHex = analyzer.analyzeEndGame();

		if( (Integer)thisBoardData.get(Analyzer.AnalysisData.TOTALMOVES) != 0){
			return Piece.EMPTY;
		}
		
		int winner = (RBHex[0] > RBHex[1]) ? Piece.RED : Piece.BLUE;
		
		learner.WriteToFile(SuryadiAI.DATABASE, (winner == this.playerSide));
		
		return (RBHex[0] == RBHex[1]) ? Piece.DEAD : winner;
		
	}
	/*************************************************************************/

	
	/**
	 * This prints the local board state 
	 */
	public void printBoard(PrintStream output) {
		for(int i = 0; i < playerBoard.size(); i++) {
			output.println(playerBoard.get(i));
		}
	}
	/*************************************************************************/

	
	
}
