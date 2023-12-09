import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Day9 {

	public static void main(String[] args) throws FileNotFoundException {
		File file = new File("C:\\Users\\Andre\\eclipse-workspace\\Advent\\src\\Day9");
		Scanner sc = new Scanner(file);
		List<Integer> nextNumbers_part1 = new ArrayList<>();
		List<Integer> nextNumbers_part2 = new ArrayList<>();
		boolean backwards = false;
		while(sc.hasNext()) {
			int[] line = Arrays.stream(sc.nextLine().trim().split("\\s+")).mapToInt(Integer::parseInt).toArray();
			int nextNumber_part1 = computeNextNumber(line,backwards);
			int nextNumber_part2 = computeNextNumber(line,!backwards);
			nextNumbers_part1.add(nextNumber_part1);
			nextNumbers_part2.add(nextNumber_part2);
			
		}
		
		
		int sum_part1 = 0;
		int sum_part2 = 0;
		for(int i = 0; i < nextNumbers_part1.size(); i++) {
			
			sum_part1 += nextNumbers_part1.get(i);
			sum_part2 += nextNumbers_part2.get(i);
		}
		System.out.println("Part 1: " + sum_part1);
		System.out.println("Part 2: " + sum_part2);
		
		
	}
	
	private static int computeNextNumber(int[] line, boolean backwards) {
		ArrayList<int[]> levels = levels(line);
		int nrLevels = levels.size();
 		int prevNext = 0;
 		
 		for(int i = nrLevels - 1; i > 0; i--) {
 			int[]  level = levels.get(i);
 			int lastInSequence = (backwards) ? level[0] : level[level.length-1];
 			if(backwards) level[0] = lastInSequence - prevNext;
 			else level[level.length-1] = prevNext + lastInSequence;
 			prevNext = (backwards) ? level[0] : level[level.length-1];
 		}
 		
 		int[] first_level  = levels.get(0);
 		int number = (backwards) ? first_level[0] : first_level[first_level.length - 1];
 		int nextNumber = (backwards) ? number - prevNext : number + prevNext;
 		return nextNumber;
	}
	
	
	public static ArrayList<int[]> levels(int[] line){
		ArrayList<int[]> levels = new ArrayList<>();
		levels.add(line);
		int[] diff = differences(line);
		int nrLevels = 0;
		
 		while(!isEndLevel(diff)) {
			diff = differences(levels.get(nrLevels));
			levels.add(diff);
			nrLevels += 1;
		}
 		return levels;
	}
	public static int[] differences(int[] line) {
		int[] differences = new int[line.length-1];
		for(int i = 0; i < line.length - 1; i++) {
			differences[i] = line[i+1] - line[i];
		}
		return differences;
	}
	
	private static boolean isEndLevel(int[] line) {
		for(int i = 0; i < line.length; i++) if(line[i] != 0) return false;
		return true;
	}

}
