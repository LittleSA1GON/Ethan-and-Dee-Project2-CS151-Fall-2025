package game.snake;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class SnakeGame {

    private Label currScoreLabel = new Label(); //restart from 0 everytime game restarts -->use currScoreLabel.setText(String.valueOf(newScore)); to update it with real time
    private VBox root; //top = toolbar, right beneath the toolbar = score (Hbox), the rest = canvas
    private ToolBar toolBar;
    private HBox scoreHBox;
    private Canvas canvas;

    /*
     * TODO: Paste the ToolBar logic + UI and add it to the root (VBox)
     */

    public SnakeGame(){ 
        this.toolBar = initToolBar(); //incorporating abstraction everywhere possible to make code readable
        this.scoreHBox = initScoreHBox();
        this.canvas = constructCanvas(); //assuming we let Stage Size be 800 x 600
        this.root = constructRoot();
    }//end of constructor

    private ToolBar initToolBar(){ //initializing ToolBar (update this after completing game manager)
        return new ToolBar(
            new Button("Menu"),
            new Button("More Button Coming")
        );
    }//end of initToolBar

    private HBox initScoreHBox(){
         HBox hBox = new HBox(15);
         hBox.setPadding(new Insets(10));
         hBox.setAlignment(Pos.TOP_RIGHT);

         Label scoreLabel = new Label("Score: ");
         currScoreLabel.setText(String.valueOf(0));
         hBox.getChildren().addAll(scoreLabel, currScoreLabel);
         return hBox;
    } //end of initScoreHBox

    private Canvas constructCanvas(){

        Canvas canvasL = new Canvas(720, 460);
        GraphicsContext gc = canvasL.getGraphicsContext2D();

        //set visible border
        gc.setStroke(Color.PURPLE);
        gc.setLineWidth(10); 
        gc.strokeRect(5, 5, canvasL.getWidth() - 10, canvasL.getHeight() - 10);

        return canvasL;
    }//end of constructCanvas

    private VBox constructRoot(){
        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(15));

        StackPane canvasHolderPane = new StackPane(canvas);
        canvasHolderPane.setAlignment(Pos.CENTER);

        vBox.getChildren().addAll(this.toolBar, this.scoreHBox, canvasHolderPane);
        return vBox;
    }

    public VBox getRootNode(){ return this.root; }


}
