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

    executorService.shutdown();

    while(!executorService.isTerminated()) {}

    long endParallelMultiply = System.nanoTime() / 1000000;
    long time = endParallelMultiply - startParallelMultiply;
    System.out.println("Time taken: " + time);

    // result.printVector();
    return result;
  }

  static class MulTask implements Runnable {
    Matrix a;
    Vector b, c, lhs, rhs;
    ExecutorService exec;

    MulTask(Matrix a, Vector b, Vector c, ExecutorService exec) {
      this.a = a;
      this.b = b;
      this.c = c;
      this.exec = exec;

      lhs = new Vector(a.getDim());
      rhs = new Vector(a.getDim());
    }

    @Override
    public void run() {
      try {
        if (a.getDim() == 1) {
          c.set(0, a.get(0, 0) * b.get(0));
          c.printVector();
          System.out.println();
        } else {
          Matrix[][] aa = a.split();
          Vector[] bb = b.split();
          Vector[] ll = lhs.split(), rr = rhs.split();

          Future<?>[][] future = (Future<?>[][]) new Future[2][2];

          for (int i = 0; i < 2; i++) {
            future[i][0] =
                exec.submit(new MulTask(aa[i][0], bb[0], ll[i], exec));
            future[i][1] =
                exec.submit(new MulTask(aa[i][1], bb[1], rr[i], exec));
          }

          for (int i = 0; i < 2; i ++) {
            for (int j = 0; j < 2; j ++) {
              future[i][j].get();
            }
          }

          Future<?> done = exec.submit(new AddTask(lhs, rhs, c, exec));
          done.get();
        }

      } catch (Exception e) {
        e.printStackTrace();
      }
    }

  }


  static class AddTask implements Runnable {
    Vector c;
    Vector a, b;
    ExecutorService exec;

    AddTask(Vector a, Vector b, Vector c, ExecutorService exec) {
      this.c = c;
      this.a = a;
      this.b = b;
      this.exec = exec;
    }

    @Override
    public void run() {
      try {
        if (a.getDim() == 1) {
          c.set(0, a.get(0) + b.get(0));
          // System.out.println();
          // System.out.println("a");
          // a.printVector();
          // b.printVector();
          // c.printVector();
          // System.out.println("b");
          // System.out.println();
        } else {
          Vector[] aa = a.split(), bb = b.split(), cc = c.split();

          Future<?>[] future = (Future<?>[]) new Future[2];

          for (int i = 0; i < 2; i++) {
            future[i] = exec.submit(new AddTask(aa[i], bb[i], cc[i], exec));
          }
          for (int i = 0; i < 2; i ++) {
            future[i].get();
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
