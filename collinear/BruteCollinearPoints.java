/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BruteCollinearPoints {
    private int numOfSegmentsWithDup;
    private LineSegment[] segments;
    private LineSegmentEndPoints[] segmentEndPoints;
    private int actualNumOfSegments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new java.lang.IllegalArgumentException();
        }
        numOfSegmentsWithDup = 0;
        actualNumOfSegments = 0;
        int n = points.length;
        Point[] pointsClone = clone(points, n);
        // Sorts the points by x and y, hence, points with same x and y will be adjacent to each other
        Arrays.sort(pointsClone);
        if (hasRepeatingPointOrNull(pointsClone)) {
            throw new java.lang.IllegalArgumentException();
        }
        // Iterate through every point in points from i to points.length - 1
        //  For every i, use j to iterate through all points from j to points.length - 1, skipping i
        // For every j, use k to iterate through all points from k to points.length - 1, skipping i and j
        // For every t, use t to iterate through all points from t to points.length - 1, skipping i, j, k
        // If slope of point i to point j, k, t are equal
        // If line does not exist in segments, insert line segment to segments
        // Increment numOfSegments
        // Determine segmentsSize using formula for maximal quadruple
        // Formula: (n / 4) + 1
        int segmentsSize = 2 * n * n;
        segments = new LineSegment[segmentsSize];
        segmentEndPoints = new LineSegmentEndPoints[segmentsSize];
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                for (int k = j + 1; k < n; k++) {
                    for (int t = k + 1; t < n; t++) {
                        double slopeJ = pointsClone[i].slopeTo(pointsClone[j]);
                        double slopeK = pointsClone[i].slopeTo(pointsClone[k]);
                        double slopeT = pointsClone[i].slopeTo(pointsClone[t]);

                        if (slopeJ == slopeK && slopeJ == slopeT) {
                            Point[] collinearPoints = new Point[4];
                            collinearPoints[0] = pointsClone[i];
                            collinearPoints[1] = pointsClone[j];
                            collinearPoints[2] = pointsClone[k];
                            collinearPoints[3] = pointsClone[t];
                            segmentEndPoints[numOfSegmentsWithDup++] = new LineSegmentEndPoints(
                                    collinearPoints[0],
                                    collinearPoints[collinearPoints.length - 1]);

                        }

                    }
                }
            }
        }
        fillSegments();
        segmentEndPoints = null;

    }

    private Point[] clone(Point[] original, int limit) {
        Point[] clone = new Point[limit];
        for (int i = 0; i < limit; i++) {
            if (original[i] == null) {
                throw new IllegalArgumentException();
            }
            clone[i] = original[i];
        }
        return clone;
    }

    private LineSegment[] clone(LineSegment[] original, int limit) {
        LineSegment[] clone = new LineSegment[limit];
        for (int i = 0; i < limit; i++) {
            clone[i] = original[i];
        }
        return clone;
    }


    private class LineSegmentEndPoints implements Comparable<LineSegmentEndPoints> {
        private Point endPoint1;
        private Point endPoint2;

        private LineSegmentEndPoints(Point endPoint1, Point endPoint2) {
            this.endPoint1 = endPoint1;
            this.endPoint2 = endPoint2;
        }

        public int compareTo(LineSegmentEndPoints that) {
            int compareEndPoint1 = endPoint1.compareTo(that.endPoint1);
            if (compareEndPoint1 == 0) {
                return endPoint2.compareTo(that.endPoint2);
            }
            return compareEndPoint1;
        }
    }

    private void fillSegments() {
        if (numOfSegmentsWithDup == 0) {
            return;
        }
        Arrays.sort(segmentEndPoints, 0, numOfSegmentsWithDup);
        segments[actualNumOfSegments++] = new LineSegment(segmentEndPoints[0].endPoint1,
                                                          segmentEndPoints[0].endPoint2);
        int i = 1;
        while (i < numOfSegmentsWithDup) {
            while (i < numOfSegmentsWithDup
                    && segmentEndPoints[i].compareTo(segmentEndPoints[i - 1]) == 0
            ) {
                i++;
            }
            if (i >= numOfSegmentsWithDup) {
                break;
            }
            segments[actualNumOfSegments++] = new LineSegment(segmentEndPoints[i].endPoint1,
                                                              segmentEndPoints[i].endPoint2);
            i++;
        }
    }

    private boolean hasRepeatingPointOrNull(Point[] points) {
        if (points.length == 1) {
            return points[0] == null;
        }
        // Iterates through points array, comparing adjacent points to see if they have same x and y and if they are the same, return true
        for (int i = 0, pointLength = points.length; i < pointLength - 1; i++) {
            if (points[i].compareTo(points[i + 1]) == 0) {
                return true;
            }
        }
        return false;
    }

    // the number of line segments
    public int numberOfSegments() {
        return actualNumOfSegments;
    }

    // the line segments
    public LineSegment[] segments() {
        if (segments.length > actualNumOfSegments) {
            segments = clone(segments, actualNumOfSegments);
        }
        return clone(segments, actualNumOfSegments);
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
