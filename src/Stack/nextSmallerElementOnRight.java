package Stack;
import java.util.Stack;
public class nextSmallerElementOnRight {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] a = {4, 5, 2, 10, 8};

        int[] ans = nextSmallerElement(a);

        System.out.println("next Smaller Elements:");

        for (int x : ans) {
            System.out.print(x + " ");
        }

	}
	public static int[] nextSmallerElement(int[] num) {
		int n = num.length;
		Stack<Integer> st = new Stack<>();
		int arr[] = new int[n];
		for(int i = n-1;i>=0;i--) {
			while(!st.isEmpty() && st.peek() >= num[i] ) {
				st.pop();
			}
			if(st.isEmpty()) {
				arr[i] = -1;
			}else {
				arr[i] = st.peek();
			}
			st.push(num[i]);
		}
		return arr;
	}

}
