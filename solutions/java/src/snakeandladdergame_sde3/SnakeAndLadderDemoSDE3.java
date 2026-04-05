package snakeandladdergame_sde3;

import java.util.Arrays;

public class SnakeAndLadderDemoSDE3 {
    public static void main(String[] args) {
        new PlayerNotificationService(); // Spawn listeners

        Player p1 = new Player("Player_1");
        GameController controller = new GameController(Arrays.asList(p1));
        
        controller.startGame();

        System.out.println("Rolling 6...");
        controller.processMove(p1, 6);
        
        System.out.println("Rolling 94 (to win)...");
        controller.processMove(p1, 94);
    }
}
