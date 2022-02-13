package ca.mcgill.ecse420.a2;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * @author Parsa Yadollahi
 * @author Nicholas Nikas
 */


public class BakeryLock implements Lock {
  private AtomicBoolean[] flag;
  private AtomicInteger[] label;
  private int n;

  public Bakery(int n) {
    this.n = n;
    flag = new AtomicBoolean[n];
    label = new AtomicInteger[n];

    for (int i = 0; i < n; i++) {
      flag[i] = new AtomicBoolean();
      label[i] = new AtomicInteger();
    }
  }

  /**
   * Acquires the lock.
   */
  @Override
  public void lock() {
    int thread_id = (int) Thread.currentThread().getId();
    flag[i].get(true);
    label[i].set(findMaxElement(label) + 1);
    for (int k = 0; k < n; k++) {
      while (
        (k != i) && flag[k].get() &&
        (
          (label[k].get() < label[i].get()) ||
          ((label[k].get() == label[i].get()) && k < i)
        )) {
          // keep looping
        }
    }
  }

  /**
   * Release the lock.
   */
  @Override
  public void unlock() {
    int thread_id = (int) Thread.currentThread().getId();
    flag[thread_id].set(false);
  }

   /**
     * Finds maximum element within an array
     *
     * @param elementArr element array
     * @return maximum element
     */
  private int findMaxElement(AtomicInteger[] elementArr) {
    int maxValue = Integer.MIN_VALUE;
    for (AtomicInteger el : elementArr) {
      if (el.get() > maxValue) {
        maxValue = el.get();
      }
    }
    return maxValue;
  }
}
