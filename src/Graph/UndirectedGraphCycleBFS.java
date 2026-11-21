package Graph;

import java.util.*;

public class UndirectedGraphCycleBFS {

    static class Pair {
        int curr;
        int par;

        Pair(int curr, int parent) {
            this.curr = curr;
            this.par = parent;
        }
    }

    public boolean isCycle(ArrayList<ArrayList<Integer>> adj) {

        int V = adj.size();

        boolean[] visited = new boolean[V];

        Queue<Pair> q = new LinkedList<>();

        // Handle disconnected components
        for (int start = 0; start < V; start++) {

            if (!visited[start]) {

                q.add(new Pair(start, -1));
                visited[start] = true;

                while (!q.isEmpty()) {

                    Pair p = q.poll();

                    int current = p.curr;
                    int parent = p.par;

                    for (int i : adj.get(current)) {

                        if (!visited[i]) {

                            visited[i] = true;
                            q.add(new Pair(i, current));

                        } else if (i != parent) {

                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public static void main(String[] args) {

        ArrayList<ArrayList<Integer>> adj = new ArrayList<>();

        int V = 4;

        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }

        // Add undirected edges
        adj.get(0).add(1);
        adj.get(1).add(0);

        adj.get(0).add(2);
        adj.get(2).add(0);

        adj.get(1).add(2);
        adj.get(2).add(1);

        adj.get(2).add(3);
        adj.get(3).add(2);

        UndirectedGraphCycleBFS obj = new UndirectedGraphCycleBFS();

        System.out.println(obj.isCycle(adj));
    }
}