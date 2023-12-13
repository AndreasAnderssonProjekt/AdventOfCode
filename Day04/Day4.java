import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class Day4 {
	
	private static class Card{
		private String[] winningNrs;
		private String[] yourNrs;
		
		public Card(String[] winningNrs, String[] yourNrs) {
			this.winningNrs = winningNrs;
			this.yourNrs = yourNrs;
		}
		public String[] getWinningNrs() {
			return this.winningNrs;
		}
		public String[] getYourNrs() {
			return this.yourNrs;
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		File file = new File("C:\\Users\\Andre\\eclipse-workspace\\Testing\\src\\Day4.txt");	
		Scanner sc = new Scanner(file);
		int sum_part1 = 0;
		int sum_part2 = 0;
		int cardNr = 1;
		HashMap<Integer, Integer> map = new HashMap<>();
		while(sc.hasNext()) {
			String line = sc.nextLine();
			Card card = getCard(line);
			if(!map.containsKey(cardNr)) {
				map.put(cardNr, 1);
			}
			else {
				map.put(cardNr, map.get(cardNr) + 1);
			}
			
			int nrMatches = getNrMatches(card);
			int cardScore = (int) Math.pow(2, nrMatches-1);
			sum_part1 += cardScore;
			
			for(int i = 1; i <= nrMatches; i++) {
				if(!map.containsKey(cardNr + i)) {
					map.put(cardNr + i, 1 * map.get(cardNr));
				}
				else {
					map.put(cardNr + i, map.get(cardNr + i) + 1*map.get(cardNr));
				}
			}
			
			cardNr += 1;
		}
		sc.close();
		for(int i : map.keySet()) {
			if(i <= cardNr) sum_part2 += map.get(i);
		}

		System.out.println("part 1: " + sum_part1);
		System.out.println("part 2: " + sum_part2);
	}
	
	public static Card getCard(String card){
		card = card.split(":",2)[1];
		String[] splitCard = card.split("\\|",2);
		String[] winningNrs = splitCard[0].trim().split("\\s+");
		String[] yourNrs = splitCard[1].trim().split("\\s+");
		return new Card(winningNrs, yourNrs);
	}
	
	public static int getNrMatches(Card c) {
		int nrMatches = 0;
		HashMap<Integer,Integer> map = new HashMap<>();
		for(String nr : c.getWinningNrs()) {
			map.put(Integer.parseInt(nr), null);
		}
		
		for(String nr : c.getYourNrs()) {
			if(map.containsKey(Integer.parseInt(nr))) nrMatches += 1;
		}
		return nrMatches;
		
	}

	
}
