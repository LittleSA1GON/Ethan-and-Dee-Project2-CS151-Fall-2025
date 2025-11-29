package game.gamemanager;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import game.snake.SnakeGame;
import game.blackjack.gamelogic.BlackJackApp;
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
    private Path userAccountsFilePath = Paths.get("txtfiles", "users.txt");
    private Path highScoresFilePath = Paths.get("txtfiles", "high_scores.txt");
    private ToolBarC toolBarC;

    //private int[] snakeGameScores; 
    //private int[] blackjackScores;

    @Override
    public void start(Stage stage){
        this.primaryStage = stage;

        FirstScreen firstScreen = new FirstScreen();
        StackPane firstScreenRootNode = firstScreen.getRootNode();
        
        currScene = new Scene(firstScreenRootNode, 800, 600);
        primaryStage.setTitle("Welcome Page");
        primaryStage.setScene(currScene);
        
        // Start GameManager music on application startup
        MusicManager.playGameManagerMusic();

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
        currScene.setRoot(loginScreenRootNode); //Not create a new scene but switch root (to use the same toolbar)

        if(comehereAfterRegistration){
            loginScreen.addSuccessfulRegistrationToVBox(); //since user goes to the login page after successful registeration 
        }
        attachListenersToLoginScreenButton(loginScreen);
        attachBackButtonListenerToLoginScreen(loginScreen);
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
        attachBackButtonListenerToCreateAccount(createAccountScreen);
    }

    public void createAndGoToMainMenuScreenScene(String username){
        this.primaryStage.setTitle("Main Menu");
        MusicManager.stopAllMusic();
        MusicManager.playGameManagerMusic();
        if(this.toolBarC == null){
            createAToolBar(username);
        }
        MainMenuScreen menuScreen = new MainMenuScreen(highScoresFilePath, username, this.toolBarC);
        BorderPane menuScreenRootNode = menuScreen.getRootNode();
        currScene.setRoot(menuScreenRootNode);

        //TODO: attach listener to "Play Blackjack" Button
        attachListenersToMainMenuScreenButtons(menuScreen, username, menuScreen.getSnakeGameScores(), menuScreen.getBlackjackScores());
    }

    public void attachListenersToMainMenuScreenButtons(MainMenuScreen menuScreen, String username, int[] snakeGameScores, int[] blackjackScores){ //TODO: Look, Top 5 highest Blackjack scores for curent user ready to use
        menuScreen.getSnakeGameButton().setOnAction(event -> {
            menuScreen.getDispalyLabel().setVisible(false);
            menuScreen.getDispalyLabel().setManaged(false);
            this.primaryStage.setTitle("Snake Game");
            MusicManager.stopGameManagerMusic();
            MusicManager.playSnakeMusic();
            SnakeGame snakeGame = new SnakeGame(highScoresFilePath, snakeGameScores, username, this.toolBarC);
            snakeGame.startGame(); 

            StackPane snakeGameRootNode = snakeGame.getRootNode();
            currScene.setRoot(snakeGameRootNode); //don't create a new scene but instead change the rootnode of currScene so that we can keep shared ToolBarC
            snakeGameRootNode.requestFocus(); 
        });
        menuScreen.getBlackjackButton().setOnAction(event -> {
            menuScreen.getDispalyLabel().setVisible(false);
            menuScreen.getDispalyLabel().setManaged(false);
            this.primaryStage.setTitle("Blackjack");
            MusicManager.stopGameManagerMusic();
            MusicManager.playBlackjackMusic();
            try {
                BlackJackApp blackjackApp = new BlackJackApp(username);
                blackjackApp.startGame(primaryStage, toolBarC.getToolBar(), blackjackApp.createGameSceneWithCustomToolbar(toolBarC.getToolBar()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        menuScreen.getMoreGameComingButton().setOnAction(event -> {
            menuScreen.getDispalyLabel().setText("We'll add more games in the future!");
            menuScreen.getDispalyLabel().setVisible(true);
            menuScreen.getDispalyLabel().setManaged(true);
        });
    }

    public void createAToolBar(String username){
        this.toolBarC = new ToolBarC();
        this.toolBarC.getJokeLabel().setVisible(false);
        this.toolBarC.getJokeLabel().setManaged(false);
        attachListenerToToolBarButtons(username);
    }
    
    public void attachListenerToToolBarButtons(String username){
        this.toolBarC.getMainMenuButton().setOnAction(event -> {
            this.toolBarC.getJokeLabel().setVisible(false);
            this.toolBarC.getJokeLabel().setManaged(false);
            createAndGoToMainMenuScreenScene(username);
        });
        this.toolBarC.getMoreButtonComing().setOnAction(event -> {
            this.toolBarC.getJokeLabel().setVisible(true);
            this.toolBarC.getJokeLabel().setManaged(true);
        });
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
                FileManager.saveUserAccount(usernameString, pwdString);
                FileManager.initUserHighScores(usernameString);
            }catch (IOException | NoSuchAlgorithmException e){
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
        try {
            boolean ok = FileManager.verifyUserAccount(username, pwd);
            if (!ok) {
                userDoesSomethingWrongLabel.setText("Incorrect Username or password");
            }
            return ok;
        } catch (Exception e) {
            userDoesSomethingWrongLabel.setText("Error: " + e.getMessage());
            return false;
        }
    }

    private void attachBackButtonListenerToLoginScreen(LoginScreen loginScreen){
        loginScreen.getBackButton().setOnAction(event -> {
            createAndGoToFirstScreenScene();
        });
    }

    private void attachBackButtonListenerToCreateAccount(CreateAccountScreen createAccountScreen){
        createAccountScreen.getBackButton().setOnAction(event -> {
            createAndGoToFirstScreenScene();
        });
    }

    public void createAndGoToFirstScreenScene(){
        this.primaryStage.setTitle("Welcome Page");
        // Only start music if it's not already playing
        if (!MusicManager.isGameManagerMusicPlaying()) {
            MusicManager.playGameManagerMusic();
        }
        FirstScreen firstScreen = new FirstScreen();
        StackPane firstScreenRootNode = firstScreen.getRootNode();
        currScene.setRoot(firstScreenRootNode);
        attachListenersToFirstScreenButtons(firstScreen.getLoginButton(), firstScreen.getCreateAccountButton());
    }

    public static void main(String[] args){
        launch();
    }
}
