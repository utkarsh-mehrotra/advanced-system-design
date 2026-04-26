package cricinfo_sde3;

public class Ball {
    private final int ballNumber;
    private final Player bowler;
    private final Player batsman;
    private final String result;

    public Ball(int ballNumber, Player bowler, Player batsman, String result) {
        this.ballNumber = ballNumber;
        this.bowler = bowler;
        this.batsman = batsman;
        this.result = result;
    }

    public int getBallNumber() {
        return ballNumber;
    }

    public Player getBowler() {
        return bowler;
    }

    public Player getBatsman() {
        return batsman;
    }

    public String getResult() {
        return result;
    }
}
