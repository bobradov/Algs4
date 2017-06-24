// Tester class for FasrCollinearPoints


import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class FastTest {
	
	public static void main(String[] args) {
        In in = new In(args[0]);
        int N = in.readInt();
        
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.show(0);
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        StdDraw.setPenRadius(0.025);
        StdDraw.setPenColor( StdDraw.RED) ;
        MultiFast fcol = new MultiFast(points);
        for (LineSegment segment : fcol.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        System.out.println("Found " + fcol.segments().length + " collinear segments.");
	}
}