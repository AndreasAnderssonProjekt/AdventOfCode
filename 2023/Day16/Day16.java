import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Day16 {

	public class Beam{
		private int row;
		private int col;
		private Direction dir;
		public Beam(int row, int col, Direction dir) {
			this.row = row;
			this.col = col;
			this.dir = dir;
		}
		
		public int getRow() {
			return this.row;
		}
		
		public int getCol() {
			return this.col;
		}
		
		public Direction getDir() {
			return this.dir;
		}
		
		public void setDir(Direction dir) {
			this.dir = dir;
		}
		
		public boolean canMove(char[][] board) {
			int nrRows = board.length;
			int nrCols = board[0].length;
			switch(this.dir) {
			case UP:
				if(this.row > 0) {
					return true;
				}
				return false;
				
			case RIGHT:
				if(this.col < nrCols - 1) {
					return true;
				}
				return false;
				
			case DOWN:
				if(this.row < nrRows - 1) {
					return true;
				}
				return false;
				
			case LEFT: 
				if(this.col > 0) {
					return true;
				}
				return false;
			default:
				return false;
			}
		}
		
		public void move(char[][] board) {
			if(canMove(board)) {
				switch(this.dir) {
				case UP:
					this.row -= 1;
					break;
				case RIGHT:
					this.col += 1;
					break;
				case DOWN: 
					this.row += 1;
					break;
				default:
					this.col -= 1;
					break;
				
				}
			}
		}
		
	}
	
	private enum Direction{
		UP,
		RIGHT,
		DOWN,
		LEFT
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		File file = new File("C:\\Users\\Andre\\eclipse-workspace\\Advent\\src\\testExempel");
		Scanner sc = new Scanner(file);
		
		ArrayList<String> lines = new ArrayList<>();
		while(sc.hasNext()) {
			String line = sc.nextLine();
			lines.add(line);
		}
		sc.close();
		
		char[][] board = new char[lines.size()][lines.get(0).length()];
		
		for(int row = 0; row < board.length; row++) {
			for(int col = 0; col < board[0].length; col++) {
				board[row][col] = lines.get(row).charAt(col);
			}
		}
		
		Day16 d = new Day16();
		
		
		
		char[][] boardCopy = d.copyBoard(board);
		
		char[][] energyBoard = d.launchBeam(boardCopy, 0, 0, Direction.RIGHT);
		for(int row = 0; row < board.length; row++) {
			for(int col = 0; col < board[0].length; col++) {
				System.out.print(energyBoard[row][col]);
			}
			System.out.println();
		}
		System.out.println("Part 1: " + d.countEnergized(energyBoard));
		
		
		
		
		int maxEnergy = 0;

		for(int startRow = 0; startRow < board.length; startRow++) {
			boardCopy = d.copyBoard(board);
			energyBoard = d.launchBeam(boardCopy, startRow, 0, Direction.RIGHT);
			maxEnergy = Math.max(maxEnergy, d.countEnergized(energyBoard));
			
			boardCopy = d.copyBoard(board);
			energyBoard = d.launchBeam(boardCopy, startRow, boardCopy[0].length - 1, Direction.LEFT);
			maxEnergy = Math.max(maxEnergy, d.countEnergized(energyBoard));
		}
		
		for(int startCol = 0; startCol < board[0].length; startCol++) {
			boardCopy = d.copyBoard(board);
			energyBoard = d.launchBeam(boardCopy, 0, startCol, Direction.DOWN);
			maxEnergy = Math.max(maxEnergy, d.countEnergized(energyBoard));
			
			boardCopy = d.copyBoard(board);
			energyBoard = d.launchBeam(boardCopy, board.length - 1, startCol, Direction.UP);
			maxEnergy = Math.max(maxEnergy, d.countEnergized(energyBoard));
		}
		
		System.out.println("Part 2: " + maxEnergy);
		
	}
	
	
	
	public char[][] copyBoard(char[][] board){
		char[][] boardClone = new char[board.length][];
		for(int i = 0; i < board.length; i++) boardClone[i] = board[i].clone();
		return boardClone;
	}
	
	public int countEnergized(char[][] board) {
		int sum = 0;
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				if(board[i][j] == '#') sum += 1;
			}
		}
		return sum;
	}
	public char[][] launchBeam(char[][] board, int startRow, int startCol, Direction startDir) {
		char[][] energy = new char[board.length][board[0].length];
		Queue<Beam> q = startBeams(board, startRow, startCol, startDir);
		energy[startRow][startCol]  = '#';
		
		while(!q.isEmpty()) {
			Beam beam = q.poll();
			while(beam.canMove(board)) {
				fillTrack(board, beam);
				beam.move(board);
				int row = beam.getRow();
				int col = beam.getCol();
				
				if(board[row][col] == '<' || board[row][col] == '>' || board[row][col] == '^' || board[row][col] == 'v') {
					if(board[row][col] == '<' && beam.getDir() == Direction.LEFT) break;
					else if(board[row][col] == '>' && beam.getDir() == Direction.RIGHT) break;
					else if(board[row][col] == '^' && beam.getDir() == Direction.UP) break;
					else if(board[row][col] == 'v' && beam.getDir() == Direction.DOWN) break;
				}
				
		
				
				
				
				row = beam.getRow();
				col = beam.getCol();
				
				
				energy[row][col]  = '#';
				
				
				
				
				
				
				//Split case 1.
				if(board[row][col] == '-') {
					if(beam.getDir() == Direction.UP || beam.getDir() == Direction.DOWN) {
						q.add(new Beam(row, col, Direction.LEFT));
						q.add(new Beam(row, col, Direction.RIGHT));
						energy[row][col]  = '#';
						break;
					}
				}
				
				//Split case 2.
				else if(board[row][col] == '|') {
					if(beam.getDir() == Direction.LEFT || beam.getDir() == Direction.RIGHT) {
						System.out.println("SPLIT");
						q.add(new Beam(row, col, Direction.UP));
						q.add(new Beam(row, col, Direction.DOWN));
						energy[row][col]  = '#';
						break;
					}
				}
				
				// 90-degree turn case 1.
				else if(board[row][col] == '/') {
					if(beam.getDir() == Direction.LEFT) beam.setDir(Direction.DOWN);
					else if(beam.getDir() == Direction.RIGHT) beam.setDir(Direction.UP);
					else if(beam.getDir() == Direction.UP) beam.setDir(Direction.RIGHT);
					else beam.setDir(Direction.LEFT);
					
					energy[row][col]  = '#';
				}
				
				//90-degree turn case 2.
				else if(board[row][col] == '\\'){
					if(beam.getDir() == Direction.LEFT) beam.setDir(Direction.UP);
					else if(beam.getDir() == Direction.RIGHT) beam.setDir(Direction.DOWN);
					else if(beam.getDir() == Direction.UP) {
						
						beam.setDir(Direction.LEFT);
					}
					else beam.setDir(Direction.RIGHT);
					
					energy[row][col]  = '#';
			}
			
				
			
				
		}
		}
		return energy;
	}
	
	public void fillTrack(char[][] board, Beam beam) {
		int row = beam.getRow();
		int col = beam.getCol();
        if(board[row][col] == '.') {
			switch(beam.getDir()) {
			case UP:
				board[row][col] = '^';
				break;
			case RIGHT:
				board[row][col] = '>';
				break;
			case DOWN:
				board[row][col] = 'v';
				break;
			default:
				board[row][col] = '<';
			}
		}
	}
	
	public Queue<Beam> startBeams(char[][] board, int row, int col, Direction dir) {
		Queue<Beam> q = new LinkedList<>();
		if(board[row][col] == '.') q.add(new Beam(row,col,dir));
		else if(board[row][col] == '|' && (dir == Direction.RIGHT || dir == Direction.LEFT)) {
			q.add(new Beam(row,col,Direction.UP));
			q.add(new Beam(row,col,Direction.DOWN));
		}
		else if(board[row][col] == '-' && (dir == Direction.UP || dir == Direction.DOWN)) {
			q.add(new Beam(row,col,Direction.LEFT));
			q.add(new Beam(row,col,Direction.RIGHT));
		}
		else if(board[row][col] == '/') {
			if(dir == Direction.DOWN) q.add(new Beam(row,col,Direction.LEFT));
			else if(dir == Direction.UP) q.add(new Beam(row,col,Direction.RIGHT));
			else if(dir == Direction.LEFT) q.add(new Beam(row,col,Direction.DOWN));
			else q.add(new Beam(row,col,Direction.UP));
		}
		else if(board[row][col] == '\\') {
			if(dir == Direction.DOWN) q.add(new Beam(row,col,Direction.RIGHT));
			else if(dir == Direction.UP) q.add(new Beam(row,col,Direction.LEFT));
			else if(dir == Direction.LEFT) q.add(new Beam(row,col,Direction.UP));
			else q.add(new Beam(row,col,Direction.DOWN));
		}
		return q;
	}
	
	

}
