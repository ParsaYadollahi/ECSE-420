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
  private static final int NUMBER_THREADS = 20;
  private static int counter = 0;


  private static FilterLock filterLock = new FilterLock(NUMBER_THREADS);
  private static BakeryLock bakeryLock = new BakeryLock(NUMBER_THREADS);
  // private static ThirdLock thirdLock = new ThirdLock();
  // private static VolatileExample volatileEx = new VolatileExample();

  public static void main(String[] args) {

    // No Lock Test
    ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_THREADS);

    for (int i = 0; i < NUMBER_THREADS; i++) {
      executorService.execute(new NoLockRunnable());
    }

    executorService.shutdown();
    try {
      executorService.awaitTermination(10, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    System.out.println("No Lock: " + counter);

    // Filter Lock Test
    counter = 0;
    executorService = Executors.newFixedThreadPool(NUMBER_THREADS);
    for (int i = 0; i < NUMBER_THREADS; i++) {
      executorService.execute(new FilterLockRunnable());
    }

    executorService.shutdown();
    try {
      executorService.awaitTermination(10, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    System.out.println("Filter Lock: " + counter);


    // Bakery Lock Test
    counter = 0;
    executorService = Executors.newFixedThreadPool(NUMBER_THREADS);
    for (int i = 0; i < NUMBER_THREADS; i++) {
      executorService.execute(new BakeryLockRunnable());
    }

    executorService.shutdown();
    try {
      executorService.awaitTermination(10, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    System.out.println("Bakery Lock: " + counter);



    // // Third Lock Test
    // counter = 0;
    // executorService = Executors.newFixedThreadPool(NUMBER_THREADS);
    // for (int i = 0; i < NUMBER_THREADS; i++) {
    //   executorService.execute(new ThirdLockRunnable());
    // }

    // executorService.shutdown();
    // try {
    //   executorService.awaitTermination(10, TimeUnit.SECONDS);
    // } catch (InterruptedException e) {
    //   e.printStackTrace();
    // }

    // System.out.println("Third Lock: " + counter);


    // // Volatile Lock Test
    // counter = 0;
    // executorService = Executors.newFixedThreadPool(NUMBER_THREADS);
    // for (int i = 0; i < NUMBER_THREADS; i++) {
    //   executorService.execute(new VolatileRunnable());
    // }

    // executorService.shutdown();
    // try {
    //   executorService.awaitTermination(10, TimeUnit.SECONDS);
    // } catch (InterruptedException e) {
    //   e.printStackTrace();
    // }

    // System.out.println("Third Lock: " + counter);

  }


  private static class NoLockRunnable implements Runnable {

    private NoLockRunnable() {}

    @Override
    public void run() {
      try {
        if (counter < 10) {
          Thread.sleep(10);
          counter++;
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  private static class FilterLockRunnable implements Runnable {

    private FilterLockRunnable() {}

    @Override
    public void run() {
      try {
        filterLock.lock();
        if (counter < 10) {
          Thread.sleep(10);
          counter++;
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        filterLock.unlock();
      }
    }
  }

  private static class BakeryLockRunnable implements Runnable {

    private BakeryLockRunnable() {}

    @Override
    public void run() {
      try {
        bakeryLock.lock();
        if (counter < 10) {
          Thread.sleep(10);
          counter++;
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        bakeryLock.unlock();
      }
    }
  }

  // private static class ThirdLockRunnable implements Runnable {
  //   private ThirdLockRunnable() {}

  //   @Override
  //   public void run() {
  //     try {
  //       thirdLock.lock();
  //       System.out.println("b??????");
  //       if (counter < 10) {
  //         Thread.sleep(10);
  //         counter++;
  //       }
  //     } catch (InterruptedException e) {
  //       e.printStackTrace();
  //     } finally {
  //       System.out.println("c??????");
  //       thirdLock.unlock();
  //     }
  //   }
  // }

  // private static class VolatileRunnable implements Runnable {

  //   private VolatileRunnable() {}

  //   @Override
  //   public void run() {
  //     volatileEx.reader();
  //     volatileEx.writer();
  //   }
  // }
}
