import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Day2 {
	private final static int blue = 14;
	private final static int green = 13;
	private final static int red = 12;

	public static void main(String[] args) throws IOException {
		File file = new File("C:\\Users\\Andre\\eclipse-workspace\\Testing\\src\\Day2.txt");
		//File file = new File("C:\\Users\\Andre\\eclipse-workspace\\Testing\\src\\testexempel.txt");
		Scanner sc = new Scanner(file);
		int gameID = 0;
		int res_part1 = 0;
		int res_part2 = 0;
		
		while(sc.hasNext()) {
			String game = sc.nextLine();
			int[] largest = getLargestPossible(blue, green, red, game);
			
			gameID += 1;
			
			
			if(blue >= largest[0] && green >= largest[1] && red >= largest[2] && blue + green + red >= largest[0] + largest[1] + largest[2]) {
				res_part1 += gameID;
			}
			
			int power = largest[0] * largest[1] * largest[2];
			res_part2 += power;
			
		}
		System.out.println(res_part1);
		System.out.println(res_part2);

	}
	
	
	
	public static int[] getLargestPossible(int blue, int green, int red, String game) throws IOException {
			int largestBlue = 0;
			int largestGreen = 0;
			int largestRed = 0;
			int largestTotal = 0;
			
			String[] components = game.split(" ");
			int total = 0;
			for(int i = 2; i < components.length; i = i + 2) { //Ignore the first two elements as they correspond to "Game" and the gamenumber.
				int nrCubes = Integer.parseInt(components[i]);
				String color = components[i+1];
				
				total += nrCubes;
				if(total > largestTotal) largestTotal = total;
				if(color.charAt(color.length() - 1) == ';') total = 0; //New subset
				
				switch(color.charAt(0)) {
					case 'b': 
						if(nrCubes > largestBlue) largestBlue = nrCubes;
						break;
					
					case 'g': 
						if(nrCubes > largestGreen) largestGreen = nrCubes;
						break;
					
					case 'r': 
						if(nrCubes > largestRed) largestRed = nrCubes;
						break;
						
				
					}
			}
			
		int[] largest = new int[] {largestBlue, largestGreen, largestRed};
		return largest;
		
	}
	
	

}
