/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int n;
    private int openSites;
    private WeightedQuickUnionUF quickUnionUF;
    private WeightedQuickUnionUF ufOnlyTopVirtualSite;
    private boolean[] isOpenSite;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        this.n = n;
        // Create WeightedQuickUnionUF object with size (n * n) + 2, where + 2 is for virtual top and bottom sites
        // Create bool array with same size as WeightedQuickUnionUF, where true means site is open and false means site is not open
        quickUnionUF = new WeightedQuickUnionUF((n * n) + 2);
        isOpenSite = new boolean[(n * n) + 2];
        // Set openSites to 0
        openSites = 0;
        // Create WeightedQuickUnionUF object with size (n * n + 1), with only top virtual sites
        ufOnlyTopVirtualSite = new WeightedQuickUnionUF((n * n) + 1);

    }

    private boolean crossPrescribedRange(int row, int col) {
        return (row < 1 || col < 1 || row > n || col > n);
    }

    private int indexFromRowAndCol(int row, int col) {
        return (((row - 1) * n) + (col - 1)) + 1;
    }

    private void unionStandard(int index1, int index2) {
        // Union index1 and index2 for both quickUnionUF and ufNoVirtualSite
        quickUnionUF.union(index1, index2);
        ufOnlyTopVirtualSite.union(index1, index2);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (crossPrescribedRange(row, col)) {
            throw new IllegalArgumentException();
        }

        // Return if site is already open
        if (isOpen(row, col)) {
            return;
        }

        // Get array index from row and col
        int currentSiteIndex = indexFromRowAndCol(row, col);
        // Set isOpenSite[currentSiteIndex] to true to indicate site is open
        isOpenSite[currentSiteIndex] = true;
        // Increment openSites
        openSites++;
        // If first or last row, connect site to virtual top or virtual bottom site
        if (row == 1) {
            unionStandard(currentSiteIndex, 0);
        }
        if (row == n) {
            quickUnionUF.union(currentSiteIndex, (n * n) + 1);
        }

        // Check if sites adjacent (up, down, left, right) to the current site is open
        // If open, union this site and the adjacent site
        if (row > 1) {
            if (isOpen(row - 1, col)) {
                unionStandard(currentSiteIndex, indexFromRowAndCol(row - 1, col));
            }
        }
        if (row < n) {
            if (isOpen(row + 1, col)) {
                unionStandard(currentSiteIndex, indexFromRowAndCol(row + 1, col));
            }
        }
        if (col > 1) {
            if (isOpen(row, col - 1)) {
                unionStandard(currentSiteIndex, indexFromRowAndCol(row, col - 1));
            }
        }
        if (col < n) {
            if (isOpen(row, col + 1)) {
                unionStandard(currentSiteIndex, indexFromRowAndCol(row, col + 1));
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (crossPrescribedRange(row, col)) {
            throw new IllegalArgumentException();
        }
        // Convert row and col to index of isOpenSite array
        // Return true if isOpenSite at the index converted is true, else return false
        int currentIndex = indexFromRowAndCol(row, col);
        return isOpenSite[currentIndex];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (crossPrescribedRange(row, col)) {
            throw new IllegalArgumentException();
        }
        // Return true if root of index of quickUnionUF at (row, col) is same as root of virtual top site (index = 0 of quickUnionUF)
        return ufOnlyTopVirtualSite.find(indexFromRowAndCol(row, col)) == ufOnlyTopVirtualSite.find(
                0);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return quickUnionUF.find((n * n) + 1) == quickUnionUF.find(0);
    }

    // test client (optional)
    public static void main(String[] args) {

    }
}
