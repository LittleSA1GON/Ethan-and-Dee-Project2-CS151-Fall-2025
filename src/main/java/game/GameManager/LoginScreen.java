package game.gamemanager;

import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class LoginScreen {
    
    private StackPane rootStackPane; 
    private Pane backgroundPane;

    public LoginScreen(){
        rootStackPane = new StackPane();
        
        constructBackgroundPane();
        rootStackPane.getChildren().add(this.backgroundPane);

        dimBackgroundImage();
    
    

    }

    public void constructBackgroundPane(){
        backgroundPane = new Pane();
        backgroundPane.setStyle(
            "-fx-background-image: url('/game/gamemanager/OceanAndIslandTemple:Net.png');" + //loads the image
            "-fx-background-size: cover;" + //scales to fill the whole pane
            "-fx-background-position: center center;" //keeps the image center
        );
    }

    public void dimBackgroundImage(){
        Pane dimOverlay = new Pane(); //to make background image dimmer
        dimOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.25);"); 

        GaussianBlur blur = new GaussianBlur(4);
        this.backgroundPane.setEffect(blur);
        this.rootStackPane.getChildren().add(dimOverlay);
    }

    public StackPane getRootNode(){
        return this.rootStackPane;
    }
    
}
