package aiproj.util;

import java.util.ArrayList;
 
public class Move{
	
	public int P;
	public int Row;
	public int Col;	
	
	//public HexCoordinates[] ajacent;
	
	public Move() {
		
	}
	
	
	
	public Move(int row, int col, int p) {
		this.Row = row;
		this.Col = col;
		this.P = p;
	}
	
	public void setP(int P){
		this.P = P;
	}
	
	public ArrayList<HexCoordinates> getAjacent() {
		
		ArrayList<HexCoordinates> ajacent = new ArrayList<HexCoordinates>(0);
		
		Analyzer analyzer = Analyzer.getAnalyzer();
		
		if (analyzer.isHexagon(this.Row + 1, this.Col + 1)) {
			ajacent.add(new HexCoordinates(this.Row + 1, this.Col + 1));
		}
		if (analyzer.isHexagon(this.Row, this.Col + 1)) {
			ajacent.add(new HexCoordinates(this.Row, this.Col + 1));
		}
		if (analyzer.isHexagon(this.Row + 1, this.Col)) {
			ajacent.add(new HexCoordinates(this.Row + 1, this.Col));
		}
		if (analyzer.isHexagon(this.Row - 1, this.Col - 1)) {
			ajacent.add(new HexCoordinates(this.Row - 1, this.Col - 1));
		}
		if (analyzer.isHexagon(this.Row, this.Col - 1)) {
			ajacent.add(new HexCoordinates(this.Row, this.Col - 1));
		}
		if (analyzer.isHexagon(this.Row - 1, this.Col)) {
			ajacent.add(new HexCoordinates(this.Row - 1, this.Col));		
		}
		
		return ajacent;
	}
	
}
