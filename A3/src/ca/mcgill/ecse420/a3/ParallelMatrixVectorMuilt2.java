package ca.mcgill.ecse420.a3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ParallelMatrixVectorMuilt2 {

	public static double[][] matrix;
	public static double[] vector;
	public static int NUMBER_THREADS;
	public static int MATRIX_SIZE;

	public ParallelMatrixVectorMuilt2(double[][] matrix, double[] vector,
			int MATRIX_SIZE, int NUMBER_THREADS){
		this.matrix = matrix;
		this.vector = vector;
		this.MATRIX_SIZE = MATRIX_SIZE;
		this.NUMBER_THREADS = NUMBER_THREADS;
	}

	/**
	 * @return the result of the multiplication
	 * */
	public static double[] parallelMultiplication() {
    	double[] c = new double[MATRIX_SIZE];
    	ExecutorService executor = Executors.newFixedThreadPool(NUMBER_THREADS);
    	for (int i = 0; i < MATRIX_SIZE; i++) {
    		executor.execute(new OneEntryMultiplication(matrix, vector, c, i, MATRIX_SIZE));
    	}
    	executor.shutdown();
    	try {
			executor.awaitTermination(30, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	/*
    	// if you want to print the result
    	for (int i = 0; i < MATRIX_SIZE; i++) {
			System.out.print(c[i] + " ");
		}
    	System.out.println("");
		*/

      for (int row = 0 ; row < c.length ; row++ ) {
        System.out.print("| " + c[row] + " |\n");
      }
    	return c;
	}
}

//task class: one row of the matrix multiplied by the vector
//to produce one entry of the result vector.
class OneEntryMultiplication implements Runnable {
	private double[][] a;
	private double[] b, c;
	private int i, MATRIX_SIZE;
	public OneEntryMultiplication(double[][] m1, double[] m2, double[] m3,
			int index, int m_size) {
		a = m1; b = m2; c = m3;
		i = index; MATRIX_SIZE = m_size;
	}
	@Override
	public void run() {
		for (int j = 0; j < MATRIX_SIZE; j++) {
			c[i] = c[i] + a[i][j] * b[j];
		}
	}
}
