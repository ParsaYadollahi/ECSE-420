package ca.mcgill.ecse420.a3;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

// Bounded lock-based blocking queue
public class LockBasedQueue<T> {

    public ReentrantLock enqLock, deqLock;    // Locks for enqueue and dequeue
    public Condition notEmptyCondition, notFullCondition;   // Conditions whether queue is empty or not
    public AtomicInteger size;                          // Amount of used slots in queue
    public int head;                        // Head entry
    public int tail;                       // Tail entry
    public int capacity;                  // Max capacity in queue
    public Object[] queue;                // Array version of linked list

    public LockBasedQueue(int capacity) {
        this.queue = new Object[capacity];
        this.head = 0;
        this.tail = this.head;
        this.capacity = capacity;
        this.size = new AtomicInteger(0);
        this.enqLock = new ReentrantLock();
        this.deqLock = new ReentrantLock();
        this.notFullCondition = enqLock.newCondition();
        this.notEmptyCondition = deqLock.newCondition();
    }

    // Add object to the end of the queue
    public void enqueue(T value) {
        if (value == null) {
            throw new NullPointerException();
        }

        // boolean to track if queue becomes empty
        boolean isQueueEmpty = false;

        enqLock.lock();

        try {
            // ensure queue is not full when enqueuing, otherwise wait until not full
            while (this.size.get() == this.capacity) {
                try {
                    this.notFullCondition.await();
                } catch (InterruptedException e) {}
            }

            add(value);

            // verify if queue is empty to prevent false dequeue later
            if (this.size.getAndIncrement() == 0) {
                isQueueEmpty = true;
            }
        } finally {
            this.enqLock.unlock();
        }

        // set the dequeue locks to prevent a dequeue in an empty queue
        if (isQueueEmpty) {
            this.deqLock.lock();
            try {
                this.notEmptyCondition.signalAll();
            } finally {
                this.deqLock.unlock();
            }
        }
    }

    // Remove and return the head of the queue
    public T dequeue() {
        T value;

        // boolean to track if queue becomes full
        boolean isQueueFull = false;

        this.deqLock.lock();

        try {
            // ensure queue is not empty when dequeuing, otherwise wait until not empty
            while (this.size.get() == 0) {
                try {
                    this.notEmptyCondition.await();
                } catch (InterruptedException e) {}
            }

            value = remove();

            // verify if queue is full to prevent false enqueue later
            if (this.size.getAndDecrement() == this.capacity) {
                isQueueFull = true;
            }
        } finally {
            this.deqLock.unlock();
        }

        // set the enqueue locks to prevent a enqueue in a full queue
        if (isQueueFull) {
            this.enqLock.lock();
            try {
                this.notFullCondition.signalAll();
            } finally {
                this.enqLock.unlock();
            }
        }
        return value;
    }

    // add element to tail of queue
    public void add(T element) {
        final Object[] items = this.queue;
        items[this.tail] = element;
        // set tail to first element if reach capacity
        if (++this.tail == items.length) {
            this.tail = 0;
        }
    }

    // remove element in the head of the queue
    public T remove() {
        final Object[] items = this.queue;
        T element = (T) items[this.head];
        items[this.head] = null;
        // set head to first element if reach capacity
        if (++this.head == items.length)
            this.head = 0;
        return element;
    }
}