package game.gamemanager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class LoginScreen extends BackgroundSetUp {
    
    private VBox loginVBox;

    private TextField usernameTF;
    private TextField pwdTF;

    private Button loginButton;

    private String usernameString;
    private String pwdString;

    String oceanImgPathString = "/game/gamemanager/OceanAndIslandTemple:Net.png";
    String gamepadImgPathString ="/game/gamemanager/gamepad_bg_800x600.png";

    public LoginScreen(){
        rootStackPane = new StackPane();
        
        constructBackgroundPane(gamepadImgPathString);
        constructDimAndBlurBackgroundImage();
       
        constructVBox();
        rootStackPane.getChildren().addAll(backgroundPane, dimOverlay, this.loginVBox);
    }

    @Override
    public void constructVBox(){
        loginVBox = new VBox(15);
        loginVBox.setPrefSize(300, 250);
        loginVBox.setMaxSize(300, 250);
        loginVBox.setAlignment(Pos.TOP_CENTER); //children centered horizontally
        loginVBox.setPadding(new Insets(20));
        loginVBox.setStyle(
            "-fx-background-color: #c9f3ff;" +
            "-fx-background-radius: 5;" //round corners
        );

        Label pleaseLogin = new Label("Please Login");
        pleaseLogin.setStyle(
            "-fx-font-size: 20px"
        );

        this.usernameTF = new TextField();
        this.usernameTF.setPromptText("Username");
        usernameTF.setStyle(
            "-fx-background-color: #62ddff;" + 
            "-fx-prompt-text-fill: black;"
        );

        this.pwdTF = new TextField();
        this.pwdTF.setPromptText("Password");
        this.pwdTF.setStyle(
            "-fx-background-color: #62ddff;" +
            "-fx-prompt-text-fill: black;"
            
        );

        this.loginButton = new Button("Login");
        this.loginButton.setMaxWidth(Double.MAX_VALUE); // fill the vBox width
        this.loginButton.setStyle(
            "-fx-background-color: #ff0066;" +
            "-fx-text-fill: white;"
        );

        loginVBox.getChildren().addAll(pleaseLogin, usernameTF, pwdTF, loginButton);
        
        //TODO: use usernameString and pwdString for backend logic
        loginButton.setOnAction(event -> {
            this.usernameString = usernameTF.getText();
            this.pwdString = pwdTF.getText();

            //TODO: handle validation for username and pwd with saved "user_accounts.txt" (also add encrytion for username and pwd in an extra text file)
        });

    }

    @Override
    public StackPane getRootNode(){
        return this.rootStackPane;
    }
    
}
