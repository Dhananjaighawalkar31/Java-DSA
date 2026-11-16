package Graph;

import java.util.ArrayList;
import java.util.Stack;

public class DFSUsingStack {

    public ArrayList<Integer> dfs(ArrayList<ArrayList<Integer>> adj, int V) {

        ArrayList<Integer> result = new ArrayList<>();
        int[] visited = new int[V];
        Stack<Integer> st = new Stack<>();
        st.push(0);
        visited[0] = 1;
        while(!st.isEmpty()){
            int k = st.pop();
            result.add(k);
            ArrayList<Integer> arr = adj.get(k);
            for(int i = arr.size()-1;i>=0;i--){
                int x= arr.get(i);
                if(visited[x] == 0){
                    st.push(x);
                    visited[x] = 1;
                }
            }
        }
        return result;


    }

    public static void main(String[] args) {

        int V = 6;

        ArrayList<ArrayList<Integer>> adj = new ArrayList<>();

        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }

        adj.get(0).add(1);
        adj.get(1).add(0);

        adj.get(0).add(2);
        adj.get(2).add(0);

        adj.get(1).add(3);
        adj.get(3).add(1);

        adj.get(1).add(4);
        adj.get(4).add(1);

        adj.get(2).add(5);
        adj.get(5).add(2);

        DFSUsingStack obj = new DFSUsingStack();

        System.out.println(obj.dfs(adj, V));
    }
}