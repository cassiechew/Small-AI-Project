package machineLearning.util;

import java.util.ArrayList;

import aiproj.util.BoardMovePair;
import aiproj.util.HexCoordinates;


public class MoveCatalogue extends ArrayList<Integer[]>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8813981021616564142L;

	public void add(int[] category) {
		
		for (Integer[] entry : this) {
			if (entry[0] == category[0] && entry[1] == category[1]) {
				
				entry[2] += category[2];
				return;
			}
		}
		
		Integer[] newEntry = {category[0], category[1], 1};
		
		this.add(newEntry);
		
	}
	
	public Integer[] get(int[] category) {
		
		
		for (Integer[] entry : this) {
			if (entry[0] == category[0] && entry[1] == category[1]) {

				return entry;
				
			}			
		}
		Integer[] empty = {0,0,0};
		
		return empty;
	}
	
	public static int[] categorizeMove(BoardMovePair movePair) {
		
		movePair.getBoard().setCharAt(movePair.getMove().Row, movePair.getMove().Col, '+');
		
		ArrayList<HexCoordinates> ajacents = movePair.getMove().getAjacent();
		int[] category = {0,0,0};
		int[] temp = {0,0,0};
		int i = 0;
		for (HexCoordinates hex : ajacents) {
			temp[i] = hex.checkAjacency(movePair.getBoard());
			i++;
		}
		
		if (temp[0] < temp[1]) {
			temp[2] = 1;
			return temp;
		}
		else {
			category[1] = temp[0];
			category[0] = temp[1];
			category[2] = 1;
			return category;
		}
	}
		
}
