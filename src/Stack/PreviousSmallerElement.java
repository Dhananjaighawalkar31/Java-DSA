package Stack;

public class PreviousSmallerElement {

    public static void main(String[] args) {

        int[] a = {4, 5, 2, 10, 8};

        int[] ans = PSE(a);

        System.out.println("Previous Smaller Elements:");

        for (int x : ans) {
            System.out.print(x + " ");
        }
    }

    static int[] PSE(int[] a) {
        int n = a.length;
        int[] arr = new int[n];
        Stack st = new Stack();

        for (int i = 0; i < n; i++) {

            while (!st.isEmpty() && st.peek() >= a[i]) {
                st.pop();
            }

            if (st.isEmpty()) {
                arr[i] = -1;
            } else {
                arr[i] = st.peek();
            }

            st.push(a[i]);
        }

        return arr;
    }
}