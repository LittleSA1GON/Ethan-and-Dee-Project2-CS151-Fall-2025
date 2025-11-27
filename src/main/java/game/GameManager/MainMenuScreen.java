
package game.gamemanager;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class MainMenuScreen extends BackgroundSetUp {

    private String username; 
    private BorderPane rootBorderPane;
    private ToolBarC toolBar;

    private Button snakeGameButton; 
    private Button blackJackButton;

    private Label welcomeLabel = new Label("How's it going, " + username + "?");
    private Label randomGameQuote = new Label("\"The right man in the wrong place can make all the difference in the world\"\n-G-Man in Half-Life 2\n");

    public MainMenuScreen(){
        rootBorderPane = new BorderPane();
        

        //TODO: continue
        //Testing
        toolBar = new ToolBarC();
        constructBorderPane();
    }

    public void constructBorderPane(){
       //TODO
        rootBorderPane.setTop(toolBar.getToolBar());
    }

    @Override 
    public void constructVBox(){

    }

    public BorderPane getRootNode(){
        return rootBorderPane;
    }

}
