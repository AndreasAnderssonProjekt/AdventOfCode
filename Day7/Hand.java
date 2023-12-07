package Day7;

import java.util.HashMap;
import java.util.Map;

public class Hand implements Comparable<Hand> {
	private String hand;
	private int bid;
	private boolean joker;
	private int strength;
	
	private class Card implements Comparable<Card>{
		private int rank;
		public Card(char c, boolean joker) {
			switch(c) {
				case('T'): 
					this.rank = 10;
					break;
				case('J'):
					if(joker) this.rank = 1;
					else this.rank = 11;
					break;
				case('Q'):
					this.rank = 12;
					break;
				case('K'):
					this.rank = 13;
					break;
				case('A'):
					this.rank = 14;
					break;
				default: //c is digit.
					this.rank = (int) c - '0';
				
			}		
		}
		
		public int getRank() {
			return this.rank;
		}
		
		@Override
		public int compareTo(Card c) {
			int res =  this.getRank() - c.getRank();
			if(res == 0) return 0;
			return (res > 0) ? 1 : -1;
		}
	}
	
	public Hand(String hand, int bid, boolean joker) {
		this.hand = hand;
		this.bid = bid;
		this.strength = evaluateHand(this.hand);
		this.joker = joker;
	}
	
	public String getHand() {
		return this.hand;
	}
	
	
	public int getBid() {
		return this.bid;
	}
	
	public int getStrength() {
		return strength;
	}
	
	public void setJoker(boolean joker) {
		this.joker = joker;
		this.strength = evaluateHand(this.hand);
	}
	
	private int evaluateHand(String hand) {
		Map<Character, Integer> count = emptyCountMap();
		for(int i = 0; i < hand.length(); i++) {
			count.put(hand.charAt(i), count.get(hand.charAt(i))+1);
		}
		
		int cardDuplicates1 = 0;
		int cardDuplicates2 = 0;
		for(char key : count.keySet()) {
			if(joker) {
				if(key == 'J') continue;
			}
			int nr = count.get(key);
			if(nr > cardDuplicates1) {
				cardDuplicates2 = cardDuplicates1;
				cardDuplicates1 = nr;
			}
			else if(nr > cardDuplicates2) cardDuplicates2 = nr;
		}
		
		if(joker) {
			cardDuplicates1 += count.get('J');
			
		}
		
		
		if(cardDuplicates1 == 5) return 6; //Five of a kind.
		if(cardDuplicates1 == 4) return 5; //Four of a kind.
		if(cardDuplicates1 == 3 && cardDuplicates2 == 2) return 4; // Full house.
		if(cardDuplicates1 == 3) return 3; //Three of a kind.
		if(cardDuplicates1 == 2 && cardDuplicates2 == 2) return 2; //Two pair.
		if(cardDuplicates1 == 2) return 1; //One pair.
		return 0; //High card.
		
	}

	@Override
	public int compareTo(Day7.Hand h) {
		if(this.strength == h.strength) {
			for(int i = 0; i < this.hand.length(); i++) {
				char card1 = this.hand.charAt(i);
				char card2 = h.hand.charAt(i);
				int comp = (new Card(card1,this.joker)).compareTo(new Card(card2,this.joker));
				
				if(comp == 0) continue;
				return comp;
			}
			return 0;
		} 
		else return (this.strength > h.strength) ? 1 : -1;
		
		
	}
	
	//Helper Method.
	private Map<Character, Integer> emptyCountMap(){
		Map<Character, Integer> count = new HashMap<>();
		count.put('A',0);
		count.put('1', 0);
		count.put('2', 0);
		count.put('3', 0);
		count.put('4', 0);
		count.put('5', 0);
		count.put('6', 0);
		count.put('7', 0);
		count.put('8', 0);
		count.put('9', 0);
		count.put('T', 0);
		count.put('J', 0);
		count.put('Q', 0);
		count.put('K', 0);
		return count;
	}
	
	
}
