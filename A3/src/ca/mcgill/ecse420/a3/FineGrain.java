package ca.mcgill.ecse420.a3;

import ca.mcgill.ecse420.a3.Node;

public class FineGrain<T> {
  private static Node head;

  public FineGrain() {
    head = new Node<>(Integer.MAX_VALUE);
    head.next = new Node<>(Integer.MAX_VALUE);
  }

  // Code taken from chapter 9
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


  // Code taken from chapter 9
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


  // Question 2.1
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
        /*
        Only modification to add and remove.
        If we've found the node, we return true
        */
        if (curr.key == key){
          return true;
        }
      } finally {
        curr.unlock();
      }
    } finally {
      prev.unlock();
    }
    return false;
  }

  public static void printLinkedList() {
    Node curr = head.next;
    String linkedList = "";

    while(curr.item != null) {
      linkedList += "[ " + curr.item.toString() + " ]";
      curr = curr.next;

      if (curr.next != null){
        linkedList += " -> ";
      }
    }

    // If we have no nodes, add an empty one to make it look nice nice
    if (linkedList == ""){
      linkedList = "[ ]";
    }

    System.out.println(linkedList);
    if (linkedList == "[ ]") {
      System.out.println("Successfully 1. added, 2. found and 3. removed every node ✅");
    }
  }
}
