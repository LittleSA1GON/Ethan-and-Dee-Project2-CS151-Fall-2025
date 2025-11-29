package game.gamemanager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class LoginScreen extends BackgroundSetUp {
    private VBox loginVBox;

    private TextField usernameTF;
    private TextField pwdTF;

    private Button loginButton;
    private Button backButton;

    private Label successfulLabel = new Label ("Account is created successcully!, please log in");
    private Label userDoesSomethingWrongLabel;

    String oceanImgPathString = "/game/gamemanager/OceanAndIslandTemple.png";
    String gamepadImgPathString ="/game/gamemanager/gamepad_bg_800x600.png";

    public LoginScreen(){
        rootStackPane = new StackPane();
        
        constructBackgroundPane(gamepadImgPathString);
        constructDimAndBlurBackgroundImage();

        this.successfulLabel.setVisible(false);
        this.successfulLabel.setManaged(false);
        this.successfulLabel.setStyle(
            "-fx-text-fill: red;"
        );
       
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
        userDoesSomethingWrongLabel = new Label();
        userDoesSomethingWrongLabel.setVisible(false);
        userDoesSomethingWrongLabel.setManaged(false); //don't take up layout space
        userDoesSomethingWrongLabel.setWrapText(true); //enable text wrapping
        userDoesSomethingWrongLabel.setMaxWidth(Double.MAX_VALUE);
        userDoesSomethingWrongLabel.setStyle(
            "-fx-text-fill: red;"
        );

        this.loginButton = new Button("Login");
        this.loginButton.setMaxWidth(Double.MAX_VALUE); // fill the vBox width
        this.loginButton.setStyle(
            "-fx-background-color: #ff0066;" +
            "-fx-text-fill: white;"
        );

        this.backButton = new Button("Back");
        this.backButton.setMaxWidth(Double.MAX_VALUE);
        this.backButton.setStyle(
            "-fx-background-color: #cccccc;" +
            "-fx-text-fill: black;"
        );

        HBox buttonBox = new HBox(10, loginButton, backButton);
        buttonBox.setMaxWidth(Double.MAX_VALUE);

        loginVBox.getChildren().addAll(pleaseLogin, usernameTF, pwdTF, userDoesSomethingWrongLabel, successfulLabel, buttonBox);
    }

    public void addSuccessfulRegistrationToVBox(){
        successfulLabel.setVisible(true);
        successfulLabel.setManaged(true);
    }
    public Label getSuccessfulLabel(){
        return this.successfulLabel;
    }
    public Button getLoginButton(){
        return this.loginButton;
    }
    public Button getBackButton(){
        return this.backButton;
    }
    public TextField getUsernameTF(){
        return this.usernameTF;
    }
    public TextField getPasswordTF(){
        return this.pwdTF;
    }
    public Label getUserDoesSomethingWrongLabel(){
        return this.userDoesSomethingWrongLabel;
    }
    public StackPane getRootNode(){
        return this.rootStackPane;
    }
    
}
