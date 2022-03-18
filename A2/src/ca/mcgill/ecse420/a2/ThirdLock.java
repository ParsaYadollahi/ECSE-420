package ca.mcgill.ecse420.a2;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author Parsa Yadollahi
 * @author Nicholas Nikas
 */


public class ThirdLock implements Lock {
  // We use AtomicInteger[] instead of int[] because of the way Java manages memory

  /**
   * Constructor for Filter lock
   *
   * @param n thread count
  */

  private int turn;
  private boolean busy = false;

  public ThirdLock() {
  }

  public void lock() {
    int me = (int) Thread.currentThread().getId();
    turn = me;
    do {
      busy = true;
      System.out.println("me = " + me + "  turn = " + turn + " busy = " + busy);
    } while ( turn == me || busy);
  }

  public void unlock() {
    busy = false;
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
