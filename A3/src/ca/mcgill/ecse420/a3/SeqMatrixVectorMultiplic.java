package ca.mcgill.ecse420.a3;

public class SeqMatrixVectorMultiplic {

  public static void main(String[] args) {}

  /**
	 * Returns the result of a sequential matrix and vector multiplication
	 * The Matrix and vector are randomly generated
	 * @param matrix is the matrix
	 * @param vector is the vector
	 * @return the result of the multiplication
	 * */
  public static double[] sequentialMatrixVectorMultiplication(double[][] matrix, double[] vector) {
    int rows = matrix.length;
    int cols = matrix[0].length;

    double[] vector_res = new double[rows];

    if (rows != cols) {
      throw new ArithmeticException("Invalid Matrix dimensions");
    }

    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        vector_res[row] = matrix[row][col] * vector[col];
      }
    }
    return vector_res;
  }

  /**
   * Code taken from Assignment 1
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

  /**
   * Code taken from Assignment 1
	 * Populates a matrix of given size with randomly generated integers between 0-10.
	 * @param numRows number of rows
	 * @param numCols number of cols
	 * @return matrix
	 */
	private static double[] generateRandomVector (int numRows) {
		double vector[] = new double[numRows];
		for (int row = 0 ; row < numRows ; row++ ) {
				vector[row] = (double) ((int) (Math.random() * 10.0));
		}
		return vector;
	}
}
