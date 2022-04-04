package ca.mcgill.ecse420.a3;

import ca.mcgill.ecse420.a3.Node;

public class FineGrain<T> {
  private static Node head;

  public FineGrain() {
    head = new Node<>(Integer.MAX_VALUE);
    head.next = new Node<>(Integer.MAX_VALUE);
  }


  public boolean add(T item) {
    int key = item.hashCode();

    head.lock();
    Node prev = head;
    try {
      Node curr = prev.next;
      curr.lock();
      try {
        while (curr.key < key) {
          prev.unlock();
          prev = curr;
          curr = curr.next;
          curr.lock();
        }

        if (curr.key == key) {
          return false;
        }

        Node newNode = new Node<T>(item);
        newNode.next = curr;
        prev.next = newNode;
        return true;
      } finally {
        curr.unlock();
      }
    } finally {
      prev.unlock();
    }
  }


  public boolean remove(T item) {
    Node prev = null;
    Node curr = null;

    int key = item.hashCode();
    head.lock();

    try {
      prev = head;
      curr = prev.next;
      curr.lock();
      try {
        while (curr.key < key) {
          prev.unlock();
          prev = curr;
          curr = curr.next;
          curr.lock();
        }
        if (curr.key == key) {
          prev.next = curr.next;
          return true;
        }
        return false;
      } finally {
        curr.unlock();
      }
    } finally {
      prev.unlock();
    }
  }

  public boolean contains(T item) {
    Node prev = null;
    Node curr = null;

    int key = item.hashCode();
    head.lock();

    try {
      prev = head;
      curr = prev.next;
      curr.lock();
      try {
        while(curr.key < key) {
          prev.unlock();
          prev = curr;
          curr = curr.next;
          curr.lock();
        }
        if (curr.key == key){
          return true;
        } else {
          return false;
        }
      } finally {
        curr.unlock();
      }
    } finally {
      prev.unlock();
    }
  }


  public static void printLinkedList() {
    Node curr = head.next;
    String print = "";

    while(curr.item != null) {
      print += "[ ";
      print += curr.item.toString();
      curr = curr.next;
      print += " ]";
      if (curr.next != null){
        print += " - ";
      }
    }
    if (print == ""){
      print = "[  ]";
    }
    System.out.println(print);
  }

  // @Override
  //   public synchronized String toString() {
  //       Node curr = head.next;
  //       StringBuilder sb = new StringBuilder("[ ");

  //       while (curr.item != null) {
  //           sb.append(curr.item.toString()).append(" ");
  //           curr = curr.next;
  //       }
  //       sb.append("]");
  //       return sb.toString();
  //   }
}
