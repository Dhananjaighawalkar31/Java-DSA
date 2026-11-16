import java.util.*;

public class NumberOfIslands8Directions {

    class Pair {
        int x;
        int y;

        Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public void bfs(char[][] grid, boolean[][] visited, int i, int j) {

        int row = grid.length;
        int col = grid[0].length;

        Queue<Pair> q = new ArrayDeque<>();

        visited[i][j] = true;
        q.add(new Pair(i, j));

        // All 8 directions
        int[] delrow = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] delcol = {-1, 0, 1, -1, 1, -1, 0, 1};

        while (!q.isEmpty()) {

            Pair p = q.poll();

            int r = p.x;
            int c = p.y;

            for (int k = 0; k < 8; k++) {

                int nrow = r + delrow[k];
                int ncol = c + delcol[k];

                if (nrow >= 0 && nrow < row &&
                        ncol >= 0 && ncol < col &&
                        grid[nrow][ncol] == '1' &&
                        !visited[nrow][ncol]) {

                    visited[nrow][ncol] = true;
                    q.add(new Pair(nrow, ncol));
                }
            }
        }
    }

    public int numIslands(char[][] grid) {

        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }

        int row = grid.length;
        int col = grid[0].length;

        boolean[][] visited = new boolean[row][col];

        int count = 0;

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {

                if (!visited[i][j] && grid[i][j] == '1') {

                    count++;
                    bfs(grid, visited, i, j);
                }
            }
        }

        return count;
    }

    public static void main(String[] args) {

        char[][] grid = {
                {'1', '1', '0', '0', '0'},
                {'1', '1', '0', '0', '0'},
                {'0', '0', '1', '0', '0'},
                {'0', '0', '0', '1', '1'}
        };

        NumberOfIslands8Directions obj = new NumberOfIslands8Directions();

        System.out.println(obj.numIslands(grid));
    }
}