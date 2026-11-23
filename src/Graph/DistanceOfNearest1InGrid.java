package Graph;

import java.util.*;

class DistanceOfNearest1InGrid {

    static class Pair {
        int x;
        int y;
        int z;

        Pair(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    public ArrayList<ArrayList<Integer>> nearest(int[][] grid) {

        int row = grid.length;
        int col = grid[0].length;

        ArrayList<ArrayList<Integer>> res = new ArrayList<>();
        boolean[][] visited = new boolean[row][col];

        Queue<Pair> q = new ArrayDeque<>();

        for (int i = 0; i < row; i++) {

            ArrayList<Integer> li = new ArrayList<>();

            for (int j = 0; j < col; j++) {

                if (grid[i][j] == 1) {
                    visited[i][j] = true;
                    q.add(new Pair(i, j, 0));
                }

                li.add(0);
            }

            res.add(li);
        }

        int[] delRow = {-1, 0, 1, 0};
        int[] delCol = {0, 1, 0, -1};

        while (!q.isEmpty()) {

            Pair p = q.poll();

            int r = p.x;
            int c = p.y;
            int steps = p.z;

            for (int i = 0; i < 4; i++) {

                int m = delRow[i] + r;
                int n = delCol[i] + c;

                if (m >= 0 && m < row &&
                        n >= 0 && n < col &&
                        !visited[m][n]) {

                    q.add(new Pair(m, n, steps + 1));

                    visited[m][n] = true;

                    res.get(m).set(n, steps + 1);
                }
            }
        }

        return res;
    }
}