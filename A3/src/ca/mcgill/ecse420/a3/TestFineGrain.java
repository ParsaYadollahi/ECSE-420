package ca.mcgill.ecse420.a3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import ca.mcgill.ecse420.a3.FineGrain;
import ca.mcgill.ecse420.a3.Node;

public class TestFineGrain {
  public static int NUM_THREADS = 3;
  public static int NUM_ITEMS = 10;
  public static int THREAD_ITEMS = NUM_ITEMS / NUM_THREADS;

  public static FineGrain<Integer> fineGrain= new FineGrain<>();

  public static void main(String[] args) {
    ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);

    for (int i = 0; i < NUM_THREADS; i++){
      executorService.execute(new NodeRunnable(i));
    }
    executorService.shutdown();

    try {
      executorService.awaitTermination(10, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    fineGrain.printLinkedList();
  }

  public static class NodeRunnable implements Runnable {
    int thread_num;

    public NodeRunnable(int t_num) {
      this.thread_num = t_num;
    }

    @Override
    public void run() {
      // Each thread adds THREAD_ITEMS to the linked list
      for (int thread_item = 0; thread_item < THREAD_ITEMS; thread_item++) {
        // Get unique integers
        int item = thread_num + THREAD_ITEMS * thread_item;

        if (!fineGrain.add(item)) {
          System.out.println("Failed to add " + item);
        }

        try {
          Thread.sleep(10);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

        if (fineGrain.contains(item)) {
          if (!fineGrain.remove(item)) {
            System.out.println("Failed to Remove " + item);
          }
        } else {
          System.out.println("Failed to Find " + item);
        }
      }
    }
  }
}
