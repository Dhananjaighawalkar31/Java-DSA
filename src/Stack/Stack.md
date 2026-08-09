# 📚 Stack — DSA Revision Handbook

> Reverse-engineered from your [`Java-DSA/src/Stack`](https://github.com/Dhananjaighawalkar31/Java-DSA/tree/main/src/Stack) package. Same format as the Trees handbook: what the problem actually asks → your original code untouched → bugs/edge-cases called out separately → intuition and why you picked this approach → line-by-line walkthrough → a full dry run showing the stack's contents at every step → test cases → and, only in a clearly separate section, a better/alternate approach if one exists.

---

## 📇 Progression Index

**Building the ADT itself**
1. Stack (Custom Linked-List Implementation)
2. Stack Using Array (Custom Array Implementation + `search()`)

**First applications**
3. Balanced Parenthesis

**The monotonic-stack family**
4. Previous Greater Element
5. Previous Smaller Element
6. Next Greater Element (on the Right)
7. Next Smaller Element (on the Right)
8. Next Greater Element II (Circular Array)
9. Stock Span Problem

**Design & simulation problems**
10. Min Stack (O(1) `getMin`)
11. Trapping Rain Water
12. Asteroid Collisions

---

## ⚠️ A Package-Wide Quirk Worth Knowing

Your package is named `Stack`, **and** it contains a custom class also named `Stack` (Problem 1). This creates a subtle but important resolution rule in Java: any file in this package that does **not** explicitly write `import java.util.Stack;` will silently resolve the bare word `Stack` to **your own custom linked-list implementation**, not the JDK's — because an explicit import of `java.util.Stack` shadows the same-package class, but *no* import means Java falls back to whatever `Stack` exists in the current package first.

Concretely, in your actual files:
- **Files that import `java.util.Stack`** (use the JDK's battle-tested version): `paranthesis.java`, `previousGreaterElement.java`, `NextGreaterElemetOnRight.java`, `nextSmallerElementOnRight.java`, `NextGreaterElementOnRight2.java`, `StockSpanner.java`, `AsteroidCollisions.java`.
- **Files that do NOT import it** (silently use *your own* custom `Stack.java` instead): `PreviousSmallerElement.java` and `minStack.java`.

This isn't a bug — your custom `Stack`'s API (`push`, `pop`, `peek`, `isEmpty`) is compatible enough that both problems work correctly either way — but it's worth being deliberate about, since it means `PreviousSmallerElement.java` and `minStack.java` are *also*, incidentally, real-world tests of your own `Stack.java` implementation. If you ever refactor your custom `Stack` and one of these two files suddenly breaks, this is why.

---

## 🔢 Shared Example Arrays

Used consistently across the monotonic-stack problems (4–8) for easy comparison:
```text
arr = [4, 5, 2, 10, 8]
```

---

# Problem 1 — Stack (Custom Linked-List Implementation)

## 1. What is the problem?
Build the Stack ADT (Abstract Data Type) from scratch — `push`, `pop`, `peek`, `isEmpty`, `size` — using your own singly linked list instead of `java.util.Stack`. This is the foundational piece the rest of the package quietly depends on (see the package-wide note above).

## 2. My Code
```java
package Stack;

public class Stack {
    class Node {
        Node next;
        int val;
        Node(int val) { this.val = val; }
    }
    Node top;
    int size;
    Stack() { top = null; size = 0; }

    @Override
    public String toString() {
        String res = "Stack -> ";
        Node t = top;
        while (t != null) { res += t.val + " "; t = t.next; }
        return res;
    }

    public boolean isEmpty() { return top == null; }
    public int size() { return this.size; }

    public int pop() {
        if (top == null) { throw new RuntimeException("Stack is Empty"); }
        int k = top.val;
        top = top.next;
        size--;
        return k;
    }

    public int peek() {
        if (top == null) { throw new RuntimeException("Stack is Empty"); }
        return top.val;
    }

    public void push(int val) {
        Node n = new Node(val);
        n.next = top;
        top = n;
        size++;
    }

    public static void main(String[] args) {
        Stack st = new Stack();
        st.push(4); st.push(2); st.push(3); st.push(1);
        System.out.println(st.peek());
        System.out.println(st.pop());
        st.push(7);
        System.out.println(st.size());
        System.out.println(st);
    }
}
```
*(Your file also keeps an earlier, fully commented-out draft below the class — a first attempt using a `public class Node` and private helper methods that mostly returned placeholder values like `int p = 0;`. Worth noting as a nice artifact of your own learning progression: it shows you iterated from stub methods to a real working implementation. Safe to delete once you're confident you don't need the reference anymore.)*

## 4. Issues / Bugs / Edge Cases
- `toString()` builds the result with repeated `String +=` concatenation in a loop — functionally correct, but for a stack with many elements this is O(n²) due to how Java strings are immutable (each `+=` creates a new String object). Not a real problem at typical stack sizes, but `StringBuilder` (as used in `StackUsingArray`'s `toString`, Problem 2) is the standard fix — worth noticing you actually used the better pattern in your *other* implementation but not this one.
- `pop()` and `peek()` both throw `RuntimeException("Stack is Empty")` on empty — consistent with each other, though see Problem 2 for a naming inconsistency across your two Stack implementations.
- Otherwise fully correct: O(1) `push`/`pop`/`peek`/`isEmpty`/`size`, no resizing concerns (unlike an array-backed stack).

## 5. Intuition & Why a Linked List
The **top of the stack = the head of the linked list**. This pairing is the whole trick: linked-list insertion/removal at the head is O(1) with no shifting required (unlike inserting at the head of an array, which would be O(n)). `push` prepends a new head; `pop` removes the current head and returns its value. There's no fixed capacity to worry about either — the list just grows and shrinks node by node, unlike Problem 2's array version which must pre-declare a size.

## 6. Line-by-Line Walkthrough (push/pop, the two core operations)
| Line | What happens |
|---|---|
| `Node n = new Node(val); n.next = top;` | The new node points to whatever was previously on top — this is what "prepending" means for a linked list |
| `top = n;` | The new node becomes the official top |
| `size++;` | Bookkeeping — kept in sync so `size()` doesn't need to walk the list |
| `int k = top.val; top = top.next;` (pop) | Read the top's value, then advance `top` to the next node — the old top node becomes unreachable and gets garbage collected |

## 7. Dry Run — matching your `main()` exactly
| Operation | Stack (top → bottom) | Returned/Printed |
|---|---|---|
| `push(4)` | `4` | — |
| `push(2)` | `2 → 4` | — |
| `push(3)` | `3 → 2 → 4` | — |
| `push(1)` | `1 → 3 → 2 → 4` | — |
| `peek()` | `1 → 3 → 2 → 4` (unchanged) | prints `1` |
| `pop()` | `3 → 2 → 4` | prints `1` (removed) |
| `push(7)` | `7 → 3 → 2 → 4` | — |
| `size()` | `7 → 3 → 2 → 4` | prints `4` |
| `toString()` | — | prints `"Stack -> 7 3 2 4 "` |

## 8. Test Cases
| Operation sequence | Expected result |
|---|---|
| `push(4),push(2),push(3),push(1)` then `peek()` | `1` |
| Same, then `pop()` | returns `1`, stack becomes `3,2,4` (top to bottom) |
| `new Stack().pop()` (empty stack) | throws `RuntimeException("Stack is Empty")` |
| `new Stack().peek()` (empty stack) | throws `RuntimeException("Stack is Empty")` |
| `new Stack().isEmpty()` | `true` |
| `new Stack().size()` | `0` |
| Push 100 elements, then check `size()` | `100` |

## Better / Alternative Approach
Functionally this is already the standard, correct linked-list-backed stack — O(1) for every operation, no capacity limit. The one genuine improvement: swap the `toString()` string concatenation for a `StringBuilder`, exactly like you already did in `StackUsingArray` — purely a performance/style consistency fix, not a correctness one.

---

# Problem 2 — Stack Using Array (Custom Array Implementation)

## 1. What is the problem?
Same ADT as Problem 1, implemented with a fixed-capacity array instead of a linked list — plus an extra `search(val)` method (mirrors `java.util.Stack`'s own `search()`: returns the 1-based distance from the top, or `-1` if not found).

## 2. My Code
```java
package Stack;

public class StackUsingArray {
    private int[] arr;
    private int top;

    public StackUsingArray(int capacity) { arr = new int[capacity]; top = -1; }

    public void push(int val) {
        if (top == arr.length - 1) { throw new RuntimeException("Stack Overflow"); }
        arr[++top] = val;
    }

    public int pop() {
        if (top == -1) { throw new RuntimeException("Stack Underflow"); }
        return arr[top--];
    }

    public int peek() {
        if (top == -1) { throw new RuntimeException("Stack is Empty"); }
        return arr[top];
    }

    public int size() { return top + 1; }
    public boolean isEmpty() { return top == -1; }

    public int search(int val) {
        int pos = 1;
        for (int i = top; i >= 0; i--) {
            if (arr[i] == val) { return pos; }
            pos++;
        }
        return -1;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Stack -> ");
        for (int i = top; i >= 0; i--) { sb.append(arr[i]).append(" "); }
        return sb.toString();
    }

    public static void main(String[] args) {
        StackUsingArray s = new StackUsingArray(5);
        s.push(10); s.push(20); s.push(30); s.push(40);
        System.out.println(s);
        System.out.println("Top Element : " + s.peek());
        System.out.println("Size : " + s.size());
        System.out.println("Search 30 : " + s.search(30));
        System.out.println("Popped : " + s.pop());
        System.out.println(s);
        System.out.println("Is Empty : " + s.isEmpty());
    }
}
```

## 4. Issues / Bugs / Edge Cases
- **Minor naming inconsistency, worth tidying:** `pop()` on empty throws `"Stack Underflow"`, but `peek()` on empty throws `"Stack is Empty"` — same failure condition (`top == -1`), two different messages. Not a bug, but if you're ever asserting on exception messages in a test, this inconsistency will bite you. Pick one convention.
- Fixed capacity means genuine risk of `"Stack Overflow"` if you don't size it generously enough upfront — this is an inherent trade-off of array-backed stacks versus Problem 1's linked-list version, not a mistake, but worth remembering when choosing which implementation to reach for.
- `search()` and `toString()` are both correct — `toString` correctly uses `StringBuilder` (better than Problem 1's string concatenation, as noted there).

## 5. Intuition & Why an Array (and Why `++top`)
The array-backed approach trades away Problem 1's "unlimited growth" for **better cache locality and less per-element memory overhead** (no `Node` objects, no `next` pointers) — a classic space/flexibility trade-off. `top` here is an **index**, not a pointer: `top = -1` means empty (a clean, idiomatic sentinel), and `arr[++top] = val` is a compact way to say "increment top, then write to that new position" in one line — the pre-increment (`++top`) form matters here since you want the *incremented* index used for the write, not the old one.

The `search()` method is a nice touch worth remembering — it demonstrates you can walk the array **from `top` downward** to search "from the top of the stack outward," which is the only stack-consistent way to define "distance from the top."

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `if (top == arr.length - 1) throw ...;` (push) | Capacity check — `top` sitting at the last valid index means the array is full |
| `arr[++top] = val;` | Advance `top` first, then write — this is what makes `top` always point at the *current* top element, never one past it |
| `return arr[top--];` (pop) | Read the current top's value, THEN decrement — post-decrement here is deliberate: you want the *pre-decrement* value returned |
| `search()` loop from `top` down to `0` | Counts distance from the top (`pos` starts at 1, matching the 1-indexed convention `java.util.Stack.search()` also uses) |

## 7. Dry Run — matching your `main()` exactly (capacity 5)
| Operation | Array state (index 0..top) | `top` | Output |
|---|---|---|---|
| `push(10)` | `[10]` | 0 | — |
| `push(20)` | `[10,20]` | 1 | — |
| `push(30)` | `[10,20,30]` | 2 | — |
| `push(40)` | `[10,20,30,40]` | 3 | — |
| `toString()` | (unchanged) | 3 | `"Stack -> 40 30 20 10 "` |
| `peek()` | (unchanged) | 3 | `"Top Element : 40"` |
| `size()` | (unchanged) | 3 | `"Size : 4"` |
| `search(30)` | walks index 3→0: `arr[3]=40≠30 (pos=2)`, `arr[2]=30==30 → return 2` | 3 | `"Search 30 : 2"` |
| `pop()` | returns `arr[3]=40`, `top` becomes 2 | 2 | `"Popped : 40"` |
| `toString()` | `[10,20,30]`, printed top-down | 2 | `"Stack -> 30 20 10 "` |
| `isEmpty()` | (unchanged) | 2 | `"Is Empty : false"` |

## 8. Test Cases
| Operation | Expected result |
|---|---|
| `push` 5 elements into capacity-5 stack, then `push` a 6th | throws `RuntimeException("Stack Overflow")` |
| `pop()` on a fresh empty stack | throws `RuntimeException("Stack Underflow")` |
| `peek()` on a fresh empty stack | throws `RuntimeException("Stack is Empty")` |
| `search(999)` where 999 was never pushed | `-1` |
| `search(x)` where `x` is the most recently pushed value | `1` |
| `isEmpty()` immediately after construction | `true` |

## Better / Alternative Approach
Both your implementations (this and Problem 1) are correct, standard textbook ADT implementations — the "better" choice between them is entirely situational: use the linked-list version (Problem 1) when you don't know the maximum size in advance; use this array version when you know an upper bound and want the memory/cache benefits. A middle-ground alternative worth knowing: a **dynamically-resizing array** (double the capacity when full, like `ArrayList`'s internal strategy) gets you the array's performance benefits without the hard overflow limit — that's effectively what `java.util.Stack` (backed by `Vector`) does under the hood.

---

# Problem 3 — Balanced Parenthesis

## 1. What is the problem?
Given a string of brackets (`(`, `)`, `{`, `}`, `[`, `]`), determine whether every opening bracket has a matching, correctly-nested closing bracket. Classic first "real" application of a stack.

## 2. My Code
```java
package Stack;
import java.util.Stack;
public class paranthesis {
    public static void main(String[] args) {
        System.out.println(balancedParenthesis("{}[]()"));
        System.out.println(balancedParenthesis("([)]"));
    }

    private static boolean balancedParenthesis(String str) {
        Stack<Character> s = new Stack<>();
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (ch == '(' || ch == '{' || ch == '[') {
                s.push(ch);
            } else {
                if (s.isEmpty()) { return false; }
                char k = s.pop();
                if (ch == ')' && k != '(') return false;
                if (ch == '}' && k != '{') return false;
                if (ch == ']' && k != '[') return false;
            }
        }
        return s.isEmpty();
    }
}
```

## 4. Issues / Bugs / Edge Cases
None — this correctly handles all the classic edge cases: unmatched closing bracket with nothing to pop (`s.isEmpty()` guard), wrong bracket type popped (the three explicit type-mismatch checks), and leftover unclosed opens at the very end (`return s.isEmpty()`, not just `return true`).

## 5. Intuition & Why a Stack
Brackets nest in **LIFO order** — the most recently opened bracket must be the next one closed. That's exactly what a stack gives you for free: push every opening bracket, and when a closing bracket arrives, it must match whatever's currently on **top** (the most recent unclosed open) — if it doesn't, the string can't be validly nested no matter what comes later.

**Why the final `return s.isEmpty()` matters, not just `return true`:** even if every closing bracket you *did* encounter matched correctly, there might still be unclosed opening brackets left sitting on the stack at the end (e.g. `"(()"`) — the string is only truly balanced if the stack is completely empty once you've read every character.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `if (opening bracket) s.push(ch);` | Remember it, we'll need to match it later |
| `if (s.isEmpty()) return false;` | A closing bracket arrived, but nothing is open to match it against — immediately invalid |
| `char k = s.pop();` | Take the most recently opened bracket — it MUST correspond to this closer for valid nesting |
| three `if` checks | Verify the popped opener actually matches this specific closer's type |
| `return s.isEmpty();` | Catches leftover unclosed opens — true balance requires everything to have been matched by the end |

## 7. Dry Run — `balancedParenthesis("([)]")` (the invalid example from your `main()`)
| i | ch | Action | Stack after |
|---|---|---|---|
| 0 | `(` | push | `[(]` |
| 1 | `[` | push | `[(, []` |
| 2 | `)` | pop → `k='['`. Check: `ch==')' && k!='('` → `'['!='('` is true → **return false** | — |

Result: **false** — correctly rejects the improperly interleaved brackets.

**Contrast — the valid example `"{}[]()"`:**
| i | ch | Action | Stack after |
|---|---|---|---|
| 0 | `{` | push | `[{]` |
| 1 | `}` | pop `k='{'`, matches → continue | `[]` |
| 2 | `[` | push | `[[]` |
| 3 | `]` | pop `k='['`, matches → continue | `[]` |
| 4 | `(` | push | `[(]` |
| 5 | `)` | pop `k='('`, matches → continue | `[]` |

Loop ends, `s.isEmpty()` → **true**.

## 8. Test Cases
| Input | Output |
|---|---|
| `"{}[]()"` | `true` |
| `"([)]"` | `false` |
| `""` (empty string) | `true` (vacuously balanced — stack never touched, ends empty) |
| `"((("` (unclosed opens) | `false` (stack has leftover `(`s at the end) |
| `")("` (closer before any opener) | `false` (first char triggers the `s.isEmpty()` guard) |
| `"{[()]}"` (properly nested, mixed types) | `true` |

## Better / Alternative Approach
Already the standard, optimal O(n) solution — this is the textbook-correct approach for this exact problem. No meaningful improvement exists.

---

# Problem 4 — Previous Greater Element

## 1. What is the problem?
For every element in an array, find the nearest element **to its left** that is strictly greater than it. If none exists, the answer for that position is `-1`. This is the first of your five "monotonic stack" problems — worth reading carefully since the same skeleton reappears (mirrored in different directions) in Problems 5–8.

## 2. My Code
```java
package Stack;
import java.util.Stack;
public class previousGreaterElement {
    public static void main(String[] args) {
        int[] nums = {4, 5, 2, 10, 8};
        previousGreaterElement obj = new previousGreaterElement();
        int[] ans = obj.previousGreaterElement(nums);
        System.out.println("Previous Greater Elements:");
        for (int x : ans) { System.out.print(x + " "); }
    }

    private int[] previousGreaterElement(int[] nums) {
        int n = nums.length;
        int ans[] = new int[n];
        Stack<Integer> st = new Stack<>();
        for (int i = 0; i < n; i++) {
            if (st.isEmpty()) {
                ans[i] = -1;
            } else {
                while (!st.isEmpty() && nums[i] >= st.peek()) { st.pop(); }
                if (st.isEmpty()) { ans[i] = -1; } else { ans[i] = st.peek(); }
            }
            st.push(nums[i]);
        }
        return ans;
    }
}
```

## 4. Issues / Bugs / Edge Cases
None — correct. (Small style note: the outer `if (st.isEmpty())` before the loop is actually redundant — if the stack is empty, the `while` loop's own `!st.isEmpty()` condition would immediately be false anyway, so the inner `if (st.isEmpty()) ans[i]=-1;` would set the same result. Not wrong, just an unnecessary extra branch — see the cleaner version in Problem 5/7 for comparison.)

## 5. Intuition & Why a "Monotonic" Stack
Process the array **left to right**, keeping the stack in **strictly decreasing order from bottom to top**. At each new element:
- **Pop everything on top that's `<= ` the current value** — those popped elements can never be the "previous greater" answer for anything that comes *after* the current element either, because the current (larger-or-equal) element is now a "closer, at-least-as-good" blocker standing in front of them. This is the key insight that makes the stack stay small and the whole algorithm O(n) total, not O(n²), even though there's a nested `while` loop — each element is pushed once and popped at most once across the *entire* run.
- **Whatever's left on top after popping (if anything) is the answer** — it survived the popping because it's genuinely greater than the current element, and it's the closest such element since everything smaller/equal between them just got removed.
- **Push the current value** regardless, so it's available as a potential answer for future elements.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `while (!st.isEmpty() && nums[i] >= st.peek()) st.pop();` | Discard everything that's now "blocked" and can never be anyone's answer going forward |
| `if (st.isEmpty()) ans[i] = -1; else ans[i] = st.peek();` | Whatever survived is the nearest larger element to the left |
| `st.push(nums[i]);` | Always push — even if this element became someone's answer, it might still be the answer for a *future* (smaller) element too |

## 7. Dry Run — `nums = [4, 5, 2, 10, 8]`
| i | nums[i] | Pops (while loop) | Stack after pop | ans[i] | Stack after push |
|---|---|---|---|---|---|
| 0 | 4 | (stack empty, no pops) | `[]` | -1 | `[4]` |
| 1 | 5 | pop 4 (4≤5) | `[]` | -1 | `[5]` |
| 2 | 2 | none (2<5) | `[5]` | 5 | `[5,2]` |
| 3 | 10 | pop 2, pop 5 | `[]` | -1 | `[10]` |
| 4 | 8 | none (8<10) | `[10]` | 10 | `[10,8]` |

Result: `ans = [-1, -1, 5, -1, 10]`

## 8. Test Cases
| Input | Output |
|---|---|
| `[4, 5, 2, 10, 8]` | `[-1, -1, 5, -1, 10]` |
| `[1, 2, 3, 4]` (strictly increasing) | `[-1, -1, -1, -1]` (nothing to the left is ever bigger) |
| `[4, 3, 2, 1]` (strictly decreasing) | `[-1, 4, 3, 2]` (each element's immediate left neighbor is always bigger) |
| `[5]` (single element) | `[-1]` |
| `[3, 3, 3]` (all equal) | `[-1, -1, -1]` (equal doesn't count as "greater" — the `>=` pop condition correctly discards equal values too) |

## Better / Alternative Approach
This IS the standard, optimal O(n) monotonic stack solution (each element pushed and popped at most once). The only cleanup worth making: drop the redundant outer `if (st.isEmpty())` check noted above — the `while` loop and inner `if` already handle that case correctly on their own, exactly as Problems 5 and 7 demonstrate.

---

# Problem 5 — Previous Smaller Element

## 1. What is the problem?
Mirror of Problem 4 — for every element, find the nearest element to its **left** that is strictly **smaller**.

## 2. My Code
```java
package Stack;

public class PreviousSmallerElement {
    public static void main(String[] args) {
        int[] a = {4, 5, 2, 10, 8};
        int[] ans = PSE(a);
        System.out.println("Previous Smaller Elements:");
        for (int x : ans) { System.out.print(x + " "); }
    }

    static int[] PSE(int[] a) {
        int n = a.length;
        int[] arr = new int[n];
        Stack st = new Stack();
        for (int i = 0; i < n; i++) {
            while (!st.isEmpty() && st.peek() >= a[i]) { st.pop(); }
            if (st.isEmpty()) { arr[i] = -1; } else { arr[i] = st.peek(); }
            st.push(a[i]);
        }
        return arr;
    }
}
```

## 4. Issues / Bugs / Edge Cases
None functionally. **Notable detail** (see the package-wide note at the top): `Stack st = new Stack();` here has no `import java.util.Stack;` anywhere in this file — so this is silently using **your own custom `Stack` class from Problem 1**, not the JDK's. It happens to work perfectly since your custom `push`/`pop`/`peek`/`isEmpty` API is identical in behavior to `java.util.Stack`'s — but it's worth knowing this file is quietly exercising your own hand-built data structure, not the standard library one your other monotonic-stack files use.

## 5. Intuition & Why This Approach
Cleaner version of Problem 4's structure (no redundant outer `if`) — maintain a stack in strictly **increasing** order from bottom to top this time (the mirror of Problem 4's decreasing order, since we now want smaller, not greater). Pop while the top is `>= ` the current element (those can never be anyone's "previous smaller" going forward, since the current — smaller-or-equal — element now blocks them). Whatever survives on top is the nearest smaller element to the left.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `while (!st.isEmpty() && st.peek() >= a[i]) st.pop();` | Discard everything not smaller than the current element — they're permanently blocked from being anyone's future answer |
| `if (st.isEmpty()) arr[i]=-1; else arr[i]=st.peek();` | Survivor (if any) is the nearest smaller element |
| `st.push(a[i]);` | Always push, regardless of whether this element became an answer for something else |

## 7. Dry Run — `a = [4, 5, 2, 10, 8]`
| i | a[i] | Pops | Stack after pop | arr[i] | Stack after push |
|---|---|---|---|---|---|
| 0 | 4 | none | `[]` | -1 | `[4]` |
| 1 | 5 | none (4<5) | `[4]` | 4 | `[4,5]` |
| 2 | 2 | pop 5, pop 4 | `[]` | -1 | `[2]` |
| 3 | 10 | none (2<10) | `[2]` | 2 | `[2,10]` |
| 4 | 8 | pop 10 (10≥8) | `[2]` | 2 | `[2,8]` |

Result: `arr = [-1, 4, -1, 2, 2]`

## 8. Test Cases
| Input | Output |
|---|---|
| `[4, 5, 2, 10, 8]` | `[-1, 4, -1, 2, 2]` |
| `[1, 2, 3, 4]` (increasing) | `[-1, 1, 2, 3]` (immediate left neighbor is always smaller) |
| `[4, 3, 2, 1]` (decreasing) | `[-1, -1, -1, -1]` (nothing to the left is ever smaller) |
| `[5]` | `[-1]` |
| `[3, 3, 3]` | `[-1, -1, -1]` (equal doesn't count — `>=` correctly discards ties) |

## Better / Alternative Approach
Already the optimal O(n) approach and, structurally, this is actually the *cleaner* of your two "previous element" implementations (no redundant branch, unlike Problem 4). The only thing worth deciding deliberately: whether you want this file using your custom `Stack` or `java.util.Stack` — currently it's using yours by accident of omission rather than by explicit choice.

---

# Problem 6 — Next Greater Element (on the Right)

## 1. What is the problem?
For every element, find the nearest element **to its right** that is strictly greater. `-1` if none exists.

## 2. My Code
```java
package Stack;
import java.util.Stack;
public class NextGreaterElemetOnRight {
    public static int[] nextGreaterElement(int[] a) {
        int n = a.length;
        int[] x = new int[n];
        Stack<Integer> s = new Stack<>();
        for (int i = n-1; i >= 0; i--) {
            if (s.isEmpty()) {
                x[i] = -1;
            } else {
                while (!s.isEmpty()) {
                    if (a[i] < s.peek()) { x[i] = s.peek(); break; }
                    else { s.pop(); }
                }
                if (s.isEmpty()) { x[i] = -1; }
            }
            s.push(a[i]);
        }
        return x;
    }

    public static void main(String[] args) {
        int[] a = {4,12,5,6,9,8,1,3};
        int[] ans = nextGreaterElement(a);
        for (int i : ans) { System.out.print(i + " "); }
    }
}
```

## 4. Issues / Bugs / Edge Cases
None functionally — correct output. **Worth noting a style difference** from Problem 7 (`nextSmallerElementOnRight`), which solves the mirror problem with the more idiomatic `while (!empty && peek() >= a[i]) pop();` pattern. Here, you instead wrote an inner `if/else` with an explicit `break` — functionally equivalent (both discard everything ≤ the current element and stop at the first genuinely greater survivor), but noticeably more verbose. Good to recognize both patterns are "the same algorithm," since interviewers sometimes expect the more compact form.

## 5. Intuition & Why Right-to-Left
To find something "to the right," you process the array **backward** — by the time you reach index `i`, the stack already contains (a filtered, monotonic version of) everything that was originally to `i`'s right. Same monotonic-decreasing-from-bottom idea as Problem 4, just scanning in the opposite direction because "next" (rightward) is the mirror of "previous" (leftward).

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `for (i = n-1; i >= 0; i--)` | Process right to left — this is the entire trick that turns "next" into "previous, but backward" |
| inner `while (!s.isEmpty())` with `if/break` else `pop` | Keep discarding anything ≤ current (blocked forever), stop the moment something genuinely greater survives on top |
| `x[i] = s.peek(); break;` | Found the nearest greater element to the right — record it and stop searching further down the stack |
| `s.push(a[i]);` | Always push, so this element is available for elements further to the left |

## 7. Dry Run — `a = [4, 12, 5, 6, 9, 8, 1, 3]` (processing i = 7 down to 0)
| i | a[i] | Stack before | Action | x[i] | Stack after |
|---|---|---|---|---|---|
| 7 | 3 | `[]` | empty → x=-1 | -1 | `[3]` |
| 6 | 1 | `[3]` | 1<3 → found | 3 | `[3,1]` |
| 5 | 8 | `[3,1]` | pop 1, pop 3, empty | -1 | `[8]` |
| 4 | 9 | `[8]` | pop 8, empty | -1 | `[9]` |
| 3 | 6 | `[9]` | 6<9 → found | 9 | `[9,6]` |
| 2 | 5 | `[9,6]` | 5<6 → found | 6 | `[9,6,5]` |
| 1 | 12 | `[9,6,5]` | pop 5, pop 6, pop 9, empty | -1 | `[12]` |
| 0 | 4 | `[12]` | 4<12 → found | 12 | `[12,4]` |

Result: `x = [12, -1, 6, 9, -1, -1, 3, -1]`

## 8. Test Cases
| Input | Output |
|---|---|
| `[4,12,5,6,9,8,1,3]` | `[12,-1,6,9,-1,-1,3,-1]` |
| `[1,2,3,4]` (increasing) | `[2,3,4,-1]` (immediate right neighbor is always bigger, except the last) |
| `[4,3,2,1]` (decreasing) | `[-1,-1,-1,-1]` (nothing to the right is ever bigger) |
| `[5]` | `[-1]` |
| `[3,3,3]` | `[-1,-1,-1]` (strict `<` in the `if` means equal values never count) |

## Better / Alternative Approach
Functionally already optimal O(n). Stylistically: see Problem 7's cleaner `while(!empty && peek()>=curr) pop();` pattern — worth rewriting this one to match, purely for readability and consistency across your own codebase.

---

# Problem 7 — Next Smaller Element (on the Right)

## 1. What is the problem?
Mirror of Problem 6 — nearest element to the right that's strictly smaller.

## 2. My Code
```java
package Stack;
import java.util.Stack;
public class nextSmallerElementOnRight {
    public static void main(String[] args) {
        int[] a = {4, 5, 2, 10, 8};
        int[] ans = nextSmallerElement(a);
        System.out.println("next Smaller Elements:");
        for (int x : ans) { System.out.print(x + " "); }
    }

    public static int[] nextSmallerElement(int[] num) {
        int n = num.length;
        Stack<Integer> st = new Stack<>();
        int arr[] = new int[n];
        for (int i = n-1; i >= 0; i--) {
            while (!st.isEmpty() && st.peek() >= num[i]) { st.pop(); }
            if (st.isEmpty()) { arr[i] = -1; } else { arr[i] = st.peek(); }
            st.push(num[i]);
        }
        return arr;
    }
}
```

## 4. Issues / Bugs / Edge Cases
None — clean, correct, and the more idiomatic pattern compared to Problem 6's version of the same idea in the opposite direction.

## 5. Intuition & Why This Approach
Same right-to-left scan as Problem 6, but this time discard everything `>= ` the current element (they can never be a "next smaller" answer once a smaller-or-equal blocker shows up). Whatever survives on top is the nearest smaller element to the right.

## 6. Line-by-Line Walkthrough
Same structure as Problem 5 (Previous Smaller), just scanning right-to-left instead of left-to-right — worth noticing these two problems are near-identical in code shape, differing only in loop direction, which is the whole "previous vs next" distinction in a nutshell.

## 7. Dry Run — `num = [4, 5, 2, 10, 8]` (processing i = 4 down to 0)
| i | num[i] | Pops | Stack after pop | arr[i] | Stack after push |
|---|---|---|---|---|---|
| 4 | 8 | none | `[]` | -1 | `[8]` |
| 3 | 10 | none (8<10) | `[8]` | 8 | `[8,10]` |
| 2 | 2 | pop 10, pop 8 | `[]` | -1 | `[2]` |
| 1 | 5 | none (2<5) | `[2]` | 2 | `[2,5]` |
| 0 | 4 | pop 5 (5≥4) | `[2]` | 2 | `[2,4]` |

Result: `arr = [2, 2, -1, 8, -1]`

## 8. Test Cases
| Input | Output |
|---|---|
| `[4, 5, 2, 10, 8]` | `[2, 2, -1, 8, -1]` |
| `[1,2,3,4]` (increasing) | `[-1,-1,-1,-1]` (nothing to the right is ever smaller) |
| `[4,3,2,1]` (decreasing) | `[3,2,1,-1]` (immediate right neighbor always smaller, except the last) |
| `[5]` | `[-1]` |
| `[3,3,3]` | `[-1,-1,-1]` |

## Better / Alternative Approach
Already the standard optimal O(n) solution — this is genuinely the cleanest-written monotonic stack method in your whole Stack package, worth using as your personal reference template for the pattern going forward.

---

# Problem 8 — Next Greater Element II (Circular Array)

## 1. What is the problem?
LeetCode 503. Same as Problem 6 (next greater to the right), but the array is now **circular** — after the last element, wrap back around to the beginning when searching. So the last element genuinely can find an answer among the *first* elements, unlike in a normal linear array.

## 2. My Code
```java
package Stack;
import java.util.Stack;
public class NextGreaterElementOnRight2 {
    public static void main(String[] args) {
        NextGreaterElementOnRight2 obj = new NextGreaterElementOnRight2();
        int[] nums = {1, 2, 1};
        int[] ans = obj.nextGreaterElements(nums);
        for (int x : ans) { System.out.print(x + " "); }
    }

    public int[] nextGreaterElements(int[] nums) {
        int[] arr = new int[nums.length];
        Stack<Integer> st = new Stack<>();
        int n = nums.length;
        for (int i = 2*n-1; i >= 0; i--) {
            while (!st.isEmpty() && st.peek() <= nums[i % n]) { st.pop(); }
            if (i < n) {
                if (st.isEmpty()) { arr[i] = -1; } else { arr[i] = st.peek(); }
            }
            st.push(nums[i % n]);
        }
        return arr;
    }
}
```

## 4. Issues / Bugs / Edge Cases
None — correctly handles the circular wraparound, and correctly avoids writing into `arr` during the "warm-up" pass (`i >= n`), since that first half-pass exists purely to prime the stack with wraparound context, not to produce real answers yet.

## 5. Intuition & Why the `2n-1` Trick
The circularity is simulated **without physically duplicating the array** — instead, the loop runs from `2n-1` down to `0`, and every array access uses `nums[i % n]` to wrap the index back into range. Conceptually, this walks the array **twice** (as if it were laid out as `nums + nums` back-to-back), but the modulo means no extra memory is used for an actual doubled array.

**Why the first pass (`i` from `2n-1` down to `n`) doesn't write any answers:** that "phantom" first walk exists purely to let the stack "see" the elements that would wrap around from the end back to the beginning, so that by the time the loop reaches the *real* indices (`i < n`), the stack already reflects the correct circular context. The `if (i < n)` guard is what prevents this warm-up pass from corrupting the real answer array.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `for (i = 2*n-1; i >= 0; i--)` | Walk the array conceptually twice, from the "end of the second lap" back to the true start |
| `nums[i % n]` | Wraps any index back into `[0, n-1]` — this is what makes indices beyond `n-1` behave as if the array repeated |
| `while (peek() <= nums[i%n]) pop();` | Same monotonic-stack logic as Problem 6, just using `<=` here instead of `<` (meaning ties get popped too — equal values don't count as "greater," matching the strict-greater-than requirement) |
| `if (i < n) { ... }` | **Only write real answers once we're in the true index range** — the warm-up lap's results are thrown away, they only existed to build correct stack state |
| `st.push(nums[i % n]);` | Push regardless — every value (real pass or warm-up pass) needs to be available for elements further back |

## 7. Dry Run — `nums = [1, 2, 1]` (n=3, loop from i=5 down to 0)
| i | idx=i%n | nums[idx] | Pops | `i<n`? | arr write | Stack after push |
|---|---|---|---|---|---|---|
| 5 | 2 | 1 | none (empty) | no (5≥3) | — | `[1]` |
| 4 | 1 | 2 | pop 1 (1≤2) | no (4≥3) | — | `[2]` |
| 3 | 0 | 1 | none (2>1, stop) | no (3≥3) | — | `[2,1]` |
| 2 | 2 | 1 | pop 1 (1≤1) | **yes** | `arr[2]=peek()=2` | `[2,1]` |
| 1 | 1 | 2 | pop 1, pop 2, empty | **yes** | `arr[1]=-1` | `[2]` |
| 0 | 0 | 1 | none (2>1, stop) | **yes** | `arr[0]=peek()=2` | `[2,1]` |

Result: `arr = [2, -1, 2]` — this is exactly the well-known LeetCode 503 sample answer for `[1,2,1]`.

## 8. Test Cases
| Input | Output |
|---|---|
| `[1, 2, 1]` | `[2, -1, 2]` |
| `[1, 2, 3, 4, 3]` (LeetCode's other classic example) | `[2, 3, 4, -1, 4]` |
| `[5]` (single element, wraps to itself but can't be "greater than itself") | `[-1]` |
| `[1, 1, 1]` (all equal) | `[-1, -1, -1]` (the `<=` pop condition means equal values never count as "greater") |
| `[3, 2, 1]` (strictly decreasing, so the wraparound is essential to find any answer) | `[-1, 3, 3]` (element `1` finds `3` only by wrapping past the end back to index 0) |

## Better / Alternative Approach
This IS the standard, optimal O(n) solution for LeetCode 503 (the `2n-1` modulo trick is the well-known accepted technique — avoids the O(n) *extra space* a literal array-doubling approach would cost). No further improvement needed.

---

# Problem 9 — Stock Span Problem

## 1. What is the problem?
LeetCode 901. For each day's stock price, the "span" is how many consecutive days *up to and including today* had a price `<= ` today's price, counting backward until you hit a day with a strictly higher price (or run out of days).

## 2. My Code
```java
package Stack;
import java.util.Stack;
public class StockSpanner {
    public static int[] stockSpan(int[] arr) {
        int n = arr.length;
        int[] span = new int[n];
        Stack<Integer> st = new Stack<>();
        for (int i = 0; i < n; i++) {
            while (!st.isEmpty() && arr[st.peek()] <= arr[i]) { st.pop(); }
            if (st.isEmpty()) { span[i] = i + 1; } else { span[i] = i - st.peek(); }
            st.push(i);
        }
        return span;
    }

    public static void main(String[] args) {
        int[] arr = {100, 80, 60, 70, 60, 75, 85};
        int[] ans = stockSpan(arr);
        for (int x : ans) { System.out.print(x + " "); }
    }
}
```
*(Your file also keeps a fully commented-out earlier version above this one — a genuine `StockSpanner` **class** with a constructor and a `next(int price)` method, designed for the true LeetCode 901 "online" format where prices arrive one at a time via repeated calls, rather than as a complete array known upfront. Both versions use the exact same monotonic-stack idea; the difference is purely architectural (streaming API vs. batch array function). Worth knowing both exist in your file — see "Better / Alternative Approach" below.)*

## 4. Issues / Bugs / Edge Cases
None in the active code — correct. Note that the **stack here holds indices, not values** (`st.push(i)`, not `st.push(arr[i])`) — this is a small but important shift from Problems 4–8, necessary because span calculations need the *distance* (`i - st.peek()`) between positions, which values alone can't give you.

## 5. Intuition & Why Indices Instead of Values
Same monotonic-decreasing-stack idea as the earlier problems, but this time you need to know **how far back** the blocking element was, not just what its value was — so the stack stores **indices**, and you look up `arr[st.peek()]` whenever you need the actual value for comparison. Popping condition: discard any index whose price is `<= ` today's price (that day can never "block" a future day's span either, for the same reasoning as Problems 4–8). Whatever index survives on top is the most recent day with a strictly higher price — the span is simply the number of days between that blocking day and today (`i - st.peek()`).

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `while (arr[st.peek()] <= arr[i]) st.pop();` | Discard any earlier day whose price wasn't higher than today's — it can never block anyone else's span calculation either |
| `if (st.isEmpty()) span[i] = i+1;` | No higher day survived anywhere to the left — the span extends all the way back to day 0, which is `i+1` days total (inclusive counting) |
| `else span[i] = i - st.peek();` | The survivor's index tells you exactly how many days back the "wall" is — span is the gap between them |
| `st.push(i);` | Store this day's *position*, not its price — needed for future span-distance calculations |

## 7. Dry Run — `arr = [100, 80, 60, 70, 60, 75, 85]`
| i | arr[i] | Pops (by index) | Stack after pop | span[i] | Stack after push |
|---|---|---|---|---|---|
| 0 | 100 | none (empty) | `[]` | 0+1=**1** | `[0]` |
| 1 | 80 | none (arr[0]=100>80) | `[0]` | 1-0=**1** | `[0,1]` |
| 2 | 60 | none (arr[1]=80>60) | `[0,1]` | 2-1=**1** | `[0,1,2]` |
| 3 | 70 | pop idx2 (arr[2]=60≤70) | `[0,1]` | 3-1=**2** | `[0,1,3]` |
| 4 | 60 | none (arr[3]=70>60) | `[0,1,3]` | 4-3=**1** | `[0,1,3,4]` |
| 5 | 75 | pop idx4(60≤75), pop idx3(70≤75) | `[0,1]` | 5-1=**4** | `[0,1,5]` |
| 6 | 85 | pop idx5(75≤85), pop idx1(80≤85) | `[0]` | 6-0=**6** | `[0,6]` |

Result: `span = [1, 1, 1, 2, 1, 4, 6]` — matches the exact comments in your own commented-out streaming version, confirming both implementations agree.

## 8. Test Cases
| Input | Output |
|---|---|
| `[100, 80, 60, 70, 60, 75, 85]` | `[1, 1, 1, 2, 1, 4, 6]` |
| `[10, 20, 30, 40]` (strictly increasing prices) | `[1, 2, 3, 4]` (every day extends the span, since each day beats every prior day) |
| `[40, 30, 20, 10]` (strictly decreasing) | `[1, 1, 1, 1]` (nothing ever beats the very first day, so every span resets to 1) |
| `[50]` (single day) | `[1]` |
| `[5, 5, 5]` (all equal prices) | `[1, 2, 3]` (the `<=` pop condition means equal prices DO extend the span, unlike the strict `>` needed to block it) |

## Better / Alternative Approach
Your **active** batch version is correct and O(n) total for a fully-known array. However, the actual LeetCode 901 problem statement specifically wants the **streaming/online** version — the exact commented-out `StockSpanner` class already sitting in your file, which processes one `next(price)` call at a time without knowing future prices in advance. Both use the identical core monotonic-stack trick (just storing `[price, span]` pairs directly in the streaming version instead of separate index/array lookups) — worth uncommenting that version specifically if you ever need to submit to LeetCode itself, since the platform tests the streaming API shape, not a batch array function.

---

# Problem 10 — Min Stack (O(1) `getMin`)

## 1. What is the problem?
LeetCode 155. Design a stack that supports the usual `push`/`pop`/`peek`, plus a `getMin()` that returns the current minimum element — **all in O(1) time**. The naive approach (scan the whole stack for the min every time) would be O(n) per query; this needs to be faster.

## 2. My Code
```java
package Stack;

public class minStack {
    Stack min;
    Stack a;
    int size;

    public minStack() { a = new Stack(); min = new Stack(); size = 0; }

    public void push(int val) {
        a.push(val);
        if (min.isEmpty() || val <= min.peek()) { min.push(val); }
        size++;
    }

    public int pop() throws Exception {
        if (a.isEmpty()) { throw new Exception("Stack is empty"); }
        size--;
        int x = a.pop();
        if (x == min.peek()) { min.pop(); }
        return x;
    }

    public int peek() throws Exception {
        if (a.isEmpty()) { throw new Exception("Stack is empty"); }
        return a.peek();
    }

    public int getMin() throws Exception {
        if (min.isEmpty()) { throw new Exception("Stack is Empty"); }
        return min.peek();
    }

    public boolean isEmpty() { if (size == 0) { return true; } return false; }
    public int size() { return size; }

    public static void main(String[] args) throws Exception {
        minStack a = new minStack();
        a.push(0); a.push(5); a.push(2); a.push(10); a.push(1);
        System.out.println(a.getMin());
    }
}
```

## 4. Issues / Bugs / Edge Cases
None — this correctly handles the classic "duplicate minimum" trap (explained below). One thing worth noticing (see the package-wide note): no `import java.util.Stack;` anywhere in this file, so `Stack min;` and `Stack a;` are both **your own custom `Stack` class from Problem 1**, not the JDK's — again, works correctly, but good to be aware of.

## 5. Intuition & Why Two Stacks
Maintain **two parallel stacks**: `a` holds every value normally, and `min` holds a *running history of minimums*, kept perfectly in sync with `a` so it can be popped at exactly the right moments.
- **On push:** always push to `a`. Only push to `min` if the new value is `<= ` the current min (or `min` is empty) — meaning it's a new minimum, or *ties* the current minimum.
- **On pop:** always pop from `a`. If the value just popped from `a` equals `min`'s current top, pop `min` too — this "expires" that minimum since the element that established it is gone.

**Why `<=` and not strict `<` in the push condition is the crucial correctness detail:** if you used strict `<`, pushing a duplicate of the current minimum wouldn't get recorded in `min` — and then when you later pop one of those duplicates, the `x == min.peek()` check in `pop()` would incorrectly remove the minimum from `min` even though another identical value is still sitting in `a`. Using `<=` ensures every occurrence of the current minimum gets its own entry in `min`, so popping one duplicate only removes one corresponding entry, correctly leaving the min accurate for the rest.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `a.push(val);` | Always record the value in the main stack |
| `if (min.isEmpty() \|\| val <= min.peek()) min.push(val);` | Track this value as a (possibly tied) new minimum |
| `int x = a.pop();` | Remove and capture the value leaving the main stack |
| `if (x == min.peek()) min.pop();` | If the value that just left WAS the current minimum, that minimum "expires" along with it |
| `getMin()` | Simply peek the `min` stack — O(1), no scanning required |

## 7. Dry Run — matching your `main()` exactly: `push(0), push(5), push(2), push(10), push(1)`
| Operation | `a` (top→bottom) | `min` (top→bottom) | Reasoning |
|---|---|---|---|
| `push(0)` | `0` | `0` | min empty → push |
| `push(5)` | `5,0` | `0` (unchanged) | `5<=0`? false → don't push to min |
| `push(2)` | `2,5,0` | `0` (unchanged) | `2<=0`? false → don't push |
| `push(10)` | `10,2,5,0` | `0` (unchanged) | `10<=0`? false → don't push |
| `push(1)` | `1,10,2,5,0` | `0` (unchanged) | `1<=0`? false → don't push |
| `getMin()` | — | `0` | returns `0` — correctly the true minimum of `{0,5,2,10,1}` |

## 8. Test Cases
| Operation sequence | Expected result |
|---|---|
| `push(0),push(5),push(2),push(10),push(1)` then `getMin()` | `0` |
| `push(3),push(3)` then `pop()` then `getMin()` | `3` (duplicate minimum correctly survives one pop, thanks to the `<=` logic) |
| `push(5),push(1),push(1),pop(),pop()` then `getMin()` | `5` (both `1`s correctly removed from `min`, leaving `5` as the true minimum) |
| `getMin()` on a freshly constructed, never-pushed-to stack | throws `Exception("Stack is Empty")` |
| `pop()` on empty stack | throws `Exception("Stack is empty")` (note: lowercase "empty" here vs `getMin()`'s uppercase "Empty" — a tiny casing inconsistency across your own two exception messages, harmless but worth tidying) |

## Better / Alternative Approach
This is already the standard, optimal O(1)-per-operation solution — the two-stack approach (with the `<=` duplicate-handling trick) is the textbook-correct accepted answer for LeetCode 155. A common space-saving alternative (not necessarily "better," just a different trade-off) is storing each `min` stack entry only when it *changes*, paired with a count of how many times that minimum currently appears in `a` — saves some memory on inputs with many duplicate values, at the cost of slightly more bookkeeping logic. Your version is simpler to read and reason about, which has real value too.

---

# Problem 11 — Trapping Rain Water

## 1. What is the problem?
LeetCode 42. Given a row of bar heights (like a bar chart / elevation map), compute how much water would be trapped between the bars after rain, assuming water can't escape off either end.

## 2. My Code
```java
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
        for (int i = 0; i < n; i++) { pmax = Math.max(pmax, arr[i]); pm[i] = pmax; }
        int smax = Integer.MIN_VALUE;
        for (int i = n-1; i >= 0; i--) { smax = Math.max(smax, arr[i]); sm[i] = smax; }
        for (int i = 0; i < n; i++) {
            if (arr[i] < pm[i] && arr[i] < sm[i]) { sum += Math.min(pm[i], sm[i]) - arr[i]; }
        }
        return sum;
    }
}
```

## 4. Issues / Bugs / Edge Cases
None — correct. **Worth flagging explicitly, since this lives in your `Stack` package:** this particular solution doesn't actually use a `Stack` at all — it's the classic **prefix-max / suffix-max array** technique. Trapping Rain Water is a genuinely famous problem with a well-known *stack-based* solution too (which would fit the folder's theme more literally) — see "Better / Alternative Approach" below for that version, since it's worth having both in your toolkit.

## 5. Intuition & Why Two Extra Arrays
Water sitting above any single bar is bounded by **the shorter of the two "walls"** on either side of it — specifically, the tallest bar anywhere to its left, and the tallest bar anywhere to its right. The water level at position `i` can never exceed `min(tallest-to-the-left, tallest-to-the-right)`, because water would simply spill over whichever wall is shorter.

So: precompute, for every position, the running maximum from the left (`pm[i]` = tallest bar from the start up to and including `i`) and the running maximum from the right (`sm[i]` = tallest bar from `i` to the end). Then for each position, the trapped water is `min(pm[i], sm[i]) - arr[i]` — but only if the current bar is actually shorter than *both* walls (otherwise there's no basin at all, the bar itself IS the wall).

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| first loop, `pm[i] = pmax` (running max so far, left to right) | For every position, "what's the tallest wall to my left (including myself)?" |
| second loop, `sm[i] = smax` (running max, right to left) | Same idea, mirrored — "what's the tallest wall to my right (including myself)?" |
| `if (arr[i] < pm[i] && arr[i] < sm[i])` | Only positions genuinely lower than BOTH walls can hold water — a position that IS one of the walls holds none |
| `sum += Math.min(pm[i], sm[i]) - arr[i];` | Water depth here = shorter wall's height minus this bar's own height |

## 7. Dry Run — `arr = [4, 2, 0, 6, 3, 2, 5]`
| i | arr[i] | pm[i] (max so far, L→R) | sm[i] (max so far, R→L) | Water here |
|---|---|---|---|---|
| 0 | 4 | 4 | 6 | `4<4`? no → 0 |
| 1 | 2 | 4 | 6 | `min(4,6)-2 = 2` |
| 2 | 0 | 4 | 6 | `min(4,6)-0 = 4` |
| 3 | 6 | 6 | 6 | `6<6`? no → 0 |
| 4 | 3 | 6 | 5 | `min(6,5)-3 = 2` |
| 5 | 2 | 6 | 5 | `min(6,5)-2 = 3` |
| 6 | 5 | 6 | 5 | `5<5`? no → 0 |

Total: `0+2+4+0+2+3+0 = 11`

## 8. Test Cases
| Input | Output |
|---|---|
| `[4, 2, 0, 6, 3, 2, 5]` | `11` |
| `[0,1,0,2,1,0,1,3,2,1,2,1]` (the classic LeetCode 42 example) | `6` |
| `[1,2,3,4,5]` (strictly increasing — no basin ever forms) | `0` |
| `[5,4,3,2,1]` (strictly decreasing — same reasoning) | `0` |
| `[3]` (single bar) | `0` |
| `[]` (empty array) | `0` (both loops simply do nothing, `sum` stays 0) |

## Better / Alternative Approach
Your version is a fully correct O(n) time / O(n) space solution. Two genuinely different alternatives worth knowing, since this is such a classic problem:
- **Stack-based approach** (the one that actually fits this package's theme): maintain a stack of indices with decreasing heights; whenever you find a bar taller than the stack's top, pop and compute water trapped *between* the popped bar and the new taller bar as a "layer." Same O(n) time, but demonstrates the monotonic-stack pattern from Problems 4–9 applied to a 2D-volume problem instead of a 1D nearest-element problem.
- **Two-pointer approach:** track a `left` and `right` pointer moving inward, along with running `leftMax`/`rightMax` — achieves the same O(n) time as your version but in **O(1) space**, since it never needs the full `pm`/`sm` arrays. This is generally considered the "optimal" accepted solution when space matters.

---

# Problem 12 — Asteroid Collisions

## 1. What is the problem?
LeetCode 735. Each asteroid moves at the same speed in one direction — positive value = moving right, negative = moving left. When two asteroids moving toward each other meet, the smaller (by absolute size) explodes; if equal, both explode. Asteroids moving the same direction never collide. Return the final surviving state.

## 2. My Code
```java
package Stack;
import java.util.Stack;
public class AsteroidCollisions {
    private int[] asteroidCollision(int[] arr) {
        int n = arr.length;
        Stack<Integer> st = new Stack<>();
        for (int i = 0; i < n; i++) {
            boolean destroyed = false;
            while (!st.isEmpty() && arr[i] < 0 && st.peek() > 0) {
                if (st.peek() < -arr[i]) {
                    st.pop();
                } else if (st.peek() == -arr[i]) {
                    st.pop();
                    destroyed = true;
                    break;
                } else {
                    destroyed = true;
                    break;
                }
            }
            if (!destroyed) { st.push(arr[i]); }
        }
        int k = st.size();
        int ans[] = new int[k];
        for (int i = k-1; i >= 0; i--) { ans[i] = st.pop(); }
        return ans;
    }

    public static void main(String[] args) {
        AsteroidCollisions obj = new AsteroidCollisions();
        int[][] testCases = {
            {5, 10, -5}, {8, -8}, {10, 2, -5}, {-2, -1, 1, 2},
            {1, -2, -2, -2}, {-2, -2, 1, -2}, {1, 2, 3}, {-1, -2, -3},
            {3, 5, -2}, {5, -5, 10}, {4, 3, -5}, {1, -1, -2, 2},
            {7, -3, -4}, {2, -1, -2}, {1}
        };
        for (int[] arr : testCases) {
            System.out.println("Input : " + java.util.Arrays.toString(arr));
            int[] ans = obj.asteroidCollision(arr);
            System.out.println("Output: " + java.util.Arrays.toString(ans));
            System.out.println("--------------------------------");
        }
    }
}
```

## 4. Issues / Bugs / Edge Cases
None — this correctly handles all three collision outcomes (smaller destroyed, equal both destroyed, larger survives and blocks) and correctly leaves same-direction asteroids alone (the `while` condition specifically requires `arr[i] < 0 && st.peek() > 0` — a collision can only ever happen between a left-mover meeting a right-mover already ahead of it). Your own 15-case test suite in `main()` is genuinely thorough — a great habit worth continuing on future problems.

## 5. Intuition & Why the Three-Way Branch
A collision can ONLY happen when the **current** asteroid is moving left (`arr[i] < 0`) AND something already on the stack is moving right (`st.peek() > 0`) — two right-movers never catch up to each other (same speed), two left-movers never do either, and a right-mover meeting a left-mover *already passed* isn't a collision (they're moving apart). This is why the `while` loop's guard condition is doing real work, not just a style choice.

Within an actual collision, there are exactly three physically possible outcomes, and your `if/else if/else` maps to them directly:
1. **Stack's top is smaller** (`st.peek() < -arr[i]`) → it explodes, **pop it and keep checking** — the current asteroid might have enough "size" left to also destroy the *next* thing down the stack, so the loop continues rather than stopping here.
2. **Exactly equal** (`st.peek() == -arr[i]`) → **both** explode — pop the stack's asteroid, mark the current one destroyed too, and stop (nothing further to check).
3. **Stack's top is bigger** → the current asteroid explodes, the stack's asteroid survives unchanged — mark destroyed and stop.

**Why `destroyed` needs to be tracked with a boolean rather than just breaking out and moving on:** the current asteroid might survive multiple rounds of popping smaller things (case 1) before either finding something too big (case 3) or running out of stack entirely (loop ends because `st.isEmpty()`) — either way, if it's never marked `destroyed`, it correctly gets pushed as a genuine survivor.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `while (!st.isEmpty() && arr[i]<0 && st.peek()>0)` | Only enter collision-resolution when a genuine head-on collision is even possible |
| `if (st.peek() < -arr[i]) st.pop();` | Smaller survivor destroyed, but current asteroid keeps moving — check the next one down |
| `else if (st.peek() == -arr[i]) { st.pop(); destroyed=true; break; }` | Mutual destruction — both gone, stop checking further |
| `else { destroyed=true; break; }` | Current asteroid is the smaller one — it's destroyed, the stack's asteroid is untouched |
| `if (!destroyed) st.push(arr[i]);` | Only genuine survivors get added to the "still flying" stack |
| final reversal loop | The stack naturally holds survivors in reverse order (LIFO) — flip it back into left-to-right array order for the answer |

## 7. Dry Run — `{1, -2, -2, -2}` (a case with a chain reaction worth tracing in detail)
| i | arr[i] | Collision checks | destroyed? | Stack after |
|---|---|---|---|---|
| 0 | 1 | `arr[i]<0`? false → while never runs | false | `[1]` |
| 1 | -2 | peek=1>0, -2<0 → enter: `1 < 2`? yes → pop 1. Stack now empty → while exits (isEmpty) | false (never hit an else-branch) | `[-2]` |
| 2 | -2 | peek=-2, NOT `>0` → while condition false, never enters | false | `[-2,-2]` |
| 3 | -2 | peek=-2, NOT `>0` → while condition false | false | `[-2,-2,-2]` |

Final stack (bottom→top): `[-2,-2,-2]`. Reversal loop produces `ans = [-2, -2, -2]`.

**Physical sanity check:** `1` (right-moving, small) collides with the first `-2` (left-moving, bigger) → `1` is destroyed, that `-2` continues. The remaining two `-2`s were already moving left with nothing right-moving left to collide with → all three `-2`s ultimately survive. ✅ matches.

## 8. Test Cases
*(Straight from your own excellent 15-case test suite — reproducing the key ones with expected outputs for quick reference)*

| Input | Output | What it tests |
|---|---|---|
| `{5, 10, -5}` | `[5, 10]` | left-mover destroyed by a bigger, earlier right-mover further down the stack |
| `{8, -8}` | `[]` | exact-size mutual destruction |
| `{10, 2, -5}` | `[10]` | chain reaction: `-5` destroys `2` first, then meets `10` and is itself destroyed |
| `{-2, -1, 1, 2}` | `[-2, -1, 1, 2]` | no collisions possible at all — left-movers first, then right-movers, never meeting |
| `{1, -2, -2, -2}` | `[-2, -2, -2]` | traced in detail above |
| `{7, -3, -4}` | `[7]` | current asteroid (`-3`) destroyed, but `-4` then also destroyed by the same surviving `7` |
| `{1}` | `[1]` | trivial single-element, no collision possible |

## Better / Alternative Approach
This is already the standard, optimal O(n) stack-based solution for LeetCode 735 — each asteroid is pushed and popped at most once overall, same amortized-O(n) reasoning as the monotonic stack problems earlier in this file. No meaningful algorithmic improvement exists; this is the expected approach.

---

*End of handbook. 12 files, every one with your original code preserved, intuition, a full dry run, and test cases. The package-wide note at the top about the `Stack`/`Stack` naming collision is worth re-reading occasionally — it's the one thing in this whole package that's easy to forget and mildly confusing to rediscover cold.*