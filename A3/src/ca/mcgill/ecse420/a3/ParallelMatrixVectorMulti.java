package ca.mcgill.ecse420.a3;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ParallelMatrixVectorMulti {
  static int NUM_THREADS = 4;


  public static Vector parallelMatrixVectorMultiplication(Matrix matrix, Vector vector) throws InterruptedException, ExecutionException{
    if (matrix.getDim() != vector.getDim()) {
      throw new ArithmeticException("Invalid Matrix dimensions");
    }

    long startParallelMultiply = System.nanoTime() / 1000000;

    Vector result = new Vector(vector.getDim());
    ExecutorService executorService = Executors.newCachedThreadPool();

    Future<?> future = executorService.submit(new MulTask(matrix, vector, result, executorService));
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
	 * @param a matrix to multiply
	 * @param b vector to multiply
   * @param c result vector
   * @param exec ExecutorService
	 * @return matrix
	 */
  static class MulTask implements Runnable {
    Matrix a;
    Vector b, c, lhs, rhs;
    ExecutorService exec;


    MulTask(Matrix a, Vector b, Vector c, ExecutorService executorService) {
      this.a = a;
      this.b = b;
      this.c = c;
      this.exec = executorService;

      lhs = new Vector(b.getDim());
      rhs = new Vector(b.getDim());
    }

    @Override
    public void run() {
      try {
        if (a.getDim() != 1) {
          Matrix[][] aa = a.split();
          Vector[] bb = b.split(), ll = lhs.split(), rr = rhs.split();

          Future<?>[][] future = (Future<?>[][]) new Future[2][2];

          for (int i = 0; i < 2; i++) {
            MulTask a = new MulTask(aa[i][0], bb[0], ll[i], exec);
            MulTask b = new MulTask(aa[i][1], bb[1], rr[i], exec);

            future[i][0] = exec.submit(a);
            future[i][1] = exec.submit(b);
          }

          for (int i = 0; i < 2; i ++) {
            for (int j = 0; j < 2; j ++) {
              future[i][j].get();
            }
          }

          Future<?> done = exec.submit(new AddTask(lhs, rhs, c, exec));
          done.get();

        } else {
          c.set(0, a.get(0, 0) * b.get(0));
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

    AddTask(Vector resVectL, Vector rightVectR, Vector resultVector, ExecutorService executorService) {
      this.resultVector = resultVector;
      this.resVectL = resVectL;
      this.resVectR = rightVectR;
      this.executorService = executorService;
    }

    @Override
    public void run() {
      try {
        if (resVectR.getDim() != 1) {
          Vector[] resVectLSplit = resVectL.split();
          Vector[] subvResVectSplit = resVectR.split();
          Vector[] resultSplit = resultVector.split();

          Future<?>[] future = (Future<?>[]) new Future[2];

          for (int i = 0; i < 2; i++) {
            future[i] = executorService.submit(new AddTask(resVectLSplit[i], subvResVectSplit[i], resultSplit[i], executorService));
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
