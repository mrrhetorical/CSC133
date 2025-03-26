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

	public int[][] getArray() {
		return Arrays.copyOf(liveArr, liveArr.length);
	}

	public void copyToNextArray() {
		//todo: unsure what implementation should do
	}

	public void set(int row, int col, int newValue) {
		//todo: update next array at row/col with value
	}

	public void setCell(int row, int col, int newValue) {
		// Convert driver file call to hw specification
		set(row, col, newValue);
	}

	public void updateToNNSum() {
		//todo: update nextArray to the sum of the nearest neighbor elements in the liveArray
	}

	public void updateToNearestNNSum() {
		// Convert driver file call to hw specification
		updateToNNSum();
	}

	public void printArray() {
		//todo: print array
	}

	public void swapLiveAndNext() {

	}


}
