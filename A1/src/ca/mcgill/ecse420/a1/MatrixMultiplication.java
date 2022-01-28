package ca.mcgill.ecse420.a1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MatrixMultiplication {

	private static final int NUMBER_THREADS = 1;
	private static final int MATRIX_SIZE = 2000;

	public static void main(String[] args) {

		// Generate two random matrices, same size
		double[][] a = generateRandomMatrix(MATRIX_SIZE, MATRIX_SIZE);
		double[][] b = generateRandomMatrix(MATRIX_SIZE, MATRIX_SIZE);
		sequentialMultiplyMatrix(a, b);
		parallelMultiplyMatrix(a, b);
	}

	/**
	 * Returns the result of a sequential matrix multiplication
	 * The two matrices are randomly generated
	 * @param a is the first matrix
	 * @param b is the second matrix
	 * @return the result of the multiplication
	 * */
	public static double[][] sequentialMultiplyMatrix(double[][] a, double[][] b) {
		double[][] result_matrix = new double[MATRIX_SIZE][MATRIX_SIZE];
		for (int i = 0; i < MATRIX_SIZE; i++) {
			for (int j = 0; j < MATRIX_SIZE; j++) {
				for (int k = 0; k < MATRIX_SIZE; k++){
					result_matrix[i][j] += a[i][k] * b[k][j];
				}
			}
		}
		return result_matrix;
	}

	/**
	 * Returns the result of a concurrent matrix multiplication
	 * The two matrices are randomly generated
	 * @param a is the first matrix
	 * @param b is the second matrix
	 * @return the result of the multiplication
	 * */
	public static double[][] parallelMultiplyMatrix(double[][] a, double[][] b) {
		double[][] result_matrix = new double[MATRIX_SIZE][MATRIX_SIZE];

		try {
			// Create a thread pool
			ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_THREADS);

			for (int i = 0; i < MATRIX_SIZE; i++) {
				for (int j = 0; j < MATRIX_SIZE; j++) {
					executorService.execute(new ParallelMultiply(a.length, b.length, a, b, result_matrix));
				}
			}

			// No other threads accept tasks
			executorService.shutdown();

			// Wait for threads to finish
			executorService.awaitTermination(5, TimeUnit.SECONDS);
			System.out.println("Multiplication succeessfully terminated: " + executorService.isTerminated());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result_matrix;
	}

	static class ParallelMultiply implements Runnable {
		private int row;
		private  int col;
		private double[][] a;
		private double[][] b;
		private double[][] result_matrix;

		ParallelMultiply(int row, int col, double[][] a, double[][] b, double[][] result_matrix) {
			this.row = row;
			this.col = col;
			this.a = a;
			this.b = b;
			this.result_matrix = result_matrix;
		}

		public void run() {
			for (int k = 0; k < MATRIX_SIZE; k++) {
				result_matrix[row][col] += a[row][k] * b[k][col];
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