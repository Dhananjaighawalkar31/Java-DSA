# 🔗 LinkedList — DSA Revision Handbook

> Reverse-engineered from your `LinkedList.java`. Same format as the Trees and Stack handbooks: what the problem actually asks → your original code untouched → bugs/edge-cases called out separately → intuition and why you picked this approach → line-by-line walkthrough → a full dry run showing the list's pointers at every step → test cases → and, only in a clearly separate section, a better/alternate approach if one exists.

---

## 📇 Progression Index

**Building the ADT itself**
1. Add at Head
2. Add at Index
3. Add at Tail
4. Print (Traversal)
5. Get (Value at Index)
6. Delete (at Index)
7. Contains (Search by Value)
8. Find Index (Search Returning Position)

**Reversal**
9. Reverse Linked List (Iterative)
10. Reverse Linked List (Recursive)

**Fast/Slow Pointer Techniques**
11. Middle of Linked List
12. Nth Node From End
13. Remove Nth Node From End (Version A — Dummy Node)
14. Remove Nth Node From End (Version B — Early-Return Style)

**Cycle Detection**
15. Has Cycle (Floyd's Tortoise and Hare)
16. Detect Cycle Start Node

**Structural Problems**
17. Is Palindrome
18. Delete Every Kth Node

---

## 🔢 Shared Example List

Built by tracing your own `main()` exactly — this is the list every problem below refers back to unless a different example is specifically more illustrative:

```java
li.addAtHead(10);       // list: 10
li.addAtIndex(20, 1);   // 1 == size, so this calls addAtTail → 10 -> 20
li.addAtTail(30);       // 10 -> 20 -> 30
li.addAtIndex(100, 2);  // insert between index 1 and 2 → 10 -> 20 -> 100 -> 30
```
```text
10 -> 20 -> 100 -> 30 -> null
```
`li.print(li.head)` prints `"10 20 100 30 "`, and `li.findIndex(100)` correctly returns `2` — both match your own `main()`'s expected output exactly.

---

# Problem 1 — Add at Head

## 1. What is the problem?
Insert a new node at the very front of the list, so it becomes the new first element.

## 2. Example
Starting from an empty list, `addAtHead(10)` — the first line of your own `main()`.

## 3. My Code
```java
public void addAtHead(int i){
    Node n = new Node(i);
    n.next = head;
    head = n;
    size++;
}
```

## 4. Issues / Bugs / Edge Cases
None. Correctly handles the empty-list case too: when `head` is `null`, `n.next = head` sets `n.next = null` (correct — the new node is now the only, and last, node), and `head = n` makes it the head.

## 5. Intuition & Why This Approach
Inserting at the head is the one linked-list insertion that's **always O(1)**, regardless of list length — because you never need to walk anywhere first. The two-line pattern (`n.next = head; head = n;`) is the fundamental "prepend" operation every linked list problem eventually builds on: point the new node at whatever used to be first, then officially make it first.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `Node n = new Node(i);` | Create the new node — its `next` starts as `null` by default |
| `n.next = head;` | Wire it to point at the current front of the list (works correctly even if `head` is `null`) |
| `head = n;` | The new node is now officially the front |
| `size++;` | Keep the bookkeeping field in sync |

## 7. Dry Run — building the shared example list, first step
| Call | `head` before | `head` after | List |
|---|---|---|---|
| `addAtHead(10)` | `null` | `10` | `10 -> null` |

## 8. Test Cases
| Operation | Expected result |
|---|---|
| `addAtHead(10)` on empty list | list becomes `10 -> null`, `size = 1` |
| `addAtHead(20)` on `10 -> null` | list becomes `20 -> 10 -> null`, `size = 2` |
| Call `addAtHead` 3 times with `1, 2, 3` in order | list becomes `3 -> 2 -> 1 -> null` (most recent head-insert ends up first) |

## Better / Alternative Approach
Already the optimal O(1) solution — this is the only correct way to implement head insertion for a singly linked list.

---

# Problem 2 — Add at Index

## 1. What is the problem?
LeetCode 707-style. Insert a value at an arbitrary index `i`, shifting everything at and after that position back by one.

## 2. Example
`addAtIndex(20, 1)` on the list `10 -> null` (size 1) — from your `main()`.

## 3. My Code
```java
public void addAtIndex(int val, int i) throws Exception {
    if(i<0 || i>size) {
        throw new Exception("Wrong index");
    }
    if(i==0) {
        addAtHead(val);
    }
    else if(i==size) {
        addAtTail(val);
    }else {
        Node t = head;
        for(int j = 0;j<i-1;j++) {
            t = t.next;
        }
        Node n = new Node(val);
        n.next = t.next;
        t.next = n;
        size++;
    }
}
```

## 4. Issues / Bugs / Edge Cases
None in this method itself — the bounds check (`i<0 || i>size`) correctly allows `i == size` as a valid "insert at the very end" case (not out of bounds), and correctly delegates to `addAtHead`/`addAtTail` for the two boundary cases rather than duplicating their logic. **However:** the `i==size` branch delegates to `addAtTail`, which itself has a real bug on empty lists (see Problem 3) — so `addAtIndex(val, 0)` on an empty list is fine (goes through `addAtHead`), but that's the only reason this method doesn't inherit `addAtTail`'s crash, since `i==size==0` is caught by the `i==0` check first, not the `i==size` check.

## 5. Intuition & Why Three Separate Cases
Inserting in the *middle* of a linked list requires access to the node **right before** the insertion point (so you can rewire its `.next`) — there's no way to "step backward" in a singly linked list, so you must walk forward from the head. The three-way split exists because the two boundary cases (`i==0`, `i==size`) don't actually need that "node before" logic at all — they're better handled by simply reusing your own dedicated head/tail methods rather than making the general-case loop handle awkward boundary conditions (like `i-1 == -1`, which would be nonsensical to loop toward).

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `if(i<0 \|\| i>size) throw ...;` | `i` must be a valid insertion point — anywhere from "before everything" (0) to "right after everything" (size) |
| `if(i==0) addAtHead(val);` | Special-cased — inserting at the very front needs no "node before" lookup |
| `else if(i==size) addAtTail(val);` | Special-cased — inserting at the very end, same reasoning |
| `for(j=0;j<i-1;j++) t=t.next;` | Walk to the node **just before** the target index — this is why the loop stops at `i-1`, not `i` |
| `n.next = t.next; t.next = n;` | Classic middle-insertion: new node points to what used to come next, then the predecessor points to the new node |

## 7. Dry Run — `addAtIndex(100, 2)` on `10 -> 20 -> 30` (size 3), the fourth call in your `main()`
```text
i=2, size=3. i≠0, i≠size(3) → general case.
t = head = 10.
for j=0; j<1 (i-1=1); j++: j=0 → t = t.next = 20. loop stops (j becomes 1, 1<1 false).
t = 20 (the node right before index 2).
n = Node(100). n.next = t.next = 30.
t.next = n → 20.next = 100.
size++ → size=4.
```
Result: `10 -> 20 -> 100 -> 30 -> null` — matches the shared example list exactly.

## 8. Test Cases
| Call | On list | Result |
|---|---|---|
| `addAtIndex(20, 1)` | `10` (size 1) | `i==size` → delegates to `addAtTail` → `10 -> 20` |
| `addAtIndex(100, 2)` | `10 -> 20 -> 30` (size 3) | `10 -> 20 -> 100 -> 30` (traced above) |
| `addAtIndex(5, 0)` | `10 -> 20` | `i==0` → delegates to `addAtHead` → `5 -> 10 -> 20` |
| `addAtIndex(5, 10)` on a 3-element list | throws `Exception("Wrong index")` |
| `addAtIndex(5, -1)` | throws `Exception("Wrong index")` |

## Better / Alternative Approach
Already the standard, correct O(i) approach (must walk to the insertion point — no way around that for a singly linked list). No meaningful algorithmic improvement, though see Problem 3's note about maintaining a `tail` pointer, which would also make the `i==size` delegated case faster.

---

# Problem 3 — Add at Tail

## 1. What is the problem?
Append a new value at the very end of the list.

## 2. Example
`addAtTail(30)` on `10 -> 20` (size 2), from your `main()`.

## 3. My Code
```java
public void addAtTail(int val) {
    if(head == null) {
        System.out.println("empty tail");
    }
    Node t = head;
    while(t.next != null) {
        t = t.next;
    }
    t.next = new Node(val);
    size++;
}
```

## 4. Issues / Bugs / Edge Cases
**Real bug, not just an edge case gap:** when `head == null` (empty list), this method prints `"empty tail"` but then **keeps executing anyway** — `Node t = head;` sets `t = null`, and the very next line, `while(t.next != null)`, immediately throws a `NullPointerException` since you can't call `.next` on `null`. The print statement looks like it's "handling" the empty-list case, but it doesn't actually `return` afterward, so the crash happens regardless. Try `new LinkedList().addAtTail(5)` directly and you'll see this immediately.

**Also worth noting (a performance characteristic, not a bug):** every call to `addAtTail` walks the *entire* list to find the last node — O(n) per call. If you frequently append, this adds up. See "Better / Alternative Approach" below.

## 5. Intuition & Why This Approach
Unlike head insertion, there's no shortcut to "the end" of a singly linked list without a dedicated `tail` pointer — so the method walks forward (`while (t.next != null) t = t.next;`) until it finds the node whose `.next` is `null` (the current last node), then attaches the new node there.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `if (head == null) System.out.println("empty tail");` | **Intended** to flag the empty case, but doesn't actually prevent the crash below |
| `Node t = head; while(t.next != null) t = t.next;` | Walk to the current last node — the loop condition specifically checks the *next* node's existence, so `t` ends up being the last real node, not `null` |
| `t.next = new Node(val);` | Attach the new node after the current last one |
| `size++;` | Bookkeeping |

## 7. Dry Run — `addAtTail(30)` on `10 -> 20` (from your `main()`)
```text
head=10, not null → skip the print.
t = head = 10.
while(t.next != null): t.next=20, not null → t = t.next = 20.
while check again: t=20, t.next=null → loop stops.
t.next = new Node(30) → 20.next = 30.
size++.
```
Result: `10 -> 20 -> 30 -> null` — matches the shared example's third build step.

## 8. Test Cases
| Call | On list | Result |
|---|---|---|
| `addAtTail(30)` | `10 -> 20` | `10 -> 20 -> 30` |
| `addAtTail(5)` | empty list (`head == null`) | **currently crashes with `NullPointerException`** — see bug above |
| `addAtTail(99)` | single-node list `10` | `10 -> 99` |

## Better / Alternative Approach
Two independent fixes worth making, one for correctness and one for performance:

**Fix the crash (correctness):**
```java
public void addAtTail(int val) {
    if (head == null) {
        head = new Node(val);
        size++;
        return;
    }
    Node t = head;
    while (t.next != null) { t = t.next; }
    t.next = new Node(val);
    size++;
}
```

**Fix the O(n)-per-call cost (performance):** maintain a `tail` pointer alongside `head` as an instance field, updated on every insertion/deletion that touches the end of the list. Then `addAtTail` becomes O(1) — just `tail.next = new Node(val); tail = tail.next;` — no walking required at all. This is the standard trade-off every production linked-list implementation (including `java.util.LinkedList`, which is actually doubly-linked with both head and tail tracked) makes.

---

# Problem 4 — Print (Traversal)

## 1. What is the problem?
Display every value in the list, in order, for debugging/visualization purposes.

## 2. Example
`print(li.head)` on the shared example list.

## 3. My Code
```java
public void print(Node temp) {
    while(temp != null) {
        System.out.print(temp.val + " ");
        temp = temp.next;
    }
    System.out.println();
}
```

## 4. Issues / Bugs / Edge Cases
None. Correctly handles an empty list (`temp == null` from the start → loop never runs, just prints a blank line via the final `System.out.println()`).

## 5. Intuition & Why This Approach
The most fundamental linked-list operation there is: **follow `.next` until you hit `null`**. Every other traversal-based method in this file (get, contains, findIndex, and the search phase of most algorithms below) is a variation of this exact loop shape, just doing something different than printing at each step.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `while (temp != null)` | Keep going until we fall off the end of the list |
| `System.out.print(temp.val + " ");` | Visit — in this case, display |
| `temp = temp.next;` | Advance |
| final `System.out.println();` | Just a newline for clean output formatting, runs once after the loop regardless of list length |

## 7. Dry Run — `print(li.head)` on `10 -> 20 -> 100 -> 30`
```text
temp=10: print "10 ". temp=20.
temp=20: print "20 ". temp=100.
temp=100: print "100 ". temp=30.
temp=30: print "30 ". temp=null.
temp=null: loop stops.
println() → newline.
```
Output: `"10 20 100 30 \n"` — matches your own `main()`'s actual output.

## 8. Test Cases
| Input list | Output |
|---|---|
| `10 -> 20 -> 100 -> 30` | `"10 20 100 30 "` |
| Empty list (`temp == null`) | `""` (just a blank line) |
| Single node `[7]` | `"7 "` |

## Better / Alternative Approach
Functionally already correct and optimal (O(n), has to touch every node once). A minor testability improvement: return a `String` (or `List<Integer>`) instead of printing directly — makes the method usable in automated tests via `assertEquals`, rather than only visually inspectable via console output.

---

# Problem 5 — Get (Value at Index)

## 1. What is the problem?
Return the value stored at a given index, or `-1` if the index is invalid.

## 2. Example
`get(2)` on the shared example list `10 -> 20 -> 100 -> 30` should return `100`.

## 3. My Code
```java
private int get(int i) {
    if(i<0 || i>=size) {
        return -1;
    }
    Node t = head;
    for(int j = 0;j<i;j++) {
        t = t.next;
    }
    return t.val;
}
```

## 4. Issues / Bugs / Edge Cases
None — bounds check is correct (`i >= size`, not `i > size`, since valid indices only go up to `size-1`).

## 5. Intuition & Why This Approach
Unlike an array, a linked list has no direct "jump to index i" operation — you must walk from the head, one `.next` at a time, exactly `i` steps. This is the fundamental trade-off of linked lists versus arrays: O(1) insertion at the head, but O(i) random access, the opposite of an array's characteristics.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `if (i<0 \|\| i>=size) return -1;` | Reject invalid indices upfront, before touching the list at all |
| `for(j=0;j<i;j++) t=t.next;` | Walk exactly `i` steps forward from the head |
| `return t.val;` | After the loop, `t` is sitting exactly at index `i` |

## 7. Dry Run — `get(2)` on `10 -> 20 -> 100 -> 30`
```text
i=2, size=4 → valid.
t = head = 10.
j=0: t = t.next = 20.
j=1: t = t.next = 100.
loop stops (j becomes 2, 2<2 false).
return t.val = 100.
```

## 8. Test Cases
| Call | On list | Result |
|---|---|---|
| `get(2)` | `10 -> 20 -> 100 -> 30` | `100` |
| `get(0)` | same list | `10` |
| `get(3)` | same list | `30` (last valid index) |
| `get(4)` | same list (size 4) | `-1` (out of bounds — index 4 doesn't exist) |
| `get(-1)` | same list | `-1` |
| `get(0)` on empty list | `-1` |

## Better / Alternative Approach
Already the correct, optimal solution for a singly linked list's inherent constraints — O(i) is the best possible without switching to a different data structure (e.g., an array/`ArrayList` for O(1) random access, at the cost of O(n) insertion in the middle).

---

# Problem 6 — Delete (at Index)

## 1. What is the problem?
Remove the node at a given index, correctly re-linking around it.

## 2. Example
`delete(2)` on `10 -> 20 -> 100 -> 30` should remove `100`, leaving `10 -> 20 -> 30`.

## 3. My Code
```java
private Node delete(int index) throws Exception{
    if(index <0 || index>=size) {
        throw new Exception("wrong index");
    }
    if(index == 0) {
        head = head.next;
        size--;
        return head;
    }
    else if(index == size-1) {
        Node t = head;
        for(int i = 0;i<index-1;i++) {
            t = t.next;
        }
        t.next = null;
        size--;
        return head;
    }else {
        Node t = head;
        for(int i = 0;i<index-1;i++) {
            t = t.next;
        }
        t.next = t.next.next;
    }
    size--;
    return head;
}
```

## 4. Issues / Bugs / Edge Cases
None — correct, though the structure is worth reading carefully once: the final `size--; return head;` at the very bottom of the method looks at first glance like it might run on *every* call, but it's only actually reached by the general (middle-deletion) `else` branch — both the `index==0` and `index==size-1` branches explicitly `return` before ever reaching that final pair of lines. So there's no double-decrement bug, just a structure that reads a little ambiguously on a quick skim.

## 5. Intuition & Why Three Cases (Same Shape as Problem 2)
Same reasoning as `addAtIndex`: deleting the **first** node just means moving `head` forward (no "node before" needed). Deleting the **last** node or a **middle** node both need the node immediately *before* the target (to rewire its `.next`), found the same way — walk `index-1` steps from the head. The only difference between the "last" and "middle" branches is that deleting the last node sets `.next = null` (nothing to reconnect to), while deleting a middle node sets `.next = t.next.next` (skip over the removed node, reconnecting to whatever came after it).

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `if (index==0) { head = head.next; ... }` | Removing the first node is just "make the second node the new head" |
| `for(i=0;i<index-1;i++) t=t.next;` (both other branches) | Walk to the node right before the deletion target |
| `t.next = null;` (last-node branch) | Nothing comes after the deleted node anymore |
| `t.next = t.next.next;` (middle branch) | Skip over the removed node entirely — the node that used to be two steps ahead of `t` is now directly connected |

## 7. Dry Run — `delete(2)` on `10 -> 20 -> 100 -> 30` (size 4)
```text
index=2, size=4. index≠0. index==size-1(3)? 2≠3 → general (middle) case.
t = head = 10.
for i=0; i<1 (index-1=1); i++: i=0 → t = t.next = 20. loop stops.
t = 20.
t.next = t.next.next → 20.next was 100, now becomes 100.next = 30. So 20.next = 30.
size-- → size=3.
```
Result: `10 -> 20 -> 30 -> null` — correctly removed `100`.

## 8. Test Cases
| Call | On list | Result |
|---|---|---|
| `delete(2)` | `10 -> 20 -> 100 -> 30` | `10 -> 20 -> 30` |
| `delete(0)` | `10 -> 20 -> 30` | `20 -> 30` (head removal) |
| `delete(2)` | `10 -> 20 -> 30` (size 3, index 2 == size-1) | `10 -> 20` (last-node removal) |
| `delete(5)` on a 3-element list | throws `Exception("wrong index")` |
| `delete(0)` on a single-node list | `head` becomes `null`, list is now empty |

## Better / Alternative Approach
Correct and already O(index) — the best possible for a singly linked list without a `tail`/back-pointer. The only stylistic improvement: like `addAtIndex`, the "walk to `index-1`" logic is duplicated across two of the three branches — could be factored into a small private helper (`getNodeBefore(index)`) to avoid repeating it, purely for readability, not correctness or speed.

---

# Problem 7 — Contains (Search by Value)

## 1. What is the problem?
Check whether a given value exists anywhere in the list.

## 2. Example
`contains(1000)` on the shared example list — from the (commented-out) line in your `main()`.

## 3. My Code
```java
private boolean contains(int x) {
    Node t = head;
    while(t!=null) {
        if(x == t.val) {
            return true;
        }
        t = t.next;
    }
    return false;
}
```

## 4. Issues / Bugs / Edge Cases
None — correctly returns `false` for an empty list (loop never runs) and correctly short-circuits the moment a match is found rather than scanning the whole list unnecessarily.

## 5. Intuition & Why This Approach
A singly linked list has no ordering guarantee (unlike a sorted array or a BST) — so there's no way to skip checking any node. Straightforward linear scan is the only correct approach, but it's written to **stop early** the instant a match is found, which matters for the average case even though the worst case (value not present, or it's the last element) is still O(n).

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `while (t != null)` | Standard traversal guard |
| `if (x == t.val) return true;` | Early exit the moment a match is found |
| `t = t.next;` | Otherwise keep looking |
| `return false;` | Only reached if the whole list was scanned with no match |

## 7. Dry Run — `contains(1000)` on `10 -> 20 -> 100 -> 30`
```text
t=10: 1000==10? no. t=20.
t=20: 1000==20? no. t=100.
t=100: 1000==100? no. t=30.
t=30: 1000==30? no. t=null.
t=null: loop stops.
return false.
```

## 8. Test Cases
| Call | On list | Result |
|---|---|---|
| `contains(1000)` | `10 -> 20 -> 100 -> 30` | `false` |
| `contains(100)` | same list | `true` |
| `contains(10)` | same list | `true` (first element) |
| `contains(30)` | same list | `true` (last element — worst case for early-exit, still O(n)) |
| `contains(5)` on empty list | `false` |

## Better / Alternative Approach
Already optimal O(n) for an unsorted singly linked list — there's no way to do better without additional structure (e.g., a `HashSet<Integer>` maintained alongside the list would give O(1) lookups, at the cost of O(n) extra space and the bookkeeping overhead of keeping it in sync with every insertion/deletion).

---

# Problem 8 — Find Index (Search Returning Position)

## 1. What is the problem?
Same search as Problem 7, but return the **position** of the match instead of just whether it exists — `-1` if not found.

## 2. Example
`findIndex(100)` on the shared example list — the actual last line of your `main()`.

## 3. My Code
```java
private int findIndex(int val) {
    Node t = head;
    int i = 0;
    while(t!=null) {
        if(t.val == val) {
            return i;
        }
        i++;
        t = t.next;;
    }
    return -1;
}
```

## 4. Issues / Bugs / Edge Cases
None functionally — the double semicolon (`t = t.next;;`) is a harmless empty statement, not a bug, just a stray keystroke.

## 5. Intuition & Why This Approach
Nearly identical to `contains` (Problem 7) — the only addition is a running counter `i` that increments alongside the traversal, so that when a match is found, you already know exactly how many steps you took to get there. This is the natural way to turn a "does it exist" check into a "where is it" check without needing a second pass.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `int i = 0;` | Position counter, starts at index 0 (the head) |
| `if (t.val == val) return i;` | Found it — `i` already reflects the correct position |
| `i++; t = t.next;` | Advance both the counter and the pointer together, in lockstep |

## 7. Dry Run — `findIndex(100)` on `10 -> 20 -> 100 -> 30`
```text
t=10,i=0: 10==100? no. i=1. t=20.
t=20,i=1: 20==100? no. i=2. t=100.
t=100,i=2: 100==100? yes → return 2.
```
Result: **2** — matches your own `main()`'s printed output exactly.

## 8. Test Cases
| Call | On list | Result |
|---|---|---|
| `findIndex(100)` | `10 -> 20 -> 100 -> 30` | `2` |
| `findIndex(10)` | same list | `0` |
| `findIndex(999)` | same list | `-1` |
| `findIndex(5)` on empty list | `-1` |

## Better / Alternative Approach
Already optimal O(n). Worth noting as a DRY opportunity (not a correctness issue): `contains(x)` and `findIndex(x)` do almost identical traversals — `contains` could simply be rewritten as `return findIndex(x) != -1;` to avoid maintaining two nearly-duplicate loops that could drift out of sync if one is ever modified without the other.

---

# Problem 9 — Reverse Linked List (Iterative)

## 1. What is the problem?
LeetCode 206. Reverse the entire list in place — the old tail becomes the new head, and every `.next` pointer flips direction.

## 2. Example
`reverseLL(li.head)` on the shared example list `10 -> 20 -> 100 -> 30`.

## 3. My Code
```java
public Node reverseLL(Node head) {
    if(head == null || head.next == null) {
        return head;
    }
    Node p = null;
    Node c = head;
    while( c != null) {
        Node n = c.next;
        c.next = p;
        p = c;
        c = n;
    }
    return p;
}
```

## 4. Issues / Bugs / Edge Cases
None — correctly handles both the empty-list and single-node cases via the early return (a single node, or no nodes, is trivially "already reversed").

## 5. Intuition & Why Three Pointers
Reversing a singly linked list in place needs exactly three tracked pointers at every step: **`p`** (previous — what the current node should now point *back* to), **`c`** (current — the node actively being flipped), and **`n`** (next — saved *before* you overwrite `c.next`, since once you flip `c.next` to point backward, you'd otherwise lose the way forward entirely). This "save next before you destroy it" step is the one line that makes the whole algorithm work — skip it and you'd disconnect the rest of the list the moment you flip the first pointer.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `Node n = c.next;` | **Critical** — grab the forward path before it's overwritten |
| `c.next = p;` | Flip this node's pointer to point backward instead of forward |
| `p = c;` | This node is now "done" — it becomes the `previous` for the next iteration |
| `c = n;` | Advance to the node we saved a moment ago |
| `return p;` | When the loop ends (`c` is `null`), `p` is sitting on the last node processed — which is now the new head |

## 7. Dry Run — `reverseLL(head)` on `10 -> 20 -> 100 -> 30`
| Step | c (before) | n = c.next | c.next = p | p (after) | c (after) |
|---|---|---|---|---|---|
| 1 | 10 | 20 | `10.next = null` | 10 | 20 |
| 2 | 20 | 100 | `20.next = 10` | 20 | 100 |
| 3 | 100 | 30 | `100.next = 20` | 100 | 30 |
| 4 | 30 | null | `30.next = 100` | 30 | null |

Loop ends (`c == null`). Return `p = 30`.

Final structure: `30 -> 100 -> 20 -> 10 -> null` — the shared example list, fully reversed.

## 8. Test Cases
| Input | Output |
|---|---|
| `10 -> 20 -> 100 -> 30` | `30 -> 100 -> 20 -> 10` |
| Empty list (`head == null`) | `null` (unchanged) |
| Single node `[7]` | `[7]` (unchanged — a one-node list is its own reverse) |
| Two nodes `1 -> 2` | `2 -> 1` |

## Better / Alternative Approach
This is already the standard, optimal iterative solution: O(n) time, O(1) extra space. See Problem 10 for the recursive alternative — same time complexity, but O(n) *space* due to the call stack, worth knowing as the trade-off between the two styles.

---

# Problem 10 — Reverse Linked List (Recursive)

## 1. What is the problem?
Same as Problem 9, solved recursively instead of iteratively.

## 2. Example
`reverseR(li.head)` on the shared example list.

## 3. My Code
```java
public Node reverseR(Node head) {
    if(head == null || head.next == null) {
        return head;
    }
    Node h = reverseR(head.next);
    Node s = head.next;
    s.next = head;
    head.next = null;
    return h;
}
```

## 4. Issues / Bugs / Edge Cases
None — correct and handles the same base cases as the iterative version.

## 5. Intuition & Why This Approach
The recursion trusts that `reverseR(head.next)` will **fully reverse everything after the current node**, and returns the new head of that already-reversed sublist. All that's left for the current call to do is attach itself to the *end* of that already-reversed sublist:
- `head.next` (before any mutation) is the node that used to come right after `head` — but since everything after `head` has *already been reversed* by the recursive call, that same node is now the **last** node of the reversed sublist.
- So: make that last node point back to `head` (`s.next = head`), and make `head` the new tail by clearing its own `.next` (`head.next = null`).
- The overall new head (`h`) — found all the way down at the base case (the original last node) — just gets passed back up unchanged through every level of the recursion.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `if (head==null \|\| head.next==null) return head;` | Base case — the original last node (or an empty list) becomes the anchor that every level returns unchanged |
| `Node h = reverseR(head.next);` | Recursively reverse everything after this node first — `h` is always the *original last node*, unchanged all the way back up |
| `Node s = head.next;` | The node that's now the tail-end of the already-reversed sublist |
| `s.next = head;` | Attach the current node onto that tail — extending the reversed chain by one |
| `head.next = null;` | Current node becomes the new (temporary) tail, until an even earlier call attaches something after it |

## 7. Dry Run — `reverseR(10)` on `10 -> 20 -> 100 -> 30`
```text
reverseR(10): head=10, head.next=20 (not null) → recurse
  reverseR(20): head=20, head.next=100 (not null) → recurse
    reverseR(100): head=100, head.next=30 (not null) → recurse
      reverseR(30): head=30, head.next=null → BASE CASE → return 30
    h=30 (returned). s=head.next=30 (head is 100 here). s.next=head → 30.next=100.
    head.next=null → 100.next=null.
    return h=30
  h=30 (returned). s=head.next=100 (head is 20 here). s.next=head → 100.next=20.
  head.next=null → 20.next=null.
  return h=30
h=30 (returned). s=head.next=20 (head is 10 here). s.next=head → 20.next=10.
head.next=null → 10.next=null.
return h=30
```
Final links, assembled bottom-up during the unwind: `30.next=100`, `100.next=20`, `20.next=10`, `10.next=null`.

Result: `30 -> 100 -> 20 -> 10 -> null` — identical to Problem 9's output, confirming both implementations agree.

## 8. Test Cases
| Input | Output |
|---|---|
| `10 -> 20 -> 100 -> 30` | `30 -> 100 -> 20 -> 10` |
| Empty list | `null` |
| Single node `[7]` | `[7]` |
| Two nodes `1 -> 2` | `2 -> 1` |

## Better / Alternative Approach
Prefer the **iterative version (Problem 9)** for very long lists: this recursive version uses O(n) call-stack space, meaning a sufficiently long list (tens of thousands of nodes, depending on JVM stack size settings) risks a `StackOverflowError` that the iterative version simply can't hit, since it only ever uses three local variables regardless of list length. That said, this recursive version is a genuinely valuable one to understand deeply — the "trust the recursion to handle everything after me" mental model here is the same one that shows up throughout tree recursion (as in your Trees handbook), just applied to a linear structure instead of a branching one.

---

# Problem 11 — Middle of Linked List

## 1. What is the problem?
LeetCode 876. Find the middle node in a single pass, without knowing the list's length in advance. For even-length lists, return the **second** of the two middle nodes (the LeetCode-standard convention).

## 2. Example
`middleNode()` on the shared example list `10 -> 20 -> 100 -> 30` (even length, 4 nodes).

## 3. My Code
```java
private Node middleNode() {
    Node f = head;
    Node s = head;
    while(f!=null && f.next != null) {
        s = s.next;
        f = f.next.next;
    }
    return s;
}
```

## 4. Issues / Bugs / Edge Cases
None — correctly implements the standard "fast/slow" (tortoise and hare) technique, and correctly lands on the second middle node for even-length lists (verified in the dry run below).

## 5. Intuition & Why Fast/Slow Pointers
Two pointers start together at the head. The **fast** pointer moves two steps for every one step the **slow** pointer takes. By the time `fast` reaches the end of the list, `slow` — having covered exactly half the distance — is sitting right at the middle. This avoids a two-pass approach (count the length first, then walk to `length/2`) entirely, doing it in a **single pass** instead.

**Why the loop condition is `f != null && f.next != null`** (not just `f != null`): this specifically controls what happens for even-length lists. If `f.next` is `null`, that means `f` is exactly one step away from falling off the end — stopping *here* rather than trying to jump one more pair of steps is precisely what makes `s` land on the *second* middle node for even lengths, matching LeetCode's expected convention.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `f = head; s = head;` | Both start together |
| `while (f != null && f.next != null)` | Keep going as long as fast can still take a full two-step jump |
| `s = s.next;` | Slow advances one step |
| `f = f.next.next;` | Fast advances two steps — this is what creates the 2:1 speed ratio |
| `return s;` | Once fast can't jump anymore, slow is exactly at the middle |

## 7. Dry Run — `middleNode()` on `10 -> 20 -> 100 -> 30` (4 nodes, even)
```text
f=10, s=10.
iter1: f≠null, f.next=20≠null → true. s=s.next=20. f=f.next.next=10.next.next=100.
iter2: f=100, f.next=30≠null → true. s=s.next=100. f=f.next.next=100.next.next=null (since 100.next=30, 30.next=null).
iter3 check: f=null → loop stops.
return s=100.
```
Result: node with value **100** — the second of the two middle elements (`20` and `100`), correctly matching the LeetCode convention for even-length lists.

## 8. Test Cases
| Input | Output |
|---|---|
| `10 -> 20 -> 100 -> 30` (even, 4 nodes) | `100` (second middle) |
| `1 -> 2 -> 3 -> 4 -> 5` (odd, 5 nodes) | `3` (the single true middle) |
| Single node `[7]` | `7` |
| Empty list | `null` (loop never runs, `s` stays `head` which is `null`) |
| Two nodes `1 -> 2` | `2` (second of the two "middle" elements) |

## Better / Alternative Approach
Already the standard, optimal single-pass O(n) time / O(1) space solution — this is the expected approach for this exact problem. A two-pass alternative (count length, then walk `length/2` steps) also works but is strictly worse (touches the list twice instead of once) with no compensating benefit.

---

# Problem 12 — Nth Node From End

## 1. What is the problem?
Return the value/node that sits `n` positions from the end of the list (1-indexed: `n=1` means the very last node), without knowing the list's length upfront.

## 2. Example
`nthFromEnd(2)` on the shared example list `10 -> 20 -> 100 -> 30` — should return the node `100` (2nd from the end).

## 3. My Code
```java
private Node nthFromEnd(int n) {
    Node f = head;
    Node s = head;
    for(int i = 0;i<n;i++) {
        f = f.next;
    }
    while(f!=null) {
        f = f.next;
        s = s.next;
    }
    return s;
}
```

## 4. Issues / Bugs / Edge Cases
**Unguarded edge case worth knowing:** if `n` is greater than the list's length, the very first `for` loop will run `f.next` on a `null` `f` partway through, throwing a `NullPointerException`. There's no upfront check like `if (n > size) throw ...` or similar. This is more of an *implicit precondition* (the method assumes `1 <= n <= size`) than an outright bug in normal usage, but worth being deliberate about if this is ever called with untrusted input.

## 5. Intuition & Why a "Gap" of `n`
The trick: first advance the `fast` pointer **`n` steps ahead** of `slow`, creating a fixed gap of exactly `n` nodes between them. Then move both pointers forward together, one step at a time, until `fast` falls off the end (`null`). Because the gap between them never changes, the moment `fast` reaches the end, `slow` is necessarily sitting exactly `n` positions before the end — without ever needing to know the list's total length in advance.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `for(i=0;i<n;i++) f=f.next;` | Establish the gap — advance `fast` `n` steps while `slow` stays put |
| `while (f != null) { f=f.next; s=s.next; }` | Move both together, preserving the gap, until `fast` runs out of list |
| `return s;` | `slow` is now exactly `n` nodes from the end |

## 7. Dry Run — `nthFromEnd(2)` on `10 -> 20 -> 100 -> 30`
```text
f=10, s=10.
for i=0,1 (n=2): f=f.next=20, f=f.next=100. after loop, f=100.
while(f!=null):
  f=f.next=30, s=s.next=20. f not null.
  f=f.next=null, s=s.next=100. loop stops (f is null).
return s=100.
```
Result: node `100` — correctly the 2nd-from-last node in `10, 20, 100, 30` (counting from the end: `30` is 1st, `100` is 2nd).

## 8. Test Cases
| Call | On list | Result |
|---|---|---|
| `nthFromEnd(2)` | `10 -> 20 -> 100 -> 30` | `100` |
| `nthFromEnd(1)` | same list | `30` (the last node) |
| `nthFromEnd(4)` | same list (size 4) | `10` (the head, when n equals the full length) |
| `nthFromEnd(5)` | same list (size 4, n exceeds length) | **currently crashes with `NullPointerException`** — see bug above |

## Better / Alternative Approach
Add a bounds guard for robustness:
```java
private Node nthFromEnd(int n) {
    if (n <= 0 || n > size) { return null; }
    ...
}
```
With that fix, this is already the standard, optimal single-pass O(n) solution — no further algorithmic improvement needed.

---

# Problem 13 — Remove Nth Node From End (Version A — Dummy Node)

## 1. What is the problem?
LeetCode 19. Remove the node sitting `n` positions from the end, in a single pass, correctly handling the case where the node to remove is the head itself.

## 2. Example
`removeNthFromEnd(2)` on the shared example list `10 -> 20 -> 100 -> 30` — should remove `100` (2nd from end), leaving `10 -> 20 -> 30`.

## 3. My Code
```java
private Node removeNthFromEnd(int n) {
    Node dummy = new Node(0);
    dummy.next = head;
    Node f = dummy;
    Node s = dummy;
    for(int i = 0; i <= n; i++) {
        f = f.next;
    }
    while(f != null) {
        f = f.next;
        s = s.next;
    }
    s.next = s.next.next;
    head = dummy.next;
    return head;
}
```

## 4. Issues / Bugs / Edge Cases
None — correctly handles removing the head via the dummy node technique (see intuition below), and correctly reassigns the instance field `head` at the end so the change actually takes effect on the object, not just the local variable.

## 5. Intuition & Why a Dummy Node
This is the same "gap of n" idea as Problem 12, but with a genuinely important twist: **both pointers start at a dummy node** placed *before* the real head, and the gap established is `n+1` (note the `i <= n` loop, meaning `n+1` iterations), not `n`. This one-node offset is exactly what's needed so that when the pointers finish moving, `slow` (`s`) lands on the node **immediately before** the one to remove — not on the target node itself, since you need the predecessor to rewire `.next` around it.

**Why the dummy node specifically matters:** without it, removing the actual head node would need special-case handling (there's no "node before the head" to rewire) — exactly the same problem Problems 2 and 6 solved with explicit `if (i==0)` branches. The dummy node sidesteps that entirely: it acts as a permanent "node before the head" that always exists, so the exact same logic handles head-removal and middle-removal identically, with zero special-casing.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `Node dummy = new Node(0); dummy.next = head;` | Create a throwaway node that sits conceptually "one before" the real list |
| `for(i=0;i<=n;i++) f=f.next;` | Advance fast `n+1` steps from the dummy — this establishes the offset that makes `slow` land on the predecessor, not the target |
| `while (f != null) { f=f.next; s=s.next; }` | Move both together until fast runs out |
| `s.next = s.next.next;` | `s` is the predecessor — skip over the target node entirely |
| `head = dummy.next;` | Read the (possibly new) head back out of the dummy, since the dummy's `.next` always correctly reflects whatever the current first real node is |

## 7. Dry Run — `removeNthFromEnd(2)` on `10 -> 20 -> 100 -> 30`
```text
dummy -> 10 -> 20 -> 100 -> 30. f=dummy, s=dummy.
for i=0,1,2 (n=2, so 3 iterations, i<=2): f=dummy.next=10, f=10.next=20, f=20.next=100.
after loop, f=100.
while(f != null):
  f=f.next=30, s=s.next=10. f not null.
  f=f.next=null, s=s.next=20. loop stops.
s=20. s.next = s.next.next → 20.next was 100, becomes 100.next = 30. So 20.next=30.
head = dummy.next = 10 (unchanged, since we didn't remove the head this time).
```
Result: `10 -> 20 -> 30 -> null` — correctly removed `100`.

**Head-removal test** — `removeNthFromEnd(4)` on the same 4-node list (removing the head, `10`):
```text
dummy -> 10 -> 20 -> 100 -> 30. f=dummy, s=dummy.
for i=0..4 (n=4, 5 iterations, i<=4): f=dummy.next=10, f=20, f=100, f=30, f=30.next=null.
after loop, f=null.
while(f != null): false immediately → loop never runs. s stays = dummy.
s.next = s.next.next → dummy.next was 10, becomes 10.next = 20. So dummy.next = 20.
head = dummy.next = 20.
```
Result: `20 -> 100 -> 30 -> null` — correctly removed the head (`10`), with **zero special-case code** needed for this scenario.

## 8. Test Cases
| Call | On list | Result |
|---|---|---|
| `removeNthFromEnd(2)` | `10 -> 20 -> 100 -> 30` | `10 -> 20 -> 30` |
| `removeNthFromEnd(4)` | same list (n == size, removes head) | `20 -> 100 -> 30` |
| `removeNthFromEnd(1)` | same list (removes the last node) | `10 -> 20 -> 100` |
| `removeNthFromEnd(1)` | single-node list `[7]` | empty list (`head = null`) |

## Better / Alternative Approach
This is already the standard, textbook-optimal O(n) single-pass solution — the dummy-node technique here is genuinely the cleanest known way to solve LeetCode 19 without special-casing head removal. See Problem 14 for a different (also correct) way to solve the exact same head-removal challenge, worth comparing directly.

---

# Problem 14 — Remove Nth Node From End (Version B — Early-Return Style)

## 1. What is the problem?
The exact same problem as Problem 13, solved a second time with a different technique — worth comparing directly since both handle head-removal correctly, just via different mechanisms.

## 2. Example
`removeNthFromEnd(li.head, 2)` on the shared example list.

## 3. My Code
```java
public Node removeNthFromEnd(Node head, int n) {
    if(head == null || head.next == null){
        return null;
    }
    Node p = head;
    Node q = head;
    for(int i = 0;i<n;i++){
        q = q.next;
    }
    if(q==null){
        return head.next;
    }
    while(q.next != null ){
        p = p.next;
        q = q.next;
    }
    p.next = p.next.next;
    return head;
}
```

## 4. Issues / Bugs / Edge Cases
None functionally — but the very first guard (`head == null || head.next == null → return null`) is **partially redundant**: for the `head == null` case, it correctly prevents a crash (without it, the `for` loop below would call `.next` on a `null` `q`). But for the `head.next == null` case (a single-node list), the general logic below would actually reach the *exact same correct answer on its own* — tracing it: `p=q=head`; the `for` loop with `n=1` sets `q=head.next=null`; then `if (q==null) return head.next;` → returns `null` anyway. So that half of the guard is a harmless shortcut, not something strictly necessary — worth knowing it's there more for clarity/fast-path than because the general logic can't handle it.

## 5. Intuition & Why This Approach (and How It Differs From Version A)
Same "gap" technique as Problem 13, but this version handles head-removal with an **explicit early-return check** instead of a dummy node: advance `q` (fast) exactly `n` steps. **If `q` becomes `null` at that point**, that specifically means the node to remove was the head itself (there weren't enough nodes left after `n` steps to need a predecessor at all) — so just return `head.next` directly, skipping the head. Otherwise, advance both `p` and `q` together until `q` reaches the *last* node (note: `while (q.next != null)`, not `while (q != null)` — a deliberate difference from Version A, landing `p` on the predecessor of the target via a slightly different stopping condition).

**Version A vs. Version B, side by side:** both solve "how do I remove the head without special-casing it" — Version A sidesteps the problem entirely with a permanent fake predecessor (the dummy node); Version B detects the head-removal case explicitly and handles it with its own dedicated return statement. Neither is "more correct" than the other — they're two legitimate, different engineering choices for the exact same requirement, similar in spirit to how your two `CeilBSTrecursive` versions in the Trees handbook took different approaches to shared state.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `if (head==null \|\| head.next==null) return null;` | Fast-path / null-safety guard (see note above on partial redundancy) |
| `for(i=0;i<n;i++) q=q.next;` | Advance `q` exactly `n` steps (not `n+1`, unlike Version A — a direct consequence of not having a dummy node to offset from) |
| `if (q==null) return head.next;` | **The head-removal detector** — if `q` ran off the end after exactly `n` steps, the target was the head |
| `while (q.next != null) { p=p.next; q=q.next; }` | Advance both together until `q` is at the true last node — `p` ends up at the predecessor of the target |
| `p.next = p.next.next;` | Skip over the target node |

## 7. Dry Run — `removeNthFromEnd(head, 2)` on `10 -> 20 -> 100 -> 30`
```text
head=10, head.next=20 (not null) → proceed past the guard.
p=10, q=10.
for i=0,1 (n=2): q=10.next=20, q=20.next=100.
after loop, q=100. Not null → skip the head-removal shortcut.
while(q.next != null):
  q=100, q.next=30 (not null) → p=p.next=20, q=q.next=30.
  q=30, q.next=null → loop stops.
p=20. p.next = p.next.next → 20.next was 100, becomes 100.next = 30. So 20.next=30.
return head=10 (the reference itself is unchanged, since the head wasn't removed).
```
Result: `10 -> 20 -> 30 -> null` — identical result to Version A (Problem 13).

**Head-removal test** — same call pattern, `removeNthFromEnd(head, 4)` on the same 4-node list:
```text
head=10, head.next=20 (not null) → proceed.
p=10, q=10.
for i=0..3 (n=4): q=10.next=20, q=20.next=100, q=100.next=30, q=30.next=null.
after loop, q=null → head-removal detected! return head.next = 20.
```
Result: `20 -> 100 -> 30 -> null` — same result as Version A's head-removal test.

## 8. Test Cases
| Call | On list | Result |
|---|---|---|
| `removeNthFromEnd(head, 2)` | `10 -> 20 -> 100 -> 30` | `10 -> 20 -> 30` |
| `removeNthFromEnd(head, 4)` | same list (removes head) | `20 -> 100 -> 30` |
| `removeNthFromEnd(head, 1)` on single-node `[7]` | `null` (caught by the very first guard) |
| `removeNthFromEnd(null, 1)` | `null` (caught by the very first guard, `head == null` branch) |

## Better / Alternative Approach
Both this version and Version A (Problem 13) are already correct, optimal O(n) single-pass solutions — genuinely worth keeping both in mind as two different valid patterns for "avoid special-casing head removal": the **dummy node** pattern (Version A) generalizes well to lots of linked-list problems beyond just this one, while the **explicit early-return** pattern (this version) can be slightly more direct to read when there's only one specific edge case to handle, as here.

---

# Problem 15 — Has Cycle (Floyd's Tortoise and Hare)

## 1. What is the problem?
LeetCode 141. Determine whether the list contains a cycle (some node's `.next` eventually loops back to an earlier node instead of reaching `null`).

## 2. Example
The shared example list `10 -> 20 -> 100 -> 30` has **no** cycle, so it's not a very interesting trace on its own — using a dedicated cyclic example instead for this problem and the next:
```text
A -> B -> C -> D -> B   (D points back to B, not to null — a cycle exists, starting at B)
```

## 3. My Code
```java
private boolean hasCycle() {
    if(head == null || head.next == null) {
        return false;
    }
    Node f = head;
    Node s = head;
    while(f!=null && f.next != null && s != null) {
        f = f.next.next;
        s = s.next;
        if(f==s) {
            return true;
        }
    }
    return false;
}
```

## 4. Issues / Bugs / Edge Cases
None — correct. (Minor observation, not a bug: the `s != null` check in the `while` condition is technically redundant, since `s` moves at half the speed of `f` — if `f` were ever to become `null` first, the loop would already exit via the `f != null` check before `s` could possibly reach `null` too. Harmless, just an extra check that never actually changes the outcome.)

## 5. Intuition & Why Two Speeds Detect a Cycle
If there's no cycle, the **fast** pointer (moving two steps at a time) simply reaches `null` first, same as in a normal traversal — no surprises. But if there **is** a cycle, both pointers eventually enter it and just keep looping around forever — and because `fast` gains on `slow` by exactly one extra step every iteration, it's mathematically guaranteed to eventually "lap" `slow` and land on the exact same node at the same time. That collision (`f == s`) is the proof a cycle exists — there's no other way two independently-moving pointers on a finite structure could ever meet like that except by both being trapped in a loop.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `if (head==null \|\| head.next==null) return false;` | Trivial cases — can't have a meaningful cycle with 0 or 1 nodes pointing to `null` |
| `f = f.next.next; s = s.next;` | The 2:1 speed difference is the entire mechanism |
| `if (f==s) return true;` | The moment they land on the same node, a cycle is proven |
| `return false;` | Only reached if `f` (or `f.next`) hit `null` naturally — no cycle exists |

## 7. Dry Run — `hasCycle()` on the cyclic example `A -> B -> C -> D -> B`
```text
f=A, s=A.
iter1: f=A.next.next=C (A->B->C). s=A.next=B. f==s? C vs B → no.
iter2: f=C.next.next=B (C->D->B, since D.next=B). s=B.next=C. f==s? B vs C → no.
iter3: f=B.next.next=D (B->C->D). s=C.next=D. f==s? D vs D → YES → return true.
```
Result: **true** — cycle correctly detected after 3 iterations.

**Contrast — the shared (acyclic) example list `10 -> 20 -> 100 -> 30`:**
```text
f=10,s=10. iter1: f=100,s=20. iter2: f=null(30.next.next, since 30.next=null→f.next.next fails)... 
```
Actually let's trace precisely: iter2: f=100, f.next=30(not null)→loop continues. f=f.next.next=30.next=null. s=s.next=100. Check loop condition again: f==null → loop stops. return false. Correctly reports no cycle.

## 8. Test Cases
| Input | Output |
|---|---|
| `A -> B -> C -> D -> B` (cycle back to B) | `true` |
| `10 -> 20 -> 100 -> 30` (shared example, no cycle) | `false` |
| Empty list | `false` |
| Single node with no self-loop | `false` |
| Single node pointing to itself (`A -> A`) | `true` (a valid, if unusual, cycle case — this method correctly handles it since `head.next` isn't `null` in this scenario, so it doesn't get caught by the trivial-case guard, and the main loop correctly detects `f==s` on the very first iteration) |

## Better / Alternative Approach
This is already the standard, optimal solution: O(n) time, **O(1) space** — the defining advantage of Floyd's algorithm. The straightforward alternative — maintain a `HashSet<Node>` of every node visited, and check membership before advancing — also works correctly and is arguably easier to read at first glance, but costs O(n) *extra space*, which is exactly what Floyd's technique avoids.

---

# Problem 16 — Detect Cycle Start Node

## 1. What is the problem?
LeetCode 142. If a cycle exists, don't just report *that* one exists (Problem 15) — return the **specific node where the cycle begins**.

## 2. Example
Same cyclic example as Problem 15: `A -> B -> C -> D -> B` (cycle starts at `B`).

## 3. My Code
```java
private Node detectCycleStart() {
    if(head == null || head.next == null) {
        return head;
    }
    Node f = head;
    Node s = head;
    while(f!=null && f.next != null && s != null) {
        f = f.next.next;
        s = s.next;
        if(f==s) {
             	Node p1 = head;
                Node p2 = s;
                
                while(p1 != p2) {
                    p1 = p1.next;
                    p2 = p2.next;
                }

                return p1;
        }
    }
    return null;
}
```

## 4. Issues / Bugs / Edge Cases
Functionally correct for genuine cycle detection and start-finding. One small semantic wrinkle worth knowing: for a list with no cycle at all where `head.next == null` (a single, non-looping node), this returns `head` (the node itself) via the early guard — rather than `null`, which is what the main return-null-at-the-bottom path uses to mean "no cycle." A caller who assumes "non-null return = a real cycle start" could be misled by this one specific case. Not a crash, just a minor inconsistency in what "no cycle" is represented as across the two return paths.

## 5. Intuition & Why the Second Phase Works (Floyd's Algorithm, Part 2)
Phase 1 is identical to Problem 15 — fast/slow pointers meet somewhere *inside* the cycle once one exists. The genuinely clever part is **phase 2**: reset one pointer (`p1`) back to `head`, leave the other (`p2`) at the meeting point, then move **both one step at a time**. They are mathematically guaranteed to meet again — this time exactly at the **start of the cycle**. This relies on a real mathematical property of the distances involved (the distance from `head` to the cycle's start equals the distance from the meeting point back around to the cycle's start, given how far ahead `fast` had traveled relative to `slow` before they first met) — it's not obvious by inspection, but it's a well-established, provable result, and your implementation applies it correctly.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| (phase 1, identical to Problem 15) | Detect that a cycle exists and find *a* meeting point somewhere inside it |
| `Node p1 = head; Node p2 = s;` | Phase 2 setup — one pointer resets to the true start, the other stays at the meeting point |
| `while (p1 != p2) { p1=p1.next; p2=p2.next; }` | Advance both at equal (single) speed — they're guaranteed to converge exactly at the cycle's start |
| `return p1;` | The node both pointers land on together is the cycle's starting node |

## 7. Dry Run — `detectCycleStart()` on `A -> B -> C -> D -> B`
```text
Phase 1 (identical trace to Problem 15): f and s meet at node D after 3 iterations.
Phase 2:
p1 = head = A. p2 = s = D.
Check p1 != p2: A != D → true, keep going.
  p1 = p1.next = B. p2 = p2.next = B (D.next = B, per the cycle).
Check p1 != p2: B != B → false → loop stops.
return p1 = B.
```
Result: node **B** — correctly identified as the cycle's starting node.

## 8. Test Cases
| Input | Output |
|---|---|
| `A -> B -> C -> D -> B` | node `B` |
| `10 -> 20 -> 100 -> 30` (no cycle) | `null` |
| Empty list | `null` (returned via `head`, which is itself `null`) |
| Single node, no self-loop | that same node object is returned (per the semantic wrinkle noted above — worth being aware this isn't `null` even though there's no real cycle) |
| Single node pointing to itself (`A -> A`) | node `A` (correctly identified as its own cycle start) |

## Better / Alternative Approach
This is already the standard, optimal Floyd's-algorithm solution: O(n) time, O(1) space — genuinely one of the more elegant results in classic algorithms, and your implementation is textbook-correct. The straightforward alternative (`HashSet<Node>`, return the first node seen twice) also works and is more intuitive to derive from scratch, but costs O(n) space, same trade-off as Problem 15.

---

# Problem 17 — Is Palindrome

## 1. What is the problem?
LeetCode 234. Check whether the list reads the same forward and backward — e.g. `1 -> 2 -> 3 -> 2 -> 1` is a palindrome, `1 -> 2 -> 3` is not.

## 2. Example
The shared example list `10 -> 20 -> 100 -> 30` is **not** a palindrome — using a dedicated palindrome example for a more useful trace:
```text
7 -> 3 -> 3 -> 7
```

## 3. My Code
```java
private boolean isPalindrome() {
    if(head == null || head.next == null) {
        return true;
    }
    Node f = head;
    Node s = head;
    while(f!= null && f.next!=null) {
        f = f.next.next;
        s = s.next;
    }
    Node h1 = head;
    Node h2 = reverseLL(s);
    while(h2 != null) {
        if(h1.val != h2.val) {
            return false;
        }
        h1 = h1.next;
        h2 = h2.next;
    }
    return true;
}
```

## 4. Issues / Bugs / Edge Cases
**Real side-effect worth knowing, even though the method itself is logically correct:** this reverses the **second half of the original list in place** (by calling `reverseLL(s)`, which mutates real nodes belonging to the list, not a copy). After `isPalindrome()` returns, the list's back half is left permanently reversed — the original forward structure is **not restored**. If your code (or anything else) relies on the list staying intact after calling this method, that's a genuine bug waiting to surface; if you only ever needed the boolean answer and never touch the list again, it happens to be harmless. Worth being deliberate about which situation you're in.

## 5. Intuition & Why "Find Middle, Reverse Second Half, Compare"
Checking a palindrome the "obvious" way (compare first element to last, second to second-last, etc.) is easy on an array but painful on a singly linked list, since there's no way to walk backward from the end. The trick: use the exact same fast/slow "find the middle" technique from Problem 11, then **reverse only the second half** — now that reversed half can be walked forward, comparing element-by-element against the first half walked forward too. Two forward walks, meeting in the middle conceptually, replace what would otherwise require backward traversal.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `while (f != null && f.next != null) { f=f.next.next; s=s.next; }` | Standard middle-finding (same as Problem 11), leaves `s` at (or just past) the middle |
| `Node h2 = reverseLL(s);` | Reverse everything from the middle onward — **this mutates the actual list**, not a copy |
| `while (h2 != null) { if (h1.val != h2.val) return false; ... }` | Walk both halves forward simultaneously, comparing values pairwise |
| `return true;` | Only reached if every pair matched |

## 7. Dry Run — `isPalindrome()` on `7 -> 3 -> 3 -> 7`
```text
Find middle: f=7,s=7.
  iter1: f.next=3(not null)→f=f.next.next=3(second 3). s=s.next=3(first 3).
  iter2: f=3(second),f.next=7(not null)→f=f.next.next=null(since second-3.next=7,7.next=null). s=s.next=3(second 3).
  loop stops (f null).
s = second "3" node (index 2 in the list).

h1 = head = 7(first).
h2 = reverseLL(s) → reverses the sublist starting at the second "3": original "3 -> 7", reversed becomes "7 -> 3".
  So h2 = 7(second, now first of the reversed piece) -> 3(second, now last).

Compare:
  h1=7(first), h2=7(the reversed one): equal. h1=h1.next=3(first). h2=h2.next=3(second, now last in reversed chain).
  h1=3(first), h2=3(second): equal. h1=h1.next=3(second, since original first-3's next is second-3). h2=h2.next=null (reversed chain ended).
  loop stops (h2 null).
return true.
```
Result: **true** — correctly identified as a palindrome. (Side effect: the original list's back half, `3 -> 7`, is now permanently `7 -> 3` in memory.)

## 8. Test Cases
| Input | Output |
|---|---|
| `7 -> 3 -> 3 -> 7` (even length) | `true` |
| `1 -> 2 -> 3 -> 2 -> 1` (odd length) | `true` |
| `10 -> 20 -> 100 -> 30` (shared example, not a palindrome) | `false` |
| Empty list | `true` (vacuously a palindrome) |
| Single node `[7]` | `true` |
| `1 -> 2` (not a palindrome) | `false` |

## Better / Alternative Approach
**Restore the list after checking**, if the caller shouldn't see a mutated structure — reverse the second half back once the comparison is done:
```java
// after the comparison loop, before returning:
reverseLL(s);  // un-reverse it, restoring the original order
```
This preserves the O(1)-extra-space advantage while eliminating the side effect. The alternative that avoids mutation *entirely*: copy all values into an array (or push them onto a stack) while traversing once, then compare against the list again from the front — trades O(n) extra space for a guarantee the original list is never touched at all.

---

# Problem 18 — Delete Every Kth Node

## 1. What is the problem?
Walk through the list and remove every `k`-th node (1-indexed position: `k`, `2k`, `3k`, ...), leaving the rest connected.

## 2. Example
`deleteK(li.head, 2)` on the shared example list `10 -> 20 -> 100 -> 30` — should remove the 2nd and 4th nodes (`20` and `30`), leaving `10 -> 100`.

## 3. My Code
```java
Node deleteK(Node head, int k) {
    if(head == null || k<=0) {
        return head;
    }
    Node c = head;
    Node p = null;
    int i = 0;
    while(c != null) {
        i++;
        if(i%k == 0) {
            if(p == null) {
                head = head.next;
            }else {
                p.next = c.next;
            }
        }else{
            p = c;
        }
        c = c.next;
    }
    return head;
}
```

## 4. Issues / Bugs / Edge Cases
None — genuinely correct, including two subtle cases worth explicitly recognizing worked correctly: (1) `k = 1` (delete *every* node, including consecutive ones), and (2) deleting the very first node when it happens to be a multiple of `k`. Both are traced below.

## 5. Intuition & Why the Counter Never Resets
A running counter `i` (never reset, incrementing across the *entire* list regardless of how many deletions have happened) tracks each node's original 1-indexed position. Whenever `i % k == 0`, the current node `c` needs to go. The `p` pointer tracks the most recently **kept** node — so deletion is just the usual "predecessor skips over the target" rewiring (`p.next = c.next`), with one extra wrinkle: if `p` is still `null` (meaning no node has been kept *yet* — the very first node(s) themselves are being deleted), there's no predecessor to rewire, so `head` itself has to advance instead.

**Why `c = c.next` at the very end always uses the target's own original `.next`, not something already modified:** deleting a node only ever changes *its predecessor's* `.next` pointer (`p.next = c.next` or `head = head.next`) — it never touches `c.next` itself. So `c`'s own forward pointer remains a reliable way to continue the walk, even for a node that was just logically removed from the chain.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `if (head==null \|\| k<=0) return head;` | Defensive guard — nothing to do on an empty list, and `k<=0` would make "every kth node" meaningless |
| `i++;` | Track this node's original position, unconditionally, every iteration |
| `if (i%k==0)` | This node is due for deletion |
| `if (p==null) head=head.next;` | No surviving predecessor yet — the list's front itself must move forward |
| `else p.next=c.next;` | Normal case — the last kept node skips over the one being deleted |
| `else { p = c; }` | This node survives — it becomes the new "last kept node" for future deletions to rewire around |
| `c = c.next;` | Always advance, regardless of whether this node was just deleted or kept |

## 7. Dry Run — `deleteK(head, 2)` on `10 -> 20 -> 100 -> 30`
| c | i | i%2==0? | Action | p after | Result so far |
|---|---|---|---|---|---|
| 10 | 1 | no | `p = 10` | 10 | `10 -> 20 -> 100 -> 30` |
| 20 | 2 | yes | `p(10)≠null → p.next = c.next(100)` → `10.next=100` | 10 | `10 -> 100 -> 30` (20 skipped) |
| 100 *(via c.next, unaffected by the rewire above)* | 3 | no | `p = 100` | 100 | unchanged |
| 30 | 4 | yes | `p(100)≠null → p.next = c.next(null)` → `100.next=null` | 100 | `10 -> 100` (30 skipped) |

Result: `10 -> 100 -> null` — correctly removed the 2nd and 4th nodes.

**`k=1` stress test** — `deleteK(head, 1)` on a fresh `1 -> 2 -> 3`:
```text
c=1,p=null,i=1. 1%1==0 → p==null → head=head.next=2. c=c.next=2.
c=2,p=null(still!),i=2. 2%1==0 → p still null → head=head.next=2.next=3. c=c.next=3.
c=3,p=null,i=3. 3%1==0 → p still null → head=head.next=3.next=null. c=c.next=null.
loop ends. return head=null.
```
Result: **empty list** — every single node correctly deleted when `k=1`, including the tricky case where `p` never gets a chance to become non-null since nothing ever survives to be "kept."

## 8. Test Cases
| Input | k | Output |
|---|---|---|
| `10 -> 20 -> 100 -> 30` | 2 | `10 -> 100` |
| `1 -> 2 -> 3` | 1 | `null` (empty — every node deleted) |
| `1 -> 2 -> 3 -> 4 -> 5 -> 6 -> 7` | 3 | `1 -> 2 -> 4 -> 5 -> 7` (removes positions 3 and 6) |
| `1 -> 2 -> 3` | 10 (k larger than the list) | `1 -> 2 -> 3` (unchanged — no position ever reaches a multiple of 10) |
| `1 -> 2 -> 3` | 0 | `1 -> 2 -> 3` (unchanged — caught by the `k<=0` guard) |
| Empty list, any `k` | any | `null` (caught by the `head==null` guard) |

## Better / Alternative Approach
This is already a correct, optimal single-pass O(n) solution — no algorithmic improvement needed. A recursive version of the same idea is a common alternative some prefer for its conciseness, but it would trade the current O(1) space for O(n) call-stack space, the same trade-off discussed in Problem 10 — worth using this iterative version as the default for exactly that reason.

---

*End of handbook. 18 methods across the ADT itself, reversal, fast/slow-pointer techniques, cycle detection, and structural problems — every one with your original code preserved, intuition, a full dry run, and test cases. The one real, reproducible bug worth fixing first is `addAtTail`'s crash on an empty list (Problem 3) — everything else in this file is either already correct or has only minor stylistic/consistency notes attached.*