package game.GameManager;

import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public abstract class BackgroundSetUp {
    protected StackPane rootStackPane;
    protected Pane backgroundPane;
    protected Pane dimOverlay;

    public void constructBackgroundPane(String oceanImgPathString){
        backgroundPane = new Pane();
        backgroundPane.setStyle(
            "-fx-background-image: url('" + oceanImgPathString + "');" + //loads the image
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
    
    protected abstract void constructVBox();
}
