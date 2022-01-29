package ca.mcgill.ecse420.a1;

public class Deadlock {
  public static String Lock1 = "lock 1";
  public static String Lock2 = "lock 2";
  public static String Thread1 = "Thread 1";
  public static String Thread2 = "Thread 2";

  public static void main(String[] args) {
    DeadlockThread thread1 = new DeadlockThread(Lock1, Lock2, Thread1);
    DeadlockThread thread2 = new DeadlockThread(Lock2, Lock1, Thread2);
    thread1.start();
    thread2.start();
  }

  public static class DeadlockThread extends Thread {
    private String lock1;
    private String lock2;
    private String threadNumber;


    public DeadlockThread(String lock1, String lock2, String threadNumber) {
      this.lock1 = lock1;
      this.lock2 = lock2;
      this.threadNumber = threadNumber;
    }

    public void run(){
      synchronized(lock1) {
        System.out.println(threadNumber + ": Holding " + lock1);

        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        System.out.println(threadNumber + ": waiting for " + lock2);
        synchronized (lock2) {
          System.out.println(threadNumber + ": Holding lock 1 & 2");
        }
      }

    }
  }
}
