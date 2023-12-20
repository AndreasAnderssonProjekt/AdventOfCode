package AdventOfCode;

public abstract class Module {
	private String name;
	
	public Module(String name) {
		this.name = name;
	}
	
	@Override
	public boolean equals(Object m) {
		return this.name == ((Module) m).getName(); 
	}
	
	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
	
	public String getName() {
		return this.name;
	}
	
	public abstract void update(Pulse incPulse, Module inputModule);
	
	public abstract Pulse sendPulse();
	
	public abstract  boolean readyToSend();
}
