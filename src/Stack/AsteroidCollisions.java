package Stack;
import java.util.Stack;
public class AsteroidCollisions {

	private int[] asteroidCollision(int[] arr) {
		int n = arr.length;
		Stack<Integer> st = new Stack<>();
		for(int i = 0;i<n;i++) {
			boolean destroyed = false;
			
			while(!st.isEmpty() && arr[i] <0 && st.peek() > 0) {
				if(st.peek() < -arr[i]) {
					st.pop();
				}else if( st.peek() == -arr[i]) {
					st.pop();
					destroyed = true;
					break;
				}else{
					destroyed = true;
					break;
				}
			}
			
			if(!destroyed) {
				st.push(arr[i]);
			}
		}
		int k = st.size();
		int ans[] = new int[k];
		
		for(int i = k-1;i>=0;i--) {
			ans[i] = st.pop();
		}
		return ans;
	}
	public static void main(String[] args) {

		AsteroidCollisions obj = new AsteroidCollisions();

	    int[][] testCases = {
	        {5, 10, -5},
	        {8, -8},
	        {10, 2, -5},
	        {-2, -1, 1, 2},
	        {1, -2, -2, -2},
	        {-2, -2, 1, -2},
	        {1, 2, 3},
	        {-1, -2, -3},
	        {3, 5, -2},
	        {5, -5, 10},
	        {4, 3, -5},
	        {1, -1, -2, 2},
	        {7, -3, -4},
	        {2, -1, -2},
	        {1}
	    };

	    for (int[] arr : testCases) {

	        System.out.println("Input : " + java.util.Arrays.toString(arr));

	        int[] ans = obj.asteroidCollision(arr);

	        System.out.println("Output: " + java.util.Arrays.toString(ans));

	        System.out.println("--------------------------------");
	    }
	}

}
