package ca.mcgill.ecse420.a3;

public class TestQueue {

    public static void main(String[] args) {
        LockBasedQueue<Integer> lbq = new LockBasedQueue<>(4);
        lbq.enqueue(1);
        lbq.enqueue(2);
        lbq.enqueue(3);
        lbq.enqueue(4);

        System.out.println(lbq.queue[lbq.head]); // Should output 1
        System.out.println(lbq.queue[lbq.tail]); // Should output 1

        lbq.dequeue();
        lbq.enqueue(5);
        lbq.dequeue();
        lbq.enqueue(6);

        System.out.println(lbq.queue[lbq.head]); // Should output 3
        System.out.println(lbq.queue[lbq.tail]); // Should output 3

        LockFreeQueue<Integer> lfq = new LockFreeQueue<>(4);
        lfq.enqueue(1);
        lfq.enqueue(2);
        lfq.enqueue(3);
        lfq.enqueue(4);

        System.out.println(lfq.queue.get(lfq.head.get())); // Should output 1
        System.out.println(lfq.queue.get(lfq.tail.get())); // Should output 1

        lfq.dequeue();
        lfq.enqueue(5);
        lfq.dequeue();
        lfq.enqueue(6);

        System.out.println(lfq.queue.get(lfq.head.get())); // Should output 3
        System.out.println(lfq.queue.get(lfq.tail.get())); // Should output 3
    }
}