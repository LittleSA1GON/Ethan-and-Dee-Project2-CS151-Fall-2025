package game.gamemanager;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GameManager extends Application{

    private Stage primaryStage;
    private Scene currScene;
    
    @Override
    public void start(Stage stage){
        this.primaryStage = stage;

        FirstScreen firstScreen = new FirstScreen();
        StackPane firstScreenRootNode = firstScreen.getRootNode();
        
        currScene = new Scene(firstScreenRootNode, 800, 600);
        primaryStage.setTitle("Welcome Page");
        primaryStage.setScene(currScene);

        attachListenersToFirstScreenButtons(firstScreen.getLoginButton(), firstScreen.getCreateAccountButton());

       

       
        primaryStage.show();
        
    }

    public void attachListenersToFirstScreenButtons(Button firstScreenLoginButton, Button firstScreenSignUpButton){
        firstScreenLoginButton.setOnAction(event -> {
            createAndGoToLoginScreenScene();
            this.primaryStage.setTitle("User Login");
        });

        firstScreenSignUpButton.setOnAction(event -> {
            createAndGoToCreateAccountScreenScene();
            this.primaryStage.setTitle("Create an Account");
        });
    }

    public void createAndGoToLoginScreenScene(){
        LoginScreen loginScreen = new LoginScreen();
        StackPane loginScreenRootNode = loginScreen.getRootNode();
        currScene.setRoot(loginScreenRootNode); //Not create a new scene but switch root
    }

    public void createAndGoToCreateAccountScreenScene(){
        CreateAccountScreen createAccountScreen = new CreateAccountScreen();
        StackPane createAccountScreenRootNode = createAccountScreen.getRootNode();
        currScene.setRoot(createAccountScreenRootNode);
    }

    public static void main(String[] args){
        launch();
    }
}
