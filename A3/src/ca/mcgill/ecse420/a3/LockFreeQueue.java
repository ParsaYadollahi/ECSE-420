package ca.mcgill.ecse420.a3;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;

// bounded lock-free blocking queue
public class LockFreeQueue<T> {

    public AtomicReferenceArray<T> queue;
    public AtomicInteger head, tail, size;
    public int capacity;

    public LockFreeQueue(int maxSize) {
        this.capacity = maxSize;
        this.queue = new AtomicReferenceArray<>(maxSize);
        this.head = new AtomicInteger(0);
        this.tail = new AtomicInteger(0);
        this.size = new AtomicInteger(0);
    }

    // add object to the end of the queue
    public void enqueue(T value) {
        int size = this.size.get();

        // ensure queue is not full when we enqueue, otherwise wait until not full
        while (size == this.capacity || !this.size.compareAndSet(size, size + 1)) {
            size = this.size.get();
        }

        this.queue.set(this.tail.getAndIncrement(), value);

        // if we reach capacity set tail to 0
        if (this.tail.get() == this.capacity) {
            this.tail.set(0);
        }
    }

    // remove and return the head of the queue
    public T dequeue() {
        int size = this.size.get();

        // ensure queue is not empty when we dequeue, otherwise wait until not empty
        while (size == 0 || !this.size.compareAndSet(size, size - 1)) {
            size = this.size.get();
        }
        T value = this.queue.getAndSet(this.head.getAndIncrement(), null);

        // set head to beginning of queue if we reach capacity
        if (this.head.get() == this.capacity) {
            this.head.set(0);
        }

        return value;
    }
}