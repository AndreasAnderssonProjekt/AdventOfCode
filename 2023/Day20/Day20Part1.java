package AdventOfCode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;



public class Day20Part1 {
	public static void main(String[] args) throws FileNotFoundException {
		File file = new File("C:\\Users\\Andre\\eclipse-workspace\\AdventOfCOde\\src\\testExempel");
		Scanner sc = new Scanner(file);
		Day20Part1 d = new Day20Part1();
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
		
		int nrPushes = 1000;
		int[] pulses = d.pressButton(nrPushes, neighbours, modules, order);
		long part1 = pulses[0] * pulses[1];;
		System.out.println("Part 1: " + part1);
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
	
	public int[] pressButton(int nrPushes, HashMap<Module, ArrayList<String>> neighbours, HashMap<String, Module> modules, ArrayList<String> order){
		int nrLow = 0;
		int nrHigh = 0;
		addConjunctionConnections(neighbours, modules);
		for(int push = 0; push < nrPushes; push++) {
			nrLow += 1; // Low singal sent from button to broadcast.
				
				Queue<String> transmitters = new LinkedList<>();
				transmitters.add("broadcaster");

				while(!transmitters.isEmpty()) {
					String name = transmitters.poll();
					Module transmitter = modules.get(name);
					ArrayList<String> recievers = neighbours.get(transmitter);
					Pulse p = transmitter.sendPulse();

					if(p == Pulse.LOW) nrLow += 1 * recievers.size();
					else if(p == Pulse.HIGH) nrHigh += 1 * recievers.size();
					if(p == null) continue;
					
					
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
		}
		return new int[] {nrLow, nrHigh};	
	}
	

}
