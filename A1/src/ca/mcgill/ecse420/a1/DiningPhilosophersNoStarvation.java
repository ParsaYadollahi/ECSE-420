package ca.mcgill.ecse420.a1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosophersNoStarvation {

	public static void main(String[] args) {

		int numberOfPhilosophers = 5;
    Philosopher[] philosophers = new Philosopher[numberOfPhilosophers];
    Chopstick[] chopsticks = new Chopstick[numberOfPhilosophers];
    ExecutorService executorService = Executors.newFixedThreadPool(numberOfPhilosophers);
    int leftIndex;
    int rightIndex;


    // Initialize shared objects (chopsticks)
    for (int i = 0; i < numberOfPhilosophers; i++) {
      chopsticks[i] = new Chopstick();
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

  public static class Chopstick {
    private ReentrantLock lock = new ReentrantLock(true);

    public Chopstick() {}

    public boolean grabChopstick(){
      return lock.tryLock();

    }

    public void dropChopstick(){
      lock.unlock();
    }
  }

	public static class Philosopher implements Runnable {
    private final Chopstick rightChopstick;
    private final Chopstick leftChopstick;
    private int numberEaten = 0;

    public Philosopher(Chopstick rightChopstick, Chopstick leftChopstick) {
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
          if (leftChopstick.grabChopstick()) {
            System.out.println(name + " has the left chopstick and is waiting for the right");
            Thread.sleep(10);

            // Lock the Philosopher's right chopstick
            // If the chopstick is already locked they must wait for it to be available
            if (rightChopstick.grabChopstick()) {
              System.out.println(name + " has left and right chopsticks and is eating");
              Thread.sleep(10);
              numberEaten++;
              rightChopstick.dropChopstick();
            }
            // Release the right chopstick
            System.out.println(name + " has released the left chopstick");
            leftChopstick.dropChopstick();
          }
          Thread.sleep((long) (Math.random() * 10));
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
