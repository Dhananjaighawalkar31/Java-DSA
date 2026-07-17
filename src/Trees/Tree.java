package Trees;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

class Node {

    int data;
    Node left;
    Node right;

    Node(int data){
        this.data = data;
       
    }
}

class Tree {

    public static void main(String[] args) {

        Node root = new Node(1);

        root.left = new Node(2);
        root.right = new Node(3);

        root.left.left = new Node(4);
        root.left.right = new Node(5);

        root.right.left = new Node(6);
//        preOrder(root);
//        postOrder(root);
//        inOrder(root);
//        levelWiseTraversal(root);
//        iterativePreOrder(root);
//        iterativeInOrder(root);
//        iterativePostOrder(root);
//        maxDepth(root);
//        minDepth(root);
        countNodes(root);
    }

    private static int countNodes(Node root) {
    	if(root==null) {
    		return 0;
    	}
		int left = countNodes(root.left);
		int right = countNodes(root.right);
		return 1+ left + right;
		
	}

	private static int minDepth(Node root) {
    	if(root == null) {
    		return 0;
    	}
		if(root.right == null && root.left == null) {
			return 1;
		}
		int l = 0;
		int r = 0;
		if(root.left != null) {
			l = minDepth(root.left);
		}
		if(root.right != null) {
			r = minDepth(root.right);
		}
		if(l>0 && r >0) {
			return 1 + Math.min(l, r);
		}
		if(l>0 && r==0) {
			return 1 + l;
		}
		return 1+r;
		
	}

	private static int maxDepth(Node root) {
		if(root == null) {
			return 0;
		}
		int a = maxDepth(root.left);
		int b = maxDepth(root.right);
		return 1+Math.max(a, b);
		
	}

	private static List<Integer> iterativePostOrder(Node root) {
		List<Integer> al = new ArrayList<Integer>();
		if(root == null) {
			return al;
		}
		Stack<Node> st1 = new Stack<>();
		Stack<Node> st2 = new Stack<>();
		st1.push(root);
		Node x = root;
		while(!st1.isEmpty()) {
			x = st1.pop();
			st2.push(x);
			if(x.left != null) {
				st1.push(x.left);
			}
			if(x.right != null) {
				st1.push(x.right);
			}
		}
		while(!st2.isEmpty()) {
			Node k = st2.pop();
			al.add(k.data);
		}
		return al;
	}

	private static List<Integer> iterativeInOrder(Node root) {
		List<Integer> al = new ArrayList<>();
		Stack<Node> st = new Stack<>();
		Node node = root;
		while(true) {
			if(node != null) {
				st.push(node);
				node = node.left;
			}else {
				if(st.isEmpty()) {
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
		while(!st.isEmpty()) {
			Node curr = st.pop();
			System.out.print(curr.data);
			if(curr.right != null) {
				st.push(curr.right);
			}
			if(curr.left != null) {
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
		if(root == null) {
			return;
		}
		inOrder(root.left);
		System.out.println(root.data);
		inOrder(root.right);
		
	}

	private static void postOrder(Node root) {
		if(root == null) {
			return;
		}
		postOrder(root.left);
		postOrder(root.right);
		System.out.println(root.data);
		
	}

	private static void preOrder(Node root) {
		if(root == null) {
			return;
		}
		System.out.println(root.data);
		preOrder(root.left);
		preOrder(root.right);
		
	}
}