import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class PegMain {
	
	//A program to find solutions to various Pegboard Solitare puzzles
	//First we need a graph to store the pegboard.

	private static Scanner scan = new Scanner(System.in);
	
	// 2D arrays inputs
	// array[column number (or coordinate x)][row number (or coordinate y)] format
	/*private static int instance1[][] = {{0, 4, 7}, {1, 2, 8}, {3, 5, 6}};
	private static int instance2[][] = {{0, 1, 4}, {5, 8 ,7}, {2, 3, 6}};
	private static int instance3[][] = {{8, 2, 3}, {6, 5 ,0}, {7, 4, 1}};
	
	//goal state
	private static int goal[][] = {{1, 4, 7 }, {2, 5, 8 }, {3, 6, 0}};
*/
	//control variables
	private static int numExpanded = 0;
	private static final int maxNumExpanded = 100000;
	private static int numStatesPrinted = 0;
	private static final int maxNumStatesPrinted = 5;
	private static long startTime;
	private static long executionTime = 0;
	
	/*
	 * Main program
	 * For each input instance, run the 3 different algorithms
	 * It will run the three algorithms for instance 1, 
	 * then for instance 2 and finally for instance 3.
	 */
	public static void main(String[] args) {
		runAlgorithms();
	}
	
	/*
	 * For each algorithm, prints the required execution information
	 * for reports b), c), and d). The report a) is printed inside the 
	 * code of each algorithm.
	 * 
	 * Each algorithm returns an object of type "Solution"
	 * which contains information about the solution found or null otherwise 
	 */
	private static void runAlgorithms() {
		System.out.println("");
		System.out.println("Press ENTER button to continue... NEXT ALGORITHM: ITERATIVE DEEPENING TREE SEARCH");
		scan.nextLine();
		
		//run algorithm
		startTime = System.currentTimeMillis();
		Solution solution = IDS();
		executionTime = System.currentTimeMillis() - startTime;
		
		//code for question "a" inside the algorithm
		//The variable solution stores the data for question "b"
		System.out.println("");
		if (solution != null) {
			System.out.println("SOLUTION FOUND");
			System.out.println("SEQUENCE OF TILES ID: " + solution.getIdSequence());
			System.out.println("NUMBER OF MOVES: " + solution.getNumberOfMoves());
		}
		else {
			System.out.println("SOLUTION NOT FOUND, MORE THAN " + maxNumExpanded + " NODES EXPANDED");
		}
		//report c)
		System.out.println("NODES EXPANDED: " + numExpanded);
		//report d)
		System.out.println("EXECUTION TIME: " + executionTime + "ms");
		
		
		// Reset global variables for next algorithm
		reset(); 
		
		System.out.println("");
		System.out.println("Press ENTER button to continue... NEXT ALGORITHM: DEPTH FIRST GRAPH SEARCH");
		scan.nextLine();
		startTime = System.currentTimeMillis();
		solution = DFGS();
		executionTime = System.currentTimeMillis() - startTime;
		System.out.println("");
		//report b)
		if (solution != null) {
			System.out.println("SOLUTION FOUND!");
			System.out.println("SEQUENCE OF TILES ID: " + solution.getIdSequence());
			System.out.println("NUMBER OF MOVES: " + solution.getNumberOfMoves());
		} else {
			System.out.println("THE SOLUTION WAS NOT FOUND: MORE THAN " + maxNumExpanded + " NODES EXPANDED");
		}
		//report c)
		System.out.println("NODES EXPANDED: " + numExpanded);
		//report d)
		System.out.println("EXECUTION TIME: " + executionTime + "ms");
		
		// Reset global variables for next algorithm
		reset(); 
		
		System.out.println("");
		System.out.println("Press ENTER button to continue... NEXT ALGORITHM: BREADTH FIRST GRAPH SEARCH");
		scan.nextLine();
		startTime = System.currentTimeMillis();
		solution = BFGS();
		executionTime = System.currentTimeMillis() - startTime;
		System.out.println("");
		//report b)
		if (solution != null) {
			System.out.println("SOLUTION FOUND!");
			System.out.println("SEQUENCE OF TILES ID: " + solution.getIdSequence());
			System.out.println("NUMBER OF MOVES: " + solution.getNumberOfMoves());
		} else {
			System.out.println("THE SOLUTION WAS NOT FOUND: MORE THAN " + maxNumExpanded + " NODES EXPANDED");
		}
		//report c)
		System.out.println("NODES EXPANDED: " + numExpanded);
		//report d)
		System.out.println("EXECUTION TIME: " + executionTime + "ms");
		
		// Reset global variables for next algorithm
		reset(); 
		
		/*
		//A* algorithm
		System.out.println("");
		System.out.println("Press ENTER button to continue... NEXT ALGORITHM: A*");
		scan.nextLine();
		startTime = System.currentTimeMillis();
		Solution aSolution = AStar();
		executionTime = System.currentTimeMillis() - startTime;
		System.out.println("");
		//report b)
		if (aSolution != null) {
			System.out.println("SOLUTION FOUND");
			System.out.println("SEQUENCE OF TILES ID: " + aSolution.getIdSequence());
			System.out.println("NUMBER OF MOVES: " + aSolution.getNumberOfMoves());
		}
		else {
			System.out.println("SOLUTION NOT FOUND, MORE THAN " + maxNumExpanded + " NODES EXPANDED");
		}
		//report c)
		System.out.println("NODES EXPANDED: " + numExpanded);
		//report d)
		System.out.println("EXECUTION TIME: " + executionTime + "ms");
		reset();
		*/
	}
	
	/*
	 * Reset global variables for next algorithm
	 */
	private static void reset() {
		numExpanded = 0;
		numStatesPrinted = 0;
		executionTime = 0;
	}
	
	/*
	 * returns true if input equals goal state
	 */
	static boolean goalTest(Node node) {
		int pegs = 0;
		for(int row = 0; row < 7; row++) {
			for(int col = 0; col < 7; col++) {
				if (node.getState().getBoard()[row][col] == 'p') {
					pegs++;
				}
				if (pegs > 1) {//Change to try different goals
					return false;
				}
			}
		}
		return true;
		//return node.getPathCost() == 31;
	}
	
	
	/*
	 * Returns a list of successors of the node "node"
	 */
	private static ArrayList<Node> expand(Node node) {
		ArrayList<Node> successors = new ArrayList<>();		
		ArrayList<int[]> holeLocations = node.getState().getHoleLocations();
		
		for (int[] holeLocation :holeLocations) {
			// 0 is up, 1 is left, 2 is down, 3 is right
			for (int i = 0; i < 4; i++) {
				int k = i;
				if (i==1) {
					k = 2;
				}
				if (i==2) {
					k = 1;
				}
				Node successor = getSuccessor(node, k, holeLocation);
				if (successor != null) {
					successors.add(successor);
					/*if (numExpanded < 10) {
						System.out.println("SUCCESSOR!!!");
						successor.getState().printBoard();						
					}
					*/
				}
			}
		}

		return successors;
	}
	
	/*
	 * This function is only used by "expand" function.
	 * Returns a successor (Node) given the parent node
	 * and an action
	 * If the action is not possible to perform, returns null.
	 * input action: 0 is up, 1 is left, 2 is down, 3 is right
	 */
	private static Node getSuccessor(Node parent, int action, int[] holeLocation) {
		Board board = parent.getState();
		Board newBoard = null;
		int holeRow = holeLocation[0];
		int holeCol = holeLocation[1];
		
		switch(action) {
			case 0:
				if ( board.isPeg(holeRow+1, holeCol) && board.isPeg(holeRow+2, holeCol)) {
					
					newBoard = board.cloneBoard();
					
					newBoard.getBoard()[holeRow+2][holeCol] = 'O';
					newBoard.getBoard()[holeRow+1][holeCol] = 'O';
					newBoard.getBoard()[holeRow][holeCol] = 'p';
					
				}
				else {
					return null;
				}
				break;
			case 1:
				if ( board.isPeg(holeRow, holeCol+1) && board.isPeg(holeRow, holeCol+2)) {
					
					newBoard = board.cloneBoard();
					
					newBoard.getBoard()[holeRow][holeCol+2] = 'O';
					newBoard.getBoard()[holeRow][holeCol+1] = 'O';
					newBoard.getBoard()[holeRow][holeCol] = 'p';
					
				}
				else {
					return null;
				}
				break;
			case 2:
				if ( board.isPeg(holeRow-1, holeCol) && board.isPeg(holeRow-2, holeCol)) {
					
					newBoard = board.cloneBoard();
					
					newBoard.getBoard()[holeRow-2][holeCol] = 'O';
					newBoard.getBoard()[holeRow-1][holeCol] = 'O';
					newBoard.getBoard()[holeRow][holeCol] = 'p';
					
				}
				else {
					return null;
				}
				break;
			case 3:
				if ( board.isPeg(holeRow, holeCol-1) && board.isPeg(holeRow, holeCol-2)) {
					
					newBoard = board.cloneBoard();
					
					newBoard.getBoard()[holeRow][holeCol-2] = 'O';
					newBoard.getBoard()[holeRow][holeCol-1] = 'O';
					newBoard.getBoard()[holeRow][holeCol] = 'p';
					
				}
				else {
					return null;
				}
				break;
		}
		return new Node(parent, parent.getPathCost()+1, newBoard, 0);
	}
	
	//Pedro
	/* Iterative deepening Tree search algorithm
	 * this algorithm uses the depthLimited tree search algorithm with
	 * different depth levels until finding a solution
	 * It will ends only if it find a solution or if the nodes expanded
	 * are greater than 100,000 (maxNumExpanded)
	 */
	private static Solution IDS() {
		int depth = 0;
		while (numExpanded < maxNumExpanded) {
			Solution solution = depthLimitedTreeSearch(depth);
			if (solution != null) {
				return solution;
			}
			depth++;
			//System.out.println("-------------");
			//System.out.println("new depth IDS");
			//System.out.println("-------------");
		}
		return null;
	}
	
	/* Depth Limited Tree Search algorithm
	 * this algorithm use a LIFO queue to store the fringe, variable "fringe"
	 * 
	 * It will expand the deepest node until finding a solution or until
	 * reach the "depth" limit
	 */
	private static Solution depthLimitedTreeSearch(int depth) {
		Stack<Node> fringe = new Stack<>();
		
		//set initial node
		fringe.push(new Node(new Board()));
		
		//expands until finding a solution or reaching the maximum
		//expanded nodes (100,000)
		while (numExpanded < maxNumExpanded) {
			//if fringe is empty, solution not found.
			if (fringe.isEmpty()) {
				return null;
			}
			
			//pop next node
			Node node = fringe.pop();
			numExpanded ++;
			
			//if goal is reached, return the solution
			if (goalTest(node)) {
				return new Solution(node);
			}
			
			//if goal is not reached, this node will be expanded
			//print it for report "a)"
			if (numStatesPrinted < maxNumStatesPrinted) {
				numStatesPrinted++;
				System.out.println("STATE OF NODE EXPANDED " + numStatesPrinted);
				node.getState().printBoard();
			}
			
			//Expand the node if max depth is not reached
			if (node.getPathCost() < depth) {
				//new nodes contains the new nodes to add to the fringe stack
				ArrayList<Node> newNodes = expand(node);
				
				if (newNodes.isEmpty()) {
					return null;
				}
				
				//before adding the nodes to the fringe stack, order them by
				//the last number moved. This way the next "pop" of the stack
				//will always have the next node to be evaluated using the 
				//tie breaker rule if it is necessary.
				Collections.sort(newNodes, new Comparator<Node>() {
				    @Override
				    public int compare(Node n1, Node n2) {
				        return n2.getLastMovedNumber() - n1.getLastMovedNumber();
				    }
				});
				//finally, add all new nodes to the stack
				fringe.addAll(newNodes);
			}
		}
		//if nodes expanded are greater than maxNumExpanded the solution has not been found
		return null;
	}
	
	// Trevor
	/*
	 *  This function is a search that will not search the same node twice, and will continue
	 *  down one path until it can no longer continue.
	 */
	private static Solution DFGS() {
		
		// Initialize an empty set to contain closed states
		HashSet<Board> closed = new HashSet<>(maxNumExpanded);
		
		// Maintain a stack (LIFO queue). This will replace the global fringe for this search.
		Stack<Node> stack = new Stack<>();
		
		// Add the first node to search
		stack.push(new Node(new Board()));
		
		// Begin loop: if the stack is empty AND max nodes have been reached, terminate
		while (!stack.isEmpty() && numExpanded < maxNumExpanded) {
			
			// Pop off the top of the stack
			Node node = stack.pop();
			numExpanded++;
			// Goal test!
			if (goalTest(node)) {
				stack.pop().getState().printBoard();
				return new Solution(node);
			} else {
				
				if (numStatesPrinted < maxNumStatesPrinted) {
					numStatesPrinted++;
					System.out.println("STATE OF NODE EXPANDED " + numStatesPrinted);
					node.getState().printBoard();
					for (Board c:closed) {
						System.out.println("-----------------");
						c.printBoard();
					}
					System.out.println("closed size: " + closed.size());
					if (numStatesPrinted == maxNumStatesPrinted) 
						System.out.println("Loading...");
				}
				//System.out.println(node.getPathCost());
				Board temp = node.getState();
				if (!existIdentical(closed, temp)) {
					closed.add(temp);
					ArrayList<Node> toAdd = expand(node);
					/*Collections.sort(toAdd, new Comparator<Node>() {
					    @Override
					    public int compare(Node n1, Node n2) {
					        return n2.getLastMovedNumber() - n1.getLastMovedNumber();
					    }
					});*/
					stack.addAll(toAdd);
				}
				
			}
		}	// End while
		//stack.pop().getState().printBoard();
		return null;
	}
	
	//search if contains the same board or symmetrical board
	private static boolean existIdentical(HashSet<Board> closed, Board b) {
		boolean evalNextBoard = false;
		boolean wasIdentical;
		for (Board closedB:closed) {
			for (int rotation = 0; rotation < 4; rotation++) { // 1 same board, 3 rotations
				for (int reflection = 0; reflection < 3; reflection++) { // 1 same board, 2 reflections
					wasIdentical = true;
					for(int row = 0; row < 7; row++) {
						for(int col = 0; col < 7; col++) {
							char bVal = b.getBoard()[row][col];
							
							int cValRow = row;
							int cValCol = col;
							
							if (rotation == 1) {
								cValRow = col;
								cValCol = 6-row;
							}
							else if (rotation == 2) {
								cValRow = 6-row;
								cValCol = 6-col;
							}
							else if (rotation == 3) {
								cValRow = 6-col;
								cValCol = row;
							}

							if (reflection == 1) {
								cValRow = 6-cValRow;
							}
							else if (reflection == 2) {
								cValCol = 6-cValCol;
							}
							
							char cVal = closedB.getBoard()[cValRow][cValCol];
							
							if (bVal != cVal) {
								evalNextBoard = true;
								break;
							}
						}
						if (evalNextBoard) {
							evalNextBoard = false;
							wasIdentical = false;
							break;
						}
					}
					if (wasIdentical) {
						return true;
					}						
				}
			}
		}
		return false;
	}

	private static Solution BFGS() {
		
		// Initialize an empty set to contain closed states
		HashSet<Board> closed = new HashSet<>(maxNumExpanded);
		
		// Maintain a queue (FIFO queue). This will replace the global fringe for this search.
		Queue<Node> queue = new LinkedList<Node>();
		
		// Add the first node to search
		queue.add(new Node(new Board()));
		
		int pruned = 0;
		int lastDepth = 0;
		// Begin loop: if the stack is empty AND max nodes have been reached, terminate
		while (!queue.isEmpty() && numExpanded < maxNumExpanded) {
			
			// Poll off the top of the queue
			Node node = queue.poll();
			numExpanded++;
			// Goal test!
			if (goalTest(node)) {
				return new Solution(node);
			} else {
				
				if (numStatesPrinted < maxNumStatesPrinted) {
					numStatesPrinted++;
					System.out.println("STATE OF NODE EXPANDED " + numStatesPrinted);
					//if (node.getParent()!=null) node.getParent().getState().printBoard();
					//System.out.println("------------------------------");
					node.getState().printBoard();
					System.out.println("closed size: " + closed.size());
					if (numStatesPrinted == maxNumStatesPrinted) 
						System.out.println("Loading...");
				}
				if (node.getPathCost() != lastDepth) {
					System.out.println("new depth" + node.getPathCost());
					System.out.println("pruned" + pruned);
					System.out.println("time" + (System.currentTimeMillis() - startTime));
					pruned = 0;
					lastDepth = node.getPathCost();
				}
				//System.out.println(node.getPathCost());
				Board temp = node.getState();
				if (!existIdentical(closed, temp)) {
				//if (!closed.contains(temp)) {
					closed.add(temp);
					/*Board r1 = temp.rotate();
					closed.add(r1);
					Board r2 = r1.rotate();
					closed.add(r2);
					Board r3 = r2.rotate();
					closed.add(r3);
					*/
					ArrayList<Node> toAdd = expand(node);
					Collections.sort(toAdd, new Comparator<Node>() {
					    @Override
					    public int compare(Node n1, Node n2) {
					        return n2.getLastMovedNumber() - n1.getLastMovedNumber();
					    }
					});
					queue.addAll(toAdd);
				}
				else {
					pruned++;
					if (numStatesPrinted < maxNumStatesPrinted) System.out.println("not expanded");
				}
			}
		}	// End while
		queue.poll().getState().printBoard();
		System.out.println("closed size: " + closed.size());
		return null;
	}
	
	//Josh
	//Here we take a look at the almighty A* algorithm
	//we are using the Manhattan distance hueristic for this algorithim which
	//this means that the agent will know how far each node is away from the goal scenario
	/*private static Solution AStar(int problem[][]){
		
		reset();
		
		System.out.println("-------------------------------------");
		System.out.println("Begin A* Search");
		System.out.println("-------------------------------------");
		
		HashSet<State> closedSet = new HashSet<>(maxNumExpanded);
		
		//first we must initialize the stack, which we will use for this A* search
		Stack<Node> fringe = new Stack<>();
		Node prob = new Node(new State(problem));
		//make the goalState into a proper node, useful for finding manhattan values
		Node goalState = new Node(new State(goal));
		
		getMD(prob.getState() , goalState.getState());
		fringe.add(prob);
		
		//initializing variable to help us detect if we are true or not.
		boolean complete = false;
		
		//check to make sure we don't already have a solved problem
		if(goalTest(problem) == true) {
			complete = true;
		}
		
		//time for the main loop ladies and gents
		while(!fringe.isEmpty() && numExpanded < maxNumExpanded && !complete) {
			
			//lets get the current node
			numExpanded++;
			Node currNode = fringe.pop();
			//System.out.println("We are now examining this node");
			//currNode.getState().print();
			//check to see if the current node is the solution
			if(goalTest(currNode.getState().getBoard()) == true) {
				complete = true;
				System.out.println("Solution");
				currNode.getState().print();
				System.out.println("---------------------------------------");
				System.out.println("End A* Search.");
				System.out.println("---------------------------------------");
				
				return new Solution(currNode);
				
			}
			
			//print out the first 5 nodes
			if(numExpanded <= 5) {
				currNode.getState().print();
			}
			ArrayList<Node> newNodes = new ArrayList<Node>();
			//now we have to take a look at all the different states that can come from here
			//State temp = currNode.getState();
			//took a number from your book Trevor :P
			State temp = currNode.getState();
			if(!closedSet.contains(temp)) {
				closedSet.add(temp);
				newNodes = expand(currNode);
			}
			//calculate their manhattan distance
			//for each loop to run through the new nodes
			
			for(Node n : newNodes) {
				
				//set the manhattan value for this node
				n.setMD(getMD(n.getState() , goalState.getState()) + n.getPathCost());
				fringe.push(n);
				//closedSet.add(n);
				
				
			}
			//sorts in increasing order according to manhattan distance
			Collections.sort(fringe, new Comparator<Node>() {
			    @Override
			    public int compare(Node n1, Node n2) {
			        return n2.getMD() - n1.getMD();
			    }
			});
			
			
		}//end while loop
		System.out.println("NOPE!!!");
		
		return null;
	}
	//function that takes in two states in order to calculate the manhattan distance from a node to the goal state
	//state1 is the state in question and state2 is the goal state
	//returns the manhattan distance of state1
	//do not forget to add the nodes depth, or rather the number of steps it takes to get from the root to the node of state1 after performing this method
	private static int getMD(State state1, State state2) {
		
		int MD = 0;
		int[] C1 = {0,0};
		int[] C2 = {0,0};
		
		for(int i = 0; i < 8; i++) {
			//add the absolute value of the coords of the first number minus the coords of the goal state number
			//first we need the competing coordinates for the current number
			C1 = state1.getNumCoords(i);
			C2 = state2.getNumCoords(i);
			//now we subtract the x coords and y coords and add them to the manhattan distance
			MD +=  Math.abs(C1[0] - C2[0]) + Math.abs(C1[1] - C2[1]);
		}
		
		return MD;
	}
	
	*/
}
