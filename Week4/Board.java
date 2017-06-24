// Puzzle board class

import edu.princeton.cs.algs4.In;
import java.util.LinkedList;
import java.util.Arrays;

public class Board {

    private int[][] squares;
    //private int zeroRow = -1;
    //private int zeroCol = -1;

    // Cache computed values
    // once the metrics are computed, they never need to be 
    // re-computed for a given board.
    // Initialize to -1, an impossible value
    // if actually computed
    //private int cacheHamming   = -1;
    //private int cacheManhattan = -1;

    private void exch(int i, int j, int ii, int jj ) {
        int temp        = squares[i][j];
        squares[i][j]   = squares[ii][jj];
        squares[ii][jj] = temp;
    }

    private int getZeroRow() {
        int N = squares.length;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (squares[i][j] == 0) return i;
            }
        }
        return -1;
    }

    private int getZeroCol() {
        int N = squares.length;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (squares[i][j] == 0) return j;
            }
        }
        return -1;
    }

    // FIXME: will want to include a move count
    //        every time a move action is initiated
    private Board moveDown() {
        int N = squares.length;
        int zeroRow = getZeroRow();
        if (zeroRow < N-1) {
            int zeroCol = getZeroCol();
            Board newBoard = new Board(squares);
            newBoard.exch(zeroRow, zeroCol, zeroRow + 1, zeroCol);
            //newBoard.zeroRow = this.zeroRow + 1;
            //newBoard.zeroCol = this.zeroCol;
            //newBoard.moveNum = this.moveNum + 1;
            return newBoard;
        } else {
            return null;
        }
    }

    private Board moveUp() {
        int zeroRow = getZeroRow();
        if (zeroRow > 0) {
            int zeroCol = getZeroCol();
            Board newBoard = new Board(squares);
            newBoard.exch(zeroRow, zeroCol, zeroRow - 1, zeroCol);
            //newBoard.zeroRow = this.zeroRow - 1;
            //newBoard.zeroCol = this.zeroCol;
            //newBoard.moveNum = this.moveNum + 1;
            return newBoard;
        } else {
            return null;
        }
    }

    private Board moveRight() {
        int N = squares.length;
        int zeroCol = getZeroCol();
        if (zeroCol < N-1) {
            int zeroRow = getZeroRow();
            Board newBoard = new Board(squares);
            newBoard.exch(zeroRow, zeroCol, zeroRow, zeroCol + 1);
            //newBoard.zeroRow = this.zeroRow;
            //newBoard.zeroCol = this.zeroCol + 1;
            //newBoard.moveNum = this.moveNum + 1;
            return newBoard;
        } else {
            return null;
        }
    }

    private Board moveLeft() {
        int zeroCol = getZeroCol();
        if (zeroCol > 0) {
            int zeroRow = getZeroRow();
            Board newBoard = new Board(squares);
            newBoard.exch(zeroRow, zeroCol, zeroRow, zeroCol - 1);
            //newBoard.zeroRow = this.zeroRow;
            //newBoard.zeroCol = this.zeroCol - 1;
            //newBoard.moveNum = this.moveNum + 1;
            return newBoard;
        } else {
            return null;
        }
    }

    private void printBoard() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.toString());
        sb.append("\n(");
        //sb.append(zeroRow);
        //sb.append(",");
        //sb.append(zeroCol);
        sb.append(")\n");
        //sb.append("Move:" + this.moveNum + "\n");
        sb.append("Hamming:" + this.hamming() + " Manhattan:" + this.manhattan());
        System.out.println(sb.toString());
    }


    public Board(int[][] blocks)  {
        int N = blocks.length;

        // allocate private space for board
        squares = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                squares[i][j] = blocks[i][j];
                //if (blocks[i][j] == 0) {
                //    // found the sliding piece
                //    zeroCol = j;
                //    zeroRow = i;
                //}
            }
        }
        //if (zeroRow == -1 || zeroCol == -1) {
        //    System.out.println("No sliding piece in board!");
        //    throw new NullPointerException();
        //}
    }         

    
    public int dimension()   { return squares.length; }

                  
    public int hamming()     {
        int N = squares.length;
        //if (cacheHamming != -1) return cacheHamming;
        int hamm = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int goal = i*N + j + 1;
                int val  = squares[i][j];
                if (val != goal && val !=0) {
                    //System.out.println("mismatch in (" + i + "," + j + ")");
                    ++hamm;
                }
            }
        }
        //cacheHamming = hamm;
        return hamm;
    }
    
    
    public int manhattan() {
        int N = squares.length;
        //if (cacheManhattan != -1) return cacheManhattan;
        int man = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int goal = i*N + j + 1;
                int val  = squares[i][j];
                if (val != goal && val !=0) {
                    // get the vertical and horizontal offsets
                    // from the position of the value to the target
                    int targetCol  = (val - 1) % N;
                    int targetRow  = (val-targetCol) / N;
                    int deltaMan   = Math.abs(targetCol-j) + Math.abs(targetRow-i);
                    man += deltaMan;
                    /*
                    System.out.println("Expected " 
                                + val + " in (" 
                                + targetRow + "," 
                                + targetCol + ")"  + " deltaMan:" + deltaMan);
                                */
                }
            }
        }
        //cacheManhattan = man;
        return man;
    }            

    
    // Does this match the goal board?
    public boolean isGoal()  { return (hamming() == 0); }              
    
    // a board that is obtained by exchanging any pair of blocks
    public Board twin()  {
        int N = this.squares.length;
        Board newBoard = new Board(this.squares);
        int zeroRow = this.getZeroRow();

        // find a suitable pair of blocks to exchange
        // Neither block can be the 0 element
        for(int row = 0; row < N; row++) {
            if (row != zeroRow) {
                newBoard.exch(row, 0, row, 1);
                break;
            }
        }
        return newBoard;
    }                  
    
    // does this board equal y?
    // FIXME: test for Board class using getClass()
    // Use Arrays.equal or Arrays.deepEqual to test
    // for equality of arrays
    public boolean equals(Object y)  {
        // if y is uninitialized, alutomatically false
        if (y == null) return false;

        // Check that y is of Board class and cast
        if (y.getClass() != this.getClass()) return false;


        // Cast the Object y as a board
        Board that = (Board) y;
        return Arrays.deepEquals(this.squares, that.squares);
    }      

    // all neighboring boards
    
    public Iterable<Board> neighbors() {
        // Build a lits of Boards which represent
        // all the legal moves of the zero
        LinkedList<Board> neighborBoards = new LinkedList<Board>();

        // Try moving in all directions, add if legal
        Board up    = this.moveUp();
        Board down  = this.moveDown();
        Board left  = this.moveLeft();
        Board right = this.moveRight();

        if (up    != null)    neighborBoards.add(up);
        if (down  != null)    neighborBoards.add(down);
        if (left  != null)    neighborBoards.add(left);
        if (right != null)    neighborBoards.add(right);

        return neighborBoards;
    } 
      
    public String toString() {
        int N = squares.length;
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", squares[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    

    public static void main(String[] args) {
     
        // create initial board from file
        In in = new In(args[0]);

        // get size of board
        int N = in.readInt();
        int[][] blocks = new int[N][N];

        // read board data
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();

        Board initial = new Board(blocks);

        // Now prinnt current board
        System.out.println("Initial board:");
        System.out.println(initial);

        System.out.println("Dimension:" + initial.dimension());

        // Get the Hamming distance for this board
        System.out.println("Hamming distance:" + initial.hamming());

        // Get the Manhattan distance for this board
        System.out.println("Manhattan distance:" + initial.manhattan());

        // Is this the goal board?
        System.out.println("Goal? " + initial.isGoal() );

        // Create twin board which exchanges the first two elements
        Board twin = initial.twin();
        System.out.println("Twin: " + twin);

        // Test for equality
        //System.out.println("Equal to self: " + initial.equals(initial));
        System.out.println("Equal to twin: " + initial.equals(twin));
        
        
        // Now get all legal moves from the 'right' position
        System.out.println("Legal moves for initial:" + initial);
        System.out.println("Moves are:");
        for (Board curBoard : initial.neighbors()) {
            curBoard.printBoard();
        }
    }
}