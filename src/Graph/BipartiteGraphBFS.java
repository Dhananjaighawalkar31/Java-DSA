package Graph;

import java.util.*;

public class BipartiteGraphBFS {

    public boolean isBipartite(ArrayList<ArrayList<Integer>> adj) {

        int V = adj.size();

        int[] color = new int[V];
        Arrays.fill(color, -1);

        Queue<Integer> q = new LinkedList<>();

        // Handle disconnected components
        for (int i = 0; i < V; i++) {

            if (color[i] == -1) {

                color[i] = 0;
                q.add(i);

                while (!q.isEmpty()) {

                    int u = q.poll();

                    for (int v : adj.get(u)) {

                        if (color[v] == -1) {

                            color[v] = 1 - color[u];
                            q.add(v);

                        } else if (color[v] == color[u]) {

                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }
}