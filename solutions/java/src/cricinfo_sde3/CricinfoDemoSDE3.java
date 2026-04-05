package cricinfo_sde3;

public class CricinfoDemoSDE3 {
    public static void main(String[] args) {
        new LiveScoreWebSocket(); // Activate Fan-Out

        ScoreController controller = new ScoreController();
        Match m1 = new Match("IND_VS_AUS");
        controller.registerMatch(m1);

        System.out.println("Match starting...");
        m1.setState(MatchState.IN_PROGRESS);

        System.out.println("Umpire signals 6 runs!");
        controller.updateScore("IND_VS_AUS", 6);
        
        System.out.println("Direct Volatile Read by a stray client: " + m1.getCurrentScore());
    }
}
