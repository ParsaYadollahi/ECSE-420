package ca.mcgill.ecse420.a3;

import java.util.concurrent.ExecutionException;
import java.util.Arrays;


public class TestMatrixVectorMulitply {
  private static final int MAX_NUM_THREADS = 10;
  private static final int MATRIX_SIZE = 2000;

  public static void main(String [] args) throws InterruptedException, ExecutionException {
    Matrix m = new Matrix(MATRIX_SIZE);
    m.generateRandomMatrix();
    // if (MATRIX_SIZE < 15) {
    //   System.out.println("The matrix to multiply");
    //   System.out.println("----------------------");
    //   m.printMatrix();
    // }

    Vector v = new Vector(MATRIX_SIZE);
    v.generateRandomVector();
    // if (MATRIX_SIZE < 15) {
    //   System.out.println();
    //   System.out.println("The vector to multiply");
    //   System.out.println("----------------------");
    //   v.printVector();
    // }

    System.out.println();

    double start = System.currentTimeMillis();
    Vector res_seq = SeqMatrixVectorMultiplic.multiply(m, v);
    double end = System.currentTimeMillis();
    // if (MATRIX_SIZE < 15) {
    //   System.out.println("Sequential Multiply");
    //   System.out.println("-------------------");
    //   res_seq.printVector();
    // }

    System.out.println("[TIME] Sequential time: " + (end - start) / 1000.0 + " seconds");
    System.out.println("----------------------");


    double[] parallelMultiplyTimes = new double[MAX_NUM_THREADS];
    Vector res_parallel = new Vector(10);

    for (int i = 1; i < MAX_NUM_THREADS+1; i++) {
      start = System.currentTimeMillis();
      res_parallel = ParallelMatrixVectorMulti.multiply(m, v, i);
      end = System.currentTimeMillis();
      // if (MATRIX_SIZE < 15) {
      //   System.out.println("Parallel Multiply");
      //   System.out.println("-----------------");
      //   res_parallel.printVector();
      // }
      parallelMultiplyTimes[i - 1] = (end - start) / 1000.0;
    }


    double fastestTime = Arrays.stream(parallelMultiplyTimes).min().getAsDouble();
    System.out.println("[TIME] Parallel time: " + fastestTime + " seconds with " + find(parallelMultiplyTimes, fastestTime));
    System.out.println("---------------------");

    System.out.println("The Sequential and Parallel vectors are the same: " + res_seq.isSame(res_parallel));
  }


  public static int find(double[] array, double value) {
    for(int i=0; i<array.length; i++)
      if(array[i] == value) return i + 1;
    return 3;
  }
}
