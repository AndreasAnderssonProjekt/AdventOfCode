package AdventOfCode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Day19 {

	public class Rule{
		private String category;
		private String operator;
		private int number;
		private String destination;
		public Rule(String category, String operator, int number, String destination) {
			this.category = category;
			this.operator = operator;
			this.number = number;
			this.destination = destination;
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		File file = new File("C:\\Users\\Andre\\eclipse-workspace\\AdventOfCOde\\src\\testExempel");
		Scanner sc = new Scanner(file);
		boolean readWorkflow = true;
		ArrayList<String> raitings = new ArrayList<>();
		HashMap<String, String[]> workflows = new HashMap<>();
		while(sc.hasNext()) {
			String line = sc.nextLine();
			
			if(line.length() == 0) { // Blank space found -> Switch to storing raitings.
				readWorkflow = false;
				continue;
			}
			if(readWorkflow) { // Store workflows.
				String[] components = line.split("\\{");
				String workflowLabel = components[0];
				String[] rules = components[1].substring(0,components[1].length() - 1).split(",");
				workflows.put(workflowLabel, rules);
			}
			else { // Store raitings.
				raitings.add(line.substring(1, line.length()-1));
			}
			
		}
		sc.close();
		
		//System.out.println(workflows);
		System.out.println("Part 1: " + acceptedSum(workflows, raitings));
		
		System.out.println("Part 2: " + part2(workflows));
		
		
	}
	
	
	public static long part2(Map<String, String[]> workflows) {
		long sum = 0;
		Map<String, long[]> ranges = new HashMap<>();
		ranges.put("x", new long[] {1, 4000});
		ranges.put("m", new long[] {1, 4000});
		ranges.put("a", new long[] {1, 4000});
		ranges.put("s", new long[] {1, 4000});
	    
		
		return countPossible(ranges,  workflows, "in");
	}
	
	public static long countPossible(Map<String, long[]> ranges, Map<String, String[]> workflows, String curr) {
		long possible = 0;
		if(curr.equals("A")) {
			long sum = 1;
			for(long[] range : ranges.values()) {
				sum *= range[1] - range[0] + 1;
			}
			return sum;
		}
		else if(curr.equals("R")) {
			return 0;
		}
		else {
			boolean broke = false;
			for(int i = 0; i < workflows.get(curr).length - 1; i++) {
				
				String rule = workflows.get(curr)[i];
				
				String[] split = rule.split(":");
				String category = split[0].substring(0,1);
				String operator = split[0].substring(1,2);
				long number = (long) Integer.parseInt(split[0].substring(2));
				String nextWorkFlow = split[1];
				long lo = ranges.get(category)[0];
				long hi = ranges.get(category)[1];
				long[] passPart = null;
				long[] failPart = null;
 				if(operator.equals("<")) {
					passPart = new long[] {lo, number - 1};
					failPart = new long[] {number, hi};
				}
				else {
					passPart = new long[] {number + 1, hi};
					failPart = new long[] {lo, number};
				}
				
				if(passPart[0] <= passPart[1]) {
					ranges =  ranges.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> (e.getValue().clone())));
					ranges.put(category, passPart);
					possible += countPossible(ranges, workflows, nextWorkFlow);
				}
				
				if(failPart[0] <= failPart[1]) {
					ranges =  ranges.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> (e.getValue().clone())));
					ranges.put(category, failPart);
				}
				else {
					broke = true;
					break;
				}
			}
			
			if(!broke) {
					String nextWorkFlow = workflows.get(curr)[workflows.get(curr).length - 1];
					possible += countPossible(ranges, workflows, nextWorkFlow);
					
				}
		}
			
		
		return possible;
	}
	
	
	
	
	
	
	
	//Part 1
	public static int acceptedSum(HashMap<String, String[]> workflows, ArrayList<String> raitings) {
		int sum = 0;
		for(String raiting : raitings) {
			String[] r = raiting.split(",");
			HashMap<String, Integer> raitingMap = raitingMap(r);
			if(accepted(workflows, raitingMap)) {
				for(String cat : raitingMap.keySet()) {
					sum += raitingMap.get(cat);
				}
			}
		}
		return sum;
	}
	
	public static boolean accepted(HashMap<String, String[]> workflows, HashMap<String, Integer> raitingMap) {
		String[] rules = workflows.get("in");
		while(true) {
			String nextFlow = nextWorkFlow(rules, raitingMap);
			if(nextFlow.equals("A")) return true;
			if(nextFlow.equals("R")) return false;
			rules = workflows.get(nextFlow);
		}
 	}
	
	public static HashMap<String, Integer> raitingMap(String[] raiting){
		HashMap<String, Integer> map = new HashMap<>();
		for(String r : raiting) {
			String[] r_split = r.split("=");
			map.put(r_split[0], Integer.parseInt(r_split[1]));
		}
		return map;
	}
	
	public static String nextWorkFlow(String[] rules, HashMap<String, Integer> raitingMap ) {
		for(int i = 0; i < rules.length; i++) {
			
			if(i == rules.length - 1)  return rules[i]; //Didn't satisfy any raiting.
			else {
				String[] r = rules[i].split(":");
				if(satisfyRule(r[0], raitingMap)) return r[1];
			}
		}
		return null;
	}
	
	
	public static boolean satisfyRule(String rule, HashMap<String, Integer> raitingMap ) {
		String category = rule.substring(0,1);
		String operator = rule.substring(1,2);
		int number = Integer.parseInt(rule.substring(2));
		
		if(operator.equals(">")) {
			return raitingMap.get(category) > number;
		}
		else if(operator.equals("<")) {
			return raitingMap.get(category) < number;
		}
		else if(operator.equals("=")) {
			return raitingMap.get(category) == number;
		}
		
		return false;
	}
	
	
	public String nextWorkflow() {
		return "";
	}

}
