package game.gamemanager;

public class UserScore {
    private final String username;
    private int score;

    public UserScore(String username, int score) {
        this.username = username;
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }
    public void setScore(int newScore) {
        score = newScore;
    }
}
