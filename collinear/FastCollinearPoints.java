/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints {
    private LineSegment[] segments;
    private LineSegmentEndPoints[] segmentEndPoints;
    private int numOfSegmentsWithDup;
    private int actualNumOfSegments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }

        Point[] pointsCopy = copyPoints(points);
        Arrays.sort(pointsCopy);
        if (hasRepeatingPointOrNull(pointsCopy)) {
            throw new IllegalArgumentException();
        }
        numOfSegmentsWithDup = 0;
        actualNumOfSegments = 0;
        int n = points.length;
        int segmentsSize = 2 * n * n;
        segments = new LineSegment[segmentsSize];
        segmentEndPoints = new LineSegmentEndPoints[segmentsSize];
        for (int i = 0; i < n; i++) {
            Arrays.sort(pointsCopy, points[i].slopeOrder());
            connect(pointsCopy);
        }
        fillSegments();
        segmentEndPoints = null;

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

    private void connect(Point[] points) {
        int n = points.length;
        if (n < 4) {
            return;
        }
        int i = 1;
        int currentIndex;
        int endIndex;
        int range;
        int x = 1;
        while (i + x <= n) {
            if (i + x != n && points[0].slopeTo(points[i]) == points[0].slopeTo(
                    points[i + x])) {
                x++;
                continue;
            }
            currentIndex = i;
            endIndex = Math.min(n - 1, currentIndex + (x - 1));

            range = endIndex - currentIndex;
            if (range >= 2) {
                Point firstPoint = points[0];
                swap(points, currentIndex - 1);
                Arrays.sort(points, currentIndex - 1, endIndex + 1);
                Point pointP = points[currentIndex - 1];
                Point pointQ = points[endIndex];
                segmentEndPoints[numOfSegmentsWithDup++] = new LineSegmentEndPoints(pointP, pointQ);

                for (int t = currentIndex - 1; t < endIndex + 1; t++) {
                    if (points[t] == firstPoint) {
                        swap(points, t);
                        break;
                    }
                }
            }
            i += x;
            x = 1;
        }
    }


    private Point[] copyPoints(Point[] points) {
        Point[] pointsCopy = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
            pointsCopy[i] = points[i];
        }
        return pointsCopy;
    }

    private void swap(Point[] points, int currentIndex) {
        Point pointX = points[currentIndex];
        points[currentIndex] = points[0];
        points[0] = pointX;
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

    private LineSegment[] clone(LineSegment[] original, int limit) {
        LineSegment[] clone = new LineSegment[limit];
        for (int i = 0; i < limit; i++) {
            clone[i] = original[i];
        }
        return clone;
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
