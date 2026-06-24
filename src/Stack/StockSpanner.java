//package Stack;
//
//import java.util.Stack;
//
//public class StockSpanner {
//
//    Stack<int[]> st;
//
//    public StockSpanner() {
//        st = new Stack<>();
//    }
//
//    public int next(int price) {
//
//        int span = 1;
//
//        while(!st.isEmpty() && st.peek()[0] <= price) {
//            span += st.peek()[1];
//            st.pop();
//        }
//
//        st.push(new int[]{price, span});
//
//        return span;
//    }
//
//    public static void main(String[] args) {
//
//        StockSpanner sp = new StockSpanner();
//
//        System.out.println(sp.next(100)); // 1
//        System.out.println(sp.next(80));  // 1
//        System.out.println(sp.next(60));  // 1
//        System.out.println(sp.next(70));  // 2
//        System.out.println(sp.next(60));  // 1
//        System.out.println(sp.next(75));  // 4
//        System.out.println(sp.next(85));  // 6
//    }
//}
package Stack;

import java.util.Stack;

public class StockSpanner {

    public static int[] stockSpan(int[] arr) {

        int n = arr.length;
        int[] span = new int[n];

        Stack<Integer> st = new Stack<>();

        for(int i = 0; i < n; i++) {

            while(!st.isEmpty() && arr[st.peek()] <= arr[i]) {
                st.pop();
            }

            if(st.isEmpty()) {
                span[i] = i + 1;
            } else {
                span[i] = i - st.peek();
            }

            st.push(i);
        }

        return span;
    }

    public static void main(String[] args) {

        int[] arr = {100, 80, 60, 70, 60, 75, 85};

        int[] ans = stockSpan(arr);

        for(int x : ans) {
            System.out.print(x + " ");
        }
    }
}