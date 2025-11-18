package game.snake;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

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
        this.canvas = new Canvas(800, 520); //if let Stage Size be 800 x 600
        this.root = constructRoot(toolBar, scoreHBox, canvas);
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

    private VBox constructRoot(ToolBar toolBar, HBox scoreHBox, Canvas canvas){
        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(15));

        vBox.getChildren().addAll(toolBar, scoreHBox, canvas);
        return vBox;
    }

    public VBox getRootNode(){ return this.root; }


}
