package aiproj.util;

public class BoardMovePair{
	
	private BoardState boardState;
	private Move move;
	private Double score;
	private Boolean wasCaptured;
	
	public BoardMovePair (BoardState boardState, Move move) {
		this.boardState = boardState;
		this.move = move;
		this.score = 0.;
		this.wasCaptured = false;
	}
	
	public BoardState getBoard() {
		return boardState;
	}
	
	public Move getMove() {
		return move;
	}
	
	public Double getScore() {
		return score;
	}
	
	public void setScore(Double score) {
		this.score = score;
	}
	
	
	public Boolean getWasCaptured() {
		return wasCaptured;
	}
	
	public void setWasCaptured(Boolean wasCaptured) {
		this.wasCaptured = wasCaptured;
	}
}
