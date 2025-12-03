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
    private Path highScoresFilePath;

    private UserScore[] snakeGameScores = new UserScore[5];
    private UserScore[] blackjackScores = new UserScore[5];

    private Button snakeGameButton;
    private Button blackJackButton;
    private Button futureGameButton;

    private Label displayALabel = new Label("");

    private Image gamerIconImage = new Image("/game/gamemanager/ChatGPTGeneratedGamerIcon.png");

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
        this.highScoresFilePath = highScoresFilePath;
        rootBorderPane = new BorderPane();
        construct2HighscoreArrays(highScoresFilePath);
        constructRootBorderPane();
    }

    public void constructRootBorderPane(){
        constructBackgroundPane("/game/gamemanager/ChatGPTGeneratedGamerIcon.png");
        constructDimAndBlurBackgroundImage();
       
        rootBorderPane.setTop(toolBarC.getToolBar());

        constructVBox(); // Left
        rootBorderPane.setLeft(this.leftVBox);

        constructRightVBox(); // Center
        VBox outerVBoxWrapper = new VBox(this.rightVBox); // for design, push down from top
        outerVBoxWrapper.setAlignment(Pos.TOP_CENTER);
        outerVBoxWrapper.setPadding(new Insets(50, 0, 0, 0));

        StackPane rightVBoxWrapper = new StackPane(backgroundPane, outerVBoxWrapper); 
        StackPane.setAlignment(rightVBox, Pos.TOP_CENTER);

        rootBorderPane.setCenter(rightVBoxWrapper);
    }

    @Override 
    public void constructVBox(){ // BorderPane's left node
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
            "-fx-background-color: rgba(0, 0, 0, 1);" +
            "-fx-padding: 30 28 30 28;" +
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

        Label top5ScoresLabel = new Label("Top 5 Players");
        top5ScoresLabel.setAlignment(Pos.CENTER);
        top5ScoresLabel.setMaxWidth(Double.MAX_VALUE);

        constructHighScoreTable(); // uses global top-5 arrays

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
        highScoreTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        highScoreTable.setPrefHeight(260);


        TableColumn<ScoreRow, String> snakeUserCol = new TableColumn<>("Snake User");
        snakeUserCol.setCellValueFactory(new PropertyValueFactory<>("snakeUser"));

        TableColumn<ScoreRow, Integer> snakeScoreCol = new TableColumn<>("Snake Score");
        snakeScoreCol.setCellValueFactory(new PropertyValueFactory<>("snakeScore"));

        TableColumn<ScoreRow, String> blackjackUserCol = new TableColumn<>("Blackjack User");
        blackjackUserCol.setCellValueFactory(new PropertyValueFactory<>("blackjackUser"));

        TableColumn<ScoreRow, Integer> blackjackScoreCol = new TableColumn<>("Blackjack Score");
        blackjackScoreCol.setCellValueFactory(new PropertyValueFactory<>("blackjackScore"));

        highScoreTable.getColumns().addAll(snakeUserCol, snakeScoreCol, blackjackUserCol, blackjackScoreCol);

        ObservableList<ScoreRow> scoresData = FXCollections.observableArrayList();
        for (int i = 0; i < 5; i++) {
            String sUser = "";
            int sScore = 0;
            String bUser = "";
            int bScore = 0;

            if (snakeGameScores[i] != null) {
                sUser = snakeGameScores[i].getUsername();
                sScore = snakeGameScores[i].getScore();
            }
            if (blackjackScores[i] != null) {
                bUser = blackjackScores[i].getUsername();
                bScore = blackjackScores[i].getScore();
            }

            scoresData.add(new ScoreRow(sUser, sScore, bUser, bScore));
        }

        highScoreTable.setItems(scoresData);
    }


    public void constructHighScoreTable(Path highScoresFilePath){
        construct2HighscoreArrays(highScoresFilePath);
        constructHighScoreTable(); 
    }

    public void construct2HighscoreArrays(Path highScoresFilePath){
        for (int i = 0; i < 5; i++) {
            snakeGameScores[i] = null;
            blackjackScores[i] = null;
        }

        if (!Files.exists(highScoresFilePath) || !Files.isReadable(highScoresFilePath)) {
            return;
        }

        try {
            List<String> lines = Files.readAllLines(highScoresFilePath);

            for (String line : lines) {
                if (line == null) continue;
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(":");

                if (parts.length < 2) {
                    continue; 
                }

                String header = parts[0].trim().toLowerCase();

                for (int idx = 1; idx < parts.length && idx <= 5; idx++) {
                    String entry = parts[idx].trim();
                    if (entry.isEmpty()) continue;

                    String[] userScoreParts = entry.split("\\+");
                    if (userScoreParts.length != 2) {
                        continue;
                    }

                    String user = userScoreParts[0];
                    int score;
                    try {
                        score = Integer.parseInt(userScoreParts[1]);
                    } 
                    catch (NumberFormatException e) {
                        continue;
                    }

                    UserScore candidate = new UserScore(user, score);

                    if ("snake".equals(header)) {
                        insertIntoTop(snakeGameScores, candidate);
                    } 
                    else if ("blackjack".equals(header)) {
                        insertIntoTop(blackjackScores, candidate);
                    }
                }
            }
        } 
        catch (IOException e) {
            displayALabel.setText("Error: " + e.getMessage());
            displayALabel.setVisible(true);
            displayALabel.setManaged(true);
        }
    }// end of construct2HighscoreArrays()

    public int[] getSnakeGameScores(){
        int[] scores = new int[5];
        for (int i = 0; i < 5; i++) {
            if (snakeGameScores[i] != null) {
                scores[i] = snakeGameScores[i].getScore();
            } 
            else {
                scores[i] = 0;
            }
        }
        return scores;
    }

    public int[] getBlackjackScores(){
        int[] scores = new int[5];
        for (int i = 0; i < 5; i++) {
            if (blackjackScores[i] != null) {
                scores[i] = blackjackScores[i].getScore();
            } 
            else {
                scores[i] = 0;
            }
        }
        return scores;
    }

    private void insertIntoTop(UserScore[] topArray, UserScore candidate) {
        if (candidate == null) return;

        for (int i = 0; i < topArray.length; i++) {
            if (topArray[i] == null || candidate.getScore() > topArray[i].getScore()) {
                for (int j = topArray.length - 1; j > i; j--) {
                    topArray[j] = topArray[j - 1];
                }
                topArray[i] = candidate;
                break;
            }
        }
    }

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
}
