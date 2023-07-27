/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    // Create array of double named openSitesFractions
    private static final double CONFIDENCE_95 = 1.96;
    private double[] openSitesFractions;
    private int n;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        this.n = n;
        // Initialise openSitesFractions with size = trials
        openSitesFractions = new double[trials];
        Percolation percolation;
        // Iterate from 0 to trials
        for (int i = 0; i < trials; i++) {
            // Initialise Percolation(n) class as percolation
            percolation = new Percolation(n);
            // While percolates method of percolation returns false
            while (!percolation.percolates()) {
                int row;
                int column;
                do {
                    // Gets a random row and column
                    row = StdRandom.uniformInt(n) + 1;
                    column = StdRandom.uniformInt(n) + 1;

                } while (percolation.isOpen(row, column));
                // Call percolation.open(row, column)
                percolation.open(row, column);
            }
            // Get fraction of open sites
            double openSitesFraction = ((double) percolation.numberOfOpenSites()) / (n * n);
            // Add openSitesFractions to openSitesFractions array
            openSitesFractions[i] = openSitesFraction;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(openSitesFractions);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if (openSitesFractions.length == 1) {
            return Double.NaN;
        }
        return StdStats.stddev(openSitesFractions);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (CONFIDENCE_95 * stddev() / Math.sqrt(openSitesFractions.length));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (CONFIDENCE_95 * stddev() / Math.sqrt(openSitesFractions.length));
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats pStats = new PercolationStats(n, t);
        StdOut.println("mean = " + pStats.mean());
        StdOut.println("stddev = " + pStats.stddev());
        StdOut.println("95% confidence interval = " + "[" + pStats.confidenceLo() + ", "
                               + pStats.confidenceHi() + "]");
    }

}
