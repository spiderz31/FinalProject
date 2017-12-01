
public class Node {

	private Board board;
	private Node parent;
	private int MD;
	private int depth;
	private int numberMoved;
	
	public Node(Node parent, int depth, Board board, int lastMovedNumber) {
		setParent(parent);
		setDepth(depth);
		setState(board);
		this.numberMoved = lastMovedNumber;
	}
	
	public Node(Board board) {
		this(null, 0, board, -1);
	}
	
	public int getPathCost() {
		return this.depth;
	}
	
	private void setDepth(int depth) {
		this.depth = depth;
	}
	
	private void setParent(Node parent) {
		this.parent = parent;
	}
	
	public Node getParent() {
		return this.parent;
	}
	
	public void setState(Board board){
		this.board = board;
	}
	
	public Board getState(){
		return this.board;
	}

	public int getLastMovedNumber() {
		return numberMoved;
	}

	public void setLastMovedNumber(int lastMovedNumber) {
		this.numberMoved = lastMovedNumber;
	}

	public int getMD() {
		return MD;
	}

	public void setMD(int mD) {
		MD = mD;
	}
	
}
