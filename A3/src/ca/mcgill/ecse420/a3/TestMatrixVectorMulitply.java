package ca.mcgill.ecse420.a3;

import java.util.concurrent.ExecutionException;

public class TestMatrixVectorMulitply {
  private static final int NUM_THREADS = 3;
  private static final int MATRIX_SIZE = 2000;

  public static void main(String [] args) throws InterruptedException, ExecutionException {
    Matrix m = new Matrix(2);
    m.generateRandomMatrix();
    m.printMatrix();

    Vector v = new Vector(2);
    v.generateRandomVector();
    v.printVector();

    System.out.println();
    // Vector res = ParallelMatrixVectorMulti.parallelMatrixVectorMultiplication(m, v);
    Vector res = ParallelMatrixVectorMuilt2.mulitply(m, v);
    res.printVector();
  }
}
