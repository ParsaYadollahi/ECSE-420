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
      // Force the last philosopher to pick up his fist chopstick
      //  with the right hand.
      // This removes the circular wait which prevents
      // the deadlock from occuring
      if (i == numberOfPhilosophers - 1) {
        leftIndex = 0;
        rightIndex = i;
      } else {
        leftIndex = i;
        rightIndex = (i + 1) % numberOfPhilosophers;
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

    private static void think_wait_or_eat() throws InterruptedException{
      Thread.sleep(10);
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
            think_wait_or_eat();

            // Lock the Philosopher's right chopstick
            // If the chopstick is already locked they must wait for it to be available
            synchronized(rightChopstick) {
              System.out.println(name + " has left and right chopsticks and is eating");
              numberEaten ++;
              think_wait_or_eat();
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
      // Display number of times each philospher has eaten
      System.out.println("\n\nPhilosopher " + name.substring(name.length() - 1) + " has eaten " + numberEaten + " times.\n\n");
		}
	}
}
