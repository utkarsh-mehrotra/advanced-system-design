package chessgame_sde2;

public class ChessGameDemoUpgraded {
    public static void main(String[] args) {
        Player p1 = new Player("Magnus", Color.WHITE);
        Player p2 = new Player("Hikaru", Color.BLACK);

        GameEngine engine = new GameEngine(p1, p2);

        System.out.println("--- SDE3 Pathing & Mathematics Rules Engine Demo ---");
        
        System.out.println("\n1. Obstructed Piece Test: Trying to move Rook [0,0] straight through a Pawn.");
        boolean blockedResult = engine.processMove(p1, 0, 0, 3, 0); // Fails
        
        System.out.println("\n2. Fool's Mate Execution (Fastest Checkmate):");
        engine.processMove(p1, 1, 5, 2, 5); // White Pawn F
        engine.processMove(p2, 6, 4, 4, 4); // Black Pawn E
        
        engine.processMove(p1, 1, 6, 3, 6); // White Pawn G
        engine.processMove(p2, 7, 3, 3, 7); // Black Queen strikes H4!
        
        // At this point, Magnus is in Checkmate! The System should accurately detect the mathematical trap.
        // Let's force Magnus to make an illegal move attempting to ignore the Check state.
        System.out.println("\n3. Post-Checkmate Guard Test:");
        boolean finalMathTest = engine.processMove(p1, 1, 0, 2, 0); // Fails because Game State is Black_Win!
    }
}
