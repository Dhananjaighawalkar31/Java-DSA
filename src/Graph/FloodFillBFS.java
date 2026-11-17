package Graph;

import java.util.*;

public class FloodFillBFS {

    class Pair {
        int x;
        int y;

        Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public int[][] floodFill(int[][] image, int sr, int sc, int newColor) {

        int row = image.length;
        int col = image[0].length;

        int originalColor = image[sr][sc];

        if (originalColor == newColor) {
            return image;
        }

        boolean[][] visited = new boolean[row][col];

        bfs(image, sr, sc, newColor, originalColor, visited, row, col);

        return image;
    }

    public void bfs(int[][] image, int sr, int sc, int newColor,
                    int originalColor, boolean[][] visited,
                    int row, int col) {

        Queue<Pair> q = new ArrayDeque<>();

        int[] delRow = {-1, 0, 1, 0};
        int[] delCol = {0, 1, 0, -1};

        visited[sr][sc] = true;
        q.add(new Pair(sr, sc));

        while (!q.isEmpty()) {

            Pair p = q.poll();

            int a = p.x;
            int b = p.y;

            image[a][b] = newColor;

            for (int k = 0; k < 4; k++) {

                int m = a + delRow[k];
                int n = b + delCol[k];

                if (m >= 0 && m < row &&
                        n >= 0 && n < col &&
                        !visited[m][n] &&
                        image[m][n] == originalColor) {

                    visited[m][n] = true;
                    q.add(new Pair(m, n));
                }
            }
        }
    }

    public static void main(String[] args) {

        int[][] image = {
                {1, 1, 1, 0},
                {0, 1, 1, 1},
                {1, 0, 1, 1}
        };

        int sr = 1;
        int sc = 2;
        int newColor = 2;

        FloodFillBFS obj = new FloodFillBFS();

        int[][] result = obj.floodFill(image, sr, sc, newColor);

        for (int[] arr : result) {
            System.out.println(Arrays.toString(arr));
        }
    }
}