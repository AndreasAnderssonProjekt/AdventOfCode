package AdventOfCode;

import java.util.HashMap;

public class Conjunction extends Module{
	private HashMap<Module, Pulse> memory = new HashMap<>();
	
	public Conjunction(String name) {
		super(name);
	}

	@Override
	public void update(Pulse incPulse, Module inputModule) {
		memory.put(inputModule, incPulse);
	}
	
	public void clearMemory() {
		this.memory.clear();
	}

	@Override
	public Pulse sendPulse() {
		boolean allHigh = true;
		for(Pulse p : memory.values()) {
			if(p == Pulse.LOW ) allHigh = false;
		}
		if(allHigh) return Pulse.LOW;
		else return Pulse.HIGH;
	}

	@Override
	public boolean readyToSend() {
		return true;
	}
	
	public void addToMemory(Module m) {
		this.memory.put(m, Pulse.LOW);
	}

	@Override
	public void reset() {
		memory = new HashMap<>();
		
	}
	
}
