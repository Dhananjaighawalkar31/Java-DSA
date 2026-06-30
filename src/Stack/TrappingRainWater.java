package Stack;

public class TrappingRainWater {

	public static void main(String[] args) {

	    int[] arr = {4, 2, 0, 6, 3, 2, 5};

	    int ans = TrappingRainWater(arr);

	    System.out.println("Total Trapped Water = " + ans);
	}
	static int TrappingRainWater(int[] arr) {
		int sum = 0;
		int n = arr.length;
		int[] pm = new int[n];
		int[] sm = new int[n];
		int pmax = Integer.MIN_VALUE;
		for(int i = 0;i<n;i++) {
			pmax = Math.max(pmax, arr[i]);
			pm[i] = pmax;
		}
		int smax = Integer.MIN_VALUE;
		for(int i = n-1;i>=0;i--) {
			smax = Math.max(smax, arr[i]);
			sm[i] = smax;
		}
		for(int i = 0;i<n;i++) {
			if(arr[i]<pm[i] && arr[i]<sm[i]) {
				sum += Math.min(pm[i],sm[i])-arr[i];
			}
		}
		return sum;
	}
}
