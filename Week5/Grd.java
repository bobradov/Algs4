
/** grid test of kdtree

*/

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;
import java.lang.Comparable;

public class Grd {

    public int InsertTest( KdTree kd, PointSET ps, int Ngrid, int nPoints ) {
        for ( int i = 0; i < nPoints; i++ ) {
            int irand = StdRandom.uniform( Ngrid );
            int jrand = StdRandom.uniform( Ngrid );
            double x = (double)irand/Ngrid;
            double y = (double)jrand/Ngrid;

            Point2D newPoint = new Point2D( x, y );
            //System.out.println(" >>> Inserting " + newPoint);
            //boolean contains = kd.contains( newPoint );
            //if (contains) System.out.println("Point " + newPoint + " already included.");
            kd.insert( newPoint );
            ps.insert( newPoint );
            //System.out.println("size=" + kd.size());
        }
        System.out.println("PS size: " + ps.size());
        return kd.size();
    }

    public static void main(String[] s) {

        StdRandom.setSeed(1);

        Grd grd = new Grd();
        KdTree kd = new KdTree();
        PointSET ps = new PointSET();

        int Ngrid = 1000;
        int NPoints = 10000;
        int npoints = grd.InsertTest( kd, ps, Ngrid, NPoints );
        System.out.println("After inserting " + NPoints + " into grid " +
                           " with " + Ngrid*Ngrid + " total slots, got " +
                           npoints + " inserted points.");


    }


}
