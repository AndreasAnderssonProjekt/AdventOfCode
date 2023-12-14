import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class Day14 {

	private static HashMap<Integer, Integer> cycle = new HashMap<>();
	public static void main(String[] args) throws FileNotFoundException {
		File file = new File("C:\\Users\\Andre\\eclipse-workspace\\Advent\\src\\testExempel");
		Scanner sc = new Scanner(file);
		List<String> list = new ArrayList<>();
		while(sc.hasNext()) {
			String line = sc.nextLine();
			list.add(line);
		}
		sc.close();

		Day14 d = new Day14();
		
		int nrRolls = 1000000000;
		
		System.out.println("Part 1: " + d.part1(list));
		System.out.println("Part 2: " + d.part2(list, nrRolls));
		
	}
	
	
	public int part1(List<String> lines) {
		char[][] board = getBoard(lines);
		rollBoard(board, false);
		return load(board);
	}
	
	public int part2(List<String> lines, int nrRolls) {
		char[][] board = this.getBoard(lines);
		
		int lengthOfCycle = 0;
		int reached = 0;
		for(int i = 0; i < nrRolls; i++) {
			rollBoard(board, true);
			int hash = java.util.Arrays.deepHashCode(board);
			if(cycle.containsKey(hash)) {
				lengthOfCycle = i - cycle.get(hash);
				reached = i;
				break;
			}else {
				
				cycle.put(hash, i);
				
			}
		}
		
		
		int endPoint = (nrRolls - reached - 1) % lengthOfCycle;
		for(int i = 0; i < endPoint; i++) {
			rollBoard(board, true);
		}
		
		return load(board);
	}
	public char[][] getBoard(List<String> list){
		char[][] board = new char[list.size()][list.get(0).length()];
		for(int row = 0; row < list.size(); row++) {
			for(int col = 0; col < list.get(row).length(); col++) {
				board[row][col] = list.get(row).charAt(col);
			}
		}
		return board;
	}
	
	public void rollBoard(char[][] board, boolean cycle) {
		for(int col = 0; col < board[0].length; col++) rollColumnNorth(board, col);
		
		if(cycle) {
			for(int row = 0; row < board.length; row++) rollColumnWest(board, row);
			for(int col = 0; col < board[0].length; col++) rollColumnSouth(board, col);
			for(int row = 0; row < board.length; row++) rollColumnEast(board, row);
		}
	}
	
	public void rollColumnNorth(char[][] board, int col) {
		Queue<Integer> emptySpace = new LinkedList<>();
		for(int row = 0; row < board.length; row++) {
			if(board[row][col] == '.') emptySpace.add(row); // Add available slot.
			else if(board[row][col] == '#') emptySpace.clear(); // Square will block upcoming round rocks.
			else {
				if(!emptySpace.isEmpty()) {
					int index = emptySpace.poll();
					board[row][col] = '.';
					board[index][col] = 'O';
					emptySpace.add(row); //Add the newly created empty space caused by moving the round rock.
				}
			}
		}
	}
	
	public void rollColumnSouth(char[][] board, int col) {
		Queue<Integer> emptySpace = new LinkedList<>();
		for(int row = board.length - 1; row >= 0; row--) {
			if(board[row][col] == '.') emptySpace.add(row); // Add available slot.
			else if(board[row][col] == '#') emptySpace.clear(); // Square will block upcoming round rocks.
			else {
				if(!emptySpace.isEmpty()) {
					int index = emptySpace.poll();
					board[row][col] = '.';
					board[index][col] = 'O';
					emptySpace.add(row); //Add the newly created empty space caused by moving the round rock.
				}
			}
		}
	}
	
	public void rollColumnWest(char[][] board, int row) {
		Queue<Integer> emptySpace = new LinkedList<>();
		for(int col = 0; col < board[row].length; col++) {
			if(board[row][col] == '.') emptySpace.add(col); // Add available slot.
			else if(board[row][col] == '#') emptySpace.clear(); // Square will block upcoming round rocks.
			else {
				if(!emptySpace.isEmpty()) {
					int index = emptySpace.poll();
					board[row][col] = '.';
					board[row][index] = 'O';
					emptySpace.add(col); //Add the newly created empty space caused by moving the round rock.
				}
			}
		}
	}
	
	public void rollColumnEast(char[][] board, int row) {
		Queue<Integer> emptySpace = new LinkedList<>();
		for(int col = board[0].length - 1; col >= 0; col--) {
			if(board[row][col] == '.') emptySpace.add(col); // Add available slot.
			else if(board[row][col] == '#') emptySpace.clear(); // Square will block upcoming round rocks.
			else {
				if(!emptySpace.isEmpty()) {
					int index = emptySpace.poll();
					board[row][col] = '.';
					board[row][index] = 'O';
					emptySpace.add(col); //Add the newly created empty space caused by moving the round rock.
				}
			}
		}
	}
	
	public int load(char[][] board) {
		int sum = 0;
		int n = board.length;
		for(int row = 0; row < board.length; row++) {
			for(int col = 0; col < board[0].length; col++) {
				if(board[row][col] == 'O') sum += n - row; 
			}
		}
		return sum;
	}

}
