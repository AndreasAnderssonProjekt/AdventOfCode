mport java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Part1 {
	public static void main(String args[]) throws IOException {
        File file = new File("C:\\Users\\Andre\\eclipse-workspace\\Testing\\src\\advent1.txt");
		Scanner sc = new Scanner(file);
		int sum = 0;
		while(sc.hasNext()) {
			String line = sc.nextLine();
			int first = -1;
			int last = -1;
			int digit = -1;
			for(int i = 0; i < line.length(); i++) {
				if(Character.isDigit(line.charAt(i))) digit = Character.getNumericValue(line.charAt(i));
				
				if (digit != -1) {
					if(first == -1) first = digit;
					last = digit;
				}	
		}
		
		sum += first * 10 + last;
	}
	System.out.println(sum);
}
}
