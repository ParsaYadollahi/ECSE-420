package ca.mcgill.ecse420.a3;

import java.util.concurrent.ExecutionException;

public class TestMatrixVectorMulitply {

  public static void main(String [] args) throws InterruptedException, ExecutionException {
    Matrix m = new Matrix(3);
    m.generateRandomMatrix();
    m.printMatrix();

    Vector v = new Vector(3);
    v.generateRandomVector();
    v.printVector();

    System.out.println();
    Vector res = ParallelMatrixVectorMulti.parallelMatrixVectorMultiplication(m, v);
    res.printVector();
  }
}
