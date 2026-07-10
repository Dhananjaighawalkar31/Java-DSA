package Trees;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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
        levelWiseTraversal(root);
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

//	private static void inOrder(Node root) {
//		if(root == null) {
//			return;
//		}
//		inOrder(root.left);
//		System.out.println(root.data);
//		inOrder(root.right);
//		
//	}

//	private static void postOrder(Node root) {
//		if(root == null) {
//			return;
//		}
//  postOrder(root.left);
//		postOrder(root.right);
//		System.out.println(root.data);
//		
//	}

	private static void preOrder(Node root) {
		if(root == null) {
			return;
		}
		System.out.println(root.data);
		preOrder(root.left);
		preOrder(root.right);
		
	}
}