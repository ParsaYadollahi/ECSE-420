package ca.mcgill.ecse420.a2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.Executors;


/**
 * @author Parsa Yadollahi
 * @author Nicholas Nikas
 */


public class TestLock {
  private static final int NUMBER_THREADS = 5;
  private static int counter = 0;

  private static Filterlock filterLock = new FilterLock(NUMBER_THREADS);
  private static Bakerylock bakeryLock = new Bakerylock(NUMBER_THREADS);

  public static void main(String[] args) {
    ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_THREADS);

    for (int i = 0; i < NUMBER_THREADS; i++) {
      executorService.execute(new LockRunnable(null));
    }

    executorService.shutdown();
    try {
      executorService.awaitTermination(10, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    System.out.println("No Lock: " + counter);

  }


  public class LockRunnable implements Runnable {
    public LockRunnable(Object customLock) {
      this.customLock = customLock;
    }

    @Override
    public void run() {
      try {
        if (customLock != null) {
          customLock.lock();
        }
        if (counter < 5) {
          Thread.sleep(10);
          counter ++;
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        if (customLock != null) {
          customLock.unlock();
        }
      }
    }
  }
}
