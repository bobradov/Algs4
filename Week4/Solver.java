// Solver class for puzzle
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.LinkedList;


public class Solver {

    private LinkedList<Board> history;
    private int neededMoves;

    // Internal class for searchnode
    // This needs to store a game state
    // implements Comparable so that a priority queue can
    // sort SearchNodes
    private class SearchNode implements Comparable<SearchNode> {

        private Board board;
        private Board prevBoard;
        private int   priority;
        private int   move;

        private SearchNode prevNode;

        private SearchNode(Board boardArg, 
                           Board prevBoardArg, 
                           int priorityArg, 
                           int moveArg,
                           SearchNode prevNodeArg) {
            board     = boardArg;
            prevBoard = prevBoardArg;
            priority  = priorityArg;
            move      = moveArg;
            prevNode  = prevNodeArg;
        }

        // The only public interface to the SearchNode class
        // This allows it to be used inside if the priority queue
        // Everything else is accessible only from a solver
        // object (including node creation)
        public int compareTo(SearchNode that) {
            if (this.priority < that.priority) return -1;
            if (this.priority > that.priority) return  1;
            return 0; // equal
        }

        private Board getBoard()     { return board; }
        private Board getPrevBoard() { return prevBoard; }
        private int   getMove()      { return move; }

        // Link to previously discovered node
        // This must be in order of discovery, not
        // removal from PQ
        private SearchNode getPrevNode() { return prevNode; }
    }

    private class Stepper {
        // Reference to a priority queue for SeacrhNodes
        private MinPQ<SearchNode> pq;
        private SearchNode        curNode;
        private int               curMove;
        private Board             curBoard;

        // Number of moves needed to complete the puzzle
        //private int neededMoves;


        private Stepper(Board initial) {
            // Initialize priority queue
            pq = new MinPQ<SearchNode>();
            curMove          = 0;
            int initPriority = initial.manhattan() + curMove;

            // Initial node has no previous board, hence null
            // Also no parent SearchNode, hence null
            curNode = new SearchNode(initial, 
                                        null, 
                                        initPriority,
                                        curMove,
                                        null);

            //System.out.println("Inserting initial node:");
            pq.insert(curNode);

        }

        private SearchNode getCurNode() { return curNode; }

        private boolean isGoal() { return curNode.getBoard().isGoal(); }

        private int getCurMove() { return curMove; }

        private void nextStep() {
            curNode            = pq.delMin();
            curMove            = curNode.getMove();
            curBoard           = curNode.getBoard();
            Board prevBoard    = curNode.getPrevBoard();

            //System.out.println(curBoard);
            //System.out.println("Move: " + curMove);
        
            // Get legal moves from this board
            //System.out.println(">>>> Legal moves:");
            for ( Board nextBoard : curBoard.neighbors() ) { 
                // The move number associated with the childredn
                // of curBoard is one larger than that of curBoard
                // same for all the children
                int nextMove = curMove + 1;

                // Make sure this board did not appear in the previous
                // iteration; otherwise, waste of time
                if (! nextBoard.equals(prevBoard)) {
                    int nextPriority = nextBoard.manhattan() + nextMove;
                    // System.out.println( nextBoard + " priority: " + nextPriority);
                    // Create a new searchnode
                    // Searchnode includes new board, its parent, and computed
                    // priority (metric + move)
                    // Also includes a reference to the parent node
                    pq.insert(new SearchNode(nextBoard, 
                                             curBoard, 
                                             nextPriority,
                                             nextMove,
                                             curNode));
                }
            }
        }
    }



    

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial)  {
        // Is it a valid Board?
        if (initial == null) {
            throw new java.lang.NullPointerException();
        }
        
        // Point to initial board, and create its twin
        Board curRealBoard = initial;
        Board curTwinBoard = initial.twin();

        //System.out.println("Board" + curRealBoard + " Twin" + curTwinBoard);

        int curStep = 0;
        

        Stepper realStepper = new Stepper(curRealBoard);
        Stepper twinStepper = new Stepper(curTwinBoard);
        //System.out.println("Working ...");

        while (! realStepper.isGoal() && ! twinStepper.isGoal() ) {
            ++curStep;
            realStepper.nextStep();
            twinStepper.nextStep();
        }

        //System.out.println("Steps taken: " + curStep);
        // Which stepper finished?

        if (realStepper.isGoal()) {
            // Found solution
            neededMoves = realStepper.getCurMove();
            history = new LinkedList<Board>();

            // Backtrack to find solution history
            SearchNode tempNode = realStepper.getCurNode(); // curNode has the solution board

            while (tempNode != null) {
                history.addFirst(tempNode.getBoard());
                tempNode = tempNode.getPrevNode();
            
            }
        } else {
            // Found solution for twin
            // This means no solution for real board
            // No building of history
            //System.out.println("Twin solved in " + curStep + " steps.");
            neededMoves = -1;
            history     = null;
        } 
        
    }         
    
    
    public boolean isSolvable()   { return neededMoves > -1; }
    
    // min number of moves to solve initial board; -1 if unsolvable
    public int moves()     { return neededMoves; } 

    // sequence of boards in a shortest solution; null if unsolvable               
    public Iterable<Board> solution() { return history; }      
      
        
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];

        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
            blocks[i][j] = in.readInt();
        
        Board  initial = new Board(blocks);
        Solver solver = new Solver(initial);
        
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            for (Board board : solver.solution()) {
                StdOut.println(board);
            }
            StdOut.println("Minimum number of moves = " + solver.moves());
        }
    }
}
