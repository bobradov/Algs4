/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *  
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;

public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param  x the <em>x</em>-coordinate of the point
     * @param  y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        //StdDraw.point(x, y);
        StdDraw.circle(x, y, 150);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertcal;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param  that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {

        if (that == null) {
            throw new java.lang.NullPointerException();
    	}

        int numerator   = this.y-that.y;
        int denominator = this.x-that.x;
        
        //System.out.println("that.y=" + that.y + " this.y=" + this.y );
        //System.out.println("num=" + numerator + " den=" + denominator);
        
        if (denominator != 0) {
            double slope =  numerator / ((double) denominator);
            if (slope == -0.0) slope = 0.0;
            return slope;
        } else if (numerator != 0) {
            return Double.POSITIVE_INFINITY;
        } else {
            return Double.NEGATIVE_INFINITY;
        }
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param  that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     *         point (x0 = x1 and y0 = y1);
     *         a negative integer if this point is less than the argument
     *         point; and a positive integer if this point is greater than the
     *         argument point
     */
    public int compareTo(Point that) {

    	if (that == null) {
    		throw new java.lang.NullPointerException();
    	}

        if (this.y < that.y) {
            return -1;
        } else if (this.y == that.y) {
            if (this.x < that.x) {
                return -1;
            } else if (this.x == that.x) {
                return 0;
            } else {
                return 1;
            }
        } else { 
            return 1;
        }
    }

    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        return new SlopeComparator();
    }
    
    private class SlopeComparator implements Comparator<Point> {
        
        public int compare(Point p, Point q) {
            double sp = slopeTo(p);
            double sq = slopeTo(q);
            
            if (sp < sq) {
                return -1;
            } else if (sp > sq) {
                return 1;
            } else {
            	return 0;
            }
                
        }
        /*
        public int compare(Point p, Point q) {
            double sp = slopeTo(p);
            double sq = slopeTo(q);
            
            if (sp < sq) {
                return -1;
            } else if (sp == sq) {
                // compare points based on their
                // order with respect to the calling point
                // In addition to slope ordering, there is
                // a coordinate-based ordering as well
                double dpy = p.y - Point.this.y;
                double dqy = q.y - Point.this.y;
                if (dpy < dqy) {
                    return -1;
                } else if (dpy > dqy) {
                    return 1;
                } else {
                    double dpx = p.x - Point.this.x;
                    double dqx = q.x - Point.this.x;
                    if (dpx < dqx) {
                        return -1;
                    } else if (dpx > dqx) {
                        return 1;
                    } else { 
                        return 0;
                    }
                }  
            } else {
                return  1;
            } 
        }
        */
    }


    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        // Test some points
        Point p = new Point(174, 257);
        Point q = new Point(161, 257);

        double slope = p.slopeTo(q);
        System.out.println("Slope from " + p + " to " + q + " = " + slope);

        Point p2 = new Point(301, 191);
        Point q2 = new Point(449, 191);

        double slope2 = p2.slopeTo(q2);
        System.out.println("Slope from " + p2 + " to " + q2 + " = " + slope2);
    }
}