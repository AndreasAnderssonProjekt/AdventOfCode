import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class Day8Part2 {

	public enum Instruction{
		LEFT,
		RIGHT
	}
	
	private static final char startLetter = 'A';
	private static final char endLetter = 'Z';
	
	public static void main(String[] args) throws FileNotFoundException {
		//File file = new File("C:\\Users\\Andre\\eclipse-workspace\\Advent\\src\\testExempel");
		File file = new File("C:\\Users\\Andre\\eclipse-workspace\\Advent\\src\\Day8");
		Scanner sc = new Scanner(file);
		
		ArrayList<Instruction> instructions = new ArrayList<>();
		ArrayList<String> lines = new ArrayList<>();
		ArrayList<Integer> currPos = new ArrayList<>(); // Store current index of each position.
		HashMap<String, ArrayList<Integer>> nodes = new HashMap<>(); // Store nodes and which lines they occur.
		
		int lineNr = 0;
		boolean instructionLine = true;
		while(sc.hasNext()) {
			String line = sc.nextLine();
			if(line.equals("")) {
				instructionLine = false;
				continue;
			}
			
			if(instructionLine) { //Read and store instructions.
				for(int i = 0; i < line.length(); i++) {
					char c = line.charAt(i);
					if(c == 'L') instructions.add(Instruction.LEFT);
					else instructions.add(Instruction.RIGHT);
				}
			}
			else {//Store lines and starting positions.
				lines.add(line);
				String node = line.substring(0,3);
				if(nodes.containsKey(node)) {
					nodes.get(node).add(lineNr);
				}else {
					ArrayList<Integer> list = new ArrayList<>();
					list.add(lineNr);
					nodes.put(node, list);
				}
				
				if(node.charAt(2) == startLetter) currPos.add(lineNr);
				lineNr += 1;
			}
			
			
		}
		
		
		int[] firstZ = new int[currPos.size()];
		int count = 0;
		
		int instructionNr = 0;
		while(count < currPos.size()) {
			Instruction instruction = instructions.get(instructionNr % instructions.size());
			updatePositions(currPos, nodes, lines, instruction);
			instructionNr += 1;
			for(int i = 0; i < firstZ.length; i++) {
				if(firstZ[i] == 0 && lines.get(currPos.get(i)).charAt(2) == endLetter) {
					firstZ[i] = instructionNr;
					count += 1;
				}
			}
			
		}
		
		System.out.println(MathHelpers.lcm(firstZ));
		
	}
	
	
	public static void updatePositions(ArrayList<Integer> currPos, HashMap<String,ArrayList<Integer>> nodes, ArrayList<String> lines, Instruction instruction) {
		for(int i = 0; i < currPos.size(); i++) {
			int pos = currPos.get(i);
			String nextNode = (instruction == Instruction.LEFT) ? lines.get(pos).substring(7,10) : lines.get(pos).substring(12,15);
			int nextPos = nodes.get(nextNode).get(0);
			currPos.set(i, nextPos);
		}
	}

}
