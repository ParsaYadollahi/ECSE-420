package ca.mcgill.ecse420.a2;

public class VolatileExample {
  int x = 0;
  boolean v = false;
  public void writer() {
    x = 42;
    v = true;
  }

  public void reader() {
    if (v == true) {
      if (x == 0 && v == true) {
        System.out.println("Hit");
      }
    }
  }

}
