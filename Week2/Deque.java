import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
      
    // Define nodes within the Deque
    private class Node {
        private Item item;
        private Node next;
        private Node prev;
        
        public Node(Item itemArg) {
            item = itemArg;
            next = null;
            prev = null;
        }
    }
    
    private class DequeIterator implements Iterator<Item> {
        // DequeIterator has public access to 
        // Deque private members
        // Internal class is like a friend class in C++
        private Node curNode = first;
        
        public boolean hasNext() {
            return curNode != null;
        }
        
        // Do nothing for remove
        public void remove() { throw new java.lang.UnsupportedOperationException(); }
         
        // This is a non-const iterator because it returns
        // the reference to the content of next
        // Can be modified in place
        public Item next() {
            if (curNode == null) {
                throw new java.util.NoSuchElementException();
            }
            Item itemRef = curNode.item;
            curNode = curNode.next;
            return itemRef;
        }
    }
           
    
    // Basic properties of Deque
    private Node first;
    private Node last;
    private int N;
    
    
    public Deque() { 
        first = null;
        last  = null;
        N     = 0;  
    }
    
    
    // is the deque empty?
    public boolean isEmpty() { return first == null; }   
    
    // return the number of items on the deque    
    public int size() { return N; }
        
    // add the item to the front    
    public void addFirst(Item item) {
        // Make sure input is valid
        if (item == null) {
            throw new java.lang.NullPointerException();
        }
        
        if (isEmpty()) {
            first = new Node(item);
            // first.next and first.prev are both null
            last = first;
        } else {
            Node oldFirst = first;
            first         = new Node(item);
            first.next    = oldFirst;
            oldFirst.prev = first;
        }
        ++N;
    }
        
        
    // add the item to the end    
    public void addLast(Item item) {
        // Make sure input is valid
        if (item == null) {
            throw new java.lang.NullPointerException();
        }
        
        Node oldLast = last;
        // Create a new node, and make it the last one
        last = new Node(item);
        if (isEmpty()) {
            first = last;
        } else {
            oldLast.next = last;
            last.prev    = oldLast;
        }
        ++N;
    }
        
    // remove and return the item from the front    
    public Item removeFirst() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        } else {
            Node oldFirst = first;
            first         = oldFirst.next; // might be null, OK
            if (first == null) {
                last = null;
            } else {
                first.prev = null;
            }
            --N;
            return oldFirst.item;
        }
    }
        
    
    // remove and return the item from the end    
    public Item removeLast() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        } else {
            Node oldLast = last;
            Item lastItem = oldLast.item;
            last = oldLast.prev;
            if (last != null) {
                last.next = null;
            } else {
                first = null;
            }
            --N;
            return lastItem;
        }
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator()  { return new DequeIterator(); }
    
    
    //---- Private methods
    
    private void printQueue() {
        Node curNode = first;
        while (curNode != null) {
            System.out.print(curNode.item + " ");
            curNode = curNode.next;
        }
        System.out.println();
        
    }
        
        
        

    // Unit tests
    public static void main(String[] args) { 
        Deque<Integer> intQueue = new Deque<Integer>();
        Deque<Integer> revQueue = new Deque<Integer>();
        
        int size = 10;
        for (int i = 0; i < size; i++) {
            intQueue.addLast(i);
            revQueue.addFirst(i);
        }
        System.out.println("Deque from addLast():");
        intQueue.printQueue();
        
        System.out.println("Deque from addFirst():");
        revQueue.printQueue();
        
        System.out.println("addLast Deque using iterator:");
        for (int curItem : intQueue) {
            System.out.print(curItem + " ");
        }
        System.out.println();
        
        System.out.println("Now removing first item from addLast() Deque:");
        System.out.println("Got: " + intQueue.removeFirst());
        intQueue.printQueue();
        
        System.out.println("Now removing last item from addFirst() Deque:");
        System.out.println("Got: " + revQueue.removeLast());
        revQueue.printQueue();
        
        System.out.println("Looping over revQue and popping from the end ...");
        while (!revQueue.isEmpty()) {
            System.out.print(revQueue.removeLast() + " ");
        }
        System.out.println();
        
        System.out.println("Looping over intQue and popping from the start ...");
        while (!intQueue.isEmpty()) {
            System.out.print(intQueue.removeFirst() + " ");
        }
        System.out.println();
        
        
    }
    
    
    
}
