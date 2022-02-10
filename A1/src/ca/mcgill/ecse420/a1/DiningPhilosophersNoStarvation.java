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

    // Function attempting to grab the chopstick
    // In proper terms to lock the ressource
    public boolean grabChopstick(){
      return lock.tryLock();
    }

    // Function attempting to drop the chopstick
    // In proper terms to unlock the ressource
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
          if (leftChopstick.grabChopstick()) {
            System.out.println(name + " has the left chopstick and is waiting for the right");
            think_wait_or_eat();

            // Lock the Philosopher's right chopstick
            // If the chopstick is already locked they must wait for it to be available
            if (rightChopstick.grabChopstick()) {
              System.out.println(name + " has left and right chopsticks and is eating");
              think_wait_or_eat();
              numberEaten++;
              rightChopstick.dropChopstick();
            }
            // Release the right chopstick
            System.out.println(name + " has released the left chopstick");
            leftChopstick.dropChopstick();
            think_wait_or_eat();
          }
          think_wait_or_eat();
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
