package pkgCSC133;

import pkgCBUtils.CBPingPongArray;

public class Driver {
    public static void main(String[] my_args) {
        final int ROWS = 7, COLS = 7;
        int myMin = 10, myMax = 20;
        CBPingPongArray myBoard = new CBPingPongArray(ROWS, COLS, myMin, myMax);
        myBoard.swapLiveAndNext();
        System.out.println("[10, 20) Board:");
        myBoard.printArray();

        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                myBoard.setCell(row, col, col);
            }  //  for(int col = 0; col < COLS; ++col)
        }  //  for(int row = 0; row < ROWS; ++row)
        myBoard.swapLiveAndNext();
        System.out.println("\n[0, COLS) Board:");
        myBoard.printArray();

        myBoard.randomizeViaFisherYatesKnuth();
        myBoard.swapLiveAndNext();
        System.out.println("\n[0, COLS) Board Randomized via FYK algorithm:");
        myBoard.printArray();

        myBoard.loadFile("neighbors_test.txt");
        System.out.println("\n[0, 1] data file array:");
        myBoard.swapLiveAndNext();
        myBoard.printArray();

        myBoard.updateToNearestNNSum();
        myBoard.swapLiveAndNext();
        System.out.println("\nNearest Neighbor sum array:");
        myBoard.printArray();
        myBoard.save("test_sum.txt");

    }  //  public static void main(String[] my_args)
}  //  public class Driver








