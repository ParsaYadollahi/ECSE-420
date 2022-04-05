package ca.mcgill.ecse420.a3;

import java.util.concurrent.ExecutionException;

public class TestMatrixVectorMulitply {

  public static void main(String [] args) throws InterruptedException, ExecutionException {
    Vector v = new Vector(128);
    v.generateRandomVector();

    Matrix m = new Matrix(128);
    m.generateRandomMatrix();

    Vector res = ParallelMatrixVectorMulti.parallelMatrixVectorMultiplication(m, v);
    System.out.println(res);
  }

}
