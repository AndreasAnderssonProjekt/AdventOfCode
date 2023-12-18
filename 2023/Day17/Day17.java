package AdventOfCode;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;



public class Day17 {

	private static int[][] board;
	private static int[][] dist;
	private static PriorityQueue<Crucible> q;
	private static HashSet<Crucible> settled;
	
	public class CrucibleComparator implements Comparator<Crucible>{
		@Override 
		public int compare(Crucible c1, Crucible c2) {
			if(c1.getCost() < c2.getCost()) return -1;
			else if(c1.getCost() == c2.getCost()) return 0;
			else return 1;
		}
	}
	
	public class Crucible implements Comparable<Crucible> {
		private int row;
		private int col;
		private Direction dir;
		private int sameDir = 0;
		private int cost;
		
		public Crucible(int row, int col, int cost) {
			this.row = row;
			this.col = col;
			this.cost = cost;
			this.dir = null;
			
			
		}
		public Crucible(int row, int col, int cost, Direction dir, int sameDir) {
			this.row = row;
			this.col = col;
			this.cost = cost;
			this.dir = dir;
			this.sameDir = sameDir;
		}
		
		public int getRow() {
			return this.row;
		}
		
		public int getCol() {
			return this.col;
		}
		
		public int getCost() {
			return this.cost;
		}
		
		public void setCost(int cost) {
			this.cost = cost;
		}
		
		public int getSameDir() {
			return this.sameDir;
		}
		
		public Direction getDir() {
			return this.dir;
		}
		
		@Override
		public boolean equals(Object c) {
			return(this.getRow() == ((Day17.Crucible) c).getRow() && this.getCol() == ((Day17.Crucible) c).getCol() && this.getDir() == ((Day17.Crucible) c).getDir() && this.getSameDir() == ((Day17.Crucible) c).getSameDir() );
		}
		
		@Override
		public int hashCode() {
			int[] dirs = dirNumber(this.dir);
			return (this.row * board.length + this.col) * 9 + (dirs[0]+1) * 3 + (dirs[1] + 1) + (this.sameDir + 1)* 18;
			
		}
		
		@Override
		public int compareTo(Crucible c) {
			if(this.getCost() < c.getCost()) return -1;
			else if(this.getCost() == c.getCost()) return 0;
			else return 1;
			
		}
		
		
		
		
	}
	
	public enum Direction{
		UP,
		RIGHT,
		DOWN,
		LEFT;
	}
	
	
	public static void main(String[] args) throws FileNotFoundException{
		File file = new File("C:\\Users\\Andre\\eclipse-workspace\\AdventOfCOde\\src\\testExempel");
		Scanner sc = new Scanner(file);
		ArrayList<String> lines = new ArrayList<>();
		while(sc.hasNext()) {
			String line = sc.nextLine();
			lines.add(line);
		}
		sc.close();
		
		board = new int[lines.size()][lines.get(0).length()];

		for(int i = 0; i < lines.size(); i++) {
			for(int j = 0; j < lines.get(0).length(); j++) {
				board[i][j] = lines.get(i).charAt(j) - '0';
			}
		}
		
		Day17 d = new Day17();
		System.out.println("Part 1: "  + d.dijkstra(0, 0, board.length - 1, board[0].length - 1, true));
		System.out.println("Part 2: "  + d.dijkstra(0, 0, board.length - 1, board[0].length - 1, false));
		
		
	}
	
	
	private int dijkstra(int startRow, int startCol, int endRow, int endCol, boolean part1) {
		int m = board.length;
		int n = board[0].length;
		
		
		dist = new int[m][n];
		
		for(int i = 0; i < m; i++) {
			for(int j = 0; j < n; j++) {
				dist[i][j] = Integer.MAX_VALUE;
			}
		}
		dist[startRow][startCol] = 0;
		
		q = new PriorityQueue<>();
		q.add(new Crucible(startRow, startCol, 0, null, 0));
		settled = new HashSet<>();
		
		while(!q.isEmpty()) {
			Crucible c = q.poll();
			
			if(settled.contains(c)) {
				
				continue;
			}
			
			settled.add(c);
			
			addNeighbours(c, part1);
		}
		
		for(int i = 0; i < dist.length; i++) {
			for(int j = 0; j < dist[0].length; j++) {
				System.out.print(" " + dist[i][j]);
			}
			System.out.println();
		}
		return dist[endRow][endCol];
 	}
	
	
	public void addNeighbours(Crucible c, boolean part1) {
		int row = c.getRow();
		int col = c.getCol();
		Direction dir = c.getDir();
		
		if(row > 0 && dir != Direction.DOWN) {
			addNext(row-1,col, Direction.UP, c, part1);
		}
		
		if(col > 0 && dir != Direction.RIGHT) {
			addNext(row,col-1, Direction.LEFT, c, part1);
		}
		
		if(row < board.length - 1 && dir != Direction.UP) {
			addNext(row+1,col, Direction.DOWN, c, part1);
		}
		
		if(col < board[0].length - 1 && dir != Direction.LEFT) {
			addNext(row,col+1, Direction.RIGHT, c, part1);
		}
		
		
	}
	
	public void addNext(int nextRow, int nextCol, Direction nextDir, Crucible prev, boolean part1) {
		Direction prevDir = prev.getDir();
		int sameDir = prev.getSameDir();
		int altDist = prev.getCost() + board[nextRow][nextCol];
		int minSame;
		int maxSame;
		
		if(part1) {
			minSame = 0;
			maxSame = 3;
		}
		else {
			minSame = 4;
			maxSame = 10;
		}
		
		if(prevDir == nextDir) {
			if(sameDir < maxSame ) {
				Crucible other = new Crucible(nextRow, nextCol, altDist, nextDir, sameDir +1);
				if(!settled.contains(other)) {
					if(dist[nextRow][nextCol] >= altDist) dist[nextRow][nextCol] = altDist;
					q.add(other);
				}
				
			}
		}
		else {
			if(prevDir != null && sameDir < minSame) return;
			Crucible other = new Crucible(nextRow, nextCol, altDist, nextDir, 1);
			if(!settled.contains(other)) {
				if(dist[nextRow][nextCol] > altDist) dist[nextRow][nextCol] = altDist;
				q.add(other);
			}
		}
	}
	
	public int[] dirNumber(Direction dir) {
		if(dir == null) return new int[] {0, 0};
		switch(dir) {
		case UP : return new int[] {0, -1};
		case DOWN : return new int[] {0, 1};
		case LEFT : return new int[] {-1, 0};
		case RIGHT  : return new int[] {1, 0};
		default : return new int[] {0, 0};
		}
		
	}
	

}
