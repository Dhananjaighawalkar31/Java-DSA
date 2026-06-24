package Stack;
import java.util.Stack;
public class paranthesis {

	public static void main(String[] args) {
        System.out.println(balancedParenthesis("{}[]()"));
        System.out.println(balancedParenthesis("([)]"));
    }

    private static boolean balancedParenthesis(String str) {

        Stack<Character> s = new Stack<>();

        for(int i = 0; i < str.length(); i++) {

            char ch = str.charAt(i);
            if(ch == '(' || ch == '{' || ch == '[') {
                s.push(ch);
            }
            else {

                if(s.isEmpty()) {
                    return false;
                }

                char k = s.pop();

                if(ch == ')' && k != '(') return false;
                if(ch == '}' && k != '{') return false;
                if(ch == ']' && k != '[') return false;
            }
        }return s.isEmpty();
    }
}
