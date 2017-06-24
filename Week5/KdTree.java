import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Stack;




public class KdTree {

    private int N      = 0;
    private Node root  = null;
    private double minDist; // helper for min distance calculation
    private Node   minNode;

    // Base Node class
    // Defines point storage and a compareTo class
    private abstract class Node {
        protected int n;
        protected Point2D p;
        protected Node parent      = null;
        protected Node leftChild   = null;
        protected Node rightChild  = null;

        protected RectHV rect      = null;

        Node( Point2D p, int n ) {
            this.p      = p;
            this.n      = n;
        }

        protected void assignRect( RectHV rect ) { 
            this.rect = rect;
        }

        protected RectHV getRect() { return rect; }


        protected int       getNum()    { return n; }
        protected Point2D   getPoint()  { return p; }

        abstract  int       compareTo( Point2D p );
        abstract  Node      makeNode( Point2D p, int n );
        abstract  String    getOrient();
        abstract  RectHV    rectCompute( RectHV parentRect, String side );
        abstract  void      setPenColor();
        abstract  void      lineThroughPoint( Point2D point );
        abstract  int       compareTo( RectHV rectArg );

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append( "Node:" );
            sb.append( n );
            sb.append( " Type: ");
            sb.append( this.getOrient() );
            sb.append( " Point: ");
            sb.append( p.toString() );
            sb.append( " LeftChild: ");
            if ( leftChild != null )
                sb.append( this.leftChild.getNum() );
            else
                sb.append( " null ");
            sb.append( " rightChild: " );
            if ( rightChild !=null )
                sb.append( this.rightChild.getNum() );
            else
                sb.append( " null ");

            // Add rectangle
            sb.append(" Rect: ");
            sb.append( rect.toString() );
            return sb.toString();
        }
    }

    // Compare based on x-coordinate of point 
    private class XNode extends Node {

        private XNode( Point2D p , int n ) {
            super( p, n );
        }

        @Override
        int compareTo( Point2D p ) {
            /*
            System.out.println("Node " + this.n + " comparing x " 
                                       + this.p.x() 
                                       + " "
                                       + p.x());
                                       */
            if ( this.p.x() < p.x() ) return -1;
            if ( this.p.x() > p.x() ) return  1;
            return 0;
        }

        @Override
        int compareTo( RectHV rectArg ) {
            if ( rect.xmin() > this.p.x() ) return -1;  // Rect is entirely left of nodeline
            if ( rect.xmax() < this.p.x() ) return  1;  // Rect is entirely right of nodeline
            return 0; // Nodeline overlaps rectangle
        }

        @Override
        Node makeNode( Point2D p, int n ) {
            // XNode creates a YNode
            return new YNode( p, n );
        }

        @Override
        String getOrient() { return "x"; }

        @Override
        RectHV rectCompute( RectHV parentRect, String side ) {
            // This is the bounding box of an XNode
            // The parent is a YNode (except for root, handled separately)
            // Because parent is a YNode, the new partition is formed by setting
            // a ymin or ymax. 
            // For "left" nodes, i.e. smaller y, ymax is set
            // For "right" nodes, i.e. larger y, ymin is set
            // Initially set to parent, then override
            double xmin = parentRect.xmin();
            double xmax = parentRect.xmax();
            double ymin = parentRect.ymin();
            double ymax = parentRect.ymax();

            Point2D parentPoint = parent.getPoint();

            if ( side.equals( "left" ) ) ymax = parentPoint.y();
            else                         ymin = parentPoint.y();
            /*
            System.out.println( "About to Xrect with " + xmin + " "
                               + " " + ymin + " " + xmax + " " + ymax );
                               */

            return new RectHV( xmin, ymin, xmax, ymax );

        }

        @Override
        void setPenColor() { StdDraw.setPenColor( StdDraw.MAGENTA ); }

        @Override
        void lineThroughPoint( Point2D point ) {
            StdDraw.line( point.x(), getRect().ymin(), point.x(), getRect().ymax());
        }

    }

    // Compare based on y-coordinate of point
    private class YNode extends Node {

        private YNode( Point2D p, int n ) {
            super( p, n );
        }

        @Override
        int compareTo( Point2D p ) {
            /*
            System.out.println("Node " + this.n + " comparing y " 
                                       + this.p.y() 
                                       + " "
                                       + p.y());
                                       */
            if ( this.p.y() < p.y() ) return -1;
            if ( this.p.y() > p.y() ) return  1;
            return 0;
        }

        @Override
        int compareTo( RectHV rectArg ) {
            if ( rect.ymin() > this.p.y() ) return -1;  // Rect is entirely left of nodeline
            if ( rect.ymax() < this.p.y() ) return  1;  // Rect is entirely right of nodeline
            return 0; // Nodeline overlaps rectangle
        }

        @Override
        Node makeNode( Point2D p, int n ) {
            // YNode creates an XNode
            return new XNode( p, n );
        }

        @Override
        String getOrient() { return "y"; }

        @Override
        RectHV rectCompute( RectHV parentRect, String side ) {
            // This is the bounding box of an YNode
            // The parent is a XNode 
            // Because parent is a XNode, the new partition is formed by setting
            // a xmin or xmax. 
            // For "left" nodes, i.e. smaller x, xmax is set
            // For "right" nodes, i.e. larger x, xmin is set
            // Initially set to parent, then override
            double xmin = parentRect.xmin();
            double xmax = parentRect.xmax();
            double ymin = parentRect.ymin();
            double ymax = parentRect.ymax();

            Point2D parentPoint = parent.getPoint();

            if ( side.equals( "left" ) ) xmax = parentPoint.x();
            else                         xmin = parentPoint.x();

            /*
            System.out.println( "About to Yrect with " + xmin + " "
                               + " " + ymin + " " + xmax + " " + ymax );
                               */

            return new RectHV( xmin, ymin, xmax, ymax );

        }

        @Override
        void setPenColor() { StdDraw.setPenColor( StdDraw.BLUE ); }

        @Override
        void lineThroughPoint( Point2D point ) {
            StdDraw.line( getRect().xmin(), point.y(), getRect().xmax(), point.y() );
        }
    }

    // construct an empty set of points 
    public KdTree() {
        //
    }


    // is the set empty? 
    public  boolean isEmpty()   { return N == 0; }

    // number of points in the set
    public  int size()      { return N; }


    private Iterable<Node> depthFirst() {
        LinkedList<Node> nodeList = new LinkedList<>();
        depthFirstFunc( root, nodeList );
        return nodeList;
    }

    private void depthFirstFunc( Node node, LinkedList<Node> nodeList ) {
        if ( node != null ) 
            nodeList.add( node );
        else
            return;
        if ( node.leftChild   != null ) depthFirstFunc( node.leftChild, nodeList );
        if ( node.rightChild  != null ) depthFirstFunc( node.rightChild, nodeList );
        return;
    }

    private Iterable<Node> levelOrder() {
        ArrayList<Node> nodeArray = new ArrayList<Node>();
        int curIndex = 0;
        int added    = 0;
        nodeArray.add( root );
        ++added;
        Node curNode;
        
        while ( added <  N ) {
            //System.out.println("index: " + curIndex + " N=" + N);
            // node currently being pointed to
            curNode = nodeArray.get( curIndex );

            // add children of current node
            if ( curNode != null ) {
                if ( curNode.leftChild  != null ) {
                    nodeArray.add( curNode.leftChild );
                    ++added;
                }
                if ( curNode.rightChild != null ) {
                    nodeArray.add( curNode.rightChild );
                    ++added;
                }
                curIndex += 1;
            }
            
        }
        return nodeArray;
    }


    private void insert( Point2D p, Node anchorNode, int n ) {
        if ( anchorNode.compareTo( p ) > 0 ) {
            // "left" node
            // Does the left node exist? If not, that's where
            // the new node goes
            if ( anchorNode.leftChild == null ) {
                anchorNode.leftChild = anchorNode.makeNode( p, n );
                anchorNode.leftChild.parent = anchorNode;
                // Now compute rectangle for the child
                // Use the parent rectangle as a starting point
                // then let the new Node figure out how to 
                // Let the computation know that this is for the "left" node
                RectHV newRect = 
                        anchorNode.leftChild.rectCompute( anchorNode.getRect(), 
                                                            "left" );
                anchorNode.leftChild.assignRect( newRect );
                return;
            } else {
                insert( p, anchorNode.leftChild, n );
            }
        } else {
            // "right" node
            if ( anchorNode.rightChild == null ) {
                anchorNode.rightChild = anchorNode.makeNode( p, n );
                anchorNode.rightChild.parent = anchorNode;
                // Now compute rectangle for the child
                // Use the parent rectangle as a starting point
                // then let the new Node figure out how to 
                // Let the computation know that this is for the "left" node
                RectHV newRect = 
                    anchorNode.rightChild.rectCompute( anchorNode.getRect(), 
                                                            "right" );
                anchorNode.rightChild.assignRect( newRect );
                return;
            } else {
                insert( p, anchorNode.rightChild, n );
            }
        }
    }

    // add the point to the set (if it is not already in the set)                     
    public   void insert( Point2D p )  {
        if (p == null) throw new java.lang.NullPointerException();
        if ( isEmpty() ) {
            // Insert root node
            // Algorithm starts with a XNode
            // Add unit square as the rectangle for the root node
            ++N;
            root = new XNode( p, N );
            root.assignRect( new RectHV( 0.0, 0.0, 1.0, 1.0 ) );
        } else {
            //System.out.println("Comparing point " + p );
            if (! this.contains(p)) {
                //System.out.println("Point " + p + " is not included");
                ++N;
                insert( p, root, N );
                /*
                for( Node node : this.levelOrder()) {
                    Point2D pnt = node.getPoint();
                    System.out.print(pnt + " ");
                }
                System.out.println();
                */
            }    
        }
    }           

    
    // does the set contain point p?
    // Do a search through the KdTree  
    public  boolean contains( Point2D p )  {
        if (p == null) throw new java.lang.NullPointerException();
        Node curNode = root;
        return ( find( p, root ) != null );
    }

    private Node find( Point2D p, Node node ) {
        // Testing for floating-point equality
        // Never a good idea, may need to revisit
        // A test with tolerance would be better
        if ( node == null ) return null;
        //System.out.println("Comparing " + p + " to node.p" + node.p );
        if ( node.p.equals( p ) ) return  node;
    
        // Look left
        if ( node.compareTo( p ) > 0 ) {
            return find( p, node.leftChild );
        }
        if ( node.compareTo( p ) <= 0 ) {
            return find( p, node.rightChild );
        }   
        // The node we're looking for should have been here,
        // but isn't. Node not found; return null
        return null;
    }          

    
    // draw all points to standard draw 
    public  void draw()  {

        for ( Node curNode : this.depthFirst() ) {

            Point2D curPoint = curNode.getPoint();

            // Draw lines
            curNode.setPenColor();
            StdDraw.setPenRadius(0.005);
            curNode.lineThroughPoint( curPoint );

            // Draw point
            StdDraw.setPenRadius(0.025);
            StdDraw.setPenColor( StdDraw.BLACK) ;
            StdDraw.point( curPoint.x(), curPoint.y() );
            
        }
    }
    



    // all points that are inside the rectangle 
    public Iterable<Point2D> range( RectHV rect ) {
        if (rect == null) throw new java.lang.NullPointerException();

        Stack<Point2D> alist = new Stack<>();

        // recursively accumulate points in rectangle
        // starting with the root node, adding to alist
        findInRect( rect, alist, root );

        return alist;
    }            

    private void findInRect( RectHV rect, Stack<Point2D> alist, Node node) {
        if ( node == null ) return;

        
        boolean intersects = true;

        
        if ( node.leftChild != null )
            if ( rect.intersects( node.leftChild.getRect()) ) 
                findInRect( rect, alist, node.leftChild );
            else
                intersects = false;

        if ( node.rightChild != null )
            if ( rect.intersects( node.rightChild.getRect()) )
                findInRect( rect, alist, node.rightChild );
            else
                intersects = false;

        if (intersects)
            if ( rect.contains( node.getPoint() ) ) alist.push( node.getPoint() );
        

        return;
    }

    

    // a nearest neighbor in the set to point p; null if the set is empty 
    public  Point2D nearest( Point2D p ) {
        if (p == null) throw new java.lang.NullPointerException();
        if (root == null) return null; // data structure is empty, return null

        minDist  = java.lang.Double.MAX_VALUE;  // reset distance measurement
        findNearest( p, root );
        return minNode.getPoint();
    }

    private void findNearest( Point2D p, Node curNode ) {
        /*
        System.out.println("Entered node " 
                            + curNode.getPoint() + " dist=" 
                            + minDist );
        if ( ! prox.isEmpty() ) System.out.println("Min:" + prox.peek().getPoint() );
        */    

        Point2D curPoint = curNode.getPoint();
        double  curDist  = curPoint.distanceSquaredTo( p );

        if ( curDist < minDist ) {
            minDist = curDist;
            minNode = curNode;
            //prox.push( curNode );
            //System.out.println("Got minNode:" + curNode.getPoint() + " with dist=" + minDist );
        }

        // But also check to see if the children have something
        // with a smaller distance. 
        // Only check if the rectangles corresponding to the children
        // can possibly have somthing with a smaller distance

        // Which order do we search in?

        // If both kids exist ... then do the more likely one first
        // Do the other one only if a better distance is not produced
        // in the more likely one

        if ( curNode.leftChild != null && curNode.rightChild != null ) {
            double left   = curNode.leftChild.getRect().distanceSquaredTo( p );
            double right  = curNode.rightChild.getRect().distanceSquaredTo( p );

            // Choose search order based on distance
            // Smaller distance to rectangle is more likely to produce a closer point
            if ( left < right ) {
                findNearest( p, curNode.leftChild );
                if (right < minDist)
                    findNearest( p, curNode.rightChild );
            } else {
                findNearest( p, curNode.rightChild );
                if (left < minDist)
                    findNearest( p, curNode.leftChild );
            }

        } else {
            // One of the kids does not exist
            // No need to optimize search order
            // Just do the one that extsist

            if ( curNode.leftChild != null ) {
                double left   = curNode.leftChild.getRect().distanceSquaredTo( p );
                if ( left  <= minDist )
                    findNearest( p, curNode.leftChild );
            }

            if ( curNode.rightChild != null ) {
                double right  = curNode.rightChild.getRect().distanceSquaredTo( p );
                if ( right <= minDist )
                    findNearest( p, curNode.rightChild );
            }
        }

        return;
    }            
    
     // unit testing of the methods (optional) 
    public static void main(String[] args)  {

        KdTree kd = new KdTree();

        kd.insert( new Point2D( 0.7, 0.2 ));
        kd.insert( new Point2D( 0.5, 0.4 ));
        kd.insert( new Point2D( 0.2, 0.3 ));
        kd.insert( new Point2D( 0.4, 0.7 ));
        kd.insert( new Point2D( 0.9, 0.6 ));
        //kd.insert( new Point2D( 0.4, 0.7 ));

        System.out.println("Contains " + kd.size() + " points.");
        System.out.println("Attempting to insert repeated point:");
        kd.insert( new Point2D( 0.5, 0.4+1e-100 ));
        System.out.println("Contains " + kd.size() + " points.");


        System.out.println("In level order:");

        for ( Node curNode : kd.levelOrder() ) {
            System.out.println( curNode );
        }

        System.out.println("In depth-first order:");

        for ( Node curNode : kd.depthFirst() ) {
            System.out.println( curNode );
        }

        // Now try to find some points
        // First test all points that are actually in the KdTree
        System.out.println("The following points should actually be found:");
        for ( Node curNode : kd.levelOrder() ) {
            Point2D testPoint = new Point2D( curNode.getPoint().x(), 
                                             curNode.getPoint().y() );
            System.out.println("Node: " + curNode.getPoint() + " contained: " 
                                        + kd.contains( testPoint ) );
        }

        System.out.println("The following should not be found:");
        System.out.println("No: Contains (0.41, 0.7):" + kd.contains( new Point2D( 0.41, 0.7 )));
        System.out.println("No: Contains (0.4, 0.71):" + kd.contains( new Point2D( 0.4, 0.71 )));
        System.out.println("No: Contains (0.39, 0.7):" + kd.contains( new Point2D( 0.39, 0.7 )));
        System.out.println("No: Contains (0.4, 0.69):" + kd.contains( new Point2D( 0.4, 0.69 )));

        // Test range function
        // Rect2D: xmin, ymin, xmax, ymax
        System.out.println("Points in range test:");
        LinkedList<RectHV> rectList = new LinkedList<>();
        rectList.add( new RectHV( 0.2, 0.3, 0.8, 0.9) );
        rectList.add( new RectHV( 0.5, 0.3, 1.0, 0.9) );
        rectList.add( new RectHV( 0.8, 0.5, 1.0, 1.0) );

        for ( RectHV curRect : rectList ) {
            System.out.println("Testing rectangle " + curRect );

            Iterable<Point2D> list = kd.range( curRect );
            for ( Point2D curPoint : list ) {
                System.out.print( curPoint + " ");
            }
            System.out.println();
        }

        // Test neaerst-point function
        System.out.println("Testing nearest point functionality:");
        LinkedList<Point2D> prox = new LinkedList<>();
        prox.add( new Point2D(0.1,   0.2 ) );
        prox.add( new Point2D(0.65,  0.15) );
        prox.add( new Point2D(0.85,  0.57) );
        for ( Point2D curPoint : prox ) {
            Point2D nearest = kd.nearest( curPoint );
            System.out.println(">>> Nearest point to " + curPoint + " is: " + nearest);
        }
    }               
}