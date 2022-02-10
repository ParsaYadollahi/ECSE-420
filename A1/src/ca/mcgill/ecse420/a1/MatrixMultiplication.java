package ca.mcgill.ecse420.a1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MatrixMultiplication {

	private static final int NUMBER_THREADS = 4;
	private static final int MATRIX_SIZE = 500;

	public static void main(String[] args) {

		// Generate two random matrices, same size
		double[][] a = generateRandomMatrix(MATRIX_SIZE, MATRIX_SIZE);
		double[][] b = generateRandomMatrix(MATRIX_SIZE, MATRIX_SIZE);

    System.out.println("Starting sequentialMultiplyMatrix:");
    long begin = System.currentTimeMillis();
		sequentialMultiplyMatrix(a, b);
    long end = System.currentTimeMillis();
    System.out.println("Done sequentialMultiplyMatrix - time = " + (end - begin));

    System.out.println();
    System.out.println("Starting parallelMultiplyMatrix:");
    begin = System.currentTimeMillis();
		parallelMultiplyMatrix(a, b);
    end = System.currentTimeMillis();
    System.out.println("Done parallelMultiplyMatrix - time = " + (end - begin));
	}

	/**
	 * Returns the result of a sequential matrix multiplication
	 * The two matrices are randomly generated
	 * @param a is the first matrix
	 * @param b is the second matrix
	 * @return the result of the multiplication
	 * */
	public static double[][] sequentialMultiplyMatrix(double[][] a, double[][] b) {
    int aRows = a.length;
    int bRows = b.length;
    int bColumns = b[0].length;
    int aColumns = a[0].length;
		double[][] c = new double[aRows][bColumns];

    // Throw exception if matrix dimensions are invalid
    if (aColumns != bRows) {
      throw new ArithmeticException("Invalid matrix dimensions");
    }

		for (int i = 0; i < aRows; i++) {
			for (int j = 0; j < bColumns; j++) {
				for (int k = 0; k < aColumns; k++){
					c[i][j] += a[i][k] * b[k][j];
				}
			}
		}
		return c;
	}

	/**
	 * Returns the result of a concurrent matrix multiplication
	 * The two matrices are randomly generated
	 * @param a is the first matrix
	 * @param b is the second matrix
	 * @return the result of the multiplication
	 * */
	public static double[][] parallelMultiplyMatrix(double[][] a, double[][] b) {
    int aRows = a.length;
    int bRows = b.length;
    int bColumns = b[0].length;
    int aColumns = a[0].length;
		double[][] c = new double[aRows][bColumns];

    // Throw exception if matrix dimensions are invalid
    if (aColumns != bRows) {
      throw new ArithmeticException("Invalid matrix dimensions");
    }

		try {
			// Create a thread pool
			ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_THREADS);

			for (int i = 0; i < aRows; i++) {
				for (int j = 0; j < bColumns; j++) {
					executorService.execute(new ParallelMultiply(i, j, a, b, c));
				}
			}

			// No other threads accept tasks
			executorService.shutdown();

			// Wait for threads to finish
			executorService.awaitTermination(MATRIX_SIZE, TimeUnit.SECONDS);
			System.out.println("Parallel multiplication succeessfully terminated: " + executorService.isTerminated());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return c;
	}

	static class ParallelMultiply implements Runnable {
		private int row;
		private  int col;
		private double[][] a;
		private double[][] b;
		private double[][] c;

		ParallelMultiply(int row, int col, double[][] a, double[][] b, double[][] c) {
			this.row = row;
			this.col = col;
			this.a = a;
			this.b = b;
			this.c = c;
		}

		public void run() {
			for (int k = 0; k < MATRIX_SIZE; k++) {
				c[row][col] += a[row][k] * b[k][col];
			}
		}
	}

	/**
	 * Populates a matrix of given size with randomly generated integers between 0-10.
	 * @param numRows number of rows
	 * @param numCols number of cols
	 * @return matrix
	 */
	private static double[][] generateRandomMatrix (int numRows, int numCols) {
		double matrix[][] = new double[numRows][numCols];
		for (int row = 0 ; row < numRows ; row++ ) {
			for (int col = 0 ; col < numCols ; col++ ) {
				matrix[row][col] = (double) ((int) (Math.random() * 10.0));
			}
		}
		return matrix;
	}

}
