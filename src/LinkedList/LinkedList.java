package LinkedList;

public class LinkedList {
	Node head;
	int size;
	class Node{
		int val;
		Node next;
		Node(int val){
			this.val = val;
		}
	}
	public LinkedList(){
		head = null;
		size = 0;
	}
	public static void main(String[] args) throws Exception {
		LinkedList li = new LinkedList();
		li.addAtHead(10);
		li.addAtIndex(20,1);
		li.addAtTail(30);
		li.addAtIndex(100,2);
		li.print(li.head);
//		li.head = li.reverseLL(li.head);
//		li.print(li.head);
//		System.out.println(li.get(2));
//		li.head = li.delete(3);
//		li.print(li.head);
//		System.out.println(li.contains(1000));
		System.out.println(li.findIndex(100));
	}

	public void addAtHead(int i){
		Node n = new Node(i);
		n.next = head;
		head = n;
		size++;
		
	}
	public void addAtIndex(int val, int i) throws Exception {
		if(i<0 || i>size) {
			throw new Exception("Wrong index");
		}
		if(i==0) {
			addAtHead(val);
		}
		else if(i==size) {
			addAtTail(val);
		}else {
			Node t = head;
			for(int j = 0;j<i-1;j++) {
				t = t.next;
			}
			Node n = new Node(val);
			n.next = t.next;
			t.next = n;
			size++;
		}
		
		
	}
	public void addAtTail(int val) {
		if(head == null) {
			System.out.println("empty tail");
		}
		Node t = head;
		while(t.next != null) {
			t = t.next;
		}
		t.next = new Node(val);
		size++;
		
	}

	public void print(Node temp) {

	    
	    while(temp != null) {
	        System.out.print(temp.val + " ");
	        temp = temp.next;
	    }

	    System.out.println();
	}
	public Node reverseLL(Node head) {
		if(head == null || head.next == null) {
			return head;
		}
		Node p = null;
		Node c = head;
		while( c != null) {
			Node n = c.next;
			c.next = p;
			p = c;
			c = n;
		}
		return p;
	}
	public Node reverseR(Node head) {
		if(head == null || head.next == null) {
			return head;
		}
		Node h = reverseR(head.next);
		Node s = head.next;
		s.next = head;
		head.next = null;
		return h;
	}
	
	


	private int get(int i) {
		if(i<0 || i>=size) {
			return -1;
		}
		Node t = head;
		for(int j = 0;j<i;j++) {
			t = t.next;
		}
		return t.val;
	}
	private Node  delete(int index) throws Exception{
		if(index <0 || index>=size) {
			throw new Exception("wrong index");
		}
		if(index == 0) {
			head = head.next;
			size--;
			return head;
		}
		else if(index == size-1) {
			Node t = head;
			for(int i = 0;i<index-1;i++) {
				t = t.next;
			}
			t.next = null;
			size--;
			return head;
		}else {
			Node t = head;
			for(int i = 0;i<index-1;i++) {
				t = t.next;
			}
			t.next = t.next.next;
		}
		size--;
		return head;
	}
	private boolean contains(int x) {
		Node t = head;
		while(t!=null) {
			if(x == t.val) {
				return true;
			}
			t = t.next;
		}
		return false;
	}
	
	private int findIndex(int val) {
		Node t = head;
		int i = 0;
		while(t!=null) {
			if(t.val == val) {
				return i;
			}
			i++;
			t = t.next;;
		}
		return -1;
	}
	private Node middleNode() {
		Node f = head;
		Node s = head;
		while(f!=null && f.next != null) {
			s = s.next;
			f = f.next.next;
		}
		return s;
	}
	private Node nthFromEnd(int n) {
		Node f = head;
		Node s = head;
		for(int i = 0;i<n;i++) {
			f = f.next;
		}
		while(f!=null) {
			f = f.next;
			s = s.next;
		}
		return s;
	}
	private Node removeNthFromEnd(int n) {

	    Node dummy = new Node(0);
	    dummy.next = head;

	    Node f = dummy;
	    Node s = dummy;

	    for(int i = 0; i <= n; i++) {
	        f = f.next;
	    }

	    while(f != null) {
	    	f = f.next;
	        s = s.next;
	    }

	    s.next = s.next.next;

	    head = dummy.next;
	    return head;
	}
	public Node removeNthFromEnd(Node head, int n) {
        if(head == null || head.next == null){
            return null;
        }
        Node p = head;
        Node q = head;
        for(int i = 0;i<n;i++){
            q = q.next;
        }
        if(q==null){
            return head.next;
        }
        while(q.next != null ){
            p = p.next;
            q = q.next;
        }
        p.next = p.next.next;
        return head;
    }
	private boolean hasCycle() {
		if(head == null || head.next == null) {
			return false;
		}
		Node f = head;
		Node s = head;
		while(f!=null && f.next != null && s != null) {
			f = f.next.next;
			s = s.next;
			if(f==s) {
				return true;
			}
		}
		return false;
	}
	private Node detectCycleStart() {
		if(head == null || head.next == null) {
			return head;
		}
		Node f = head;
		Node s = head;
		while(f!=null && f.next != null && s != null) {
			f = f.next.next;
			s = s.next;
			if(f==s) {
				 	Node p1 = head;
		            Node p2 = s;
		            
		            while(p1 != p2) {
		                p1 = p1.next;
		                p2 = p2.next;
		            }

		            return p1;
			}
		}
		return null;
	}
	private boolean isPalindrome() {
		if(head == null || head.next == null) {
			return true;
		}
		Node f = head;
		Node s = head;
		while(f!= null && f.next!=null) {
			f = f.next.next;
			s = s.next;
		}
		Node h1 = head;
		Node h2 = reverseLL(s);
		while(h2 != null) {
			if(h1.val != h2.val) {
				return false;
			}
			h1 = h1.next;
			h2 = h2.next;
		}
		return true;
	}
	Node deleteK(Node head, int k) {
		if(head == null || k<=0) {
			return head;
		}
		Node c = head;
		Node p = null;
		int i = 0;
		while(c != null) {
			i++;
			if(i%k == 0) {
				if(p == null) {
					head = head.next;
				}else {
					
					p.next = c.next;
				}
			}else{
				p = c;
			}
			c = c.next;
		}
		return head;
	}
}
