/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {
    private SET<Point2D> pointSets;

    // construct an empty set of points
    public PointSET() {
        this.pointSets = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return pointSets.isEmpty();
    }

    // number of points in the set
    public int size() {
        return pointSets.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(
            Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        pointSets.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return pointSets.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D point : pointSets) {
            point.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(
            RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        Queue<Point2D> pointsInRect = new Queue<>();
        for (Point2D point : pointSets) {
            if (rect.contains(point)) {
                pointsInRect.enqueue(point);
            }
        }
        return pointsInRect;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(
            Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        double minDistance = 0;
        Point2D nearestNeighbour = null;
        for (Point2D point : pointSets) {
            if (point.equals(p)) {
                return point;
            }
            double distance = p.distanceSquaredTo(point);
            if (minDistance == 0 || distance < minDistance) {
                nearestNeighbour = point;
                minDistance = distance;
            }
        }
        return nearestNeighbour;
    }

    // unit testing of the methods (optional)
    public static void main(
            String[] args) {

    }
}
