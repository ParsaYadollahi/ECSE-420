package ca.mcgill.ecse420.a2;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author Parsa Yadollahi
 * @author Nicholas Nikas
 */


public class Filterlock implements Lock {
  // We use AtomicInteger[] instead of int[] because of the way Java manages memory
  private AtomicInteger[] level;
  private AtomicInteger[] victim;
  private int n;

  /**
   * Constructor for Filter lock
   *
   * @param n thread count
  */
  public Filterlock(int n) {
    level = new AtomicInteger[n];
    victim = new AtomicInteger[n];
    this.n = n;

    for (int i = 1; i < n; i++) {
      level[i] = new AtomicInteger();
      victim[i] = new AtomicInteger();
    }

  }

  /**
   * Acquires the lock.
  */
  @Override
  public void lock() {
    int thread_id = (int) Thread.currentThread().getId();
    for (int i = 1; i < n; i++) {
      level[thread_id].set(i);
      victim[i].set(thread_id);

      for (int k = 0; k < n; k++) {
        while (
          (k != thread_id) &&
          (level[k].get() >= i) &&
          (victim[i].get() == thread_id)
          ) {
          // keep looping
        }
      }
    }
  }

  /**
   * Release the lock
  */
  @Override
  public void unlock() {
    int thread_id = (int) Thread.currentThread().getId();
    level[thread_id].set(0);

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
