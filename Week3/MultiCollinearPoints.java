
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;


public class MultiCollinearPoints implements Runnable {


    
    private ArrayList<LineSegment> segments;
    private Point[] points;
    private int     nPoints;
    // Define start and and indices for each instance
    // of MultiCollinearPoints
    // Each instance is launched in a thread, and performs
    // collinearity tests on the start-end subset of points.
    // However, the full set is required for each thread
    // to test against.
    private int startSearchIndex;
    private int endSearchIndex;
    
    // finds all line segments containing 4 or more points
    public MultiCollinearPoints(Point[] pointsArg, int startArg, int endArg) {
        // Create a local copy of the pointsArg array
        startSearchIndex  = startArg;
        endSearchIndex    = endArg;
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
    }

    public void run() {
        
        // Outer loop only for subset of points belonging
        // to this thread
        // Inner loop over all points
        
        for (int i = startSearchIndex; i < endSearchIndex; i++) {
            Point p = points[i];
            Point[] targetPoints = new Point[nPoints-1];
            
            Comparator<Point> comp = p.slopeOrder();
            
            //System.out.println("Working on " + i + " with point " + p);

            int index = 0;
            for (int j = 0; j < nPoints; j++) {
                if (i != j) {
                    targetPoints[index] = points[j];
                    ++index;
                }
            }
            
            // targetPoints now has all possible targets
            // sort the array using a comparator based on slope
            Arrays.sort(targetPoints, comp);
            
            // now find all the points that have the same slope to p
            double startSlope = p.slopeTo(targetPoints[0]);
            int startIndex    = 0;
            int endIndex      = 0;
            //System.out.print("Slopes to point " + p + " = ");
            
            for (int count = 0; count < targetPoints.length; count++) {
                Point curPoint = targetPoints[count];
                double curSlope = p.slopeTo(curPoint);
                //System.out.print(curSlope + " from " + curPoint + " ");
                
                if (curSlope == startSlope) {
                    endIndex = count;
                } else {
                    // Did we finish a segment?
                    if (endIndex-startIndex >= 2) {
                        /*
                        System.out.println("Found an edge with slope=" 
                                               + startSlope + " with "
                                               + (endIndex-startIndex+1) + " elements.");
                                               */
                        // Extremal points of segment
                        // This set of points only becomes a segment
                        // if all points are "greater than" the starting point
                        
                        conditionalAddEdge(targetPoints, startIndex, endIndex, p);
                        
                    }
                    // reset to new block
                    // starting new segment
                    startSlope = curSlope;
                    startIndex = count;
                    endIndex   = count;
                }
            }
            //System.out.println();
            //System.out.println("startIndex=" + startIndex + " endIndex=" + endIndex);
            // Was the end of a block at the end of the targetPoint list?
            if (endIndex-startIndex >= 2) {
                /*
                        System.out.println("Found an edge with slope=" 
                                               + startSlope + " with "
                                               + (endIndex-startIndex+1) + " elements.");
                  */      
                        conditionalAddEdge(targetPoints, startIndex, endIndex, p);
                        
                        

            }
            //System.out.println();
                    
        }
    }
    
    
    
    private void conditionalAddEdge(Point[] targetPoints, 
                               int startIndex, int endIndex,
                               Point p) {
        boolean allGreater = true;
        for (int testind = startIndex; testind < endIndex; testind++) {
            Point testq = targetPoints[testind];
            if (p.compareTo(testq) > 0) {
                allGreater = false;
                break;
            }
        }
        
        if (allGreater) {
            Point endPoint = targetPoints[endIndex];
            segments.add(new LineSegment(p, endPoint));
        }   
        
    }
    
    
    
    
    // the number of line segments
    public           int numberOfSegments()   { return segments.size(); }     
    
    // the line segments
    public LineSegment[] segments() { 
        LineSegment[] lsArray = new LineSegment[segments.size()];
        // return a new array that contains copies of the line segments
        return segments.toArray(lsArray);
    } 

    public static void main(String[] args) {

        final int MAX_THREADS = Runtime.getRuntime().availableProcessors();

        In in = new In(args[0]);
        int N = in.readInt();
        
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

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
        MultiCollinearPoints[] workers = new MultiCollinearPoints[MAX_THREADS];
        Thread[]               threads = new Thread[MAX_THREADS];

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

        // All together now, print output ...
        for ( int thread = 0; thread < MAX_THREADS; thread++ ) {
            System.out.println("Thread " + thread + " found the following segments:");
            for (LineSegment segment : workers[thread].segments()) {
                StdOut.println(segment);
            }
        }
    }          
}