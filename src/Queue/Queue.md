# 🚶 Queue — DSA Revision Handbook

> Same format as your Trees, Stack, and LinkedList handbooks: what the problem actually asks → your original code untouched → bugs/edge-cases called out separately → intuition and why you picked this approach → line-by-line walkthrough → a full dry run showing the structure's state at every step → test cases → and, only in a clearly separate section, a better/alternate approach if one exists.

This package has **two independent queue implementations** — worth reading back to back, since they make different trade-offs for the exact same ADT (fixed-capacity array vs. dynamically-growing linked list), and they handle the "pop from empty" case in two completely different ways.

---

## 📇 Progression Index

**`CircularQueue` — Array-Based, Fixed Capacity**
1. Push (Enqueue)
2. Pop (Dequeue) ⚠️ *(ambiguous return value on empty — see note)*
3. Front
4. Rear
5. isEmpty
6. isFull

**`QueueUsingLinkedList` — Linked-List-Based, Dynamic Capacity**
7. Push (Enqueue) ⭐ *(this one's O(1) — worth contrasting against your LinkedList handbook's `addAtTail`, which wasn't)*
8. Pop (Dequeue)
9. Top (Peek Front)

---

## ⚠️ A Note Before You Start: Two Different "Empty" Philosophies

`CircularQueue.pop()` returns `0` when the queue is empty — a plain `int`, indistinguishable from a genuinely stored value of `0`. `QueueUsingLinkedList.pop()` and `.top()` instead `throw new Exception("Queue is empty")`. Neither is "wrong" in isolation, but it's worth being deliberate about which convention you're relying on when you call into either class — silently getting `0` back when you expected real data (because the queue was actually empty) is a classic source of subtle bugs, especially since `0` is also a perfectly valid thing to have pushed.

---

# Problem 1 — Push (Enqueue) — `CircularQueue`

## 1. What is the problem?
Add a new element to the back of a **fixed-capacity** queue backed by a plain array, reusing freed-up space at the front once the array's physical end is reached (that's the "circular" part — the array wraps around instead of needing to shift elements or grow).

## 2. Example
`new CircularQueue(3)`, then `push(1); push(2); push(3);` — filling it to capacity, straight from your `main()`.

## 3. My Code
```java
int[] arr;
int arrSize;
int r = -1;
int f = 0;
int cap;

CircularQueue(int cap){
    arr = new int[cap];
    arrSize = 0;
    this.cap = cap;
}

private void push(int data) {
    if(arrSize == cap) {
        return;
    }
    r = (r+1)%cap;
    arr[r] = data;
    arrSize++;
}
```

## 4. Issues / Bugs / Edge Cases
**Silent failure on a full queue, worth knowing about:** if `arrSize == cap`, `push` just `return`s — no exception, no boolean success flag, nothing to tell the caller the data was dropped. If you ever call `push` in a loop without separately checking `isFull()` first, you could lose data with zero indication anything went wrong.

## 5. Intuition & Why the Modulo Trick
A plain array queue has an obvious problem: once you've popped a few elements from the front, that space at the beginning is wasted — you'd eventually run out of room at the *end* even with free space sitting unused at the *start*. The fix: treat the array as **circular** — when the rear pointer `r` reaches the last index, wrapping it back to `0` via `r = (r+1) % cap` lets you reuse that freed space instead of needing to shift every element down or allocate a bigger array.

**Why `arrSize` exists as a separate counter, rather than just comparing `f` and `r`:** in a circular buffer, `f == r` is genuinely ambiguous on its own — it could mean the queue is completely empty, *or* completely full (both states can leave the two pointers pointing at the same index, depending on the exact sequence of pushes/pops). Tracking `arrSize` explicitly sidesteps that ambiguity entirely — full and empty are just `arrSize == cap` and `arrSize == 0`, unambiguous either way.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `if (arrSize == cap) return;` | Already full — refuse the push (silently, see bug note above) |
| `r = (r+1) % cap;` | Advance the rear pointer, **wrapping around** to `0` once it passes the last index |
| `arr[r] = data;` | Write the new value into the now-current rear slot |
| `arrSize++;` | Keep the unambiguous size counter in sync |

## 7. Dry Run — `push(1); push(2); push(3);` on `new CircularQueue(3)`
| Call | `r` before | `r` after (`(r+1)%3`) | `arr` after | `arrSize` after |
|---|---|---|---|---|
| `push(1)` | -1 | 0 | `[1, _, _]` | 1 |
| `push(2)` | 0 | 1 | `[1, 2, _]` | 2 |
| `push(3)` | 1 | 2 | `[1, 2, 3]` | 3 (now full) |

## 8. Test Cases
| Call | On queue | Result |
|---|---|---|
| `push(1)` on empty `CircularQueue(3)` | `arr=[1,_,_]`, `arrSize=1` |
| `push(1); push(2); push(3);` | fills to capacity, `arrSize=3` |
| `push(4)` on an already-full `CircularQueue(3)` | **silently does nothing** — `4` is dropped, no error |
| `push` after a `pop` has freed a slot | correctly reuses the freed slot via the modulo wraparound (see Problem 2's dry run) |

## Better / Alternative Approach
The core circular-buffer technique is already the standard, optimal O(1) approach for a fixed-capacity queue — nothing to improve there. The one worthwhile change: make a full queue **observable** to the caller, either by returning a `boolean` (`true` if the push succeeded, `false` if the queue was full — this is exactly what LeetCode 622 "Design Circular Queue" expects) or by throwing an exception, matching the style your `QueueUsingLinkedList` already uses for its own empty-queue case.

---

# Problem 2 — Pop (Dequeue) — `CircularQueue` ⚠️

## 1. What is the problem?
Remove and return the element at the front of the queue, freeing its slot for future reuse.

## 2. Example
`pop()` on the queue built in Problem 1 (`[1, 2, 3]`, full).

## 3. My Code
```java
private int pop() {
    if(arrSize == 0) {
        return 0;
    }
    int k = arr[f];
    f = (f+1)%cap;
    arrSize--;
    return k;
}
```

## 4. Issues / Bugs / Edge Cases
**The empty-queue return value is genuinely ambiguous.** Popping an empty queue returns `0` — but `0` is also a completely valid value someone could have legitimately pushed. There's no way for a caller to distinguish "the queue was empty" from "the queue had a `0` in it." Contrast this directly with `Front()`/`Rear()` in this same class (Problems 3–4), which use `-1` as their empty-queue sentinel instead — `pop()` doesn't follow that same convention, which is worth noticing as an inconsistency within the class itself. It's also a different philosophy entirely from `QueueUsingLinkedList.pop()` (Problem 8), which throws a checked `Exception` rather than returning any sentinel value at all.

## 5. Intuition & Why This Approach
Same core idea as `push`, mirrored: read the value sitting at the front index `f`, then advance `f` forward with the same wraparound (`(f+1) % cap`) so the next pop reads the correct next element — including correctly wrapping back to index `0` if the front pointer walks off the physical end of the array.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `if (arrSize == 0) return 0;` | Empty-queue guard — but see the bug note above about the return value itself |
| `int k = arr[f];` | Read the value at the current front position *before* moving anything |
| `f = (f+1) % cap;` | Advance the front pointer, wrapping around exactly like `push` does for the rear |
| `arrSize--;` | Keep the size counter accurate |

## 7. Dry Run — continuing from Problem 1's full queue `[1, 2, 3]` (`f=0`, `r=2`, `arrSize=3`), then `pop()` followed by `push(4)` (exactly your `main()`'s sequence)
```text
pop(): arrSize(3) ≠ 0.
  k = arr[f=0] = 1
  f = (0+1) % 3 = 1
  arrSize-- → 2
  return 1
```
Printed: `1` — matches your `main()`'s actual output.

**Then `push(4)`** — this is where the wraparound reuse actually becomes visible:
```text
push(4): arrSize(2) ≠ cap(3) → proceed
  r = (2+1) % 3 = 0   ← wraps back to index 0, which pop() just freed!
  arr[0] = 4           ← overwrites the old '1', which is fine, it's already been popped
  arrSize++ → 3
```
Final state: `arr = [4, 2, 3]`, `f=1`, `r=0`, `arrSize=3` (full again). The queue logically holds `2, 3, 4` in that order — `Front()` correctly reads `arr[f=1]=2`, `Rear()` correctly reads `arr[r=0]=4`, even though physically the "4" sits at the very start of the underlying array. This is the entire point of the circular design: index `0` got reused immediately without needing to shift `2` and `3` down.

## 8. Test Cases
| Call | On queue | Result |
|---|---|---|
| `pop()` on `[1,2,3]` (full, cap 3) | returns `1`, queue becomes logically `[2,3]` |
| `pop()` on an empty queue | **returns `0`** — ambiguous, see bug above |
| `pop()` then immediately `push()` | correctly reuses the freed slot via wraparound (traced above) |
| Popping all elements one by one | `arrSize` correctly reaches `0`, `isEmpty()` becomes `true` |

## Better / Alternative Approach
Fix the ambiguous return value — either match `Front()`/`Rear()`'s own convention:
```java
private int pop() {
    if (arrSize == 0) { return -1; }
    ...
}
```
or, more robustly (and consistent with `QueueUsingLinkedList`'s style), throw an exception instead of returning any sentinel at all:
```java
private int pop() {
    if (arrSize == 0) { throw new RuntimeException("Queue is empty"); }
    ...
}
```
Either fix is a one-line change; the throwing version is generally preferred since it makes the empty case impossible to silently ignore.

---

# Problem 3 — Front — `CircularQueue`

## 1. What is the problem?
Peek at the element currently at the front of the queue, without removing it.

## 2. Example
`Front()` on `[1, 2, 3]` (from Problem 1's build).

## 3. My Code
```java
public int Front() {
    if(isEmpty()){
        return -1;
    }
    return arr[f];
}
```

## 4. Issues / Bugs / Edge Cases
None — and this is exactly the sentinel convention `pop()` (Problem 2) should arguably be following too, for consistency within the same class.

## 5. Intuition & Why This Approach
The front index `f` always points at the oldest still-present element — that invariant is maintained entirely by `push`/`pop`'s own bookkeeping, so `Front()` itself just needs a direct array read, guarded by an empty check.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `if (isEmpty()) return -1;` | Unambiguous empty-queue signal — `-1` can never be confused with a real index-based read here since it's returned *before* touching the array at all |
| `return arr[f];` | Direct read — no computation needed, `f` is always already correct |

## 7. Dry Run — `Front()` on `[1, 2, 3]`, `f=0`
```text
isEmpty()? arrSize(3)==0? no.
return arr[0] = 1
```
Result: **1**

## 8. Test Cases
| Call | On queue | Result |
|---|---|---|
| `Front()` on `[1,2,3]` | 1 |
| `Front()` on an empty queue | -1 |
| `Front()` right after a `pop()` | reflects the new front correctly (see Problem 2's dry run — becomes `2` after popping `1`) |
| `Front()` called twice in a row (no mutation between) | same value both times — it's a pure read |

## Better / Alternative Approach
Already the standard, optimal O(1) solution. Nothing to improve.

---

# Problem 4 — Rear — `CircularQueue`

## 1. What is the problem?
Peek at the element currently at the back of the queue, without removing it.

## 2. Example
`Rear()` on `[1, 2, 3]`.

## 3. My Code
```java
public int Rear() {
    if(isEmpty()){
        return -1;
    }
    return arr[r];
}
```

## 4. Issues / Bugs / Edge Cases
None.

## 5. Intuition & Why This Approach
Exact mirror of `Front()` — the rear index `r` always points at the most recently pushed element, maintained by `push`'s bookkeeping, so this is again just a guarded direct read.

## 6. Line-by-Line Walkthrough
Identical shape to Problem 3, reading `arr[r]` instead of `arr[f]`.

## 7. Dry Run — `Rear()` on `[1, 2, 3]`, `r=2`
```text
isEmpty()? no.
return arr[2] = 3
```
Result: **3**

## 8. Test Cases
| Call | On queue | Result |
|---|---|---|
| `Rear()` on `[1,2,3]` | 3 |
| `Rear()` on an empty queue | -1 |
| `Rear()` right after a `push(4)` (following Problem 2's wraparound example) | 4 |
| `Rear()` on a queue with exactly one element | that element (front and rear are the same) |

## Better / Alternative Approach
Already the standard, optimal O(1) solution.

---

# Problem 5 — isEmpty — `CircularQueue`

## 1. What is the problem?
Report whether the queue currently holds zero elements.

## 2. Example
`isEmpty()` on a freshly-constructed `CircularQueue(3)`, versus on `[1,2,3]`.

## 3. My Code
```java
public boolean isEmpty() {
    if(arrSize == 0){
        return true;
    }
    return false;
}
```

## 4. Issues / Bugs / Edge Cases
None functionally. Purely stylistic: `if (x) return true; return false;` is equivalent to `return x;` — the explicit `if` is unnecessary but harmless.

## 5. Intuition & Why This Approach
This is exactly why `arrSize` exists as a separate tracked field rather than being derived from `f` and `r` on the fly (see Problem 1's note on the `f==r` ambiguity) — `isEmpty` becomes a trivial, unambiguous O(1) check.

## 6. Line-by-Line Walkthrough
Self-explanatory — direct comparison against `0`.

## 7. Dry Run
```text
isEmpty() on fresh CircularQueue(3): arrSize=0 → true
isEmpty() on [1,2,3]: arrSize=3 → false
```

## 8. Test Cases
| State | Result |
|---|---|
| Freshly constructed | `true` |
| After one `push` | `false` |
| After pushing then popping that same one element | `true` again |
| Full queue | `false` |

## Better / Alternative Approach
Simplify to `return arrSize == 0;` — purely a style improvement, identical behavior.

---

# Problem 6 — isFull — `CircularQueue`

## 1. What is the problem?
Report whether the queue is at capacity (any further `push` would be rejected).

## 2. Example
`isFull()` on `[1,2,3]` (cap 3) versus a partially-filled queue.

## 3. My Code
```java
public boolean isFull() {
    if(arrSize == cap){
        return true;
    }
    return false;
}
```

## 4. Issues / Bugs / Edge Cases
None functionally — same minor style note as Problem 5 (`if/return true/return false` could just be `return arrSize == cap;`).

## 5. Intuition & Why This Approach
Direct mirror of `isEmpty()`, checking the opposite boundary. This is the check worth calling *before* every `push` if you want to avoid Problem 1's silent-drop-on-full behavior.

## 6. Line-by-Line Walkthrough
Self-explanatory — direct comparison against `cap`.

## 7. Dry Run
```text
isFull() on [1,2,3], cap=3: arrSize(3)==cap(3) → true
isFull() on [1,2,_], cap=3: arrSize(2)==cap(3)? no → false
```

## 8. Test Cases
| State | Result |
|---|---|
| Freshly constructed | `false` |
| Filled to capacity | `true` |
| Full, then one `pop()` | `false` again |
| `CircularQueue(0)` (zero capacity) constructed | `true` immediately — `arrSize(0)==cap(0)`, meaning `push` would never succeed on this queue at all |

## Better / Alternative Approach
Simplify to `return arrSize == cap;` — style only, same behavior.

---

# Problem 7 — Push (Enqueue) — `QueueUsingLinkedList` ⭐

## 1. What is the problem?
Add a new element to the back of a queue backed by a singly linked list, with no fixed capacity limit.

## 2. Example
`push(7); push(2); push(3); push(5);` — straight from your `main()`.

## 3. My Code
```java
public Node start;
public Node end;
int size;

class Node{
    int val;
    Node next;
    Node(int val){ this.val = val; }
}

QueueUsingLinkedList(){
    start = null;
    end = null;
    size = 0;
}

private void push(int num) {
    if(size<0) {
        System.out.println("not valid");
        return;
    }
    Node n = new Node(num);
    if(size == 0) {
        start = n;
        end = n;
        size++;
    }else {
        end.next = n;
        end = n;
        size++;
    }
}
```

## 4. Issues / Bugs / Edge Cases
**The `if (size < 0)` guard is dead code.** Given how `push` and `pop` (Problem 8) maintain `size` — it starts at `0` and only ever changes via `size++` here or `size--` in `pop` (which itself only decrements when `start != null`, meaning `size` was already positive) — `size` can never actually become negative through any normal sequence of calls on this class. This check can never trigger; it's a harmless leftover, not a functional bug.

## 5. Intuition & Why This Is Genuinely Better Than Your LinkedList Handbook's `addAtTail`
This is worth pausing on, since it's a direct, concrete payoff of a lesson from your LinkedList handbook: `addAtTail` there had to **walk the entire list every single call** to find the current last node, because that class only tracked a `head` pointer. Here, `end` is maintained as its own dedicated field, updated on every push — so appending is a straight O(1) pointer rewire (`end.next = n; end = n;`), never a full traversal. This is exactly the "maintain a `tail` pointer" fix that was suggested as the performance improvement for `addAtTail` — and it's already correctly implemented here.

**Why `size == 0` is handled as its own special case:** when the list is empty, there's no existing `end.next` to safely write to (`end` is `null`), so `start` and `end` both have to be pointed at the brand-new node directly, rather than trying to link off of a nonexistent tail.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `if (size == 0) { start = n; end = n; }` | Special case — no existing tail to link from, so the new node becomes both ends of the list |
| `else { end.next = n; end = n; }` | Normal case — attach after the current tail, then that new node becomes the tail |
| `size++;` (both branches) | Bookkeeping |

## 7. Dry Run — `push(7); push(2); push(3); push(5);` on a fresh queue
| Call | `start` before | Action | `start` after | `end` after | `size` after |
|---|---|---|---|---|---|
| `push(7)` | `null` | `size==0` branch: `start=end=Node(7)` | `7` | `7` | 1 |
| `push(2)` | `7` | `end.next=2; end=2` | `7` | `2` | 2 |
| `push(3)` | `7` | `end.next=3; end=3` | `7` | `3` | 3 |
| `push(5)` | `7` | `end.next=5; end=5` | `7` | `5` | 4 |

Final list: `7 -> 2 -> 3 -> 5 -> null`, `start=7`, `end=5`, `size=4`.

## 8. Test Cases
| Call | On queue | Result |
|---|---|---|
| `push(7)` on empty queue | `start=end=Node(7)`, `size=1` |
| `push(2)` on `[7]` | `7 -> 2`, `end` now points at `2`, `size=2` |
| Four pushes in a row (as in `main()`) | `7 -> 2 -> 3 -> 5`, `size=4` |
| `push` on a queue with `size < 0` (unreachable in practice) | prints `"not valid"`, does nothing — see bug note above |

## Better / Alternative Approach
This is already the optimal O(1) push — no algorithmic improvement needed. The only cleanup worth making: delete the dead `if (size < 0)` check, since it can never fire and just adds a read without purpose.

---

# Problem 8 — Pop (Dequeue) — `QueueUsingLinkedList`

## 1. What is the problem?
Remove and return the element at the front of the queue.

## 2. Example
`pop()` on `7 -> 2 -> 3 -> 5` (from Problem 7).

## 3. My Code
```java
private int pop() throws Exception {
    if(start == null) {
        throw new Exception("Queue is empty");
    }
    int k = start.val;
    start = start.next;
    if(start == null) {
        end = null;
    }
    size--;
    return k;
}
```

## 4. Issues / Bugs / Edge Cases
None — this correctly handles the "queue just became empty" case (see intuition below), which is easy to get wrong in a head/tail-tracked linked queue.

## 5. Intuition & Why the `if (start == null) end = null;` Line Matters
The straightforward part: read the front value, then advance `start` to the next node — that's a normal linked-list "remove the head" operation. **The subtlety:** if that was the *last* node (`start` is now `null` after advancing), the `end` field would otherwise be left as a **stale dangling reference** to the node that was just removed — nothing else in `pop()` or `push()`'s `size==0` branch would clean it up on its own before the next push (though as it happens, `push`'s own `size==0` branch would correctly overwrite `end` on the *next* call regardless, since it doesn't reference the old `end` at all). The real reason this matters: both `start` and `end` are declared **`public`** — any external code reading `q.end` directly, at the moment right after the queue empties, would see a dangling reference to removed data instead of `null` without this line. It's a small piece of defensive consistency specifically because the internal state is exposed.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `if (start == null) throw new Exception(...);` | Empty-queue guard — this class chooses to throw rather than return a sentinel (contrast with `CircularQueue.pop()`, Problem 2) |
| `int k = start.val;` | Capture the value before advancing |
| `start = start.next;` | Standard "remove the head" |
| `if (start == null) end = null;` | Keep the publicly-visible `end` field honest once the queue is fully empty |
| `size--;` | Bookkeeping |

## 7. Dry Run — the full sequence from your `main()`: `pop(); top(); pop(); pop(); top();` on `7 -> 2 -> 3 -> 5`
```text
pop(): start=7≠null. k=7. start=start.next=2. start≠null→end unchanged(5). size=3. return 7.
```
Printed: `7`

```text
top(): start=2≠null. k=2. return 2.  (see Problem 9 — doesn't mutate anything)
```
Printed: `2`

```text
pop(): start=2≠null. k=2. start=start.next=3. start≠null→end unchanged(5). size=2. return 2.
```
Printed: `2`

```text
pop(): start=3≠null. k=3. start=start.next=5. start≠null→end unchanged(5). size=1. return 3.
```
Printed: `3`

```text
top(): start=5≠null. k=5. return 5.
```
Printed: `5`

Full output sequence: `7, 2, 2, 3, 5` — matches your `main()`'s actual printed output exactly, and correctly demonstrates FIFO order (first pushed, `7`, is the first popped).

## 8. Test Cases
| Call | On queue | Result |
|---|---|---|
| `pop()` on `7->2->3->5` | returns `7`, queue becomes `2->3->5` |
| `pop()` on a single-element queue `[9]` | returns `9`, `start` and `end` both become `null` (the reset line fires) |
| `pop()` on an empty queue | throws `Exception("Queue is empty")` |
| Popping all four elements from `main()`'s queue in sequence | `7, 2, 3, 5`, then a fifth `pop()` would throw |

## Better / Alternative Approach
Already the standard, optimal O(1) dequeue — correct and complete. Nothing to improve.

---

# Problem 9 — Top (Peek Front) — `QueueUsingLinkedList`

## 1. What is the problem?
Look at the front element without removing it.

## 2. Example
`top()` on `2 -> 3 -> 5` (after Problem 8's first pop).

## 3. My Code
```java
private int top() throws Exception {
    if(start == null) {
        throw new Exception("Queue is empty");
    }
    int k = start.val;
    return k;
}
```

## 4. Issues / Bugs / Edge Cases
None.

## 5. Intuition & Why This Approach
The simplest method in the whole file — `start` always already points at the front element (that invariant is maintained by `push`/`pop`), so this is just a guarded read with no mutation at all.

## 6. Line-by-Line Walkthrough
| Line | What happens |
|---|---|
| `if (start == null) throw ...;` | Same empty-queue guard as `pop()`, for consistency |
| `return start.val;` (via `k`) | Direct read, nothing else touched |

## 7. Dry Run — `top()` on `2 -> 3 -> 5`
```text
start=2≠null. k=start.val=2. return 2.
```
Result: **2** — and calling `top()` again immediately would return the same `2`, since nothing was mutated.

## 8. Test Cases
| Call | On queue | Result |
|---|---|---|
| `top()` on `2->3->5` | 2 |
| `top()` twice in a row, no `pop()` between | same value both times |
| `top()` on an empty queue | throws `Exception("Queue is empty")` |
| `top()` immediately after the queue's last element was just popped | throws (since `start` is now `null`) |

## Better / Alternative Approach
Already the standard, optimal O(1) peek. Nothing to improve. The only mild inconsistency worth knowing about (not a bug): `push` (Problem 7) handles its own invalid case by printing a message and returning silently, while `top()` and `pop()` both throw a checked `Exception` — three methods in the same small class, two different error-handling philosophies. Worth picking one convention if you revisit this class.

---

## 📎 Quick Reference

| # | Method | Class | Notes |
|---|---|---|---|
| 1 | push | CircularQueue | O(1), silently drops data when full |
| 2 | pop | CircularQueue | O(1), ⚠️ returns `0` (ambiguous) when empty |
| 3 | Front | CircularQueue | O(1), correctly returns `-1` when empty |
| 4 | Rear | CircularQueue | O(1), correctly returns `-1` when empty |
| 5 | isEmpty | CircularQueue | O(1) |
| 6 | isFull | CircularQueue | O(1) |
| 7 | push | QueueUsingLinkedList | O(1) — maintains a tail pointer, unlike your LinkedList handbook's `addAtTail` |
| 8 | pop | QueueUsingLinkedList | O(1), throws on empty |
| 9 | top | QueueUsingLinkedList | O(1), throws on empty |

---

*End of handbook. 9 methods across two independent queue implementations, every one with your original code preserved, intuition, a full dry run, and test cases. The one thing worth fixing first if you revisit this package: `CircularQueue.pop()`'s ambiguous `0`-on-empty return (Problem 2) — a one-line change to match `Front()`/`Rear()`'s own `-1` convention, or better, to throw like `QueueUsingLinkedList` already does.*