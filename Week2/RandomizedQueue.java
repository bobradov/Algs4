import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item>  implements Iterable<Item> {
    
    private Item[] data = (Item[]) new Object[1];
    private int N       = 0; // number of entries, not size of array
    
    //
    //--- Inner class for Iterator
    //
    private class RandomizedQueueIterator implements Iterator<Item> {
        private int[] nonZeroIndexArray;
        private int curIndex = 0;
        
        
        public RandomizedQueueIterator() {
            // Build array of non-zero indices
            nonZeroIndexArray = new int[N];
            int index = 0;
            for (int i = 0; i < data.length; i++) {
                if (data[i] != null) {
                    nonZeroIndexArray[index++] = i;
                }   
            }
            // Now randomize the order using the Knuth shuffle algorithm
            StdRandom.shuffle(nonZeroIndexArray);
        }
        
        public boolean hasNext() {
            return curIndex != N;
        }
        
        // Do nothing for remove
        public void remove() { throw new java.lang.UnsupportedOperationException(); }
         
        // This is a non-const iterator because it returns
        // the reference to the content of next
        // Can be modified in place
        public Item next() {
            if (hasNext()) {
                Item itemRef = data[nonZeroIndexArray[curIndex++]];
                return itemRef;
            } else {
                throw new java.util.NoSuchElementException();
            }
        }
    }
    
    // Constructor (empty randomized queue)
    public RandomizedQueue() {
        // nothing much here
        // private variables already initialized
    }
    
    private String getQueueString() {
        StringBuilder strB = new StringBuilder(6*data.length);
        for (int i = 0; i < data.length; i++) {
            strB.append(data[i] + " ");
        }
        return strB.toString();
    }
    
    private String getQueueStatString() {
        return "N: " + Integer.toString(N) + " Size: " 
                     + Integer.toString(data.length);
    }
        
    
     
    private void resize(int max) {
        // Create a new array with the capacity set to max
        // max may be smaller than N
        // not all entries are copied, only those that are not null
        // may be randomly distributed in array
        Item[] tempData = (Item[]) new Object[max];
        
        int index = 0; // start index of resized array
        
        // loop over old array
        for (int i = 0; i < data.length; i++) {
            if (data[i] != null) {
                tempData[index] = data[i];
                ++index;
            }
        }
        data = tempData; // tempData is garbage-collected
    }
    
    
    
     // is the queue empty?
    public boolean isEmpty() { return N == 0; }
    
    // return the number of items on the queue
    public int size()  { return N; }                      
   
     // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new java.lang.NullPointerException();
        }
        // Check to make sure the current array size has enough capacity
        // for the new item
        //System.out.println("enqueue: N=" + N + " data.length=" + data.length);
        //printQueue();
        
        if (N == data.length) {
            int newSize = 2*data.length;
            if (newSize < 2) newSize = 2;
            resize(newSize);
        }
        data[N++] = item;
    }
            
    // When dequeing, it will be occasionally necessary to shrink the array
    // At that point (length == N/4) an N/2 array will be made, and all
    // non-null entries copied into the first half of the array.
    // The order is actually not important, since only random access is
    // ever used.
    
    // remove and return a random item 
    public Item dequeue() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        // Choose a random index in the populated part of the array
        int randIndex = StdRandom.uniform(N);
        
        // If this happens to be the very last populated index (N-1),
        // then nothing special left to do
        // Otherwise, we delete the "interior" index, replace it with the 
        // last one, then delete the last one
        Item randItem = data[randIndex];
        
        if (randIndex != N-1) {
            data[randIndex] = data[N-1];
        }
        data[N-1] = null;
        --N;
        // Do we need to resize?
        // Otherwise, wasting memory and creating too sparse an array
        if (N == data.length/4) resize(data.length/2);
        return randItem;
    }
    
    
    
    // return (but do not remove) a random item
    public Item sample()   {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        return data[StdRandom.uniform(N)];
    }
    
    public Iterator<Item> iterator()  { return new RandomizedQueueIterator(); }      
    
    public static void main(String[] args)   {
        
        RandomizedQueue<Integer> rqint = new RandomizedQueue<Integer>();
        
        System.out.println("Adding to queue ...");
        for (int i = 0; i < 30; i++) {
            rqint.enqueue(i);
            System.out.println(rqint.getQueueString());
        }
        System.out.println(rqint.getQueueString());
        
        System.out.println("Now printing using iterator:");
        for (int curitem : rqint) {
            System.out.print(curitem + " ");
        }
        System.out.println();
        
        System.out.println("Double iterator loop:");
        for (int oneItem : rqint) {
            for (int twoItem : rqint) {
                System.out.println("Outer: " + oneItem + " Inner: " + twoItem);
            }
        }
        
        System.out.println("Choosing some random entries, not deleting ...");
        for (int i = 0; i < 10; i++) {
            int retval = rqint.sample();
            System.out.print(retval + " ");
            //System.out.println(rqint.getQueueString());
        }
        System.out.println();
        
        System.out.println("Randomly dequeueing ...");
        for (int i = 0; i < 28; i++) {
            int retval = rqint.dequeue();
            //System.out.print(retval + " ");
            System.out.println(rqint.getQueueString());
        }
        System.out.println();
        System.out.println(rqint.getQueueString());
        
        
        // Deque completely, then add 5 more elements 
        System.out.println("Dequeueing completely ...");
        rqint.dequeue();
        rqint.dequeue();
        System.out.println("After emptying, size=" + rqint.size());
        
        for (int i = 0; i < 5; i++) {
            rqint.enqueue(i);
        }
        
        
        System.out.println("After adding, printing using iterator:");
        for (int curitem : rqint) {
            System.out.print(curitem + " ");
        }
        System.out.println();
        
        System.out.println("Now doing autotester ...");
        
        RandomizedQueue<Integer> rq2 = new RandomizedQueue<Integer>();
        
        System.out.println("isEmpty:" + rq2.isEmpty());
        rq2.enqueue(427);
        System.out.println(rq2.dequeue());
        System.out.println("isEmpty:" + rq2.isEmpty());
        System.out.println("size:" + rq2.size());
        System.out.println("Doing one more enqueue:");
        rq2.enqueue(81);
        rq2.enqueue(104);
        rq2.enqueue(11);
        rq2.enqueue(114);
        System.out.println(rq2.getQueueStatString());
        
    }
        
}