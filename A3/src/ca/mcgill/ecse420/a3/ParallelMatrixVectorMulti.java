package ca.mcgill.ecse420.a3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ParallelMatrixVectorMulti {
  public int NUM_THREADS;
  public Matrix matrix;
  public Vector vector;

  public ParallelMatrixVectorMulti(Matrix matrix, Vector vector, int NUM_THREADS) {
    this.matrix = matrix;
    this.vector = vector;
    this.NUM_THREADS = NUM_THREADS;
  }

  public static Vector mulitply(Matrix a, Vector b, int num_threads) {
    Vector c = new Vector(b.dimension);
    int m_size = a.dimension;
    ExecutorService exec = Executors.newFixedThreadPool(num_threads);

    for (int col = 0; col < m_size; col++) {
      exec.execute(new OneEntryMultiply(a, b, c, col));
    }
    exec.shutdown();

    try {
      exec.awaitTermination(10, TimeUnit.SECONDS);
  } catch (InterruptedException e) {
      e.printStackTrace();
  }
    return c;
  }

  static class OneEntryMultiply implements Runnable {
    private Matrix a;
    private Vector b, c;
    private int MATRIX_SIZE, col;

    public OneEntryMultiply(Matrix a, Vector b, Vector c, int col){
      this.a = a;
      this.b = b;
      this.c = c;
      this.MATRIX_SIZE = a.dimension;
      this.col = col;
    }

    @Override
    public void run() {
      for (int row = 0; row < MATRIX_SIZE; row++) {
        c.set(col, c.get(col) + a.get(col, row) * b.get(row));
      }
    }
  }
}
