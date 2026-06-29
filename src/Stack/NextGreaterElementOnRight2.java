package Stack;
import java.util.Stack;
public class NextGreaterElementOnRight2 {

	public static void main(String[] args) {

	    NextGreaterElementOnRight2 obj = new NextGreaterElementOnRight2();

	    int[] nums = {1, 2, 1};

	    int[] ans = obj.nextGreaterElements(nums);

	    for(int x : ans) {
	        System.out.print(x + " ");
	    }
	}
	public int[] nextGreaterElements(int[] nums) {
		int[] arr = new int[nums.length];
		Stack<Integer> st = new Stack<>();
		int n = nums.length;
		for(int i = 2*n-1;i>=0;i--) {
			while(!st.isEmpty() && st.peek() <= nums[i%n]) {
				st.pop();
			}
			if(i<n) {
				if(st.isEmpty()) {
					arr[i] = -1;
				}else {
					arr[i] = st.peek();
				}
			}
			st.push(nums[i % n]);
		}
		return arr;
    }

}

