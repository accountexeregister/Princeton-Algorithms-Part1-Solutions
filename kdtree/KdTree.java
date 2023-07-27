/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private Node root;
    private int size;

    private class Node {
        private Point2D point;
        private RectHV rect;
        private Node left;
        private Node right;

        private Node(Point2D point) {
            this.point = point;
        }
    }

    public KdTree() {
        size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(
            Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        root = insert(p, root, 0, 1, 0, 1, 0);
    }

    private Node insert(Point2D p, Node x, double xMin, double xMax, double yMin, double yMax,
                        int level) {
        if (x == null) {
            Node t = new Node(p);
            if (level % 2 == 0) {
                t.rect = new RectHV(p.x(), yMin, p.x(), yMax);
            }
            else {
                t.rect = new RectHV(xMin, p.y(), xMax, p.y());
            }
            x = t;
            size++;
            return x;
        }
        if (x.point.equals(p)) {
            return x;
        }
        if (level % 2 == 0) {
            if (p.x() < x.point.x()) {
                x.left = insert(p, x.left, xMin, x.point.x(), yMin, yMax, level + 1);
            }
            else {
                x.right = insert(p, x.right, x.point.x(), xMax, yMin, yMax, level + 1);
            }
        }
        else {
            if (p.y() < x.point.y()) {
                x.left = insert(p, x.left, xMin, xMax, yMin, x.point.y(), level + 1);
            }
            else {
                x.right = insert(p, x.right, xMin, xMax, x.point.y(), yMax, level + 1);
            }
        }
        return x;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return search(p, root, 0) != null;
    }

    private Node search(Point2D p, Node x, int level) {
        if (x == null) {
            return null;
        }
        if (x.point.equals(p)) {
            return x;
        }
        int pCompareToNodeCoordinate;
        if (level % 2 == 0) {
            pCompareToNodeCoordinate = Double.compare(p.x(), x.point.x());
        }
        else {
            pCompareToNodeCoordinate = Double.compare(p.y(), x.point.y());
        }

        if (pCompareToNodeCoordinate < 0) {
            x = search(p, x.left, level + 1);
        }
        else {
            x = search(p, x.right, level + 1);
        }
        return x;
    }

    // draw all points to standard draw
    public void draw() {
        draw(root, 0);
    }

    private void draw(Node x, int level) {
        if (x == null) {
            return;
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        x.point.draw();
        if (level % 2 == 0) {
            StdDraw.setPenColor(StdDraw.RED);
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
        }
        StdDraw.setPenRadius();
        x.rect.draw();
        draw(x.left, level + 1);
        draw(x.right, level + 1);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(
            RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        Queue<Point2D> pointsInRange = new Queue<>();
        addPointsInRange(rect, root, pointsInRange, 0);
        return pointsInRange;
    }

    private void addPointsInRange(RectHV rect, Node x, Queue<Point2D> pointsIterable, int level) {
        if (x == null) {
            return;
        }
        if (rect.contains(x.point)) {
            pointsIterable.enqueue(x.point);
        }
        if (x.rect.intersects(rect)) {
            addPointsInRange(rect, x.left, pointsIterable, level + 1);
            addPointsInRange(rect, x.right, pointsIterable, level + 1);
        }
        else {
            int rectCompareNodeRectCoordinate;
            if (level % 2 == 0) {
                rectCompareNodeRectCoordinate = Double.compare(rect.xmax(), x.rect.xmin());
            }
            else {
                rectCompareNodeRectCoordinate = Double.compare(rect.ymax(), x.rect.ymin());
            }

            if (rectCompareNodeRectCoordinate < 0) {
                addPointsInRange(rect, x.left, pointsIterable, level + 1);
            }
            else {
                addPointsInRange(rect, x.right, pointsIterable, level + 1);
            }

        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(
            Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (root == null) {
            return null;
        }
        return nearest(p, root, root, 0).point;
    }

    private Node nearest(Point2D p, Node x, Node champion, int level) {
        if (x == null) {
            return champion;
        }
        if (p.equals(x.point)) {
            return x;
        }
        double distance = p.distanceSquaredTo(x.point);
        if (distance < champion.point.distanceSquaredTo(p)) {
            champion = x;
        }
        int pCompareNodePointCoord;
        if (level % 2 == 0) {
            pCompareNodePointCoord = Double.compare(p.x(), x.point.x());
        }
        else {
            pCompareNodePointCoord = Double.compare(p.y(), x.point.y());
        }

        Node firstXToSearch;
        Node secondXToSearch;
        if (pCompareNodePointCoord < 0) {
            firstXToSearch = x.left;
            secondXToSearch = x.right;
        }
        else {
            firstXToSearch = x.right;
            secondXToSearch = x.left;
        }

        champion = nearest(p, firstXToSearch, champion, level + 1);
        if (!(champion.point.distanceSquaredTo(p) < x.rect.distanceSquaredTo(p))) {
            champion = nearest(p, secondXToSearch, champion, level + 1);
        }
        return champion;
    }

    public static void main(String[] args) {

    }
}
