package AdventOfCode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.stream.Collectors;



public class Day20Part2 {
	public static void main(String[] args) throws FileNotFoundException {
		File file = new File("C:\\Users\\Andre\\eclipse-workspace\\AdventOfCOde\\src\\testExempel");
		Scanner sc = new Scanner(file);
		Day20Part2 d = new Day20Part2();
		HashMap<String, Module> modules = new HashMap<>();
		HashMap<Module, ArrayList<String>> neighbours = new HashMap<>();
		ArrayList<String> order = new ArrayList<>();
		while(sc.hasNext()) {
			String line = sc.nextLine();
			String[] components = line.split(" -> ");
			String mod = components[0];
			
			Module m;
			String modName;
			if(mod.charAt(0) == '%') {
				modName = mod.substring(1);
				m = new Flipflop(modName);
			}
			else if(mod.charAt(0) == '&') {
				modName = mod.substring(1);
				m = new Conjunction(modName);
			}
			else {
				modName = mod;
				m = new Broadcaster(modName);
			}
			
			neighbours.put(m, new ArrayList<String>());
			modules.put(modName, m);
			order.add(modName);
			
			String[] recievers = components[1].split(", ");
			for(String name : recievers) {
				neighbours.get(m).add(name);
			}
			
		}
		sc.close();
		
		ArrayList<String> targets = d.transmitterConnectedToTarget("rx", neighbours, modules);
		System.out.println(targets.toString());
		ArrayList<String> targetsTargets = new ArrayList<>();
			
		
		for(String target : targets) {
			targetsTargets.addAll(d.transmitterConnectedToTarget(target, neighbours, modules));
		}
		
		ArrayList<Integer> cycles = new ArrayList<>();
		for(String target : targetsTargets) {
			System.out.println(target);
			 
		    d.reset(neighbours,  modules);
			cycles.add(d.cycleLength(target, neighbours, modules));
		}
		System.out.println(cycles.toString());
		
		System.out.println("Part 2: " + MathHelper.lcm_of_array_elements(cycles));
	}
		
	
	public int cycleLength(String target, HashMap<Module, ArrayList<String>> neighbours, HashMap<String, Module> modules) {
		addConjunctionConnections(neighbours, modules);
		int iteration = 0;
		
		
		while(true) {
				Queue<String> transmitters = new LinkedList<>();
				transmitters.add("broadcaster");

				while(!transmitters.isEmpty()) {
					String name = transmitters.poll();
					Module transmitter = modules.get(name);
					ArrayList<String> recievers = neighbours.get(transmitter);
					Pulse p = transmitter.sendPulse();
					if(p == null) continue;
					
					if(name.equals(target) && p == Pulse.HIGH) {
						return iteration + 1;
					}
					
					
					for(int i = 0; i < recievers.size() ;i++) {
							if(!modules.containsKey(recievers.get(i))) continue;
							Module reciever = modules.get(recievers.get(i));
							reciever.update(p, transmitter);
					}
						
					for(int i = 0; i < recievers.size(); i++) {
							if(!modules.containsKey(recievers.get(i))) continue;
							Module reciever = modules.get(recievers.get(i));
							if(reciever.readyToSend()) transmitters.add(reciever.getName());
						}	
				}		
				iteration += 1;
		}
		

	}
	
	
	public ArrayList<String> transmitterConnectedToTarget(String target, HashMap<Module, ArrayList<String>> neighbours, HashMap<String, Module> modules) {
		addConjunctionConnections(neighbours, modules);
		int iteration = 0;
		int max_iteration = 1;
		ArrayList<String> connected = new ArrayList<>();
		while(iteration < max_iteration) {
				Queue<String> transmitters = new LinkedList<>();
				transmitters.add("broadcaster");

				while(!transmitters.isEmpty()) {
					String name = transmitters.poll();
					Module transmitter = modules.get(name);
					ArrayList<String> recievers = neighbours.get(transmitter);
					Pulse p = transmitter.sendPulse();
					if(p == null) continue;
					
					if(recievers.contains(target)) {
						if(!connected.contains(name)) connected.add(name);
					}
					
					
					for(int i = 0; i < recievers.size() ;i++) {
							if(!modules.containsKey(recievers.get(i))) continue;
							Module reciever = modules.get(recievers.get(i));
							reciever.update(p, transmitter);
					}
						
					for(int i = 0; i < recievers.size(); i++) {
							if(!modules.containsKey(recievers.get(i))) continue;
							Module reciever = modules.get(recievers.get(i));
							if(reciever.readyToSend()) transmitters.add(reciever.getName());
						}	
				}		
				iteration += 1;
		}
		
		return connected;
	}
	
	
	

	public void addConjunctionConnections(HashMap<Module, ArrayList<String>> neighbours, HashMap<String, Module> modules) {
		for(Module m : neighbours.keySet()) {
			for(String name : neighbours.get(m)) {
				if(modules.containsKey(name) && modules.get(name).getClass() == Conjunction.class) {
					Module conj = modules.get(name);
					Module connection = m;
					((Conjunction) conj).addToMemory(connection);
				}
			}
		}
	}
	
	public void reset(HashMap<Module, ArrayList<String>> neighbours, HashMap<String, Module> modules) {
		for(String key : modules.keySet()) {
			modules.get(key).reset();
		}
	}
	
	
}
