
import java.util.ArrayList;
import java.util.Arrays;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;


public class BruteCollinearPoints {
   
    private ArrayList<LineSegment> segments;
    private Point[] points;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] pointsArg)  {
        int nPoints = pointsArg.length;
        points = new Point[nPoints];
        for (int i = 0; i < nPoints; i++) {
            points[i] = pointsArg[i];
        }

        // Make sure there are no duplicates, or bad points
        Arrays.sort(points);
        for (int i = 0; i < nPoints-1; i++) {
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


        segments = new ArrayList<LineSegment>();
    
        // Store quartet of points being examined
        // Also store slopes relative to first point
        Point[] potPoints  = new Point[4];
        double[] slopes    = new double[3];
        
        // Loop over all points as starting points
        // Assume there are at least four points
        // Otherwise definitely no segment
        int totPoints = 0;
        
        for (int i = 0; i < nPoints; i++) {
            potPoints[0] = points[i];
            
            for (int j = i+1; j < nPoints; j++) {
                potPoints[1] = points[j];
                slopes[0]    = potPoints[1].slopeTo(potPoints[0]);
                
                for (int k = j+1; k < nPoints; k++) {
                   potPoints[2] = points[k];
                   slopes[1]    = potPoints[2].slopeTo(potPoints[0]);
                   
                    for (int l = k+1; l < nPoints; l++) {
                        potPoints[3] = points[l];
                        slopes[2]    = potPoints[3].slopeTo(potPoints[0]);
                        ++totPoints;
                       // Do the four points described by potPoints form
                       // a line segment?
                       
                        if ((slopes[0] == slopes[1]) &&
                            (slopes[1] == slopes[2])) {
                            // Line segment formed by these four points
                            // Now find the extremal points
                            /*
                            System.out.println("Candidate found for " +
                                               potPoints[0] + " " +
                                               potPoints[1] + " with slope " + slopes[0] + " " +
                                               potPoints[2] + " with slope " + slopes[1] + " " +
                                               potPoints[3] + " with slope " + slopes[2]);
                            */
                            
                            // Find extremal points in edge
                            Point startPoint = minPoint(potPoints);
                            Point endPoint   = maxPoint(potPoints);
                            //System.out.println(" Start: " + startPoint + " End: " + endPoint );
                            
                            // Create and add a LineSegment 
                            segments.add(new LineSegment(startPoint, endPoint));
                        }
                    }
                }
            }
        }
        //assert totPoints == nPoints*(nPoints-1)*(nPoints-2)*(nPoints-3)/24;
        //System.out.println("Total points: " + totPoints );
        //System.out.println("Expected: " + nPoints*(nPoints-1)*(nPoints-2)*(nPoints-3)/24 );
        
          
    }

    private Point maxPoint(Point[] pointsAry) {
        Point maxPoint = pointsAry[0];
        int   maxIndex = 0;
        for (int i = 1; i < pointsAry.length; i++) {
            if (pointsAry[i].compareTo(maxPoint) > 0) {
                maxIndex = i;
                maxPoint = pointsAry[maxIndex];
            }
        }
        return pointsAry[maxIndex];
    }
    
    private Point minPoint(Point[] pointsAry) {
        Point minPoint = pointsAry[0];
        int   minIndex = 0;
        for (int i = 1; i < pointsAry.length; i++) {
            if (pointsAry[i].compareTo(minPoint) < 0) {
                minIndex = i;
                minPoint = pointsAry[minIndex];
            }
        }
        return pointsAry[minIndex];
    }
            
        
    
    
        
    // the number of line segments   
    public           int numberOfSegments()  { return segments.size(); }
    
    // the line segments
    public LineSegment[] segments() { 
        LineSegment[] lsArray = new LineSegment[segments.size()];
        // return a new array that contains copies of the line segments
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

        BruteCollinearPoints fcol = new BruteCollinearPoints(points);
        for (LineSegment segment : fcol.segments()) {
            StdOut.println(segment);
        }
    }            
}