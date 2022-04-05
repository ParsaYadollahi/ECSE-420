package ca.mcgill.ecse420;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ParallelMatrixVectorMulti {
  static int NUM_THREADS = 4;


  public static double[] parallelMatrixVectorMultiplication(double[][] matrix, double[] vector){

    int rows = matrix.length;
    int cols = matrix[0].length;

    double[] vector_res = new double[rows];

    ExecutorService executorService = Executors.newCachedThreadPool();

    if (rows != cols) {
      throw new ArithmeticException("Invalid Matrix dimensions");
    }





    return vector_res;
  }


  static class MultiplyTask implements Runnable {
    double[][] matrix;
    double[] vector, vector_res;
    int rows, cols;

    MultiplyTask(double[][] matrix, double[] vector, double[] vector_res) {
      this.matrix = matrix;
      this.vector = vector;
      this.vector_res = vector_res;

    }

    @Override
    public void run() {
      rows = matrix.length;
      cols = matrix[0].length;

      for (int row = 0; row < rows; row++) {
        for (int col = 0; col < cols; col++) {
          vector_res[row] += matrix[row][col] * vector[col];
        }
      }

    }

  }

}
