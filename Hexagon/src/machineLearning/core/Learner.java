package machineLearning.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import aiproj.util.BoardMovePair;
import machineLearning.interfaces.ILearner;
import machineLearning.util.MoveCatalogue;

public class Learner implements ILearner{

	
	/** Integer array of size 3 to hold move category and number of instances, [1st, 2nd, noInstance] */
	private MoveCatalogue moveCategories;
	
	
	
	public Learner () {
		moveCategories = new MoveCatalogue();
	}
	
	/**
	 * Takes a move 
	 * @param move
	 * @return
	 */
	private void moveLogger(int[] moveCategory) {
		
		moveCategories.add(moveCategory);
		
	}

	
	
	
	
	
	public void betaFence(BoardMovePair move) {
		
		moveLogger(MoveCatalogue.categorizeMove(move));
		
	}


	@Override
	public void WriteToFile(String filename, boolean isWin) {
		
		
		MoveCatalogue buffer = new MoveCatalogue();
		
		try {
			
			File file = new File(filename);
			
			if(!file.exists()) {
			    file.createNewFile();
			} 
			
			
			for (String line : Files.readAllLines(Paths.get(filename))) {
				
				Integer data[] = {0,0,0};
				int i = 0;
				
				for (String part : line.split(",")) {
					data[i] = Integer.valueOf(part);
					i++;
				}
				i = 0;
				
				buffer.add(data);
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int winMultiplyer = 1;
		
		if (!isWin) {
			winMultiplyer = -1;
		}
		
		for (Integer[] data : this.moveCategories) {
			
			int[] dataBuffer = {0,0,0};
			
			dataBuffer[0] = (int)data[0];
			dataBuffer[1] = (int)data[1];
			dataBuffer[2] = (int)data[2] * winMultiplyer;
			
			buffer.add(dataBuffer);
		}
		
		try {
			PrintWriter writer = new PrintWriter(filename);
			
			for (Integer[] data : buffer) {
				writer.print(data[0] + ",");
				writer.print(data[1] + ",");
				writer.println(data[2]);
			}
			
			writer.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
	}
	

}
