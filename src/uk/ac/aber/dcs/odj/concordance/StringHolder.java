package uk.ac.aber.dcs.odj.concordance;

public class StringHolder extends CustomNode {
	private String string;
	
	public StringHolder() {}
	
	public StringHolder(String s) {
		this.set(s);
	}
	
	public void set(String s) {
		this.string = s;
	}
	
	public String get() {
		return this.string;
	}
}
