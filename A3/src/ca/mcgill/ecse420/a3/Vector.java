package ca.mcgill.ecse420.a3;

public class Vector {
  int dimension;
  double[] data;
  int rowDisplace;

  public Vector(int d) {
    dimension = d;
    rowDisplace = 0;
    data = new double[d];
  }

  Vector(double[] matrix, int x, int d) {
    data = matrix;
    rowDisplace = x;
    dimension = d;
  }

  public double get(int row) {
    return data[row + rowDisplace];
  }

  public void set(int row, double value) {
    data[row + rowDisplace] = value;
  }

  public int getDimension() {
    return dimension;
  }

  public Vector[] split() {
    Vector[] result = new Vector[2];
    int newDimension = dimension / 2;
    result[0] = new Vector(data, rowDisplace, newDimension);
    result[1] = new Vector(data, rowDisplace + newDimension, newDimension);
    return result;
  }

  /**
   * Code taken from Assignment 1
	 * Populates a matrix of given size with randomly generated integers between 0-10.
	 * @param numRows number of rows
	 * @param numCols number of cols
	 * @return matrix
	 */
	public void generateRandomVector() {
		for (int row = 0 ; row < dimension ; row++ ) {
				data[row] = (double) ((Math.random() * 10.0));
		}
	}

  /**
	 * Prints content of a Vector
	 * @params None
	 * @return None
	 */
	public void printVector() {
		for (int row = 0 ; row < dimension ; row++ ) {
				System.out.print("| " + data[row] + " |\n");
		}
	}
}
