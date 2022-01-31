package ca.mcgill.ecse420.a1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DiningPhilosophers {

	public static void main(String[] args) {

		int numberOfPhilosophers = 5;
    Philosopher[] philosophers = new Philosopher[numberOfPhilosophers];
    Object[] chopsticks = new Object[numberOfPhilosophers];
    ExecutorService executorService = Executors.newFixedThreadPool(numberOfPhilosophers);
    int leftIndex;
    int rightIndex;


    // Initialize shared objects (chopsticks)
    for (int i = 0; i < numberOfPhilosophers; i++) {
      chopsticks[i] = new Object();
    }

    // Initialize the threads (Philosopher) and execute the threads
    for (int i = 0; i < numberOfPhilosophers; i++) {
      leftIndex = i;
      rightIndex = (i == numberOfPhilosophers - 1) ? 0 : i+1;
      philosophers[i] = new Philosopher(chopsticks[leftIndex], chopsticks[rightIndex]);

      executorService.execute(philosophers[i]);
      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    executorService.shutdown();
	}

	public static class Philosopher implements Runnable {
    private final Object rightChopstick;
    private final Object leftChopstick;

    public Philosopher(Object rightChopstick, Object leftChopstick) {
      this.rightChopstick = rightChopstick;
      this.leftChopstick = leftChopstick;
    }

		@Override
		public void run() {
      // Keep iterating until we hit a deadlock
      while (true) {
        String name = Thread.currentThread().getName();
        try {
          // Lock the Philosopher's left chopstick
          // If the chopstick is already locked they must wait for it to be available
          synchronized(leftChopstick) {
            System.out.println(name + " has the left left chopstick and is waiting for the right");
            Thread.sleep(10);

            // Lock the Philosopher's right chopstick
            // If the chopstick is already locked they must wait for it to be available
            synchronized(rightChopstick) {
              System.out.println(name + " has left and right chopsticks and is eating");
            }
            // Release the right chopstick
            System.out.println(name + " has released the left chopstick");
          }
          // Release the left chopstick
          System.out.println(name + " has released the right chopstick");
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
		}
	}
}
