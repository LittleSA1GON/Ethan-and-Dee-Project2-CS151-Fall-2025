package game.gamemanager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

        attachListnersToCreateAccountScreenButton(createAccountScreen);
    }

    public void attachListnersToCreateAccountScreenButton(CreateAccountScreen createAccountScreen){
        createAccountScreen.getCreateAccountButton().setOnAction(event -> {
            String usernameString = createAccountScreen.getUsernameTF().getText().trim();
            String pwdString = createAccountScreen.getPwdTF().getText().trim();
            Label userDoesSomethingWrongLabel = createAccountScreen.getUserDoesSomethingWrongLabel();
            Path userAccountsFilePath = createAccountScreen.getUserAccountsFilePath();

            if(!createAccountScreen.isValidUsername(usernameString)){
                userDoesSomethingWrongLabel.setVisible(true);
                userDoesSomethingWrongLabel.setManaged(true);
                return;
            }

            if(!createAccountScreen.isValidPwd(pwdString, usernameString)){
                userDoesSomethingWrongLabel.setVisible(true);
                userDoesSomethingWrongLabel.setManaged(true);
                return;
            }

            userDoesSomethingWrongLabel.setVisible(false);
            userDoesSomethingWrongLabel.setManaged(false);

            try{
                Files.writeString(userAccountsFilePath, usernameString + ":" + pwdString + "\n", StandardOpenOption.APPEND, StandardOpenOption.CREATE);
            }catch (IOException e){
                userDoesSomethingWrongLabel.setText("Error: " + e.getMessage());
                userDoesSomethingWrongLabel.setVisible(true);
                userDoesSomethingWrongLabel.setManaged(true);
                return;
            }
            //if there were no errors along the way, this is the step to call loginScreen 
        });
    }

    public static void main(String[] args){
        launch();
    }
}
