package game.gamemanager;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GameManager extends javafx.application.Application{
    
    @Override
    public void start(Stage primaryStage){
        LoginScreen loginScreen = new LoginScreen();

        StackPane loginScreenRootNode = loginScreen.getRootNode();

        Scene loginScene = new Scene(loginScreenRootNode, 800, 600);

        primaryStage.setTitle("Welcome Page");
        primaryStage.setScene(loginScene);
        primaryStage.show();
        
    }

    public static void main(String[] args){
        launch();
    }
}
