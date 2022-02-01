package ca.mcgill.ecse420.a1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DiningPhilosophersNoDeadlock {

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
      if (i == numberOfPhilosophers) {
        rightIndex = leftIndex;
        leftIndex = 0;
      } else {
        rightIndex = i+1;
      }
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
    private int numberEaten = 0;

    public Philosopher(Object rightChopstick, Object leftChopstick) {
      this.rightChopstick = rightChopstick;
      this.leftChopstick = leftChopstick;
    }

		@Override
		public void run() {
      // Keep iterating until we hit a deadlock
      String name = Thread.currentThread().getName();
      for (int i = 0; i < 100; i++) {
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
              Thread.sleep(10);
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
      System.out.println("\n\nPhilosopher " + name.substring(name.length() - 1) + " has eaten " + numberEaten + " times.\n\n");
		}
	}
}