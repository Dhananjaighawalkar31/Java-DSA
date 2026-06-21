package Stack;

public class StackUsingArray {

    private int[] arr;
    private int top;

    public StackUsingArray(int capacity) {
        arr = new int[capacity];
        top = -1;
    }

    public void push(int val) {
        if (top == arr.length - 1) {
            throw new RuntimeException("Stack Overflow");
        }

        arr[++top] = val;
    }

    public int pop() {
        if (top == -1) {
            throw new RuntimeException("Stack Underflow");
        }

        return arr[top--];
    }

    public int peek() {
        if (top == -1) {
            throw new RuntimeException("Stack is Empty");
        }

        return arr[top];
    }

    public int size() {
        return top + 1;
    }

    public boolean isEmpty() {
        return top == -1;
    }

    public int search(int val) {
        int pos = 1;

        for (int i = top; i >= 0; i--) {
            if (arr[i] == val) {
                return pos;
            }
            pos++;
        }

        return -1;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Stack -> ");

        for (int i = top; i >= 0; i--) {
            sb.append(arr[i]).append(" ");
        }

        return sb.toString();
    }

    public static void main(String[] args) {

        StackUsingArray s = new StackUsingArray(5);

        s.push(10);
        s.push(20);
        s.push(30);
        s.push(40);

        System.out.println(s);

        System.out.println("Top Element : " + s.peek());

        System.out.println("Size : " + s.size());

        System.out.println("Search 30 : " + s.search(30));

        System.out.println("Popped : " + s.pop());

        System.out.println(s);

        System.out.println("Is Empty : " + s.isEmpty());
    }
}