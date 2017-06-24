/** 
Tester class for KdTree
*/

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;
import java.lang.Comparable;

public class KdTester {

    private Iterable<Point2D> makePoints( int N, int GridSize ) {
        LinkedList<Point2D> list = new LinkedList<>();

        for (int i = 0; i < N; i++) {
            int irnd = StdRandom.uniform( GridSize );
            int jrnd = StdRandom.uniform( GridSize );
            double x = (double)irnd/GridSize;
            double y = (double)jrnd/GridSize;
            list.add( new Point2D( x, y ) );
        }
        return list;
    }

    private <E extends Comparable<E> > ArrayList<E> convertAndSort( Iterable<E> kiter ) {
            ArrayList<E> klist = new ArrayList<E>();
            for ( E ckp : kiter ) klist.add( ckp );
            Collections.sort( klist );
            return klist;
    }

    private <E extends Comparable<E> > boolean compArrays( ArrayList<E> klist, ArrayList<E> plist ) {
        boolean diff = false;
        for (int i = 0; i < klist.size(); i++) {
            boolean test = klist.get(i).equals( plist.get(i) );
            if (!test) System.out.println("Detected difference for:" + 
                                           klist.get(i) + " " + plist.get(i) );
            diff = diff || (!test);
        }
        return diff;
    }

    private boolean testContains( Iterable<Point2D> list, PointSET ps, KdTree kd ) {

        System.out.println("Point     PointSET      KdTree");
        for ( Point2D curPoint : list ) {
            boolean psres = ps.contains( curPoint );
            boolean kdres = kd.contains( curPoint );
            if (psres != kdres) return false;
            //System.out.println( curPoint + "   " + psres + "   " + kdres);
        }
        return true;
    }

    private boolean testNearest( Iterable<Point2D> list, PointSET ps, KdTree kd, int M, int GridSize)  {
        boolean diff = false;
        Iterable<Point2D> testPoints = this.makePoints( M, GridSize );

        //Point2D tp = new Point2D( 0.65, 0.45 );
        //LinkedList<Point2D> testPoints = new LinkedList<>();
        //testPoints.add(tp);

        Stopwatch tps = new Stopwatch();
        ArrayList<Point2D> psArray = new ArrayList<>();
        for ( Point2D curTest : testPoints ) {
            Point2D nearestPS = ps.nearest( curTest );
            psArray.add( nearestPS );
            //System.out.println("PS Testing for " + curTest + " got nearest " + nearestPS);
        }
        double timeps = tps.elapsedTime();
        System.out.println("PS time for nearest(): " + timeps);

        Stopwatch tkd = new Stopwatch();
        ArrayList<Point2D> kdArray = new ArrayList<>();
        for ( Point2D curTest : testPoints ) {
            Point2D nearestKD = kd.nearest( curTest );
            kdArray.add( nearestKD );
            //System.out.println("KD Testing for " + curTest + " got nearest " + nearestKD);
        }
        double timekd = tkd.elapsedTime();
        System.out.println("KD time for nearest(): " + timekd);

        boolean mismatch = false;
        for (int i = 0; i < psArray.size(); i++) {
            //System.out.println("ps: " + psArray.get(i) + " kd:" + kdArray.get(i));
            if ( !psArray.get(i).equals( kdArray.get(i)) ) {
                System.out.println("Mismatch for point: " + psArray.get(i));
                mismatch = true;
            }
        }
        return mismatch;
    }

    public static void main(String[] args) {

        StdRandom.setSeed(1);

        KdTester tester = new KdTester();

        PointSET ps = new PointSET();
        KdTree   kd = new KdTree();

        System.out.println("Before adding points:");
        System.out.println("PS is empty:" + ps.isEmpty());
        System.out.println("KD is empty:" + kd.isEmpty());

        int N = 500000;
        int GridSize = 10000;
        System.out.println("Making " + N + " points ...");
        Iterable<Point2D> list = tester.makePoints( N, GridSize );



        Stopwatch timerps  = new Stopwatch();
        for ( Point2D curPoint : list ) {
            ps.insert( curPoint );
        }
        double tpsinsert = timerps.elapsedTime();
        System.out.println("Insert time for PS:" + tpsinsert);


        Stopwatch timerkd  = new Stopwatch();
        for ( Point2D curPoint : list ) {
            kd.insert( curPoint );
        }
        double tkdinsert = timerps.elapsedTime();
        System.out.println("Insert time for KD:" + tkdinsert);

        System.out.println("After adding " + N + " points:");
        System.out.println("PS is empty:" + ps.isEmpty());
        System.out.println("PS has " + ps.size() + " points.");
        System.out.println("KD is empty:" + kd.isEmpty());
        System.out.println("KD has " + kd.size() + " points.");

        //--- Test "contains" functionality
        Iterable<Point2D> testlist = tester.makePoints( 100, GridSize*2 );
        boolean contret = tester.testContains( testlist, ps, kd );
        System.out.println("Contains matches: " + contret );


        //--- Test range functionality
        System.out.println("Points in range test:");
        LinkedList<RectHV> rectList = new LinkedList<>();
        rectList.add( new RectHV( 0.2, 0.3, 0.8, 0.9 ) );
        rectList.add( new RectHV( 0.5, 0.3, 1.0, 0.9 ) );
        rectList.add( new RectHV( 0.8, 0.5, 1.0, 1.0 ) );
        rectList.add( new RectHV( 0.0, 0.0, 1.0, 1.0 ) );
        rectList.add( new RectHV( 0.45, 0.45, 0.55, 0.55 ) );
        rectList.add( new RectHV( 0.45, 0.45, 1.55, 1.55 ) );
        rectList.add( new RectHV( 1.45, 1.45, 1.55, 1.55 ) );

        for ( RectHV curRect : rectList ) {
            System.out.println("Testing rectangle " + curRect );

            Stopwatch timerPS       = new Stopwatch();
            Iterable<Point2D> piter = ps.range( curRect );
            double timePS           = timerPS.elapsedTime();
            System.out.println("PS time: " + timePS);


            Stopwatch timerKD       = new Stopwatch();
            Iterable<Point2D> kiter = kd.range( curRect );
            double timeKD           = timerKD.elapsedTime();
            System.out.println("KD time: " + timeKD);

            
            
            // Put results into sortable lists and sort
            // before comparison
            ArrayList<Point2D> klist = tester.convertAndSort( kiter );
            ArrayList<Point2D> plist = tester.convertAndSort( piter );
            
            boolean diff = tester.compArrays( klist, plist );
            if ( !diff ) System.out.println("No differences found.");
            else         System.out.println("Found differences.");


        }

        //---- Test nearest-point functionality
        int M = 100; // number of testpoints
        boolean res = tester.testNearest( list, ps, kd, M, GridSize*100);
        System.out.println("Nearest test pass: " + !res);

    }
}


