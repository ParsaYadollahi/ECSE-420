package ca.mcgill.ecse420.a3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ca.mcgill.ecse420.a3.FineGrain;
import ca.mcgill.ecse420.a3.Node;

public class TestFineGrain {
  public static int NUM_THREADS = 4;
  public static int NUM_ITEMS = 10;
  public static int THREAD_ITEMS = NUM_ITEMS / NUM_THREADS;

  public static FineGrain<Integer> fineGrain= new FineGrain<>();

  public static void main(String[] args) {
    ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);

    for (int i = 0; i < NUM_THREADS; i++){
      executorService.execute(new NodeRunnable(i * THREAD_ITEMS));
    }

    executorService.shutdown();

    fineGrain.printLinkedList();
    System.out.println("Empty string means we've successfully found and removed every node ✅");
  }

  public static class NodeRunnable implements Runnable {
    int count;

    public NodeRunnable(int count) {
      this.count = count;
    }

    @Override
    public void run() {
      for (int i = 0; i < THREAD_ITEMS; i++) {
        // System.out.println(count + i);
        if (!fineGrain.add(count + i)) {
          System.out.println("Failed to add " + count + i);
        }

        if (fineGrain.contains(count + i)) {
          if (!fineGrain.remove(count + i)) {
            System.out.println("Failed to Remove " + count + i);
          }
        } else {
          System.out.println("Failed to Find " + count + i);
        }
      }
    }
  }
}
