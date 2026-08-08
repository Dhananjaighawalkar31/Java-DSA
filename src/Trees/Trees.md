# 🌳 Binary Tree DSA — Complete Notes & Intuition

> Reference doc for the `Tree.java` file. Each section = one problem: **what it asks, why the code looks like that, the core intuition, complexity, and test cases.**

---

## 📌 Setup: `Node` class

```java
class Node {
    int data;
    Node left;
    Node right;
}
```
Standard binary tree node. Every problem below operates on this.

**Helper classes used later:**
- `Tuple(node, column, row)` — for vertical order traversal (BFS with coordinates)
- `NodeWithLine(node, line)` — for top/bottom view (BFS with horizontal distance)
- `PairForWidthOfTree(node, index)` — for max width (BFS with positional index)

---

## 1. Recursive Traversals — `preOrder`, `inOrder`, `postOrder`

**Problem:** Visit every node in a tree in Root-Left-Right (pre), Left-Root-Right (in), or Left-Right-Root (post) order.

**Intuition:** These are the *foundation* of tree recursion. The position of `System.out.println(root.data)` relative to the two recursive calls decides the order. Every other problem in this file is a variation of this base pattern (do something before/between/after recursing into children).

```java
preOrder(root)  → print BEFORE going left/right
inOrder(root)   → print BETWEEN going left and right
postOrder(root) → print AFTER going left/right
```

**Test case (tree used in `main`):**
```
        1
       / \
      2   3
     / \  /
    4  5 6
```
- PreOrder: `1 2 4 5 3 6`
- InOrder: `4 2 5 1 6 3`
- PostOrder: `4 5 2 6 3 1`

---

## 2. `levelWiseTraversal` (BFS / Level Order)

**Problem:** Print the tree level by level (top to bottom, left to right per level), e.g. LeetCode 102 "Binary Tree Level Order Traversal".

**Intuition:** Recursion naturally goes *deep* first, but level order needs to go *wide* first. So you switch tools: recursion → **Queue (BFS)**.
Key trick: capture `q.size()` **before** the inner loop starts (`levelSize`). That's what freezes "how many nodes are in the current level" — otherwise children you just added would mix into the same level.

**Approach:**
1. Push root into queue.
2. While queue not empty: record `levelSize`, loop exactly that many times, popping nodes and pushing their children.
3. Each level's popped values go into one sub-list.

**Test case:**
```
Input: same tree as above
Output: [[1], [2,3], [4,5,6]]
```

---

## 3. `iterativePreOrder`

**Problem:** Do preorder traversal *without recursion* (interviewers ask this to test if you understand recursion = implicit stack).

**Intuition:** Recursion uses the call stack behind the scenes. To go iterative, you maintain your **own explicit `Stack<Node>`**.
Because a stack is LIFO, and we want **left processed before right**, we push **right child first, then left child** — so left pops out first.

**Approach:**
1. Push root.
2. Pop → print → push right, then push left.
3. Repeat till stack empty.

**Test case:** Output must match recursive preorder: `1 2 4 5 3 6`

---

## 4. `iterativeInOrder`

**Problem:** Inorder traversal without recursion.

**Intuition:** Unlike preorder, you can't just print on pop — you need to **go all the way left first**, then process, then move right. So the pattern is:
- Keep pushing left children until `null`.
- When you can't go left anymore, pop, visit, then move to `.right` and repeat.

This mimics recursion's "unwind" behavior manually.

**Approach:**
```
while node != null OR stack not empty:
    if node != null: push node, node = node.left
    else: pop node, add to result, node = node.right
```

**Test case:** Output: `4 2 5 1 6 3` (same as recursive inorder)

---

## 5. `iterativePostOrder` (Two-Stack Method)

**Problem:** Postorder without recursion — hardest of the three because postorder = Left-Right-Root, and with a stack you naturally get Root-first orders.

**Intuition (the trick you used):** Postorder is the **reverse of "Root-Right-Left"**.
So:
1. Do a *modified preorder* but push **left before right** (so right pops first) → this naturally produces Root-Right-Left order into `st2`.
2. Popping everything out of `st2` reverses it → Left-Right-Root = postorder. 

That's why you have two stacks: `st1` generates the reversed order, `st2` collects it, and the final pop-all-of-st2 gives correct postorder.

**Test case:** Output: `4 5 2 6 3 1`

---

## 6. `iterativePostOrderSingleStack`

**Problem:** Same as above but the *optimized, single-stack* version — a common follow-up ("can you do it with just one stack?").

**Intuition:** Track a `lastVisited` node.
- Go as left as possible, pushing along the way.
- At the top of stack, look at its **right child**:
  - If right child is `null` OR right child was **already visited** (`== lastVisited`) → you're allowed to process (pop) this node now.
  - Otherwise, you haven't explored the right subtree yet → go right.

This is the classic **"visit right subtree before popping yourself" check**, which is what makes single-stack postorder tricky — you need to remember what you last finished, or you'd loop forever re-visiting the same right subtree.

**Test case:** Output: `4 5 2 6 3 1` (same as two-stack version, just more efficient)

---

## 7. `maxDepth`

**Problem:** LeetCode 104 — find the maximum depth (height) of a binary tree.

**Intuition:** Classic **"ask your children, then add 1 for yourself"** recursion pattern.
`maxDepth(node) = 1 + max(maxDepth(left), maxDepth(right))`
Base case: `null` node has depth 0.

**Test case:**
```
Tree above → maxDepth = 3 (1 → 2 → 4, or 1 → 2 → 5, or 1 → 3 → 6)
Empty tree → 0
Single node → 1
```

---

## 8. `minDepth`

**Problem:** LeetCode 111 — find the shortest path from root to the **nearest leaf** (not just nearest null).

**Intuition (why it's trickier than maxDepth):** A node with only ONE child is NOT a valid "shorter path" — you can't stop there because it's not a leaf. So `min(left, right)` alone breaks if one side is 0 (meaning "no child there", not "shortest depth 0").
That's why the code explicitly checks:
- If both children exist → `1 + min(l, r)`
- If only left exists → `1 + l`
- If only right exists → `1 + r`

**Test case:**
```
      1
     /
    2
   /
  3
```
minDepth should be **3** (not 1), because node 1 → 2 → 3 is the only path to an actual leaf (node 1 has no right child, so you can't stop at depth 1).

---

## 9. `countNodes`

**Problem:** Count total number of nodes in the tree.

**Intuition:** Simplest possible recursion: `count(node) = 1 (for itself) + count(left) + count(right)`.

**Test case:** Tree above → 6 nodes.

---

## 10. `invertTree`

**Problem:** LeetCode 226 — Mirror the tree (swap left and right children at every node).

**Intuition:** Recurse down FIRST (invert children), then swap the (already-inverted) left/right pointers at the current node. Order technically doesn't matter here (could swap first then recurse) — both work since swapping is a local pointer operation.

**Test case:**
```
Input:        Output:
    1             1
   / \           / \
  2   3    →    3   2
 / \           / \
4   5         5   4
```

---

## 11. `hasPathSum`

**Problem:** LeetCode 112 — does any root-to-**leaf** path sum exactly equal `targetSum`?

**Intuition:** At every step, subtract current node's value from target and pass it down. Only check for a match **at leaf nodes** (root.left == null && root.right == null) — an internal node "matching" doesn't count since path must end at a leaf.

**Test case:**
```
Tree above, targetSum = 5
Path 1→2→? no leaf sums to 5 alone via simple paths... 
Example that works: single node tree with value 5, targetSum=5 → true
Tree 1-2-4 (path sum 1+2+4=7): hasPathSum(root,7) → true
hasPathSum(root,5) → false (no root-to-leaf path sums to 5 in this tree)
```

---

## 12. `diameterOfTree`

**Problem:** LeetCode 543 — Diameter = longest path between any two nodes (path may or may not pass through root, measured in **number of edges**).

**Intuition:** The diameter *through* any node = `leftHeight + rightHeight`. So while computing height recursively (bottom-up), at every node you **also** update a global `max` with `lh + rh`. This is a classic "compute one thing, but piggyback a global answer update" pattern — you get diameter "for free" while computing height.

**Test case:**
```
Tree above:
      1
     / \
    2   3
   / \  /
  4  5 6
Diameter = 3 (path 4-2-1-3-6 has 4 edges... let's verify: 
  4→2→1→3→6 = 4 edges, but diameter counted as lh+rh at node 1 = 2+2=4? 
  Actually check by node: at node 2, lh=1(via4),rh=1(via5)→ diam contribution 2
  at node 1, lh=2 (2->4), rh=2(3->6) → contribution 4
So max diameter = 4 edges (path 4-2-1-3-6))
```
*(Double check by running it — this is exactly why the doc matters, so you can re-verify against real output!)*

---

## 13. `isBalanced`

**Problem:** LeetCode 110 — check if tree is height-balanced (every node's left/right subtree heights differ by ≤ 1).

**Intuition:** Naive way = compute height of every subtree separately → O(n²). 
**The trick:** compute height and check balance **in the same recursive pass**, using `-1` as a **sentinel/poison value** meaning "already found unbalanced below — stop wasting time computing further, just bubble up -1."
This turns it into O(n).

**Test case:**
```
Balanced:        1
                / \
               2   3
              / 
             4
→ true (max diff 1 anywhere)

Unbalanced:      1
                /
               2
              /
             3
→ false (node 1: left height 2, right height 0, diff = 2)
```

---

## 14. `BinaryTreePath`

**Problem:** LeetCode 257 — Return all root-to-leaf paths as strings like `"1->2->4"`.

**Intuition:** Build the path string as you recurse down (pass current path + new node as parameter — no need to backtrack/undo since Strings are immutable, each recursive call gets its own copy). Only add to result list when you hit a **leaf**.

**Test case:**
```
Tree above → ["1->2->4", "1->2->5", "1->3->6"]
```

---

## 15. `maxPathSum`

**Problem:** LeetCode 124 — Maximum Path Sum. Path can start and end at ANY node (doesn't have to include root, doesn't have to end at leaf).

**Intuition (the hardest tree DP problem in this list):** Two different concepts get returned:
1. What you **return** to your parent = `node.data + max(0, leftGain, rightGain)` — because a parent can only extend the path through ONE child (can't use both, path would fork).
2. What you **update the global max with** = `node.data + leftGain + rightGain` — because at THIS node specifically, you're allowed to use BOTH children (the path bends here, this is its peak).
Negative gains are clamped to 0 (`if lh<0: lh=0`) because you'd rather not extend the path at all than add a negative sum.

**Test case:**
```
Tree above (all positive values):
Max path sum = 4 + 2 + 5 + 1 + 3 + 6 = 21 (the whole tree, since all values positive)

Classic LeetCode example: [-10,9,20,null,null,15,7] → answer 42 (path 15→20→7)
```

---

## 16. `zigzagLevelOrder`

**Problem:** LeetCode 103 — Level order traversal but alternate direction each level (left→right, then right→left, then left→right...).

**Intuition:** Same BFS level-order skeleton as problem #2, but with a boolean flag `b` that flips each level. When `b` is false, reverse that level's list before adding to result. You still collect nodes left-to-right while processing (queue order doesn't change) — only the **output list** gets reversed for alternate levels. Simpler than trying to actually traverse right-to-left.

**Test case:**
```
Tree above → [[1], [3,2], [4,5,6]]
(level 0 normal, level 1 reversed: 3,2 instead of 2,3, level 2 normal)
```

---

## 17. `boundaryTraversalInAntiClockWise` (+ `getLeft`, `getLeaf`, `getRight`, `isLeaf`)

**Problem:** Print the boundary of the tree (outline) in anti-clockwise order: root → left boundary (top to bottom, excluding leaves) → all leaves (left to right) → right boundary (bottom to top, excluding leaves).

**Intuition:** Broken into 3 clean sub-problems, each with a DIFFERENT traversal strategy — a good example of "decompose a hard problem into simpler known patterns":
- `getLeft`: Walk down from root.left, prefer going left, but go right if no left child exists (root.left excluded if it's a leaf itself... handled by `isLeaf` checks) — **added in top-to-bottom order** directly.
- `getLeaf`: A pure inorder-ish traversal (any traversal works) that only picks up nodes with no children.
- `getRight`: Walk down from root.right, prefer going right — but pushed onto a **Stack** first, then popped, because we need this side printed **bottom-to-top** (reverse of natural top-down visiting order).

**Why `isLeaf` checks are everywhere:** to avoid double-counting a boundary node as both "left boundary" and "leaf" (a leaf on the left edge shouldn't be printed twice).

**Test case:**
```
Tree above:
      1
     / \
    2   3
   / \  /
  4  5 6
Boundary (anticlockwise) = [1, 2, 4, 5, 6, 3]
(1=root, 2=left boundary non-leaf, 4,5=leaves left to right... wait 6 is also leaf... 
 Actually: root=1, left boundary(excl leaf)=[2], leaves=[4,5,6], right boundary(excl leaf, bottom-up)=[3]
 Result: [1, 2, 4, 5, 6, 3])
```

---

## 18. `verticalTraversal`

**Problem:** LeetCode 987 — Print nodes column by column (vertical lines through the tree). Within the same column AND same row, sort by value ascending (ties broken by value, per LeetCode's rules).

**Intuition:** This is BFS with **coordinates** instead of just node values:
- `column` shifts by `-1` going left, `+1` going right (like a number line, root = 0).
- `row` increases by `+1` every level down (needed to correctly order same-column nodes that are at different levels, AND to know which nodes are "tied" at the exact same row+column for sorting).
- Data structure: `TreeMap<column, TreeMap<row, PriorityQueue<value>>>` — nested sorted maps + min-heap so everything comes out already sorted: columns ascending, rows ascending within column, values ascending within same row+column.

**Test case:**
```
Tree above:
Column -2: [4]
Column -1: [2]
Column  0: [1, 6]   (1 is row0, 6 is row2 — both same column, different row, so order = row order: 1 then 6)
Column  1: [5]      wait — 5's column is +1? let's recompute:
  root(1): col 0, row 0
  2: col -1, row 1;  3: col +1, row 1
  4: col -2, row 2;  5: col 0, row 2;  6: col 0, row 2  (3's left child)
Result columns: [-2:[4], -1:[2], 0:[1,5,6] (row0=1, row2=5&6 sorted by value), +1:[3]]
```

---

## 19. `topViewOfBinaryTree`

**Problem:** What nodes are visible if you look at the tree from directly **above** (top view) — one node per vertical line, whichever is encountered **first** (topmost/root-closest).

**Intuition:** BFS with horizontal distance (`line`, same idea as column above, but simpler — no row needed since BFS processes level-by-level so first-seen = topmost automatically).
Key line: `if (!map.containsKey(line))` — **only insert if this vertical line hasn't been recorded yet**. Since BFS goes level by level, the first node to reach a given line IS the topmost one — later nodes at that line get ignored.

**Test case:**
```
Tree above → Top view = [4, 2, 1, 3, 6]
(line -2:4, line -1:2, line 0:1, line +1:3, line +1... wait 6 is line 0 too via 3.left)
Let's recompute lines: 1(0), 2(-1), 3(+1), 4(-2), 5(0), 6(0)
Since 1 arrives at line 0 first (BFS level order), 5 and 6 arriving later at line 0 get ignored.
Top view = [4, 2, 1, 3]
```

---

## 20. `bottomViewOfBinaryTree`

**Problem:** Opposite of top view — what's visible from **below**. One node per vertical line, whichever is **last**/deepest.

**Intuition:** Nearly identical code to top view, EXCEPT: **always overwrite** `map.put(line, node)` (no `containsKey` check). Since BFS still goes level by level, later nodes always overwrite earlier ones at the same line — so what remains at the end is the deepest (bottom-most) node per line. Simple but elegant: same BFS, just remove one `if` check to flip the whole problem.

**Test case:**
```
Same tree → Bottom view = [4, 2, 5, 6, 3]
(line -2:4, -1:2, 0: overwritten by 5 then 6 → last one wins = 6, +1:3)
lines: 1(0)→2(-1)→3(1)→4(-2)→5(0)→6(0)
final map: -2:4, -1:2, 0:6 (5 got overwritten), 1:3
Bottom view = [4, 2, 6, 3]
```

---

## 21. `rightViewOfBinaryTree`

**Problem:** LeetCode 199 — nodes visible from the right side (rightmost node at every level).

**Intuition:** DFS (not BFS this time!) where you recurse **right before left**, and only add a node to result when `al.size() == currentDepth` — meaning "this is the first time we've reached this depth," which, since right is visited first, guarantees it's the rightmost node at that depth.

**Test case:**
```
Tree above → Right view = [1, 3, 6]
```

---

## 22. `leftViewOfBInaryTree`

**Problem:** LeetCode leftside view — nodes visible from the left (leftmost at every level).

**Intuition:** Exact mirror of right view — recurse **left before right**. Same `al.size()==depth` trick, but now the first node reaching a new depth is the leftmost one.

**Test case:**
```
Tree above → Left view = [1, 2, 4]
```

---

## 23. `isSymmetric`

**Problem:** LeetCode 101 — Is the tree a mirror of itself (symmetric around its center)?

**Intuition:** Compare `root.left` vs `root.right` using a helper that checks **two trees are mirrors** of each other: `n1.data == n2.data` AND `n1.left mirrors n2.right` AND `n1.right mirrors n2.left` (note the CROSS comparison — that's the key insight, not `left vs left`).

**Test case:**
```
Symmetric:      1
               / \
              2   2
             / \ / \
            3  4 4  3
→ true

Tree in main() (2≠3, different structure) → false
```

---

## 24. `rootToNodePath`

**Problem:** Find the path (list of values) from root to a target node value `k`.

**Intuition:** Classic **backtracking**: add current node to path, recurse into children, and if neither returns `true`, **remove the last added node** (`al.remove(al.size()-1)`) before backtracking up — this "undo" step is essential, otherwise wrong-path nodes would stay in the list.

**Test case:**
```
rootToNodePath(root, 5) → [1, 2, 5]
rootToNodePath(root, 6) → [1, 3, 6]
rootToNodePath(root, 7) → [] (7 doesn't exist in tree, since helper eventually returns false and list ends up emptied)
```

---

## 25. `lowestCommonAncestors`

**Problem:** LeetCode 236 — Find LCA of two nodes x and y.

**Intuition:** At each node, ask: "is x or y found in my left subtree? in my right subtree?"
- If **both sides return non-null** → current node IS the LCA (x and y are in different subtrees, splitting here).
- If only one side is non-null → LCA must be further down that side, so bubble that answer up.
- Base case: if current node itself IS x or y, return it immediately (a node can be its own ancestor).

**Test case:**
```
lowestCommonAncestors(root, 2, 3) → returns node 1 (root), since 2 and 3 are in different subtrees
lowestCommonAncestors(root, 4, 5) → returns node 2 (both 4,5 under node 2)
```

---

## 26. `MaximumWidthofBinaryTree`

**Problem:** LeetCode 662 — Maximum width = max number of nodes between the leftmost and rightmost non-null node at any level (**counting the gaps as if nulls were present**, like a complete binary tree indexing).

**Intuition:** Assign each node an **index** as if the tree were a complete binary tree array: root=0, left child = `2*i+1`, right child = `2*i+2`. Width at a level = `lastIndex - firstIndex + 1`.
**The subtlety:** indices can overflow (grow exponentially) for skewed trees, so the code **normalizes** by subtracting the minimum index of that level (`currIndex = pair.index - min`) before using it for the next level's calculation — keeps numbers small and prevents overflow while preserving relative gaps.

**Test case:**
```
Tree above:
Level 0: index [0] → width 1
Level 1: 2(idx1), 3(idx2) → normalized: min=1, so 2→0, 3→1 → width 1-0+1=2
Level 2: 4(idx3),5(idx4) from node2(norm 0); 6(idx5) from node3(norm1)
  raw indices before normalize: 4→2*0+1=1, 5→2*0+2=2, 6→2*1+1=3
  min=1 → normalized: 4→0, 5→1, 6→2 → width = 2-0+1 = 3
Max width = 3
```

---

## 27. `ChildrenSumProperty`

**Problem:** GFG classic — Modify tree so every node's value equals the sum of its children's values (a structural property, not a search problem).

**Intuition:** Three-phase per node:
1. Compute sum of existing children.
2. If `childSum >= root.data` → update root's data to childSum (root "absorbs" the larger sum).
   Else → **push root's current value DOWN** to whichever child exists (so children match parent, since parent can't shrink to be smaller than what's expected structurally).
3. **After** recursing into both children (which may have changed based on step 2's push-down), recompute the sum again and update root's data one final time (since children may have grown from their own children sum property being enforced) — this bottom-up correction step ensures consistency propagates back up.

**Test case:** This mutates the tree in place — best verified by printing before/after with a small tree, e.g.:
```
Input:      50
           /  \
          7    2
Output (no change needed, 7+2=9 < 50, so children get overwritten to 50 each,
 then bottom-up fix updates root to 50+50=100... run it and print to see exact behavior!)
```
*(This one's genuinely easier to understand by tracing with a debugger/print statements than reading — good candidate to re-run when you revisit!)*

---

## 28. `markParents` (helper for #29, #30)

**Problem:** Binary trees only have `left`/`right` pointers — no way to go "up" to a parent. This helper builds that missing link.

**Intuition:** Simple BFS over the whole tree, and for every node visited, record `parentMap[child] = current`. This turns the tree into a graph you can traverse in **any direction** — required for both "distance K" and "burning tree" problems below, since both need to move both up AND down from a starting node.

---

## 29. `distanceK`

**Problem:** LeetCode 863 — Find all node values that are exactly distance `k` from a given `target` node (distance can go through parent, not just children).

**Intuition:** Once you have `parentMap` (treating the tree as an undirected graph), this becomes a **plain BFS shortest-path/level problem**: start from target, expand to left child, right child, AND parent, marking visited to avoid going backward. Stop once you've done `k` expansions — whatever's left in the queue at that point is your answer.

**Test case:**
```
distanceK(root, root, 2) → nodes exactly 2 edges from root(1): [4, 5, 6]
(1→2→4, 1→2→5, 1→3→6, all distance 2)
```

---

## 30. `BurningTree`

**Problem:** LeetCode 1376-style — fire starts at `target` node and spreads to adjacent nodes (parent, left, right) every minute. How many minutes until the ENTIRE tree burns?

**Intuition:** Same exact BFS-with-parentMap pattern as `distanceK`, but instead of stopping at fixed `k`, you run until the **queue is empty** and count how many minutes had at least one new node catch fire (`burned` flag per level) — this is literally "multi-source BFS levels = time elapsed," a very common pattern for "spreading/infection" problems (rotting oranges is the same idea).

**Test case:**
```
BurningTree(root, root) with tree above:
Minute 0: {1}
Minute 1: {2, 3} (fire spreads to children of 1)
Minute 2: {4, 5, 6} (fire spreads to children of 2 and 3)
Total time = 2
```

---

## 31. `CountNodeCBT` (+ `heightLeft`, `heightRight`)

**Problem:** LeetCode 222 — Count nodes in a **Complete Binary Tree** faster than O(n) (must beat plain traversal, target O(log²n)).

**Intuition (the clever part):** In a complete binary tree, you can detect a **perfect subtree** cheaply:
- Compute height by always going left (`heightLeft`) and always going right (`heightRight`) from the current node.
- **If they're equal** → this subtree IS perfect (every level completely filled) → node count = `2^height - 1` (closed-form formula, no need to count node by node!).
- **If they differ** → the subtree isn't perfect (has some incomplete last level), so recurse normally into both children and add 1 for the current node.

This exploits the "complete tree" guarantee (only the last level can be partially filled, and it fills left-to-right) — so at any node, AT LEAST one side (usually the "smaller" recursive call) will hit the perfect case quickly, giving O(log²n) instead of O(n).

**Test case:**
```
Tree above:
      1
     / \
    2   3
   / \  /
  4  5 6
At root: heightLeft(1→2→4)=3, heightRight(1→3)=2 → NOT equal, so recurse normally
At node2: heightLeft(2→4)=2, heightRight(2→5)=2 → EQUAL! perfect subtree → count=2^2-1=3 (nodes 2,4,5) ✓ instantly
At node3: heightLeft(3→6)=2, heightRight(3)=1 → not equal → recurse: node3 = 1 + count(6) + count(null) = 1+1+0=2
Total = 1(root) + 3(from node2) + 2(from node3) = 6 ✓ matches actual node count
```

---

## 32. `buildTreePreOrderInorder` (+ `build` helper)

**Problem:** LeetCode 105 — Reconstruct a unique binary tree given its preorder and inorder traversal arrays.

**Intuition:** Two key facts make this work:
1. **Preorder's first element is always the root** (root visited before children).
2. **Inorder splits cleanly around the root** — everything left of root's position in inorder is the left subtree, everything right is the right subtree.

So: grab `preorder[preStart]` as root, find its index in inorder (via a `HashMap<value, index>` built once upfront for O(1) lookup instead of O(n) linear search each time), compute `numsLeft = inRoot - inStart` (size of left subtree), then recursively carve out the correct preorder/inorder index ranges for left and right subtrees. This is a classic **"use one traversal to find the root, use the other to find subtree boundaries"** pattern.

**Why the HashMap matters:** Without it, finding `root.data`'s position in `inorder` every call is O(n), making the whole algorithm O(n²). The map makes each lookup O(1) → overall O(n).

**Test case:**
```
preorder = [1,2,4,5,3,6], inorder = [4,2,5,1,6,3]
→ rebuilds the exact tree from main():
        1
       / \
      2   3
     / \  /
    4  5 6
```

---

## 33. `buildTreePostOrderInorder` (+ `buildPostOrder` helper)

**Problem:** Same reconstruction idea as #32, but from **postorder + inorder** instead of preorder + inorder.

**Intuition:** The only real change: **postorder's LAST element is the root** (root visited after both children in postorder), instead of the first. So you read `postorder[b]` (the end of the current range) as root instead of `preorder[preStart]`. Everything else — using the HashMap to split inorder around the root, then recursing on left/right index ranges — is identical in spirit to #32.

**Common gotcha this code handles correctly:** since postorder is Left-Right-Root, when slicing the postorder array for children you must account for `leftNums` correctly: left subtree occupies `postorder[a .. a+leftNums-1]`, right subtree occupies `postorder[a+leftNums .. b-1]` (root itself is at `b`, excluded from both children's ranges).

**Test case:**
```
postorder = [4,5,2,6,3,1], inorder = [4,2,5,1,6,3]
→ rebuilds the same tree as #32
```

---

## 34. `serialize`

**Problem:** LeetCode 297 — Convert a tree into a single string so it can be stored/transmitted, then rebuilt later.

**Intuition:** Use **level-order (BFS) with explicit null markers** (`"n"`). Unlike a normal BFS that skips null children, here every position gets serialized — real value OR `"n"` — so that when you read the string back, you know EXACTLY which positions had children and which didn't, without needing extra structure to encode gaps.

**Approach:** Standard BFS, but push `node.left` and `node.right` onto the queue even when they're `null` — then when polling, check `if (node != null)` before deciding whether to append its value or `"n"`.

**Test case:**
```
Tree above → serialize(root) = "1 2 3 4 5 n 6 n n n n n n n n "
(root=1, then 2,3, then 4,5 for node2's children, "n" for node3.left... wait 3 has left=6,
 so it's actually: level0:[1] level1:[2,3] level2:[4,5,n,6]... then leaves' children all "n")
```
*(Best verified by literally running `System.out.println(serialize(root))` — the exact spacing/null pattern is easiest to confirm by eye.)*

---

## 35. `deserialize`

**Problem:** Reverse of #34 — rebuild the tree from the serialized string.

**Intuition:** Mirror the BFS logic exactly. Split the string by spaces, first token is root's value. Then walk the array two-at-a-time (`arr[i]` = left child, `arr[++i]` = right child) for each node popped from the queue — pushing new tree nodes onto the queue only when the token isn't `"n"`. Because serialize() wrote every position (real or "n") in strict BFS order, deserialize() can consume the array in that exact same order and rebuild the identical structure.

**Test case:**
```
deserialize(serialize(root)) → produces a tree structurally identical to the original root
(this "round-trip" property is the standard way to test serialize/deserialize pairs)
```

---

## 36. `morrisInorder` — ⭐ (see the dedicated interactive dry-run artifact for full step-by-step)

**Problem:** Inorder traversal using **O(1) extra space** — no recursion (no call stack) and no explicit `Stack<Node>`.

**Intuition (the big idea):** Normally, once you go left, you need SOME way to get back up to the parent afterward — that's usually the call stack's job. Morris traversal instead **temporarily borrows the tree's own null right-pointers** to store a "way back" — this is called **threading**.

For any node with a left child:
1. Find its **inorder predecessor** (the rightmost node in its left subtree — the last node you'd visit before this one).
2. If that predecessor's `.right` is `null` → it's unused, so **plant a thread**: point it back to the current node, then dive left.
3. Later, when you naturally arrive back at that predecessor via the thread, you **detect** it (`pre.right == curr` instead of `null`) → that's your signal "I've already explored my left side." You **remove the thread** (restoring the tree exactly as it was — this algorithm cleans up after itself), visit the current node, and move right.

If a node has **no left child**, there's nothing to thread — just visit it immediately and move right (this is the simple base case, same as the "leftmost fallback" idea in iterative inorder).

**Why this matters for you specifically:** this is the "cursor movement" problem — `curr` bounces between levels in a way that looks like it's revisiting nodes, but it's not random: it's following threads it planted itself. **See the separate interactive artifact for a full 9-iteration dry run with diagrams** — reading code alone for this one is genuinely hard; watching `curr` and `pre` move is what makes it click.

**Test case:**
```
Tree above → morrisInorder prints: 4 2 5 1 6 3  (matches standard inorder exactly)
Tree is left completely unchanged after the traversal finishes (all threads removed).
```

---

## 37. `FlattenTree` (recursive, rewires in place)

**Problem:** LeetCode 114 — Flatten a binary tree into a **linked list following preorder** (using the `right` pointers only, `left` always `null`), in place.

**Intuition:** Recurse first (`FlattenTree(root.left)`, `FlattenTree(root.right)`) so that BY THE TIME you handle the current node, both subtrees are ALREADY flattened lists. Then it's just pointer surgery:
1. Save `root.right` (the already-flattened right list) aside.
2. Walk to the END of the already-flattened left list (`while temp.right != null`).
3. Attach the saved right list to that end.
4. Move the whole (now-combined) left list into `root.right`, and null out `root.left`.

This is a classic **"solve subproblems first, then splice results together"** divide-and-conquer pattern — same shape as merge step in merge sort.

**Test case:**
```
Input:        Output (as a right-only chain):
    1          1 → 2 → 4 → 5 → 3 → 6  (this IS preorder order!)
   / \
  2   3
 / \  /
4  5 6
```

---

## 38. `FlattenTreeReversePreOrder`

**Problem:** Same flattening goal as #37, but a cleverer approach: build the list **backwards**.

**Intuition:** If you traverse in **reverse preorder** (Right → Left → Root, i.e. the mirror image of preorder), and at each node just set `root.right = prev` (the previously processed node) and `root.left = null`, then update `prev = root` for the parent call — you build the correct flattened chain **automatically**, because reverse-preorder visits nodes in the EXACT REVERSE of the order you want them chained. This avoids the "walk to the end of the left list" step in #37 entirely — O(1) extra work per node instead of walking a growing chain each time (making this O(n) total vs #37's O(n²) worst case on skewed trees).

**Test case:**
```
Same tree → same output chain: 1 → 2 → 4 → 5 → 3 → 6
```

---

## 39. `FlattenTreeIterative`

**Problem:** Flatten without recursion, using an explicit stack.

**Intuition:** This is essentially **iterative preorder** (push right then left so left pops first — same trick as method #3), but instead of collecting values into a result list, you rewire pointers as you go: after popping `curr`, whatever is next on top of the stack (`st.peek()`) IS the next node in preorder — so `curr.right = st.peek()` directly builds the chain, and `curr.left = null` cleans up.

**Test case:**
```
Same tree → same output chain: 1 → 2 → 4 → 5 → 3 → 6
```

---

## 40. `FlattenTreeMorries` (Morris-style flattening — O(1) space!)

**Problem:** Flatten the tree with **zero extra space** — no stack, no recursion.

**Intuition:** Reuses the exact threading idea from `morrisInorder` (#36), but applied differently: for every node with a left child, find the **rightmost node of the left subtree** (`pre`), then attach the current node's existing right subtree onto the end of that (`pre.right = curr.right`), then **move the entire left subtree into the right slot** (`curr.right = curr.left; curr.left = null`), and move on (`curr = curr.right`). No threads even need to be "removed" here — this variant permanently rewires the tree rather than temporarily borrowing pointers, since flattening is a permanent restructuring anyway (unlike traversal, which must leave the tree unchanged).

**Test case:**
```
Same tree → same output chain: 1 → 2 → 4 → 5 → 3 → 6, achieved in true O(1) extra space
```

---

## 41. `SearchBinarySearchTree` (recursive)

**Problem:** LeetCode 700 — Search for a value in a **BST** (not just any binary tree).

**Intuition:** This is where BSTs pay off: because of the BST property (left subtree < node < right subtree), you don't need to check both children like in a normal tree search — you can **discard half the tree at every step**, exactly like binary search on a sorted array. `root.data > val` → search only left; otherwise → search only right.

**Test case:**
```
On a BST (not the sample tree above, which isn't a valid BST!) e.g.:
        8
       / \
      3   10
     / \
    1   6
SearchBinarySearchTree(root, 6) → returns the node with data=6
SearchBinarySearchTree(root, 99) → returns null
```

---

## 42. `SearchBSTiterative`

**Problem:** Same as #41, iterative version (no recursion overhead).

**Intuition:** Direct translation of the recursive logic into a `while` loop — same "eliminate half the tree" logic, just without call-stack frames. This is the version you'd actually want in an interview follow-up ("can you do it without recursion / in O(1) space?").

**Test case:** Same as #41, iterative — identical results, less memory.

---

## 43. `CeilBST` (iterative)

**Problem:** GFG/LeetCode-style — Find the **ceil** of a value in a BST: the smallest value in the tree that is `>= target` (or `-1` if none exists).

**Intuition:** Walk down like a normal BST search, but **remember your best candidate so far**: every time you move into a node whose value `>= target`, that node is a POSSIBLE answer (`ceil = root.data`) — but there might be an even smaller valid one further left, so keep going left. Every time a node's value `< target`, it can never be the ceil (too small), so go right without recording it.

**Test case:**
```
BST:        8
           / \
          3   10
         / \
        1   6
CeilBST(root, 10) → 10 (exact match)
CeilBST(root, 7)  → 8  (6 is too small, 8 is the smallest value ≥ 7)
CeilBST(root, 11) → -1 (nothing in the tree is ≥ 11)
```

---

## 44. `CeilBSTrecursive` (two versions in your code — worth noting the difference!)

**Problem:** Same ceil logic as #43, but recursive. You actually wrote **two versions** — a nice comparison of two recursion styles:

**Version A — `static int CeilBSTrecursive(int target, Node root)`:**
Returns the answer directly through the return value. Recurses left when `root.data >= target` (this node is a candidate, but check if left has an even better one via `leftans`); if left subtree returns `-1` (no better candidate found there), falls back to `root.data`. Recurses right (unconditionally returning that result) when `root.data < target`. Fully self-contained — no shared state needed.

**Version B — instance method using `this.ceil` as a field:**
Instead of returning the answer through the call stack, it **mutates a shared instance variable** `ceil` every time a valid candidate is found, and the recursive calls just return that same field at the end. This works because there's only ever ONE path being explored (BST search doesn't branch), so there's no risk of concurrent overwrites — but it's a different style: **side-effect-based** vs **pure return-value-based**. Generally, the pure version (A) is considered cleaner/safer since it doesn't depend on object state, but the field-based version (B) can be handy when you need to track "best answer so far" across a more complex traversal.

**Test case:** Same as #43 — both versions should return identical answers, e.g. `CeilBSTrecursive(10, root)` → 10, using whichever version you call.

---

## 🎯 Quick Reference Table

| # | Method | Pattern | LeetCode Equivalent |
|---|--------|---------|---------------------|
| 1 | pre/in/post order | Basic recursion | 144/94/145 |
| 2 | levelWiseTraversal | BFS | 102 |
| 3-6 | Iterative traversals | Explicit stack | 144/94/145 (iterative) |
| 7 | maxDepth | Bottom-up recursion | 104 |
| 8 | minDepth | Bottom-up recursion (careful w/ single child) | 111 |
| 9 | countNodes | Bottom-up recursion | 222 (naive) |
| 10 | invertTree | Recursion + swap | 226 |
| 11 | hasPathSum | Top-down recursion | 112 |
| 12 | diameterOfTree | Bottom-up + global var | 543 |
| 13 | isBalanced | Bottom-up + sentinel -1 | 110 |
| 14 | BinaryTreePath | Backtracking (string) | 257 |
| 15 | maxPathSum | Tree DP (gain vs contribution) | 124 |
| 16 | zigzagLevelOrder | BFS + flag | 103 |
| 17 | boundaryTraversal | Decomposed traversal | GFG |
| 18 | verticalTraversal | BFS + coordinates | 987 |
| 19 | topView | BFS + first-wins | GFG |
| 20 | bottomView | BFS + last-wins | GFG |
| 21 | rightView | DFS right-first | 199 |
| 22 | leftView | DFS left-first | GFG |
| 23 | isSymmetric | Cross-comparison recursion | 101 |
| 24 | rootToNodePath | Backtracking (list) | GFG |
| 25 | lowestCommonAncestors | Bottom-up recursion | 236 |
| 26 | MaximumWidth | BFS + index normalization | 662 |
| 27 | ChildrenSumProperty | Push-down + pull-up | GFG |
| 28 | markParents | BFS graph-building | (helper) |
| 29 | distanceK | BFS on parent-augmented graph | 863 |
| 30 | BurningTree | Multi-source BFS (levels=time) | 1376-style |
| 31 | CountNodeCBT | Height-comparison + formula | 222 (optimal) |
| 32 | buildTree (pre+in) | Root-from-preorder, split-by-inorder | 105 |
| 33 | buildTree (post+in) | Root-from-postorder (last elem), split-by-inorder | 106 |
| 34 | serialize | BFS with null markers | 297 |
| 35 | deserialize | BFS rebuild from markers | 297 |
| 36 | morrisInorder | Threading (temporary right-pointers) | 94 (O(1) space) |
| 37 | FlattenTree | Post-order splice (bottom-up) | 114 |
| 38 | FlattenTreeReversePreOrder | Reverse-preorder + `prev` pointer | 114 (optimal) |
| 39 | FlattenTreeIterative | Explicit stack, rewire on the fly | 114 (iterative) |
| 40 | FlattenTreeMorries | Threading, permanent rewire | 114 (O(1) space) |
| 41 | SearchBinarySearchTree | BST property, halve search space | 700 |
| 42 | SearchBSTiterative | Same, no recursion | 700 (iterative) |
| 43 | CeilBST | BST walk + "best candidate so far" | GFG |
| 44 | CeilBSTrecursive (x2) | Return-value vs field-based recursion | GFG |

---

### 💡 How to use this file
Whenever you forget why a method looks the way it does, search this file for the method name — you'll get the problem, the "aha" trick, and a test case to mentally re-run the code against.