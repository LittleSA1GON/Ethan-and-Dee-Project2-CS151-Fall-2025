package game.GameManager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class FirstScreen extends BackgroundSetUp {
    private VBox firstScreenVBox;
    private Button loginButton;
    private Button createAccountButton;

    String oceanImgPathString = "/game/gamemanager/OceanAndIslandTemple.png";
    
    public FirstScreen(){
        rootStackPane = new StackPane();
        
        constructBackgroundPane(oceanImgPathString);
        constructDimAndBlurBackgroundImage();

        constructVBox();
        rootStackPane.getChildren().addAll(backgroundPane, dimOverlay, firstScreenVBox);
    }

    @Override
    public void constructVBox(){
        firstScreenVBox = new VBox(15);
        firstScreenVBox.setPrefSize(300, 250);
        firstScreenVBox.setMaxSize(300, 250);

        firstScreenVBox.setAlignment(Pos.TOP_CENTER);
        firstScreenVBox.setPadding(new Insets(20));
        firstScreenVBox.setStyle(
            "-fx-background-color: #c9f3ff;" +
            "-fx-background-raius: 5;"
        );

        Label welcome = new Label("Welcome");
        welcome.setStyle(
            "-fx-font-size: 22px;" +
            "-fx-font-family: 'Segoe UI';"
        );

        loginButton = new Button("Login");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setStyle(
            "-fx-background-color: #ff0066;" +
            "-fx-text-fill: white;"
        );
        
        createAccountButton = new Button("Sign Up");
        createAccountButton.setMaxWidth(Double.MAX_VALUE);
        createAccountButton.setStyle(
            "-fx-background-color: #ff0066;" +
            "-fx-text-fill: white;"
        );
        
        firstScreenVBox.getChildren().addAll(welcome, loginButton, createAccountButton);
    }

    public StackPane getRootNode(){
        return this.rootStackPane;
    }
    public Button getLoginButton(){
        return this.loginButton;
    }
    public Button getCreateAccountButton(){
        return this.createAccountButton;
    }
}
