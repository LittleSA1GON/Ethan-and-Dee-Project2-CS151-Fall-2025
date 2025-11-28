package game.GameManager;

public class ScoreRow {
    private int snakeGameScore;
    private int blackjackScore;

    public ScoreRow(int snakeGameScore, int blackjackScore){
        this.snakeGameScore = snakeGameScore;
        this.blackjackScore = blackjackScore;
    }
    public Integer getSnakeGameScore(){ //javafx TableView's requires a getter that returns Integer (a reference type)
        return this.snakeGameScore; //autoboxing int -> Integer
    }
    public Integer getBlackjackScore(){
        return this.blackjackScore;
    }
}
