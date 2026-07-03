package Stack;

public class minStack {

	Stack min;
	Stack a;
	int size;
	public minStack() {
		a = new Stack();
		min = new Stack();
		size = 0;
    }

    public void push(int val) {
    	a.push(val);
    	if(min.isEmpty() || val <= min.peek()) {
    		min.push(val);
    	}
    	size++;
    }

    public int pop() throws Exception {
    	if(a.isEmpty()) {
    		throw new Exception("Stack is empty");
    	}
    	size--;
		return a.pop();
    }

    public int peek() throws Exception {
    	if(a.isEmpty()) {
    		throw new Exception("Stack is empty");
    	}
    	return a.peek();
    }

    public int getMin() {
    	if(min.isEmpty()) {
    		return -1;
    	}
    	int k = min.pop();
    	return k;
    }

    public boolean isEmpty() {
    	if(size == 0) {
    		return true;
    	}
    	return false;
    }

    public int size() {
    	return size;
    }
	public static void main(String[] args) {
		minStack a = new minStack();
		a.push(0);
		a.push(5);
		a.push(2);
		a.push(10);
		a.push(1);

		System.out.println(a.getMin());

	}
}
