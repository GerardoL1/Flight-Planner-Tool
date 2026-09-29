import java.util.LinkedList;

/**
 * A minimal last-in, first-out stack of {@link PathState} objects, backed by a linked list.
 */
class Stack {

    /** Backing list; the front of the list is the top of the stack. */
    LinkedList<PathState> list = new LinkedList<>();

    /**
     * Pushes an item onto the top of the stack.
     *
     * @param item the item to push
     */
    public void push(PathState item) {
        list.addFirst(item);
    }

    /**
     * Removes and returns the item on top of the stack.
     *
     * @return the top item
     * @throws java.util.NoSuchElementException if the stack is empty
     */
    public PathState pop() {
        return list.removeFirst();
    }

    /**
     * Returns the item on top of the stack without removing it.
     *
     * @return the top item, or {@code null} if the stack is empty
     */
    public PathState peek() {
        return list.peekFirst();
    }

    /**
     * Tells whether the stack has no items.
     *
     * @return {@code true} if the stack is empty
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }
}