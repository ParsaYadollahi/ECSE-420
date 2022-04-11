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
  public static Vector multiply(Matrix matrix, Vector vector) {
    int rows = matrix.dimension;
    int cols = vector.dimension;

    Vector vector_res = new Vector(rows);

    if (rows != cols) {
      throw new ArithmeticException("Invalid Matrix dimensions");
    }

    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        vector_res.set(row, vector_res.get(row) + matrix.get(row, col) * vector.get(col));
      }
    }
    return vector_res;
  }
}
