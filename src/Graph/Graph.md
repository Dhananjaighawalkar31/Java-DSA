# Graph Data Structure

## Problems Solved

| #  | Problem | Approach | Intuition |
|----|---|---|---|
| 1  | Breadth First Search (BFS) | Queue | Explore nodes level by level. |
| 2  | Depth First Search (DFS) | Recursion | Explore as deep as possible before backtracking. |
| 3  | Iterative DFS | Stack | Use LIFO to simulate recursive DFS. |
| 4  | Number of Provinces | DFS | Count connected components in a graph. |
| 5  | Number of Islands (8 Directions) | BFS | Explore all 8 neighbouring cells to identify connected land. |
| 6  | Flood Fill | BFS | Replace the colour of all connected cells having the original colour. |
| 7  | Flood Fill | DFS | Recursively visit and recolour connected cells. |
| 8  | Rotten Oranges | Multi-Source BFS | Start from all rotten oranges simultaneously and track the minimum time to rot fresh oranges. |
| 9  | Undirected Graph Cycle Detection | BFS | Detect a visited neighbour that is not the current node's parent. |
| 10 | Bipartite Graph | BFS (Two-Coloring) | Assign two alternating colors to adjacent vertices; a same-color conflict means the graph is not bipartite. |
| 11 | Distance of Nearest 1 in Grid | Multi-Source BFS | Start BFS from all cells containing 1 simultaneously to calculate the minimum distance to every other cell. |
| 12 | Surrounded Regions | DFS | Start DFS from boundary O cells and preserve all connected O cells; convert the remaining O cells into X. |