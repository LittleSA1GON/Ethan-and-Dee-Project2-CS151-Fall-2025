package game.snake;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SnakeGameApp extends Application{

    @Override 
    public void start(Stage primaryStage){
        
        SnakeGame snakeGame = new SnakeGame();
        
        snakeGame.startGame();

        Scene scene = new Scene(snakeGame.getRootNode(), 800, 600);

        primaryStage.setTitle("Snake Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }//end of start

    public static void main(String[] args){
        launch();
    }
}