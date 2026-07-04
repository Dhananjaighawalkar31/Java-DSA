package Stack;
import java.util.Stack;
public class previousGreaterElement {
	public static void main(String[] args) {

	    int[] nums = {4, 5, 2, 10, 8};

	    previousGreaterElement obj = new previousGreaterElement();

	    int[] ans = obj.previousGreaterElement(nums);

	    System.out.println("Previous Greater Elements:");

	    for (int x : ans) {
	        System.out.print(x + " ");
	    }
	}

	private int[] previousGreaterElement(int[] nums) {
		int n = nums.length;
		int ans[] = new int[n];
		Stack<Integer> st = new Stack<>();
		for(int i = 0;i<n;i++) {
			if(st.isEmpty()) {
				ans[i] = -1;
			}else {
				while(!st.isEmpty() && nums[i] >= st.peek()) {
					st.pop();
				}
				if(st.isEmpty()) {
					ans[i] = -1;
				}else {
					ans[i] = st.peek();
				}
			}
			st.push(nums[i]);
		}
		return ans;
	}
}
