
public class BoardNode {

	public char value;
	private boolean hasPeg = false;
	private boolean isEdge = false;
		
	public BoardNode(char val) {
		setValue(val);
		this.hasPeg = true;
		this.isEdge = false;
		
	}
	
	public void setValue(char c) {
		this.value = c;
	}
	
	public char getValue() {
		return this.value;
	}
	
	public boolean getEdgeState() {
		return this.isEdge;
	}
	
	public void setEdgeState(boolean state) {
		this.isEdge = state;
	}
	
	public boolean getPeg() {
		return this.hasPeg;
	}
	
	public void removePeg() {
		this.hasPeg = false;
		this.value = 'O';
	}
	
}
