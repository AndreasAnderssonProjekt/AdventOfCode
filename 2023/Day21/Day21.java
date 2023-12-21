package AdventOfCode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Day21 {

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
		boolean[][] visited = new boolean[lines.size()][lines.get(0).length()];
		Queue<int[]> q = new LinkedList<>();
		
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				board[i][j] = lines.get(i).charAt(j);
				if(lines.get(i).charAt(j) == 'S') {
					q.add(new int[] {i, j});
					visited[i][j] = true;
				}
				visited[i][j] = false;
			}
		}

		int stopLayer = 64;
		bfs(q, board, visited, 0, stopLayer-1, new char[lines.size()][lines.get(0).length()], new char[lines.size()][lines.get(0).length()]);
	
	}
	
	public static void bfs(Queue<int[]> q, char[][] board, boolean[][] visited, int layer, int stopLayer, char[][] prevBoard, char[][] prevprevBoard) {

		updateBoard(prevprevBoard, prevBoard);

		updateBoard(prevBoard, board);
		
		subtractBoard(board, prevBoard);
		
		addBoard(board, prevprevBoard);
		
		Queue<int[]> neighbours = new LinkedList<>();
		
		while(!q.isEmpty()) {
			
			int[] pos = q.poll();
			int i= pos[0];
			int j  = pos[1];
			board[i][j] = '.';
			//Add neighbouring tiles if not visited and not an obstacle.
			if(i > 0 && board[i-1][j] != '#') {
				if(!visited[i-1][j]) {
					board[i-1][j] = 'O';
					neighbours.add(new int[] {i-1, j});
					visited[i-1][j] = true;
				}
			}
			
			if(i < board.length - 1  &&  board[i+1][j] != '#') {
				if(!visited[i+1][j]) {
					board[i+1][j] = 'O';
					neighbours.add(new int[] {i+1, j});
					visited[i+1][j] = true;
				}
			}
			
			if(j > 0 && board[i][j-1] != '#') {
					if(!visited[i][j-1]) {
						board[i][j-1] = 'O';
						neighbours.add(new int[] {i, j-1});
						visited[i][j-1] = true;
					}
			}
			
			if(j < board[0].length - 1 && board[i][j+1] != '#') {
				if(!visited[i][j+1]) {
					board[i][j+1] = 'O';
					neighbours.add(new int[] {i, j+1});
					visited[i][j+1] = true;
				}
				
			}
			
		}
		
		//Cycle.
		if(equalBoards(prevprevBoard, board)) {
			if(stopLayer - layer % 2 == 0) System.out.println("Answer: " + countTiles(board));
			else System.out.println("Answer: " + countTiles(prevBoard));
			return;
		}
		
		//Reached the last step.
		if(layer == stopLayer) {
			System.out.println("Part 1: " + (countTiles(board) ));	
			return;
		}
		
		
		bfs(neighbours, board, visited, layer + 1, stopLayer, prevBoard, prevprevBoard);
		
		
		
	}

	public static int countTiles(char[][] board) {
		int count = 0;
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				if(board[i][j] == 'O') count += 1;
			}
		}
		return count;
	}
	
	public static boolean equalBoards(char[][] b1, char[][] b2) {
		if(b1.length != b2.length || b1[0].length != b2[0].length) return false;
		for(int i = 0; i < b1.length; i++) {
			for(int j = 0; j < b1[0].length; j++) {
				if(b1[i][j] != b2[i][j]) return false;
			}
		}
		return true;
	}
	
	public static void updateBoard(char[][] board, char[][] newBoard) {
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				board[i][j] = newBoard[i][j];
			}
		}
	}
	
	public static void subtractBoard(char[][] b1, char[][] b2) {
		for(int i = 0; i < b1.length; i++) {
			for(int j = 0; j < b1[0].length; j++) {
				if(b2[i][j] == 'O') b1[i][j] = '.';
			}
		}
	}
	
	public static void addBoard(char[][] b1, char[][] b2) {
		for(int i = 0; i < b1.length; i++) {
			for(int j = 0; j < b1[0].length; j++) {
				if(b2[i][j] == 'O') b1[i][j] = 'O';
			}
		}
	}
	
	
}
