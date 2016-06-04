package machineLearning.interfaces;

import aiproj.util.BoardMovePair;

public interface ISentience {

	public boolean init (String filename);
	public double reference(BoardMovePair move);

	
}
