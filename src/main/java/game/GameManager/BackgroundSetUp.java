package game.gamemanager;


import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.effect.GaussianBlur;

public abstract class BackgroundSetUp {
    protected StackPane rootStackPane;
    protected Pane backgroundPane;
    protected Pane dimOverlay;

    public void constructBackgroundPane(){
        backgroundPane = new Pane();
        backgroundPane.setStyle(
            "-fx-background-image: url('/game/gamemanager/OceanAndIslandTemple:Net.png');" + //loads the image
            "-fx-background-size: cover;" + //scales to fill the whole pane
            "-fx-background-position: center center;" //keeps the image center
        );
    }

    public void constructDimAndBlurBackgroundImage(){
        dimOverlay = new Pane(); //to make background image dimmer
        dimOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.15);"); 

        GaussianBlur blur = new GaussianBlur(4);
        this.backgroundPane.setEffect(blur);
    }
    
    abstract void constructVBox();

}
