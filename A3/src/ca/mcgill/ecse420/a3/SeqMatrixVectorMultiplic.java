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
        vector_res[row] += matrix[row][col] * vector[col];
      }
    }
    return vector_res;
  }
}
