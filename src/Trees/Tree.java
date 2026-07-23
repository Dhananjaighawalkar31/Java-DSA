package Trees;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import java.util.TreeMap;

class Node {

	int data;
	Node left;
	Node right;

	Node(int data) {
		this.data = data;

	}
}

class Tuple {
	Node node;
	int column;
	int row;

	public Tuple(Node node, int column, int row) {
		this.node = node;
		this.column = column;
		this.row = row;
	}
}
class NodeWithLine {
	Node node;
	int line;
	public NodeWithLine (Node node,	int line) {
		this.node = node;
		this.line = line;
	}
}

class Tree {
	int max = 0;

	public static void main(String[] args) {

		Node root = new Node(1);

		root.left = new Node(2);
		root.right = new Node(3);

		root.left.left = new Node(4);
		root.left.right = new Node(5);

		root.right.left = new Node(6);
		Tree t = new Tree();
//        preOrder(root);
//        postOrder(root);
//        inOrder(root);
//        levelWiseTraversal(root);
//        iterativePreOrder(root);
//        iterativeInOrder(root);
//        iterativePostOrder(root);
//        maxDepth(root);
//        minDepth(root);
//        countNodes(root);
//        invertTree(root);
//        hasPathSum(root,5);
//        Tree t = new Tree();
//        t.diameterOfTree(root);
//        System.out.println(t.max);
//        isBalanced(root);
//        BinaryTreePath(root);
//        t.maxPathSum(root);
//        zigzagLevelOrder(root);
//		List<List<Integer>> ans = t.verticalTraversal(root);
//
//		for (List<Integer> list : ans) {
//			System.out.println(list);
//		}
//		topViewOfBinaryTree(root);
		bottomViewOfBinaryTree(root);
	}

	private static List<Integer> bottomViewOfBinaryTree(Node root) {
		List<Integer> al = new ArrayList<>();
		if(root == null) {
			return al;
		}
		Queue<NodeWithLine > q = new LinkedList<>();
		TreeMap<Integer,Node> map = new TreeMap<>();
		q.offer(new NodeWithLine (root,0));
		while(!q.isEmpty()) {
			NodeWithLine  tv = q.poll();
			Node node = tv.node;
			int line = tv.line;
//			map.putIfAbsent(line, node);
//			if(!map.containsKey(line)) {
//				map.put(line, node);
//			}else {
				map.put(line, node);
//			}
			if(node.left != null) {
				q.offer(new NodeWithLine (node.left,line-1));
			}
			if(node.right != null) {
				q.offer(new NodeWithLine (node.right,line+1));
			}
		}
		for(Node rows : map.values()) {
			al.add(rows.data);
		}
		return al;
	}

	private static List<Integer> topViewOfBinaryTree(Node root) {
		List<Integer> al = new ArrayList<>();
		if(root == null) {
			return al;
		}
		TreeMap<Integer,Node> map = new TreeMap<>();
		Queue<NodeWithLine > q = new LinkedList<>();
		NodeWithLine  tv = new NodeWithLine (root,0);
		q.offer(tv);
		while(!q.isEmpty()) {
			NodeWithLine  dummy = q.poll();
			int line = dummy.line;
			Node node = dummy.node;
			if(!map.containsKey(line)) {  //map.putIfAbsent(line, node);
				map.put(line, node);
			}
			if(node.left != null) {
				q.offer(new NodeWithLine (node.left,line-1));
			}
			if(node.right != null) {
				q.offer(new NodeWithLine (node.right,line+1));
			}
		}
		for(Node rows : map.values()) {
			al.add(rows.data);
		}
		return al;
		
	}

	public List<List<Integer>> verticalTraversal(Node root) {

		List<List<Integer>> ans = new ArrayList<>();

		if (root == null)
			return ans;

		TreeMap<Integer, TreeMap<Integer, PriorityQueue<Integer>>> map = new TreeMap<>();

		Queue<Tuple> q = new LinkedList<>();

		// Root starts at column = 0, row = 0
		q.offer(new Tuple(root, 0, 0));

		while (!q.isEmpty()) {

			Tuple tuple = q.poll();

			Node node = tuple.node;
			int column = tuple.column;
			int row = tuple.row;

			// Create column if it doesn't exist
			if (!map.containsKey(column)) {
				map.put(column, new TreeMap<>());
			}

			// Create row inside that column if it doesn't exist
			if (!map.get(column).containsKey(row)) {
				map.get(column).put(row, new PriorityQueue<>());
			}

			// Store node value
			map.get(column).get(row).offer(node.data);

			// Left child
			if (node.left != null) {
				q.offer(new Tuple(node.left, column - 1, row + 1));
			}

			// Right child
			if (node.right != null) {
				q.offer(new Tuple(node.right, column + 1, row + 1));
			}
		}

		// Read values from TreeMap
		for (TreeMap<Integer, PriorityQueue<Integer>> rows : map.values()) {

			List<Integer> list = new ArrayList<>();

			for (PriorityQueue<Integer> pq : rows.values()) {

				while (!pq.isEmpty()) {
					list.add(pq.poll());
				}
			}

			ans.add(list);
		}

		return ans;
	}

	private static List<Integer> boundaryTraversalInAntiClockWise(Node root) {
		if (root == null) {
			return null;
		}
		List<Integer> al = new ArrayList<>();
		if (!isLeaf(root)) {
			al.add(root.data);
		}
		if (root.left != null) {
			getLeft(root.left, al);
		}
		getLeaf(root, al);
		if (root.right != null) {
			getRight(root.right, al);
		}
		return al;

	}

	private static void getRight(Node root, List<Integer> al) {
		Stack<Integer> st = new Stack<>();
		Node curr = root;
		while (curr != null) {
			if (curr.right != null) {
				if (!isLeaf(curr)) {
					st.push(curr.data);
				}
				curr = curr.right;
			} else {
				if (!isLeaf(curr)) {
					st.push(curr.data);
				}
				curr = curr.left;
			}
		}
		while (!st.isEmpty()) {
			al.add(st.pop());
		}

	}

	private static void getLeaf(Node root, List<Integer> al) {
		if (root == null) {
			return;
		}
		getLeaf(root.left, al);
		if (isLeaf(root)) {
			al.add(root.data);
		}
		getLeaf(root.right, al);

	}

	private static void getLeft(Node root, List<Integer> al) {
		Node curr = root;
		while (curr != null) {
			if (curr.left != null) {
				if (!isLeaf(curr)) {
					al.add(curr.data);
				}
				curr = curr.left;
			} else {
				if (!isLeaf(curr)) {
					al.add(curr.data);
				}
				curr = curr.right;
			}
		}

	}

	private static boolean isLeaf(Node root) {
		if (root.left == null && root.right == null) {
			return true;
		}
		return false;
	}

	private static List<List<Integer>> zigzagLevelOrder(Node root) {
		List<List<Integer>> res = new ArrayList<>();
		if (root == null) {
			return res;
		}
		Queue<Node> q = new ArrayDeque<>();
		q.offer(root);
		boolean b = true;
		while (!q.isEmpty()) {
			int n = q.size();
			List<Integer> al = new ArrayList<>();
			for (int i = 0; i < n; i++) {
				Node t = q.poll();
				if (t.left != null) {
					q.offer(t.left);
				}
				if (t.right != null) {
					q.offer(t.right);
				}
				al.add(t.data);
			}
			if (b) {
				res.add(al);
			} else {
				Collections.reverse(al);
				res.add(al);
			}
			b = !b;
		}
		return res;

	}

	int maxi = 0;

	private int maxPathSum(Node root) {
		maxi = root.data;
		helpermaxPathSum(root);
		return maxi;
	}

	private int helpermaxPathSum(Node root) {
		if (root == null) {
			return 0;
		}
		int lh = helpermaxPathSum(root.left);
		int rh = helpermaxPathSum(root.right);
		if (lh < 0) {
			lh = 0;
		}
		if (rh < 0) {
			rh = 0;
		}
		maxi = Math.max(maxi, root.data + lh + rh);
		return root.data + Math.max(lh, rh);
	}

	private static List<String> BinaryTreePath(Node root) {
		List<String> al = new ArrayList<>();

		if (root == null) {
			return al;
		}

		String path = root.data + "";
		helper(root, al, path);
		return al;
	}

	private static void helper(Node root, List<String> al, String path) {
		if (root == null) {
			return;
		}
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

	private static boolean isBalanced(Node root) {
		if (helper(root) == -1) {
			return false;
		}
		return true;

	}

	private static int helper(Node root) {
		if (root == null) {
			return 0;
		}
		int lh = helper(root.left);
		if (lh == -1) {
			return -1;
		}
		int rh = helper(root.right);
		if (rh == -1) {
			return -1;
		}
		int diff = Math.abs(lh - rh);
		if (diff > 1) {
			return -1;
		}
		return 1 + Math.max(lh, rh);
	}

	private int diameterOfTree(Node root) {
		if (root == null) {
			return 0;
		}
		int lh = diameterOfTree(root.left);
		int rh = diameterOfTree(root.right);
		max = Math.max(max, lh + rh);
		return 1 + Math.max(lh, rh);
	}

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

	private static Node invertTree(Node root) {

		if (root == null) {
			return null;
		}

		invertTree(root.left);
		invertTree(root.right);
		Node dummy = root.left;
		root.left = root.right;
		root.right = dummy;
		return root;

	}

	private static int countNodes(Node root) {
		if (root == null) {
			return 0;
		}
		int left = countNodes(root.left);
		int right = countNodes(root.right);
		return 1 + left + right;

	}

	public boolean isSameTree(Node p, Node q) {
		if (p == null && q == null) {
			return true;
		}
		if (p == null || q == null || p.data != q.data) {
			return false;
		}
		return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
	}

	private static int minDepth(Node root) {
		if (root == null) {
			return 0;
		}
		if (root.right == null && root.left == null) {
			return 1;
		}
		int l = 0;
		int r = 0;
		if (root.left != null) {
			l = minDepth(root.left);
		}
		if (root.right != null) {
			r = minDepth(root.right);
		}
		if (l > 0 && r > 0) {
			return 1 + Math.min(l, r);
		}
		if (l > 0 && r == 0) {
			return 1 + l;
		}
		return 1 + r;

	}

	private static int maxDepth(Node root) {
		if (root == null) {
			return 0;
		}
		int a = maxDepth(root.left);
		int b = maxDepth(root.right);
		return 1 + Math.max(a, b);

	}

	private static List<Integer> iterativePostOrder(Node root) {
		List<Integer> al = new ArrayList<Integer>();
		if (root == null) {
			return al;
		}
		Stack<Node> st1 = new Stack<>();
		Stack<Node> st2 = new Stack<>();
		st1.push(root);
		Node x = root;
		while (!st1.isEmpty()) {
			x = st1.pop();
			st2.push(x);
			if (x.left != null) {
				st1.push(x.left);
			}
			if (x.right != null) {
				st1.push(x.right);
			}
		}
		while (!st2.isEmpty()) {
			Node k = st2.pop();
			al.add(k.data);
		}
		return al;
	}

	private static List<Integer> iterativeInOrder(Node root) {
		List<Integer> al = new ArrayList<>();
		Stack<Node> st = new Stack<>();
		Node node = root;
		while (true) {
			if (node != null) {
				st.push(node);
				node = node.left;
			} else {
				if (st.isEmpty()) {
					break;
				}
				node = st.pop();
				al.add(node.data);
				node = node.right;
			}
		}
		return al;

	}

	private static void iterativePreOrder(Node root) {
		Stack<Node> st = new Stack<>();
		st.push(root);
		while (!st.isEmpty()) {
			Node curr = st.pop();
			System.out.print(curr.data);
			if (curr.right != null) {
				st.push(curr.right);
			}
			if (curr.left != null) {
				st.push(curr.left);
			}
		}

	}

	private static List<List<Integer>> levelWiseTraversal(Node root) {

		List<List<Integer>> res = new ArrayList<>();

		if (root == null) {
			return res;
		}

		Queue<Node> q = new LinkedList<>();
		q.offer(root);

		while (!q.isEmpty()) {

			int levelSize = q.size();

			List<Integer> level = new ArrayList<>();

			for (int i = 0; i < levelSize; i++) {

				Node node = q.poll();

				level.add(node.data);

				if (node.left != null) {
					q.offer(node.left);
				}

				if (node.right != null) {
					q.offer(node.right);
				}
			}

			res.add(level);
		}

		return res;
	}

	private static void inOrder(Node root) {
		if (root == null) {
			return;
		}
		inOrder(root.left);
		System.out.println(root.data);
		inOrder(root.right);

	}

	private static void postOrder(Node root) {
		if (root == null) {
			return;
		}
		postOrder(root.left);
		postOrder(root.right);
		System.out.println(root.data);

	}

	private static void preOrder(Node root) {
		if (root == null) {
			return;
		}
		System.out.println(root.data);
		preOrder(root.left);
		preOrder(root.right);

	}
}