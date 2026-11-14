package Graph;

import java.util.ArrayList;

class Solution {

    public ArrayList<Integer> dfs(ArrayList<ArrayList<Integer>> adj) {

        int size = adj.size();

        int[] visited = new int[size];
        ArrayList<Integer> li = new ArrayList<>();

        dfs(li, visited, 0, adj);

        return li;
    }

    public void dfs(ArrayList<Integer> li, int[] visited,
                    int node, ArrayList<ArrayList<Integer>> adj) {

        visited[node] = 1;
        li.add(node);

        for (int neighbour : adj.get(node)) {
            if (visited[neighbour] == 0) {
                dfs(li, visited, neighbour, adj);
            }
        }
    }
}