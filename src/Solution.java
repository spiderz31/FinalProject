/*
 * CS4750/7750 HW #2
 * Pedro Carrion Castagnola
 * Trevor Levins
 * Joshua Lewis
 */

/*
 * Helper class to print the solution for the report b)
 * numberOfMoves: total number of moves of the solution
 * idSequences: sequence of tile numbers (or IDs) for reaching the solution
 */
public class Solution {
	
	private int numberOfMoves;
	private String idSequence;
	
	public Solution(Node node) {
		this.numberOfMoves = node.getPathCost();
		setIdSequence(node);
	}

	private void setIdSequence(Node node) {
		idSequence = Integer.toString(node.getLastMovedNumber());
		Node parent = node.getParent();
		//limit the maximum number of solution sequence to 1000,
		//otherwise, it took too much time to print big solution sequences
		int i = 0;
		while (parent != null && parent.getLastMovedNumber() != -1 && i < 1000) {
			idSequence = parent.getLastMovedNumber() + ", " + idSequence;
			parent = parent.getParent();
			i++;
		}
	}

	public int getNumberOfMoves() {
		return numberOfMoves;
	}

	public String getIdSequence() {
		return idSequence;
	}
}
