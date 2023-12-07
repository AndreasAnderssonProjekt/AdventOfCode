import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class Advent1Part2 {
	
	private static HashMap<String, Integer> stringDigits = new HashMap<>(){{
		put("one", 1);
		put("two", 2);
		put("three", 3);
		put("four", 4);
		put("five", 5);
		put("six", 6);
		put("seven", 7);
		put("eight", 8);
		put("nine", 9);
	}};
	
	
	public static void main(String args[]) throws IOException{
		File file = new File("C:\\Users\\Andre\\eclipse-workspace\\Testing\\src\\advent1.txt");
		Scanner sc = new Scanner(file);
		int sum = 0;
		while(sc.hasNext()) {
			String line = sc.nextLine();
			int first = -1;
			int last = -1;
			
			for(int i = 0; i < line.length(); i++) {
				char c = line.charAt(i);
				int digit = -1;
				if(Character.isDigit(c)) {
					digit = Character.getNumericValue(c);
				}
				else {
					int stringDigit = getStringDigit(line.substring(0,i+1));
					if(stringDigit != -1) digit = stringDigit;
				}
				
				
				if(digit != -1) {
					if(first == -1) first = digit;
					last = digit;
				}
				
		}	
			
			sum += first * 10 + last;
	}
		sc.close();
		System.out.println(sum);

}
	public static int getStringDigit(String str) {
		int digit = -1;
		for(String key : stringDigits.keySet()) {
			if(key.length() > str.length()) continue;
			if(str.substring(str.length() - key.length()).equals(key)) {
				digit = stringDigits.get(key);
				 break;
			}
		}
		return digit;
	}
	
	
}
