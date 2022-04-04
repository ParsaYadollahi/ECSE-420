package ca.mcgill.ecse420.a3;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Node<T> {
  T item;
  int key;
  Node next;
  Lock lock;


  Node(int key) {
    this.item = null;
    this.key = key;
    this.lock = new ReentrantLock();
  }

  Node(T item) {
    this.item = item;
    this.key = item.hashCode();
    this.lock = new ReentrantLock();
  }


  public void lock() {
    this.lock.lock();
  }

  public void unlock() {
    this.lock.unlock();
  }
}
