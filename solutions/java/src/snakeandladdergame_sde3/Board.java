package snakeandladdergame_sde3;

public class Board {
    private static final int BOARD_SIZE = 100;
    private final int[] jumpBoard; // O(1) jump lookup instead of O(N) list scans

    public Board() {
        jumpBoard = new int[BOARD_SIZE + 1]; // 1-indexed to 100
        initializeBoard();
    }

    private void initializeBoard() {
        // Initialize snakes
        addJump(16, 6);
        addJump(48, 26);
        addJump(64, 60);
        addJump(93, 73);

        // Initialize ladders
        addJump(1, 38);
        addJump(4, 14);
        addJump(9, 31);
        addJump(21, 42);
        addJump(28, 84);
        addJump(51, 67);
        addJump(80, 99);
    }

    private void addJump(int start, int end) {
        jumpBoard[start] = end;
    }

    public int getBoardSize() {
        return BOARD_SIZE;
    }

    public int getNewPositionAfterSnakeOrLadder(int position) {
        if (position > BOARD_SIZE) return position;
        int jumpDestination = jumpBoard[position];
        return jumpDestination == 0 ? position : jumpDestination;
    }
}
