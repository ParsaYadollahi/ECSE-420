package ca.mcgill.ecse420.a3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParallelMatrixVectorMuilt2 {
  public int NUM_THREADS;
  public Matrix matrix;
  public Vector vector;

  public ParallelMatrixVectorMuilt2(Matrix matrix, Vector vector, int NUM_THREADS) {
    this.matrix = matrix;
    this.vector = vector;
    this.NUM_THREADS = NUM_THREADS;
  }

  public static Vector mulitply(Matrix matrix, Vector vector, int NUM_THREADS) {
    Vector c = new Vector(vector.dimension);
    int MATRIX_SIZE = matrix.dimension;
    ExecutorService exec = Executors.newFixedThreadPool(NUM_THREADS);

    for (int col = 0; col < MATRIX_SIZE; col++) {
      exec.execute(new OneEntryMultiply(matrix, vector, c, col));
    }
    exec.shutdown();

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
