import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
 

public class Day3Part1 {

	public static void main(String[] args) throws IOException {
		File file = new File("C:\\Users\\Andre\\eclipse-workspace\\Testing\\src\\day3.txt");
		//File file = new File("C:\\Users\\Andre\\eclipse-workspace\\Testing\\src\\testexempel.txt");
		Scanner sc = new Scanner(file);
		
		String line1 = sc.nextLine();
		String line2 = line1;
		String prevLine = line1;

		int sum = 0;
		
		
		
		HashMap<ArrayList<Integer>,ArrayList<Integer>> validSymbols = new HashMap<>();
		while(sc.hasNext()) {
			//Look for symbols that are neighbours to the numbers in the middle line.
			prevLine = line1;
			line1 = line2;
			line2 = sc.nextLine();
			sum += sumOfLine(prevLine, line1, line2);
			
		}
		// Look at the last line.
		sum += sumOfLine(line1,line2,null);
		
		
		
		System.out.println(sum);
		sc.close();
		
		

	}
	
	
	public static int sumOfPart(int start, int end, String prevLine, String line1, String line2) {
		int number = 0;
		if(nearbySymbol(start,end,line1,line2) || nearbySymbol(start,end,line1,prevLine)) {
			
			
			String stringNumber = line1.substring(start, end+1);
			
			number = Integer.parseInt(stringNumber);
		
			
		}
		return number;
	}
	
	public static int sumOfLine(String prevLine, String line1, String line2) {
		int start = -1;
		int end = -1;
		int sum = 0;
		
		for(int i = 0; i < line1.length(); i++) {
			if(Character.isDigit(line1.charAt(i))){
				if(start == -1) {
					start = i;
					end = i;
				}
				else { //At the end on each line, consider the current number before moving to the next line.
					end = i;
					if(end == line1.length() - 1) {
						sum += sumOfPart(start, end, prevLine, line1, line2);
						start = -1;
						end = -1;
					}
				}
				
			}
			
			else { // We have reached a non-digit character. Determine if there is a nearby symbol.
				if(start != -1 ) {
					sum += sumOfPart(start, end, prevLine, line1, line2);
					start = -1;
					end = -1;
					
				}
			}
					
		}
		return sum;
	}
	
	
	
	
	public static boolean nearbySymbol(int start, int end, String line1, String line2) {
		
		if(nearbySymbolInSameLine(line1, start, end)) return true;
		if(nearbySymbolInAdjacentLine(line2,start,end)) return true;
		return false;
	}
	
	public static boolean nearbySymbolInSameLine(String line, int start, int end) {
		
		if(start - 1 >= 0) {
			if(isSymbol(start-1,line)) return true;
		}
		if(end + 1 < line.length()) {
			
			if(isSymbol(end+1, line)) return true;
		}
		return false;
		
	}
	
	public static boolean nearbySymbolInAdjacentLine(String adjLine, int start, int end) {
		if(adjLine != null) {
			start = Math.max(0, start-1);
			end = Math.min(adjLine.length()-1, end + 1);
			
		for(int i = start; i < end + 1; i++) {
			
			
			if( isSymbol(i, adjLine)) return true;
		}
		}
		return false;
	}
		
	

	public static boolean isSymbol(int index, String line) {
		return !Character.isDigit(line.charAt(index)) && line.charAt(index) != '.';
	}
}
