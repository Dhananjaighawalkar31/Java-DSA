package Queue;

public class QueueUsingLinkedList {
	public Node start;
	public Node end;
	int size;
	
	class Node{
		int val;
		Node next;
		Node(int val){
			this.val = val;
		}
	}
	QueueUsingLinkedList(){
		start = null;
		end = null;
		size = 0;
	}

	public static void main(String[] args) throws Exception {
		QueueUsingLinkedList q = new QueueUsingLinkedList();
		q.push(7);
		q.push(2);
		q.push(3);
		q.push(5);
		System.out.println(q.pop());
		System.out.println(q.top());
		System.out.println(q.pop());
		System.out.println(q.pop());
		System.out.println(q.top());


		
	}

	private void push(int num) {
		if(size<0) {
			System.out.println("not valid");
			return;
		}
		Node n = new Node(num);
		if(size == 0) {
			
			start = n;
			end = n;
			size++;
		}else {
			end.next = n;
			end = n;
			size++;
		}
		
	}

	private int top() throws Exception {
		if(start == null) {
			throw new Exception("Queue is empty");
		}
		int k = start.val;
		return k;
	}

	private int pop() throws Exception {
		if(start == null) {
			throw new Exception("Queue is empty");
		}
		int k = start.val;
		start = start.next;
		if(start == null) {
			end = null;
		}
		size--;
		return k;
	}

}
