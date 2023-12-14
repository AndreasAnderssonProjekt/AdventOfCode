import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Day12 {
	
	public static void main(String[] args) throws FileNotFoundException {
		Day12 d = new Day12();
		File file = new File("C:\\Users\\Andre\\eclipse-workspace\\Advent\\src\\testExempel");
		Scanner sc = new Scanner(file);

		int totArrangements = 0;

		while(sc.hasNext()) {
			String[] line = sc.nextLine().trim().split(" ");
			String sequence = line[0];
			String[] conditions = line[1].split(",");

			int arrangements = d.arrangements(sequence, 0, conditions, 0, 0);
			totArrangements += arrangements;
		}

		System.out.println("Part 1:" + totArrangements);

	}

	public int arrangements(String sequence, int seqIndex, String[] conditions, int condIndex, int nrCompleted) {
		int sum = 0;
		if(seqIndex >= sequence.length()) {
			return (condIndex >= conditions.length && nrCompleted == 0 || (condIndex == conditions.length - 1 && Integer.parseInt(conditions[condIndex]) == nrCompleted)) ? 1 : 0;
		}

		char[] translations;
		if(sequence.charAt(seqIndex) == '?') translations = new char[] {'.', '#'};
		else translations = new char[] {sequence.charAt(seqIndex)};

		for(int i = 0; i < translations.length; i++) {
			if(translations[i] == '#') {
					sum += arrangements(sequence, seqIndex+1, conditions, condIndex, nrCompleted + 1);
			}
			else {
				if(nrCompleted > 0){
					if(condIndex < conditions.length && Integer.parseInt(conditions[condIndex]) == nrCompleted) {
						sum += arrangements(sequence, seqIndex + 1, conditions, condIndex + 1, 0);
					}
				}
				else sum += arrangements(sequence, seqIndex + 1, conditions, condIndex, 0);
			}
		}
		return sum;

	}
}

	

	



	


