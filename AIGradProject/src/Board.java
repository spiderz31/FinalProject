
public class Board {
	
	private BoardNode[][] board = new BoardNode[7][7];
	
	public Board() {
		setUpBoard();
		printBoard();
	}
	
	private void setUpBoard() {
		
		for(int i = 0; i < 7; i++) {
			
			for(int j = 0; j < 7; j++) {
				
				//remember, row/col
				if((j == 0 || j == 1 || j == 5 || j == 6) && (i == 0 || i == 1 || i == 5 || i == 6)) {
					//set as a #
					board[j][i] = new BoardNode('#');
				}
							
				else {
					//set as a peg p
					board[j][i] = new BoardNode('p');
				}
			}
		}
		//pull the peg from the center
		board[3][3].removePeg();
		
		
	}
	
	public void printBoard() {
		for(int i = 0; i < 7; i++) {
			
			for(int j = 0; j < 7; j++) {
			
				System.out.print(board[j][i].getValue()+" ");
				
			}
			System.out.println();
	
		}
	}
	
}
