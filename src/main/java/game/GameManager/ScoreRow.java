package game.gamemanager;

public class ScoreRow {

    private final String snakeUser;
    private final int snakeScore;
    private final String blackjackUser;
    private final int blackjackScore;

    public ScoreRow(String snakeUser, int snakeScore, String blackjackUser, int blackjackScore) {
        this.snakeUser = snakeUser;
        this.snakeScore = snakeScore;
        this.blackjackUser = blackjackUser;
        this.blackjackScore = blackjackScore;
    }

    public String getSnakeUser() {
        return snakeUser;
    }
    public int getSnakeScore() {
        return snakeScore;
    }

    public String getBlackjackUser() {
        return blackjackUser;
    }

    public int getBlackjackScore() {
        return blackjackScore;
    }
}
