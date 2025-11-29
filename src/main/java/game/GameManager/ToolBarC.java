package game.gamemanager;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;

public class ToolBarC {
    private ToolBar toolBar;
    private Button mainMenuButton;
    private Button logoutButton;
    private Button deleteAccountButton;
    private Button moreButtonComing;
    private Label titleLabel = new Label("Game Hub");
    private Label jokeLabel = new Label("Don't click this, you are making me sad");

    public ToolBarC(){
        mainMenuButton = new Button("Main Menu");
        moreButtonComing = new Button("More Buttons Coming");
        logoutButton = new Button("Logout");
        deleteAccountButton = new Button("Delete Account");
        jokeLabel.setVisible(false);
        jokeLabel.setManaged(false);
        toolBar = new ToolBar(mainMenuButton, logoutButton, deleteAccountButton, moreButtonComing, titleLabel, jokeLabel);
        makeDesignBetter();
    }

    public ToolBar getToolBar(){
        return this.toolBar;
    }
    public Button getMainMenuButton(){
        return this.mainMenuButton;
    }
    public Button getLogoutButton(){
        return this.logoutButton;
    }
    public Button getDeleteAccountButton(){
        return this.deleteAccountButton;
    }
    public Button getMoreButtonComing(){
        return this.moreButtonComing;
    }
    public Label getJokeLabel(){
        return this.jokeLabel;
    }
    public void makeDesignBetter(){
        this.mainMenuButton.setStyle(
            "-fx-background-color: linear-gradient(#00b4d8, #0077b6);" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 6 16 6 16;" +
            "-fx-border-color: rgba(255,255,255,0.25);" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 10;" +
            "-fx-cursor: hand;"
        );
        this.logoutButton.setStyle(
            "-fx-background-color: linear-gradient(#00b4d8, #0077b6);" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 6 16 6 16;" +
            "-fx-border-color: rgba(255,255,255,0.25);" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 10;" +
            "-fx-cursor: hand;"
        );
        this.deleteAccountButton.setStyle(
            "-fx-background-color: linear-gradient(#00b4d8, #0077b6);" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 6 16 6 16;" +
            "-fx-border-color: rgba(255,255,255,0.25);" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 10;" +
            "-fx-cursor: hand;"
        );
        this.moreButtonComing.setStyle(
            "-fx-background-color: linear-gradient(#00b4d8, #0077b6);" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 6 16 6 16;" +
            "-fx-border-color: rgba(255,255,255,0.25);" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 10;" +
            "-fx-cursor: hand;"
        );
        this.titleLabel.setStyle(
            "-fx-text-fill: #e0faff;" +
            "-fx-font-size: 18px;" +
            "-fx-font-weight: bold;"
        );
        this.jokeLabel.setStyle(
            "-fx-text-fill: red;"
        );
        this.toolBar.setStyle(
            "-fx-background-color: rgba(0,0,0,0.75);" +
            "-fx-padding: 8 16 8 16;" +
            "-fx-spacing: 10;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 8, 0.5, 0, 2);"
        );
    }
}
