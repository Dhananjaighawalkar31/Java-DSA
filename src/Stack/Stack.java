package Stack;

public class Stack {
	class Node{
		Node next;
		int val;
		Node(int val){
			this.val  = val;
		}
	}
	Node top;
	int size;
	Stack(){
		top = null;
		size = 0;
	}
	@Override
	public String toString() {
	    String res = "Stack -> ";

	    Node t = top;

	    while(t != null) {
	        res += t.val + " ";
	        t = t.next;
	    }

	    return res;
	}
	public boolean isEmpty() {
	    return top == null;
	}
	public int size() {
		return this.size;
	}
	

	public int pop() {
		if(top == null) {
	        throw new RuntimeException("Stack is Empty");
	    }
		int k = top.val;
		top = top.next;
		size--;
		return k;
	}

	public int peek() {
		if(top == null) {
	        throw new RuntimeException("Stack is Empty");
	    }
		return top.val;
		
	}

	public void push(int val) {
		Node n = new Node(val);
		n.next = top;
		top = n;
		size++;
		
	}
	public static void main(String[] args) {
		Stack st = new Stack();
		st.push(4);
		st.push(2);
		st.push(3);
		st.push(1);
		System.out.println(st.peek());
		System.out.println(st.pop());
		st.push(7);
		System.out.println(st.size());;
		System.out.println(st);
	}


//	public class Node{
//	int val;
//	Node next;
//	Node(int val){
//		this.val = val;
//	}
//}
//private Node top;
//private int size;
//public Stack(){
//	top = null;
//	size = 0;
//}
//
//public static void main(String[] args) {
//	Stack s = new Stack();
//	s.push(10);
//	s.push(20);
//	System.out.println(s.pop());
//	System.out.println(s.peek());
//	System.out.println(s.isEmpty());
//	System.out.println(s.size());
//	
//}
//
//private int peek() {
//	// TODO Auto-generated method stub
//	int p = 0;
//	return p;
//}
//
//private boolean isEmpty() {
//	// TODO Auto-generated method stub
//	return true;
//}
//
//private int size() {
//	// TODO Auto-generated method stub
//	
//	return size;
//}
//
//private int pop() {
//	// TODO Auto-generated method stub
//	int p = 0;
//	return p;
//}
//
//private void push(int val) {
//	Node t = new Node(val);
//	t.next = top;
//	top = t;
//	
//}
}
