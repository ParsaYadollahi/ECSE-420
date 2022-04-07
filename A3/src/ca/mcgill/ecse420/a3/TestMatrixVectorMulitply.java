package ca.mcgill.ecse420.a3;

import java.util.concurrent.ExecutionException;

public class TestMatrixVectorMulitply {
  private static final int NUM_THREADS = 3;
  private static final int MATRIX_SIZE = 2000;

  public static void main(String [] args) throws InterruptedException, ExecutionException {
    Matrix m = new Matrix(MATRIX_SIZE);
    m.generateRandomMatrix();
    m.printMatrix();

    Vector v = new Vector(MATRIX_SIZE);
    v.generateRandomVector();
    v.printVector();

    System.out.println();

    Vector res = ParallelMatrixVectorMuilt2.mulitply(m, v, NUM_THREADS);
    res.printVector();
  }
}
