package ca.mcgill.ecse420.a2;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author Parsa Yadollahi
 * @author Nicholas Nikas
 */


public class BakeryLock implements Lock {
  private AtomicBoolean[] flag;
  private AtomicInteger[] label;
  private int n;

  public BakeryLock(int n) {
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
    int thread_id = (int) Thread.currentThread().getId() % n;
    flag[thread_id].set(true);
    label[thread_id].set(findMaxElement(label) + 1);
    for (int k = 0; k < n; k++) {
      while (
        (k != thread_id) && flag[k].get() &&
        (
          (label[k].get() < label[thread_id].get()) ||
          ((label[k].get() == label[thread_id].get()) && k < thread_id)
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
    int thread_id = (int) Thread.currentThread().getId() % n;
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

  @Override
  public void lockInterruptibly() throws InterruptedException {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean tryLock() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public Condition newCondition() {
    // TODO Auto-generated method stub
    return null;
  }
}
