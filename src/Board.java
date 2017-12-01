import java.util.ArrayList;
import java.util.Arrays;

public class Board {
	
	private Space[][] board = new Space[7][7];
	
	public Board(boolean emptyBoard) {
		if (!emptyBoard) {
			setUpBoard();
		}
	}
	
	public Board() {
		setUpBoard();
	}
	
	private void setUpBoard() {
		for(int i = 0; i < 7; i++) {
			for(int j = 0; j < 7; j++) {
				//remember, row/col
				if((j == 0 || j == 1 || j == 5 || j == 6) && (i == 0 || i == 1 || i == 5 || i == 6)) {
					//set as a #
					board[j][i] = new Space('#');
				}
							
				else {
					//set as a peg p
					board[j][i] = new Space('p');
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
	
	public void setBoard(Space[][] board) {
		this.board = board;
	}
	

	// To better check whether two states are equal or not.
	// Two states are equal IF their 2D array is the same.
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(board);
		return result;
	}

	// To better check whether two states are equal, this must also
	// be overridden
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Board other = (Board) obj;
		if (!Arrays.deepEquals(board, other.board))
			return false;
		return true;
	}
	
	public Space[][] getBoard() {
		return board;
	}
	/*
	public int[] getDotCoords() {
		return getNumCoords(0);
	}
	
	//function to get coordinates for any given number
	public int[] getNumCoords(int num) {
		for (int x = 0; x <= 2; x++) {
			for (int y = 0; y <= 2 ; y++) {
				if (this.board[y][x] == num) {
					int[] coords = {y,x};
					return coords;
				}
			}
		}
		System.out.println("Error in coord finding!");
		return null;
	}
		
	//change the position of two numbers in the board and returns
	//a new state
	public int[][] swap(int x, int y, int nx, int ny){
		int[][] newState = new int[3][3];;
		for(int i=0; i<board.length; i++) {
			for(int j=0; j<board[i].length; j++) {
				newState[i][j]=board[i][j];	
			}
		}
		newState[x][y] = board[nx][ny]; 	
		newState[nx][ny] = board[x][y];
		return newState;
	}
	*/

	public ArrayList<int[]> getHoleLocations() {
		ArrayList<int[]> holeLocations = new ArrayList<int[]>();
		for(int row = 0; row < 7; row++) {	
			for(int col = 0; col < 7; col++) {
				if (board[row][col].getValue()=='O') {
					int[] hLoc = new int[2];
					hLoc[0] = row;
					hLoc[1] = col;
					holeLocations.add(hLoc);
				}
			}
		}
		return holeLocations;
	}

	public boolean isPeg(int row, int col) {
		if (row < 0 || row > 6 || col < 0 || col > 6) {
			return false;
		}
		Space value = board[row][col];
		if (value.getValue() == 'p') {
			return true;
		}
		return false;
	}

	public Board cloneBoard() {
		Board clonedBoard = new Board(true);
		//clonedBoard.setBoard(board.clone());
		for(int row = 0; row < 7; row++) {	
			for(int col = 0; col < 7; col++) {
				clonedBoard.getBoard()[row][col] = new Space(board[row][col].getValue());
			}
		}
		return clonedBoard;
	}	
	
}
