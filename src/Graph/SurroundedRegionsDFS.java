package Graph;

public class SurroundedRegionsDFS {

    public void dfs(char[][] board, int m, int n,
                    boolean[][] visited, int i, int j) {

        if (i >= m || j >= n || i < 0 || j < 0 || visited[i][j]) {
            return;
        }

        visited[i][j] = true;

        int[] delRow = {-1, 0, 1, 0};
        int[] delCol = {0, 1, 0, -1};

        for (int k = 0; k < 4; k++) {

            int x = delRow[k] + i;
            int y = delCol[k] + j;

            if (x >= 0 && x < m && y >= 0 && y < n
                    && !visited[x][y] && board[x][y] == 'O') {

                dfs(board, m, n, visited, x, y);
            }
        }
    }

    public void solve(char[][] board) {

        int m = board.length;
        int n = board[0].length;

        boolean[][] visited = new boolean[m][n];

        // Step 1: Start DFS from boundary O cells
        for (int i = 0; i < m; i++) {

            for (int j = 0; j < n; j++) {

                if ((i == 0 || j == 0 || i == m - 1 || j == n - 1)
                        && !visited[i][j] && board[i][j] == 'O') {

                    dfs(board, m, n, visited, i, j);
                }
            }
        }

        // Step 2: Capture surrounded O regions
        for (int i = 0; i < m; i++) {

            for (int j = 0; j < n; j++) {

                if (!visited[i][j] && board[i][j] == 'O') {
                    board[i][j] = 'X';
                }
            }
        }
    }
}