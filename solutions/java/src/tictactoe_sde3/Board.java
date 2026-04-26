package tictactoe_sde3;

public class Board {
    private final char[][] grid = new char[3][3];

    public Board() {
        for(int i=0; i<3; i++) for(int j=0; j<3; j++) grid[i][j] = '-';
    }

    // Encapsulated strict modification
    public boolean placePiece(int row, int col, char piece) {
        if (row < 0 || row > 2 || col < 0 || col > 2 || grid[row][col] != '-') {
            return false;
        }
        grid[row][col] = piece;
        return true;
    }

    public void printBoard() {
        for(int i=0; i<3; i++) {
            System.out.println(grid[i][0] + " | " + grid[i][1] + " | " + grid[i][2]);
        }
    }
}
