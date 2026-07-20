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
    int max = 0;
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
//        countNodes(root);
//        invertTree(root);
//        hasPathSum(root,5);
//        Tree t = new Tree();
//        t.diameterOfTree(root);
//        System.out.println(t.max);
//        isBalanced(root);
        BinaryTreePath(root);
    }
    private static List<String> BinaryTreePath(Node root) {
        List<String> al = new ArrayList<>();

        if(root == null) {
            return al;
        }

        String path = root.data + "";
        helper(root, al, path);
        return al;
    }

	private static void helper(Node root, List<String> al, String path) {
		if(root == null) {
			return;
		}
		if(root.left == null && root.right == null) {
			al.add(path);
			return;
		}
		if(root.left != null) {
			helper(root.left,al,path +"->" + root.left.data);
		}
		if(root.right != null) {
			helper(root.right,al,path +"->" + root.right.data);
		}
		
	}

	private static boolean isBalanced(Node root) {
		if(helper(root) == -1) {
			return false;
		}
		return true;
		
	}

	private static int helper(Node root) {
		if(root == null) {
			return 0;
		}
		int lh = helper(root.left);
		if(lh == -1) {
			return -1;
		}
		int rh = helper(root.right);
		if(rh == -1) {
			return -1;
		}
		int diff = Math.abs(lh-rh);
		if(diff > 1) {
			return -1;
		}
		return 1 + Math.max(lh, rh);
	}

	private  int diameterOfTree(Node root) {
		if(root == null) {
			return 0;
		}
		int lh = diameterOfTree(root.left);
		int rh = diameterOfTree(root.right);
		max = Math.max(max,lh+rh);
		return 1 + Math.max(lh,rh);
	}

	private static boolean hasPathSum(Node root, int targetSum) {
		if(root.left == null && root.right == null && targetSum - root.data == 0) {
			return true;
		}
		if(root.left == null && root.right == null) {
			return false;
		}
		targetSum = targetSum-root.data;
		return hasPathSum(root.left,targetSum) || hasPathSum(root.right,targetSum);
		
	}

	private static Node invertTree(Node root) {
		
    	if(root == null) {
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
    	if(root==null) {
    		return 0;
    	}
		int left = countNodes(root.left);
		int right = countNodes(root.right);
		return 1+ left + right;
		
	}
    public boolean isSameTree(Node p, Node q) {
        if(p == null && q == null){
            return true;
        }
        if(p == null || q == null || p.data != q.data){
            return false;
        }
        return isSameTree(p.left,q.left) && isSameTree(p.right,q.right);
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