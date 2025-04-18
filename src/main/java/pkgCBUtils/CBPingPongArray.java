package pkgCBUtils;

import java.util.List;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CBPingPongArray {

	private int [][] liveArr;
	private int [][] nextArr;

	private int ROWS,
			COLS,
			RAND_MIN,
			RAND_MAX,
			DEFAULT_VALUE = 99;

	protected CBPingPongArray(int numRows, int numCols) {
		ROWS = numRows;
		COLS = numCols;

		nextArr = new int[ROWS][COLS];
		liveArr = new int[ROWS][COLS];

		for (int i = 0; i < ROWS; i++) {
			Arrays.fill(nextArr[i], DEFAULT_VALUE);
			Arrays.fill(liveArr[i], DEFAULT_VALUE);
		}

	}

	public CBPingPongArray(int numRows, int numCols, int randMin, int randMax) {
		this(numRows, numCols);

		Random rand = new Random();

		for (int r = 0; r < ROWS; r++) {
			for (int c = 0; c < COLS; c++) {
				int val = rand.nextInt(randMin, randMax);
				liveArr[r][c] = val;
				nextArr[r][c] = val;
			}
		}

		randomize();
	}

	// Gets the value at the normalized index for the 2d array given a 1d index
	private int getValueAt1dIndex(final int[][] arr, int index) {
		return arr[index / ROWS][index % COLS];
	}

	// Sets the value at the normalized index in the 2d array given a 1d index
	private void setValueAt1dIndex(int[][] arr, int index, int value) {
		arr[index / ROWS][index % COLS] = value;
	}

	public void randomize() {
		Random rand = new Random();
		int length = ROWS * COLS;

		copyToNextArray();

		//Not converting to a 1d array. Instead, will be treating index as a 1d index and converting to 2d indices.
		for (int i = 0; i < length; i++) {
			int j = rand.nextInt(length);

			int temp = getValueAt1dIndex(nextArr, i);
			setValueAt1dIndex(nextArr, i, getValueAt1dIndex(nextArr, j));
			setValueAt1dIndex(nextArr, j, temp);
		}

	}

	public void randomizeInRange() {
		Random rand = new Random();
		List<Integer> values = IntStream.range(0, ROWS * COLS).boxed().collect(Collectors.toList());

		int length = ROWS * COLS;
		for (int i = 0; i < length; i++) {
			int index = rand.nextInt(values.size());
			int value = values.remove(index);

			setValueAt1dIndex(nextArr, i, value);
		}
	}

	public void randomizeViaFisherYatesKnuth() {
		// Convert driver file call to hw specification
		randomize();
	}

	public void save(String fileName) {

		File file = new File(fileName);

		if (!file.exists()) {
			try {
				if (!file.createNewFile())
					throw new IOException("Could not create file " + fileName);
			} catch (IOException e) {
				System.err.printf("Could not create save file \"%s\"!\n", fileName);
				e.printStackTrace();
				return;
			}
		}

		// Auto clean up so I don't have to close by using this sytle of try-catch
		try (BufferedWriter outputStream = new BufferedWriter(new FileWriter(fileName))) {

			outputStream.write(String.format("%d\n%d %d\n", DEFAULT_VALUE, ROWS, COLS));

			for (int r = 0; r < ROWS; r++) {
				outputStream.write(String.format("%d  ", r));
				for (int c = 0; c < COLS; c++) {
					outputStream.write(String.format("%d ", liveArr[r][c]));
				}
				outputStream.write("\n");
			}


			outputStream.flush();
		} catch (IOException e) {
			System.err.printf("Could not save file \"%s\"!\n", fileName);
			e.printStackTrace();
			return;
		}
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

			Scanner rowScanner = new Scanner(scanner.nextLine());

			int col = 0;
			while (rowScanner.hasNextInt()) {
				nextArr[row][col++] = rowScanner.nextInt();
			}

			for (; col < COLS; col++) {
				nextArr[row][col] = DEFAULT_VALUE;
			}
			rowScanner.close();
		}

		scanner.close();
	}

	public record RCPair(int row, int col) {}

	public RCPair[] getNearestNeighborsArray(int orgRow, int orgCol) {
		RCPair[] neighbors = new RCPair[8];
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
		for (int i = 0; i < ROWS * COLS; i++) {
			setValueAt1dIndex(nextArr, i, getValueAt1dIndex(liveArr, i));
		}
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
		for (int row = 0; row < ROWS; row++) {
			System.out.printf(row + "  ");
			for (int col = 0; col < COLS; col++) {
				System.out.printf(liveArr[row][col] + " ");
			}
			System.out.println();
		}
	}

	public void swapLiveAndNext() {
		int[][] temp = liveArr;
		liveArr = nextArr;
		nextArr = temp;
	}


}
