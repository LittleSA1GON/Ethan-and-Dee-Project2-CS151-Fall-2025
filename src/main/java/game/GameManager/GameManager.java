package game.gamemanager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GameManager extends Application{

    private Stage primaryStage;
    private Scene currScene;
    private Path userAccountsFilePath = Paths.get("user_accounts.txt");
    private Path highScoresFilePath = Paths.get("high_scores.txt");

    
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
            createAndGoToLoginScreenScene(false);
        });
        firstScreenSignUpButton.setOnAction(event -> {
            createAndGoToCreateAccountScreenScene();
        });
    }

    public void createAndGoToLoginScreenScene(boolean comehereAfterRegistration){
        this.primaryStage.setTitle("User Login");
        LoginScreen loginScreen = new LoginScreen();
        StackPane loginScreenRootNode = loginScreen.getRootNode();
        currScene.setRoot(loginScreenRootNode); //Not create a new scene but switch root

        if(comehereAfterRegistration){
            loginScreen.addSuccessfulRegistrationToVBox(); //since user goes to the login page after successful registeration 
        }
        attachListenersToLoginScreenButton(loginScreen);
        /* TODO: if have time, add 1 more boolean and if not the first time logging in after registering, turn the label off, not here */
        //loginScreen.getSuccessfulLabel().setVisible(false);
        //loginScreen.getSuccessfulLabel().setManaged(false);
        
    }

    public void createAndGoToCreateAccountScreenScene(){
        this.primaryStage.setTitle("Create an Account");
        CreateAccountScreen createAccountScreen = new CreateAccountScreen();
        StackPane createAccountScreenRootNode = createAccountScreen.getRootNode();
        currScene.setRoot(createAccountScreenRootNode);

        attachListnersToCreateAccountScreenButton(createAccountScreen);
    }

    public void createAndGoToMainMenuScreenScene(String username){
        this.primaryStage.setTitle("Main Menu");
        MainMenuScreen menuScreen = new MainMenuScreen(highScoresFilePath, username);
        BorderPane menuScreenRootNode = menuScreen.getRootNode();
        currScene.setRoot(menuScreenRootNode);

        //TODO: attach listener to play games (2 buttons), main menu buttons
    }

    public void attachListnersToCreateAccountScreenButton(CreateAccountScreen createAccountScreen){
        createAccountScreen.getCreateAccountButton().setOnAction(event -> {
            String usernameString = createAccountScreen.getUsernameTF().getText().trim();
            String pwdString = createAccountScreen.getPwdTF().getText().trim();
            Label userDoesSomethingWrongLabel = createAccountScreen.getUserDoesSomethingWrongLabel();

            if(!createAccountScreen.isValidUsername(usernameString, userAccountsFilePath)){
                userDoesSomethingWrongLabel.setVisible(true);
                userDoesSomethingWrongLabel.setManaged(true);
                return;
            }
            if(!createAccountScreen.isValidPwd(pwdString, usernameString)){
                userDoesSomethingWrongLabel.setVisible(true);
                userDoesSomethingWrongLabel.setManaged(true);
                return;
            }

            try{
                Files.writeString(userAccountsFilePath, usernameString + ":" + pwdString + "\n", StandardOpenOption.APPEND, StandardOpenOption.CREATE);
                Files.writeString(highScoresFilePath, usernameString + ":1000:1000:1000:1000:1000:1000:1000:1000:1000:1000" + "\n" , StandardOpenOption.APPEND, StandardOpenOption.CREATE);
            }catch (IOException e){
                userDoesSomethingWrongLabel.setText("Error: " + e.getMessage());
                userDoesSomethingWrongLabel.setVisible(true);
                userDoesSomethingWrongLabel.setManaged(true);
                return;
            }
            
            createAndGoToLoginScreenScene(true);//if there were no errors along the way, this is the step to call loginScreen 
        });
    }

    public void attachListenersToLoginScreenButton(LoginScreen loginScreen){
        loginScreen.getLoginButton().setOnAction(event -> {
            String usernameString = loginScreen.getUsernameTF().getText().trim();
            String pwdString = loginScreen.getPasswordTF().getText().trim();
            Label userDoesSomethingWrongLabel = loginScreen.getUserDoesSomethingWrongLabel();

            userDoesSomethingWrongLabel.setVisible(false);
            userDoesSomethingWrongLabel.setManaged(false);

            if(checkLoggingInUsername(usernameString, pwdString, userDoesSomethingWrongLabel)){
                createAndGoToMainMenuScreenScene(usernameString);
            }
            else{
                userDoesSomethingWrongLabel.setVisible(true);
                userDoesSomethingWrongLabel.setManaged(true);
            }
        });
    }

    public boolean checkLoggingInUsername(String username, String pwd, Label userDoesSomethingWrongLabel){
        if(Files.exists(userAccountsFilePath) && Files.isReadable(userAccountsFilePath)){
            try{
                List<String> lines = Files.readAllLines(userAccountsFilePath);
                for(String line : lines){
                    String[] parts = line.split(":", 2); //username = parts[0], password = parts[1]
                    if(username.equals(parts[0])){
                        if(pwd.equals(parts[1])){
                            //TODO: use this user's info to go to his main menu
                            return true;
                        }
                        else{
                            userDoesSomethingWrongLabel.setText("Incorrect Username or password"); //wrong password, but use generic message for security reasons
                            return false; //can return (unique usernames)
                        }
                    }
                }
                userDoesSomethingWrongLabel.setText("No such user exists!");
                return false;

            }catch(IOException e){
                userDoesSomethingWrongLabel.setText("Error: " + e.getMessage());
                return false;
            }
        }
        else{
            userDoesSomethingWrongLabel.setText("No such user exists!"); //no file exists -> no user has registered yet
                return false;
        }
    }

    public static void main(String[] args){
        launch();
    }
}
