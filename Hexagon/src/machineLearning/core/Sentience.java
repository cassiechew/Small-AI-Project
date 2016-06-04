package machineLearning.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import aiproj.util.BoardMovePair;
import machineLearning.interfaces.ISentience;
import machineLearning.util.MoveCatalogue;

public class Sentience implements ISentience{

	MoveCatalogue catalogue;
	
	public Sentience () {
		this.catalogue = new MoveCatalogue();
	}
	
	public boolean init (String filename) {
		try {
			
			File file = new File(filename);
			
			if(!file.exists()) {
				throw new IOException();
			} 
			
			for (String line : Files.readAllLines(Paths.get(filename))) {
				Integer data[] = {0,0,0};
				int i = 0;
				
				for (String part : line.split(",")) {
					data[i] = Integer.valueOf(part);
					i++;
				}
				i = 0;
				
				catalogue.add(data);
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public MoveCatalogue getCatalogue() {
		return this.catalogue;
	}
	
	

	public double reference(BoardMovePair move) {
		
		int[] data;
		Integer[] referenceData;
		data = MoveCatalogue.categorizeMove(move);
		
		referenceData = catalogue.get(data);
		
		
		return referenceData[2];
	}

	
}
