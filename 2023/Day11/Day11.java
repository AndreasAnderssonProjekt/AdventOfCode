import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

class Day11Final{

private class Galaxy{
		private int row;
		private int col;
		
		public Galaxy(int row, int col) {
			this.row = row;
			this.col = col;
		}
		
		public int getRow() {
			return this.row;
		}
		
		public int getCol() {
			return this.col;
		}
		
		public int distTo(Galaxy g) {
			return Math.abs(this.row - g.row) + Math.abs(this.col - g.col);
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException{
		File file = new File("C:\\Users\\Andre\\eclipse-workspace\\Advent\\src\\testExempel");
		Scanner sc = new Scanner(file);
		
		ArrayList<String> lines = new ArrayList<>();
		
		while(sc.hasNext()) {
			String line = sc.nextLine();
			lines.add(line);
		}
		sc.close();
		
		Day11Final d = new Day11Final();
		char[][] universe = d.convertToCharMatrix(lines);
		
		
		
		int stepSize = 1;
		ArrayList<int[]> jumps = d.jumps(universe, stepSize);
		long dist = d.shortestPairDistances(universe, jumps);
		ArrayList<Galaxy> galaxies = d.findGalaxies(universe);
		System.out.println("Part 1: " + dist);
		stepSize = 1000000 - 1;
		
		
		jumps = d.jumps(universe, stepSize);
		dist = d.shortestPairDistances(universe, jumps);
		System.out.println("Part 2: " + dist);
		
		

	}
	
	public long shortestPairDistances(char[][] galaxy, ArrayList<int[]> jumps) {
		long dist = 0;
		ArrayList<Galaxy> galaxies = findGalaxies(galaxy);
		for(int i = 0; i < galaxies.size(); i++) {
			for(int j = i; j < galaxies.size(); j++) {
				Galaxy g1 = galaxies.get(i);
				Galaxy g2 = galaxies.get(j);
				int row1 = g1.getRow();
				int row2 = g2.getRow();
				int col1 = g1.getCol();
				int col2 = g2.getCol();
				int[] rowsToExpand = jumps.get(0);
				int[] colsToExpand = jumps.get(1);
				dist += galaxies.get(i).distTo(galaxies.get(j)) + (Math.abs(rowsToExpand[row2] - rowsToExpand[row1]) + Math.abs(colsToExpand[col2] - colsToExpand[col1]));
			}
		}
		return dist;
		
	}
	
	
	
	public  ArrayList<Galaxy> findGalaxies(char[][] galaxy){
		ArrayList<Galaxy> galaxies = new ArrayList<>();
		for(int i = 0; i < galaxy.length; i++) {
			for(int j = 0; j < galaxy[0].length; j++) {
				if(galaxy[i][j] == '#') galaxies.add(new Galaxy(i,j)) ;
			}
		}
		return galaxies;
	}
	
	public char[][] convertToCharMatrix(ArrayList<String> lines){
		char[][] m = new char[lines.size()][lines.get(0).length()];
		for(int i = 0; i < lines.size(); i++) {
			for(int j = 0; j < lines.get(i).length();j++) {
				m[i][j] = lines.get(i).charAt(j);
			}
		}
		return m;
	}
	
	
	public ArrayList<int[]> jumps(char[][] galaxy, int stepSize){
		int[] rowsToExpand = new int[galaxy.length];
		int[] colsToExpand = new int[galaxy[0].length];
		
		int nrRows = 0;
		for(int i = 0; i < galaxy.length; i++) {
			if(isEmpty(galaxy[i])) {
				nrRows += stepSize;
			}
			rowsToExpand[i] = nrRows;
		}
		
		
		int nrCols = 0;
		for(int i = 0; i < galaxy[0].length; i++) {
			char[] col = new char[galaxy.length];
			for(int j = 0; j < galaxy.length; j++) {
				col[j] = galaxy[j][i];
			}
			if(isEmpty(col)) {
				nrCols += stepSize;
			}
			colsToExpand[i] = nrCols;
			
		}
		
		ArrayList<int[]> jumps = new ArrayList<>();
		jumps.add(rowsToExpand);
		jumps.add(colsToExpand);
		return jumps;
		
	}
	
	public boolean isEmpty(char[] line) {
		for(int i = 0; i < line.length; i++) if(line[i] != '.') return false;
		return true;
	}

}
