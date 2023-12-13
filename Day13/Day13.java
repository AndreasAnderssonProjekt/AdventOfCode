import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Day13 {

	public static void main(String[] args) throws FileNotFoundException {
		Day13 d = new Day13();
		File file = new File("C:\\Users\\Andre\\eclipse-workspace\\Advent\\src\\testExempel");
		Scanner sc = new Scanner(file);
		
		ArrayList<String> lines = new ArrayList<>();
		long totReflections = 0;
		long totReflections_part2 = 0;
		ArrayList<Integer> oldHorizontal = null;
		ArrayList<Integer> oldVertical = null;
		int[] smudge = null;
		while(sc.hasNext()) {
			String line = sc.nextLine();
			if(line == "") {
				oldHorizontal = d.horizontalReflections(lines);
				oldVertical = d.verticalReflections(lines);
				totReflections += d.sumReflections(oldHorizontal, oldVertical);
				smudge = d.smudge(lines);
				totReflections_part2 += d.newReflections(lines, oldHorizontal, oldVertical);
				lines.clear();
			}else{
			lines.add(line);
			
			}
		}
		oldHorizontal = d.horizontalReflections(lines);
		oldVertical = d.verticalReflections(lines);
		totReflections += d.sumReflections(oldHorizontal, oldVertical);
		smudge = d.smudge(lines);
		
		totReflections_part2 += d.newReflections(lines, oldHorizontal, oldVertical);
		sc.close();
		
		System.out.println("Part 1: " + totReflections);
		
		System.out.println("Part 2: " + totReflections_part2);
	}
	
	
	public long newReflections(ArrayList<String> lines, ArrayList<Integer> oldHorizontal, ArrayList<Integer> oldVertical) {
		ArrayList<Integer> newHorizontal = horizontalReflections(lines);
		ArrayList<Integer> newVertical = verticalReflections(lines);
		long totReflections = 0;
		for(int i : newHorizontal) {
			if(!oldHorizontal.contains(i)) totReflections += 100 * (i+1);
		}
		
		for(int i : newVertical) {
			if(!oldVertical.contains(i)) totReflections += i+1;
		}
		
		return totReflections;
		
	}
	
	public long sumReflections(ArrayList<Integer> horizontal, ArrayList<Integer> vertical) {
		long tot = 0;
		for(int i : horizontal) {
			tot += 100 * (i+1);
		}
		
		for(int i : vertical) {
			tot += i+1;
		}
		return tot;
	}
	
	
	public ArrayList<Integer> horizontalReflections(ArrayList<String> lines) {
		ArrayList<Integer> symLines = new ArrayList<>();
		for(int i = 0; i < lines.size() - 1; i++) {
			boolean sym = false;
			if(lines.get(i).equals(lines.get(i+1))) {
				sym = true;
				int left = i;
				int right = i+1;
				while(left > 0 && right < lines.size() - 1) {
					left -= 1;
					right += 1;
					if(!lines.get(left).equals(lines.get(right))) {
						sym = false;
						break;
					}
				}
				
			}
			if(sym) symLines.add(i);
		}
		return symLines;
	}
	
	
	
	public ArrayList<Integer> verticalReflections(ArrayList<String> lines) {
		ArrayList<Integer> symLines = new ArrayList<>();
		for(int i = 0; i < lines.get(0).length() - 1; i++) {
			String col1 = ensembleColumn(lines, i);
			String col2 = ensembleColumn(lines, i+1);
			boolean sym = false;
			if(col1.equals(col2)){
				sym = true;
				
				int left = i;
				int right = i+1;
				
				while(left > 0 && right < lines.get(0).length() - 1) {
					left -= 1;				
					right += 1;
					col1 = ensembleColumn(lines, left);
					col2 = ensembleColumn(lines, right);
					if(!col1.equals(col2)) {
						sym = false;
						break;
					}
				}
				
			}
			if(sym) symLines.add(i);
		}
		return symLines;
	}
	
	public String ensembleColumn(ArrayList<String> lines, int col) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < lines.size(); i++) {
			sb.append(lines.get(i).charAt(col));
		}
		return sb.toString();
	}
	
	
	
	public int[] smudge(ArrayList<String> lines) {
		
		//Case: horizontal
		for(int i = 0; i < lines.size() - 1; i++) {
			String row1 = lines.get(i);
			String row2 = lines.get(i+1);
			int divergeIndex = differByOne(row1, row2);
			if(divergeIndex != -1) {
				
				
				for(int j : new int[] {i+1,i}) { // Test replacements in each line.
					String temp = lines.get(j);
					String replacement = replacement(temp,divergeIndex);
					lines.set(j, replacement);
					if(isSymmetryLine(lines, i)) {
						
						return new int[] {j, divergeIndex};
					}
					else lines.set(j, temp);
				}	
			}
			
			// Change a sign of one row within the symmetry interval.
			else if(row1.equals(row2)) {
				
				if(isSymmetryLine(lines,i)) {
					continue; //Already symmetry line, continue to next.
				}
				int low = i;
				int high = i + 1;
				
				while(low > 0 && high < lines.size() - 1) {
					low -= 1;
					high += 1;
					row1 = lines.get(low);
					row2 = lines.get(high);
					divergeIndex = differByOne(row1, row2);
					if(divergeIndex != -1) {
						for(int j : new int[] {low,high}) { // Test replacements in each line.
							String temp = lines.get(j);
							String replacement = replacement(temp,divergeIndex);
							lines.set(j, replacement);
							if(isSymmetryLine(lines, i)) {
								return new int[] {j, divergeIndex};
							}
							else lines.set(j, temp);
						}	
					}
					
				}
			}
		}
		
		// Case: vertical
		for(int i = 0; i < lines.get(0).length() - 1; i++) {
			String col1 = ensembleColumn(lines, i);
			String col2 = ensembleColumn(lines, i+1);
			int divergeIndex = differByOne(col1, col2);
			if(divergeIndex != -1) {
				
				
				for(int j : new int[] {i+1,i}) { // Test replacements in each line.
					String temp = lines.get(divergeIndex);
					
					StringBuilder sb = new StringBuilder(temp);
					if(lines.get(divergeIndex).charAt(i) == '.') sb.setCharAt(i, '#');
					else sb.setCharAt(i, '.');
					lines.set(divergeIndex, sb.toString());
					
					if(isVerticalSymmetryLine(lines, i)) {
						return new int[] {j, divergeIndex};
					}
					else lines.set(divergeIndex, temp);
				}	
			}
			
			// Change a sign of one row within the symmetry interval.
			else if(col1.equals(col2)) {
				
				if(isVerticalSymmetryLine(lines,i)) {
					continue; //Already symmetry line, continue to next.
				}
				int low = i;
				int high = i + 1;
				
				while(low > 0 && high < lines.get(0).length() - 1) {
					low -= 1;
					high += 1;
					col1 = ensembleColumn(lines, low);
					col2 = ensembleColumn(lines, high);
					divergeIndex = differByOne(col1, col2);
					if(divergeIndex != -1) {
						
						for(int j : new int[] {high,low}) {  // Test replacements in each line.
							String temp = lines.get(divergeIndex);
							
							StringBuilder sb = new StringBuilder(temp);
							if(lines.get(divergeIndex).charAt(j) == '.') sb.setCharAt(j, '#');
							else sb.setCharAt(j, '.');
						
							lines.set(divergeIndex, sb.toString());
							if(isVerticalSymmetryLine(lines, i)) {
								return new int[] {j, divergeIndex};
							}
							else lines.set(divergeIndex, temp);
						}	
					}
					
				}
			}
		}
		return new int[] {-1, -1};
	}
	
	public int differByOne(String line1, String line2) {
		if(line1.length() != line2.length()) return -1;
		int differ = 0;
		int index = -1;
		for(int i = 0; i < line1.length(); i++) {
			if(line1.charAt(i) != line2.charAt(i)) {
				differ += 1;
				index = i;
			}
		}
		return (differ == 1) ? index : -1;
		
	}
	
	//Index = the lower index of the symmetry lines
	public boolean isSymmetryLine(ArrayList<String> lines, int index) {
		
		if(index == lines.size() - 1) return lines.get(index-1).equals(lines.get(index));
		if(index == 0) return lines.get(0).equals(lines.get(1));
		if(! lines.get(index).equals(lines.get(index + 1))) return false;
		int low = index;
		int high = index + 1;
		while(low > 0 && high < lines.size() - 1) {
			low -= 1;
			high += 1;
			if(!lines.get(low).equals(lines.get(high))) return false;
		}
		
		return true;
		
	}
	
	public boolean isVerticalSymmetryLine(ArrayList<String> lines, int index) {
		if(index == lines.get(0).length() - 1) return ensembleColumn(lines, index-1).equals(ensembleColumn(lines, index));
		if(index == 0) return ensembleColumn(lines, 0).equals(ensembleColumn(lines, 1));
		if(! ensembleColumn(lines, index).equals(ensembleColumn(lines, index+1))) return false;
		
			String col1 = ensembleColumn(lines, index);
			String col2 = ensembleColumn(lines, index+1);
			
			if(col1.equals(col2)){
				
				
				int left = index;
				int right = index+1;
				
				while(left > 0 && right < lines.get(0).length() - 1) {
					left -= 1;				
					right += 1;
					col1 = ensembleColumn(lines, left);
					col2 = ensembleColumn(lines, right);
					if(!col1.equals(col2)) {
						
						return false;
					}
				}
				
			}
			
		
		return true;
	}
	
	public String replacement(String line, int index) {
		StringBuilder sb = new StringBuilder(line);
		if(line.charAt(index) == '.') sb.setCharAt(index, '#');
		else sb.setCharAt(index, '.');
		return sb.toString();
	}
}

