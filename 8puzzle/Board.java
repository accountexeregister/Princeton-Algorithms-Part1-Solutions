/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;

public class Board {
    private final int[][] tiles;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        int n = tiles.length;
        this.tiles = new int[n][n];
        for (int i = 0, k = 1; i < n; i++) {
            for (int j = 0; j < n; j++, k++) {
                this.tiles[i][j] = tiles[i][j];
            }
        }
    }

    private void swap(int[][] arr, int x, int y) {
        if (x == y) {
            return;
        }
        int[] originalX = arr[x];
        arr[x] = arr[y];
        arr[y] = originalX;
    }

    // string representation of this board
    public String toString() {
        int n = dimension();
        StringBuilder s = new StringBuilder();
        s.append(n);
        for (int i = 0; i < n; i++) {
            s.append("\n");
            for (int j = 0; j < n; j++) {
                s.append(" ");
                s.append(tiles[i][j]);
            }
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return tiles.length;
    }

    // number of tiles out of place
    public int hamming() {
        int n = dimension();
        int hammingDistance = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    continue;
                }
                // Increment hammingDistance if number at tiles[i][j] is out of place
                if (tiles[i][j] != orderedNumber(i, j)) {
                    hammingDistance++;
                }
            }
        }
        return hammingDistance;
    }

    // Given the number arranged in tile in proper in the input row and column
    private int orderedNumber(int row, int column) {
        int n = dimension();
        if (row == n - 1 && column == n - 1) {
            return 0;
        }
        return (row * n) + column + 1;
    }


    private int distanceY(int orderedNum, int row, int[][] orderedNumToRowAndColumnsArr) {
        return Math.abs(row - orderedNumToRowAndColumnsArr[orderedNum][0]);
    }

    private int distanceX(int orderedNum, int column, int[][] orderedNumToRowAndColumnsArr) {
        return Math.abs(column - orderedNumToRowAndColumnsArr[orderedNum][1]);
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int n = dimension();
        // Creates orderedNumToRowAndColumnsArr where i in orderedNumToRowAndColumnsArr[i] represents a number (0 to n^2 - 1)
        // orderedNumToRowAndColumnsArr[i][0] represents the row of orderedNumToRowAndColumnsArr[i] arranged properly in a tile
        // orderedNumToRowAndColumnsArr[i][1] represents the column of orderedNumToRowAndColumnsArr[i] arranged properly in a tile
        int[][] orderedNumToRowAndColumnsArr = new int[n * n][2];
        for (int i = 0, k = 1; i < n; i++) {
            for (int j = 0; j < n; j++, k++) {
                int orderedNum = tiles[i][j];
                int orderedNumRow = (orderedNum - 1) / n;
                int orderedNumColumn = orderedNum - (orderedNumRow * n) - 1;
                // 0 indicates row of orderedNum
                orderedNumToRowAndColumnsArr[orderedNum][0] = orderedNumRow;
                // 1 indicates column of orderedNum
                orderedNumToRowAndColumnsArr[orderedNum][1] = orderedNumColumn;
            }
        }
        int manhattanSum = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    continue;
                }
                int distanceX = distanceX(tiles[i][j], j, orderedNumToRowAndColumnsArr);
                int distanceY = distanceY(tiles[i][j], i, orderedNumToRowAndColumnsArr);
                manhattanSum += distanceX + distanceY;
            }
        }
        return manhattanSum;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) return false;
        if (this.getClass() != y.getClass()) {
            return false;
        }
        Board other = (Board) y;
        if (this.dimension() != other.dimension()) {
            return false;
        }
        return Arrays.deepEquals(tiles, other.tiles);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int n = dimension();
        int blankRow = 0;
        int blankColumn = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    blankRow = i;
                    blankColumn = j;
                }
            }
        }
        Queue<Board> queue = new Queue<>();
        if (blankColumn > 0) {
            queue.enqueue(
                    createNeighbour(blankRow, blankColumn, blankRow, blankColumn - 1));
        }
        if (blankRow < n - 1) {
            queue.enqueue(
                    createNeighbour(blankRow, blankColumn, blankRow + 1, blankColumn));
        }
        if (blankColumn < n - 1) {
            queue.enqueue(
                    createNeighbour(blankRow, blankColumn, blankRow, blankColumn + 1));
        }
        if (blankRow > 0) {
            queue.enqueue(
                    createNeighbour(blankRow, blankColumn, blankRow - 1, blankColumn));
        }
        return queue;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int n = dimension();
        int[][] numToRowAndColumnsArr = new int[n * n][2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int orderedNum = tiles[i][j];
                numToRowAndColumnsArr[orderedNum][0] = i;
                numToRowAndColumnsArr[orderedNum][1] = j;
            }
        }
        int numToRowAndColumnsN = numToRowAndColumnsArr.length;
        int numXRowAndColumnIndex = 1;
        int row1ForTwin = numToRowAndColumnsArr[numXRowAndColumnIndex][0];
        int col1ForTwin = numToRowAndColumnsArr[numXRowAndColumnIndex][1];
        swap(numToRowAndColumnsArr, numXRowAndColumnIndex,
             numToRowAndColumnsN - 1);
        numToRowAndColumnsArr[numToRowAndColumnsN-- - 1] = null;
        int numYRowAndColumnIndex = numToRowAndColumnsN - 1;
        int row2ForTwin = numToRowAndColumnsArr[numYRowAndColumnIndex][0];
        int col2ForTwin = numToRowAndColumnsArr[numYRowAndColumnIndex][1];
        return createNeighbour(row1ForTwin, col1ForTwin, row2ForTwin, col2ForTwin);
    }

    private Board createNeighbour(int originalRow, int originalColumn, int rowToSwap,
                                  int columnToSwap) {
        int[][] boardTiles = cloneIntArr(this.tiles);
        int x = this.tiles[originalRow][originalColumn];
        boardTiles[originalRow][originalColumn] = this.tiles[rowToSwap][columnToSwap];
        boardTiles[rowToSwap][columnToSwap] = x;
        return new Board(boardTiles);
    }

    private int[][] cloneIntArr(int[][] originalArr) {
        int rowSize = originalArr.length;
        int columnSize = originalArr[0].length;
        int[][] cloneArr = new int[rowSize][columnSize];
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < columnSize; j++) {
                cloneArr[i][j] = originalArr[i][j];
            }
        }
        return cloneArr;
    }

    // unit testing (not graded)
    public static void main(String[] args) {

        int[][] boardArr = { { 2, 4, 1 }, { 3, 8, 0 }, { 5, 7, 6 } };

        Board board = new Board(boardArr);
        System.out.println(board);
        System.out.println(board.twin());
        System.out.println("Board dimension: " + board.dimension());
        System.out.println("Board hamming: " + board.hamming());
        System.out.println("Board manhattan: " + board.manhattan());
        int[][] boardArr2 = { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };
        Board board2 = new Board(boardArr2);
        System.out.println("Board 1 equals board 2: " + board.equals(board2));
        int[][] boardArr3 = { { 1, 0, 3 }, { 4, 2, 5 }, { 7, 8, 6 } };
        Board board3 = new Board(boardArr3);
        System.out.println("Board 3 toString: " + board3.toString());
        System.out.println("Board 3 neighbours");
        for (Board boardNeighbor : board3.neighbors()) {
            System.out.println(boardNeighbor.toString());
        }
        int[][] boardArr4 = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } };
        Board board4 = new Board(boardArr4);
        System.out.println("Board4 isGoal: " + board4.isGoal());
        System.out.println("Board isGoal: " + board.isGoal());

        for (Board neighbors : board.neighbors()) {
            System.out.println(neighbors.toString());
        }
    }
}
