import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Day10 {

	public class Pipe{
		private int x;
		private int y;
		private char type;
		
		public Pipe(int x, int y, char type) {
			this.x = x;
			this.y = y;
			this.type = type;
		}
		
		public int getX() {
			return this.x;
		}
		
		public int getY() {
			return this.y;
		}
		
		public char getType() {
			return this.type;
		}
	}
	
	
	public static void main(String[] args) throws FileNotFoundException {
		File file = new File("C:\\Users\\Andre\\eclipse-workspace\\Advent\\src\\Day10");
		Scanner sc = new Scanner(file);
		
		ArrayList<String> lines = new ArrayList<>();
		
		while(sc.hasNext()) {
			String line = sc.nextLine();
			lines.add(line);
		}
		sc.close();
		Day10 d = new Day10();
		int boardHeight = lines.size();
		int boardWidth = lines.get(0).length();
		boolean[][] visited = new boolean[boardHeight][boardWidth];
		
		Pipe startPipe = d.startPosition(lines);
		
		
		d.dfs(startPipe, lines, visited);
		
		
		int nrInside = 0;
		for(int i = 0; i < visited.length; i++) {
			
					if(d.containedPoint(i, lines, visited)) {
						nrInside += 1;
					}
		}
		
		System.out.println("Part 1: " + d.longestDistance(visited));
		System.out.println("Part 2: " + nrInside);
		
		

	}
	
	
	public int longestDistance(boolean[][] visited) {
		int dist = 0;
		for(int i = 0; i < visited.length; i++) {
			for(int j = 0; j < visited[0].length; j++) {
				if(visited[i][j]) dist += 1;
			}
		}
		return Math.floorDiv((dist-1),2) + 1;
	}
	
	
	public boolean containedPoint(int y, ArrayList<String> lines, boolean[][] visited) {
		Character up = null;
		boolean inside = false;
		for(int i = 0; i < visited[y].length; i++) {
			
			char c = lines.get(y).charAt(i);
			if(c == '|' && visited[y][i]) {
				assert (up == null);
				inside = !inside;
			}
			
			else if(c == '-' && visited[y][i]) {
				assert (up != null);
			}
			
			else if((c == 'L' || c == 'F' || c == 'S') && visited[y][i]) {
				assert (up == null);
				if(c == 'L') up = 'L';
			}
			else if((c == '7' || c == 'J') && visited[y][i]) {
				assert (up != null);
				if(c != ((up != null) ? 'J' : '7')) {
					inside = !inside;
				}
				up = null;
				
				
			}
				
		}
		return !inside;
	}
	
	public void dfs(Pipe startPipe, ArrayList<String> lines, boolean[][] visited) {
		
		Queue<Pipe> queue = new LinkedList<>();
		queue.add(startPipe);
		visited[startPipe.getY()][startPipe.getX()] = true;
		while(!queue.isEmpty()) {
		Pipe pipe1 = queue.poll();
		
		int x = pipe1.getX();
		int y = pipe1.getY();
		
		Pipe pipe = null;
		
		int addedPipes = 0;
		if(x > 0) {
			pipe = new Pipe(x-1,y, lines.get(y).charAt(x-1));
			if(compatablePipes(pipe1,pipe,lines) && !visited[y][x-1]) {
				visited[y][x-1] = true;
				queue.add(pipe);
				addedPipes += 1;
				if(x == 4 && y == 78) System.out.println("left");
			}
		}
		
		if(x < lines.get(y).length() - 1) {
			pipe = new Pipe(x+1,y, lines.get(y).charAt(x+1));
			if(compatablePipes(pipe1,pipe,lines) && !visited[y][x+1]) {
				visited[y][x+1] = true;
				
				queue.add(pipe);
				addedPipes += 1;
				
			}
		}
		
		if(y > 0) {
			pipe = new Pipe(x,y-1, lines.get(y-1).charAt(x));
			if(compatablePipes(pipe1,pipe,lines) && !visited[y-1][x]) {
				//System.out.println("HEJ3");
				visited[y-1][x] = true;
				
				queue.add(pipe);
				
				addedPipes += 1;
				
				
			}
		}
		
		if(y < lines.size() - 1) {
			pipe = new Pipe(x,y+1, lines.get(y+1).charAt(x));
			if(compatablePipes(pipe1,pipe,lines) && !visited[y+1][x]) {
				//System.out.println("HEJ4");
				
				visited[y+1][x] = true;
				queue.add(pipe);
				addedPipes += 1;
				
			}
		}
		
		
		}
		
	}
	
	
	public boolean compatablePipes(Pipe pipe1, Pipe pipe2, ArrayList<String> lines) {
		int x1 = pipe1.getX();
		int y1 = pipe1.getY();
		char type1 = pipe1.getType();
		int x2 = pipe2.getX();
		int y2 = pipe2.getY();
		char type2 = pipe2.getType();
		
		// pipe1 lie to the left of pipe2. In this case, the only compatable pipe combinations are
		//(-,-), (L,-), (-,J), (-,7), (F,J), (F,7), (L,7), (F,-),(L,J),(F,L)
		if(x1 < x2) { 
			if((type1 == '-' && type2 == '-') ||(type1 == 'L' && type2 == '-') || (type1 == '-' && type2 == 'J') || (type1 == '-' && type2 == '7') || (type1 == 'F' && type2 == 'J') || (type1 == 'F' && type2 == '7') || (type1 == 'L' && type2 == '7') || (type1 == 'F' && type2 == '-') || (type1 == 'L' && type2 == 'J') ) {
				return true;
			}
		}
		//pipe1 lie to the right of pipe2. In this case, the only compatable pipe combinations are
		//(-,-),(-,L), (J,-), (7,-), (J,F), (7,F), (7,L),(-,F), (J,L), (L,F)
		else if(x1 > x2) {
			if((type1 == '-' && type2 == '-') ||(type1 == '-' && type2 == 'L') || (type1 == 'J' && type2 == '-') || (type1 == '7' && type2 == '-') || (type1 == 'J' && type2 == 'F') || (type1 == '7' && type2 == 'F') || (type1 == '7' && type2 == 'L')|| (type1 == '-' && type2 == 'F') || (type1 == 'J' && type2 == 'L') ) {
				return true;
			}
		}
		//pipe1 lie above pipe2. In this case, the only compatable pipe combinations are
		//(|,|), (|,J), (|,L), (7,|),(7,J),(7,L) (F,|), ,(F,J),(F,L)
		else if(y1 < y2) {
			if((type1 == '|' && type2 == '|') || (type1 == '|' && type2 == 'J') || (type1 == '|' && type2 == 'L') || (type1 == '7' && type2 == '|') || (type1 == 'F' && type2 == 'J') || (type1 == '7' && type2 == 'J') || (type1 == '7' && type2 == 'L') || (type1 == 'F' && type2 == '|') || (type1 == 'F' && type2 == 'J') || (type1 == 'F' && type2 == 'L')) {
				return true;
			}
		}
		//pipe1 lie below pipe2. In this case, the only compatable pipe combinations are
		//(|,|), (j,|), (L,|), (|,7), (J,F), (J,7),(L,7),(|,F),(J,F), (L,F)
		else if(y1 > y2) {
			if((type1 == '|' && type2 == '|') || (type1 == 'J' && type2 == '|') || (type1 == 'L' && type2 == '|') || ( type1 == '|' && type2 == '7') || (type1 == 'J' && type2 == 'F') || (type1 == 'J' && type2 == '7') || (type1 == 'L' && type2 == '7') || (type1 == '|' && type2 == 'F') || (type1 == 'J' && type2 == 'F') || (type1 == 'L' && type2 == 'F')) {
				return true;
			}
		}
		return false;
	}
	
	
	public Pipe startPosition(ArrayList<String> lines) {
		for(int i = 0; i < lines.size(); i++) {
			for(int j = 0; j < lines.get(i).length(); j++) {
				if (lines.get(i).charAt(j) == 'S') {
					
					return new Pipe(j,i,'F');
				}
			}
		}
		return null;
	}
	
	
 
}
