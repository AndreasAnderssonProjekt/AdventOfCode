package Day7;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		File file = new File("C:\\Users\\Andre\\eclipse-workspace\\Day7\\src\\Day7\\testexempel.txt");
		Scanner sc = new Scanner(file);		
		List<Hand> hands = new ArrayList<>();
		boolean joker = false;
		while(sc.hasNext()) {
			String[] line = sc.nextLine().trim().split("\\s+");
			String cards = line[0];
			String bid = line[1];
			Hand h = new Hand(cards,Integer.parseInt(bid),joker);
			hands.add(h);
		}
		sc.close();
		
		int result_part1 = 0;
		Collections.sort(hands);
		
		for(int i = 0; i < hands.size(); i++) {
			Hand hand = hands.get(i);
			result_part1 += hand.getBid() * (i+1);
		}
		
		int result_part2 = 0;
		for(Hand hand : hands) hand.setJoker(!joker);
		Collections.sort(hands);
		for(int i = 0; i < hands.size(); i++) {
			Hand hand = hands.get(i);
			result_part2 += hand.getBid() * (i+1);
		}
		
		System.out.println("Part 1: " + result_part1);
		System.out.println("Part 2: " + result_part2);

	}

}
