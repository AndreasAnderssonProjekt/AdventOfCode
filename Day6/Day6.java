import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day6 {

	public static void main(String[] args) throws FileNotFoundException {
		File file = new File("C:\\Users\\Andre\\eclipse-workspace\\Testing\\src\\Day6.txt");
		Scanner sc = new Scanner(file);
		String[] time = null;
		String[] dist = null;
		StringBuilder time_part2 = new StringBuilder();
		StringBuilder dist_part2 = new StringBuilder();
		int index = 0;
		//Read required Strings for part 1.
		while(sc.hasNext()) {
			String line = sc.nextLine();
			if(index == 0) time = line.trim().split("\\s+");
			else dist = line.trim().split("\\s+");
			index += 1;
		}
		
		//Create required String for part 2.
		for(int i = 1; i < time.length; i++) {
			time_part2.append(time[i]);
			dist_part2.append(dist[i]);
		}
		
		int res1 = 0;
		for(int i = 1; i < time.length; i++) {
			int ways = numberOfWays(Double.parseDouble(time[i]), Double.parseDouble(dist[i]));
			
			if(res1 == 0) {
				res1 += ways;
			}
			else res1 *= ways;
		}
		
		System.out.println(res1);
		int res2 = numberOfWays(Double.parseDouble(time_part2.toString()), Double.parseDouble(dist_part2.toString()));
		System.out.println(res2);
	}
	
	//The solution is obtained by solving (time - pushedTime) * pushedTime > record.
	private static int numberOfWays(double time, double record) {
		double exp = Math.pow(time/2, 2) - (record+1);
		double sol1 = Math.ceil((time/2) - Math.sqrt(exp));
		double sol2 = Math.floor((time/2) + Math.sqrt(exp));
		return (int) (Math.floor(sol2) - Math.ceil(sol1)) + 1;
	}

}
