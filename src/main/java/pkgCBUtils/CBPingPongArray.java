package pkgCBUtils;

import java.util.Arrays;

public class CBPingPongArray {

	private int [][] liveArr;
	private int [][] nextArr;

	private final int ROWS,
			COLS,
			RAND_MIN,
			RAND_MAX;

	private static final int DEFAULT_VALUE = 99;

	public CBPingPongArray(int numRows, int numCols, int randMin, int randMax) {
		ROWS = numRows;
		COLS = numCols;

		RAND_MIN = randMin;
		RAND_MAX = randMax;

		liveArr = new int[ROWS][COLS];
		nextArr = new int[ROWS][COLS];

		randomize();
		//todo: complete method
	}

	public void randomize() {
		//todo: use fisher-yates-knuth alrogithm to randomize. Don't replace values
	}

	public void randomizeInRange() {
		//
	}

	public void randomizeViaFisherYatesKnuth() {
		// Convert driver file call to hw specification
		randomize();
	}

	public void save(String fileName) {
		//todo: save LIVE array to file
	}

	public void save() {
		// Ik this is a magic string but this is the ONLY place this is ever used.
		save("ppa_data.txt");
	}


	public void loadFile(String dataFileName) {
		//todo: load file
	}

	public record RCPair(int row, int col) {}

	public RCPair[] getNearestNeighborsArray(int orgRow, int orgCol) {
		RCPair[] neighbors = new RCPair[9];
		int index = 0;
		for (int row = -1; row <= 1; row++) {
			for (int col = -1; col <= 1; col++) {

				if (row == 0 && col == 0)
					continue;

				int neighborRow = (orgRow + row + ROWS) % ROWS;
				int neighborCol = (orgCol + col + COLS) % COLS;

				neighbors[index++] = new RCPair(neighborRow, neighborCol);
			}
		}
		return neighbors;
	}

	public int[][] getArray() {
		return Arrays.copyOf(liveArr, liveArr.length);
	}

	public void copyToNextArray() {
		System.arraycopy(liveArr, 0, nextArr, 0, liveArr.length);
	}

	public void set(int row, int col, int newValue) {
		nextArr[row][col] = newValue;
	}

	public void setCell(int row, int col, int newValue) {
		// Convert driver file call to hw specification
		set(row, col, newValue);
	}

	public void updateToNNSum() {
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				int sum = 0;
				for (RCPair neighbor : getNearestNeighborsArray(row, col)) {
					sum += liveArr[neighbor.row][neighbor.col];
				}
				nextArr[row][col] = sum;
			}
		}
	}

	public void updateToNearestNNSum() {
		// Convert driver file call to hw specification
		updateToNNSum();
	}

	public void printArray() {
		//todo: print array
	}

	public void swapLiveAndNext() {
		int[][] temp = liveArr;
		liveArr = nextArr;
		nextArr = temp;
	}


}
