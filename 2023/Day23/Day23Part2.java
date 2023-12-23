package AdventOfCode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

public class Day23Part2 {

	private class Node implements Comparable<Node>{
		private int row;
		private int col;
		private int cost;
		
		
		public Node(int row, int col, int cost) {
			this.row = row;
			this.col = col;
			this.cost = cost;
			
		}
		
		public int getCol() {
			return this.col;
		}
		
		public int getRow() {
			return this.row;
		}
		
		public int getCost() {
			return this.cost;
		}
		
		
		@Override
		public int compareTo(Node o) {
			if(this.getCost() > o.getCost()) return -1;
			else if(this.getCost() < o.getCost()) return 1;
			return 0;
		}
		
		@Override
		public boolean equals(Object o) {
			return (this.getRow() == ((Node) o).getRow() && this.getCol() == ((Node) o).getCol());
		}
		
		@Override
		public int hashCode() {
			return(this.row * 71 + this.col) * 9;
		}
		
		@Override
		public String toString() {
			return Arrays.toString(new int[] {this.row, this.col});
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		File file = new File("C:\\Users\\Andre\\eclipse-workspace\\AdventOfCOde\\src\\testExempel");
		Scanner sc = new Scanner(file);
		ArrayList<String> lines = new ArrayList<>();
		
		while(sc.hasNext()) {
			String line = sc.nextLine();
			lines.add(line);
		}
		sc.close();

		char[][] board = new char[lines.size()][lines.get(0).length()];
		
		for(int i = 0; i < lines.size(); i++) {
			for(int j = 0; j < lines.get(i).length(); j++) {
				board[i][j] = lines.get(i).charAt(j);
			}
		}
		
		Day23Part2 d = new Day23Part2();
		int startRow = 0;
		int startCol = 0;
		
		//Find start.
		for(int i = 0; i < lines.get(startRow).length(); i++) {
			if(lines.get(startRow).charAt(i) == '.') startCol = i;
		}
		Node start = d.new Node(startRow, startCol, 0);
				
		int endRow = lines.size() - 1;
		int endCol = 0;
		//Find end point.
		for(int i = 0; i < lines.get(endRow).length(); i++) {
			if(lines.get(endRow).charAt(i) == '.') endCol = i;
		}
		Node end = d.new Node(endRow, endCol, 0);
		
		
		Stack<Node> crossings = d.crossings(board, start, end);
		
		
		HashMap<Node, HashMap<Node, Integer>> graphMap = d.graphMapping(board, crossings);
		HashSet<Node> visited =  new HashSet<Node>();
		visited.add(d.new Node(startRow, startCol, 0));
		int res = d.dfs(d.new Node(startRow, startCol, 0), d.new Node(endRow, endCol, 0), graphMap, visited);
		
		System.out.println("Part 1: " + res);
		System.out.println("Part 2: ");
		
		
	}
	
	//Creates a Map which maps each cross-point to a tuple consisting of the next reachable cross-point and the distance between them.
	public HashMap<Node, HashMap<Node, Integer>> graphMapping(char[][] board, Stack<Node> crossings) {
		HashMap<Node, HashMap<Node, Integer>> graphMap = new HashMap<>();
		for (Node node : crossings) {
            graphMap.put(node, new HashMap<>());
        }

		for(Node crossNode : crossings) {
			
			
			Stack<Node> nodes = new Stack<>();
			HashSet<Node> visited = new HashSet<>();
			
			
			nodes.add(crossNode);
			visited.add(crossNode);
			
			while(!nodes.isEmpty()) {
				
				Node node = nodes.pop();
				int row = node.getRow();
				int col = node.getCol();
				int cost = node.getCost();
				
				
				if(cost != 0 && crossings.contains(node)) {
					HashMap<Node, Integer> map = graphMap.get(crossNode);
					map.put(node, cost);
					graphMap.put(crossNode, map);
					continue;
				}
				
				if(row > 0 && board[row-1][col] != '#' && !visited.contains(new Node(row-1, col, cost + 1))){
					nodes.add(new Node(row-1, col, cost + 1));
					visited.add(new Node(row-1, col, cost + 1));
				}
				
				if(row < board.length - 1 && board[row+1][col] != '#' && !visited.contains(new Node(row+1, col, cost + 1))){
					nodes.add(new Node(row+1, col, cost + 1));
					visited.add(new Node(row+1, col, cost + 1));
				}
				
				if(col > 0 && board[row][col-1] != '#' && !visited.contains(new Node(row, col-1, cost + 1))){
						nodes.add(new Node(row, col-1, cost + 1));
						visited.add(new Node(row, col-1, cost + 1));
				}
				
				if(col < board[0].length - 1 && board[row][col+1] != '#' && !visited.contains(new Node(row, col+1, cost + 1))){
					nodes.add(new Node(row, col+1, cost + 1));
					visited.add(new Node(row, col+1, cost + 1));
				}
			}
			
		}
		
		return graphMap;
	}
	
	
	private int dfs(Node node, Node end, HashMap<Node, HashMap<Node, Integer>> graphMap, HashSet<Node> visited) {
        if (node.equals(end)) {
            return 0;
        }
        int max = Integer.MIN_VALUE;
        visited.add(node);
        
        for (Node next : graphMap.get(node).keySet()) {
        	
            if (!visited.contains(next)) {
                max = Math.max(max, dfs(next, end, graphMap, visited) + graphMap.get(node).get(next));
            }
        }
        visited.remove(node);
        return max;
    }
	
	//Returns the nodes where there are multiple choices for the next step.
	public Stack<Node> crossings(char[][] board, Node start, Node end) {
		Stack<Node> crossings = new Stack<>();
		crossings.add(end);
		crossings.add(start);
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				if(isCrossing(board, i, j))  crossings.add(new Node(i, j, 0));
			}
		}
		return crossings;
	}
	
	public boolean isCrossing(char[][] board, int row, int col) {
		if(board[row][col] == '#') return false;
		int count = 0;
		if(row > 0 && board[row-1][col] != '#') count += 1;
		if(row < board.length - 1 && board[row+1][col] != '#') count += 1;
		if(col > 0 && board[row][col-1] != '#') count += 1;
		if(col < board[0].length - 1 && board[row][col+1] != '#') count += 1;
		return count >= 3;
	}
	
	
	
	

}
