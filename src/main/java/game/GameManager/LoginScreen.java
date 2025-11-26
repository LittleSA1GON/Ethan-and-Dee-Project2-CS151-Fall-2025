package game.gamemanager;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class LoginScreen {
    
    private StackPane rootStackPane; 
    private Image backgroundImage = new Image("/game/gamemanager/OceanAndIslandTemple:Net.png");
    private Pane backgroundPane;

    public LoginScreen(){
        rootStackPane = new StackPane();
        
        constructBackgroundPane();
        rootStackPane.getChildren().add(this.backgroundPane);
        
    
    

    }

    public void constructBackgroundPane(){

        backgroundPane = new Pane();
        backgroundPane.setStyle(
            "-fx-background-image: url('/game/gamemanager/OceanAndIslandTemple:Net.png');" + //loads the image
            "-fx-background-size: cover;" + //scales to fill the whole pane
            "-fx-background-position: center center;" //keeps the image center
        );

    }


    public StackPane getRootNode(){
        return this.rootStackPane;
    }
    
}
