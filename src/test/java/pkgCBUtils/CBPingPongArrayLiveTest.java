package pkgCBUtils;

import java.util.Arrays;

public class CBPingPongArrayLiveTest {
	public static void main(String[] args) {
		printResult("ult_a", ult_a());
		printResult("ult_b", ult_b());
		printResult("ult_c", ult_c());
		printResult("ult_d", ult_d());
	}

	private static void printResult(String method, boolean result) {
		System.out.printf("%s: %s\n", method, result ? "PASS" : "FAIL");
	}

	private static boolean ult_a() {
		CBPingPongArrayLive arrayA = new CBPingPongArrayLive(7, 7, 0);
		arrayA.loadFile("ult_a_input.txt");
		arrayA.swapLiveAndNext();


		CBPingPongArrayLive arrayB = new CBPingPongArrayLive(7, 7, 0);
		arrayB.loadFile("ult_a_verify.txt");
		arrayB.swapLiveAndNext();

		final int[][] readOnlyB = arrayB.getArray();
		for (int r = 0; r < 7; r++) {
			for (int c = 0; c < 7; c++) {
				arrayB.set(r, c, readOnlyB[r][c] == 0 ? 1 : 0);
			}
		}
		arrayB.swapLiveAndNext();

		return Arrays.deepEquals(arrayA.getArray(), arrayB.getArray());
	}

	private static boolean ult_b() {
		CBPingPongArrayLive arr = new CBPingPongArrayLive(5, 5, 0);
		arr.swapLiveAndNext();

		int[][] bArr = new int[5][5];
		for (int r = 0; r < 5; r++) {
			Arrays.fill(bArr[r], 0);
		}

		return Arrays.deepEquals(arr.getArray(), bArr);
	}

	private static boolean ult_c() {
		CBPingPongArrayLive arr = new CBPingPongArrayLive(5, 5, 0);
		arr.set(0, 0, 127);

		int[][] values = arr.getArray();
		if (values[0][0] == 127)
			return false;

		arr.swapLiveAndNext();

		values = arr.getArray();
		if (values[0][0] != 127)
			return false;

		return true;
	}

	private static boolean ult_d() {
		CBPingPongArrayLive arr = new CBPingPongArrayLive(5, 5, 10);
		arr.swapLiveAndNext();

		// It was not clear from the assignment instructions how we are supposed to test randomization.

		for (int i = 0; i < 5; i++) {
			// Verify the amount of live cells is the same after
			arr.randomizeViaFisherYatesKnuth();
			arr.swapLiveAndNext();

			int aliveCells = 0;
			final int[][] arrayValues = arr.getArray();
			for (int r = 0; r < 5; r++) {
				for (int c = 0; c < 5; c++) {
					if (arrayValues[r][c] == 1) {
						aliveCells++;
					}
				}
			}

			if (aliveCells != 10)
				return false;
		}

		return true;
	}

}
