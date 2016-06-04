package machineLearning.interfaces;


import aiproj.util.BoardMovePair;

public interface ILearner {
	
	public void betaFence(BoardMovePair move);
	public void WriteToFile(String filename, boolean isWin);

}

