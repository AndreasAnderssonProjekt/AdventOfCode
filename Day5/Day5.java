import java.io.File;
import java.io.FileNotFoundException;
import java.security.KeyStore.Entry;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class Day5 {
	private static final int nrOfMaps = 7;
	private static List<Map<Long[],Long[]>> maps;
	
	public static void main(String[] args) throws FileNotFoundException, NumberFormatException {
		File file = new File("C:\\Users\\Andre\\eclipse-workspace\\Testing\\src\\Day5.txt");
		
		for(boolean part1 : new boolean[] {true, false}) {
			maps = initializeMaps(nrOfMaps);
			Scanner sc = new Scanner(file);
			int mapNr = 0;
			boolean skipLine = false; // If we find an empty line skipLine is set to true.
			while(sc.hasNext()) {
				String line = sc.nextLine().trim();
				String[] nrs = line.split("\\s+");
				if(skipLine) { //Previous line was empty. Skip this one as it is a map title.
					skipLine = false;
					continue;
				}
				
				if(line == "" || mapNr == 0) { //Compute the values for the current mapping and initialise keys for the next map.
					initializeMap(nrs, mapNr, part1);
					skipLine = true;
					mapNr += 1;
					continue;
				}
				
				if(lineIsMapTitle(line, mapNr)) continue; //Skip the line if it  is a title of a map.
				
				updateMap(nrs, mapNr);
				
			}
			String pre = (part1) ? "Part1: " : "Part2: ";
			
			
			System.out.println(pre + computeSmallestValue(maps.size()-1));
		}
		
	}
	
	// HELPER METHODS
	
	private static List<Map<Long[],Long[]>> initializeMaps(int nrOfMaps){
		 List<Map<Long[],Long[]>> maps = new ArrayList<>();
		 for(int i = 0; i < nrOfMaps; i++) {
			 maps.add(new HashMap<Long[],Long[]>());
		 }
		 return maps;
	}
	
	private static void initializeMap(String[] nrs, int mapNr, boolean part1) {
		if(mapNr >= maps.size()) return;
		if(mapNr == 0) initializeSeeds(nrs, mapNr, part1);		
		else initializeNonSeedMap(nrs, mapNr);
	}
	
	private static void initializeSeeds(String[] nrs, int mapNr, boolean part1) {
		if(part1) addSeedNumbers(nrs, mapNr);
		else addSeedsInRange(nrs, mapNr);
	}
	
	private static void addSeedNumbers(String[] nrs, int mapNr) {
		for(int i = 1; i < nrs.length; i++) {
			long key = Long.parseLong(nrs[i]);
			maps.get(mapNr).put(new Long[]{key,key},new Long[]{key,key});
		 }
		
	}
	
	private static void addSeedsInRange(String[] nrs, int mapNr) {
		for(int i = 1; i < nrs.length - 1; i = i + 2) {
			long seedStart = Long.parseLong(nrs[i]);
			long seedRangeLength = Long.parseLong(nrs[i+1]);
			long key_lower = seedStart;
			long key_upper = seedStart + seedRangeLength - 2;
			maps.get(mapNr).put(new Long[]{key_lower, key_upper}, new Long[]{key_lower, key_upper});
			
		}
	}
	
	private static void initializeNonSeedMap(String[] nrs, int mapNr) {
		for(Long[] key : maps.get(mapNr-1).keySet()) {
			Long[] v = maps.get(mapNr-1).get(key);
			maps.get(mapNr).put(v,v);
		}
	}
	
	private static void updateMap(String[] nrs, int mapNr) {
		long start1 = Long.parseLong(nrs[0]);
		long start2 = Long.parseLong(nrs[1]);
		long rangeLength = Integer.parseInt(nrs[2]);
		ArrayList<Long[]> keysToRemove = new ArrayList<>();
		ArrayList<Long[]> keysToAdd = new ArrayList<>();
		ArrayList<Long[]> valuesToAdd = new ArrayList<>();
		for(Long[] key : maps.get(mapNr-1).keySet()) {
			long key_lower = key[0];
			long key_upper = key[1];
			
			Long[] boundary = getMappingBoundary(key_lower, key_upper, start2, rangeLength);
			long first_inRange = boundary[0];
			long last_inRange = boundary[1];
			
				if(first_inRange != -1 ) {// Found non-trivial map.
					keysToRemove.add(key);
					
					Long[] mapTo = new Long[] {start1 + (first_inRange - start2), start1 + (last_inRange - start2)};
					
					keysToAdd.add(new Long[] {first_inRange, last_inRange});
					valuesToAdd.add(mapTo);
					if(first_inRange > key_lower) {
						keysToAdd.add(new Long[] {key_lower, first_inRange});
						valuesToAdd.add(new Long[] {key_lower, first_inRange});
					}
					if(last_inRange < key_upper) {
						keysToAdd.add(new Long[] {last_inRange, key_upper});
						valuesToAdd.add(new Long[] {last_inRange, key_upper});
						
					}
				}			
	}
		updateMap(keysToRemove, keysToAdd, valuesToAdd, mapNr-1);
	}
		

	private static void updateMap(ArrayList<Long[]> keysToRemove, ArrayList<Long[]> keysToAdd, ArrayList<Long[]> valuesToAdd, int mapNr){
		
		
		for(Long[] key : keysToRemove) {
			maps.get(mapNr).remove(key);
		}
		
		for(int i = 0; i < keysToAdd.size(); i++) {
			Long[] key = keysToAdd.get(i);
			Long[] value = valuesToAdd.get(i);
			maps.get(mapNr).put(key, value);
		}
	}
	
	
	private static Long[] getMappingBoundary(long key_lower, long key_upper, long start, long rangeLength) {
		long first_inRange;
		long last_inRange;
		
		if(key_upper <= start || key_lower >= start + rangeLength) { // Trivial map.
			first_inRange = -1;
			last_inRange = -1;
		}
		else {
			boolean withinUpperLimit = (key_upper <= start + rangeLength);
			boolean witinLowerLimit = (key_lower >= start);
			if(withinUpperLimit && witinLowerLimit) {
				first_inRange = key_lower;
				last_inRange = key_upper;
				;
			}
			else if(withinUpperLimit) {
				first_inRange = start;
				last_inRange = key_upper;
			}
			else {
				first_inRange = key_lower;
				last_inRange = start + rangeLength;
			}
		}
		
		return new Long[] {first_inRange, last_inRange};
		
	}
	private static boolean withinUpperLimit(long key_upper, long start, long length) {
		return (key_upper <= start + length);
	}
	
	private static boolean withinLowerLimit(long key_lower, long start) {
		return(key_lower >= start);
	}
	
	private static boolean lineIsMapTitle(String line, int mapNr) {
		return !Character.isDigit(line.charAt(0));
	}
	
	private static long computeSmallestValue(int mapNr) {
		long smallestValue = Long.MAX_VALUE;	
		for(Long[] key : maps.get(mapNr).keySet()) {
			if(maps.get(mapNr).get(key)[0] < smallestValue) smallestValue = maps.get(mapNr).get(key)[0];
		}
		return smallestValue;

}
}
