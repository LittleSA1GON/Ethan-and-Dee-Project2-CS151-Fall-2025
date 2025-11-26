package game.gamemanager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class LoginScreen {
    
    private StackPane rootStackPane; 
    private Pane backgroundPane;
    private VBox loginVBox;

    private TextField usernameTF;
    private TextField pwdTF;

    private Button loginButton;

    public LoginScreen(){
        rootStackPane = new StackPane();
        
        constructBackgroundPane();
        rootStackPane.getChildren().add(this.backgroundPane);

        dimAndBlurBackgroundImage();
       
        constructLoginVBox();
        rootStackPane.getChildren().add( this.loginVBox);
    

    }

    public void constructBackgroundPane(){
        backgroundPane = new Pane();
        backgroundPane.setStyle(
            "-fx-background-image: url('/game/gamemanager/OceanAndIslandTemple:Net.png');" + //loads the image
            "-fx-background-size: cover;" + //scales to fill the whole pane
            "-fx-background-position: center center;" //keeps the image center
        );
    }

    public void dimAndBlurBackgroundImage(){
        Pane dimOverlay = new Pane(); //to make background image dimmer
        dimOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.15);"); 

        GaussianBlur blur = new GaussianBlur(4);
        this.backgroundPane.setEffect(blur);
        this.rootStackPane.getChildren().add(dimOverlay); //Be careful, this won't affect the background image if we add this before backgroundimage to the backgroundpane
    }

    public void constructLoginVBox(){
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
            //"-fx-text-fill: ;" +
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
        
    }

    public StackPane getRootNode(){
        return this.rootStackPane;
    }
    
}
