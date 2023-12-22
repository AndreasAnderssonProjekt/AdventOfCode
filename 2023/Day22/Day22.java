package AdventOfCode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

public class Day22 {

	private class Brick implements Comparable<Brick>{
		private int[] start;
		private int[] end;
		private int brickNr;
		
		public Brick(int[] start, int[] end, int brickNr) {
			this.start = start;
			this.end = end;
			this.brickNr = brickNr;
		}
		
		public int[] getStart() {
			return this.start;
		}
		
		public int[] getEnd() {
			return this.end;
		}
		
		public int getBrickNr() {
			return this.brickNr;
		}

		@Override
		public int compareTo(Brick b) {
			if(this.start[2] < b.getStart()[2]) return -1;
			else if (this.start[2] > b.getStart()[2]) return 1;
			return 0;
		}
	}
	
	private class Point{
		private int x;
		private int y;
		
		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public int getX() {
			return this.x;
		}
		
		public int getY() {
			return this.y;
		}
		
		@Override
		public boolean equals(Object o) {
			return(this.x == ((Point) o).getX() && this.y == ((Point) o).getY());
			
		}
		
		@Override 
		public int hashCode() {
			int hash = 7;
			hash = 71 * hash + this.x;
			hash = 71 * hash + this.y;
			return hash;
		}
		
		@Override
		public String toString() {
			return Arrays.toString(new int[] {this.x, this.y});
		}
	}
	
	
	private static int[][][] board;
	public static void main(String[] args) throws FileNotFoundException {
		File file = new File("C:\\Users\\Andre\\eclipse-workspace\\AdventOfCOde\\src\\testExempel");
		Scanner sc = new Scanner(file);
		Queue<Brick> bricks = new PriorityQueue<>();
		Map<Point, Integer> lowest = new HashMap<>(); //Key = Point (x,y) , Value = the lowest available z for that point.
		Day22 d = new Day22();
		int[] largestCoords = new int[3];
		
		int brickNr = 1;
		while(sc.hasNext()) {
			String line = sc.nextLine();
			String[] components = line.split("~");
			String[] coord1 = components[0].split(",");
			String[] coord2 = components[1].split(",");
			int[] start = new int[coord1.length];
			int[] end = new int[coord2.length];
			
			for(int i = 0; i < coord1.length; i++) {
				int n1 = Integer.parseInt(coord1[i]);
				int n2 = Integer.parseInt(coord2[i]);
				
				start[i] = n1;
				end[i] = n2;
				if(end[i] > largestCoords[i]) largestCoords[i] = end[i];
			}
			bricks.add(d.new Brick(start, end, brickNr));
			brickNr += 1;
		}
		sc.close();

		
		board = new int[largestCoords[0]+1][largestCoords[1]+1][largestCoords[2]+1];
		
		
		//Initially the lowest point possible is one step above ground level.
		for(int x = 0; x <= largestCoords[0]; x++) {
			for(int y = 0; y <= largestCoords[1]; y++) {
				for(int z = 0; z <= largestCoords[2]; z++)
				lowest.put(d.new Point(x,y), 1);
			}
		}
		
		//Let all bricks fall.
		while(!bricks.isEmpty()) {
			Brick b = bricks.poll();
			int[] start = b.getStart();
			int[] end = b.getEnd();
			int z = -1;
			//Compute the lowest available height for the current brick.
			for(int x = start[0]; x <= end[0]; x++) {
				for(int y = start[1]; y <= end[1]; y++) {
					Point point = d.new Point(x, y);
					if(lowest.get(point) > z) z = lowest.get(point);
				}
			}
			
			//Place brick at the lowest available height.
			for(int x = start[0]; x <= end[0]; x++) {
				for(int y = start[1]; y <= end[1]; y++) {
					lowest.put(d.new Point(x,y), z + end[2] - start[2] + 1);
				}
			}
			
			//Place brick on board.
			for(int x = start[0]; x <= end[0]; x++) {
				for(int y = start[1]; y <= end[1]; y++) {
					for(int zCord = z; zCord <= z +(end[2] - start[2]); zCord++)
						board[x][y][zCord] = b.getBrickNr();
			}
		}
		}

		Map<Integer, ArrayList<Integer>> supportedBy = supportedBy(); //Key = name of brick. Value = the bricks that supports the key brick.
		Map<Integer, ArrayList<Integer>> supports = supports(); //Key = name of brick. Value = the bricks that are supported by the key brick.
		Map<Integer, Integer> desintegrate = new HashMap<>(); //Key = name of brick (only those who cause others to fall). Value = bricks that fall by removing the key brick.
		ArrayList<Integer> canRemove = new ArrayList<>(); //The bricks that can be removed without causing any falls.
		
		//Determine which bricks causes others to fall and which does not affect other bricks.
		for(Integer key : supportedBy.keySet()) {
			ArrayList<Integer> suppBy = supportedBy.get(key);
			if(suppBy.size() > 1) {
				 for(int i = 0; i < suppBy.size(); i++) { //Check if any of the bricks supported by the key brick is a lone supporter of any other brick.
					 boolean safe = true;
					 int supportBrick = suppBy.get(i);
					 for(int j = 0; j < supports.get(supportBrick).size(); j++) {
						 int otherSupportedBrick = supports.get(supportBrick).get(j);
						 if(supportedBy.get(otherSupportedBrick).size() == 1 && otherSupportedBrick != key) { //Brick supported by the key is a lone supporter of another brick.
							 safe = false;
						 }
					 }
					 if(!canRemove.contains(supportBrick)) {
						 if(safe) canRemove.add(supportBrick);
						 else desintegrate.put(supportBrick, 0);
					 }
				 }
			}
		}
		
		//Add the bricks at the top.
		for(Integer key : supports.keySet()) {
			if(!canRemove.contains(key)) {
				if(supports.get(key).size() == 0) canRemove.add(key);
				else desintegrate.put(key, 0);
			}
		}
		
		System.out.println("Part 1: " + canRemove.size());
		System.out.println("Part 2: " + totDesintegration(desintegrate, supportedBy, supports));
	}
	
	public static int totDesintegration(Map<Integer, Integer> desintegrate, Map<Integer, ArrayList<Integer>> supportedBy, Map<Integer, ArrayList<Integer>> supports) {
		int tot = 0;
		for(int key : desintegrate.keySet()) tot += nrDesintegrate(key, supportedBy, supports);
		return tot;
	}
	
	public static int nrDesintegrate(int brick, Map<Integer, ArrayList<Integer>> supportedBy, Map<Integer, ArrayList<Integer>> supports) {
		ArrayList<Integer> removed = new ArrayList<>();
		removed.add(brick);
		Queue<Integer> potentialVictims = new LinkedList<>();
		
		for(int b : supports.get(brick)) {
			potentialVictims.add(b);
		}
		//Cascade through the bricks connected to the brick and check whether they fall.
		while(!potentialVictims.isEmpty()) {
			int victim = potentialVictims.poll();
			if(isSubset(supportedBy.get(victim),removed)) {
				if(!removed.contains(victim)) {
					removed.add(victim);
					for(int b : supports.get(victim)) {
						potentialVictims.add(b);
					}
				}

			}
		}
		
		return removed.size() - 1;
	}
	
	public static boolean isSubset(ArrayList<Integer> l1, ArrayList<Integer> l2) {
		for(int i : l1) {
			if(!l2.contains(i)) return false;
		}
		return true;
	}
	
	//Creates a Map where Key : brick name, Value: the bricks supports the key brick.
	public static Map<Integer, ArrayList<Integer>> supportedBy() {
		Map<Integer, ArrayList<Integer>> supportedBy = new HashMap<>();
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				for(int k = 0; k < board[0][0].length; k++) {
					if(board[i][j][k] != 0 && board[i][j][k-1] != 0 && board[i][j][k] != board[i][j][k-1]) {
						if(supportedBy.containsKey(board[i][j][k])){
							if(supportedBy.get(board[i][j][k]).contains(board[i][j][k-1])) continue;
							else supportedBy.get(board[i][j][k]).add(board[i][j][k-1]);
						}
						else {
							ArrayList<Integer> list = new ArrayList<>();
							list.add(board[i][j][k-1]);
							supportedBy.put(board[i][j][k], list);
						}
					}
				}
			}
		}
		return supportedBy;
	}
	
	//Creates a Map where Key : brick name, Value: the bricks supported by the key brick.
	public static Map<Integer, ArrayList<Integer>> supports() {
		Map<Integer, ArrayList<Integer>> supports = new HashMap<>();
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				for(int k = 0; k < board[0][0].length - 1; k++) {
					int brick = board[i][j][k];
					if(brick != 0 && !supports.containsKey(brick)) supports.put(brick, new ArrayList<Integer>());
					if(brick != 0 && board[i][j][k+1] != 0 && brick != board[i][j][k+1]) {
						if(!supports.get(brick).contains(board[i][j][k+1])) supports.get(brick).add(board[i][j][k+1]);
						
					}
					}
				}
			}
		
		return supports;
	}
	
}

