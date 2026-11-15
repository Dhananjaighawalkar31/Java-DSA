package Graph;

public class NumberOfProvinces {
    public static void main(String[] args) {

        NumberOfProvinces obj = new NumberOfProvinces();

        int[][] isConnected = {
                {1, 1, 0},
                {1, 1, 0},
                {0, 0, 1}
        };

        System.out.println(obj.findCircleNum(isConnected));
    }
    public int findCircleNum(int[][] isConnected) {

        int v = isConnected.length;
        int[] visited = new int[v];

        int count = 0;

        for (int i = 0; i < v; i++) {

            if (visited[i] == 0) {
                dfs(isConnected, visited, i);
                count++;
            }
        }

        return count;
    }

    public void dfs(int[][] isConnected, int[] visited, int node) {

        visited[node] = 1;

        for (int i = 0; i < isConnected.length; i++) {

            if (isConnected[node][i] == 1 && visited[i] == 0) {
                dfs(isConnected, visited, i);
            }
        }
    }
}