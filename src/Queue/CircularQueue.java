package Queue;

public class CircularQueue {
	int[] arr;
	int arrSize;
	int r = -1;
	int f = 0;
	int cap;
	CircularQueue(int cap){
		arr = new int[cap];
		arrSize = 0;
		this.cap = cap;
	}

	public static void main(String[] args) {
		
		CircularQueue q = new CircularQueue(3);
		q.push(1);
		q.push(2);
		q.push(3);
		System.out.println(q.pop());
		q.push(4);
		
	}

	private int pop() {
		if(arrSize == 0) {
			return 0;
		}
		int k = arr[f];
		f = (f+1)%cap;
		arrSize--;
		return k;
	}

	private void push(int data) {
		if(arrSize == cap) {
			return;
		}
		r = (r+1)%cap;
		arr[r] = data;
		
		arrSize++;
	}
	public int Front() {
        if(isEmpty()){
            return -1;
        }
        return arr[f];
    }
    
    public int Rear() {
        if(isEmpty()){
            return -1;
        }
        return arr[r];
    }
    
    public boolean isEmpty() {
        if(arrSize == 0){
            return true;
        }
        return false;
    }

    
    public boolean isFull() {
        if(arrSize == cap){
            return true;
        }
        return false;
    }
	

}
