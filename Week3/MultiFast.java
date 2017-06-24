import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;


public class MultiFast  {


    
    private ArrayList<LineSegment> segments;
    private Point[] points;
    private int     nPoints;
    private MultiCollinearPoints[] workers; 
    // Define start and and indices for each instance
    // of MultiCollinearPoints
    // Each instance is launched in a thread, and performs
    // collinearity tests on the start-end subset of points.
    // However, the full set is required for each thread
    // to test against.
    private int startSearchIndex;
    private int endSearchIndex;
    
    // finds all line segments containing 4 or more points
    public MultiFast(Point[] pointsArg) {
        
        nPoints           = pointsArg.length;

        // Local copy of the points array
        points = new Point[nPoints];
        for (int i = 0; i < nPoints; i++) {
            points[i] = pointsArg[i];
        }


        // Store all segments in an ArrayList
        segments = new ArrayList<LineSegment>();

        // Are there at least four points in the input data?
        // If not, just skip everything.
        if (nPoints < 4) return;


        final int MAX_THREADS = Runtime.getRuntime().availableProcessors();

        

        // Make sure there are no duplicates, or bad points
        Arrays.sort(points);
        for (int i = 0; i < points.length-1; i++) {
            Point p = points[i];
            Point q = points[i+1];
            if ((p == null) || (q == null)) {
                throw new java.lang.NullPointerException();
            }
            // this test depends on the array being sorted
            if (p.compareTo(q) == 0) {
                throw new java.lang.IllegalArgumentException();
            }
        }

        // Create an array of MultiCollinearPoints objects
        // One object for each thread
        System.out.println( "MAX_THREADS = " + MAX_THREADS );
        
        workers          = new MultiCollinearPoints[MAX_THREADS];
        Thread[] threads = new Thread[MAX_THREADS];

        int num_per_thread = points.length / MAX_THREADS;
        int start = 0;
        int end   = 0;

        // Submit all threads
        for ( int i = 0; i < workers.length; i++ ) {
            if ( i < workers.length-1 ) end = start + num_per_thread;
            else                        end = points.length;
            System.out.println("Multi between " + start + " and " + end );
            workers[i] = new MultiCollinearPoints( points, start, end );
            threads[i] = new Thread( workers[i] );
            threads[i].start();
            start = end;
        }

        // Join all the threds to the main thread
        // No printing of output until everyone is finished.
        try {
            for ( Thread cur_thread : threads ) cur_thread.join();
        } catch ( InterruptedException e ) {
            System.out.println("Main thread Interrupted!");
        }

        /*
        // All together now, print output ...
        for ( int thread = 0; thread < MAX_THREADS; thread++ ) {
            System.out.println("Thread " + thread + " found the following segments:");
            for (LineSegment segment : workers[thread].segments()) {
                StdOut.println(segment);
            }
        }
        */

        // Create a single array of segments
        for ( MultiCollinearPoints mc : workers ) {
            for ( LineSegment ls : mc.segments() ) {
                segments.add( ls );
            }
        }
    }
    
    // the number of line segments
    public  int numberOfSegments() {
       return segments.size();
    }     
    
    // the line segments
    public LineSegment[] segments() { 
        LineSegment[] lsArray = new LineSegment[ this.numberOfSegments() ];
        return segments.toArray(lsArray);
    } 

    public static void main(String[] args) {

        In in = new In(args[0]);
        int N = in.readInt();
        
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        MultiFast mf = new MultiFast( points );
        System.out.println("Found a total of " + mf.numberOfSegments() + " segments.");
        for ( LineSegment ls : mf.segments() ) {
            System.out.println( ls );
        }
    }          
}