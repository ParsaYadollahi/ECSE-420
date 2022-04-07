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
    System.out.println(future);

    executorService.shutdown();

    while(!executorService.isTerminated()) {}

    long endParallelMultiply = System.nanoTime() / 1000000;
    long time = endParallelMultiply - startParallelMultiply;
    System.out.println(time);

    // result.printVector();
    return result;
  }


  /**
	 * Splits the matrix and vector in 2 until it can no longer be split
	 * @param matrix matrix to multiply
	 * @param vector vector to multiply
   * @param resultVector result vector
   * @param executorService ExecutorService
	 * @return matrix
	 */
  static class MultiplyTask implements Runnable {
    Matrix matrix;
    Vector vector, resultVector, resVectL, resVectR;
    ExecutorService executorService;
    int left = 0, right = 1;


    MultiplyTask(Matrix matrix, Vector vector, Vector resultVector, ExecutorService executorService) {
      this.matrix = matrix;
      this.vector = vector;
      this.resultVector = resultVector;
      this.executorService = executorService;

      resVectL = new Vector(vector.getDimension());
      resVectR = new Vector(vector.getDimension());
    }

    @Override
    public void run() {
      try {
        int n = matrix.getDimension();
        if (n != 1) {
          Matrix[][] splitMat = matrix.split();
          Vector[] splitVect = vector.split(), splitResVectL = resVectL.split(), splitResVectR = resVectR.split();

          Future<?>[][] future = (Future<?>[][]) new Future[2][2];

          for (int i = 0; i < 2; i++) {
            MultiplyTask a = new MultiplyTask(splitMat[i][left], splitVect[left], splitResVectL[i], executorService);
            MultiplyTask b = new MultiplyTask(splitMat[i][right], splitVect[right], splitResVectR[i], executorService);

            future[i][left] = executorService.submit(a);
            future[i][right] = executorService.submit(b);
          }

          for (int i = 0; i < 2; i ++) {
            for (int j = 0; j < 2; j ++) {
              future[i][j].get();
            }
          }

          Future<?> merge = executorService.submit(new AddTask(resultVector, resVectL, resVectR, executorService));
          merge.get();

        } else {
          resultVector.set(0, resultVector.get(0) + matrix.get(0, 0) * vector.get(0));
        }

      } catch (Exception e) {
        e.printStackTrace();
      }
    }

  }


  static class AddTask implements Runnable {
    Vector resultVector;
    Vector resVectL, resVectR;
    ExecutorService executorService;

    AddTask(Vector resultVector, Vector resVectL, Vector rightVectR, ExecutorService executorService) {
      this.resultVector = resultVector;
      this.resVectL = resVectL;
      this.resVectR = rightVectR;
      this.executorService = executorService;
    }

    @Override
    public void run() {
      try {
        if (resVectR.getDimension() != 1) {
          Vector[] resVectLSplit = resVectL.split();
          Vector[] subvResVectSplit = resVectR.split();
          Vector[] resultSplit = resultVector.split();

          Future<?>[] future = (Future<?>[]) new Future[2];

          for (int i = 0; i < 2; i++) {
            future[i] = executorService.submit(new AddTask(resultSplit[i], resVectLSplit[i], subvResVectSplit[i], executorService));
          }
          for (int i = 0; i < 2; i ++) {
            future[i].get();
          }
        } else {
          resultVector.set(0, resVectL.get(0) + resVectR.get(0));
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
