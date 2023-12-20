package AdventOfCode;

public class Broadcaster extends Module{
	private Pulse curr = Pulse.LOW;
	
	public Broadcaster(String name) {
		super(name);
	}

	@Override
	public Pulse sendPulse() {
		return this.curr;
	}

	@Override
	public boolean readyToSend() {
		return true;
	}

	@Override
	public void update(Pulse incPulse, Module inputModule) {
		this.curr = incPulse;
		
	}

	@Override
	public void reset() {
		this.curr = Pulse.LOW;
		
	}
	
}
