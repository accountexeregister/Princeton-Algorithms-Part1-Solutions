/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private final boolean puzzleSolved;
    private final int totalMovesFinal;
    private final SearchNode finalSearchNode;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        MinPQ<SearchNode> minPQ = new MinPQ<SearchNode>();
        MinPQ<SearchNode> minPQTwin = new MinPQ<SearchNode>();
        minPQ.insert(new SearchNode(initial));
        minPQTwin.insert(new SearchNode(initial.twin()));
        SearchNode deletedSearchNode;
        // Create minPQsN that determine number of MinPQ object to switch between
        int minPQsN = 2;
        // minPQsIndex is only 0 or 1, where 0 refers to minPQ, 1 refers to minPQTwin
        int minPQsIndex = 0;
        MinPQ<SearchNode> currentMinPQ = minPQ;
        if (!(currentMinPQ.min().board.isGoal())) {
            while (true) {
                if (minPQsIndex == 0) {
                    currentMinPQ = minPQ;
                }
                else {
                    currentMinPQ = minPQTwin;
                }
                deletedSearchNode = currentMinPQ.delMin();
                insertNeighbors(deletedSearchNode, currentMinPQ);
                if (currentMinPQ.min().board.isGoal()) {
                    break;
                }
                minPQsIndex++;
                if (minPQsIndex >= minPQsN) {
                    minPQsIndex = 0;
                }
            }
        }
        SearchNode solvedNodeMin;
        if (currentMinPQ.min().moves < currentMinPQ.min().priority
                || minPQsIndex != 0) {
            puzzleSolved = false;
            totalMovesFinal = -1;
            finalSearchNode = null;
            return;
        }
        solvedNodeMin = currentMinPQ.min();
        finalSearchNode = solvedNodeMin;
        totalMovesFinal = solvedNodeMin.moves;
        puzzleSolved = true;
    }

    private void insertNeighbors(SearchNode target, MinPQ<SearchNode> targetMinPQ) {
        for (Board previousNodeNeighbor : target.board.neighbors()) {
            if (target.previousSearchNode != null && previousNodeNeighbor.equals(
                    target.previousSearchNode.board)) {
                continue;
            }
            SearchNode previousNodeNeighborNode = new SearchNode(previousNodeNeighbor,
                                                                 target);
            targetMinPQ.insert(previousNodeNeighborNode);
        }
    }

    private final class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final int moves;
        private final SearchNode previousSearchNode;
        private final int priority;

        private SearchNode(Board board) {
            this.board = board;
            previousSearchNode = null;
            moves = 0;
            priority = priority();
        }

        private SearchNode(Board board, SearchNode previousSearchNode) {
            this.board = board;
            this.previousSearchNode = previousSearchNode;
            moves = previousSearchNode.moves + 1;
            priority = priority();
        }

        public int compareTo(SearchNode other) {
            // Using Manhattan priority function
            return Integer.compare(priority, other.priority);
        }

        private int priority() {
            return board.manhattan() + moves;
        }

    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return puzzleSolved;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return totalMovesFinal;
    }

    // sequence of boards in the shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!puzzleSolved) {
            return null;
        }
        Stack<Board> solutionBoards = new Stack<Board>();
        SearchNode searchNodeTraceBack = finalSearchNode;
        while (searchNodeTraceBack != null) {
            solutionBoards.push(searchNodeTraceBack.board);
            searchNodeTraceBack = searchNodeTraceBack.previousSearchNode;
        }
        return solutionBoards;
    }

    // test client (see below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
