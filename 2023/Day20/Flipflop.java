package AdventOfCode;

public class Flipflop extends Module{
	private boolean on = false;
	private Pulse pulse = null;
	
	public Flipflop(String name) {
		super(name);
	}

	public boolean getStatus() {
		return this.on;
	}
	
	public void setStatus(boolean status) {
		this.on = status;
	}
	
	@Override
	public void update(Pulse incPulse, Module inputModule) {
		this.pulse = incPulse;
		if(incPulse == Pulse.HIGH) {
			return;
		}
		boolean status = this.getStatus();
		if(incPulse == Pulse.LOW) this.setStatus(!status);
	}

	@Override
	public Pulse sendPulse() {	
		boolean status = this.getStatus();
		if(status) return Pulse.HIGH;
		else return Pulse.LOW;	
	}

	@Override
	public boolean readyToSend() {
		return (this.pulse != Pulse.HIGH);
	}
	
}
