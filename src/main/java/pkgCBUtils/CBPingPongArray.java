package pkgCBUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class CBPingPongArray {

	private int [][] liveArr;
	private int [][] nextArr;

	private int ROWS,
			COLS,
			RAND_MIN,
			RAND_MAX,
			DEFAULT_VALUE = 99;


	public CBPingPongArray(int numRows, int numCols, int randMin, int randMax) {
		ROWS = numRows;
		COLS = numCols;

		RAND_MIN = randMin;
		RAND_MAX = randMax;

		liveArr = new int[ROWS][COLS];
		nextArr = new int[ROWS][COLS];

		for (int r = 0; r < ROWS; r++) {
			for (int c = 0; c < COLS; c++) {
				liveArr[r][c] = DEFAULT_VALUE;
				nextArr[r][c] = DEFAULT_VALUE;
			}
		}

		randomize();
	}

	public void randomize() {
		Random rand = new Random();
		int length = ROWS * COLS;
		//Not converting to a 1d array. Instead, will be treating index as a 1d index and converting to 2d indices.
		for (int i = 0; i < length; i++) {
			int currentRow = i / COLS;
			int currentCol = i % COLS;

			int j = rand.nextInt(length);

			int newRow = j / COLS;
			int newCol = j % COLS;

			nextArr[currentRow][currentCol] = liveArr[newRow][newCol];
		}

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
		Scanner scanner;

		try {
			scanner = new Scanner(new File(dataFileName));
		} catch (FileNotFoundException e) {
			System.err.println("File not found: " + dataFileName);
			return;
		}

		DEFAULT_VALUE = scanner.nextInt();
		ROWS = scanner.nextInt();
		COLS = scanner.nextInt();

		for (int row = 0; row < ROWS; row++) {
			// ignores the row header
			scanner.nextInt();
			for (int col = 0; col < COLS; col++) {
				nextArr[row][col] = scanner.nextInt();
			}
		}
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
