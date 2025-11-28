
package game.gamemanager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MainMenuScreen extends BackgroundSetUp {

    private String username;
    private BorderPane rootBorderPane;
    private ToolBarC toolBarC;
    private VBox leftVBox;
    private VBox rightVBox;
    private TableView<ScoreRow> highScoreTable; //every row is a ScoreRow object

    private int[] snakeGameScores = new int[5];
    private int[] blackjackScores = new int[5];

    private Button snakeGameButton; 
    private Button blackJackButton;
    private Button futureGameButton;

    private Label displayALabel = new Label("");

    private Image gamerIconImage = new Image("/game/gamemanager/ChatGPTGeneratedGamerIcon.png");

    //private Path highScoresFilePath = Paths.get("high_scores.txt"); //delete this after testing it (pass the file into constructor from the gamemanager instead)

    String primaryGameButtonStyle =
    "-fx-background-color: linear-gradient(#00b4d8, #0077b6);" +
    "-fx-text-fill: white;" +
    "-fx-font-size: 16px;" +
    "-fx-font-weight: bold;" +
    "-fx-background-radius: 12;" +
    "-fx-padding: 10 26 10 26;" +
    "-fx-border-color: rgba(255,255,255,0.25);" +
    "-fx-border-width: 1;" +
    "-fx-border-radius: 12;" +
    "-fx-cursor: hand;" +
    "-fx-pref-width: 240;";

    public MainMenuScreen(Path highScoresFilePath, String username, ToolBarC toolBarC){
        this.username = username;
        this.toolBarC = toolBarC;

        rootBorderPane = new BorderPane();
        construct2HighscoreArrays(highScoresFilePath);
        constructRootBorderPane();
    }

    public void constructRootBorderPane(){
        constructBackgroundPane("/game/gamemanager/ChatGPTGeneratedGamerIcon.png");
        constructDimAndBlurBackgroundImage();
       
        rootBorderPane.setTop(toolBarC.getToolBar());

        constructVBox(); //Left
        rootBorderPane.setLeft(this.leftVBox);

        constructRightVBox(); //Center
        VBox outerVBoxWrapper = new VBox(this.rightVBox); //for design purpose, I want the top5ScoresLabel and the table to appear abit lower so we can see the background image
        outerVBoxWrapper.setAlignment(Pos.TOP_CENTER);
        outerVBoxWrapper.setPadding(new Insets(50, 0, 0, 0));

        StackPane rightVBoxWrapper = new StackPane(backgroundPane, outerVBoxWrapper); //to add background + outerVBoxWrapper to Center
        StackPane.setAlignment(rightVBox, Pos.TOP_CENTER);

        rootBorderPane.setCenter(rightVBoxWrapper);
    }

    @Override 
    public void constructVBox(){ //BordorPane's left node
        leftVBox = new VBox(10);

        Label welcomeLabel = new Label("How's it going, " + username + "?");
        Label randomGameQuote = new Label("\"The right man in the wrong place can make all the difference in the world\"\n-G-Man in Half-Life 2\n");

        welcomeLabel.setWrapText(true);

        snakeGameButton = new Button("Play Snake Game");
        snakeGameButton.setStyle(primaryGameButtonStyle);

        blackJackButton = new Button("Play Blackjack");
        blackJackButton.setStyle(primaryGameButtonStyle);

        futureGameButton = new Button("More Games Coming");
        futureGameButton.setStyle(primaryGameButtonStyle);

        ImageView gameIconImageView = new ImageView(gamerIconImage);
        gameIconImageView.setFitWidth(100);
        gameIconImageView.setPreserveRatio(true);

        HBox gameIconHBox = new HBox(gameIconImageView);
        gameIconHBox.setAlignment(Pos.CENTER);
        gameIconHBox.setMaxWidth(Double.MAX_VALUE);

        displayALabel.setVisible(false);
        displayALabel.setManaged(false);

        leftVBox.setAlignment(Pos.CENTER);
        leftVBox.setPrefWidth(290);
        leftVBox.setMaxWidth(380);

        leftVBox.getChildren().addAll(gameIconHBox, welcomeLabel, randomGameQuote, snakeGameButton, blackJackButton, futureGameButton, displayALabel);

        leftVBox.setStyle(
            "-fx-background-color: rgba(0, 0, 0, 1);" +  // dark semi-transparent card
            "-fx-padding: 30 28 30 28;" +                   // top right bottom left
            "-fx-alignment: center;"
        );
        welcomeLabel.setStyle(
            "-fx-text-fill: #ffffff;" +
            "-fx-font-size: 28px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 0 0 8 0;"
        );
        randomGameQuote.setWrapText(true);
        randomGameQuote.setStyle(
            "-fx-text-fill: #ffffffff;" +
            "-fx-font-size: 13px;" +
            "-fx-line-spacing: 4px;" +
            "-fx-opacity: 0.9;"
        );
        displayALabel.setStyle(
            "-fx-text-fill: red;"
        );
        displayALabel.setWrapText(true);
    }

    public void constructRightVBox(){
        rightVBox = new VBox(15);

        Label top5ScoresLabel = new Label("Your Top 5 highest scores");
        top5ScoresLabel.setAlignment(Pos.CENTER);
        top5ScoresLabel.setMaxWidth(Double.MAX_VALUE);

        constructHighScoreTable();

        rightVBox.getChildren().addAll(top5ScoresLabel, this.highScoreTable);
        rightVBox.setMaxWidth(Double.MAX_VALUE);
        
        top5ScoresLabel.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #ffffff;" +
            "-fx-padding: 0 0 10 0;" +
            "-fx-alignment: center;"
        );

        rightVBox.setStyle(
            "-fx-background-color: rgba(0, 0, 0, 0.65);" +
            "-fx-padding: 20;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 15, 0.3, 0, 4);"
        );
    }

    public void constructHighScoreTable(){
        highScoreTable = new TableView<>();
        highScoreTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS); //make columns auto-resize
        highScoreTable.setPrefHeight(260);

        TableColumn<ScoreRow, Integer> snakeGameColumn = new TableColumn<>("Snake Game"); //creates a column with header "Snake Game"
        snakeGameColumn.setCellValueFactory(new PropertyValueFactory<>("snakeGameScore")); //*** uses the getSnakeGameScore() from ScoreRow for each row of snakeGameColumn*/

        TableColumn<ScoreRow, Integer> blackjackColumn = new TableColumn<>("Blackjack");
        blackjackColumn.setCellValueFactory(new PropertyValueFactory<>("blackjackScore")); //*** uses the getBlackjackScore() from ScoreRow */

        highScoreTable.getColumns().addAll(snakeGameColumn, blackjackColumn); //add both columns to the tableview

        //create ObservableList of rows (add current user's top 5 highest scores for each game into the ScoreRow object)
        ObservableList<ScoreRow> scoresData = FXCollections.observableArrayList();
        for(int i = 0; i < 5; i++){
            scoresData.add(new ScoreRow(snakeGameScores[i] + 1000, blackjackScores[i] + 1000)); //TODO: Look here: this is where I add 1000 to make the default starting score, 1000
        }

        highScoreTable.setItems(scoresData); //give scoresData to the table

        highScoreTable.setFixedCellSize(35); //height per row
        highScoreTable.setPrefHeight((37 * 5) + 30); //(rowsHeight(added 2 px extra) * noOfRows) + header height 
        highScoreTable.setMaxHeight((37 * 5) + 30);
        
        highScoreTable.setStyle(
            "-fx-background-color: transparent;" + 
            "-fx-table-header-border-color: white;" +
            "-fx-padding: 4;" +
            "-fx-border-color: rgba(0, 0, 0, 1);" +
            "-fx-border-radius: 12;"
        );

        snakeGameColumn.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-alignment: CENTER;" +
            "-fx-text-fill: #0077b6;"
        );
        blackjackColumn.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-alignment: CENTER;" +
            "-fx-text-fill: #0077b6;"
        );
    }//end of constructHighScoreTable()

    public void construct2HighscoreArrays(Path highScoresFilePath){
        if(Files.exists(highScoresFilePath) && Files.isReadable(highScoresFilePath)){
            try{
                List<String> lines = Files.readAllLines(highScoresFilePath);
                for(String line : lines){
                    String[] parts = line.split(":", 11);//username = parts[0], snakegamescores are from parts[1] to parts[5], blackjackscores are from parts[6] to parts[11] (inclusive)
                    if(parts[0].equals(username)){
                        for(int i = 0; i < 5; i++){
                            snakeGameScores[i] = Integer.valueOf(parts[i+1]);
                        }
                        for(int i = 0; i < 5; i++){
                            blackjackScores[i] = Integer.valueOf(parts[i + 6]);
                        }
                        return;
                    }
                }
            }catch (IOException e){
                displayALabel.setText("Error: " + e.getMessage());
                displayALabel.setVisible(true);
                displayALabel.setManaged(true);
            }
        }
        else{
            //files should always exists here and readable 
        }
    }//end of construct2HighscoreArrays()

    public BorderPane getRootNode(){
        return rootBorderPane;
    }

    public Label getDispalyLabel(){
        return this.displayALabel;
    }

    public Button getSnakeGameButton(){
        return this.snakeGameButton;
    }
    public Button getBlackjackButton(){
        return this.blackJackButton;
    }
    public Button getMoreGameComingButton(){
        return this.futureGameButton;
    }
    public int[] getSnakeGameScores(){
        return this.snakeGameScores;
    }
    public int[] getBlackjackScores(){  //TODO: look, this is current's user's top 5 scores for blackjack
        return this.blackjackScores;
    }
}
