import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Day15 {

	private class Lens{
		private String label;
		private int focalLength;
		
		public Lens(String label, int focalLength) {
			this.label = label;
			this.focalLength = focalLength;
		}
		
		public int getFocalLength() {
			return this.focalLength;
		}
		
		public void setFocalLength(int focalLength) {
			this.focalLength = focalLength;
		}
		
		public String getLabel() {
			return this.label;
		}
		
		@Override
		public boolean equals(Object l) {
			return this.getLabel().equals(((Lens) l).getLabel());
			
		}
		
		@Override
		public String toString() {
			return label + " " + focalLength;
			
		}
	}
	private static HashMap<Integer, ArrayList<Lens>> boxes = new HashMap<>();
	
	public static void main(String[] args) throws FileNotFoundException {
		File file = new File("C:\\Users\\Andre\\eclipse-workspace\\Advent\\src\\testExempel");
		Scanner sc = new Scanner(file);
		Day15 d = new Day15();
		int sum = 0;
		while(sc.hasNext()) {
			String line = sc.nextLine();
			String[] components = line.split(",");
			
			for(String component : components) {
				sum += d.hash(component);
				d.boxOperation(component);
			}
		}
		sc.close();
		
		System.out.println(boxes);
		
		System.out.println("Part 1: " + sum);
		System.out.println("Part 2: " + d.focusPower());

	}
	
	public int hash(String component) {
		int hash = 0;
		for(int i = 0; i < component.length(); i++) {
			hash += (int) component.charAt(i);
			hash *= 17;
			hash %= 256;
		}
		return hash;
	}
	
	public void boxOperation(String component) {
		if(component.contains("=")) {
			String[] split = component.split("="); 
			Lens lens = new Lens(split[0], Integer.parseInt(split[1]));
			int box = hash(lens.getLabel());
			
			if(boxes.containsKey(box)) {
				if(boxes.get(box).contains(lens)) {
					Lens l = boxes.get(box).get(boxes.get(box).lastIndexOf(lens));
					l.setFocalLength(lens.getFocalLength());
				}
				else boxes.get(box).add(lens);
			}
			else {
				ArrayList<Lens> list = new ArrayList<>();
				list.add(lens);
				boxes.put(box, list);
			}
		} else { //Remove if possible.
			String label = component.split("-")[0];
			int box = hash(label);
			if(boxes.containsKey(box)) {
				Lens lens = new Lens(label,0);
				if(boxes.get(box).contains(lens)) boxes.get(box).remove(lens);
			}
		}
	}
	
	public int focusPower() {
		int focusPower = 0;
		for(int box : boxes.keySet()) {
			for(int slot = 0; slot < boxes.get(box).size(); slot++) {
				focusPower += (box + 1) * (slot + 1) *boxes.get(box).get(slot).getFocalLength();
			}
		}
		return focusPower;
	}

}
