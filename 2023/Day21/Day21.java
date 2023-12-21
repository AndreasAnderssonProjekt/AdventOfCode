package AdventOfCode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Day21 {
	private static char[][] board;
	private static char[][] prevBoard;
	private static char[][] prevprevBoard;
	private static boolean[][] visited;
	
	public static void main(String[] args) throws FileNotFoundException {
		File file = new File("C:\\Users\\Andre\\eclipse-workspace\\AdventOfCOde\\src\\testExempel");
		Scanner sc = new Scanner(file);
		
		ArrayList<String> lines = new ArrayList<>();
		while(sc.hasNext()) {
			String line = sc.nextLine();
			lines.add(line);
		}
		sc.close();
		
		board =  new char[lines.size()][lines.get(0).length()];
		visited =  new boolean[lines.size()][lines.get(0).length()];
		Queue<int[]> q = new LinkedList<>();
		
		initialize(q, lines);
		

		prevBoard = new char[lines.size()][lines.get(0).length()];
		prevprevBoard = new char[lines.size()][lines.get(0).length()];
		int stopLayer = 64;
		
		bfs(q, 0, stopLayer-1);
		System.out.println("Part 1: " + countTiles(board));
		
	
		
		//Part 2
		/**The horizontal and vertical path starting at the center do not have any obstacles.
		 * Hence the final board will have: 
		 * height of nrSteps // board.length - 1,
		 * width of nrSteps // board[0].length - 1.
		 * Since the input data have a dimension 131x131 and the starting position is placed
		 * right in the middle of it the earliest we can reach the border is at step 65.
		 * Luckily, it turns out that the input NrSteps minus 65 is divisible by the grid length (131):
		 * 26501365-65/131 = 202300.
		 * As the quickest way to a new board is through the middle lines we arrive at the middle
		 * of the border of the new board.
		 * To continue as the board size is odd i.e, 131 two succeeding boards will be filled
		 * with a different number of tiles. Thus we have to distinguish between the number of
		 * odd and even boards.
   		 * Lastly there will also be boards which havent been fully filled that also have to be considered.
		*/
		
		stopLayer = 26501365;
		
		int board_size = lines.size();
		long expanded_board_size = Math.floorDiv(stopLayer, board.length);
		
		long nrOdd = (long) Math.pow(Math.floorDiv(expanded_board_size, 2) * 2 - 1, 2);
		long nrEven = (long) Math.pow(Math.floorDiv(expanded_board_size, 2) * 2 , 2);
		
		//Number of points in fully filled boards (odd and even).
		initialize(q, lines);
		bfs(q, 0, board_size * 2);
		long pointsOdd = countTiles(board);
		initialize(q, lines);
		bfs(q, 0, board_size * 2 + 1);
		long pointsEven = countTiles(board);
		
		//Number of points in corners.
		initialize(q, lines);
		int[] start = q.poll();
		q.add(new int[] {board_size - 1, start[1]});
		bfs(q, 0, board_size-2);
		long topCorner = countTiles(board);
		
		initialize(q, lines);
		start = q.poll();
		q.add(new int[] {start[0], 0});
		bfs(q, 0, board_size - 2);
		long rightCorner = countTiles(board);
		
		initialize(q, lines);
		start = q.poll();
		q.add(new int[] {0, start[1]});
		bfs(q, 0, board_size - 2);
		long bottomCorner = countTiles(board);
		
		initialize(q, lines);
		start = q.poll();
		q.add(new int[] {start[0], board_size - 1});
		bfs(q, 0, board_size - 2);
		long leftCorner = countTiles(board);
		
		//Large triangles.
		initialize(q, lines);
		q.remove();
		q.add(new int[] {board_size - 1, 0});
		bfs(q, 0, Math.floorDiv(board_size * 3, 2) - 2);
		long topRightLarge = countTiles(board);
		
		initialize(q, lines);
		q.remove();
		q.add(new int[] {board_size - 1, board_size - 1});
		bfs(q, 0, Math.floorDiv(board_size * 3, 2) - 2);
		long topLeftLarge = countTiles(board);
		
		initialize(q, lines);
		q.clear();
		q.add(new int[] {0, 0});
		bfs(q, 0, Math.floorDiv(board_size * 3, 2) - 2);
		long bottomRightLarge = countTiles(board);
		
		initialize(q, lines);
		q.remove();
		q.add(new int[] {0, board_size-1});
		bfs(q, 0, Math.floorDiv(board_size * 3, 2) - 2);
		long bottomLeftLarge = countTiles(board);
		
		//Small triangles.
		initialize(q, lines);
		q.remove();
		q.add(new int[] {board_size - 1, 0});
		bfs(q, 0, Math.floorDiv(board_size,2) - 2);
		long topRightSmall = countTiles(board);
		
		initialize(q, lines);
		q.remove();
		q.add(new int[] {board_size - 1, board_size - 1});
		bfs(q, 0,  Math.floorDiv(board_size,2) - 2);
		long topLeftSmall = countTiles(board);
		
		initialize(q, lines);
		q.remove();
		q.add(new int[] {0, 0});
		bfs(q, 0,  Math.floorDiv(board_size,2) - 2);
		long bottomRightSmall = countTiles(board);
		
		initialize(q, lines);
		q.remove();
		q.add(new int[] {0, board_size - 1});
		bfs(q, 0,  Math.floorDiv(board_size,2) - 2);
		long bottomLeftSmall = countTiles(board);
		
		
		long part2 = (expanded_board_size) * (bottomLeftSmall + bottomRightSmall + topLeftSmall + topRightSmall);
		part2 += (expanded_board_size-1) * (bottomLeftLarge + bottomRightLarge + topLeftLarge + topRightLarge);
		part2 += topCorner + bottomCorner + leftCorner + rightCorner;
		part2 += (nrOdd * pointsOdd) + (nrEven * pointsEven);
		
		System.out.println("Part 2: " + part2);
		
	}
	
	
	
	public static void bfs(Queue<int[]> q, int layer, int stopLayer) {

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
		
		
		
		//Reached the last step.
		if(layer == stopLayer) {
			return;
		}
		
		
		bfs(neighbours, layer + 1, stopLayer);
		
		
		
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
	
	public static void printBoard(char[][] board) {
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				System.out.print(board[i][j]);
			}
			System.out.println();
		}
	}
	
	public static void initialize(Queue<int[]> q, ArrayList<String> lines) {
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
		prevBoard = new char[board.length][board[0].length];
		prevprevBoard = new char[board.length][board[0].length];
	}
	
	
}
