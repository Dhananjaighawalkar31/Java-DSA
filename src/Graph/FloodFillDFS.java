package Graph;

public class FloodFillDFS {

    public int[][] floodFill(int[][] image, int sr, int sc, int color) {

        int row = image.length;
        int col = image[0].length;

        int originalColor = image[sr][sc];

        if (originalColor == color) {
            return image;
        }

        boolean[][] visited = new boolean[row][col];

        dfs(image, sr, sc, color, row, col, originalColor, visited);

        return image;
    }

    public void dfs(int[][] image, int sr, int sc, int color,
                    int row, int col, int originalColor,
                    boolean[][] visited) {

        if (sr < 0 || sr >= row ||
                sc < 0 || sc >= col ||
                visited[sr][sc] ||
                image[sr][sc] != originalColor) {
            return;
        }

        visited[sr][sc] = true;
        image[sr][sc] = color;

        // Up
        dfs(image, sr - 1, sc, color, row, col, originalColor, visited);

        // Right
        dfs(image, sr, sc + 1, color, row, col, originalColor, visited);

        // Down
        dfs(image, sr + 1, sc, color, row, col, originalColor, visited);

        // Left
        dfs(image, sr, sc - 1, color, row, col, originalColor, visited);
    }

    public static void main(String[] args) {

        int[][] image = {
                {1, 1, 1, 0},
                {0, 1, 1, 1},
                {1, 0, 1, 1}
        };

        FloodFillDFS obj = new FloodFillDFS();

        int[][] result = obj.floodFill(image, 1, 2, 2);

        for (int[] arr : result) {
            System.out.println(java.util.Arrays.toString(arr));
        }
    }
}