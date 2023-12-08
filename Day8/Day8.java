import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day8 {

	public static void main(String args[]) throws FileNotFoundException {
		//File file = new File("C:\\Users\\Andre\\eclipse-workspace\\Advent\\src\\testExempel");
		File file = new File("C:\\Users\\Andre\\eclipse-workspace\\Advent\\src\\Day8");
		Scanner sc = new Scanner(file);
		
		boolean instructionLine = true;
		List<Instruction> instructions = new ArrayList<>();
		List<String> lines = new ArrayList<>();
		String currPos = "AAA";
		int instructionNr = 0;
		int lineNr = 0;
		
		while(true) {
			String line = null;
			if(sc.hasNext()){
				line = sc.nextLine();
				if(!instructionLine) {
					lines.add(line);
				}	
			}
			else {
				line = lines.get(lineNr % lines.size());
			}
			if(line.equals("")){
					instructionLine = false;
					continue;
			}
			
			if(instructionLine) {
				for(int i = 0; i < line.length(); i++) {
					char c = line.charAt(i);
					if(c == 'R') instructions.add(Instruction.RIGHT);
					else instructions.add(Instruction.LEFT);
				}
			}
			
			else {
				
				
				if(!currPos.equals(lines.get(lineNr % lines.size()).substring(0,3))) {
					lineNr += 1;
					continue;
				}
				
				if(instructions.get(instructionNr % instructions.size()) == Instruction.LEFT) {
					currPos = lines.get(lineNr % lines.size()).substring(7,10);
				}
				else currPos = lines.get(lineNr % lines.size()).substring(12,15);
				
				instructionNr += 1;
				if(currPos.equals("ZZZ")) break;
				
			}
			
		}
		
		System.out.println("Number of Steps: " + instructionNr);
	}
	
	public enum Instruction{
		LEFT,
		RIGHT
	}
}
