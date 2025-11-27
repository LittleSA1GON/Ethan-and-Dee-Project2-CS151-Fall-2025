package game.gamemanager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class CreateAccountScreen extends BackgroundSetUp{

    private VBox createAccountVBox;

    private TextField usernameTF;
    private TextField pwdTF;

    private Button createAccountButton;

    private String usernameString;
    private String pwdString;

    String controllerImgPathString = "/game/gamemanager/ChatGPTGeneratedControllerBackground.png";

    public CreateAccountScreen(){
        rootStackPane = new StackPane();

        constructBackgroundPane(controllerImgPathString);
        constructDimAndBlurBackgroundImage();

        constructVBox();
        rootStackPane.getChildren().addAll(backgroundPane, dimOverlay, this.createAccountVBox);
    }
   


    @Override 
    public void constructVBox(){
        createAccountVBox = new VBox(15);
        createAccountVBox.setPrefSize(300, 250);
        createAccountVBox.setMaxSize(300, 250);
        createAccountVBox.setAlignment(Pos.TOP_CENTER);
        createAccountVBox.setPadding(new Insets(20));
        createAccountVBox.setStyle(
            "-fx-background-color: #c9f3ff;" +
            "-fx-background-radius: 5;" //round corners
        );

        Label pleaseSignUp = new Label("Please Register");
        pleaseSignUp.setStyle(
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

        this.createAccountButton = new Button("Sign Up");
        this.createAccountButton.setMaxWidth(Double.MAX_VALUE); // fill the vBox width
        this.createAccountButton.setStyle(
            "-fx-background-color: #ff0066;" +
            "-fx-text-fill: white;"
        );
        
        createAccountVBox.getChildren().addAll(pleaseSignUp, usernameTF, pwdTF, createAccountButton);

        //TODO: use usernameString and pwdString for backend logic
        createAccountButton.setOnAction(event -> {

            this.usernameString = usernameTF.getText();
            this.pwdString = pwdTF.getText();

        });

    }

    @Override
    public StackPane getRootNode(){
        return this.rootStackPane;
    }
    
}
