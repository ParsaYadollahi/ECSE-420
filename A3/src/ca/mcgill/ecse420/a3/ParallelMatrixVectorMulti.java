package ca.mcgill.ecse420.a3;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ParallelMatrixVectorMulti {
  static int NUM_THREADS = 4;


  public static Vector parallelMatrixVectorMultiplication(Matrix matrix, Vector vector) throws InterruptedException, ExecutionException{
    if (matrix.getDimension() != vector.getDimension()) {
      throw new ArithmeticException("Invalid Matrix dimensions");
    }

    long startParallelMultiply = System.nanoTime() / 1000000;

    Vector result = new Vector(vector.getDimension());
    ExecutorService executorService = Executors.newCachedThreadPool();

    Future<?> future = executorService.submit(new MultiplyTask(matrix, vector, result, executorService));
    future.get();

    executorService.shutdown();

    while(!executorService.isTerminated()) {}

    long endParallelMultiply = System.nanoTime() / 1000000;
    long time = endParallelMultiply - startParallelMultiply;
    System.out.println(time);

    return result;
  }


  static class MultiplyTask implements Runnable {
    Matrix matrix;
    Vector vector, result, lhs, rhs;
    ExecutorService executorService;


    MultiplyTask(Matrix matrix, Vector vector, Vector result, ExecutorService executorService) {
      this.matrix = matrix;
      this.vector = vector;
      this.result = result;
      this.executorService = executorService;

      lhs = new Vector(vector.getDimension());
      rhs = new Vector(vector.getDimension());
    }

    @Override
    public void run() {
      try {
        if (matrix.getDimension() != 1) {
          Matrix[][] subMatrix = matrix.split();
          Vector[] subVectors = vector.split();
          Vector[] subLHS = lhs.split();
          Vector[] subRHS = rhs.split();

          Future<?>[][] future = (Future<?>[][]) new Future[2][2];
          for (int i = 0; i < 2; i++) {
            future[i][0] = executorService.submit(new MultiplyTask(subMatrix[i][0], subVectors[0], subLHS[0], executorService));
            future[i][1] = executorService.submit(new MultiplyTask(subMatrix[i][1], subVectors[1], subRHS[0], executorService));
          }
          for (int i = 0; i < 2; i ++) {
            for (int j = 0; j < 2; j ++) {
              future[i][j].get();
            }
          }

          // Future<?> ret = executorService.submit();
        } else {
          result.set(0, matrix.get(0, 0) * vector.get(0));
        }

      } catch (Exception e) {
        e.printStackTrace();
      }
    }

  }


  static class AddTask implements Runnable {
    Vector result;
    Vector v1, v2;
    ExecutorService executorService;

    AddTask(Vector result, Vector v1, Vector v2, ExecutorService executorService) {
      this.result = result;
      this.v1 = v1;
      this.v2 = v2;
      this.executorService = executorService;
    }

    @Override
    public void run() {
      try {
        if (v1.getDimension() != 1) {
          Vector[] subv1 = v1.split();
          Vector[] subv2 = v2.split();
          Vector[] subResult = result.split();

          Future<?>[] future = (Future<?>[]) new Future[2];

          for (int i = 0; i < 2; i++) {
            future[i] = executorService.submit(new AddTask(subResult[i], subv1[i], subv2[i], executorService));
          }
          for (int i = 0; i < 2; i ++) {
            future[i].get();
          }
        } else {
          result.set(0, v1.get(0) + v2.get(0));
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
