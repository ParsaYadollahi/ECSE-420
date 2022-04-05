package ca.mcgill.ecse420.a3;

public class Matrix {
  int dimension;
  double[][] data;
  int rowDisplace, colDisplace;

  public Matrix(int d) {
    dimension = d;
    rowDisplace = 0;
    colDisplace = 0;
    data = new double[d][d];
  }

  Matrix(double[][] matrix, int x, int y, int d) {
    data = matrix;
    rowDisplace = x;
    colDisplace = y;
    dimension = d;
  }

  public double get(int row, int col) {
    return data[row + rowDisplace][col + colDisplace];
  }

  public void set(int row, int col, double val) {
    data[row + rowDisplace][col + colDisplace] = val;
  }

  public int getDimension() {
    return dimension;
  }

  public Matrix[][] split() {
    Matrix[][] result = new Matrix[2][2];
    int newDimension = dimension / 2;
    result[0][0] = new Matrix(data, rowDisplace, colDisplace, newDimension);
    result[0][1] = new Matrix(data, rowDisplace, colDisplace + newDimension, newDimension);
    result[1][0] = new Matrix(data, rowDisplace + newDimension, colDisplace, newDimension);
    result[1][1] = new Matrix(data, rowDisplace + newDimension, colDisplace + newDimension, newDimension);
    return result;
  }

  /**
   * Code taken from Assignment 1
	 * Populates a matrix of given size with randomly generated integers between 0-10.
	 * @param numRows number of rows
	 * @param numCols number of cols
	 * @return matrix
	 */
	public void generateRandomMatrix() {
		for (int row = 0 ; row < dimension  ; row++ ) {
			for (int col = 0 ; col < dimension ; col++ ) {
				data[row][col] = (double) ((int) (Math.random() * 10.0));
			}
		}
	}
}
