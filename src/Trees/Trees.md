# 🌳 Trees DSA Revision Handbook

> Reverse-engineered from your `Tree.java` progression. This is built so that if you disappear from DSA for 2 weeks and come back, you can open this file, find the method, and reconstruct not just *what* the code does but *why you wrote it that way* — in under a minute per problem.

**How each entry is structured:** What the problem actually asks → an example tree → your original code untouched → bugs/edge-cases called out separately → the intuition and why you picked recursion/queue/stack/map → a line-by-line walkthrough → a full dry run showing the data structure's state at each step → test cases → and, *only in a clearly separate section*, a better/alternate approach if one exists. Your code is never silently rewritten inline.

**Note on interactivity:** Markdown files are static — no JS runs inside a `.md` viewer (GitHub, Obsidian, VS Code preview all just render text). For the handful of problems where *watching pointers move* is genuinely more valuable than reading about it (Morris traversal being the prime example), the companion is a separate interactive `.html` artifact. This handbook tells you which ones deserve that treatment.

---

## 📖 At a Glance

| | |
|---|---|
| **Total methods covered** | 60, across 8 categories (traversals → basic recursion → BFS/views → ancestors → graph-augmented → construction/serialization → Morris/flatten → BST operations) |
| **Known real bugs, flagged inline** | `addAtTail`-style crashes aside (that's the LinkedList handbook) — here: Problem 41's `hasPathSum` null-root crash, Problem 48's empty-tree insert, Problem 56's `inorderPredecessor` wrong-answer bug, Problem 57's `BSTIterator` double bug, Problem 58's `twoSumFindTarget` (always returns `false` as a result of #57) |
| **Where `main()` currently calls the wrong tree** | Problem 53 (`LCAbst`) — your `main()`'s only active line runs a BST-only method against the original non-BST tree; traced in detail there |
| **Best entry points if you're short on time** | Problem 36 (Morris Inorder — has a companion interactive artifact), Problem 52 vs 51 (two techniques, same problem), Problem 57→58 (a real bug chain worth understanding) |
| **How to navigate** | Full index below. Every `⚠️` marker = a verified bug, documented with a dry-run proof and a fix. Every `⭐` = worth comparing directly against a sibling problem. |

---

## 📇 Progression Index

*(In the order you actually wrote them in the file — helper methods like `markParents`, `build`, `buildPostOrder`, `heightLeft/Right`, `isLeaf` are folded into the problem they support rather than listed separately.)*

**Traversals — the foundation**
1. Recursive Preorder / Inorder / Postorder
2. Level Order Traversal (BFS)
3. Iterative Preorder
4. Iterative Inorder
5. Iterative Postorder (Two-Stack)
6. Iterative Postorder (Single-Stack, Optimized)

**Basic recursive properties**
7. Maximum Depth
8. Minimum Depth
9. Count Nodes
10. Same Tree
11. Invert Binary Tree
12. Path Sum (root-to-leaf)
13. Diameter of Tree
14. Balanced Binary Tree
15. All Root-to-Leaf Paths
16. Maximum Path Sum (any node to any node)

**BFS variants & views**
17. Zigzag Level Order
18. Boundary Traversal (Anti-Clockwise)
19. Vertical Order Traversal
20. Top View
21. Bottom View
22. Right View
23. Left View

**Structural / ancestor problems**
24. Symmetric Tree
25. Root-to-Node Path
26. Lowest Common Ancestor (LCA)
27. Maximum Width of Binary Tree
28. Children Sum Property

**Graph-augmented tree problems (parent pointers)**
29. Distance K from Target Node
30. Burning Tree (Time to Burn Entire Tree)

**Complete binary tree optimization**
31. Count Nodes in a Complete Binary Tree (O(log²n))

**Tree construction & serialization**
32. Build Tree from Preorder + Inorder
33. Build Tree from Postorder + Inorder
34. Serialize a Binary Tree
35. Deserialize a Binary Tree

**Morris Traversal & Flattening (O(1) space)**
36. Morris Inorder Traversal ⭐ *(see interactive artifact)*
37. Flatten Binary Tree (Recursive, bottom-up splice)
38. Flatten Binary Tree (Reverse Preorder, O(n))
39. Flatten Binary Tree (Iterative, Stack)
40. Flatten Binary Tree (Morris-style, O(1) space)

**Binary Search Tree operations**
41. Search in a BST (Recursive)
42. Search in a BST (Iterative)
43. Ceil in a BST (Iterative)
44. Ceil in a BST (Recursive — Version A: pure return value)
45. Ceil in a BST (Recursive — Version B: instance field / side-effect)

**Binary Search Tree — Structural & Query Operations**
46. Floor in a BST (Iterative)
47. Floor in a BST (Recursive)
48. Insert into a BST (Iterative)
49. Delete a Node from a BST (Merge Strategy)
50. Kth Smallest Element in a BST
51. Validate BST (Range-Bounding, Top-Down)
52. Validate BST (Min/Max Propagation, Bottom-Up) ⭐ *(compare directly against #51)*
53. Lowest Common Ancestor in a BST (Optimized) ⚠️ *(see note — your `main()` currently calls this on the wrong tree)*

**Advanced BST Techniques**
54. Construct BST from Preorder Traversal
55. Inorder Successor in a BST
56. Inorder Predecessor in a BST ⚠️ *(real bug — wrong answer when target value exists in the tree)*
57. BST Iterator (Controlled Ascending / Descending Traversal) ⚠️ *(two bugs — `hasNext()` inverted, `isReverse` flag semantics inverted)*
58. Two Sum IV — Input is a BST ⚠️ *(currently always returns `false` — direct consequence of #57)*
59. Recover Binary Search Tree
60. Largest BST Subtree

---

## 🌲 Shared Example Trees

Used consistently throughout unless a problem specifically needs a BST..

**General tree** (from your `main()` — used for all non-BST problems):
```text
        1
       / \
      2   3
     / \  /
    4  5 6
```
PreOrder: `1 2 4 5 3 6` · InOrder: `4 2 5 1 6 3` · PostOrder: `4 5 2 6 3 1` · Level order: `[[1],[2,3],[4,5,6]]`

**BST example** (used for Search/Ceil problems, since your `main()` tree is *not* a valid BST):
```text
          8
        /   \
       3     10
      / \      \
     1   6      14
```
InOrder (sorted): `1 3 6 8 10 14`

**New helper class used by Problems 51–52:**
```java
class Info {
    int min;
    int max;
    boolean isBST;
    Info(int min, int max, boolean isBST) {
        this.min = min; this.max = max; this.isBST = isBST;
    }
}
```
Bundles a subtree's minimum value, maximum value, and whether it's a valid BST, all in one return value — this is what lets `checkBST` (Problem 52) validate the whole tree in a single bottom-up pass instead of needing a second top-down range check like `validBST` (Problem 51) does.

**Two more helper classes used by Problems 57–60:**
```java
class BSTIterator {
    private Stack<Node> st = new Stack<>();
    boolean isReverse;
    // ... see Problem 57 — has two real bugs, both documented and fixed there
}
class LargestBST {
    int size;
    int min;
    int max;
    LargestBST(int size, int max, int min) { ... }  // note: constructor order is (size, MAX, min)
}
```

**⚠️ A note before you get to Problem 53:** your `main()` currently has `LCAbst(root, new Node(2), new Node(3));` as its only active (uncommented) call — and `root` there is still the *original* tree from the top of `main()` (`1,2,3,4,5,6`), which is **not a valid BST**. `LCAbst` specifically relies on BST ordering to decide which way to recurse, so running it against a non-BST tree doesn't error out — it just silently returns the wrong node. Problem 53 traces through exactly what happens and why, so you can see the wrong answer it currently produces versus the correct one it gives on an actual BST.

---

# Problem 1 — Recursive Preorder / Inorder / Postorder

## 1. What is the problem?
Visit every node in the tree exactly once, in a defined order. This isn't really "a problem" in the algorithmic sense — it's the *base primitive* every other tree method in this file builds on. Once you're fluent in "do something before/between/after recursing into children," you can solve most of the rest of this list.

## 2. Example Tree
```text
        1
       / \
      2   3
     / \  /
    4  5 6
```

## 3. My Code
```java
private static void inOrder(Node root) {
    if (root == null) { return; }
    inOrder(root.left);
    System.out.println(root.data);
    inOrder(root.right);
}

private static void postOrder(Node root) {
    if (root == null) { return; }
    postOrder(root.left);
    postOrder(root.right);
    System.out.println(root.data);
}

private static void preOrder(Node root) {
    if (root == null) { return; }
    System.out.println(root.data);
    preOrder(root.left);
    preOrder(root.right);
}
```

## 4. Issues / Bugs / Edge Cases
- None. `if (root == null) return;` correctly handles the empty-tree base case for all three.
- Minor style note (not a bug): these `System.out.println` directly instead of returning a `List<Integer>` — fine for practice/debugging, but every LeetCode version of this problem wants a `List<Integer>` returned. Worth having a `List`-returning variant memorized too since that's what interviewers actually ask for.

## 5. Intuition & Why This Approach
Recursion mirrors the tree's own recursive definition ("a tree is a root plus two smaller trees"), so the code shape matches the data shape exactly — no bookkeeping needed. The *only* thing that changes between all three orders is **where you put the "visit" line relative to the two recursive calls**:
- Print before both calls → **Pre**order (Root-Left-Right)
- Print between the calls → **In**order (Left-Root-Right)
- Print after both calls → **Post**order (Left-Right-Root)

Why recursion and not a loop? Because a tree doesn't have a fixed number of "next" pointers like a list — it branches. The call stack does the bookkeeping of "which subtree am I in, and what do I do when I return from it" for free.

## 6. Line-by-Line Walkthrough (inOrder, as the representative example)
| Line | What happens |
|---|---|
| `if (root == null) return;` | Base case — an empty subtree contributes nothing and stops the recursion from going further |
| `inOrder(root.left);` | Fully finish the entire left subtree before touching the current node |
| `System.out.println(root.data);` | Only now, after left is 100% done, visit the current node |
| `inOrder(root.right);` | Then fully process the right subtree |

## 7. Dry Run — `inOrder(root)` call stack
```text
inOrder(1)
 └─ inOrder(2)
     └─ inOrder(4)
         └─ inOrder(null) → return (base case)
         → print 4
         └─ inOrder(null) → return
     → print 2
     └─ inOrder(5)
         └─ inOrder(null) → return
         → print 5
         └─ inOrder(null) → return
 → print 1
 └─ inOrder(3)
     └─ inOrder(6)
         └─ inOrder(null) → return
         → print 6
         └─ inOrder(null) → return
     → print 3
     └─ inOrder(null) → return
```
Output order: `4 2 5 1 6 3` — matches expected inorder.

## 8. Test Cases
| Input | preOrder | inOrder | postOrder |
|---|---|---|---|
| Tree above | `1 2 4 5 3 6` | `4 2 5 1 6 3` | `4 5 2 6 3 1` |
| Empty tree (`root = null`) | *(nothing printed)* | *(nothing printed)* | *(nothing printed)* |
| Single node `[5]` | `5` | `5` | `5` |
| Left-skewed `1←2←3` (all left children) | `1 2 3` | `3 2 1` | `3 2 1` |

## Better / Alternative Approach
If an interviewer specifically wants a `List<Integer>` returned instead of printed (very common), pass an accumulator list through the recursion:
```java
private static void inOrder(Node root, List<Integer> result) {
    if (root == null) return;
    inOrder(root.left, result);
    result.add(root.data);
    inOrder(root.right, result);
}
```
Same shape, same intuition — just collecting instead of printing. Worth having this version memorized since it's what you'll actually submit on LeetCode.

---

# Problem 2 — Level Order Traversal (BFS)

## 1. What is the problem?
Print (or return) the tree level-by-level, top to bottom, left to right within each level. LeetCode 102.

## 2. Example Tree
Same tree as above.

## 3. My Code
```java
private static List<List<Integer>> levelWiseTraversal(Node root) {
    List<List<Integer>> res = new ArrayList<>();
    if (root == null) { return res; }
    Queue<Node> q = new LinkedList<>();
    q.offer(root);
    while (!q.isEmpty()) {
        int levelSize = q.size();
        List<Integer> level = new ArrayList<>();
        for (int i = 0; i < levelSize; i++) {
            Node node = q.poll();
            level.add(node.data);
            if (node.left != null) { q.offer(node.left); }
            if (node.right != null) { q.offer(node.right); }
        }
        res.add(level);
    }
    return res;
}
```

## 4. Issues / Bugs / Edge Cases
- None. Handles `root == null` correctly (returns empty list, not `null` — good, avoids `NullPointerException` for the caller).

## 5. Intuition & Why a Queue
Recursion naturally dives *deep* first (that's what problem 1 showed). Level order needs to go *wide* first — process everything at depth 0, then everything at depth 1, etc. A **Queue (FIFO)** is exactly the right structure for "process things in the order they were discovered" — which is precisely what breadth-first means.

**The key trick:** capture `q.size()` into `levelSize` *before* the inner loop starts. This freezes "how many nodes belong to the current level" at the moment the level began. If you didn't do this and just looped `while(!q.isEmpty())` with no size snapshot, children you just pushed would get mixed into the same level's processing — you'd lose the level boundaries entirely.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `q.offer(root)` | Seed the queue with just the root — level 0 |
| `int levelSize = q.size();` | **Critical line.** Snapshot: "this many nodes exist at the current depth" |
| `for (i = 0; i < levelSize; i++)` | Process exactly that many — no more, no less, even though the queue's size changes as we push children mid-loop |
| `node.left/right != null → q.offer(...)` | Children get queued for the *next* level, not this one |
| `res.add(level)` | Once the loop for this level finishes, the level list is complete and gets appended |

## 7. Dry Run — Queue state at each iteration
| Step | Queue before | levelSize | Nodes processed | Queue after | `res` so far |
|---|---|---|---|---|---|
| 1 | `[1]` | 1 | 1 → push 2,3 | `[2,3]` | `[[1]]` |
| 2 | `[2,3]` | 2 | 2 → push 4,5; 3 → push 6 | `[4,5,6]` | `[[1],[2,3]]` |
| 3 | `[4,5,6]` | 3 | 4,5,6 → no children to push | `[]` | `[[1],[2,3],[4,5,6]]` |
| 4 | `[]` | — | loop ends (`q.isEmpty()`) | — | final answer |

## 8. Test Cases
| Input | Output |
|---|---|
| Tree above | `[[1],[2,3],[4,5,6]]` |
| Empty tree | `[]` |
| Single node `[7]` | `[[7]]` |
| Right-skewed `1→2→3` | `[[1],[2],[3]]` (each level has exactly 1 node) |

## Better / Alternative Approach
Your approach is already the standard, optimal O(n) solution — nothing to improve algorithmically. The only variant worth knowing: some problems (like zigzag, problem 17) reuse this *exact* skeleton and just post-process each `level` list differently, so this pattern is worth memorizing as a template, not just a one-off solution.

---

# Problem 3 — Iterative Preorder

## 1. What is the problem?
Do preorder traversal (Root-Left-Right) *without* recursion — interviewers ask this to check you understand that recursion is really just the call stack doing bookkeeping for you, and that you can take that bookkeeping over yourself.

## 2. Example Tree
Same tree as Problem 1.

## 3. My Code
```java
private static void iterativePreOrder(Node root) {
    Stack<Node> st = new Stack<>();
    st.push(root);
    while (!st.isEmpty()) {
        Node curr = st.pop();
        System.out.print(curr.data);
        if (curr.right != null) { st.push(curr.right); }
        if (curr.left != null) { st.push(curr.left); }
    }
}
```

## 4. Issues / Bugs / Edge Cases
- **Edge case not handled:** if `root == null`, `st.push(root)` pushes a null onto the stack, and the very first `st.pop()` gives you `null`, causing a `NullPointerException` on `curr.data`. Worth adding `if (root == null) return;` at the top — your recursive version handles this correctly but this iterative one doesn't.
- Otherwise correct.

## 5. Intuition & Why a Stack
The call stack in recursion is LIFO (last function call in, first to return) — so the direct iterative translation uses an explicit `Stack<Node>`.
**The trick that makes order come out right:** since a stack pops the *most recently pushed* item first, and we want **left processed before right**, we must push **right first, then left** — that way left sits on top and pops out first.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `st.push(root)` | Seed the stack |
| `Node curr = st.pop();` | Take the top of the stack — this is the node we visit *now* |
| `System.out.print(curr.data);` | Visit happens immediately on pop — this is what makes it preorder (root visited before children are even looked at) |
| `if (curr.right != null) st.push(curr.right);` | Push right **first**... |
| `if (curr.left != null) st.push(curr.left);` | ...then push left, so left ends up on top and pops next |

## 7. Dry Run — Stack state at each iteration
| Pop | Print | Stack before push | Push right? | Push left? | Stack after |
|---|---|---|---|---|---|
| — | — | `[1]` | — | — | `[1]` (initial) |
| 1 | `1` | `[]` | push 3 | push 2 | `[3,2]` |
| 2 | `2` | `[3]` | push 5 | push 4 | `[3,5,4]` |
| 4 | `4` | `[3,5]` | none | none | `[3,5]` |
| 5 | `5` | `[3]` | none | none | `[3]` |
| 3 | `3` | `[]` | push 6 | none | `[6]` |
| 6 | `6` | `[]` | none | none | `[]` → loop ends |

Output: `1 2 4 5 3 6` — matches recursive preorder exactly.

## 8. Test Cases
- Tree above → `1 2 4 5 3 6`
- Single node `[9]` → `9`
- `root == null` → **currently crashes** (see bug above) — recursive version would silently print nothing

## Better / Alternative Approach
Add the null guard:
```java
private static void iterativePreOrder(Node root) {
    if (root == null) return;
    ...
}
```
That's the only fix needed — the core algorithm is already the standard, optimal O(n) time / O(h) space (h = height) solution.

---

# Problem 4 — Iterative Inorder

## 1. What is the problem?
Inorder traversal (Left-Root-Right) without recursion.

## 2. Example Tree
Same tree.

## 3. My Code
```java
private static List<Integer> iterativeInOrder(Node root) {
    List<Integer> al = new ArrayList<>();
    Stack<Node> st = new Stack<>();
    Node node = root;
    while (true) {
        if (node != null) {
            st.push(node);
            node = node.left;
        } else {
            if (st.isEmpty()) { break; }
            node = st.pop();
            al.add(node.data);
            node = node.right;
        }
    }
    return al;
}
```

## 4. Issues / Bugs / Edge Cases
- `root == null`: works correctly — first check `node != null` is false immediately, then `st.isEmpty()` is true, breaks, returns empty list. No crash. ✅
- `while(true)` with a manual `break` is a slightly unusual style (most people write `while (node != null || !st.isEmpty())`), but functionally identical and completely correct.

## 5. Intuition & Why a Stack (and why it's harder than preorder)
Unlike preorder, you can't just print on pop — inorder needs left fully explored *first*. So the pattern has two phases that alternate:
1. **Go as far left as possible**, pushing every node you pass (you haven't visited them yet, just noted "come back to this later").
2. When you truly can't go left anymore (`node == null`), that means the top of the stack is the next node to *visit* — pop it, record it, then move to its **right** child and repeat phase 1 from there.

This exactly mirrors what recursion does automatically: "go left, then visit, then go right" — you're just managing the "go back and visit" step manually with the stack instead of letting the call stack unwind for you.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `if (node != null)` | Phase 1: still have somewhere left to go |
| `st.push(node); node = node.left;` | Remember this node, then dive further left |
| `else` branch | Phase 2: hit a null — time to process what's on top of the stack |
| `if (st.isEmpty()) break;` | Nothing left to process anywhere → done |
| `node = st.pop(); al.add(node.data);` | This is the actual "visit" — happens only once we can't go left anymore |
| `node = node.right;` | After visiting, explore the right subtree next (loop restarts phase 1 from here) |

## 7. Dry Run — Stack & node state
| node | Action | Stack after | Output so far |
|---|---|---|---|
| 1 | push 1, go left | `[1]` | `[]` |
| 2 | push 2, go left | `[1,2]` | `[]` |
| 4 | push 4, go left | `[1,2,4]` | `[]` |
| null | pop 4, visit, go right (null) | `[1,2]` | `[4]` |
| null | pop 2, visit, go right (5) | `[1]` | `[4,2]` |
| 5 | push 5, go left | `[1,5]` | `[4,2]` |
| null | pop 5, visit, go right (null) | `[1]` | `[4,2,5]` |
| null | pop 1, visit, go right (3) | `[]` | `[4,2,5,1]` |
| 3 | push 3, go left | `[3]` | `[4,2,5,1]` |
| 6 | push 6, go left | `[3,6]` | `[4,2,5,1]` |
| null | pop 6, visit, go right (null) | `[3]` | `[4,2,5,1,6]` |
| null | pop 3, visit, go right (null) | `[]` | `[4,2,5,1,6,3]` |
| null, stack empty | break | — | `[4,2,5,1,6,3]` |

Final: `4 2 5 1 6 3` ✅ matches recursive inorder.

## 8. Test Cases
- Tree above → `[4,2,5,1,6,3]`
- `root == null` → `[]` (handled correctly, no crash)
- Single node `[9]` → `[9]`
- Left-skewed tree `1←2←3` → `[3,2,1]`

## Better / Alternative Approach
Already the standard O(n) time / O(h) space solution — this is the textbook correct iterative inorder pattern. Nothing to improve except perhaps swapping `while(true) + break` for `while(node != null || !st.isEmpty())` purely for readability — behaviorally identical.

---

# Problem 5 — Iterative Postorder (Two-Stack Method)

## 1. What is the problem?
Postorder traversal (Left-Right-Root) without recursion — the hardest of the three orders to do iteratively, because a stack naturally gives you root-first orders, and postorder wants root *last*.

## 2. Example Tree
Same tree.

## 3. My Code
```java
private static List<Integer> iterativePostOrder(Node root) {
    List<Integer> al = new ArrayList<Integer>();
    if (root == null) { return al; }
    Stack<Node> st1 = new Stack<>();
    Stack<Node> st2 = new Stack<>();
    st1.push(root);
    Node x = root;
    while (!st1.isEmpty()) {
        x = st1.pop();
        st2.push(x);
        if (x.left != null) { st1.push(x.left); }
        if (x.right != null) { st1.push(x.right); }
    }
    while (!st2.isEmpty()) {
        Node k = st2.pop();
        al.add(k.data);
    }
    return al;
}
```

## 4. Issues / Bugs / Edge Cases
- `root == null` handled explicitly and correctly at the top.
- `Node x = root;` right before the loop is dead/unnecessary — `x` gets overwritten on the very first `x = st1.pop();` before it's ever read. Not a bug, just a harmless leftover line.
- Uses O(n) extra space (two stacks) — correct but not the most space-efficient version (see Problem 6).

## 5. Intuition & Why Two Stacks
Postorder = Left-Right-**Root**. The trick: **postorder is the exact reverse of "Root-Right-Left."**
So the algorithm does a *modified preorder* — but pushes **left before right** (so right pops first) — which produces nodes in Root→Right→Left order into `st2`. Then, popping everything out of `st2` at the end **reverses** that entire sequence: reverse of (Root-Right-Left) = **Left-Right-Root** = postorder. `st1` is just the engine generating the reversed order; `st2` is the "undo the reversal" collector.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `st1.push(root)` | Start the modified-preorder pass |
| `x = st1.pop(); st2.push(x);` | Every node popped from st1 immediately gets pushed to st2 (building the reversed order) |
| `if (x.left != null) st1.push(x.left);` | Push **left before right** on purpose — this is the reverse of normal preorder's push order |
| `if (x.right != null) st1.push(x.right);` | ...so that right pops out of st1 *before* left does, giving Root-Right-Left overall |
| second `while` loop | Pop everything from st2 — this reverses Root-Right-Left into Left-Right-Root |

## 7. Dry Run
**Phase 1 — building st2:**
| Pop from st1 | Push to st2 | Push left? | Push right? | st1 after |
|---|---|---|---|---|
| 1 | st2=`[1]` | push 2 | push 3 | `[2,3]` |
| 3 | st2=`[1,3]` | push 6 | none | `[2,6]` |
| 6 | st2=`[1,3,6]` | none | none | `[2]` |
| 2 | st2=`[1,3,6,2]` | push 4 | push 5 | `[4,5]` |
| 5 | st2=`[1,3,6,2,5]` | none | none | `[4]` |
| 4 | st2=`[1,3,6,2,5,4]` | none | none | `[]` → phase 1 ends |

**Phase 2 — pop everything from st2 (reverses it):** `4, 5, 2, 6, 3, 1`

Output: `4 5 2 6 3 1` ✅ matches recursive postorder.

## 8. Test Cases
- Tree above → `[4,5,2,6,3,1]`
- `root == null` → `[]`
- Single node `[9]` → `[9]`

## Better / Alternative Approach
See Problem 6 — the single-stack version achieves the same result with half the extra space, and is the version worth having ready if an interviewer asks "can you do it with less memory?"

---

# Problem 6 — Iterative Postorder (Single-Stack, Optimized)

## 1. What is the problem?
Same as Problem 5, but the memory-optimized version — the natural interviewer follow-up to "can you do it with just one stack?"

## 2. Example Tree
Same tree.

## 3. My Code
```java
public static List<Integer> iterativePostOrderSingleStack(Node root) {
    List<Integer> result = new ArrayList<>();
    if (root == null) return result;
    Stack<Node> stack = new Stack<>();
    Node curr = root;
    Node lastVisited = null;
    while (curr != null || !stack.isEmpty()) {
        if (curr != null) {
            stack.push(curr);
            curr = curr.left;
        } else {
            Node rightChild = stack.peek().right;
            if (rightChild == null || rightChild == lastVisited) {
                lastVisited = stack.pop();
                result.add(lastVisited.data);
            } else {
                curr = rightChild;
            }
        }
    }
    return result;
}
```

## 4. Issues / Bugs / Edge Cases
- None found. `root == null` handled. The `lastVisited` tracking correctly distinguishes "haven't explored right subtree yet" from "already came back from it."

## 5. Intuition & Why `lastVisited` Is Necessary
Track a `lastVisited` pointer — the single most recently *fully processed* node.
- Go as far left as possible, pushing along the way (same phase-1 idea as iterative inorder).
- Once you can't go left, look at the **top of stack's right child**:
  - If it's `null` OR it's already been visited (`== lastVisited`) → you're allowed to pop and process the top node *now*.
  - Otherwise → you haven't explored that right subtree yet → go there first (`curr = rightChild`).

**Why you can't just check `rightChild == null` alone (like inorder does):** in postorder, a node with a right child must have that ENTIRE right subtree finished before the node itself can be visited. Without `lastVisited`, you'd have no way to tell "have I already come back from the right subtree" apart from "I haven't gone there yet" — both look like "top of stack has a non-null right child" unless you remember what you last finished.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `curr != null → push, go left` | Phase 1, same as inorder |
| `rightChild = stack.peek().right` | Peek (don't pop!) — we're only *checking* whether it's safe to finalize this node |
| `rightChild == null \|\| rightChild == lastVisited` | Safe to visit: either there's no right subtree, or we already fully processed it |
| `lastVisited = stack.pop(); result.add(...)` | The actual visit — and critically, update `lastVisited` so the *parent* node (now newly on top) knows this subtree is done |
| `else: curr = rightChild;` | Not safe yet — go explore the right subtree first |

## 7. Dry Run
| curr | Stack (top→bottom shown left→right as pushed) | Action | lastVisited | Output |
|---|---|---|---|---|
| 1 | — | push 1, curr=2 | null | `[]` |
| 2 | `[1,2]` | push 2, curr=4 | null | `[]` |
| 4 | `[1,2,4]` | push 4, curr=null | null | `[]` |
| null | `[1,2,4]` | peek 4: right=null → pop 4, visit | 4 | `[4]` |
| null | `[1,2]` | peek 2: right=5, 5≠lastVisited(4) → curr=5 | 4 | `[4]` |
| 5 | `[1,2]` | push 5, curr=null | 4 | `[4]` |
| null | `[1,2,5]` | peek 5: right=null → pop 5, visit | 5 | `[4,5]` |
| null | `[1,2]` | peek 2: right=5==lastVisited → pop 2, visit | 2 | `[4,5,2]` |
| null | `[1]` | peek 1: right=3, 3≠lastVisited(2) → curr=3 | 2 | `[4,5,2]` |
| 3 | `[1]` | push 3, curr=6 | 2 | `[4,5,2]` |
| 6 | `[1,3]` | push 6, curr=null | 2 | `[4,5,2]` |
| null | `[1,3,6]` | peek 6: right=null → pop 6, visit | 6 | `[4,5,2,6]` |
| null | `[1,3]` | peek 3: right=null → pop 3, visit | 3 | `[4,5,2,6,3]` |
| null | `[1]` | peek 1: right=3==lastVisited → pop 1, visit | 1 | `[4,5,2,6,3,1]` |
| null | `[]` | stack empty, curr null → loop ends | | `[4,5,2,6,3,1]` |

Output: `4 5 2 6 3 1` ✅ same result as Problem 5, using half the stack space.

## 8. Test Cases
- Tree above → `[4,5,2,6,3,1]`
- `root == null` → `[]`
- Single node → `[node]`

## Better / Alternative Approach
This *is* the optimal single-stack solution — O(n) time, O(h) space. No further improvement needed; this is the version to default to over the two-stack one in Problem 5 when space matters.

---

# Problem 7 — Maximum Depth

## 1. What is the problem?
LeetCode 104. Find the length of the longest path from root down to any leaf (counted in number of nodes, not edges).

## 2. Example Tree
Same tree.

## 3. My Code
```java
private static int maxDepth(Node root) {
    if (root == null) { return 0; }
    int a = maxDepth(root.left);
    int b = maxDepth(root.right);
    return 1 + Math.max(a, b);
}
```

## 4. Issues / Bugs / Edge Cases
None. Clean, minimal, correct.

## 5. Intuition & Why This Approach
The classic **"ask your children, then add 1 for yourself"** bottom-up recursion pattern. A subtree rooted here is only as deep as its deepest child, plus one level for itself. `null` correctly contributes 0 — a non-existent subtree has no depth.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `if (root == null) return 0;` | Base case: no node, no depth |
| `int a = maxDepth(root.left);` | Depth contributed by everything below-left |
| `int b = maxDepth(root.right);` | Depth contributed by everything below-right |
| `return 1 + Math.max(a, b);` | Take whichever side is deeper, add 1 for the current node itself |

## 7. Dry Run
```text
maxDepth(1)
 = 1 + max(maxDepth(2), maxDepth(3))
 maxDepth(2) = 1 + max(maxDepth(4), maxDepth(5)) = 1 + max(1,1) = 2
 maxDepth(3) = 1 + max(maxDepth(6), maxDepth(null)) = 1 + max(1,0) = 2
 = 1 + max(2,2) = 3
```
Result: **3**

## 8. Test Cases
| Input | Output |
|---|---|
| Tree above | 3 |
| Empty tree | 0 |
| Single node | 1 |
| Left-skewed `1←2←3←4` | 4 |

## Better / Alternative Approach
Already optimal (O(n) time, O(h) space via recursion). An iterative BFS version (count levels) also exists if recursion depth is a concern for very deep/degenerate trees, but for typical interview purposes your version is the expected answer.

---

# Problem 8 — Minimum Depth

## 1. What is the problem?
LeetCode 111. Find the shortest path from root to the **nearest leaf** — not just the nearest `null`. This distinction is the entire difficulty of the problem.

## 2. Example Tree
```text
      1
     /
    2
   /
  3
```
*(Deliberately using a different, one-sided example here — your `main()` tree doesn't expose the tricky case this problem is actually about.)*

## 3. My Code
```java
private static int minDepth(Node root) {
    if (root == null) { return 0; }
    if (root.right == null && root.left == null) { return 1; }
    int l = 0;
    int r = 0;
    if (root.left != null) { l = minDepth(root.left); }
    if (root.right != null) { r = minDepth(root.right); }
    if (l > 0 && r > 0) { return 1 + Math.min(l, r); }
    if (l > 0 && r == 0) { return 1 + l; }
    return 1 + r;
}
```

## 4. Issues / Bugs / Edge Cases
None — this correctly handles the classic trap case below. Some naive solutions get this wrong by writing `1 + min(minDepth(left), minDepth(right))` directly, which would return `1` for the tree above (treating the missing right child as depth 0, as if it were a valid "shorter" leaf) — yours avoids that trap correctly.

## 5. Intuition & Why It's Trickier Than maxDepth
A node with only **one** child is *not* a valid stopping point — you can't call it a "leaf," so you can't just take `min(left, right)` blindly, because whichever side is missing contributes `0`, and `0` would incorrectly look like the shortest path. That's exactly why your code explicitly branches:
- Both children exist → normal case, `1 + min(l, r)`
- Only left exists → `1 + l` (the right "0" must be ignored, not compared against)
- Only right exists → `1 + r`

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `if (root == null) return 0;` | Base case |
| `if (leaf) return 1;` | True leaf — this is a valid stopping point, count it |
| `l`, `r` default to 0 | "0" here specifically means "this side doesn't exist," not "this side has depth 0" |
| `if (l>0 && r>0) return 1+min(l,r);` | Both real subtrees exist → normal shortest-path logic |
| `if (l>0 && r==0) return 1+l;` | Only left is real → must use it, right being "0" is a non-answer, not a valid shorter path |
| `return 1+r;` | Symmetric case (only right is real, or both are somehow 0 — can't happen here since the leaf check already caught that) |

## 7. Dry Run
```text
minDepth(1): not leaf (has left=2, no right)
  l = minDepth(2): not leaf (has left=3, no right)
      l = minDepth(3): IS a leaf → return 1
      r = 0 (no right child at node 2)
      l>0 && r==0 → return 1 + 1 = 2
  r = 0 (no right child at node 1)
  l>0 && r==0 → return 1 + 2 = 3
```
Result: **3** (path 1→2→3), correctly NOT returning 1 (which a naive `min()` would give by treating node 1's missing right child as depth 0).

## 8. Test Cases
| Input | Naive `min()` answer (wrong) | Your correct answer |
|---|---|---|
| `1→2→3` (left-only chain, as above) | 1 (wrong) | 3 |
| Balanced tree from Problem 1 | 3 | 3 (both agree here since tree isn't lopsided) |
| Empty tree | 0 | 0 |
| Single node | 1 | 1 |

## Better / Alternative Approach
An iterative BFS approach also works well here (arguably more intuitive for this specific problem): BFS level by level, and the moment you dequeue a node with **no children at all**, return the current level — first leaf found via BFS is guaranteed to be the minimum depth, no edge-case branching needed. Worth knowing as an alternative mental model, though your recursive solution is equally valid and already correct.

---

# Problem 9 — Count Nodes

## 1. What is the problem?
Count the total number of nodes in the tree.

## 2. Example Tree
Same tree as Problem 1.

## 3. My Code
```java
private static int countNodes(Node root) {
    if (root == null) { return 0; }
    int left = countNodes(root.left);
    int right = countNodes(root.right);
    return 1 + left + right;
}
```

## 4. Issues / Bugs / Edge Cases
None — the simplest possible correct recursion.

## 5. Intuition & Why This Approach
`count(node) = 1 (itself) + count(left subtree) + count(right subtree)`. This is the most basic bottom-up tree recursion pattern — worth knowing cold since Problem 31 (`CountNodeCBT`) is a clever *optimization* of exactly this idea for the special case of complete binary trees.

## 6. Line-by-Line Walkthrough
Self-explanatory given the intuition above — each line maps 1:1 to the formula.

## 7. Dry Run
```text
countNodes(1) = 1 + countNodes(2) + countNodes(3)
  countNodes(2) = 1 + countNodes(4) + countNodes(5) = 1+1+1 = 3
  countNodes(3) = 1 + countNodes(6) + countNodes(null) = 1+1+0 = 2
= 1 + 3 + 2 = 6
```
Result: **6**

## 8. Test Cases
| Input | Output |
|---|---|
| Tree above | 6 |
| Empty tree | 0 |
| Single node | 1 |

## Better / Alternative Approach
For a **complete** binary tree specifically, Problem 31's approach beats this O(n) solution with O(log²n) — but for a general binary tree, this O(n) approach is already optimal (you fundamentally must touch every node once).

---

# Problem 10 — Same Tree

## 1. What is the problem?
LeetCode 100. Given two trees, check whether they are structurally identical *and* have identical values at every position.

## 2. Example Tree
Any two trees to compare — e.g. two separate copies of the tree from Problem 1.

## 3. My Code
```java
public boolean isSameTree(Node p, Node q) {
    if (p == null && q == null) { return true; }
    if (p == null || q == null || p.data != q.data) { return false; }
    return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
}
```

## 4. Issues / Bugs / Edge Cases
None — this correctly handles all four null/non-null combinations via the two guard clauses before comparing children.

## 5. Intuition & Why This Approach
Two trees are the same only if: both are `null` together (base case, trivially equal), OR both are non-null with equal data AND their entire left subtrees match AND their entire right subtrees match. The `&&` at the end means the recursion short-circuits — the moment either subtree mismatch is found, it stops exploring further, avoiding wasted work.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `p==null && q==null → true` | Both trees ended at the same spot — consistent so far |
| `p==null \|\| q==null \|\| p.data!=q.data → false` | Catches: one is null and the other isn't (structural mismatch), OR both exist but values differ |
| final `return` | Only reachable if both nodes exist and match — now recursively verify both subtrees match too |

## 7. Dry Run
Comparing the tree from Problem 1 against an identical copy: every node pair matches, every subtree comparison returns `true`, final result: `true`.
Comparing against a copy where node 5's value is changed to 99: `isSameTree(5, 99)` → `p.data != q.data` → returns `false` immediately, and that `false` propagates up through every `&&` in the call chain back to the root call.

## 8. Test Cases
| p | q | Output |
|---|---|---|
| Tree above | identical copy | `true` |
| Tree above | same shape, one value different | `false` |
| `null` | `null` | `true` |
| `null` | non-empty tree | `false` |
| Same values, different shape (e.g. one has an extra leaf) | | `false` |

## Better / Alternative Approach
Already optimal — O(min(n,m)) time since it short-circuits on the first mismatch, O(h) space. This is the canonical correct solution.

---

# Problem 11 — Invert Binary Tree

## 1. What is the problem?
LeetCode 226. Mirror the entire tree — every node's left and right children swap places, recursively, at every level.

## 2. Example Tree
Same tree.

## 3. My Code
```java
private static Node invertTree(Node root) {
    if (root == null) { return null; }
    invertTree(root.left);
    invertTree(root.right);
    Node dummy = root.left;
    root.left = root.right;
    root.right = dummy;
    return root;
}
```

## 4. Issues / Bugs / Edge Cases
None. Correct and clean.

## 5. Intuition & Why This Approach
Recurse into both children *first* (so they get fully inverted too), then swap the current node's own left/right pointers using a temp variable — the classic three-line pointer swap (`dummy = a; a = b; b = dummy;`). The order of "recurse first vs swap first" actually doesn't matter here (both work, since swapping is a purely local pointer operation that doesn't depend on what's inside the subtrees) — but doing children-first, as you did, is a common convention for tree mutation problems.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `if (root == null) return null;` | Base case — nothing to invert |
| `invertTree(root.left); invertTree(root.right);` | Recursively invert both subtrees *in place* first |
| `Node dummy = root.left;` | Save left before it gets overwritten |
| `root.left = root.right;` | Left slot now holds what was in right |
| `root.right = dummy;` | Right slot now holds the original left (saved in `dummy`) |

## 7. Dry Run
```text
Before:        After invertTree(1):
    1                1
   / \              / \
  2   3            3   2
 / \               |  / \
4   5               6 5   4
```
Node 2's children (4,5) get swapped → (5,4). Node 3's child (6, was left) moves to right. Node 1's children (2,3) swap positions → (3,2).

## 8. Test Cases
| Input | Output |
|---|---|
| Tree above | Mirror image (see diagram) |
| Empty tree | `null` (no crash) |
| Single node | Unchanged (no children to swap) |
| Already-symmetric tree | Still gets swapped structurally, though it may look visually identical if values are symmetric too |

## Better / Alternative Approach
Already optimal (O(n) time, O(h) space). A BFS-based iterative version exists (swap children while processing each node in a queue) if recursion depth is a concern, but yours is the standard expected solution.

---

# Problem 12 — Path Sum (Root-to-Leaf)

## 1. What is the problem?
LeetCode 112. Does *any* root-to-leaf path in the tree sum exactly to a given `targetSum`? Note: must end at a leaf — an internal node accidentally matching the sum doesn't count.

## 2. Example Tree
Same tree.

## 3. My Code
```java
private static boolean hasPathSum(Node root, int targetSum) {
    if (root.left == null && root.right == null && targetSum - root.data == 0) {
        return true;
    }
    if (root.left == null && root.right == null) {
        return false;
    }
    targetSum = targetSum - root.data;
    return hasPathSum(root.left, targetSum) || hasPathSum(root.right, targetSum);
}
```

## 4. Issues / Bugs / Edge Cases
- **Bug: no `root == null` guard at the top.** If called with `hasPathSum(null, x)`, the very first `root.left` access throws a `NullPointerException`. This matters specifically for the *empty tree* case — LeetCode's own definition says an empty tree has no root-to-leaf paths, so the correct answer for `root == null` is `false`, not a crash. Your `main()` never actually calls this on a null root, so the bug never surfaces in your own testing, but it would fail an empty-tree test case.

## 5. Intuition & Why This Approach
Carry the *remaining* sum needed downward, subtracting the current node's value at each step (`targetSum = targetSum - root.data`). Only check for a match **at a true leaf** — checking at internal nodes would incorrectly allow "partial paths" that don't actually reach the bottom of the tree to count as valid.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| First `if` | At a leaf, and the running total minus this leaf's value hits exactly 0 → success |
| Second `if` | At a leaf, but doesn't match → this path fails, no need to look further down (there's nowhere further to go anyway) |
| `targetSum = targetSum - root.data;` | Not a leaf yet — reduce the target by what this node "used up," and pass the reduced target down |
| final `return` | Try both children; if *either* subtree finds a valid path, the whole call succeeds (`||`) |

## 7. Dry Run — `hasPathSum(root, 7)` (path 1→2→4 sums to 7)
```text
hasPathSum(1, 7): not leaf → targetSum = 7-1 = 6
  hasPathSum(2, 6): not leaf → targetSum = 6-2 = 4
    hasPathSum(4, 4): IS leaf, 4-4==0 → return true
  → true, short-circuits, never even calls hasPathSum(5, ...)
→ true
```
Result: **true**

## 8. Test Cases
| Input | targetSum | Output |
|---|---|---|
| Tree above | 7 | `true` (path 1-2-4) |
| Tree above | 5 | `false` (no root-to-leaf path sums to exactly 5) |
| Tree above | 9 | `true` (path 1-3-... wait, check: 1+3=4, no leaf there since 3 has child 6; 1+3+6=10. Let's use 10) |
| Single node `[5]` | 5 | `true` |
| `root == null` | any | **currently crashes** (see bug) — should be `false` |

## Better / Alternative Approach
Fix the null-root bug with a guard at the very top:
```java
private static boolean hasPathSum(Node root, int targetSum) {
    if (root == null) return false;
    ...
}
```
With that one-line fix, this becomes the standard, optimal O(n) worst-case solution — no further improvement needed.

---

# Problem 13 — Diameter of Tree

## 1. What is the problem?
LeetCode 543. The diameter is the length of the longest path between *any* two nodes in the tree (doesn't have to pass through the root), measured in number of **edges**.

## 2. Example Tree
Same tree.

## 3. My Code
```java
int max = 0;
...
private int diameterOfTree(Node root) {
    if (root == null) { return 0; }
    int lh = diameterOfTree(root.left);
    int rh = diameterOfTree(root.right);
    max = Math.max(max, lh + rh);
    return 1 + Math.max(lh, rh);
}
```

## 4. Issues / Bugs / Edge Cases
- Relies on the instance field `max` being freshly `0` before the call — correct in your `main()` since you create a fresh `Tree t = new Tree()` right before calling it, but if this method were called twice on the same `Tree` instance without resetting `max`, the second call's result would be polluted by the first call's leftover `max` value. Worth being aware of if you ever reuse the same object across multiple test trees.

## 5. Intuition & Why This "Piggyback" Pattern
The insight: the diameter *through any single node* equals `leftHeight + rightHeight` at that node (the longest path down-left plus the longest path down-right, meeting at this node). So while computing height bottom-up (which you need to do anyway), you can **piggyback** a check at every node: "is the path *through me* longer than the best diameter found so far?" This is a very common pattern — computing one quantity (height) while using a side-channel (the `max` field) to track a *different* global answer that depends on values seen across many different recursive calls, not just the final return value.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `if (root == null) return 0;` | Empty subtree has height 0 |
| `lh = diameterOfTree(root.left);` | Recursively get left subtree's height (and as a side effect, `max` may get updated deep inside this call) |
| `rh = diameterOfTree(root.right);` | Same for right |
| `max = Math.max(max, lh + rh);` | **The actual diameter check** — "if the path went through me, connecting my two subtrees, how long would it be?" |
| `return 1 + Math.max(lh, rh);` | What gets returned upward is just the **height** (needed by the parent's own diameter calculation) — NOT the diameter itself |

## 7. Dry Run
```text
diameterOfTree(4): lh=0, rh=0, max=max(0,0)=0, return 1
diameterOfTree(5): lh=0, rh=0, max=max(0,0)=0, return 1
diameterOfTree(2): lh=1(from 4), rh=1(from 5), max=max(0,1+1)=2, return 1+max(1,1)=2
diameterOfTree(6): lh=0, rh=0, max unchanged=2, return 1
diameterOfTree(3): lh=1(from 6), rh=0(no right child), max=max(2,1+0)=2, return 1+max(1,0)=2
diameterOfTree(1): lh=2(from node 2), rh=2(from node 3), max=max(2,2+2)=4, return 1+max(2,2)=3
```
Final `max` = **4** — the diameter passes through the root, connecting node 4 (via 2) to node 6 (via 3): path `4-2-1-3-6` = 4 edges.

## 8. Test Cases
| Input | Diameter |
|---|---|
| Tree above | 4 |
| Single node | 0 (no path exists between two distinct nodes) |
| Empty tree | 0 |
| Straight line `1-2-3-4-5` (skewed) | 4 (the whole chain) |

## Better / Alternative Approach
Already the standard, optimal O(n) solution (each node visited once). The only stylistic alternative: return the diameter through a small wrapper object/array instead of an instance field, which avoids the "stale state between calls" issue noted above without changing the algorithm's core logic.

---

# Problem 14 — Balanced Binary Tree

## 1. What is the problem?
LeetCode 110. Check whether every node's left and right subtree heights differ by at most 1 — for *every* node in the tree, not just the root.

## 2. Example Tree
Same tree.

## 3. My Code
```java
private static boolean isBalanced(Node root) {
    if (helper(root) == -1) { return false; }
    return true;
}

private static int helper(Node root) {
    if (root == null) { return 0; }
    int lh = helper(root.left);
    if (lh == -1) { return -1; }
    int rh = helper(root.right);
    if (rh == -1) { return -1; }
    int diff = Math.abs(lh - rh);
    if (diff > 1) { return -1; }
    return 1 + Math.max(lh, rh);
}
```

## 4. Issues / Bugs / Edge Cases
None — this is the correct, efficient version (see below for why the naive version is worse).

## 5. Intuition & Why `-1` as a Sentinel Value
The naive approach would compute the height of every subtree *separately* for every node's balance check — that's O(n) work repeated at every one of n nodes, giving O(n²) overall. **Your solution's trick:** compute height and check balance **in the same single pass**, using `-1` as a **poison value** meaning "unbalanced was already found somewhere below — stop computing, just propagate the failure upward without doing any more work." The moment any subtree returns `-1`, every ancestor immediately short-circuits and returns `-1` too, rather than continuing to compute real heights that no longer matter. This turns it into O(n) — a genuinely important optimization to have internalized, not just memorized.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `if (root == null) return 0;` | Empty subtree — height 0, trivially "balanced" |
| `lh = helper(root.left); if (lh == -1) return -1;` | If left subtree already found itself unbalanced, don't even bother computing right — propagate the failure immediately |
| `rh = helper(root.right); if (rh == -1) return -1;` | Same check on the right side |
| `diff = abs(lh - rh); if (diff > 1) return -1;` | **This** is the actual balance check for the current node |
| `return 1 + max(lh, rh);` | If we got this far, this subtree is balanced — return its real height so the parent can use it |

## 7. Dry Run
```text
helper(4) = 1 (leaf, lh=0, rh=0, diff=0, ok)
helper(5) = 1 (leaf)
helper(2): lh=1, rh=1, diff=0 → ok, return 1+max(1,1)=2
helper(6) = 1 (leaf)
helper(3): lh=1(from 6), rh=0(no right child), diff=1 → still ok (diff not > 1), return 1+max(1,0)=2
helper(1): lh=2(from 2), rh=2(from 3), diff=0 → ok, return 1+max(2,2)=3
```
`helper(root)` returns `3`, not `-1` → `isBalanced` returns **true**.

**Contrast — unbalanced example:**
```text
      1
     /
    2
   /
  3
```
`helper(3)=1`, `helper(2)`: lh=1, rh=0, diff=1 → still ok → returns 2. `helper(1)`: lh=2, rh=0, diff=2 → **> 1 → return -1**. `isBalanced` → **false**.

## 8. Test Cases
| Input | Output |
|---|---|
| Tree above | `true` |
| Left-skewed `1←2←3` | `false` |
| Empty tree | `true` (vacuously balanced — `helper(null)` returns 0, never -1) |
| Single node | `true` |

## Better / Alternative Approach
This already IS the better/optimized approach (O(n) vs the naive O(n²)) — nothing further to improve. Worth explicitly remembering: if you'd written `Math.abs(height(left) - height(right)) <= 1` combined with separately recursing `isBalanced(left) && isBalanced(right)`, that's the naive O(n²) version — your single-pass sentinel approach is strictly better.

---

# Problem 15 — All Root-to-Leaf Paths

## 1. What is the problem?
LeetCode 257. Return every root-to-leaf path as a string like `"1->2->4"`.

## 2. Example Tree
Same tree.

## 3. My Code
```java
private static List<String> BinaryTreePath(Node root) {
    List<String> al = new ArrayList<>();
    if (root == null) { return al; }
    String path = root.data + "";
    helper(root, al, path);
    return al;
}

private static void helper(Node root, List<String> al, String path) {
    if (root == null) { return; }
    if (root.left == null && root.right == null) {
        al.add(path);
        return;
    }
    if (root.left != null) {
        helper(root.left, al, path + "->" + root.left.data);
    }
    if (root.right != null) {
        helper(root.right, al, path + "->" + root.right.data);
    }
}
```

## 4. Issues / Bugs / Edge Cases
None. Note: there's a *second, unrelated* `helper` method used by Problem 25 (`rootToNodePath`) with a different signature (`boolean helper(List<Integer>, Node, int)`) — Java resolves this fine via overloading (different parameter types), but worth remembering these are two separate methods that happen to share a name, not one reused method.

## 5. Intuition & Why This Approach
Build the path string *as you descend* — each recursive call receives the parent's path plus its own value already appended, so there's no need to build-then-undo (no backtracking required) the way Problem 25 needs to, because **Strings are immutable in Java**: every call gets its own independent copy of the path so far, and nothing needs to be "removed" when a call returns. Only add the completed path to the result list at a true leaf.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `path = root.data + "";` | Seed the path with just the root's value, before any recursion starts |
| `if (leaf) { al.add(path); return; }` | Found a complete root-to-leaf path — record it and stop descending here |
| `helper(root.left, al, path + "->" + root.left.data)` | Recurse left, passing a brand-new string that already has the left child appended |
| `helper(root.right, ...)` | Same idea for right |

## 7. Dry Run
```text
helper(1, [], "1")
 → helper(2, [], "1->2")
     → helper(4, [], "1->2->4") → leaf → al = ["1->2->4"]
     → helper(5, [], "1->2->5") → leaf → al = ["1->2->4", "1->2->5"]
 → helper(3, [], "1->3")
     → helper(6, [], "1->3->6") → leaf → al = ["1->2->4", "1->2->5", "1->3->6"]
```

## 8. Test Cases
| Input | Output |
|---|---|
| Tree above | `["1->2->4", "1->2->5", "1->3->6"]` |
| Single node `[5]` | `["5"]` |
| Empty tree | `[]` |

## Better / Alternative Approach
Already an optimal O(n) solution — building via string concatenation on immutable paths (rather than a mutable `StringBuilder` with manual backtracking) is a genuinely clean choice for this specific problem since paths don't share much prefix-reuse benefit compared to, say, an integer-list-based approach. No real improvement needed.

---

# Problem 16 — Maximum Path Sum (Any Node to Any Node)

## 1. What is the problem?
LeetCode 124 — widely considered one of the hardest "medium" tree problems. Unlike Problem 12/15, this path can **start and end at any two nodes**, doesn't need to touch the root, and doesn't need to end at a leaf.

## 2. Example Tree
Same tree (note: all values happen to be positive here, so the true maximum path sum ends up being the entire tree — a less interesting test case than one with negative values; see test cases below for a more revealing example).

## 3. My Code
```java
int maxi = 0;

private int maxPathSum(Node root) {
    maxi = root.data;
    helpermaxPathSum(root);
    return maxi;
}

private int helpermaxPathSum(Node root) {
    if (root == null) { return 0; }
    int lh = helpermaxPathSum(root.left);
    int rh = helpermaxPathSum(root.right);
    if (lh < 0) { lh = 0; }
    if (rh < 0) { rh = 0; }
    maxi = Math.max(maxi, root.data + lh + rh);
    return root.data + Math.max(lh, rh);
}
```

## 4. Issues / Bugs / Edge Cases
- `maxi = root.data;` before the recursion starts is a good defensive initialization — correctly handles the case where every value in the tree is so negative that "just pick the single best node alone" beats any combination. Without this, `maxi = 0` initially would incorrectly allow an all-negative tree to report `0` as the max path sum, even though `0` isn't actually achievable by any real path (an empty path isn't valid — you must pick at least one node).
- No `root == null` guard on `maxPathSum` itself (only on the helper) — if called with a `null` root, `root.data` on the first line throws. Minor, since the problem guarantees at least one node.

## 5. Intuition & Why Two Different Values Get Used
This is genuinely the trickiest tree-DP pattern in your whole file because **two different quantities get computed and used for different purposes at the same time**:
1. **What gets returned to the parent** (`root.data + Math.max(lh, rh)`) — a parent can only *extend* a path through **one** of its children, because if it tried to use both, the path would fork into a "Y" shape, which isn't a valid straight path anymore.
2. **What updates the global `maxi`** (`root.data + lh + rh`) — at *this specific node*, you're allowed to imagine the path **bending** here, using both children at once, because this node is the peak of that particular path, not a pass-through point.

**Why clamp negative gains to 0** (`if (lh < 0) lh = 0;`): a negative contribution from a subtree would only ever *hurt* the sum. It's always at least as good to simply not extend the path into that subtree at all (treat it as contributing 0) rather than dragging the sum down.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `maxi = root.data;` | Seed with a real, achievable answer (a single node alone) before any bigger combination is considered |
| `lh = helpermaxPathSum(root.left);` | Best extendable gain from going into the left subtree |
| `rh = helpermaxPathSum(root.right);` | Same for right |
| clamp `lh`, `rh` to ≥ 0 | Never let a subtree's negative contribution pull the sum down |
| `maxi = Math.max(maxi, root.data + lh + rh);` | Check: "what if the best path bends right here, using both sides?" |
| `return root.data + Math.max(lh, rh);` | What actually gets handed up: the best *single* extension through this node (can't use both — would fork) |

## 7. Dry Run (all-positive tree above)
```text
helper(4): lh=0,rh=0, maxi=max(0,4+0+0)=4, return 4+max(0,0)=4
helper(5): lh=0,rh=0, maxi=max(4,5)=5, return 5
helper(2): lh=4,rh=5, maxi=max(5, 2+4+5)=11, return 2+max(4,5)=7
helper(6): lh=0,rh=0, maxi=max(11,6)=11, return 6
helper(3): lh=6,rh=0, maxi=max(11, 3+6+0)=11, return 3+max(6,0)=9
helper(1): lh=7(from 2),rh=9(from 3), maxi=max(11, 1+7+9)=17, return 1+max(7,9)=10
```
Final `maxi` = **17** (path 4-2-1-3-6, using all the "extendable" gains: 4+2+1+3+6 = 16... let's recheck: the path bending at root uses lh=7 (best single-side gain from node 2, which is 2+4=6 or 2+5=7, taking the 5 side) and rh=9 (best from node 3: 3+6=9). So the winning path is 5-2-1-3-6 = 5+2+1+3+6 = 17.) ✅ matches.

## 8. Test Cases
| Input | maxPathSum |
|---|---|
| Tree above (all positive) | 17 |
| `[-10, 9, 20, null, null, 15, 7]` (classic LeetCode example with negatives) | 42 (path 15→20→7) |
| Single node `[-3]` | -3 |
| Two nodes `[-1, -2]` (root -1, left child -2) | -1 (picking just the root beats combining with a negative child) |

## Better / Alternative Approach
This IS the standard optimal O(n) solution for this problem — no meaningful algorithmic improvement exists. Purely stylistic: some implementations pass `maxi` back via a `int[]` array parameter or a wrapper class instead of an instance field, to avoid the same "stale state across calls" concern noted in Problem 13.

---

# Problem 17 — Zigzag Level Order

## 1. What is the problem?
LeetCode 103. Same as level order (Problem 2), but alternate direction each level: left-to-right, then right-to-left, then left-to-right...

## 2. Example Tree
Same tree.

## 3. My Code
```java
private static List<List<Integer>> zigzagLevelOrder(Node root) {
    List<List<Integer>> res = new ArrayList<>();
    if (root == null) { return res; }
    Queue<Node> q = new ArrayDeque<>();
    q.offer(root);
    boolean b = true;
    while (!q.isEmpty()) {
        int n = q.size();
        List<Integer> al = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Node t = q.poll();
            if (t.left != null) { q.offer(t.left); }
            if (t.right != null) { q.offer(t.right); }
            al.add(t.data);
        }
        if (b) { res.add(al); } else { Collections.reverse(al); res.add(al); }
        b = !b;
    }
    return res;
}
```

## 4. Issues / Bugs / Edge Cases
None — correctly reuses the level-order skeleton and only touches the *output* list per level, never the actual traversal/queue order.

## 5. Intuition & Why This Approach
Same BFS skeleton as Problem 2 (level-size snapshot trick included), plus a boolean flag `b` that flips every level. **Key insight:** you don't actually traverse right-to-left in the tree itself — you always process the queue normally (left-to-right, since children were always pushed left-then-right). The zigzag effect is achieved purely by **reversing the output list** for alternate levels, after the level's values have already been collected normally. This is much simpler than trying to actually walk the tree in alternating directions.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `boolean b = true;` | Tracks whether the *current* level should stay left-to-right (`true`) or get reversed (`false`) |
| inner loop | Identical to plain level order — always collects left-to-right |
| `if (b) res.add(al); else { reverse; res.add(al); }` | Only the *output*, not the traversal, gets flipped |
| `b = !b;` | Flip the flag for the next level |

## 7. Dry Run
| Level | Collected (always L→R) | `b` | Final in `res` |
|---|---|---|---|
| 0 | `[1]` | true | `[1]` |
| 1 | `[2,3]` | false | `[3,2]` (reversed) |
| 2 | `[4,5,6]` | true | `[4,5,6]` |

Result: `[[1], [3,2], [4,5,6]]`

## 8. Test Cases
| Input | Output |
|---|---|
| Tree above | `[[1],[3,2],[4,5,6]]` |
| Empty tree | `[]` |
| Single node | `[[node]]` |

## Better / Alternative Approach
Already the standard, clean O(n) solution. An alternative worth knowing: using a `Deque` and alternately adding to the front/back while collecting (instead of collecting then reversing) avoids the `Collections.reverse` call, but is not meaningfully faster — same time complexity either way.

---

# Problem 18 — Boundary Traversal (Anti-Clockwise)

## 1. What is the problem?
Print the tree's outline: root → left boundary top-to-bottom (excluding leaves) → all leaves left-to-right → right boundary bottom-to-top (excluding leaves). A classic "decompose into 3 sub-traversals" problem.

## 2. Example Tree
Same tree.

## 3. My Code
```java
private static List<Integer> boundaryTraversalInAntiClockWise(Node root) {
    if (root == null) { return null; }
    List<Integer> al = new ArrayList<>();
    if (!isLeaf(root)) { al.add(root.data); }
    if (root.left != null) { getLeft(root.left, al); }
    getLeaf(root, al);
    if (root.right != null) { getRight(root.right, al); }
    return al;
}

private static void getRight(Node root, List<Integer> al) {
    Stack<Integer> st = new Stack<>();
    Node curr = root;
    while (curr != null) {
        if (curr.right != null) {
            if (!isLeaf(curr)) { st.push(curr.data); }
            curr = curr.right;
        } else {
            if (!isLeaf(curr)) { st.push(curr.data); }
            curr = curr.left;
        }
    }
    while (!st.isEmpty()) { al.add(st.pop()); }
}

private static void getLeaf(Node root, List<Integer> al) {
    if (root == null) { return; }
    getLeaf(root.left, al);
    if (isLeaf(root)) { al.add(root.data); }
    getLeaf(root.right, al);
}

private static void getLeft(Node root, List<Integer> al) {
    Node curr = root;
    while (curr != null) {
        if (curr.left != null) {
            if (!isLeaf(curr)) { al.add(curr.data); }
            curr = curr.left;
        } else {
            if (!isLeaf(curr)) { al.add(curr.data); }
            curr = curr.right;
        }
    }
}

private static boolean isLeaf(Node root) {
    return root.left == null && root.right == null;
}
```

## 4. Issues / Bugs / Edge Cases
- Returning `null` (not an empty list) for `root == null` is inconsistent with the rest of your file (most other list-returning methods return an empty list for the empty-tree case) — a caller doing `.size()` on the result would crash. Worth standardizing to `return new ArrayList<>();` for consistency, though not "wrong" per se if the caller is aware.
- Correctly avoids double-counting: a node that's both on the boundary path AND a leaf is deliberately skipped by `getLeft`/`getRight` (via the `isLeaf` check) since `getLeaf` will pick it up anyway.

## 5. Intuition & Why Three Different Traversal Strategies
This problem is a great example of **decomposing a hard problem into simpler known sub-problems**, each solved with a *different* strategy suited to its own shape:
- **`getLeft`**: walk down preferring left, falling back to right only if no left child exists — pushed directly into the result in **top-to-bottom order** as you go (no reversal needed, since that's already the order you want).
- **`getLeaf`**: any traversal works here (you used a simple recursive inorder-shaped walk) — it just needs to visit every leaf left-to-right, which any standard DFS naturally does.
- **`getRight`**: walk down preferring right, but this time you need the results in **bottom-to-top** order — the opposite of the natural top-down visiting order — so you push onto a `Stack` first and pop everything at the end, letting the stack's LIFO nature do the reversal for you.

## 6. Line-by-Line Walkthrough (getRight, the trickiest of the three)
| Line | What happens |
|---|---|
| `st.push(curr.data)` (both branches) | Collect data on the way down, but into a stack — order will be corrected later |
| `if (!isLeaf(curr))` | Skip pushing if this node is a leaf (leaves are handled by `getLeaf` separately) |
| final `while (!st.isEmpty())` | Pop everything — this reverses top-down collection into the bottom-up order the boundary actually needs |

## 7. Dry Run
- Root check: `1` is not a leaf → `al = [1]`
- `getLeft(2, al)`: curr=2, has left(4), not leaf → `al=[1,2]`, curr=4; 4 has no left, check right(none) → curr=4 is now checked: is it a leaf? yes → skip adding, curr=curr.left(null since curr.left doesn't exist)... *(curr becomes null via the left branch since node 4 has no left child, ending the walk)* → `al = [1, 2]`
- `getLeaf(1, al)`: recursively visits leaves left-to-right → 4, 5, 6 → `al = [1, 2, 4, 5, 6]`
- `getRight(3, al)`: curr=3, no right child → check left branch: not leaf → push 3 → `st=[3]`, curr=curr.left=6; curr=6, is leaf → don't push, curr becomes null (6 has neither left nor right, falls into left-branch, `curr.left` is null) → walk ends. Pop stack: `al = [1,2,4,5,6,3]`

Final: `[1, 2, 4, 5, 6, 3]`

## 8. Test Cases
| Input | Output |
|---|---|
| Tree above | `[1, 2, 4, 5, 6, 3]` |
| Single node | `[node]` (root itself is a leaf, so root isn't added by the first check, but IS picked up by `getLeaf`) |
| Empty tree | currently `null` (see bug note above — should arguably be `[]`) |

## Better / Alternative Approach
Algorithmically your three-part decomposition is the standard accepted solution — no better overall strategy exists for this problem. The only real fix worth making is the `return null` → `return new ArrayList<>()` consistency issue noted above.

---

# Problem 19 — Vertical Order Traversal

## 1. What is the problem?
LeetCode 987. Group nodes into vertical "columns" as if you dropped straight lines down through the tree. Within the same column *and* the same row (depth), ties are broken by sorting values ascending.

## 2. Example Tree
Same tree.

## 3. My Code
```java
class Tuple {
    Node node; int column; int row;
    public Tuple(Node node, int column, int row) {
        this.node = node; this.column = column; this.row = row;
    }
}

public List<List<Integer>> verticalTraversal(Node root) {
    List<List<Integer>> ans = new ArrayList<>();
    if (root == null) return ans;
    TreeMap<Integer, TreeMap<Integer, PriorityQueue<Integer>>> map = new TreeMap<>();
    Queue<Tuple> q = new LinkedList<>();
    q.offer(new Tuple(root, 0, 0));
    while (!q.isEmpty()) {
        Tuple tuple = q.poll();
        Node node = tuple.node; int column = tuple.column; int row = tuple.row;
        if (!map.containsKey(column)) { map.put(column, new TreeMap<>()); }
        if (!map.get(column).containsKey(row)) { map.get(column).put(row, new PriorityQueue<>()); }
        map.get(column).get(row).offer(node.data);
        if (node.left != null) { q.offer(new Tuple(node.left, column - 1, row + 1)); }
        if (node.right != null) { q.offer(new Tuple(node.right, column + 1, row + 1)); }
    }
    for (TreeMap<Integer, PriorityQueue<Integer>> rows : map.values()) {
        List<Integer> list = new ArrayList<>();
        for (PriorityQueue<Integer> pq : rows.values()) {
            while (!pq.isEmpty()) { list.add(pq.poll()); }
        }
        ans.add(list);
    }
    return ans;
}
```

## 4. Issues / Bugs / Edge Cases
None — this correctly handles the full LeetCode 987 spec (column-major output, row-ascending within column, value-ascending for true ties).

## 5. Intuition & Why This Nested Structure
This is BFS with **coordinates attached instead of just plain node values**:
- `column` shifts `-1` going left, `+1` going right (a number line centered on the root at 0).
- `row` increases by `+1` every level down — needed for two reasons: (a) to order same-column nodes correctly when they're at different depths, and (b) to know which nodes are *genuinely* tied (same column AND same row) versus just happening to share a column at different depths.
- The nested structure `TreeMap<column, TreeMap<row, PriorityQueue<value>>>` does all the sorting for free: `TreeMap` keeps keys sorted automatically (columns ascending, then rows ascending within each column), and `PriorityQueue` (a min-heap) keeps genuinely-tied values sorted ascending. By the time you read the structure out, everything is already in the exact order the problem wants — no separate sort step needed.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `q.offer(new Tuple(root, 0, 0))` | Root starts at column 0, row 0 |
| `map.containsKey / put` (column, then row) | Lazily create the nested map entries only as columns/rows are actually encountered |
| `map.get(column).get(row).offer(node.data)` | Store the value in the right (column, row) bucket — a min-heap so duplicates at the exact same spot sort themselves |
| `column - 1` / `column + 1` for children | The core coordinate math — left always shifts the column left, right always shifts it right |
| final nested `for` loops | Read the whole structure back out — TreeMap's `.values()` already iterates in sorted key order, so no explicit sort is needed anywhere |

## 7. Dry Run — column/row assigned to each node
| Node | column | row |
|---|---|---|
| 1 | 0 | 0 |
| 2 | -1 | 1 |
| 3 | +1 | 1 |
| 4 | -2 | 2 |
| 5 | 0 | 2 |
| 6 | 0 | 2 |

Grouping by column (TreeMap keeps them sorted -2, -1, 0, +1):
- Column -2: `{row2: [4]}` → `[4]`
- Column -1: `{row1: [2]}` → `[2]`
- Column 0: `{row0: [1], row2: [5,6]}` → `[1, 5, 6]` (row0 comes before row2, and within row2, 5 and 6 are genuinely tied — sorted ascending by the min-heap)
- Column +1: `{row1: [3]}` → `[3]`

Result: `[[4], [2], [1, 5, 6], [3]]`

## 8. Test Cases
| Input | Output |
|---|---|
| Tree above | `[[4],[2],[1,5,6],[3]]` |
| Single node | `[[node]]` |
| Empty tree | `[]` |
| Two nodes genuinely tied at same column+row (e.g. from a different, denser tree) | Sorted ascending within that bucket |

## Better / Alternative Approach
This is essentially the textbook-optimal LeetCode 987 solution using the standard triple-sorted-structure approach. A minor simplification some solutions use: collect all `(column, row, value)` triples into a flat `List`, then do one `Collections.sort` with a custom comparator at the end instead of nested TreeMaps — same complexity, marginally simpler code, but your nested-TreeMap version is equally valid and arguably clearer about *why* each level of sorting exists.

---

# Problem 20 — Top View

## 1. What is the problem?
Which nodes are visible looking straight down at the tree from above? One node per vertical line — whichever is encountered *first* (closest to the root).

## 2. Example Tree
Same tree.

## 3. My Code
```java
private static List<Integer> topViewOfBinaryTree(Node root) {
    List<Integer> al = new ArrayList<>();
    if (root == null) { return al; }
    TreeMap<Integer, Node> map = new TreeMap<>();
    Queue<NodeWithLine> q = new LinkedList<>();
    q.offer(new NodeWithLine(root, 0));
    while (!q.isEmpty()) {
        NodeWithLine dummy = q.poll();
        int line = dummy.line; Node node = dummy.node;
        if (!map.containsKey(line)) { map.put(line, node); }
        if (node.left != null) { q.offer(new NodeWithLine(node.left, line - 1)); }
        if (node.right != null) { q.offer(new NodeWithLine(node.right, line + 1)); }
    }
    for (Node rows : map.values()) { al.add(rows.data); }
    return al;
}
```

## 4. Issues / Bugs / Edge Cases
None — correct and clean.

## 5. Intuition & Why This Approach
Same "line" (horizontal distance) idea as vertical traversal, but simpler — no `row` needed at all. Because BFS processes strictly level-by-level, **the first node to reach a given line is guaranteed to be the topmost one at that line** — there's no need to compare depths explicitly the way vertical traversal did.

**The one line that makes this "top" view specifically:** `if (!map.containsKey(line))`. Only insert if this vertical line hasn't been claimed yet — since BFS visits shallower nodes before deeper ones, the first write to any line IS the topmost node there, and every later attempt to write to that same line gets silently ignored.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `if (!map.containsKey(line))` | **The core trick.** First-come-first-served per line, guaranteed correct because of BFS order |
| everything else | Standard BFS-with-horizontal-distance, same as vertical traversal but 1-dimensional |

## 7. Dry Run
BFS order: 1(line 0) → 2(line -1), 3(line +1) → 4(line -2), 5(line 0, but ALREADY taken by 1 → ignored), 6(line 0, also ignored)

Map ends up: `{-2: 4, -1: 2, 0: 1, 1: 3}`

Result: `[4, 2, 1, 3]`

## 8. Test Cases
| Input | Output |
|---|---|
| Tree above | `[4, 2, 1, 3]` |
| Single node | `[node]` |
| Empty tree | `[]` |

## Better / Alternative Approach
Already the standard optimal solution. Nothing to improve.

---

# Problem 21 — Bottom View

## 1. What is the problem?
Opposite of top view — which nodes are visible from *below*? One node per vertical line, whichever is *deepest*.

## 2. Example Tree
Same tree.

## 3. My Code
```java
private static List<Integer> bottomViewOfBinaryTree(Node root) {
    List<Integer> al = new ArrayList<>();
    if (root == null) { return al; }
    Queue<NodeWithLine> q = new LinkedList<>();
    TreeMap<Integer, Node> map = new TreeMap<>();
    q.offer(new NodeWithLine(root, 0));
    while (!q.isEmpty()) {
        NodeWithLine tv = q.poll();
        Node node = tv.node; int line = tv.line;
        map.put(line, node);
        if (node.left != null) { q.offer(new NodeWithLine(node.left, line - 1)); }
        if (node.right != null) { q.offer(new NodeWithLine(node.right, line + 1)); }
    }
    for (Node rows : map.values()) { al.add(rows.data); }
    return al;
}
```
*(Your file also has some commented-out lines here — `map.putIfAbsent(line, node)` and an `if/else` version — leftover experimentation, correctly ignored since only the active `map.put(line, node)` line runs.)*

## 4. Issues / Bugs / Edge Cases
None in the active code. The commented-out alternatives are worth noting as a sign you were deliberately *comparing* "first wins" (top view) vs "last wins" (bottom view) logic while writing this — good instinct, just clean up the dead code when you revisit.

## 5. Intuition & Why Almost-Identical Code Flips the Whole Problem
Nearly line-for-line identical to top view — the **entire** difference is removing the `if (!map.containsKey(line))` guard and just **always overwriting**: `map.put(line, node)` unconditionally. Since BFS still processes shallow-to-deep, later writes to the same line always come from deeper nodes — so by the time BFS finishes, whatever remains in the map at each line is the deepest (bottommost) node seen there. One `if` check removed = the entire problem flips from "top" to "bottom." Elegant, worth remembering as a "one-line problem" the next time you see it.

## 6. Line-by-Line Walkthrough
Identical to Problem 20 except: `map.put(line, node);` runs **unconditionally** every time, letting later (deeper) writes silently overwrite earlier (shallower) ones — the opposite priority from top view.

## 7. Dry Run
BFS order: 1(line 0) → 2(line -1), 3(line +1) → 4(line -2), 5(line 0), 6(line 0)

Writes to `map`, in order: `0:1`, `-1:2`, `1:3`, `-2:4`, `0:5` (overwrites `0:1`), `0:6` (overwrites `0:5`)

Final map: `{-2:4, -1:2, 0:6, 1:3}`

Result: `[4, 2, 6, 3]`

## 8. Test Cases
| Input | Output |
|---|---|
| Tree above | `[4, 2, 6, 3]` |
| Single node | `[node]` |
| Empty tree | `[]` |

## Better / Alternative Approach
Already standard and optimal. Clean up the commented-out experimentation lines for readability, but no functional change needed.

---

# Problem 22 — Right View

## 1. What is the problem?
LeetCode 199. What's visible looking at the tree from the right side — the rightmost node at every depth.

## 2. Example Tree
Same tree.

## 3. My Code
```java
private static List<Integer> rightViewOfBinaryTree(Node root) {
    List<Integer> al = new ArrayList<>();
    if (root == null) { return al; }
    rightViewHelper(root, al, 0);
    return al;
}

private static void rightViewHelper(Node root, List<Integer> al, int k) {
    if (root == null) { return; }
    if (al.size() == k) { al.add(root.data); }
    rightViewHelper(root.right, al, k + 1);
    rightViewHelper(root.left, al, k + 1);
}
```

## 4. Issues / Bugs / Edge Cases
None.

## 5. Intuition & Why DFS Instead of BFS Here
Unlike top/bottom view, this uses **DFS**, recursing **right before left**. The trick: `al.size() == k` means "this is the very first time we've reached depth `k`." Since right is always explored before left at every node, the *first* node to reach any given depth is guaranteed to be the rightmost one at that depth — later arrivals at the same depth (from left branches) find `al.size() != k` and get silently skipped.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `if (al.size() == k)` | First arrival at this depth → record it |
| `rightViewHelper(root.right, al, k+1)` FIRST | Explore right before left — this ordering is what guarantees "first arrival = rightmost" |
| `rightViewHelper(root.left, al, k+1)` SECOND | Only reaches a given depth after the right side has already claimed it, if a rightmost node exists there |

## 7. Dry Run
```text
rightViewHelper(1, [], 0): al.size()(0)==k(0) → add 1 → al=[1]
  → right first: rightViewHelper(3, [1], 1): al.size()(1)==k(1) → add 3 → al=[1,3]
      → right: rightViewHelper(null,...,2) → return
      → left: rightViewHelper(6, [1,3], 2): al.size()(2)==k(2) → add 6 → al=[1,3,6]
  → left: rightViewHelper(2, [1,3,6], 1): al.size()(3)!=k(1) → skip
      → right: rightViewHelper(5, ..., 2): al.size()(3)!=k(2) → skip
      → left: rightViewHelper(4, ..., 2): al.size()(3)!=k(2) → skip
```
Result: `[1, 3, 6]`

## 8. Test Cases
| Input | Output |
|---|---|
| Tree above | `[1, 3, 6]` |
| Single node | `[node]` |
| Empty tree | `[]` |
| Left-skewed only (no right children anywhere) | Every node appears (each depth's "rightmost" is just the only node there) |

## Better / Alternative Approach
Already optimal O(n). A BFS-based alternative (take the last element of each level from Problem 2's skeleton) also works and some find it more intuitive — functionally equivalent, same complexity.

---

# Problem 23 — Left View

## 1. What is the problem?
Mirror of Problem 22 — the leftmost node visible at every depth.

## 2. Example Tree
Same tree.

## 3. My Code
```java
private static List<Integer> leftViewOfBInaryTree(Node root) {
    List<Integer> al = new ArrayList<>();
    if (root == null) { return al; }
    leftViewHelper(root, al, 0);
    return al;
}

private static void leftViewHelper(Node root, List<Integer> al, int i) {
    if (root == null) { return; }
    if (al.size() == i) { al.add(root.data); }
    leftViewHelper(root.left, al, i + 1);
    leftViewHelper(root.right, al, i + 1);
}
```

## 4. Issues / Bugs / Edge Cases
None. (Minor naming typo: `leftViewOfBInaryTree` — capital "I" instead of lowercase — purely cosmetic, doesn't affect behavior.)

## 5. Intuition & Why This Approach
Exact mirror of right view — recurse **left before right** this time. Same `al.size() == depth` trick, but now the first node to reach a new depth is the leftmost one, since left is always explored first.

## 6. Line-by-Line Walkthrough
Same structure as Problem 22, with `left` and `right` recursive calls swapped in order.

## 7. Dry Run
```text
leftViewHelper(1,[],0): size(0)==0 → add 1 → [1]
  left: leftViewHelper(2,[1],1): size(1)==1 → add 2 → [1,2]
    left: leftViewHelper(4,[1,2],2): size(2)==2 → add 4 → [1,2,4]
      ... (4's children are null, both return immediately)
    right: leftViewHelper(5,[1,2,4],2): size(3)!=2 → skip
  right: leftViewHelper(3,[1,2,4],1): size(3)!=1 → skip
    left: leftViewHelper(6,[1,2,4],2): size(3)!=2 → skip
```
Result: `[1, 2, 4]`

## 8. Test Cases
| Input | Output |
|---|---|
| Tree above | `[1, 2, 4]` |
| Single node | `[node]` |
| Empty tree | `[]` |

## Better / Alternative Approach
Already optimal. Same BFS-based alternative note as Problem 22 applies (take the first element of each level).

---

# Problem 24 — Symmetric Tree

## 1. What is the problem?
LeetCode 101. Is the tree a mirror image of itself around its vertical center?

## 2. Example Tree
```text
      1
     / \
    2   2
   / \ / \
  3  4 4  3
```
*(A genuinely symmetric tree — your `main()` tree isn't symmetric, so this is a better test case for this specific problem.)*

## 3. My Code
```java
private static boolean isSymmetric(Node root) {
    return helperSymmetric(root.left, root.right);
}

private static boolean helperSymmetric(Node n1, Node n2) {
    if (n1 == null && n2 == null) { return true; }
    if (n1 == null || n2 == null) { return false; }
    return (n1.data == n2.data) && helperSymmetric(n1.left, n2.right) && helperSymmetric(n1.right, n2.left);
}
```

## 4. Issues / Bugs / Edge Cases
- `isSymmetric` itself doesn't guard against `root == null` — calling it on an empty tree throws (`root.left` on a null root). Minor gap; LeetCode's own constraints guarantee at least one node, so it doesn't surface in practice, but worth a mental note.

## 5. Intuition & Why the Cross-Comparison
The key insight most people get wrong on first try: you're not comparing `n1.left` to `n2.left` — you're comparing **`n1.left` to `n2.right`, and `n1.right` to `n2.left`**. That crossed comparison is exactly what "mirror image" means structurally: the left side of one subtree must match the *right* side of the other, not the same side.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `helperSymmetric(root.left, root.right)` | Start by comparing the two subtrees hanging off the root — they need to be mirrors of *each other* |
| `n1==null && n2==null → true` | Both sides ended together — consistent |
| `n1==null \|\| n2==null → false` | One side has a node, the other doesn't — structural mismatch |
| `(n1.data==n2.data) && helperSymmetric(n1.left,n2.right) && helperSymmetric(n1.right,n2.left)` | Values must match, AND the crossed subtree pairs must also be mirrors, recursively |

## 7. Dry Run
```text
helperSymmetric(2,2) [root.left vs root.right]: data matches
  → helperSymmetric(3,3) [2.left vs 2(right-side).right]: both leaves, data matches, children null&null → true
  → helperSymmetric(4,4) [2.right vs 2(right-side).left]: both leaves, data matches → true
→ true
```
Result: **true**

## 8. Test Cases
| Input | Output |
|---|---|
| Symmetric tree above | `true` |
| Your `main()` tree (2≠3 as siblings, different structure entirely) | `false` |
| Single node | `true` (trivially symmetric — no children to compare) |
| Two nodes, only a left child at root | `false` (one side null, other isn't) |

## Better / Alternative Approach
Already the standard optimal recursive solution. An iterative BFS version exists too (push pairs of nodes into a queue, compare two at a time) if recursion depth is a concern.

---

# Problem 25 — Root-to-Node Path

## 1. What is the problem?
Given a target value `k`, return the list of values from the root down to the node containing `k`.

## 2. Example Tree
Same tree.

## 3. My Code
```java
private static List<Integer> rootToNodePath(Node root, int k) {
    List<Integer> al = new ArrayList<>();
    helper(al, root, k);
    return al;
}

private static boolean helper(List<Integer> al, Node root, int k) {
    if (root == null) { return false; }
    al.add(root.data);
    if (root.data == k) { return true; }
    if (helper(al, root.left, k) || helper(al, root.right, k)) { return true; }
    al.remove(al.size() - 1);
    return false;
}
```

## 4. Issues / Bugs / Edge Cases
None — the backtracking (`al.remove(...)`) is correctly placed *only* on the failure path, after both children have been tried and neither found the target.

## 5. Intuition & Why Backtracking (and Why Not Problem 15's Approach)
Unlike Problem 15 (which used immutable Strings, so nothing needed undoing), this uses a **mutable shared `List`** — which is more space-efficient for this use case (one list reused across the whole search, rather than a new string built at every call), but means you must explicitly **undo** a wrong guess: add the current node speculatively, try both children, and if *neither* leads to the target, remove the node you just speculatively added before returning `false` to the parent call. This "add, try, undo-if-wrong" pattern is the essence of backtracking.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `al.add(root.data);` | Speculatively assume this node is part of the path |
| `if (root.data == k) return true;` | Found it — the speculative addition was correct, keep it, stop searching |
| `if (helper(left) \|\| helper(right)) return true;` | Try both children; if either succeeds, this node truly was on the path — keep the addition and propagate success upward |
| `al.remove(al.size()-1); return false;` | **Only reached if both children failed** — this node was a dead end, undo the speculative addition before returning failure to the caller |

## 7. Dry Run — `rootToNodePath(root, 6)`
```text
helper([], 1, 6): al=[1], 1≠6
  helper([1], 2, 6): al=[1,2], 2≠6
    helper([1,2], 4, 6): al=[1,2,4], 4≠6, no children → both helper calls return false → remove 4 → al=[1,2] → return false
    helper([1,2], 5, 6): al=[1,2,5], 5≠6, no children → remove 5 → al=[1,2] → return false
    both failed → remove 2 → al=[1] → return false
  helper([1], 3, 6): al=[1,3], 3≠6
    helper([1,3], 6, 6): al=[1,3,6], 6==6 → return true (KEEP 6, don't remove!)
  → true, propagates up, al stays [1,3,6]
```
Result: `[1, 3, 6]`

## 8. Test Cases
| Input | k | Output |
|---|---|---|
| Tree above | 6 | `[1, 3, 6]` |
| Tree above | 5 | `[1, 2, 5]` |
| Tree above | 7 (doesn't exist) | `[]` (every branch fails and backtracks all the way, leaving the list empty) |
| Tree above | 1 (the root) | `[1]` |

## Better / Alternative Approach
Already the standard, optimal O(n) backtracking solution — this is the expected approach for this exact problem shape.

---

# Problem 26 — Lowest Common Ancestor (LCA)

## 1. What is the problem?
LeetCode 236. Given two values `x` and `y`, find their lowest common ancestor — the deepest node that has both `x` and `y` somewhere in its subtree.

## 2. Example Tree
Same tree.

## 3. My Code
```java
private static Node lowestCommonAncestors(Node root, int x, int y) {
    if (root == null || root.data == x || root.data == y) { return root; }
    Node left = lowestCommonAncestors(root.left, x, y);
    Node right = lowestCommonAncestors(root.right, x, y);
    if (left == null) { return right; }
    else if (right == null) { return left; }
    return root;
}
```

## 4. Issues / Bugs / Edge Cases
None — this is the textbook-correct O(n) LCA solution and correctly assumes both `x` and `y` actually exist in the tree (standard LeetCode 236 guarantee).

## 5. Intuition & Why This Approach
At every node, ask: **"is `x` or `y` found somewhere in my left subtree? In my right subtree?"**
- If **both sides return something non-null** → this node IS the LCA — `x` and `y` live in *different* subtrees, and this is the exact point where their paths split.
- If only one side is non-null → the LCA must be further down that side (bubble that answer straight up unchanged).
- Base case: if the current node itself *is* `x` or `y`, return it immediately — a node is allowed to be its own ancestor (this matters if one target is actually an ancestor of the other).

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `root==null \|\| root.data==x \|\| root.data==y → return root` | Base case: either ran off the tree, or found one of the two targets — either way, hand this back up as-is |
| `left = lowestCommonAncestors(root.left, x, y);` | Search the entire left subtree for either target |
| `right = lowestCommonAncestors(root.right, x, y);` | Search the entire right subtree |
| `if (left == null) return right;` | Nothing relevant found on the left → whatever's on the right (found or not) is the real answer |
| `else if (right == null) return left;` | Symmetric case |
| `return root;` | Both sides found something → this node is where the paths to `x` and `y` diverge — the LCA |

## 7. Dry Run — `lowestCommonAncestors(root, 4, 5)`
```text
lowestCommonAncestors(1,4,5): 1≠4,1≠5
  left = lowestCommonAncestors(2,4,5): 2≠4,2≠5
    left = lowestCommonAncestors(4,4,5): 4==4 → return node 4
    right = lowestCommonAncestors(5,4,5): 5==5 → return node 5
    left≠null AND right≠null → return node 2 (this IS the LCA)
  → left = node 2
  right = lowestCommonAncestors(3,4,5): neither 4 nor 5 found anywhere under 3 → returns null
  left≠null, right==null → return left = node 2
```
Result: node **2** — correct, since 4 and 5 are both under node 2, and 2 is the deepest common ancestor.

## 8. Test Cases
| Input | x | y | Output |
|---|---|---|---|
| Tree above | 4 | 5 | node 2 |
| Tree above | 2 | 3 | node 1 (root — they're in different top-level subtrees) |
| Tree above | 2 | 4 | node 2 (2 is an ancestor of 4, so 2 is its own LCA with 4) |
| Tree above | 4 | 6 | node 1 |

## Better / Alternative Approach
This IS the optimal O(n) solution for the general binary tree case. Note: if the tree were specifically a **BST** (not the case in your `main()` example), there's a faster O(h) approach that uses the BST ordering property directly (compare both values against the current node's value to decide which single direction to recurse into) instead of exploring both subtrees — worth knowing as a distinct technique if you're specifically told the tree is a BST.

---

# Problem 27 — Maximum Width of Binary Tree

## 1. What is the problem?
LeetCode 662. The width at any level = the number of positions between the leftmost and rightmost non-null node **as if the tree were a complete binary tree array**, including the gaps where nulls would be. Return the maximum such width across all levels.

## 2. Example Tree
Same tree.

## 3. My Code
```java
class PairForWidthOFTree {
    Node node; int index;
    PairForWidthOFTree(Node node, int index) { this.node = node; this.index = index; }
}

private static int MaximumWidthofBinaryTree(Node root) {
    if (root == null) { return 0; }
    Queue<PairForWidthOFTree> q = new ArrayDeque<>();
    q.add(new PairForWidthOFTree(root, 0));
    int maxWidth = 0;
    while (!q.isEmpty()) {
        int n = q.size();
        int min = q.peek().index;
        int first = 0; int last = 0;
        for (int i = 0; i < n; i++) {
            PairForWidthOFTree pair = q.poll();
            Node node = pair.node;
            int currIndex = pair.index - min;
            if (i == 0) { first = currIndex; }
            if (i == n - 1) { last = currIndex; }
            if (node.left != null) { q.add(new PairForWidthOFTree(node.left, 2 * currIndex + 1)); }
            if (node.right != null) { q.add(new PairForWidthOFTree(node.right, 2 * currIndex + 2)); }
        }
        maxWidth = Math.max(maxWidth, last - first + 1);
    }
    return maxWidth;
}
```

## 4. Issues / Bugs / Edge Cases
None — the normalization step (subtracting `min` before computing child indices) is exactly the fix needed to prevent index overflow on deep/skewed trees, and it's implemented correctly.

## 5. Intuition & Why Index Normalization Matters
Assign each node an index as if the tree were laid out as a complete binary tree array: root = 0, left child = `2*i+1`, right child = `2*i+2`. Width at any level = `lastIndex - firstIndex + 1`.

**The subtlety your code correctly handles:** if you used raw indices without normalization, a deep, lopsided tree could make these indices grow *exponentially*, risking integer overflow well before you'd run out of actual tree depth. Your fix — `currIndex = pair.index - min` — re-bases every level's indices to start near zero (by subtracting that level's minimum index) before computing children's indices for the *next* level. This keeps numbers small while preserving the relative gaps that width actually depends on.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `int min = q.peek().index;` | Grab this level's smallest raw index (needed for normalization) |
| `currIndex = pair.index - min;` | Re-base every node in this level relative to that level's start |
| `if (i==0) first = currIndex;` / `if (i==n-1) last = currIndex;` | Since the queue processes left-to-right, the first popped node is leftmost, the last popped is rightmost |
| `2*currIndex+1` / `2*currIndex+2` | Standard complete-binary-tree array indexing for children, computed from the *normalized* index so it stays small |
| `maxWidth = Math.max(maxWidth, last-first+1);` | Width formula: count of positions spanned, inclusive |

## 7. Dry Run
| Level | Nodes (raw idx before norm) | min | Normalized indices | first | last | width |
|---|---|---|---|---|---|---|
| 0 | 1(idx0) | 0 | 0 | 0 | 0 | 1 |
| 1 | 2(idx1), 3(idx2) | 1 | 2→0, 3→1 | 0 | 1 | 2 |
| 2 | 4(idx: 2·0+1=1), 5(idx: 2·0+2=2), 6(idx: 2·1+1=3) | 1 | 4→0, 5→1, 6→2 | 0 | 2 | **3** |

Result: **3** (max width, achieved at level 2)

## 8. Test Cases
| Input | Output |
|---|---|
| Tree above | 3 |
| Single node | 1 |
| A tree with one very "spread out" level (e.g. only leftmost and rightmost children populated at some depth, everything between missing) | Correctly counts the full span including the gap |

## Better / Alternative Approach
Already the standard optimal O(n) solution with the necessary overflow-safety fix already applied. This is the expected/best solution for this problem.

---

# Problem 28 — Children Sum Property

## 1. What is the problem?
GFG classic. Modify the tree **in place** so every node's value equals the sum of its immediate children's values — a structural transformation problem, not a search/traversal-only one.

## 2. Example Tree
Best demonstrated with a small custom tree rather than the shared example (since this problem *mutates* values, the general tree isn't a clean illustration):
```text
      50
     /  \
    7    2
```

## 3. My Code
```java
private static void ChildrenSumProperty(Node root) {
    if (root == null) { return; }
    int child = 0;
    if (root.left != null) { child += root.left.data; }
    if (root.right != null) { child += root.right.data; }
    if (child >= root.data) { root.data = child; }
    else {
        if (root.left != null) { root.left.data = root.data; }
        if (root.right != null) { root.right.data = root.data; }
    }
    ChildrenSumProperty(root.left);
    ChildrenSumProperty(root.right);
    int x = 0;
    if (root.left != null) { x += root.left.data; }
    if (root.right != null) { x += root.right.data; }
    if (root.left != null || root.right != null) { root.data = x; }
}
```

## 4. Issues / Bugs / Edge Cases
None functionally — but this is genuinely the hardest method in your file to reason about from reading alone, because it mutates the tree in **three separate passes within a single call**: before recursing, during recursing, and after recursing. Worth a print-based trace (see below) whenever you revisit this one.

## 5. Intuition & Why Three Phases Per Node
1. **Compare & push down (before recursing):** compute the sum of existing children. If that sum already ≥ the current node's value, the node absorbs it (`root.data = child`). Otherwise, the current node's value is "too big" for its children to support — so it **pushes its own value down** onto whichever children exist, forcing them to match. This ensures no child is ever smaller than what the structure requires.
2. **Recurse (`ChildrenSumProperty(root.left)`, `...right`):** the children (possibly just overwritten in step 1) now enforce the same property on *their own* children, recursively, top-down.
3. **Pull up & correct (after recursing):** because step 2 may have changed the children's values again (they might have absorbed even larger sums from further down), the current node's value could now be stale. So it recomputes the children's sum one final time and updates itself — this is the bottom-up correction that makes the final result globally consistent, not just locally consistent at the moment of the first pass.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `child = sum of existing children` | Snapshot before any mutation this call |
| `if (child >= root.data) root.data = child;` | Node grows to match its children |
| `else { push root.data down to children }` | Children grow to match the node (since they were too small) |
| `ChildrenSumProperty(root.left/right);` | Recurse — same logic applies deeper, possibly changing what the children now hold |
| final block recomputing `x` and reassigning `root.data` | **Bottom-up fix-up** — children may have grown further during recursion, so re-sync the current node one more time |

## 7. Dry Run — `ChildrenSumProperty(root)` on the small tree above
```text
Call on 50: child = 7+2 = 9. 9 < 50 → push down: 7.data=50, 2.data=50.
Recurse left (now-50 node, was 7): no children → phase 1/2 do nothing, phase 3: x=0, no children → skip reassignment (root.left/right both null on this leaf)
Recurse right (now-50 node, was 2): same, leaf, nothing changes
Back at root: x = 50(left) + 50(right) = 100. root.left or root.right is non-null → root.data = 100.
```
Final tree: root=100, left=50, right=50 — every node's value now equals the sum of its children (leaves trivially satisfy this since they have no children to sum).

## 8. Test Cases
| Input | After transformation |
|---|---|
| `50 / 7, 2` (above) | `100 / 50, 50` |
| Single node `[5]` | Unchanged — `5` (no children to enforce anything against) |
| Already-valid tree, e.g. `10 / 4, 6` | Unchanged — `10` still equals `4+6` |

## Better / Alternative Approach
This IS the standard two-pass (push-down + pull-up) solution — the accepted approach for this GFG problem. No simpler correct alternative exists, since you genuinely need both a downward push (to fix children too small) and an upward correction (to keep ancestors in sync after children change).

---

# Problem 29 — Distance K from Target Node

## 1. What is the problem?
LeetCode 863. Given a target node and a distance `k`, return the values of all nodes exactly `k` edges away — where "distance" can travel through the parent, not just down through children (a plain binary tree has no parent pointers, so this is the real challenge).

## 2. Example Tree
Same tree.

## 3. My Code
```java
private static void markParents(Map<Node, Node> parentMap, Node root) {
    Queue<Node> q = new ArrayDeque<>();
    q.offer(root);
    while (!q.isEmpty()) {
        Node node = q.poll();
        if (node.left != null) { parentMap.put(node.left, node); q.offer(node.left); }
        if (node.right != null) { parentMap.put(node.right, node); q.offer(node.right); }
    }
}

private static List<Integer> distanceK(Node root, Node target, int k) {
    Map<Node, Node> parentMap = new HashMap<>();
    markParents(parentMap, root);
    Map<Node, Boolean> visited = new HashMap<>();
    Queue<Node> q = new ArrayDeque<>();
    q.offer(target);
    visited.put(target, true);
    int level = 0;
    while (!q.isEmpty()) {
        int size = q.size();
        if (level == k) { break; }
        level++;
        for (int i = 0; i < size; i++) {
            Node node = q.poll();
            if (node.left != null && visited.get(node.left) == null) { q.offer(node.left); visited.put(node.left, true); }
            if (node.right != null && visited.get(node.right) == null) { q.offer(node.right); visited.put(node.right, true); }
            Node parent = parentMap.get(node);
            if (parent != null && visited.get(parent) == null) { q.offer(parent); visited.put(parent, true); }
        }
    }
    List<Integer> ans = new ArrayList<>();
    while (!q.isEmpty()) { ans.add(q.poll().data); }
    return ans;
}
```

## 4. Issues / Bugs / Edge Cases
None — correctly handles `visited` tracking to prevent infinite loops (without it, you'd bounce forever between a node and its parent).

## 5. Intuition & Why `markParents` Changes Everything
A binary tree only has downward pointers (`left`, `right`) — there's structurally no way to go "up" to a parent. `markParents` fixes that with a simple BFS that records, for every node, who its parent is. Once you have that map, **the tree effectively becomes an undirected graph** you can move through in any direction (left, right, OR up via the parent map) — and "distance K from a node in a graph" is just... a plain BFS shortest-path problem. This reframing (tree → graph via parent pointers) is the single most important idea in this method, and it's reused identically in Problem 30.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `markParents(parentMap, root);` | Build the "undo" direction the tree doesn't naturally have |
| `q.offer(target); visited.put(target, true);` | Start BFS from the target, mark it visited immediately (never revisit it) |
| `if (level == k) break;` | Stop expanding once we've reached the target distance — whatever's in the queue right now IS the answer |
| three `if` blocks inside the loop | Try expanding in all three possible directions: left child, right child, parent — each guarded by `visited` to avoid cycles |
| final `while (!q.isEmpty())` | Drain whatever's left in the queue at exactly distance k into the answer list |

## 7. Dry Run — `distanceK(root, node2, 2)`
```text
Start: q=[2], visited={2}, level=0
level(0)==k(2)? no. level becomes 1.
  Process 2: left=4(unvisited)→push,mark; right=5(unvisited)→push,mark; parent=1(unvisited)→push,mark
  q = [4,5,1]
level(1)==k(2)? no. level becomes 2.
  Process 4: no children, parent=2(visited)→skip
  Process 5: no children, parent=2(visited)→skip
  Process 1: left=2(visited)→skip, right=3(unvisited)→push,mark, no parent
  q = [3]
level(2)==k(2)? YES → break
Drain q: ans = [3]
```
Wait — let's double check against the expected answer (nodes at distance 2 from node 2 should be 1's "far side," i.e. node 3, since paths from 2 are: 2→4 (dist 1), 2→5 (dist1), 2→1 (dist1)→3(dist2), 2→1→(nothing else)). Result: **`[3]`** ✅ correct — 3 is exactly 2 edges from 2, via 2→1→3.

## 8. Test Cases
| Input | target | k | Output |
|---|---|---|---|
| Tree above | node 1 (root) | 2 | `[4, 5, 6]` |
| Tree above | node 2 | 2 | `[3]` |
| Tree above | node 4 | 0 | `[4]` (distance 0 = itself) |
| Tree above | node 4 | 10 | `[]` (nothing that far away exists — queue empties before reaching level 10) |

## Better / Alternative Approach
This is the standard, optimal O(n) approach for this problem (parent-map + BFS). No meaningfully better approach exists for the general binary tree case.

---

# Problem 30 — Burning Tree (Time to Burn Entire Tree)

## 1. What is the problem?
Fire starts at a given target node and spreads to all directly connected nodes (left child, right child, AND parent) every minute. How many minutes until the entire tree has burned?

## 2. Example Tree
Same tree.

## 3. My Code
```java
private static int BurningTree(Node root, Node target) {
    Map<Node, Node> parentMap = new HashMap<>();
    markParents(parentMap, root);
    Map<Node, Boolean> visited = new HashMap<>();
    Queue<Node> q = new ArrayDeque<>();
    q.offer(target);
    visited.put(target, true);
    int time = 0;
    while (!q.isEmpty()) {
        int size = q.size();
        boolean burned = false;
        for (int i = 0; i < size; i++) {
            Node node = q.poll();
            if (node.left != null && visited.get(node.left) == null) { q.offer(node.left); visited.put(node.left, true); burned = true; }
            if (node.right != null && visited.get(node.right) == null) { q.offer(node.right); visited.put(node.right, true); burned = true; }
            Node parent = parentMap.get(node);
            if (parent != null && visited.get(parent) == null) { q.offer(parent); visited.put(parent, true); burned = true; }
        }
        if (burned) { time++; }
    }
    return time;
}
```

## 4. Issues / Bugs / Edge Cases
None — the `burned` flag correctly ensures `time` only increments on minutes where fire *actually spread* to at least one new node, not on every loop iteration regardless.

## 5. Intuition & Why This Is Almost Identical to Problem 29
Same exact `markParents` + BFS-in-all-directions pattern as distance K — but instead of stopping at a fixed `k`, you run the BFS until the **queue is completely empty** (everything reachable has burned), counting how many "rounds" (minutes) had at least one new node catch fire. This is a very common pattern for **multi-source-style spreading/infection problems** — "rotting oranges" on LeetCode is structurally the same idea (BFS levels literally represent elapsed time).

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `boolean burned = false;` | Reset each round — assume nothing new catches fire until proven otherwise |
| three `if` blocks | Same "try all three directions" logic as distance K |
| `burned = true;` (inside each `if`) | Fires whenever a genuinely new node gets marked visited this round |
| `if (burned) time++;` | Only count this round as an elapsed minute if fire *actually spread* — the final round where nothing new ignites doesn't add extra time |

## 7. Dry Run — `BurningTree(root, root)` (fire starts at node 1)
```text
q=[1], visited={1}, time=0
Round: process 1 → left=2(new)→push,burned=true; right=3(new)→push,burned=true; no parent
  burned=true → time=1
  q=[2,3]
Round: process 2 → left=4(new), right=5(new), parent=1(visited)→skip
       process 3 → left=6(new), right=null, parent=1(visited)→skip
  burned=true → time=2
  q=[4,5,6]
Round: process 4,5,6 → no children, parent(s) already visited → nothing new
  burned=false → time stays 2
  q=[] → loop ends
```
Result: **2** minutes — matches the earlier hand-trace in your notes (fire reaches every node by minute 2).

## 8. Test Cases
| Input | target | Output |
|---|---|---|
| Tree above | root (1) | 2 |
| Tree above | node 4 (a leaf, farthest corner) | 3 (must travel 4→2→1→3→6, worst case) |
| Single node | that node | 0 (nothing else to burn) |

## Better / Alternative Approach
Already the standard, optimal O(n) multi-directional BFS solution — this is the expected approach for "spreading through a tree treated as a graph" problems.

---

# Problem 31 — Count Nodes in a Complete Binary Tree (O(log²n))

## 1. What is the problem?
LeetCode 222. Count all nodes in a tree that's guaranteed to be a **complete** binary tree (every level fully filled except possibly the last, which fills left-to-right) — faster than the naive O(n) full traversal from Problem 9.

## 2. Example Tree
Same tree (which happens to BE complete: last level fills left-to-right, 4 and 5 before 6).

## 3. My Code
```java
private static int CountNodeCBT(Node root) {
    if (root == null) { return 0; }
    int lh = heightLeft(root);
    int rh = heightRight(root);
    if (lh == rh) { return (1 << lh) - 1; }
    return 1 + CountNodeCBT(root.left) + CountNodeCBT(root.right);
}

private static int heightRight(Node node) {
    int c = 0;
    while (node != null) { c++; node = node.right; }
    return c;
}

private static int heightLeft(Node node) {
    int c = 0;
    while (node != null) { c++; node = node.left; }
    return c;
}
```

## 4. Issues / Bugs / Edge Cases
None — correct implementation of the classic "detect perfect subtree via leftmost/rightmost height comparison" trick.

## 5. Intuition & Why This Beats O(n)
Because the tree is **guaranteed complete**, you can cheaply detect when a subtree happens to be **perfect** (every level completely full):
- Walk always-left from a node to get `heightLeft` (how deep the leftmost path goes).
- Walk always-right from the same node to get `heightRight`.
- **If they're equal**, that subtree MUST be perfect (this is only guaranteed because completeness rules out the "irregular" shapes where leftmost/rightmost heights could match without the tree actually being full) — so its node count is `2^height - 1`, a closed-form formula, computed in O(1) with no further recursion needed.
- **If they differ**, the subtree isn't perfect (its last level is only partially filled) — recurse into both children normally and add 1 for the current node.

The efficiency comes from the guarantee that, at every node, **at least one side collapses to the fast O(1) formula case** almost immediately (since completeness bounds how "uneven" the tree can be) — giving O(log²n) overall instead of O(n): O(log n) levels of recursion, each doing O(log n) work to compute the two heights.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `lh = heightLeft(root); rh = heightRight(root);` | O(log n) each — just walking one path down |
| `if (lh == rh) return (1 << lh) - 1;` | Perfect subtree detected → instant closed-form answer, `2^h - 1` |
| `return 1 + CountNodeCBT(left) + CountNodeCBT(right);` | Not perfect → fall back to normal recursive counting, but this branch is only taken near the tree's "irregular" edge, not throughout |

## 7. Dry Run
```text
CountNodeCBT(1): heightLeft(1→2→4)=3, heightRight(1→3)=2. 3≠2 → recurse normally.
  CountNodeCBT(2): heightLeft(2→4)=2, heightRight(2→5)=2. EQUAL! → perfect subtree → (1<<2)-1 = 3
     (instantly returns 3, representing nodes 2, 4, 5 — no further recursion needed)
  CountNodeCBT(3): heightLeft(3→6)=2, heightRight(3)=1. 2≠1 → recurse normally.
     CountNodeCBT(6): heightLeft=1, heightRight=1 → equal → (1<<1)-1 = 1
     CountNodeCBT(null) [3's right child] = 0
     → 1 + 1 + 0 = 2
  → 1(root) + 3(from node 2) + 2(from node 3) = 6
```
Result: **6** — matches the actual node count, computed with far fewer total operations than visiting all 6 nodes individually node-by-node in the "perfect" left branch.

## 8. Test Cases
| Input | Output |
|---|---|
| Tree above | 6 |
| Perfect tree of height 3 (7 nodes) | 7, resolved in a single O(1) formula call at the root |
| Single node | 1 |
| Empty tree | 0 |

## Better / Alternative Approach
This already IS the optimized approach (O(log²n)) versus Problem 9's O(n) — nothing further to improve for the complete-binary-tree-guaranteed case.

---

# Problem 32 — Build Tree from Preorder + Inorder

## 1. What is the problem?
LeetCode 105. You're given a tree's preorder and inorder traversal arrays and must reconstruct the *exact original tree* — not just any tree with those traversals, but the unique one that produces both.

## 2. Example Tree
Reconstructing the standard example tree:
```text
        1
       / \
      2   3
     / \  /
    4  5 6
```
`preorder = [1,2,4,5,3,6]`, `inorder = [4,2,5,1,6,3]`

## 3. My Code
```java
private static Node buildTreePreOrderInorder(int[] preorder, int[] inorder) {
    HashMap<Integer,Integer> map = new HashMap<>();
    for (int i = 0; i < inorder.length; i++) { map.put(inorder[i], i); }
    Node root = build(preorder, 0, preorder.length-1, inorder, 0, inorder.length-1, map);
    return root;
}

private static Node build(int[] preorder, int preStart, int preEnd, int[] inorder, int inStart, int inEnd, HashMap<Integer, Integer> map) {
    if (preStart > preEnd || inStart > inEnd) { return null; }
    Node root = new Node(preorder[preStart]);
    int inRoot = map.get(root.data);
    int numsLeft = inRoot - inStart;
    root.left = build(preorder, preStart+1, preStart+numsLeft, inorder, inStart, inRoot-1, map);
    root.right = build(preorder, preStart+numsLeft+1, preEnd, inorder, inRoot+1, inEnd, map);
    return root;
}
```

## 4. Issues / Bugs / Edge Cases
None — correctly assumes no duplicate values in the tree (a standard assumption for this problem, since duplicates would break the `HashMap<value, index>` lookup). Correctly guards against empty ranges via `preStart > preEnd || inStart > inEnd`.

## 5. Intuition & Why This Approach
Two facts make reconstruction possible:
1. **Preorder's first element is always the current subtree's root** (root is visited before either child in preorder).
2. **Inorder splits cleanly around the root** — everything to the left of the root's position in inorder belongs to the left subtree, everything to the right belongs to the right subtree.

So: read `preorder[preStart]` as root, find where that value sits in `inorder` (via the pre-built `HashMap` for O(1) lookup — without it, searching `inorder` linearly every call would make the whole thing O(n²)), compute how many elements belong to the left subtree (`numsLeft = inRoot - inStart`), then recursively carve out the matching index ranges in both arrays for the left and right subtree calls.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `map.put(inorder[i], i)` (setup loop) | One-time O(n) cost so every future root-lookup is O(1) instead of O(n) |
| `Node root = new Node(preorder[preStart]);` | The very first element of the current preorder range is always this subtree's root |
| `int inRoot = map.get(root.data);` | Where does that root sit in the inorder array? Everything before it (in this range) is the left subtree |
| `int numsLeft = inRoot - inStart;` | Size of the left subtree — needed to correctly slice the preorder array too |
| `build(..., preStart+1, preStart+numsLeft, ...)` | Left subtree's preorder range starts right after the root and spans exactly `numsLeft` elements |
| `build(..., preStart+numsLeft+1, preEnd, ...)` | Right subtree gets whatever's left over |

## 7. Dry Run
```text
build(pre=[1,2,4,5,3,6], 0,5, in=[4,2,5,1,6,3], 0,5):
  root = 1 (pre[0]). inRoot = index of 1 in inorder = 3. numsLeft = 3-0 = 3.
  left  = build(pre, 1,3, in, 0,2)   → subtree for [2,4,5] / inorder[4,2,5]
  right = build(pre, 4,5, in, 4,5)   → subtree for [3,6] / inorder[6,3]

  Left call: root=2 (pre[1]). inRoot(2)=1 in [4,2,5]→ actual index 1. numsLeft=1-0=1.
    left  = build(pre,2,2,in,0,0) → root=4, no children → leaf
    right = build(pre,3,3,in,2,2) → root=5, no children → leaf
  → node 2 with left=4, right=5 ✓

  Right call: root=3 (pre[4]). inRoot(3)=index in [6,3] → index 5. numsLeft=5-4=1.
    left  = build(pre,5,5,in,4,4) → root=6, no children → leaf
    right = build(pre,6,5,...) → preStart(6)>preEnd(5) → null
  → node 3 with left=6, right=null ✓
```
Reconstructed tree matches the original exactly.

## 8. Test Cases
| preorder | inorder | Reconstructed tree |
|---|---|---|
| `[1,2,4,5,3,6]` | `[4,2,5,1,6,3]` | The tree above (matches exactly) |
| `[5]` | `[5]` | Single node `5` |
| `[]` | `[]` | `null` (empty tree — both ranges immediately fail the `preStart>preEnd` check) |
| `[1,2,3]` | `[3,2,1]` | Left-skewed chain: `1←2←3` (every root has only a left child) |
| `[1,2,3]` | `[1,2,3]` | Right-skewed chain: `1→2→3` (every root has only a right child) |

## Better / Alternative Approach
This is the standard, optimal O(n) solution (thanks to the HashMap lookup) — this is exactly the expected approach for LeetCode 105. No meaningful improvement exists beyond this.

---

# Problem 33 — Build Tree from Postorder + Inorder

## 1. What is the problem?
LeetCode 106 — same reconstruction goal as Problem 32, but given **postorder + inorder** instead of preorder + inorder.

## 2. Example Tree
Same target tree as Problem 32:
`postorder = [4,5,2,6,3,1]`, `inorder = [4,2,5,1,6,3]`

## 3. My Code
```java
private static Node buildTreePostOrderInorder(int[] postorder, int[] inorder) {
    HashMap<Integer,Integer> map = new HashMap<>();
    for (int i = 0; i < inorder.length; i++) { map.put(inorder[i], i); }
    Node root = buildPostOrder(postorder, 0, postorder.length-1, inorder, 0, inorder.length-1, map);
    return root;
}

private static Node buildPostOrder(int[] postorder, int a, int b, int[] inorder, int x, int y, HashMap<Integer, Integer> map) {
    if (a > b || x > y) { return null; }
    Node root = new Node(postorder[b]);
    int k = map.get(root.data);
    int leftNums = k - x;
    root.left = buildPostOrder(postorder, a, a+leftNums-1, inorder, x, k-1, map);
    root.right = buildPostOrder(postorder, a+leftNums, b-1, inorder, k+1, y, map);
    return root;
}
```

## 4. Issues / Bugs / Edge Cases
None — the index arithmetic correctly accounts for postorder's Left-Right-**Root** ordering when slicing the array for children.

## 5. Intuition & Why It's Nearly Identical to Problem 32
The **only** conceptual change: in postorder, the root is the **last** element of the current range (`postorder[b]`), not the first — because postorder visits both children before itself. Everything else — using the HashMap to split `inorder` around the root, then recursing on the correctly-sized index ranges — follows the same logic as Problem 32.

**The one genuinely tricky part** (and a common source of off-by-one bugs if you rederive this from scratch): since postorder is Left-Right-Root, when slicing `postorder` for children, the **left subtree occupies `postorder[a .. a+leftNums-1]`** and the **right subtree occupies `postorder[a+leftNums .. b-1]`** (the root itself, at index `b`, is excluded from both).

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `Node root = new Node(postorder[b]);` | Last element of current postorder range = root (postorder visits root last) |
| `int k = map.get(root.data);` | Root's position in inorder — splits left/right subtrees |
| `int leftNums = k - x;` | Size of left subtree, same idea as Problem 32 |
| `buildPostOrder(postorder, a, a+leftNums-1, ...)` | Left subtree's postorder slice — starts at `a`, spans `leftNums` elements |
| `buildPostOrder(postorder, a+leftNums, b-1, ...)` | Right subtree gets everything else, up to (but excluding) the root itself at `b` |

## 7. Dry Run
```text
buildPostOrder(post=[4,5,2,6,3,1], 0,5, in=[4,2,5,1,6,3], 0,5):
  root = 1 (post[5]). k = index of 1 in inorder = 3. leftNums = 3-0 = 3.
  left  = buildPostOrder(post, 0,2, in, 0,2)  → covers post[4,5,2] / in[4,2,5]
  right = buildPostOrder(post, 3,4, in, 4,5)  → covers post[6,3] / in[6,3]

  Left call: root = 2 (post[2], last of [4,5,2]). k=index of 2 in [4,2,5]=1. leftNums=1-0=1.
    left  = buildPostOrder(post,0,0,in,0,0) → root=4, leaf
    right = buildPostOrder(post,1,1,in,2,2) → root=5, leaf
  → node 2 with left=4, right=5 ✓

  Right call: root = 3 (post[4], last of [6,3]). k=index of 3 in [6,3]=5. leftNums=5-4=1.
    left  = buildPostOrder(post,3,3,in,4,4) → root=6, leaf
    right = buildPostOrder(post,4,3,...) → a(4)>b(3) → null
  → node 3 with left=6, right=null ✓
```
Reconstructed tree matches exactly, same as Problem 32.

## 8. Test Cases
| postorder | inorder | Reconstructed tree |
|---|---|---|
| `[4,5,2,6,3,1]` | `[4,2,5,1,6,3]` | The tree above |
| `[5]` | `[5]` | Single node `5` |
| `[]` | `[]` | `null` |
| `[3,2,1]` | `[3,2,1]` | Left-skewed chain `1←2←3` |
| `[3,2,1]` | `[1,2,3]` | Right-skewed chain `1→2→3` |

## Better / Alternative Approach
Already the standard optimal O(n) solution (HashMap lookup avoids O(n²)). This is the expected approach for LeetCode 106.

---

# Problem 34 — Serialize a Binary Tree

## 1. What is the problem?
LeetCode 297 (serialize half). Convert an in-memory tree into a single string so it can be saved to disk, sent over a network, etc.

## 2. Example Tree
```text
        1
       / \
      2   3
     / \  /
    4  5 6
```

## 3. My Code
```java
private static String serialize(Node root) {
    if (root == null) { return ""; }
    Queue<Node> q = new LinkedList<>();
    StringBuilder sb = new StringBuilder();
    q.add(root);
    while (!q.isEmpty()) {
        Node node = q.poll();
        if (node != null) {
            sb.append(node.data + " ");
            q.add(node.left);
            q.add(node.right);
        } else {
            sb.append("n ");
        }
    }
    return sb.toString();
}
```

## 4. Issues / Bugs / Edge Cases
None. Correctly pushes `null` children onto the queue on purpose (rather than skipping them like a normal BFS would) — this is exactly what makes the encoding unambiguous for `deserialize` later.

## 5. Intuition & Why Encoding Nulls Explicitly Matters
A normal BFS skips `null` children — but that loses information! If you only wrote down real values, you'd have no way to tell, from the string alone, *which* positions had a left/right child and which didn't. Your solution's fix: use **level-order (BFS) with explicit null markers** (`"n"`) — every position, real or empty, gets written down. That's what lets `deserialize` walk the string back in the exact same order and know unambiguously where every child belongs.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `q.add(root)` | Standard BFS seed |
| `if (node != null) { append value; add both children (even if null!) }` | Real node → record its value, and queue its children regardless of whether they exist |
| `else { append "n"; }` | This position was a null child — record that explicitly instead of silently skipping it |

## 7. Dry Run
```text
q=[1]. Poll 1 (not null): sb="1 ". Push 1.left(2), 1.right(3). q=[2,3]
Poll 2 (not null): sb="1 2 ". Push 2.left(4), 2.right(5). q=[3,4,5]
Poll 3 (not null): sb="1 2 3 ". Push 3.left(6), 3.right(null). q=[4,5,6,null]
Poll 4 (not null): sb="1 2 3 4 ". Push 4.left(null), 4.right(null). q=[5,6,null,null,null]
Poll 5 (not null): sb="1 2 3 4 5 ". Push null, null. q=[6,null,null,null,null,null]
Poll 6 (not null): sb="1 2 3 4 5 6 ". Push null, null. q=[null×7]
Poll null → sb += "n ". (repeats 7 times for all the queued nulls)
```
Final string: `"1 2 3 4 5 6 n n n n n n n "` (7 trailing `n`s — one for each null child pushed: 3's right, 4's left, 4's right, 5's left, 5's right, 6's left, 6's right).

## 8. Test Cases
| Input | Output |
|---|---|
| Tree above | `"1 2 3 4 5 6 n n n n n n n "` |
| Empty tree (`root=null`) | `""` |
| Single node `[9]` | `"9 n n "` |

## Better / Alternative Approach
Already a correct, standard BFS-based serialization — equally valid alternative: a preorder/DFS-based serialization (recursively write `"val "` or `"n "` before recursing into children) achieves the same unambiguous round-trip property with a slightly different string layout. Neither is "better" — BFS-based (yours) and DFS-based are both accepted standard solutions to LeetCode 297.

---

# Problem 35 — Deserialize a Binary Tree

## 1. What is the problem?
LeetCode 297 (deserialize half) — the exact reverse of Problem 34: given the encoded string, rebuild the original tree structure.

## 2. Example Tree
Reconstructing the same tree from `"1 2 3 4 5 6 n n n n n n n "`.

## 3. My Code
```java
private static Node deserialize(String str) {
    if (str.isEmpty()) { return null; }
    Queue<Node> q = new LinkedList<>();
    String[] arr = str.trim().split(" ");
    int k = Integer.parseInt(arr[0]);
    Node root = new Node(k);
    q.add(root);
    for (int i = 1; i < arr.length; i++) {
        Node node = q.poll();
        if (!arr[i].equals("n")) {
            node.left = new Node(Integer.parseInt(arr[i]));
            q.add(node.left);
        }
        if (++i < arr.length && !arr[i].equals("n")) {
            node.right = new Node(Integer.parseInt(arr[i]));
            q.add(node.right);
        }
    }
    return root;
}
```

## 4. Issues / Bugs / Edge Cases
- Correct, but the `++i` **inside the `if` condition** (second check) is a subtle piece of code worth flagging explicitly even though it's not a bug: it advances the shared loop variable `i` as a side effect of evaluating the condition. This is intentional (each queue-poll needs to consume *two* array slots — left then right — per iteration of the outer `for`), but it's easy to misread on a quick skim. Worth a comment if you revisit this in 2 weeks.
- Correctly handles the empty-string case (`str.isEmpty() → return null`), matching `serialize`'s own empty-tree encoding.

## 5. Intuition & Why It Mirrors `serialize` Exactly
Because `serialize` wrote every position — real value or `"n"` — in strict BFS order, `deserialize` can consume the string in that *exact same order* and know unambiguously where each token belongs. The pattern: poll a node from the queue, its **next two unconsumed tokens** in the array are its left and right child respectively (skip creating a child if the token is `"n"`), and only queue newly-created (non-null) nodes for further processing — mirroring exactly how `serialize` only follows real nodes deeper.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `arr[0]` → root | First token is always the root's value (never `"n"`, since the tree is guaranteed non-empty here) |
| `node = q.poll();` | Take the next node whose children need to be filled in, in the same order they were originally serialized |
| `if (!arr[i].equals("n"))` | Real value → create the left child, queue it for its own children to be processed later |
| `if (++i < arr.length && !arr[i].equals("n"))` | Advance to the *next* token (this is the right child's slot) and do the same check |

## 7. Dry Run
```text
arr = ["1","2","3","4","5","6","n","n","n","n","n","n","n"]
root = Node(1). q=[1]. i=1.

i=1: node=poll()=1. arr[1]="2"→ node.left=Node(2), q=[2]. i becomes 2 (via ++i): arr[2]="3"→ node.right=Node(3), q=[2,3].
i=3 (loop's own i++): node=poll()=2. arr[3]="4"→ node.left=Node(4), q=[3,4]. i→4: arr[4]="5"→ node.right=Node(5), q=[3,4,5].
i=5: node=poll()=3. arr[5]="6"→ node.left=Node(6), q=[4,5,6]. i→6: arr[6]="n"→ no right child.
i=7: node=poll()=4. arr[7]="n"→ no left. i→8: arr[8]="n"→ no right.
i=9: node=poll()=5. arr[9]="n"→ no left. i→10: arr[10]="n"→ no right.
i=11: node=poll()=6. arr[11]="n"→ no left. i→12: arr[12]="n"→ no right.
loop ends (i would become 13, exceeding arr.length=13)
```
Reconstructed tree: root=1, left=2(left=4,right=5), right=3(left=6) — exactly the original tree. ✅

## 8. Test Cases
| Input string | Output |
|---|---|
| `"1 2 3 4 5 6 n n n n n n n "` | The tree above, exactly reconstructed |
| `""` | `null` |
| `"9 n n "` | Single node `9` |
| Round-trip test: `deserialize(serialize(root))` | Structurally identical to the original `root` — the standard way to validate this pair of methods |

## Better / Alternative Approach
Already the standard, correct BFS-based deserialization matching your BFS-based serialize. If you'd used a DFS/preorder-based `serialize` instead, `deserialize` would need to match with a recursive/preorder-based reconstruction — the two methods must always use the *same* traversal style as each other, which yours correctly does.

---

# Problem 36 — Morris Inorder Traversal ⭐

## 1. What is the problem?
Inorder traversal using **O(1) extra space** — no recursion (no call stack), no explicit `Stack<Node>`. This is the technique you specifically deep-dived, and it's the one worth the dedicated interactive artifact (already built separately) since reading the code alone genuinely undersells how it works.

## 2. Example Tree
```text
        1
       / \
      2   3
     / \  /
    4  5 6
```

## 3. My Code
```java
private static void morrisInorder(Node root) {
    Node curr = root;
    while (curr != null) {
        if (curr.left == null) {
            System.out.println(curr.data);
            curr = curr.right;
        } else {
            Node pre = curr.left;
            while (pre.right != null && pre.right != curr) {
                pre = pre.right;
            }
            if (pre.right == null) {
                pre.right = curr;
                curr = curr.left;
            } else {
                pre.right = null;
                System.out.println(curr.data);
                curr = curr.right;
            }
        }
    }
}
```

## 4. Issues / Bugs / Edge Cases
None — this is a textbook-correct Morris inorder implementation, including the crucial thread-removal step that restores the tree to its original shape once the traversal is done.

## 5. Intuition & Why This Approach (the big idea)
Normally, once you descend left, you need *some* mechanism to find your way back up to continue with the right side — recursion gets that "way back" for free from the call stack. Morris traversal instead **temporarily borrows the tree's own unused `null` right-pointers** to store that "way back" itself. This is called **threading**.

For any node with a left child:
1. Find its **inorder predecessor** — the rightmost node in its left subtree (the node that should be visited immediately *before* this one).
2. If that predecessor's `.right` is `null` (unused) → **plant a thread**: point it back to the current node, then dive left.
3. Later, when the traversal naturally arrives back at that predecessor via the thread, `pre.right == curr` (not `null`) — that's the algorithm's signal: **"I've already fully explored my left subtree — this is a return trip, not a first visit."** At that point: erase the thread (restoring the tree exactly as it was), visit the current node, and move right.

If a node has **no left child** at all, there's nothing to thread — visit it immediately and move right (same base-case idea as iterative inorder's "can't go left anymore" moment).

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `if (curr.left == null)` | Base case — nothing to thread, visit now |
| `pre = curr.left; while(pre.right != null && pre.right != curr) pre = pre.right;` | Walk to the rightmost node of the left subtree — **but stop early if you hit a thread pointing back to `curr`** (that's how a return trip gets detected without extra memory) |
| `if (pre.right == null) { pre.right = curr; curr = curr.left; }` | First visit to this subtree — plant the thread, dive left |
| `else { pre.right = null; visit(curr); curr = curr.right; }` | Return trip detected — clean up the thread, visit, move on |

## 7. Dry Run
*(Full 9-iteration trace with diagrams available in the companion interactive artifact — summarized here:)*

| Iteration | curr | Action | Output so far |
|---|---|---|---|
| 1 | 1 | Plant thread 5→1, descend to 2 | `[]` |
| 2 | 2 | Plant thread 4→2, descend to 4 | `[]` |
| 3 | 4 | No left child, visit, follow thread to 2 | `[4]` |
| 4 | 2 | Detect return (pre.right==curr), remove thread 4→2, visit, move to 5 | `[4,2]` |
| 5 | 5 | No left child, visit, follow thread to 1 | `[4,2,5]` |
| 6 | 1 | Detect return, remove thread 5→1, visit, move to 3 | `[4,2,5,1]` |
| 7 | 3 | Plant thread 6→3, descend to 6 | `[4,2,5,1]` |
| 8 | 6 | No left child, visit, follow thread to 3 | `[4,2,5,1,6]` |
| 9 | 3 | Detect return, remove thread 6→3, visit, curr becomes null → loop ends | `[4,2,5,1,6,3]` |

Final printed output: `4 2 5 1 6 3` — matches standard inorder exactly, and the tree is left in its original, unthreaded shape.

## 8. Test Cases
| Input | Output printed | Tree shape after traversal |
|---|---|---|
| Tree above | `4 2 5 1 6 3` | Unchanged (fully restored — no leftover threads) |
| Empty tree | *(nothing printed)* | Unchanged (`null`) |
| Single node `[9]` | `9` | Unchanged |
| Left-skewed `1←2←3` | `3 2 1` | Unchanged |
| Right-skewed `1→2→3` (no left children anywhere) | `1 2 3` | Unchanged — every node hits the "no left child" base case directly, no threading ever happens |

## Better / Alternative Approach
This already IS the optimal solution for O(1)-space inorder traversal — no algorithmic improvement exists (you cannot do inorder in less than O(1) auxiliary space, so this is the ceiling of efficiency for this problem). The only alternative worth knowing is *when not to use it*: if the tree structure must remain completely untouched at all times during traversal (e.g. concurrent readers), the temporary mutation — even though it's fully reversed by the end — could be unsafe; in that specific scenario, the stack-based iterative inorder (Problem 4) would be the safer choice despite using more memory.

---

# Problem 37 — Flatten Binary Tree (Recursive, bottom-up splice)

## 1. What is the problem?
LeetCode 114. Rearrange the tree, in place, into a linked list that follows **preorder** — every node's `left` becomes `null`, and `right` chains to the next node in preorder sequence.

## 2. Example Tree
```text
        1
       / \
      2   3
     / \  /
    4  5 6
```

## 3. My Code
```java
private static Node FlattenTree(Node root) {
    if (root == null) { return null; }
    FlattenTree(root.left);
    FlattenTree(root.right);
    if (root.left != null) {
        Node rightTree = root.right;
        Node temp = root.left;
        while (temp != null && temp.right != null) { temp = temp.right; }
        temp.right = rightTree;
        root.right = root.left;
        root.left = null;
    }
    return root;
}
```

## 4. Issues / Bugs / Edge Cases
None functionally — correct output. Worth noting as a performance point (not a bug): the `while (temp.right != null)` walk-to-the-end happens at **every** node that has a left child, and on a left-skewed tree this walk can itself be O(n) at each level, making the *worst case* overall O(n²) rather than O(n). See Problem 38 for the O(n)-guaranteed fix.

## 5. Intuition & Why This Approach
Recurse into both children **first** — so by the time you handle the current node, both subtrees are *already* fully flattened into their own linked lists. Then it's pure pointer surgery:
1. Save `root.right` (the already-flattened right list) aside in `rightTree`.
2. Walk to the **end** of the already-flattened left list.
3. Attach the saved right list onto that end.
4. Move the whole combined list into `root.right`, and null out `root.left`.

This is a classic **"solve the subproblems first, then splice the results together"** divide-and-conquer shape — the same idea as the merge step in merge sort, just applied to trees instead of arrays.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `FlattenTree(root.left); FlattenTree(root.right);` | Both subtrees are now independently flattened lists (recursion handles this bottom-up) |
| `Node rightTree = root.right;` | Must save this *before* overwriting `root.right` below, or it would be lost |
| `while (temp.right != null) temp = temp.right;` | Find the tail of the now-flattened left list |
| `temp.right = rightTree;` | Splice: left-list-tail now points to the (already flattened) right list |
| `root.right = root.left; root.left = null;` | The combined chain becomes the new right pointer; left is cleared per the problem's requirement |

## 7. Dry Run
```text
FlattenTree(1):
  FlattenTree(2):
    FlattenTree(4): leaf, root.left==null → nothing to splice, returns 4 unchanged
    FlattenTree(5): leaf, nothing to splice, returns 5 unchanged
    root(2).left=4 (not null): rightTree=5 (2's original right). temp=4, 4.right==null so temp stays 4.
      temp.right = 5 → 4.right=5. root(2).right = 4. root(2).left = null.
      → node 2 is now: left=null, right=4→5 (a 3-node chain: 2→4→5)
  FlattenTree(3):
    FlattenTree(6): leaf, nothing to splice
    3.right was null already (no FlattenTree needed there)
    root(3).left=6 (not null): rightTree=null (3 had no right child). temp=6, stays 6.
      temp.right = null → 6.right=null (unchanged). root(3).right=6. root(3).left=null.
      → node 3 is now: left=null, right=6 (chain: 3→6)
  Back at root(1): root.left = 2 (now itself the head of chain 2→4→5, not null):
    rightTree = 3 (1's original right, now itself head of chain 3→6)
    temp = 2, walk to end of 2's chain: 2→4→5, temp ends at 5.
    temp.right = 3 → 5.right = 3 (which chains to 6)
    root(1).right = 2. root(1).left = null.
```
Final chain: `1 → 2 → 4 → 5 → 3 → 6` — this IS the preorder sequence of the original tree. ✅

## 8. Test Cases
| Input | Output chain (right-pointer only) |
|---|---|
| Tree above | `1 → 2 → 4 → 5 → 3 → 6` |
| Single node | `[node]` unchanged |
| Empty tree | `null` |
| Already a right-only chain (e.g. `1→2→3`) | Unchanged — no left children exist anywhere, so the splice logic never triggers |

## Better / Alternative Approach
See Problem 38 (`FlattenTreeReversePreOrder`) — same output, but avoids the "walk to the end of the left list" step entirely, giving a guaranteed O(n) instead of this version's worst-case O(n²) on skewed trees.

---

# Problem 38 — Flatten Binary Tree (Reverse Preorder, O(n))

## 1. What is the problem?
Same flattening goal as Problem 37, but avoiding its worst-case O(n²) — a cleverer approach that builds the resulting list **backwards**.

## 2. Example Tree
Same tree.

## 3. My Code
```java
Node prev = null;
private void FlattenTreeReversePreOrder(Node root) {
    if (root == null) { return; }
    FlattenTreeReversePreOrder(root.right);
    FlattenTreeReversePreOrder(root.left);
    root.right = prev;
    root.left = null;
    prev = root;
}
```

## 4. Issues / Bugs / Edge Cases
None. Relies on the instance field `prev` starting `null` — correct as long as this method is called once per fresh flatten (same caveat as `max` in Problem 13 if the same object were reused across multiple separate trees without resetting `prev`).

## 5. Intuition & Why Traversing in Reverse-Preorder Solves the O(n²) Problem
If you visit nodes in **reverse preorder** — meaning **Right → Left → Root**, the exact mirror image of normal preorder (Root → Left → Right) — and at each node simply set `root.right = prev` (whatever was processed immediately before this one) and `root.left = null`, then update `prev = root` for whoever gets called next... the correct flattened chain builds itself **automatically**. Why? Because reverse-preorder visits nodes in the **exact reverse** of the order you want them chained in. The very last node visited (by reverse-preorder) is the tree's true root, and by the time you process it, `prev` already holds the correct "rest of the chain" built up from everything already visited. This completely avoids Problem 37's "walk to the end of the left list" step — each node does O(1) work, giving a guaranteed O(n) overall.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `FlattenTreeReversePreOrder(root.right);` FIRST | Process the right subtree first — the opposite order from normal preorder |
| `FlattenTreeReversePreOrder(root.left);` SECOND | Then the left subtree |
| `root.right = prev;` | By now, `prev` holds whatever should come immediately *after* this node in the final chain (built from everything already processed) |
| `root.left = null;` | Required by the problem |
| `prev = root;` | This node now becomes what the *next-processed* (i.e., preceding, in the real chain) node should point to |

## 7. Dry Run — call order is Right→Left→Root, so the actual visiting sequence is:
```text
Call order: FlattenTreeReversePreOrder(1)
  → right(3)
      → right(null) → return
      → left(6): right(null)→return, left(null)→return. 6.right=prev(null). prev=6.
      → root=3: 3.right=prev(6) → 3.right=6. prev=3.
  → left(2)
      → right(5): 5.right=prev(3) → 5.right=3. prev=5.
      → left(4): 4.right=prev(5) → 4.right=5. prev=4.
      → root=2: 2.right=prev(4) → 2.right=4. prev=2.
  → root=1: 1.right=prev(2) → 1.right=2. prev=1.
```
Chain (following `.right` from 1): `1 → 2 → 4 → 5 → 3 → 6` — identical result to Problem 37, reached with zero "walk to the end" steps.

## 8. Test Cases
| Input | Output chain |
|---|---|
| Tree above | `1 → 2 → 4 → 5 → 3 → 6` |
| Single node | `[node]` |
| Empty tree | no-op, `prev` stays `null` |
| Left-skewed `1←2←3` (worst case for Problem 37) | `1 → 2 → 3`, and unlike Problem 37 this completes in true O(n), not O(n²) |

## Better / Alternative Approach
This already IS the optimized answer to Problem 37's inefficiency — genuinely one of the more elegant tricks in your whole file (reversing the visit order to make a running "already built" pointer do all the work). No further improvement needed.

---

# Problem 39 — Flatten Binary Tree (Iterative, Stack)

## 1. What is the problem?
Same flattening goal, done without recursion at all.

## 2. Example Tree
Same tree.

## 3. My Code
```java
private static void FlattenTreeIterative(Node root) {
    Stack<Node> st = new Stack<>();
    st.push(root);
    while (!st.isEmpty()) {
        Node curr = st.pop();
        if (curr.right != null) { st.push(curr.right); }
        if (curr.left != null) { st.push(curr.left); }
        if (!st.isEmpty()) { curr.right = st.peek(); }
        curr.left = null;
    }
}
```

## 4. Issues / Bugs / Edge Cases
- No `root == null` guard — `st.push(root)` would push `null`, and the first `st.pop()` gives `null`, crashing on `curr.right`. Worth a top-level guard: `if (root == null) return;`.
- Otherwise correct.

## 5. Intuition & Why This Approach
This is essentially **iterative preorder** (Problem 3's exact trick: push right then left so left pops first), but instead of collecting values into a result list, you rewire pointers *as you go*: after popping `curr`, whatever's now on top of the stack (`st.peek()`) is guaranteed to be the **next node in preorder** — so `curr.right = st.peek()` directly builds the correct chain in a single pass, and `curr.left = null` cleans up per the problem's requirement.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `push(curr.right)` then `push(curr.left)` | Same ordering trick as iterative preorder — ensures left pops before right |
| `if (!st.isEmpty()) curr.right = st.peek();` | The node about to be popped next IS the next node in preorder — wire the chain directly |
| `curr.left = null;` | Required cleanup, every iteration |

## 7. Dry Run
| Pop | Stack before | Push right? | Push left? | Stack after | curr.right set to |
|---|---|---|---|---|---|
| — | `[1]` (initial push) | — | — | `[1]` | — |
| 1 | `[]` | push 3 | push 2 | `[3,2]` | peek()=2 → `1.right=2` |
| 2 | `[3]` | push 5 | push 4 | `[3,5,4]` | peek()=4 → `2.right=4` |
| 4 | `[3,5]` | none | none | `[3,5]` | peek()=5 → `4.right=5` |
| 5 | `[3]` | none | none | `[3]` | peek()=3 → `5.right=3` |
| 3 | `[]` | push 6 | none | `[6]` | peek()=6 → `3.right=6` |
| 6 | `[]` | none | none | `[]` | stack empty → `6.right` untouched (stays `null`) |

Chain: `1 → 2 → 4 → 5 → 3 → 6` — matches Problems 37/38 exactly.

## 8. Test Cases
| Input | Output chain |
|---|---|
| Tree above | `1 → 2 → 4 → 5 → 3 → 6` |
| Single node | `[node]` |
| `root == null` | **currently crashes** (see bug) — should be a silent no-op |

## Better / Alternative Approach
Add the null guard noted above. Otherwise this is a solid, standard O(n) iterative solution — equally valid alongside Problems 37/38, useful specifically when you want to avoid recursion (e.g. concern about stack overflow on very deep trees).

---

# Problem 40 — Flatten Binary Tree (Morris-style, O(1) space)

## 1. What is the problem?
Flatten the tree with **zero extra space** — no stack, no recursion, no instance fields for state.

## 2. Example Tree
Same tree.

## 3. My Code
```java
private static void FlattenTreeMorries(Node root) {
    Node curr = root;
    while (curr != null) {
        if (curr.left != null) {
            Node pre = curr.left;
            while (pre.right != null) { pre = pre.right; }
            pre.right = curr.right;
            curr.right = curr.left;
            curr.left = null;
        }
        curr = curr.right;
    }
}
```

## 4. Issues / Bugs / Edge Cases
None — correct, and genuinely elegant: no thread *removal* step is even needed here (unlike `morrisInorder`), because flattening is a **permanent** restructuring anyway, so there's nothing to "put back."

## 5. Intuition & Why No Thread Removal Is Needed Here
This reuses the exact "find the rightmost node of the left subtree" idea from `morrisInorder` (Problem 36), but applies it differently:
For every node with a left child: find `pre` (rightmost node in the left subtree), **attach the current node's existing right subtree onto the end of `pre`** (`pre.right = curr.right`), then **move the entire left subtree into the right slot** (`curr.right = curr.left; curr.left = null`), and advance (`curr = curr.right`).

**Why this doesn't need to "undo" anything the way Morris traversal does:** traversal must leave the tree exactly as it found it (temporary borrowing), but flattening is *supposed* to permanently rewire the tree — so there's no cleanup step required, making this version simpler than `morrisInorder` despite the similar-looking core trick.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `if (curr.left != null)` | Only nodes with a left child need any rewiring |
| `pre = curr.left; while (pre.right != null) pre = pre.right;` | Find the tail of curr's left subtree (once it's flattened, this becomes the natural splice point) |
| `pre.right = curr.right;` | Reattach curr's original right subtree onto the end of the left subtree — this is the actual "splice" |
| `curr.right = curr.left; curr.left = null;` | Left subtree slides into the right slot, left is cleared |
| `curr = curr.right;` | Move forward along the now-partially-built chain — note this naturally walks through nodes that were just spliced in, continuing the process node-by-node without recursion |

## 7. Dry Run
```text
curr=1: left=2 exists. pre walks from 2: 2.right=5 (not null)→pre=5; 5.right=null→stop. pre=5.
  pre.right = curr.right → 5.right = 3 (1's original right subtree, containing 3 and 6)
  curr.right = curr.left → 1.right = 2. curr.left = null.
  curr = curr.right = 2

curr=2: left=4 exists. pre walks from 4: 4.right=null→stop immediately. pre=4.
  pre.right = curr.right → 4.right = 5 (2's original right, which is node 5 — but 5 now already has .right=3 attached from the previous step!)
  curr.right = curr.left → 2.right = 4. curr.left = null.
  curr = curr.right = 4

curr=4: left=null → skip the if block entirely. curr = curr.right = 5.

curr=5: left=null → skip. curr = curr.right = 3 (inherited from the very first splice).

curr=3: left=6 exists. pre walks from 6: 6.right=null→stop. pre=6.
  pre.right = curr.right → 6.right = null (3 had no right child originally)
  curr.right = curr.left → 3.right = 6. curr.left = null.
  curr = curr.right = 6

curr=6: left=null → skip. curr = curr.right = null → loop ends.
```
Final chain (follow `.right` from 1): `1 → 2 → 4 → 5 → 3 → 6` — identical result to Problems 37/38/39, achieved in true O(1) extra space and a single pass.

## 8. Test Cases
| Input | Output chain |
|---|---|
| Tree above | `1 → 2 → 4 → 5 → 3 → 6` |
| Single node | `[node]` (loop body never triggers the `if`, curr becomes null immediately) |
| Empty tree | `curr` starts `null`, loop never runs — safe no-op |
| Right-only chain already (`1→2→3`) | Unchanged — no left children anywhere, `if` never triggers, `curr` just walks the existing chain |

## Better / Alternative Approach
This IS the optimal solution — O(n) time, true O(1) auxiliary space, no recursion. This is the best possible version of the flatten problem across all four of your implementations (37–40), and worth defaulting to whenever space is a genuine constraint.

---

# Problem 41 — Search in a BST (Recursive)

## 1. What is the problem?
LeetCode 700. Find whether a value exists in a **Binary Search Tree** and return the node if so — the first problem in your file that actually exploits the BST ordering property rather than treating the tree as a generic binary tree.

## 2. Example Tree
*(Your `main()` tree is NOT a valid BST — using a proper BST example instead, as your BST-section methods require:)*
```text
          8
        /   \
       3     10
      / \      \
     1   6      14
```

## 3. My Code
```java
private static Node SearchBinarySearchTree(Node root, int val) {
    if (root == null) { return null; }
    if (root.data == val) { return root; }
    else if (root.data > val) { return SearchBinarySearchTree(root.left, val); }
    return SearchBinarySearchTree(root.right, val);
}
```

## 4. Issues / Bugs / Edge Cases
None — clean and correct.

## 5. Intuition & Why BSTs Change the Game
For a *generic* binary tree, searching for a value requires checking both children at every node (you have no idea which side it might be on) — O(n) worst case, must potentially visit every node. A **BST** changes this completely: because of the ordering guarantee (everything in the left subtree is smaller, everything in the right subtree is larger), you can **discard half the remaining tree at every single step** — exactly like binary search on a sorted array. If `val` is smaller than the current node, it can ONLY be in the left subtree (or not exist at all) — no need to ever check the right side.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `if (root == null) return null;` | Ran off the tree without finding it — value doesn't exist |
| `if (root.data == val) return root;` | Found it |
| `else if (root.data > val)` | Target is smaller than current node → BST guarantees it can only be in the left subtree, if anywhere |
| `return SearchBinarySearchTree(root.right, val);` | Otherwise (target is larger), it can only be in the right subtree |

## 7. Dry Run — `SearchBinarySearchTree(root, 6)`
```text
SearchBinarySearchTree(8, 6): 8 > 6 → go left
SearchBinarySearchTree(3, 6): 3 < 6 → go right
SearchBinarySearchTree(6, 6): 6 == 6 → return node 6
```
Only **3 nodes** were ever touched out of 6 total — half the tree (everything under node 10) was never even looked at.

## 8. Test Cases
| Input | val | Output |
|---|---|---|
| BST above | 6 | node with data=6 |
| BST above | 8 (the root) | node with data=8 |
| BST above | 99 (doesn't exist) | `null` |
| BST above | 1 (a leaf) | node with data=1 |
| Empty tree | any | `null` |

## Better / Alternative Approach
See Problem 42 for the iterative version (same logic, no recursion overhead) — a common interviewer follow-up. Both are O(h) time (h = tree height), which is optimal for this problem on a BST.

---

# Problem 42 — Search in a BST (Iterative)

## 1. What is the problem?
Same as Problem 41, without recursion.

## 2. Example Tree
Same BST as Problem 41.

## 3. My Code
```java
private static Node SearchBSTiterative(Node root, int val) {
    while (root != null && root.data != val) {
        root = root.data > val ? root.left : root.right;
    }
    return root;
}
```

## 4. Issues / Bugs / Edge Cases
None — a clean, compact translation of the recursive logic into a loop.

## 5. Intuition & Why This Approach
Direct iterative translation of Problem 41's recursion, using a ternary to pick the direction in one line. Same "eliminate half the tree every step" logic — this is the version worth having ready for an interview follow-up like *"can you avoid the recursive call stack overhead?"* since it uses O(1) space instead of O(h) for the call stack.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `while (root != null && root.data != val)` | Keep going as long as there's still tree left AND we haven't found it yet |
| `root = root.data > val ? root.left : root.right;` | Same directional logic as the recursive version, just reassigning `root` instead of recursing |
| `return root;` | Either the found node, or `null` if the loop ran off the tree |

## 7. Dry Run — `SearchBSTiterative(root, 20)` (value doesn't exist)
```text
root=8: 8≠20, 8<20 → root = root.right = 10
root=10: 10≠20, 10<20 → root = root.right = 14
root=14: 14≠20, 14<20 → root = root.right = null
root=null: loop condition (root != null) fails → exit loop
return null
```
Result: `null` — correctly reports "not found" after checking only the relevant right-leaning path, never touching the left side of the tree at all.

## 8. Test Cases
| Input | val | Output |
|---|---|---|
| BST above | 6 | node with data=6 |
| BST above | 20 | `null` (as traced above) |
| BST above | 8 | root node itself |
| Empty tree | any | `null` |

## Better / Alternative Approach
This already IS the space-optimal version relative to Problem 41 — O(h) time, O(1) space. Nothing further to improve for this exact problem.

---

# Problem 43 — Ceil in a BST (Iterative)

## 1. What is the problem?
GFG classic. Find the **ceil** of a given value in a BST: the smallest value present in the tree that is `>= target`. Return `-1` if no such value exists (i.e., every value in the tree is smaller than the target).

## 2. Example Tree
Same BST as Problem 41.

## 3. My Code
```java
private static int CeilBST(Node root, int i) {
    int ceil = -1;
    while (root != null) {
        if (root.data >= i) {
            ceil = root.data;
            root = root.left;
        } else {
            root = root.right;
        }
    }
    return ceil;
}
```

## 4. Issues / Bugs / Edge Cases
None — correct.

## 5. Intuition & Why This Approach
Walk down the BST like a normal search, but **remember your best candidate so far** rather than stopping the moment you find *any* match:
- Whenever the current node's value is `>= target`, it's a **valid possible answer** — record it (`ceil = root.data`) — but there might be an even *smaller* valid candidate hiding further down the left subtree, so keep going left to check.
- Whenever the current node's value is `< target`, it can **never** be a valid ceil (too small) — don't record it, just move right to look for something bigger.

This is the same BST "eliminate half the tree" idea as search, but adapted to track a running best answer instead of stopping at an exact match.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `int ceil = -1;` | Default: "no valid ceil found yet" |
| `if (root.data >= i) { ceil = root.data; root = root.left; }` | Valid candidate found — record it, but keep searching left for something even closer (smaller but still `>= i`) |
| `else { root = root.right; }` | Too small to ever be the answer — must go right to find something bigger |

## 7. Dry Run — `CeilBST(root, 7)`
```text
root=8: 8>=7 → ceil=8, root=root.left=3
root=3: 3<7 → root=root.right=6
root=6: 6<7 → root=root.right=null
root=null → loop ends
return ceil = 8
```
Result: **8** — correct, since 6 is too small and 8 is the smallest value in the tree that's `>= 7`.

## 8. Test Cases
| Input | target | Output |
|---|---|---|
| BST above | 7 | 8 |
| BST above | 10 | 10 (exact match) |
| BST above | 15 | -1 (nothing in the tree is `>= 15`) |
| BST above | 2 | 3 |
| BST above | 8 | 8 (exact match at the root) |
| Empty tree | any | -1 |

## Better / Alternative Approach
Already the standard, optimal O(h) iterative solution for this problem. No meaningful improvement exists.

---

# Problem 44 — Ceil in a BST (Recursive — Version A: pure return value)

## 1. What is the problem?
Same as Problem 43, but recursive. You wrote **two different recursive versions** of this in your file — this is Version A, notable for being fully self-contained with no shared/instance state.

## 2. Example Tree
Same BST.

## 3. My Code
```java
private static int CeilBSTrecursive(int target, Node root) {
    if (root == null) { return -1; }
    if (root.data >= target) {
        int leftans = CeilBSTrecursive(target, root.left);
        if (leftans != -1) { return leftans; }
        return root.data;
    }
    return CeilBSTrecursive(target, root.right);
}
```

## 4. Issues / Bugs / Edge Cases
None — correctly self-contained, no dependency on any field between calls.

## 5. Intuition & Why This Approach
Same core logic as the iterative version (Problem 43), but the "remember the best candidate so far" behavior is achieved purely through **return values propagating up the call stack**, rather than a mutable local variable that gets reassigned across loop iterations:
- If `root.data >= target`, this node is a *candidate* — but first check if the left subtree has an even better (smaller, still valid) one via `leftans`. If the left subtree genuinely found something (`leftans != -1`), that's strictly better (smaller) than this node, so return it instead. Otherwise, this node itself is the best available answer.
- If `root.data < target`, this node can never be the answer — the entire answer must come from the right subtree, so just return whatever that recursive call finds (no need to compare against anything at this node).

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `if (root == null) return -1;` | Ran off the tree — no valid candidate down this path |
| `if (root.data >= target)` | This node is a *possible* answer |
| `int leftans = CeilBSTrecursive(target, root.left);` | Check if something even smaller (still valid) exists further left |
| `if (leftans != -1) return leftans;` | Left subtree found something better — prefer it |
| `return root.data;` | Left subtree found nothing better — this node is the best available |
| `return CeilBSTrecursive(target, root.right);` | Current node too small — the answer, if any, must live entirely in the right subtree |

## 7. Dry Run — `CeilBSTrecursive(7, root)`
```text
CeilBSTrecursive(7, 8): 8>=7 → check left
  CeilBSTrecursive(7, 3): 3<7 → return CeilBSTrecursive(7, 3.right=6)
    CeilBSTrecursive(7, 6): 6<7 → return CeilBSTrecursive(7, 6.right=null)
      CeilBSTrecursive(7, null): return -1
    → returns -1
  → returns -1
  leftans = -1 → not useful, return root.data = 8
```
Result: **8** — same answer as the iterative version, reached via return-value propagation instead of a mutable loop variable.

## 8. Test Cases
| Input | target | Output |
|---|---|---|
| BST above | 7 | 8 |
| BST above | 10 | 10 |
| BST above | 15 | -1 |
| BST above | 2 | 3 |
| Empty tree | any | -1 |

## Better / Alternative Approach
Already optimal O(h) recursive solution — this pure-return-value style is generally considered the *cleaner* of your two recursive versions (see Problem 45) since it has no dependency on object state, making it safe to call repeatedly or even concurrently without any reset step.

---

# Problem 45 — Ceil in a BST (Recursive — Version B: instance field / side-effect)

## 1. What is the problem?
Same ceil problem again — your second recursive attempt, using a different style: mutating a shared instance field instead of returning the answer through the call stack.

## 2. Example Tree
Same BST.

## 3. My Code
```java
int ceil = -1;
private int CeilBSTrecursive(Node root, int i) {
    if (root == null) { return ceil; }
    if (root.data >= i) {
        ceil = root.data;
        return CeilBSTrecursive(root.left, i);
    }
    return CeilBSTrecursive(root.right, i);
}
```

## 4. Issues / Bugs / Edge Cases
- **Important gotcha if you ever reuse this:** the field `ceil` starts at `-1` only once, when the `Tree` object is constructed. If you call this method **a second time on the same object** with a different target (without manually resetting `ceil = -1` first), the second call would start from whatever `ceil` was left at from the *first* call — silently giving a wrong answer for any target where no valid candidate exists (it would return the stale previous answer instead of `-1`). Your `main()` only calls it once per fresh usage, so this doesn't surface there, but it's a real correctness trap if this method is ever reused. This is a strictly worse pitfall than Version A, which has no such risk at all.

## 5. Intuition & Why This Style Is Different (Not Wrong, Just Different)
Instead of returning the answer *through* the recursion, this version mutates the shared field `ceil` directly, every time a valid candidate is found, and every recursive call just eventually returns whatever `ceil` currently holds. This works correctly **only because BST search never branches** — there's exactly one path being explored at any time, so there's no risk of two different recursive branches racing to overwrite `ceil` with conflicting values. This is a **side-effect-based** style, as opposed to Version A's **pure return-value-based** style.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `if (root == null) return ceil;` | Ran off the tree — return whatever the best candidate found so far was (the field, not a fresh -1) |
| `if (root.data >= i) { ceil = root.data; return CeilBSTrecursive(root.left, i); }` | Valid candidate → record it in the shared field, then keep looking left for something even smaller |
| `return CeilBSTrecursive(root.right, i);` | Too small → go right, `ceil` field untouched at this step |

## 7. Dry Run — `t.CeilBSTrecursive(root, 10)` (assuming fresh object, `ceil` starts at -1)
```text
CeilBSTrecursive(8, 10): 8<10 → go right, ceil still -1
CeilBSTrecursive(10, 10): 10>=10 → ceil=10, go left (10.left is null)
CeilBSTrecursive(null, 10): root==null → return ceil (=10)
```
Result: **10** — correct, and matches the answer both other versions would give for the same input.

## 8. Test Cases
| Input | target | Output (fresh object each time) |
|---|---|---|
| BST above | 7 | 8 |
| BST above | 10 | 10 |
| BST above | 15 | -1 |
| BST above, called TWICE on the same object: first with target=8, then target=15 without resetting `ceil` | 15 (second call) | **Bug surfaces here:** returns `8` (stale from the first call) instead of the correct `-1` |

## Better / Alternative Approach
**Prefer Version A (Problem 44)** for real use — it has no hidden state and is safe to call repeatedly. If you want to keep this field-based style (useful to know for more complex traversals where tracking "best answer so far" across branching paths is genuinely needed, unlike this single-path BST case), the fix is to reset the field at the start of the public-facing call:
```java
private int findCeil(Node root, int target) {
    ceil = -1;         // reset before every fresh search
    return CeilBSTrecursive(root, target);
}
```
This keeps the side-effect style but removes the cross-call contamination risk.

---

# Problem 46 — Floor in a BST (Iterative)

## 1. What is the problem?
The mirror image of Ceil (Problem 43): find the **floor** of a target value in a BST — the *largest* value present in the tree that is `<= target`. Return `-1` if nothing qualifies (every value in the tree is bigger than the target).

## 2. Example Tree
The shared BST example.

## 3. My Code
```java
private static int floorBSTiterative(int x, Node root) {
    Node curr = root;
    int floor = -1;
    while (curr != null) {
        if (curr.data == x) {
            floor = curr.data;
            return floor;
        }
        if (curr.data < x) {
            floor = curr.data;
            curr = curr.right;
        } else {
            curr = curr.left;
        }
    }
    return floor;
}
```

## 4. Issues / Bugs / Edge Cases
None — correct, and a clean direct mirror of `CeilBST` (Problem 43).

## 5. Intuition & Why This Approach
Exactly the same "walk down, remember your best candidate so far" pattern as Ceil, just flipped:
- If the current node's value is `< target`, it's a **valid candidate** (record it) — but there might be an even *larger* valid candidate still `<= target` further down the **right** subtree, so keep going right.
- If the current node's value is `> target`, it can never be a valid floor — go left to look for something smaller.
- An exact match (`== target`) is trivially both the floor and the answer — return immediately.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `if (curr.data == x) { floor = curr.data; return floor; }` | Exact match — nothing can beat this, return immediately |
| `if (curr.data < x) { floor = curr.data; curr = curr.right; }` | Valid candidate — record it, then look right for something even closer to (but still `<=`) the target |
| `else { curr = curr.left; }` | Too big — can never be the floor, go left |

## 7. Dry Run — `floorBSTiterative(7, root)`
```text
curr=8: 8==7?no. 8<7?no(8>7) → curr=curr.left=3
curr=3: 3==7?no. 3<7?yes → floor=3, curr=curr.right=6
curr=6: 6==7?no. 6<7?yes → floor=6, curr=curr.right=null
curr=null → loop ends
return floor=6
```
Result: **6** — correctly the largest value in the tree that's `<= 7`.

## 8. Test Cases
| Input | target | Output |
|---|---|---|
| BST example | 7 | 6 |
| BST example | 8 (exact match) | 8 |
| BST example | 0 (smaller than everything) | -1 |
| BST example | 100 (larger than everything) | 14 |
| Empty tree | any | -1 |

## Better / Alternative Approach
Already the standard, optimal O(h) iterative solution. See Problem 47 for the recursive equivalent.

---

# Problem 47 — Floor in a BST (Recursive)

## 1. What is the problem?
Same as Problem 46, recursive version — worth comparing directly against `CeilBSTrecursive` Version A (Problem 44), since this is its exact structural mirror.

## 2. Example Tree
The shared BST example.

## 3. My Code
```java
private static int floorBSTRecursive(int i, Node root) {
    if (root == null) {
        return -1;
    }
    if (root.data <= i) {
        int rightans = floorBSTRecursive(i, root.right);
        if (rightans != -1) {
            return rightans;
        }
        return root.data;
    }
    return floorBSTRecursive(i, root.left);
}
```

## 4. Issues / Bugs / Edge Cases
None — correct.

## 5. Intuition & Why This Approach
Mirror image of `CeilBSTrecursive` Version A: if `root.data <= target`, this node is a *candidate* — check the **right** subtree first for something even closer (larger, but still `<= target`); if the right subtree found nothing better, this node itself is the best available answer. If `root.data > target`, the entire answer must come from the **left** subtree instead, so just return whatever that recursive call finds.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `if (root == null) return -1;` | Ran off the tree, no candidate down this path |
| `if (root.data <= i)` | Valid candidate |
| `int rightans = floorBSTRecursive(i, root.right);` | Look right for something even closer to the target |
| `if (rightans != -1) return rightans;` | Right subtree found something better — prefer it |
| `return root.data;` | Otherwise, this node is the best available |
| `return floorBSTRecursive(i, root.left);` | Too big — the answer, if any, lives entirely in the left subtree |

## 7. Dry Run — `floorBSTRecursive(7, root)`
```text
floorBSTRecursive(7, 8): 8<=7? no → return floorBSTRecursive(7, root.left=3)
floorBSTRecursive(7, 3): 3<=7? yes → rightans = floorBSTRecursive(7, 3.right=6)
  floorBSTRecursive(7, 6): 6<=7? yes → rightans2 = floorBSTRecursive(7, 6.right=null)
    floorBSTRecursive(7, null): return -1
  rightans2 = -1 → return root.data = 6
rightans = 6, not -1 → return 6
```
Result: **6** — matches the iterative version exactly.

## 8. Test Cases
| Input | target | Output |
|---|---|---|
| BST example | 7 | 6 |
| BST example | 8 | 8 |
| BST example | 0 | -1 |
| BST example | 100 | 14 |
| Empty tree | any | -1 |

## Better / Alternative Approach
Already the standard, optimal O(h) recursive solution — a clean, self-contained (no shared state) mirror of Problem 44's approach to Ceil.

---

# Problem 48 — Insert into a BST (Iterative)

## 1. What is the problem?
LeetCode 701. Insert a new node into a BST at the position that preserves the BST ordering property.

## 2. Example Tree
The shared BST example, inserting the value `9`.

## 3. My Code
```java
private static Node insertGivenNodeInBST(Node root, Node node) {
    Node curr = root;
    while (curr != null) {
        int x = curr.data;
        int i = node.data;
        if (i > x) {
            if (curr.right != null) {
                curr = curr.right;
            } else {
                curr.right = node;
                break;
            }
        } else {
            if (curr.left != null) {
                curr = curr.left;
            } else {
                curr.left = node;
                break;
            }
        }
    }
    return root;
}
```

## 4. Issues / Bugs / Edge Cases
- **Real bug: inserting into an empty tree silently loses the node.** If `root == null`, the `while (curr != null)` loop never runs at all — the method just returns `root`, which is still `null`. The new node is never attached anywhere, and the caller has no way to know a brand-new root should have been created. Calling `insertGivenNodeInBST(null, new Node(5))` returns `null`, not a one-node tree containing `5`.
- **Design choice worth naming explicitly (not a bug):** the `else` branch (triggered when `i <= x`, i.e., the new value is less than *or equal to* the current node) sends duplicates left. This is a valid convention some BSTs use, but it's implicit here — worth knowing this is *your* choice, not an inherent BST requirement, since other implementations reject duplicates outright or send them right instead.

## 5. Intuition & Why This Approach
Standard BST insertion: walk down comparing the new value against each node, going right if it's bigger, left otherwise — exactly the same directional logic as `SearchBinarySearchTree` (Problem 41), except instead of stopping when you find a match, you keep going until you find an **empty slot** (a `null` child), and that's where the new node gets attached.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `if (i > x)` | New value belongs to the right |
| `if (curr.right != null) curr = curr.right;` | Keep walking right |
| `else { curr.right = node; break; }` | Found the empty slot — attach and stop |
| `else` branch (mirror logic) | Same idea, walking left |

## 7. Dry Run — `insertGivenNodeInBST(root, new Node(9))`
```text
curr=8: x=8,i=9. i>x(9>8) → curr.right=10 exists → curr=10.
curr=10: x=10,i=9. i>x? 9>10? no → else: curr.left? 10 has no left child → curr.left = node(9). break.
```
Result: node `9` is attached as `10`'s new left child.
```text
          8
        /   \
       3     10
      / \    /  \
     1   6  9    14
```

## 8. Test Cases
| Call | On tree | Result |
|---|---|---|
| `insertGivenNodeInBST(root, new Node(9))` | BST example | `9` becomes `10`'s left child |
| `insertGivenNodeInBST(root, new Node(0))` | BST example | `0` becomes `1`'s left child (walks 8→3→1→left) |
| `insertGivenNodeInBST(root, new Node(20))` | BST example | `20` becomes `14`'s right child |
| `insertGivenNodeInBST(null, new Node(5))` | empty tree | **currently returns `null` — the node is silently lost, see bug above** |

## Better / Alternative Approach
Fix the empty-tree bug with an upfront check:
```java
private static Node insertGivenNodeInBST(Node root, Node node) {
    if (root == null) { return node; }
    Node curr = root;
    ...
}
```
With that one-line fix, this is already the standard, optimal O(h) iterative BST insertion.

---

# Problem 49 — Delete a Node from a BST (Merge Strategy)

## 1. What is the problem?
LeetCode 450. Remove a node with a given value from a BST, correctly reconnecting its subtrees so the result is still a valid BST.

## 2. Example Tree
The shared BST example, deleting the value `3` (which has two children: `1` and `6`) — the interesting case worth tracing in full.

## 3. My Code
```java
private static Node deleteTheKey(Node root, int i) {
    if (root == null) {
        return null;
    }
    if (root.data > i) {
        root.left = deleteTheKey(root.left, i);
    } else if (root.data < i) {
        root.right = deleteTheKey(root.right, i);
    } else {
        if (root.left == null) {
            return root.right;
        }
        if (root.right == null) {
            return root.left;
        }
        if (root.left != null && root.right != null) {
            Node rightNode = root.right;
            Node curr = root.left;
            while (curr.right != null) {
                curr = curr.right;
            }
            curr.right = rightNode;
        }
        return root.left;
    }
    return root;
}
```

## 4. Issues / Bugs / Edge Cases
None — this is a correct, complete implementation of BST deletion. Worth explicitly noting: this uses the **merge strategy** rather than the more commonly-taught "replace with inorder successor" approach — see the intuition below for why both are valid.

## 5. Intuition & Why the Merge Strategy (Instead of Inorder Successor)
Standard BST search first (`root.data > i` → search left, `root.data < i` → search right) until the target node is found. Once found, there are three cases:
- **No left child** → the right subtree can just take this node's place entirely (`return root.right`).
- **No right child** → symmetric (`return root.left`).
- **Both children exist** — this is the interesting case. The most commonly-taught technique replaces the deleted node's *value* with its inorder successor. **Your approach instead physically re-grafts the subtrees**: walk to the **rightmost node of the left subtree** (the predecessor — the largest value smaller than the deleted node), and attach the *entire right subtree* there. Since every value in the right subtree is larger than every value in the left subtree (that's the BST property), and the rightmost node of the left subtree is the single largest value in that left subtree, attaching the right subtree as its right child preserves BST ordering perfectly. Then the **left subtree** (now containing everything, correctly merged) simply takes the deleted node's place.

Both techniques are equally valid, correct O(h) solutions to the same problem — just different "which side absorbs which" strategies.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `if (root.data > i) root.left = deleteTheKey(root.left, i);` | Standard BST search, left |
| `else if (root.data < i) root.right = deleteTheKey(root.right, i);` | Standard BST search, right |
| `if (root.left == null) return root.right;` | Zero or one child (right only) — right subtree replaces this node |
| `if (root.right == null) return root.left;` | Symmetric — left subtree replaces this node |
| `Node curr = root.left; while (curr.right != null) curr = curr.right;` | Find the rightmost (largest) node in the left subtree |
| `curr.right = rightNode;` | Graft the entire right subtree on as its right child |
| `return root.left;` | The (now-merged) left subtree takes the deleted node's place entirely |

## 7. Dry Run — `deleteTheKey(root, 3)` (deleting a node with two children)
```text
deleteTheKey(8, 3): 8>3 → root.left = deleteTheKey(3, 3)
  deleteTheKey(3, 3): data==i → left=1(not null), right=6(not null) → two-children case
    rightNode = 6 (root.right)
    curr = root.left = 1
    while(curr.right != null): 1.right is null → loop doesn't run, curr stays 1
    curr.right = rightNode → 1.right = 6
    return root.left → return node 1 (now with right child 6)
root(8).left = node 1 (with right=6)
return root (8)
```
Result:
```text
        8
       / \
      1   10
       \    \
        6    14
```
Node `3` is gone; its left subtree (`1`) absorbed its right subtree (`6`) as `1`'s new right child, and the whole merged piece took `3`'s old spot. BST ordering is preserved: `1 < 6 < 8`.

## 8. Test Cases
| Call | On tree | Result |
|---|---|---|
| `deleteTheKey(root, 3)` | BST example | traced above — `1` absorbs `6`, replaces `3` |
| `deleteTheKey(root, 6)` | BST example | `6` is a leaf — simply removed, `3`'s right becomes `null` |
| `deleteTheKey(root, 14)` | BST example | `14` is a leaf — simply removed |
| `deleteTheKey(root, 99)` | BST example (value doesn't exist) | tree unchanged (search runs off the end, hits `root==null`, returns `null` up through a chain that never matches) |
| `deleteTheKey(root, 8)` | BST example (deleting the root itself, two children) | left subtree (`3`'s whole tree) absorbs right subtree (`10`'s whole tree) at `3`'s rightmost descendant (`6`), and `3`'s subtree becomes the new root |

## Better / Alternative Approach
Both this merge strategy and the more commonly-taught inorder-successor-value-copy approach are equally correct, O(h)-time solutions — genuinely a matter of preference, not one being objectively better. The inorder-successor approach is arguably slightly more intuitive to explain out loud ("replace the value, then delete the successor instead"), but your merge approach avoids a second search-and-delete pass for the successor, doing the whole operation in a single traversal.

---

# Problem 50 — Kth Smallest Element in a BST

## 1. What is the problem?
LeetCode 230. Find the `k`-th smallest value in the tree (1-indexed).

## 2. Example Tree
The shared BST example.

## 3. My Code
```java
private static int kthSmallest(Node root, int k) {
    List<Node> li = new ArrayList<>();
    kthSmallestHelper(root, li);
    int n = li.size();
    return li.get(k - 1).data;
}

static void kthSmallestHelper(Node root, List<Node> li) {
    if (root == null) {
        return;
    }
    if (root.left != null) {
        kthSmallestHelper(root.left, li);
    }
    li.add(root);
    if (root.right != null) {
        kthSmallestHelper(root.right, li);
    }
}
```

## 4. Issues / Bugs / Edge Cases
- **Unguarded precondition:** no bounds check on `k`. If `k` is larger than the number of nodes (or `k <= 0`), `li.get(k-1)` throws an `IndexOutOfBoundsException` rather than failing gracefully.
- **Dead code, harmless:** `int n = li.size();` is computed but never actually used anywhere in the method.
- **Minor style redundancy** (not a bug): the `if (root.left != null)` / `if (root.right != null)` guards inside the helper are unnecessary — the base case `if (root == null) return;` at the top already makes an unconditional recursive call into a `null` child perfectly safe. This is the same small habit noticed in `ChildrenSumProperty` back in Problem 28.

## 5. Intuition & Why This Approach
The core insight: **an inorder traversal of a BST visits every node in sorted order**, for free, by definition of the BST property. So rather than building any special "kth smallest" logic, `kthSmallestHelper` is just a plain inorder traversal that collects nodes into a list instead of printing them — once that list exists, the `k`-th smallest value is simply sitting at index `k-1` (converting from 1-indexed to 0-indexed).

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `kthSmallestHelper(root, li);` | Fully populate `li` in sorted order via inorder traversal |
| `return li.get(k - 1).data;` | Direct index lookup — no searching needed once the list is built |

## 7. Dry Run — `kthSmallest(root, 2)`
```text
kthSmallestHelper builds li via inorder: visits 1, 3, 6, 8, 10, 14 in that order.
li = [Node(1), Node(3), Node(6), Node(8), Node(10), Node(14)]
li.get(2-1) = li.get(1) = Node(3)
return 3
```
Result: **3** — the 2nd smallest value in the sorted sequence `1, 3, 6, 8, 10, 14`.

## 8. Test Cases
| Call | On tree | Result |
|---|---|---|
| `kthSmallest(root, 2)` | BST example | 3 |
| `kthSmallest(root, 1)` | BST example | 1 (the smallest) |
| `kthSmallest(root, 6)` | BST example | 14 (the largest, since there are exactly 6 nodes) |
| `kthSmallest(root, 7)` | BST example (only 6 nodes exist) | **currently throws `IndexOutOfBoundsException`** — see bug above |
| `kthSmallest(root, 0)` | BST example | **currently throws `IndexOutOfBoundsException`** (`k-1 = -1`) |

## Better / Alternative Approach
Add a bounds guard:
```java
if (k <= 0 || k > li.size()) { throw new IllegalArgumentException("k out of range"); }
```
Beyond that fix, the O(n) approach here is correct but not the most efficient possible: it always builds the **entire** inorder list even if `k` is small (e.g., `k=1` still does a full O(n) traversal). A genuinely better approach for a single query: do an inorder traversal that **stops early** the moment you've visited the `k`-th node (using a counter, no list at all) — O(k) time instead of O(n), and O(h) space instead of O(n) space for the list. If you needed to answer *many* kth-smallest queries against the same tree repeatedly, an even better approach augments each BST node with a count of nodes in its left subtree, enabling O(h) per query with no traversal at all.

---

# Problem 51 — Validate BST (Range-Bounding, Top-Down)

## 1. What is the problem?
LeetCode 98. Determine whether a given binary tree satisfies the BST property everywhere — not just locally between each node and its immediate children, but genuinely for *every* ancestor-descendant relationship in the tree.

## 2. Example Tree
The shared BST example (valid) — plus a specifically-constructed **invalid** example that demonstrates why this problem is trickier than it first looks:
```text
       10
      /  \
     5    15
         /  \
        6    20
```
This tree is **NOT** a valid BST, even though every node individually looks fine compared to its *immediate* parent (`6 < 15` looks OK locally) — the problem is that `6` sits in `10`'s right subtree, yet `6 < 10`, which violates the BST property globally. Catching this kind of violation is the entire point of Problems 51 and 52.

## 3. My Code
```java
private static boolean validBST(Node root) {
    if (root == null) {
        return true;
    }
    return validBST(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
}

private static boolean validBST(Node root, int minValue, int maxValue) {
    if (root == null) {
        return true;
    }
    if (root.data <= minValue || root.data >= maxValue) {
        return false;
    }
    return validBST(root.left, minValue, root.data) && validBST(root.right, root.data, maxValue);
}
```

## 4. Issues / Bugs / Edge Cases
**A well-known LeetCode 98 gotcha, worth flagging even though it's unlikely to matter in practice:** the initial bounds use `Integer.MIN_VALUE` / `Integer.MAX_VALUE` as `int`. If the tree legitimately contained a node with the *exact* value `Integer.MIN_VALUE`, the check `root.data <= minValue` would trigger (`MIN_VALUE <= MIN_VALUE` is true) and incorrectly report the tree as invalid, even if it's actually a perfectly valid BST. The standard fix is to widen the bounds to `long` (`Long.MIN_VALUE` / `Long.MAX_VALUE`) so no legitimate `int` value can ever collide with the sentinel boundary.

## 5. Intuition & Why Range-Bounding (Not Just Local Comparison)
The naive approach — compare each node only to its immediate left/right children — is a well-known trap that silently accepts invalid trees like the counter-example above. The fix: pass down a **valid range** (`minValue`, `maxValue`) that gets progressively **tightened** as you descend. Every node must fall strictly inside the range handed to it by its ancestors — not just be consistent with its immediate parent. Going left tightens the *upper* bound to the current node's value (everything further left must be smaller than this node specifically, not just smaller than some ancestor). Going right tightens the *lower* bound the same way.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `if (root.data <= minValue \|\| root.data >= maxValue) return false;` | The actual constraint check — this node must respect **every** ancestor's bound, not just its direct parent's |
| `validBST(root.left, minValue, root.data)` | Left subtree inherits the same lower bound, but the upper bound tightens to the current node's value |
| `validBST(root.right, root.data, maxValue)` | Right subtree inherits the same upper bound, but the lower bound tightens |

## 7. Dry Run — `validBST(root)` on the shared (valid) BST example
```text
validBST(8, -inf, +inf): 8 ok → check left(3,-inf,8) && right(10,8,+inf)
  validBST(3, -inf, 8): 3 ok → check left(1,-inf,3) && right(6,3,8)
    validBST(1,-inf,3): 1 ok → validBST(null,...)=true && validBST(null,...)=true → true
    validBST(6,3,8): 6 ok (3<6<8) → true && true → true
  → true
  validBST(10,8,+inf): 10 ok → check left(null,8,10)=true && right(14,10,+inf)
    validBST(14,10,+inf): 14 ok → true
  → true
→ true && true = true
```
Result: **true** — correctly valid.

**Contrast — the invalid counter-example `10 / 5, (15 / 6, 20)`:**
```text
validBST(10,-inf,+inf): 10 ok → check left(5,-inf,10) && right(15,10,+inf)
  validBST(5,-inf,10): 5 ok → true
  validBST(15,10,+inf): 15 ok → check left(6,10,15) && right(20,15,+inf)
    validBST(6,10,15): is 6 <= minValue(10)? YES → return false!
  → false (short-circuits, right side never even needs checking)
→ true && false = false
```
Result: **false** — correctly catches the violation, because `6` was checked against the bound `10` (inherited from the root), not just against its immediate parent `15`.

## 8. Test Cases
| Input | Output |
|---|---|
| Shared BST example | `true` |
| `10 / 5, (15 / 6, 20)` (the counter-example above) | `false` |
| Single node | `true` |
| Empty tree | `true` |
| Tree with a duplicate value anywhere (e.g., two nodes both `= 5`) | `false` (strict `<=`/`>=` checks correctly reject duplicates) |

## Better / Alternative Approach
This is already a correct, standard, optimal O(n) solution. See Problem 52 for a genuinely different technique (bottom-up min/max propagation) that solves the identical problem — worth comparing both directly, since they represent two fundamentally different strategies (top-down constraint-passing vs. bottom-up range-aggregation) for the same validation task.

---

# Problem 52 — Validate BST (Min/Max Propagation, Bottom-Up)

## 1. What is the problem?
The exact same validation goal as Problem 51, solved with a structurally different technique — single-pass, bottom-up, using the new `Info` helper class instead of passed-down bounds.

## 2. Example Tree
Same shared BST example and invalid counter-example as Problem 51.

## 3. My Code
```java
private static boolean validBST2(Node root) {
    if (root == null) {
        return true;
    }
    return checkBST(root).isBST;
}

private static Info checkBST(Node root) {
    if (root == null) {
        return new Info(Integer.MAX_VALUE, Integer.MIN_VALUE, true);
    }
    Info left = checkBST(root.left);
    Info right = checkBST(root.right);
    if (!left.isBST || !right.isBST) {
        return new Info(
            Math.min(root.data, Math.min(left.min, right.min)),
            Math.max(root.data, Math.max(left.max, right.max)),
            false
        );
    }
    if (left.max >= root.data || right.min <= root.data) {
        return new Info(
            Math.min(root.data, Math.min(left.min, right.min)),
            Math.max(root.data, Math.max(left.max, right.max)),
            false
        );
    }
    int min = Math.min(root.data, Math.min(left.min, right.min));
    int max = Math.max(root.data, Math.max(left.max, right.max));
    return new Info(min, max, true);
}
```

## 4. Issues / Bugs / Edge Cases
None — correct, and genuinely more advanced than Problem 51's approach. The same `int`-vs-`long` boundary gotcha noted in Problem 51 technically applies here too, though it surfaces differently (via `Integer.MAX_VALUE`/`MIN_VALUE` as the "empty subtree" sentinel values rather than as explicit bounds).

## 5. Intuition & Why Min/Max Propagation Works
Instead of passing constraints **down** from ancestors, this approach computes, for every subtree, **three facts at once**: its minimum value, its maximum value, and whether it's internally a valid BST — then combines those facts going **up** the call stack. The clever base case: an empty subtree reports `min = Integer.MAX_VALUE` and `max = Integer.MIN_VALUE` — deliberately *inverted* sentinels, so that when a parent computes `Math.min(root.data, left.min)`, a missing (null) child can never accidentally "win" that minimum comparison, and symmetrically for max. This is the standard trick for this exact "combine children's aggregated facts" pattern.

At every real node, the actual BST check is: **is this node's value greater than everything in its left subtree, AND less than everything in its right subtree?** — captured directly as `left.max >= root.data || right.min <= root.data` (a violation if either fails). This check alone is what correctly catches violations a naive "only check immediate children" approach would miss, because `left.max` and `right.min` reflect the **entire** subtree's extremes, not just the immediate child's own value.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| base case: `Info(MAX_VALUE, MIN_VALUE, true)` | Empty subtree — inverted sentinels ensure it never wins a min/max comparison against a real value |
| `left = checkBST(root.left); right = checkBST(root.right);` | Recursively validate and summarize both subtrees first |
| `if (!left.isBST \|\| !right.isBST)` | Already broken somewhere below — propagate the failure upward (still computing min/max for consistency, but `isBST=false` is what actually matters from here on) |
| `if (left.max >= root.data \|\| right.min <= root.data)` | **The real check** — does this node's value genuinely sit strictly between the true extremes of its entire left and right subtrees? |
| final `return new Info(min, max, true);` | This subtree is valid — bundle its true min/max upward for its own parent to use |

## 7. Dry Run — `checkBST(root).isBST` on the invalid counter-example `10 / 5, (15 / 6, 20)`
```text
checkBST(10):
  left = checkBST(5): leaf. left=checkBST(null)=Info(MAX,MIN,true). right=checkBST(null)=Info(MAX,MIN,true).
    left.isBST&&right.isBST → true. Check: left.max(MIN)>=5? no. right.min(MAX)<=5? no. → valid.
    min=min(5,MAX,MAX)=5. max=max(5,MIN,MIN)=5. return Info(5,5,true).
  right = checkBST(15):
    left=checkBST(6): leaf → Info(6,6,true) (same reasoning as node 5 above)
    right=checkBST(20): leaf → Info(20,20,true)
    both isBST=true. Check: left.max(6)>=15? no. right.min(20)<=15? no. → valid AT NODE 15 (locally).
    min=min(15,6,20)=6. max=max(15,6,20)=20. return Info(6,20,true).
  Back at 10: left=Info(5,5,true), right=Info(6,20,true). Both isBST=true individually.
  Check: left.max(5)>=10? no. right.min(6)<=10? YES → violation detected!
  return Info(min(10,5,6)=5, max(10,5,20)=20, false)
```
Result: `checkBST(root).isBST` = **false** — correctly rejected, because `right.min` (the true minimum of the *entire* right subtree, which is `6`) was compared against `root.data` (`10`), catching the violation that a check comparing only `15` against its own immediate children would have completely missed.

## 8. Test Cases
| Input | Output |
|---|---|
| Shared BST example | `true` |
| `10 / 5, (15 / 6, 20)` (the counter-example) | `false` |
| Single node | `true` |
| Empty tree | `true` |
| Tree with a duplicate value | `false` |

## Better / Alternative Approach
Both this method and Problem 51 are correct, optimal O(n) single-pass solutions to the identical problem — genuinely just two different techniques worth having both in your toolkit. Problem 51's top-down range-passing is generally considered slightly easier to explain out loud in an interview; this bottom-up `Info`-propagation approach generalizes more naturally to problems where you need to know more than just "is it valid" — e.g., "what's the largest valid BST *subtree* within an otherwise-invalid tree" (LeetCode 333) is a direct extension of exactly this pattern.

---

# Problem 53 — Lowest Common Ancestor in a BST (Optimized)

## 1. What is the problem?
LeetCode 235. The BST-specific version of Problem 26's general-tree LCA — since a BST has ordering information available, the search can be made much faster than exploring both subtrees at every node.

## 2. Example Tree
The shared BST example, finding the LCA of `1` and `6`.

**⚠️ Important: this is different from what your `main()` currently tests.** Your `main()`'s only active (uncommented) line is `LCAbst(root, new Node(2), new Node(3));` — but `root` there is still the *original* non-BST tree from the top of `main()` (`1, 2, 3, 4, 5, 6`, where `1`'s children are `2` and `3`). Since `LCAbst` assumes BST ordering to decide which way to recurse, running it against a tree that isn't actually a BST doesn't throw an error — it just silently walks to the wrong place and returns an incorrect answer. Both scenarios are traced below so you can see exactly what goes wrong.

## 3. My Code
```java
private static Node LCAbst(Node root, Node p, Node q) {
    if (root == null || root.data == p.data || root.data == q.data) {
        return root;
    }
    if (root.data < p.data && root.data < q.data) {
        return LCAbst(root.right, p, q);
    }
    if (root.data > p.data && root.data > q.data) {
        return LCAbst(root.left, p, q);
    }
    return root;
}
```

## 4. Issues / Bugs / Edge Cases
No bug in the code itself — it's a correct, standard BST-LCA implementation. The issue is entirely in **how it's currently being called** (see the warning above): this method's correctness *depends on* being given an actual BST. Called against a non-BST tree, it produces a wrong answer with no indication anything went wrong — worth being very deliberate about which tree variable you pass to which method in this file, since several BST-specific methods (this one, plus Problems 41–52) all share the generic `Node`/`root` naming with the non-BST methods earlier in the file.

## 5. Intuition & Why This Beats the General-Tree Version
Problem 26's general-tree LCA has to explore **both** subtrees at every node, because a general binary tree carries no information about where a value might be. A BST changes this completely: at any node, you can compare `p` and `q`'s values against the current node's value and immediately know which single direction to go:
- If **both** `p` and `q` are greater than the current node, the LCA must be somewhere in the **right** subtree — the left subtree can be discarded entirely.
- If **both** are smaller, the LCA must be in the **left** subtree.
- Otherwise — one is smaller-or-equal and the other is larger-or-equal, meaning their paths **split** at this exact node (or the current node *is* one of them) — this node is the LCA.

This turns an O(n) worst-case general-tree search into an O(h) BST search, the same "eliminate half the tree at every step" idea that powers BST search, ceil, and floor throughout this file.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `root.data == p.data \|\| root.data == q.data` | Found one of the targets directly — it's allowed to be its own ancestor |
| `root.data < p.data && root.data < q.data` | Both targets are bigger — discard the left subtree entirely, recurse right only |
| `root.data > p.data && root.data > q.data` | Both targets are smaller — discard the right subtree entirely, recurse left only |
| final `return root;` | Neither of the above — the paths to `p` and `q` diverge here, so this node is the LCA |

## 7. Dry Run A — `LCAbst(root, Node(1), Node(6))` on the actual BST example (correct usage)
```text
LCAbst(8, 1, 6): 8==1? no. 8==6? no.
  8<1 && 8<6? false (8 is not < 1)
  8>1 && 8>6? 8>1 true, 8>6 true → BOTH true → recurse left: LCAbst(3, 1, 6)
LCAbst(3, 1, 6): 3==1? no. 3==6? no.
  3<1 && 3<6? false (3 is not < 1)
  3>1 && 3>6? 3>1 true, 3>6 false → not both true → falls through
  return root (node 3)
```
Result: node **3** — correct, since `1` and `6` are `3`'s direct left and right children, splitting exactly there.

## 7b. Dry Run B — the same call, but against your `main()`'s actual (non-BST) tree
Tree: `1` (root), left=`2`, right=`3`; `2`'s children are `4`,`5`; `3`'s left child is `6`. Calling `LCAbst(root=1, Node(2), Node(3))`:
```text
LCAbst(1, 2, 3): 1==2? no. 1==3? no.
  1<2 && 1<3? 1<2 true, 1<3 true → BOTH true → recurse right: LCAbst(root.right=3, 2, 3)
LCAbst(3, 2, 3): 3==2? no. 3==3? YES → return root (node 3)
```
Result: node **3**.

**But the actual correct LCA of the nodes valued `2` and `3` in this tree is node `1`** (the root — `2` and `3` are literally siblings, both direct children of `1`), exactly as Problem 26's general-tree LCA method would correctly compute. `LCAbst` gets this wrong because it's navigating purely by **comparing values** as if the tree were ordered like a BST — but in this tree, `1 < 2` and `1 < 3` being true doesn't actually mean "both targets are in the right subtree" the way it would in a real BST; it's coincidental value comparison against a tree with no such ordering guarantee. This is exactly why the warning above matters: the method doesn't fail loudly, it just quietly gives you the wrong node.

## 8. Test Cases
| Call | Tree | p | q | Output | Correct? |
|---|---|---|---|---|---|
| `LCAbst(root, Node(1), Node(6))` | shared BST example | 1 | 6 | node 3 | ✅ yes — this is a real BST |
| `LCAbst(root, Node(1), Node(14))` | shared BST example | 1 | 14 | node 8 (root) | ✅ yes |
| `LCAbst(root, Node(8), Node(3))` | shared BST example | 8 | 3 | node 8 | ✅ yes (8 is an ancestor of 3, so 8 is its own LCA with 3) |
| `LCAbst(root, Node(2), Node(3))` | **your `main()`'s original non-BST tree** | 2 | 3 | node 3 | ❌ **wrong** — correct answer is node 1, see Dry Run B above |

## Better / Alternative Approach
The method itself is already the standard, optimal O(h) solution *for an actual BST* — nothing to improve there. The real fix needed is at the **call site**: update `main()` to build (or point at) a genuine BST before calling `LCAbst`, or use Problem 26's general-tree `lowestCommonAncestors` instead if the tree in question isn't guaranteed to be a BST. As a defensive measure, some implementations add an assertion or a comment at the top of BST-specific methods like this one, explicitly noting the precondition — worth considering given how easy it is to accidentally pass the wrong tree in a file that has both BST and non-BST examples living side by side.

---

# Problem 54 — Construct BST from Preorder Traversal

## 1. What is the problem?
LeetCode 1008. Given only a preorder traversal array (guaranteed to come from a valid BST — no duplicates), rebuild the exact original tree. Unlike Problems 32/33, you get **just one** traversal array here — no inorder array to cross-reference — because a BST's ordering property makes the inorder sequence redundant information (it would just be the array sorted).

## 2. Example
`preorder = [8, 3, 1, 6, 10, 14]` — the preorder sequence of the shared BST example.

## 3. My Code
```java
private static Node bstFromPreorder(int[] arr){ 
    return bstFromPreorder(arr, Integer.MAX_VALUE, new int[] {0});
}

private static Node bstFromPreorder(int[] arr, int maxValue, int[] i) {
    if (i[0] == arr.length || arr[i[0]] > maxValue) {
        return null;
    }
    Node root = new Node(arr[i[0]]);
    i[0]++;
    root.left = bstFromPreorder(arr, root.data, i);
    root.right = bstFromPreorder(arr, maxValue, i);
    return root;
}
```

## 4. Issues / Bugs / Edge Cases
None — correct. Worth understanding the `int[] i = {0}` trick specifically: Java passes `int` by value, so a plain `int i` parameter couldn't be advanced by a recursive call and have that change visible to the caller. Wrapping it in a one-element array (`int[]`) is a common Java idiom that gives you a mutable "shared pointer" across recursive calls, since arrays are passed by reference.

## 5. Intuition & Why This Approach
Preorder always visits **root first**, so `arr[i[0]]` is always the next node to build. The clever part is the `maxValue` bound, which does double duty:
- While building the **left** subtree, `maxValue` tightens to `root.data` — because everything in a BST's left subtree must be smaller than the root.
- While building the **right** subtree, `maxValue` stays whatever it was inherited from above — because the right subtree only needs to respect the *original* ancestor bound, not be re-bounded by the current root on the low end.
- **The key trick that needs no explicit lower bound:** by the time you start building the right subtree, the shared index `i[0]` has already consumed every value that belongs to the left subtree (they were all `<= root.data`, per BST ordering). So whatever the preorder array has *next* is guaranteed — by construction — to belong to the right subtree, as long as it's still `<= maxValue`. The moment a value exceeds `maxValue`, that signals "we've walked past the end of this subtree" and recursion should stop (`return null`) without consuming that value — it belongs to an ancestor's right subtree instead.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `if (i[0] == arr.length \|\| arr[i[0]] > maxValue) return null;` | Either the array is exhausted, or the next value is too big to belong here — in both cases, this subtree ends |
| `Node root = new Node(arr[i[0]]); i[0]++;` | Consume the current value as this subtree's root, advance the shared pointer |
| `root.left = bstFromPreorder(arr, root.data, i);` | Left subtree — bound tightens to the current root's value |
| `root.right = bstFromPreorder(arr, maxValue, i);` | Right subtree — inherits the *same* bound this call received |

## 7. Dry Run — `bstFromPreorder([8, 3, 1, 6, 10, 14])`
```text
call(maxValue=+inf, i=[0]): arr[0]=8, 8<=+inf → root=Node(8), i=[1]
  root.left = call(maxValue=8, i=[1]): arr[1]=3, 3<=8 → root=Node(3), i=[2]
    root.left = call(maxValue=3, i=[2]): arr[2]=1, 1<=3 → root=Node(1), i=[3]
      root.left  = call(maxValue=1, i=[3]): arr[3]=6, 6>1 → return null
      root.right = call(maxValue=3, i=[3]): arr[3]=6, 6>3 → return null
      return Node(1)  [leaf]
    root.right = call(maxValue=8, i=[3]): arr[3]=6, 6<=8 → root=Node(6), i=[4]
      root.left  = call(maxValue=6, i=[4]): arr[4]=10, 10>6 → return null
      root.right = call(maxValue=8, i=[4]): arr[4]=10, 10>8 → return null
      return Node(6)  [leaf]
    return Node(3) with left=1, right=6
  root.right = call(maxValue=+inf, i=[4]): arr[4]=10, 10<=+inf → root=Node(10), i=[5]
    root.left  = call(maxValue=10, i=[5]): arr[5]=14, 14>10 → return null
    root.right = call(maxValue=+inf, i=[5]): arr[5]=14, 14<=+inf → root=Node(14), i=[6]
      i[0]==arr.length(6) for both children → both null
      return Node(14)  [leaf]
    return Node(10) with left=null, right=14
  return Node(8) with left=Node(3){1,6}, right=Node(10){null,14}
```
Result: exactly reconstructs the shared BST example — `8 / (3 / (1, 6), 10 / (null, 14))`.

## 8. Test Cases
| Input | Output |
|---|---|
| `[8, 3, 1, 6, 10, 14]` | the shared BST example, exactly |
| `[5]` | single node `5` |
| `[]` | `null` (empty tree — `i[0]==arr.length` immediately) |
| `[1, 2, 3, 4]` (all increasing — every value is a right child) | a purely right-skewed chain `1→2→3→4` |
| `[4, 3, 2, 1]` (all decreasing — every value is a left child) | a purely left-skewed chain `4←3←2←1` |

## Better / Alternative Approach
Already the standard, optimal O(n) solution (each array element is visited exactly once, thanks to the shared index — no repeated scanning). This is the expected approach for LeetCode 1008.

---

# Problem 55 — Inorder Successor in a BST

## 1. What is the problem?
LeetCode 285. Given a target value, find the node with the **next larger value** in the BST (the node that would come immediately after the target in an inorder traversal).

## 2. Example
The shared BST example, finding the successor of `6` (a value that actually exists in the tree) and of `2` (a value that doesn't).

## 3. My Code
```java
private static Node inorderSuccessor(Node root, Node target) {
    Node curr = root;
    Node successor = null;
    while (curr != null) {
        if (curr.data > target.data) {
            successor = curr;
            curr = curr.left;
        } else {
            curr = curr.right;
        }
    }
    return successor;
}
```

## 4. Issues / Bugs / Edge Cases
None — correct, including the case where `target`'s value actually exists as a node in the tree (see the dry run below — compare this against Problem 56, which gets the equivalent case wrong).

## 5. Intuition & Why This Approach
Walk down the tree comparing each node's value against the target: whenever `curr.data > target.data`, this node is a **valid candidate** for successor (record it) — but there might be an even smaller value that's still bigger than the target further down the **left** subtree, so keep going left to look for something closer. Whenever `curr.data <= target.data`, this node is too small (or is the target itself) — go right to look for something bigger. Using strict `>` here (not `>=`) is exactly what correctly excludes the target's own value from being considered its own successor.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `if (curr.data > target.data)` | Valid candidate — strictly bigger than target |
| `successor = curr; curr = curr.left;` | Record it, then look left for something even closer |
| `else { curr = curr.right; }` | Too small (or equal) — go right for something bigger |

## 7. Dry Run — `inorderSuccessor(root, Node(6))` (target value exists in the tree)
```text
curr=8: 8>6 → successor=8, curr=curr.left=3
curr=3: 3>6? no → curr=curr.right=6
curr=6: 6>6? no (equal, not strictly greater) → curr=curr.right=null
curr=null → loop ends
return successor=8
```
Result: **8** — correctly the next value after `6` in sorted order `1,3,6,8,10,14`.

**Contrast — `inorderSuccessor(root, Node(2))` (target doesn't exist in the tree):**
```text
curr=8: 8>2 → successor=8, curr=curr.left=3
curr=3: 3>2 → successor=3, curr=curr.left=1
curr=1: 1>2? no → curr=curr.right=null
curr=null → return successor=3
```
Result: **3** — correctly the smallest value greater than `2`.

## 8. Test Cases
| Target | Output |
|---|---|
| `6` (exists in tree) | node `8` |
| `2` (doesn't exist) | node `3` |
| `14` (the largest value) | `null` (nothing is bigger) |
| `0` (smaller than everything) | node `1` |

## Better / Alternative Approach
Already the standard, optimal O(h) iterative solution — this is the expected approach for LeetCode 285.

---

# Problem 56 — Inorder Predecessor in a BST ⚠️

## 1. What is the problem?
The mirror of Problem 55 — find the node with the **next smaller value** than the target.

## 2. Example
The shared BST example, finding the predecessor of `6` (exists in tree) and of `2` (doesn't exist) — chosen deliberately to expose a real bug.

## 3. My Code
```java
private static Node inorderPredecessor(Node root, Node target) {
    Node curr = root;
    Node pre = null;
    while (curr != null) {
        if (curr.data > target.data) {
            curr = curr.left;
        } else {
            pre = curr;
            curr = curr.right;
        }
    }
    return pre;
}
```

## 4. Issues / Bugs / Edge Cases
**Real bug, confirmed by dry run below:** the `else` branch triggers whenever `curr.data <= target.data` — which includes the case `curr.data == target.data`. That means if the target value actually exists as a node in the tree, this method records **the target's own node** as a candidate predecessor and then, finding nothing further right that's still `<=` target, ends up returning **the target itself** as its own predecessor — clearly wrong, since a predecessor must be strictly *less than* the target. Compare this directly against Problem 55's `inorderSuccessor`, which uses strict `>` for its recording condition and correctly avoids the equivalent trap. This bug never surfaces in the actual (commented-out) call in `main()` — `inorderPredecessor(root, new Node(2))` — purely by luck, since `2` doesn't exist as an actual node value in the tree.

## 5. Intuition & Why This Approach (as intended)
The *intended* logic mirrors Problem 55 exactly, flipped: whenever `curr.data` is strictly **less than** the target, it's a valid candidate (record it), then look **right** for something even closer (bigger, but still less than target). Whenever `curr.data` is **greater than or equal to** the target, go **left** instead (too big, or it's the target itself, which must be excluded from being its own predecessor).

## 6. Line-by-Line Walkthrough (as currently written)
| Line | What happens | Correct? |
|---|---|---|
| `if (curr.data > target.data) curr = curr.left;` | Too big — go left, don't record | ✅ correct |
| `else { pre = curr; curr = curr.right; }` | Triggers for `curr.data <= target.data`, **including equality** | ❌ the equality case should NOT be recorded |

## 7. Dry Run — `inorderPredecessor(root, Node(6))` (target exists in the tree — this is where it breaks)
```text
curr=8: 8>6 → curr=curr.left=3 (no record)
curr=3: 3>6? no → pre=3, curr=curr.right=6
curr=6: 6>6? no (equal falls into the else branch!) → pre=6 (overwrites the correct pre=3!), curr=curr.right=null
curr=null → loop ends
return pre=6
```
Result: **6** — **wrong**. The predecessor of `6` should be `3` (the largest value strictly less than `6` in `1,3,6,8,10,14`), but this returns `6` itself.

**Contrast — `inorderPredecessor(root, Node(2))` (target doesn't exist — happens to work correctly):**
```text
curr=8: 8>2 → curr=curr.left=3
curr=3: 3>2 → curr=curr.left=1
curr=1: 1>2? no → pre=1, curr=curr.right=null
curr=null → return pre=1
```
Result: **1** — correct, since `2` never exactly matches any node's value, the bug never gets triggered.

## 8. Test Cases
| Target | Output (current code) | Correct answer | Bug triggered? |
|---|---|---|---|
| `6` (exists in tree) | `6` | `3` | ❌ yes |
| `8` (exists in tree) | `8` | `6` | ❌ yes |
| `2` (doesn't exist) | `1` | `1` | ✅ no |
| `9` (doesn't exist) | `8` | `8` | ✅ no |

## Better / Alternative Approach
Fix by excluding equality from the "record" branch — use `>=` for the "go left, don't record" condition instead of strict `>`:
```java
private static Node inorderPredecessor(Node root, Node target) {
    Node curr = root;
    Node pre = null;
    while (curr != null) {
        if (curr.data >= target.data) {
            curr = curr.left;
        } else {
            pre = curr;
            curr = curr.right;
        }
    }
    return pre;
}
```
Re-tracing `inorderPredecessor(root, Node(6))` with this fix: `curr=8: 8>=6→left=3. curr=3: 3>=6? no→pre=3,right=6. curr=6: 6>=6→left=null. return pre=3.` — correct.

---

# Problem 57 — BST Iterator (Controlled Ascending / Descending Traversal) ⚠️

## 1. What is the problem?
LeetCode 173-style — build a stateful iterator object over a BST that yields values one at a time via repeated `next()` calls, using O(h) space (not O(n)) by never materializing the full traversal upfront. Your version is generalized to support **both** directions (smallest-to-largest, or largest-to-smallest) via a single `isReverse` flag — this is what powers Problem 58's two-pointer technique.

## 2. Example
The shared BST example, constructing the iterator both ways.

## 3. My Code
```java
class BSTIterator {
    private Stack<Node> st = new Stack<>();
    boolean isReverse;

    public BSTIterator(Node root, boolean isReverse) {
        this.isReverse = isReverse;
        pushAll(root);
    }

    private void pushAll(Node root) {
        Node curr = root;
        while (curr != null) {
            st.push(curr);
            if (isReverse) {
                curr = curr.left;
            } else {
                curr = curr.right;
            }
        }
    }

    public int next() {
        Node temp = st.pop();
        if (isReverse) {
            pushAll(temp.right);
        } else {
            pushAll(temp.left);
        }
        return temp.data;
    }

    public boolean hasNext() {
        return st.isEmpty();
    }
}
```

## 4. Issues / Bugs / Edge Cases — two real bugs here

**Bug 1 — `hasNext()` is inverted.** It returns `st.isEmpty()`, which is `true` when there's **nothing left**, and `false` when there **is** more to iterate — exactly backwards from what the name and every conventional Java iterator promises. A standard `while (iterator.hasNext())` loop using this class would never execute even once on a freshly-built iterator with data in it, since `hasNext()` would immediately report `false`. The fix is simply `return !st.isEmpty();`.

**Bug 2 — the `isReverse` flag's actual behavior is the opposite of its name.** Tracing through `pushAll` and `next()` (see the dry run below) reveals that `isReverse = false` actually produces **descending** order, and `isReverse = true` actually produces **ascending** order — precisely inverted from what any caller would reasonably assume given the name. This isn't just a cosmetic/naming issue: it's what breaks Problem 58 completely (see that entry for the full consequence).

Neither bug crashes anything — both fail silently, which is exactly what makes them dangerous.

## 5. Intuition & Why This Approach (and how the bugs happen)
The core technique is the same "push a path down, and when you pop a node, push its unexplored side" pattern from iterative inorder traversal (the original Trees handbook, Problem 4) — just wrapped in a resumable object instead of a single self-contained loop. For a genuinely **ascending** iterator: the constructor should push root, then keep going **left** (so the smallest reachable value ends up on top of the stack, ready to be the first result); after popping a node, `next()` should push its **right** subtree next (the standard "now explore what comes after this node" step in inorder logic).

**Here's exactly where the flag gets flipped:** the code writes `if (isReverse) { curr = curr.left; }` in `pushAll` — meaning `isReverse = true` is the branch that goes left first (which is actually the *ascending* behavior described above), while `isReverse = false` goes right first (which is actually *descending* behavior). The two methods (`pushAll` and `next()`) are internally *consistent* with each other under this same (inverted) sense of the flag — so the class isn't randomly broken, it's just **completely mislabeled**.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| constructor `pushAll(root)` | Builds the initial stack — which direction depends on the (mislabeled) flag |
| `while (isReverse) curr = curr.left else curr = curr.right` | The actual direction-determining logic — see bug 2 |
| `next()`: `pop`, then `pushAll` the popped node's *other* side | Standard resumable-inorder continuation — correctly paired with whichever direction `pushAll` used |
| `hasNext()`: `return st.isEmpty();` | See bug 1 — backwards |

## 7. Dry Run — both constructions on the shared BST example
**`new BSTIterator(root, false)`** (the flag says "not reversed," i.e. you'd expect ascending):
```text
Constructor pushAll(8), isReverse=false → goes RIGHT each time: push 8, curr=10; push 10, curr=14; push 14, curr=null.
Stack (bottom→top): [8, 10, 14]

next(): pop 14. isReverse=false → pushAll(14.left=null) → nothing pushed. return 14.
next(): pop 10. pushAll(10.left=null) → nothing. return 10.
next(): pop 8. pushAll(8.left=3): push 3, curr=6 (right); push 6, curr=null. Stack becomes [3, 6].
        return 8.
next(): pop 6. pushAll(6.left=null) → nothing. return 6.
next(): pop 3. pushAll(3.left=1): push 1, curr=null. Stack becomes [1].
        return 3.
next(): pop 1. return 1.
```
Sequence produced: **14, 10, 8, 6, 3, 1** — this is **descending** order, despite `isReverse = false`.

**`new BSTIterator(root, true)`** (the flag says "reversed," i.e. you'd expect descending):
```text
Constructor pushAll(8), isReverse=true → goes LEFT each time: push 8, curr=3; push 3, curr=1; push 1, curr=null.
Stack: [8, 3, 1]

next(): pop 1. pushAll(1.right=null) → nothing. return 1.
next(): pop 3. pushAll(3.right=6): push 6, curr=null. return 3.
next(): pop 6. pushAll(6.right=null) → nothing. return 6.
next(): pop 8. pushAll(8.right=10): push 10, curr=null. return 8.
next(): pop 10. pushAll(10.right=14): push 14, curr=null. return 10.
next(): pop 14. return 14.
```
Sequence produced: **1, 3, 6, 8, 10, 14** — this is **ascending** order, despite `isReverse = true`.

Confirmed: the flag's real-world meaning is the exact opposite of its name.

## 8. Test Cases
| Construction | Actual output sequence | What the name implies it should produce |
|---|---|---|
| `new BSTIterator(root, false)` | 14, 10, 8, 6, 3, 1 (descending) | ascending |
| `new BSTIterator(root, true)` | 1, 3, 6, 8, 10, 14 (ascending) | descending |
| `iterator.hasNext()` on a freshly-built, non-empty iterator | `false` | `true` |
| `iterator.hasNext()` after fully draining all values | `true` | `false` |

## Better / Alternative Approach
Two independent one-line fixes, both worth making:
```java
public boolean hasNext() {
    return !st.isEmpty();   // fixed: negate it
}
```
```java
// swap the direction inside pushAll so the flag means what it says:
if (isReverse) { curr = curr.right; } else { curr = curr.left; }
// and correspondingly in next():
if (isReverse) { pushAll(temp.left); } else { pushAll(temp.right); }
```
With both fixes applied, `isReverse = false` correctly gives ascending order and `isReverse = true` correctly gives descending order — and, critically, Problem 58's `twoSumFindTarget` would then work correctly *without needing any changes of its own*, since it already calls this class with the (originally-intended) correct semantics in mind.

---

# Problem 58 — Two Sum IV: Input is a BST ⚠️ (currently always returns `false`)

## 1. What is the problem?
LeetCode 653. Given a BST and a target sum `k`, determine whether any two **distinct** node values sum to exactly `k`.

## 2. Example
The shared BST example (`1, 3, 6, 8, 10, 14`), target `k = 4`. Note: `1 + 3 = 4`, so the correct answer is `true`.

## 3. My Code
```java
private static boolean twoSumFindTarget(Node root, int k) {
    if (root == null) {
        return false;
    }
    BSTIterator l = new BSTIterator(root, false); // left pointer
    BSTIterator r = new BSTIterator(root, true);  // right pointer

    int i = l.next();
    int j = r.next();
    while (i < j) {
        if (i + j < k) {
            i = l.next();
        } else if (i + j > k) {
            j = r.next();
        } else if (i + j == k) {
            return true;
        }
    }
    return false;
}
```

## 4. Issues / Bugs / Edge Cases
**This method is currently completely broken — it will return `false` for every input, regardless of whether a valid pair actually exists.** This isn't a new bug of its own; it's the direct, cascading consequence of Problem 57's flag-inversion bug. The comments reveal the clear intent: `l` (constructed with `isReverse=false`) is meant to be the ascending "left pointer" (starts at the smallest value), and `r` (constructed with `isReverse=true`) is meant to be the descending "right pointer" (starts at the largest value) — the classic two-pointer-on-sorted-data setup. But because `BSTIterator`'s flag is inverted, `l` actually produces **descending** values and `r` actually produces **ascending** values — exactly swapped from what this method assumes.

## 5. Intuition & Why This Approach (as intended) — and exactly how it breaks
The intended algorithm is the classic **two-pointer technique** applied to a BST treated as an implicit sorted sequence (via the two iterators, standing in for "start of the sorted array" and "end of the sorted array" without materializing the array): start `i` at the smallest value and `j` at the largest. If `i + j` is too small, the only way to increase it is to advance `i` forward (next smallest). If `i + j` is too big, advance `j` backward (next largest-but-smaller). If they ever match `k`, done. The loop naturally terminates when the two pointers cross (`i >= j`).

**Exactly how the bug breaks this:** because `l` is actually descending, `i = l.next()` returns the **largest** value first (`14` in the shared example) instead of the smallest. Because `r` is actually ascending, `j = r.next()` returns the **smallest** value first (`1`) instead of the largest. So the very first loop condition check is `while (i < j)` → `while (14 < 1)` → **false immediately** — the loop body never runs even once, and the function falls straight through to `return false`, no matter what `k` is or what pairs might actually exist in the tree.

## 6. Line-by-Line Walkthrough (showing where it goes wrong)
| Line | Intended behavior | Actual behavior (given Problem 57's bug) |
|---|---|---|
| `BSTIterator l = new BSTIterator(root, false);` | ascending iterator | **descending** iterator |
| `BSTIterator r = new BSTIterator(root, true);` | descending iterator | **ascending** iterator |
| `int i = l.next();` | smallest value | **largest** value |
| `int j = r.next();` | largest value | **smallest** value |
| `while (i < j)` | true initially (small < large) | **false immediately** (large is not < small) |

## 7. Dry Run — `twoSumFindTarget(root, 4)` as currently written
```text
l = BSTIterator(root, false)  → actually descending
r = BSTIterator(root, true)   → actually ascending
i = l.next() = 14   (l's first value, descending order starts at the max)
j = r.next() = 1    (r's first value, ascending order starts at the min)
while (14 < 1): false → loop never runs
return false
```
Result: **false** — **wrong**. `1 + 3 = 4` is a genuine valid pair sitting right there in the tree, but the method never even gets a chance to check it.

**Contrast — the same call, with Problem 57's bugs fixed (`isReverse` meaning corrected):**
```text
l = ascending iterator → i = l.next() = 1
r = descending iterator → j = r.next() = 14
while (1 < 14):
  i+j = 15 > 4 → j = r.next() = 10
while (1 < 10):
  i+j = 11 > 4 → j = r.next() = 8
while (1 < 8):
  i+j = 9 > 4 → j = r.next() = 6
while (1 < 6):
  i+j = 7 > 4 → j = r.next() = 3
while (1 < 3):
  i+j = 4 == 4 → return true
```
Result (with the fix): **true** — correct.

## 8. Test Cases
| k | Current output | Correct output (once Problem 57 is fixed) |
|---|---|---|
| 4 (1+3 exists) | `false` | `true` |
| 18 (8+10 exists) | `false` | `true` |
| 100 (no pair sums to this) | `false` | `false` (correctly, but for the wrong reason currently) |
| Single-node tree, any k | `false` | `false` (correctly — no distinct pair possible with one node) |

## Better / Alternative Approach
No changes are needed in this method itself — fixing `BSTIterator` (Problem 57) resolves this completely, since the algorithm's own two-pointer logic is already correct. This is a good example of why bugs in a shared utility class can silently propagate: this method's logic was never actually wrong, but it was built on a foundation that was.

---

# Problem 59 — Recover Binary Search Tree

## 1. What is the problem?
LeetCode 99. Exactly two nodes in a BST have had their **values** accidentally swapped (the tree's shape/structure is untouched — only two values are in the wrong spots). Find and fix them, restoring correct BST ordering, ideally in O(1) extra space beyond the recursion itself.

## 2. Example
Two custom examples, since this problem's whole point only shows up with an intentionally-corrupted tree:
- **Adjacent swap:** the shared BST with `3` and `6` swapped → `8 / (6 / (1, 3), 10 / (null, 14))`
- **Non-adjacent swap:** the shared BST with `3` and `10` swapped → `8 / (10 / (1, 6), 3 / (null, 14))`

## 3. My Code
```java
Node curr = null;
Node pre = null;
Node first = null;
Node mid = null;
Node second = null;

public void recoverBST(Node root) {
    inorderRecoverBST(root);
    if (second != null) {
        int temp = first.data;
        first.data = second.data;
        second.data = temp;
    } else {
        int temp = first.data;
        first.data = mid.data;
        mid.data = temp;
    }
}

public void inorderRecoverBST(Node root) {
    if (root == null) {
        return;
    }
    inorderRecoverBST(root.left);
    curr = root;
    if (pre != null) {
        if (pre.data > curr.data) {
            if (first == null) {
                first = pre;
                mid = curr;
            } else {
                second = curr;
            }
        }
    }
    pre = curr;
    inorderRecoverBST(root.right);
}
```

## 4. Issues / Bugs / Edge Cases
None — correct, standard implementation of the classic technique. Like `diameterOfTree` (Problem 13) and `CeilBSTrecursive` Version B (Problem 45), this relies on **instance fields** (`curr`, `pre`, `first`, `mid`, `second`) rather than parameters — meaning a fresh object (or a manual reset of all five fields) is required before each independent call, or leftover state from a previous call will corrupt the next one.

## 5. Intuition & Why "First Violation" and "Second Violation" Are Tracked Separately
A correct BST's inorder traversal is always strictly ascending. If exactly two values got swapped, the inorder sequence will show either **one** "drop" (if the swapped values were adjacent in sorted order) or **two** separate "drops" (if they weren't adjacent — swapping two distant values breaks ordering at two different points: once where the smaller value now sits too early, and once where the larger value now sits too late).

The algorithm walks the tree inorder, comparing each consecutive pair (`pre`, `curr`):
- **First time** `pre.data > curr.data` is found: `first = pre` (the too-large value, appearing too early) and `mid = curr` (the too-small value right after it — this is a *guess* that gets discarded if a second violation shows up).
- **If a second violation happens**, that confirms the swap was non-adjacent: `second = curr` becomes the true second half of the swapped pair (replacing the tentative guess `mid`), and `first` (from the first violation) remains correct as-is.

Finally: if `second` was ever set, swap `first` and `second` directly. Otherwise (only one violation was ever found — the adjacent case), swap `first` and `mid`.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `inorderRecoverBST(root.left);` | Standard inorder recursion — process everything smaller first |
| `if (pre != null && pre.data > curr.data)` | A violation — inorder ordering was broken between these two consecutive nodes |
| `if (first == null) { first = pre; mid = curr; }` | First violation — tentatively guess an adjacent swap |
| `else { second = curr; }` | A second violation happened — confirms non-adjacent, `mid`'s guess gets superseded |
| `pre = curr;` | Always advance, regardless of whether a violation was found this step |
| `if (second != null)` (in `recoverBST`) | Two violations found → swap the two confirmed nodes directly |
| `else` | Only one violation found → swap `first` and `mid` |

## 7. Dry Run A — Adjacent swap: `8 / (6 / (1, 3), 10 / (null, 14))` (originally `3` and `6` swapped)
```text
Inorder sequence visited: 1, 6, 3, 8, 10, 14
Compare consecutive pairs:
  pre=null,curr=1: pre is null, skip check. pre=1.
  pre=1,curr=6: 1>6? no. pre=6.
  pre=6,curr=3: 6>3? YES → first violation. first=null so far → first=6(node), mid=3(node). pre=3.
  pre=3,curr=8: 3>8? no. pre=8.
  pre=8,curr=10: no. pre=10.
  pre=10,curr=14: no. pre=14.
Only one violation total → second stays null.
recoverBST: second==null → swap first.data(6) and mid.data(3).
  first.data becomes 3, mid.data becomes 6.
```
Result: the node that erroneously held `6` is corrected back to `3`, and the node that erroneously held `3` is corrected back to `6` — tree fully restored to the original shared BST example.

## 7b. Dry Run B — Non-adjacent swap: `8 / (10 / (1, 6), 3 / (null, 14))` (originally `3` and `10` swapped)
```text
Inorder sequence visited: 1, 10, 6, 8, 3, 14
Compare consecutive pairs:
  pre=null,curr=1: skip. pre=1.
  pre=1,curr=10: 1>10? no. pre=10.
  pre=10,curr=6: 10>6? YES → first violation. first=10(node), mid=6(node). pre=6.
  pre=6,curr=8: 6>8? no. pre=8.
  pre=8,curr=3: 8>3? YES → SECOND violation. first is already set → second=3(node). pre=3.
  pre=3,curr=14: 3>14? no. pre=14.
Two violations found → second is not null.
recoverBST: swap first.data(10) and second.data(3).
  first.data becomes 3, second.data becomes 10.
```
Result: the node holding `10` (which was originally `3`'s position) gets corrected to `3`, and the node holding `3` (originally `10`'s position) gets corrected to `10` — tree fully restored, exactly matching the original shared BST example. Note `mid` (the node holding `6`) was never touched — it was only ever a placeholder guess for the adjacent case.

## 8. Test Cases
| Corrupted input | Detected `first` / `mid` / `second` | Result after `recoverBST` |
|---|---|---|
| `3` and `6` swapped (adjacent) | first=6, mid=3, second=null | fully restored original tree |
| `3` and `10` swapped (non-adjacent) | first=10, mid=6, second=3 | fully restored original tree |
| Root and a leaf swapped | depends on position, but always correctly detected via one or two violations | fully restored |
| Already-valid BST (no swap) | `first` stays `null` — calling `recoverBST` on an untouched valid tree would actually throw a `NullPointerException` on `first.data`, since the method assumes exactly two nodes are swapped, per the problem's guarantee | (not a valid input per the problem's own constraints) |

## Better / Alternative Approach
This is already the standard, optimal O(n) time / O(h) space (via recursion) solution — the exact accepted technique for LeetCode 99. A true O(1)-space version exists using **Morris inorder traversal** (the same threading technique from the original Trees handbook, Problem 36) instead of recursion, avoiding the O(h) call stack entirely — worth knowing as the "even more optimal" follow-up if an interviewer asks for constant space.

---

# Problem 60 — Largest BST Subtree

## 1. What is the problem?
LeetCode 333. Given a binary tree that might **not** be a valid BST overall, find the size (node count) of the **largest subtree** within it that *is* a valid BST.

## 2. Example
A custom tree with an embedded valid BST inside an otherwise-invalid structure:
```text
        10
       /  \
      5    15
     / \     \
    1   8     7
```
The left subtree (`5` with children `1`, `8`) is a valid 3-node BST. The right side (`15` with right child `7`) is **not** valid, since `7 < 15` can't legally sit in `15`'s right subtree. The whole tree, taken together, also isn't a valid BST. The correct answer here is `3`.

## 3. My Code
```java
class LargestBST {
    int size;
    int min;
    int max;
    LargestBST(int size, int max, int min) {
        this.size = size;
        this.max = max;
        this.min = min;
    }
}

private static int largestBST(Node root) {
    LargestBST ans = helper(root, new LargestBST(0, Integer.MIN_VALUE, Integer.MAX_VALUE));
    return ans.size;
}

private static LargestBST helper(Node root, LargestBST largestBST) {
    if (root == null) {
        return new LargestBST(0, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
    if (root.left == null && root.right == null) {
        return new LargestBST(1, root.data, root.data);
    }
    LargestBST l = helper(root.left, largestBST);
    LargestBST r = helper(root.right, largestBST);
    if (l.max < root.data && root.data < r.min) {
        return new LargestBST(l.size + r.size + 1,
            Math.max(Math.max(l.max, r.max), root.data),
            Math.min(Math.min(l.min, r.min), root.data));
    }
    return new LargestBST(Math.max(l.size, r.size), Integer.MAX_VALUE, Integer.MIN_VALUE);
}
```

## 4. Issues / Bugs / Edge Cases
No functional bug — this is correct (verified in detail below). Two things worth knowing, not because they're wrong, but because they're easy to misread:
- **The `helper` method's second parameter (`largestBST`) is never actually used anywhere inside the method body.** It's dead/vestigial — every `LargestBST` object used inside `helper` is freshly constructed via `new LargestBST(...)`, never read from the passed-in parameter. Harmless, but worth knowing it can be deleted from the method signature entirely with zero behavior change.
- **The constructor's parameter order is `(size, max, min)`** — note `max` comes before `min`, which is easy to misread as `(size, min, max)` at a glance. Every call site in this file gets the order right, but it's worth double-checking carefully if you ever add a new call.

## 5. Intuition & Why the "Poison Values" Trick Works
This extends the exact same bottom-up min/max-propagation idea from `checkBST`/`Info` (Problem 52), but now also tracking **size**, since the goal isn't just "is this a valid BST" but "how big is the largest valid BST anywhere in this structure."

The clever part is what gets returned when a subtree turns out **not** to be a valid BST: instead of returning `false` and stopping (like `checkBST` does), `helper` still returns a `LargestBST` object — with `size = Math.max(l.size, r.size)` (correctly carrying forward the best valid-BST size found *anywhere* further down, even through an invalid ancestor), but with `max = Integer.MAX_VALUE` and `min = Integer.MIN_VALUE` — deliberately extreme, "poisoned" sentinel values. These poisoned bounds guarantee that **no ancestor above this point can ever successfully merge this subtree into a larger valid BST** (since `MAX_VALUE` can never be `< ` any finite value, and no finite value can ever be `< MIN_VALUE`) — exactly the correct behavior, since an already-broken subtree can never be part of a valid larger one. Meanwhile, the `size` value keeps propagating upward unaffected by the poisoning, so the best answer found anywhere is never lost.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| null base case: `LargestBST(0, MIN_VALUE, MAX_VALUE)` | Same inverted-sentinel trick as `Info` — an empty subtree can never accidentally win a min/max comparison |
| leaf base case: `LargestBST(1, root.data, root.data)` | A single node is trivially a valid BST of size 1 |
| `if (l.max < root.data && root.data < r.min)` | The actual BST-validity check — does the current root fit strictly between the true extremes of its left and right subtrees? |
| valid case: `size = l.size + r.size + 1` | Both children are valid BSTs that fit together correctly — combine them plus the current node |
| invalid case: `size = Math.max(l.size, r.size)` | Can't combine — but propagate whichever child had the better answer, poisoning the bounds so this broken piece can't be used by an ancestor |

## 7. Dry Run — `largestBST(root)` on the custom example tree
```text
helper(10):
  l = helper(5):
    l2 = helper(1) = LargestBST(1, 1, 1)   [leaf]
    r2 = helper(8) = LargestBST(1, 8, 8)   [leaf]
    check: l2.max(1) < 5 && 5 < r2.min(8)? 1<5 true, 5<8 true → VALID merge
    return LargestBST(1+1+1=3, max(1,8,5)=8, min(1,8,5)=1)
  l = LargestBST(size=3, max=8, min=1)

  r = helper(15):
    l3 = helper(15.left=null) = LargestBST(0, MIN_VALUE, MAX_VALUE)
    r3 = helper(7) = LargestBST(1, 7, 7)   [leaf]
    check: l3.max(MIN_VALUE) < 15 && 15 < r3.min(7)? first true, but 15<7 FALSE → INVALID
    return LargestBST(max(0,1)=1, MAX_VALUE, MIN_VALUE)   [poisoned]
  r = LargestBST(size=1, max=MAX_VALUE, min=MIN_VALUE)   [poisoned]

  Back at root=10: check l.max(8) < 10 && 10 < r.min(MIN_VALUE)?
    8<10 true, but 10 < MIN_VALUE? FALSE → INVALID (as expected — r was already poisoned)
  return LargestBST(max(l.size=3, r.size=1)=3, MAX_VALUE, MIN_VALUE)   [poisoned]

largestBST(root) returns ans.size = 3
```
Result: **3** — correctly identifies the `{5, 1, 8}` subtree as the largest valid BST, even though it had to "see through" the invalid right side of the tree to find it.

## 8. Test Cases
| Input | Output |
|---|---|
| Custom example above | 3 |
| The shared BST example (entirely valid) | 6 (the whole tree) |
| A tree that's entirely invalid everywhere (no two adjacent nodes satisfy BST ordering) | 1 (every single node, in isolation, is trivially a valid "BST" of size 1) |
| Single node | 1 |
| Empty tree | 0 |

## Better / Alternative Approach
This is already the standard, optimal single-pass O(n) solution for LeetCode 333 — genuinely the same core technique as `checkBST` (Problem 52), extended to also carry a size. No meaningful improvement exists beyond removing the two harmless-but-confusing details noted above (the unused parameter, and the easy-to-misread constructor argument order) for clarity's sake.

---

## 📎 Appendix — Full Method-to-LeetCode/GFG Cross-Reference

| # | Method | Reference |
|---|---|---|
| 1 | pre/in/postOrder | LC 144 / 94 / 145 |
| 2 | levelWiseTraversal | LC 102 |
| 3 | iterativePreOrder | LC 144 (iterative) |
| 4 | iterativeInOrder | LC 94 (iterative) |
| 5 | iterativePostOrder (2-stack) | LC 145 (iterative) |
| 6 | iterativePostOrderSingleStack | LC 145 (optimized) |
| 7 | maxDepth | LC 104 |
| 8 | minDepth | LC 111 |
| 9 | countNodes | LC 222 (naive) |
| 10 | isSameTree | LC 100 |
| 11 | invertTree | LC 226 |
| 12 | hasPathSum | LC 112 |
| 13 | diameterOfTree | LC 543 |
| 14 | isBalanced | LC 110 |
| 15 | BinaryTreePath | LC 257 |
| 16 | maxPathSum | LC 124 |
| 17 | zigzagLevelOrder | LC 103 |
| 18 | boundaryTraversalInAntiClockWise | GFG |
| 19 | verticalTraversal | LC 987 |
| 20 | topViewOfBinaryTree | GFG |
| 21 | bottomViewOfBinaryTree | GFG |
| 22 | rightViewOfBinaryTree | LC 199 |
| 23 | leftViewOfBInaryTree | GFG |
| 24 | isSymmetric | LC 101 |
| 25 | rootToNodePath | GFG |
| 26 | lowestCommonAncestors | LC 236 |
| 27 | MaximumWidthofBinaryTree | LC 662 |
| 28 | ChildrenSumProperty | GFG |
| 29 | distanceK | LC 863 |
| 30 | BurningTree | GFG (1376-style) |
| 31 | CountNodeCBT | LC 222 (optimal) |
| 32 | buildTreePreOrderInorder | LC 105 |
| 33 | buildTreePostOrderInorder | LC 106 |
| 34 | serialize | LC 297 |
| 35 | deserialize | LC 297 |
| 36 | morrisInorder | LC 94 (O(1) space) |
| 37 | FlattenTree | LC 114 |
| 38 | FlattenTreeReversePreOrder | LC 114 (optimal recursive) |
| 39 | FlattenTreeIterative | LC 114 (iterative) |
| 40 | FlattenTreeMorries | LC 114 (O(1) space) |
| 41 | SearchBinarySearchTree | LC 700 |
| 42 | SearchBSTiterative | LC 700 (iterative) |
| 43 | CeilBST | GFG |
| 44 | CeilBSTrecursive (A) | GFG |
| 45 | CeilBSTrecursive (B) | GFG |
| 46 | floorBSTiterative | GFG |
| 47 | floorBSTRecursive | GFG |
| 48 | insertGivenNodeInBST | LC 701 |
| 49 | deleteTheKey | LC 450 |
| 50 | kthSmallest | LC 230 |
| 51 | validBST | LC 98 |
| 52 | checkBST / validBST2 | LC 98 (alt. technique) |
| 53 | LCAbst | LC 235 |
| 54 | bstFromPreorder | LC 1008 |
| 55 | inorderSuccessor | LC 285 |
| 56 | inorderPredecessor | GFG |
| 57 | BSTIterator | LC 173 (generalized) |
| 58 | twoSumFindTarget | LC 653 |
| 59 | recoverBST | LC 99 |
| 60 | largestBST | LC 333 |

---

*End of handbook. 60 methods, every one with your original code preserved, a plain-language problem statement, intuition, a full dry run, and test cases. Revisit the Progression Index at the top to jump straight to whichever one you've forgotten. Methods flagged ⚠️ in the index (Problems 53, 56, 57, 58) have real, verified bugs — worth reading those four closely, especially the 57→58 chain, since it's a good lesson in how a bug in a shared utility class can silently break something that otherwise looks completely correct.*