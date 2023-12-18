package AdventOfCode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Day18Part2 {

	public class Point{
		private long row;
		private long col;
		
		public Point(long row, long col) {
			this.row = row;
			this.col = col;
		}
		
		public long getRow() {
			return this.row;
		}
		
		public long getCol() {
			return this.col;
		}
	}
	
	
	
	public static void main(String[] args) throws FileNotFoundException {
		File file = new File("C:\\Users\\Andre\\eclipse-workspace\\AdventOfCOde\\src\\testExempel");
		Scanner sc = new Scanner(file);
		ArrayList<Point> points = new ArrayList<>();
		Day18Part2 d = new Day18Part2();
		
		
		HashMap<String, Point> directions = new HashMap<>() {{
			put("3", d.new Point(-1,0));
			put("1", d.new Point(1, 0));
			put("0", d.new Point(0, 1));
			put("2", d.new Point(0, -1));
		}};
		
		int boundaryPoints = 0;
		points.add(d.new Point(0,0));
		while(sc.hasNext()) {
			String[] components = sc.nextLine().trim().split("\\s+");
			String color = components[2];
			String direction = color.substring(color.length()-2, color.length()-1);
			int length = Integer.parseInt(color.substring(2,color.length()-2), 16);
            boundaryPoints += length;
			
			
			Point prev = points.get(points.size() - 1);
			long row = prev.getRow();
			long col = prev.getCol();
			long rowInc = 0;
			long colInc = 0;
			if(directions.containsKey(direction)) {
				rowInc = directions.get(direction).getRow();
				colInc = directions.get(direction).getCol();
			}
			points.add(d.new Point(row + rowInc * length, col + colInc * length ));
		}
		sc.close();
		
		long area = d.area(points);
		//Picks theorem.
		long i = area - Math.floorDiv(boundaryPoints,2) + 1;
		System.out.println("Part 1: " + (i + boundaryPoints));

	}
	
	//Shoelace formula.
	public long area(ArrayList<Point> points) {
		long sum = 0;
		for(int i = 1; i < points.size() - 1; i++) {
			Point p_middle = points.get(i);
			Point p_left = points.get(i-1);
			Point p_right = points.get(i+1);
			sum += p_middle.getRow() * (p_right.getCol() - p_left.getCol());
		}
		return Math.abs(sum/2);
	}

}
