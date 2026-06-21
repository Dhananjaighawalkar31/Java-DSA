package Stack;

public class Stack {
	public class Node{
		int val;
		Node next;
		Node(int val){
			this.val = val;
		}
	}
	private Node top;
	private int size;
	public Stack(){
		top = null;
		size = 0;
	}

	public static void main(String[] args) {
		Stack s = new Stack();
		s.push(10);
		s.push(20);
		System.out.println(s.pop());
		System.out.println(s.peek());
		System.out.println(s.isEmpty());
		System.out.println(s.size());
		
	}

	private int peek() {
		// TODO Auto-generated method stub
		int p = 0;
		return p;
	}

	private boolean isEmpty() {
		// TODO Auto-generated method stub
		return true;
	}

	private int size() {
		// TODO Auto-generated method stub
		
		return size;
	}

	private int pop() {
		// TODO Auto-generated method stub
		int p = 0;
		return p;
	}

	private void push(int val) {
		Node t = new Node(val);
		t.next = top;
		top = t;
		
	}

}
