package ca.mcgill.ecse420.a3;

public class TestMatrixVectorMulitply {

	private static final int NUMBER_THREADS = 3;
	private static final int MATRIX_SIZE = 2;

	public static void main(String[] args) {
		double[][] matrix = generateRandomMatrix(MATRIX_SIZE, MATRIX_SIZE);
		double[] vector = generateRandomVector(MATRIX_SIZE);

    for (int row = 0 ; row < MATRIX_SIZE  ; row++ ) {
			for (int col = 0 ; col < MATRIX_SIZE ; col++ ) {
				System.out.print(matrix[row][col] + " ");
			}
      System.out.println();
		}

    for (int row = 0 ; row < vector.length ; row++ ) {
      System.out.print("| " + vector[row] + " |\n");
    }

		ParallelMatrixVectorMuilt2 par = new ParallelMatrixVectorMuilt2(matrix, vector, MATRIX_SIZE, NUMBER_THREADS);
		double start = System.currentTimeMillis();
		par.parallelMultiplication();
		double end = System.currentTimeMillis();
		System.out.println("Parallel: " + (end-start)/1000 + "s");
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

    /**
     * Populates a vector of given size with randomly generated integers between 0-10.
     * @param numCols number of cols
     * @return vector
     */
    private static double[] generateRandomVector (int numCols) {
        double vector[] = new double[numCols];
        for (int col = 0 ; col < numCols ; col++ ) {
            vector[col] = (double) ((int) (Math.random() * 10.0));
        }
        return vector;
    }
}
