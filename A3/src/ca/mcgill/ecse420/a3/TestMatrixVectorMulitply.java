package ca.mcgill.ecse420.a3;

import java.util.concurrent.ExecutionException;

public class TestMatrixVectorMulitply {
  private static final int NUM_THREADS = 3;
  private static final int MATRIX_SIZE = 2000;

  public static void main(String [] args) throws InterruptedException, ExecutionException {
    Matrix m = new Matrix(MATRIX_SIZE);
    m.generateRandomMatrix();
    if (MATRIX_SIZE < 15) {
      System.out.println("The matrix to multiply");
      System.out.println("----------------------");
      m.printMatrix();
    }

    Vector v = new Vector(MATRIX_SIZE);
    v.generateRandomVector();
    if (MATRIX_SIZE < 15) {
      System.out.println();
      System.out.println("The vector to multiply");
      System.out.println("----------------------");
      v.printVector();
    }

    System.out.println();

    double start = System.currentTimeMillis();
    Vector res_seq = ParallelMatrixVectorMulti.mulitply(m, v, NUM_THREADS);
    double end = System.currentTimeMillis();
    if (MATRIX_SIZE < 15) {
      System.out.println("Sequential Multiply");
      System.out.println("-------------------");
      res_seq.printVector();
    }

    System.out.println("[TIME] Sequential time: " + (end - start) / 1000.0 + " seconds");
    System.out.println("----------------------");


    start = System.currentTimeMillis();
    Vector res_parallel = ParallelMatrixVectorMulti.mulitply(m, v, NUM_THREADS);
    end = System.currentTimeMillis();
    if (MATRIX_SIZE < 15) {
      System.out.println("Parallel Multiply");
      System.out.println("-----------------");
      res_parallel.printVector();
    }
    System.out.println();

    System.out.println("[TIME] Parallel time: " + (end - start) / 1000.0 + " seconds");
    System.out.println("---------------------");


    System.out.println("The Sequential and Parallel vectors are the same: " + res_seq.isSame(res_parallel));
  }
}
