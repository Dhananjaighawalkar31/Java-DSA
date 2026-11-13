package Graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class BFS {

    public ArrayList<Integer> bfs(ArrayList<ArrayList<Integer>> adj) {
        int v = adj.size();

        int[] visited = new int[v];

        Queue<Integer> q = new LinkedList<>();

        int start = 0;

        visited[start] = 1;
        q.add(start);

        ArrayList<Integer> result = new ArrayList<>();

        while (!q.isEmpty()) {
            int node = q.poll();
            result.add(node);

            for (int neighbour : adj.get(node)) {
                if (visited[neighbour] == 0) {
                    visited[neighbour] = 1;
                    q.add(neighbour);
                }
            }
        }

        return result;
    }
}