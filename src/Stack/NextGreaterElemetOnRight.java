package Stack;
import java.util.Stack;
public class NextGreaterElemetOnRight {
	

	public static int[] nextGreaterElement(int[] a) {
		int n = a.length;
		int[] x = new int[n];
		Stack<Integer> s = new Stack<>();
		for(int i = n-1;i>=0;i--) {
			if(s.isEmpty()) {
				x[i] = -1;
			}else {
				while(!s.isEmpty()) {
					if(a[i]<s.peek()) {
						x[i] = s.peek();
						break;
					}else {
						s.pop();
					}
				}
				if(s.isEmpty()) {
					x[i] = -1;
				}
			}
			s.push(a[i]);
		}
		return x;
	}
	
	public static void main(String[] args) {
	    int[] a = {4,12,5,6,9,8,1,3};

	    int[] ans = nextGreaterElement(a);

	    for(int i : ans) {
	        System.out.print(i + " ");
	    }
	}
}
