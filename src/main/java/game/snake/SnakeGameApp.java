package game.snake;

import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SnakeGameApp extends Application{

    @Override 
    public void start(Stage primaryStage){
        
        SnakeGame snakeGame = new SnakeGame(Paths.get("high_scores.txt"), null, null);
        
        snakeGame.startGame();

        Scene scene = new Scene(snakeGame.getRootNode(), 800, 600);

        primaryStage.setTitle("Snake Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        snakeGame.getRootNode().requestFocus(); //give focus to snakeGame's rootnode to handle events on it
    }//end of start

    public static void main(String[] args){
        launch();
    }
}