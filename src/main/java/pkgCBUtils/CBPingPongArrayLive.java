package pkgCBUtils;

public class CBPingPongArrayLive extends CBPingPongArray {

	private static final int DEAD = 0;
	private static final int ALIVE = 1;

	public CBPingPongArrayLive(int numRows, int numCols, int numLiveCells) {
		super(numRows, numCols);

		for (int r = 0, cellsLeft = numLiveCells; r < numRows; r++) {
			for (int c = 0; c < numCols; c++, cellsLeft--) {
				set(r, c, cellsLeft > 0 ? ALIVE : DEAD);
			}
		}
		swapLiveAndNext();
		randomizeViaFisherYatesKnuth();
	}

	public int countLiveDegreeTwoNeighbors(int row, int col) {
		return 0; // Default value 0 per assignment specification
	}

}
