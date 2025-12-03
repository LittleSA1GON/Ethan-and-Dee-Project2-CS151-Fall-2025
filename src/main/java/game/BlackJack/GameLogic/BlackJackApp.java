package game.blackjack.gamelogic;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import javafx.scene.control.PasswordField;
import javafx.stage.Modality;
import game.gamemanager.FileManager;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Priority;

import game.blackjack.players.Player;
import game.blackjack.players.Dealer;
import game.blackjack.players.Computer;
import game.blackjack.supportingfiles.Card;
import game.blackjack.gamelogic.gameenums.GameState;
import game.blackjack.supportingfiles.cardenums.*;

public class BlackJackApp extends Application {
    private BlackJackGame game;
    private String username;
    private ToolBar toolbarc;
    private Scene gameScene;
    private static final int CARD_DELAY_MS = 120;
    private static final int COMPUTER_TURN_DELAY_MS = 1000;
    private static final int DEALER_TURN_DELAY_MS = 1000;    

    private BorderPane contentPane;
    private Pane gameArea;

    private Label statusLabel;
    private TextArea gameLogArea;
    private HBox dealerHandBox;
    private VBox dealerSectionBox;
    private HBox comp1HandBox;
    private VBox comp1SectionBox;
    private HBox comp2HandBox;
    private VBox comp2SectionBox;
    private HBox humanHandBox;
    private VBox humanSectionBox;

    private Label humanMoneyLabel;
    private Label comp1MoneyLabel;
    private Label comp2MoneyLabel;
    private Label dealerMoneyLabel;
    private Label humanNameLabel;
    private Label comp1NameLabel;
    private Label comp2NameLabel;
    private Label dealerNameLabel;
    private Label comp1BetLabel;
    private Label comp2BetLabel;
    private Label humanBetLabel;

    private Button hitButton;
    private Button standButton;
    private Button saveBetButton;
    private ChoiceBox<Integer> betChoiceBox;
    private Label dealerScoreLabel;
    private Label comp1ScoreLabel;
    private Label comp2ScoreLabel;
    private Label humanScoreLabel;

    private Label gameStateLabel;
    private Label blackjackTitleLabel;
    private Button newGameBtn;
    private Button loadGameBtn;
    private Button saveGameBtn;

    public BlackJackApp(String username) {
        this.username = username;
    }

    public BlackJackApp() {
        this.username = "Player";
    }

    public void startGame(Stage primaryStage, ToolBar toolbarc, BorderPane root) {
        this.toolbarc = toolbarc;

        BorderPane blackjackRoot = createGameSceneWithCustomToolbar(toolbarc);

        Scene scene = primaryStage.getScene();
        if (scene == null) {
            gameScene = new Scene(blackjackRoot);
            primaryStage.setScene(gameScene);
        } else {
            scene.setRoot(blackjackRoot);
            gameScene = scene;
        }

        primaryStage.setTitle("BlackJack");
    }

    public BorderPane createGameSceneWithCustomToolbar(ToolBar customToolbar) {
        Pane backgroundPane = new Pane();
        backgroundPane.setPrefWidth(1200);
        backgroundPane.setPrefHeight(800);
        backgroundPane.setStyle(
            "-fx-background-image: url('/game/blackjack/BlackJackBackground.jpg');" +
            "-fx-background-size: cover;" +
            "-fx-background-position: center center;"
        );

        Pane dimOverlay = new Pane();
        dimOverlay.setPrefWidth(1200);
        dimOverlay.setPrefHeight(800);
        dimOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.0);");

        contentPane = new BorderPane();
        contentPane.setPrefWidth(1200);
        contentPane.setPrefHeight(800);

        HBox blackjackControlBar = createBlackjackControlBar();
        VBox topContainer = new VBox();
        topContainer.getChildren().addAll(customToolbar, blackjackControlBar);
        contentPane.setTop(topContainer);

        if (game == null) {
            contentPane.setCenter(createPreGameCenter());
        } 
        else {
            gameArea = createGameArea();
            contentPane.setCenter(gameArea);
        }

        StackPane stacked = new StackPane(backgroundPane, dimOverlay, contentPane);
        StackPane.setAlignment(contentPane, Pos.TOP_LEFT);

        BorderPane root = new BorderPane();
        root.setCenter(stacked);
        return root;
    }

    private HBox createBlackjackControlBar() {
        HBox bar = new HBox(20);
        bar.setPadding(new Insets(8));
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setStyle("-fx-background-color: rgba(0,0,0,0.7);");

        blackjackTitleLabel = new Label("BlackJack");
        blackjackTitleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24; -fx-font-weight: bold;");

        gameStateLabel = new Label("State: MENU");
        gameStateLabel.setStyle("-fx-text-fill: #ffff00; -fx-font-size: 20; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        newGameBtn = new Button("New Game");
        newGameBtn.setStyle("-fx-font-size: 16;");
        newGameBtn.setOnAction(e -> startNewGame());

        loadGameBtn = new Button("Load Game");
        loadGameBtn.setStyle("-fx-font-size: 16;");
        loadGameBtn.setOnAction(e -> loadGame());

        saveGameBtn = new Button("Save Game");
        saveGameBtn.setStyle("-fx-font-size: 16;");
        saveGameBtn.setOnAction(e -> saveGame());

        showPreGameButtons();

        bar.getChildren().addAll(blackjackTitleLabel, gameStateLabel, spacer, newGameBtn, loadGameBtn, saveGameBtn);
        return bar;
    }


    private void showPreGameButtons() {
        if (newGameBtn != null) {
            newGameBtn.setVisible(true);
            newGameBtn.setManaged(true);
        }
        if (loadGameBtn != null) {
            loadGameBtn.setVisible(true);
            loadGameBtn.setManaged(true);
        }
        if (saveGameBtn != null) {
            saveGameBtn.setVisible(false);
            saveGameBtn.setManaged(false);
        }
        if (gameStateLabel != null) {
            gameStateLabel.setText("State: MENU");
        }
    }

    private void showInGameButtons() {
        if (newGameBtn != null) {
            newGameBtn.setVisible(false);
            newGameBtn.setManaged(false);
        }
        if (loadGameBtn != null) {
            loadGameBtn.setVisible(false);
            loadGameBtn.setManaged(false);
        }
        if (saveGameBtn != null) {
            saveGameBtn.setVisible(true);
            saveGameBtn.setManaged(true);
        }
        if (game != null && gameStateLabel != null) {
            gameStateLabel.setText("State: " + game.getGameState());
        }
    }

    private StackPane createPreGameCenter() {
        VBox box = new VBox(20);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(40));

        Label title = new Label("Welcome to BlackJack");
        title.setStyle("-fx-font-size: 48; -fx-font-weight: bold; -fx-text-fill: white;");

        Label prompt = new Label("Use the New Game or Load Game buttons above to begin.");
        prompt.setStyle("-fx-font-size: 24; -fx-text-fill: white;");

        box.getChildren().addAll(title, prompt);

        box.setStyle(
            "-fx-background-color: rgba(47, 70, 57, 0.65);" +  
            "-fx-background-radius: 20;" +                 
            "-fx-border-radius: 20;" +                    
            "-fx-border-color: rgba(255, 255, 255, 0.8);" + 
            "-fx-border-width: 2;"
        );

        StackPane wrapper = new StackPane(box);
        wrapper.setAlignment(Pos.CENTER);        
        wrapper.setPadding(new Insets(40));      

        return wrapper;
    }

    public void startNewGame() {
        try {
            game = new BlackJackGame(username);
            showInGameButtons();
            switchToGameArea();
        } 
        catch (Exception e) {
            e.printStackTrace();
            showAlert("Error starting new game: " + e.getMessage());
        }
    }

    private void loadGame() {
        try {
            if (!isSaveAvailableForUser(username)) {
                showAlert("No saved game found for user: " + username);
                return;
            }
            String pwd = promptForPassword("Enter account password to load save:");
            if (pwd == null) {
                showAlert("Load cancelled.");
                return;
            }
            game = new BlackJackGame(username, true, pwd);
            showInGameButtons();
            switchToGameArea();
        } 
        catch (Exception e) {
            showAlert("Error loading game: " + e.getMessage());
        }
    }

    private void switchToGameArea() {
        if (contentPane == null) {
            return;
        }
        gameArea = createGameArea();
        contentPane.setCenter(gameArea);
    }

    private void switchToPreGameArea() {
        if (contentPane == null) {
            return;
        }
        game = null;
        gameArea = createPreGameCenter();
        contentPane.setCenter(gameArea);
        showPreGameButtons();
    }

    private VBox createGameArea() {
        VBox mainArea = new VBox(10);
        mainArea.setPadding(new Insets(15));
        mainArea.setStyle("-fx-background-color: transparent;");
        
        statusLabel = new Label(game.getLastStatusMessage() != null ? game.getLastStatusMessage() : "Starting game...");
        statusLabel.setStyle("-fx-font-size: 32; -fx-font-weight: bold; -fx-text-fill: white;");

        gameLogArea = new TextArea();
        gameLogArea.setPrefRowCount(4);
        gameLogArea.setWrapText(true);
        gameLogArea.setEditable(false);
        gameLogArea.setStyle("-fx-font-family: monospace; -fx-font-size: 22; -fx-control-inner-background: #1a1a1a; -fx-text-fill: #00ff00;");

        dealerSectionBox = createDealerSection();
        HBox dealerBox = new HBox();
        dealerBox.setAlignment(Pos.CENTER);
        dealerBox.setStyle("-fx-background-color: transparent;");
        dealerBox.getChildren().add(dealerSectionBox);

        HBox middleSection = createMiddleSection();

        humanSectionBox = createHumanSection();
        HBox humanBox = new HBox();
        humanBox.setAlignment(Pos.CENTER);
        humanBox.setStyle("-fx-background-color: transparent;");
        humanBox.getChildren().add(humanSectionBox);

        HBox controlArea = createControlArea();
        if (humanSectionBox != null) {
            controlArea.setPrefWidth(humanSectionBox.getPrefWidth());
        }

        mainArea.getChildren().addAll(statusLabel, gameLogArea, dealerBox, middleSection, humanBox, controlArea);
        updateUI();
        restoreControlsForGameState();
        return mainArea;
    }
    private void restoreControlsForGameState() {
        if (game == null) {
            return;
        }

        if (betChoiceBox == null || saveBetButton == null ||
            hitButton == null || standButton == null) {
            return;
        }

        hitButton.setDisable(true);
        standButton.setDisable(true);

        GameState state = game.getGameState();

        switch (state) {
            case NEW_ROUND:
                betChoiceBox.setDisable(false);
                saveBetButton.setDisable(false);
                break;

            case PLAYER_TURN:
                betChoiceBox.setDisable(true);
                saveBetButton.setDisable(true);

                if (game.getCurrentPlayer() == game.getHumanPlayer()
                        && !game.getHumanPlayer().hasBusted()
                        && !game.getHumanPlayer().hasStood()) {
                    hitButton.setDisable(false);
                    standButton.setDisable(false);
                }
                break;

            case DEALER_TURN:
            case ROUND_OVER:
            default:
                betChoiceBox.setDisable(true);
                saveBetButton.setDisable(true);
                hitButton.setDisable(true);
                standButton.setDisable(true);
                break;
        }
    }


    private VBox createDealerSection() {
        VBox section = new VBox(3);
        section.setPadding(new Insets(10));
        section.setAlignment(Pos.TOP_CENTER);
        section.setPrefHeight(200);
        section.setPrefWidth(400);
        section.setMinHeight(200);
        section.setMinWidth(400);
        section.setStyle("-fx-border-color: #cccccc; -fx-border-width: 2; -fx-background-color: rgba(10, 93, 10, 0.5);");

        VBox leftBox = new VBox(5);
        leftBox.setAlignment(Pos.CENTER);
        leftBox.setPrefWidth(80);
        
        dealerNameLabel = new Label("DEALER");
        dealerNameLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: white; -fx-wrap-text: true;");
        
        ImageView dealerImg = new ImageView(new Image(getClass().getResourceAsStream("/game/blackjack/imageDealer.png")));
        dealerImg.setFitWidth(90);
        dealerImg.setFitHeight(90);
        
        section.getChildren().add(dealerNameLabel);
        leftBox.getChildren().addAll(dealerImg);
        
        VBox rightBox = new VBox(3);
        rightBox.setAlignment(Pos.TOP_LEFT);
        rightBox.setPrefWidth(300);
        
        dealerHandBox = createCardDisplayBox();
        dealerHandBox.setId("dealerHandBox");

        dealerScoreLabel = new Label("Score: 0");
        dealerScoreLabel.setStyle("-fx-font-size: 24; -fx-text-fill: white; -fx-font-weight: bold;");
        
        dealerMoneyLabel = new Label();
        dealerMoneyLabel.setStyle("-fx-font-size: 24; -fx-text-fill: #ffff00;");
        
        rightBox.getChildren().addAll(dealerHandBox, dealerScoreLabel);
        
        HBox mainBox = new HBox(10);
        mainBox.setAlignment(Pos.CENTER_LEFT);
        mainBox.getChildren().addAll(leftBox, rightBox);
        
        section.getChildren().add(mainBox);
        return section;
    }

    private HBox createMiddleSection() {
        HBox middle = new HBox(20);
        middle.setPadding(new Insets(10));
        middle.setAlignment(Pos.CENTER);
        middle.setStyle("-fx-background-color: transparent;");

        comp1SectionBox = createComputerSection(1);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        comp2SectionBox = createComputerSection(2);

        middle.getChildren().addAll(comp1SectionBox, spacer, comp2SectionBox);
        return middle;
    }
    
    private VBox createComputerSection(int compNum) {
        VBox section = new VBox(3);
        section.setPadding(new Insets(10));
        section.setAlignment(Pos.TOP_CENTER);
        section.setPrefHeight(200);
        section.setPrefWidth(350);
        section.setMinHeight(200);
        section.setMinWidth(350);
        section.setStyle("-fx-border-color: #cccccc; -fx-border-width: 2; -fx-background-color: rgba(10, 93, 10, 0.5);");

        Computer comp = (compNum == 1) ? game.getComputerPlayer1() : game.getComputerPlayer2();
        HBox handBox = (compNum == 1) ? comp1HandBox : comp2HandBox;
        Label nameLabel = (compNum == 1) ? comp1NameLabel : comp2NameLabel;
        Label betLabel = (compNum == 1) ? comp1BetLabel : comp2BetLabel;
        Label scoreLabel = (compNum == 1) ? comp1ScoreLabel : comp2ScoreLabel;
        
        if (handBox == null) {
            handBox = createCardDisplayBox();
            if (compNum == 1) {
                comp1HandBox = handBox;
                comp1HandBox.setId("comp1HandBox");
            } 
            else {
                comp2HandBox = handBox;
                comp2HandBox.setId("comp2HandBox");
            }
        }
        if (nameLabel == null) {
            nameLabel = new Label(comp.getUsername());
            if (compNum == 1) comp1NameLabel = nameLabel;
            else comp2NameLabel = nameLabel;
        }
        if (betLabel == null) {
            betLabel = new Label("Bet: $0");
            if (compNum == 1) comp1BetLabel = betLabel;
            else comp2BetLabel = betLabel;
        }
        if (scoreLabel == null) {
            scoreLabel = new Label("Score: 0");
            scoreLabel.setStyle("-fx-font-size: 24; -fx-text-fill: white; -fx-font-weight: bold;");
            if (compNum == 1) comp1ScoreLabel = scoreLabel;
            else comp2ScoreLabel = scoreLabel;
        }
        
        VBox leftBox = new VBox(5);
        leftBox.setAlignment(Pos.CENTER);
        leftBox.setPrefWidth(70);
        
        nameLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: white; -fx-wrap-text: true;");
        
        ImageView compImg = new ImageView(getCompPersonalityImage(comp));
        compImg.setFitWidth(90);
        compImg.setFitHeight(90);
        
        Label moneyLabel = (compNum == 1) ? comp1MoneyLabel : comp2MoneyLabel;
        if (moneyLabel == null) {
            moneyLabel = new Label("$" + comp.getMoney());
            if (compNum == 1) comp1MoneyLabel = moneyLabel;
            else comp2MoneyLabel = moneyLabel;
        }
        moneyLabel.setStyle("-fx-font-size: 20; -fx-text-fill: #ffff00;");
        
        section.getChildren().add(nameLabel);
        leftBox.getChildren().addAll(compImg, moneyLabel);
        
        VBox rightBox = new VBox(3);
        rightBox.setAlignment(Pos.TOP_LEFT);
        rightBox.setPrefWidth(250);
        
        scoreLabel.setStyle("-fx-font-size: 24; -fx-text-fill: white; -fx-font-weight: bold;");
        betLabel.setStyle("-fx-font-size: 20; -fx-text-fill: #ffff00;");
        
        rightBox.getChildren().addAll(handBox, scoreLabel, betLabel);
        
        HBox mainBox = new HBox(10);
        mainBox.setAlignment(Pos.CENTER_LEFT);
        mainBox.getChildren().addAll(leftBox, rightBox);
        
        section.getChildren().add(mainBox);
        return section;
    }

    private VBox createHumanSection() {
        VBox section = new VBox(3);
        section.setPadding(new Insets(10));
        section.setAlignment(Pos.TOP_CENTER);
        section.setPrefHeight(200);
        section.setPrefWidth(400);
        section.setMinHeight(200);
        section.setMinWidth(400);
        section.setStyle("-fx-border-color: #cccccc; -fx-border-width: 2; -fx-background-color: rgba(10, 93, 10, 0.5);");

        VBox leftBox = new VBox(5);
        leftBox.setAlignment(Pos.CENTER);
        leftBox.setPrefWidth(80);
        
        humanNameLabel = new Label(game.getHumanPlayer().getUsername());
        humanNameLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: white; -fx-wrap-text: true;");
        
        ImageView humanImg = new ImageView(new Image(getClass().getResourceAsStream("/game/blackjack/imageHuman.png")));
        humanImg.setFitWidth(90);
        humanImg.setFitHeight(90);
        
        humanMoneyLabel = new Label("$" + game.getHumanPlayer().getMoney());
        humanMoneyLabel.setStyle("-fx-font-size: 20; -fx-text-fill: #ffff00;");
        
        section.getChildren().add(humanNameLabel);
        leftBox.getChildren().addAll(humanImg, humanMoneyLabel);
        
        VBox rightBox = new VBox(3);
        rightBox.setAlignment(Pos.TOP_LEFT);
        rightBox.setPrefWidth(300);
        
        humanHandBox = createCardDisplayBox();
        humanHandBox.setId("humanHandBox");
    
        humanScoreLabel = new Label("Score: 0");
        humanScoreLabel.setStyle("-fx-font-size: 24; -fx-text-fill: white; -fx-font-weight: bold;");
        
        humanBetLabel = new Label("Bet: $0");
        humanBetLabel.setStyle("-fx-font-size: 20; -fx-text-fill: #ffff00;");
        
        rightBox.getChildren().addAll(humanHandBox, humanScoreLabel, humanBetLabel);
        
        HBox mainBox = new HBox(10);
        mainBox.setAlignment(Pos.CENTER_LEFT);
        mainBox.getChildren().addAll(leftBox, rightBox);
        
        section.getChildren().add(mainBox);
        return section;
    }

    private HBox createCardDisplayBox() {
        HBox handBox = new HBox(5);
        handBox.setStyle("-fx-border-color: #666666; -fx-border-width: 1; -fx-padding: 3; -fx-background-color: #0d0d0d;");
        handBox.setPrefHeight(80);
        handBox.setPrefWidth(280);
        handBox.setAlignment(Pos.CENTER_LEFT);
        return handBox;
    }

    private HBox createControlArea() {
        HBox controlArea = new HBox(20);
        controlArea.setPadding(new Insets(8));
        controlArea.setAlignment(Pos.CENTER);
        controlArea.setStyle("-fx-border-color: #999999; -fx-border-width: 1; -fx-background-color: #2a2a2a;");

        HBox bettingBox = new HBox(5);
        bettingBox.setAlignment(Pos.CENTER);

        Label betLabel = new Label("Bet: $");
        betLabel.setStyle("-fx-font-size: 22; -fx-text-fill: white;");
        
        betChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList(10, 25, 50, 100, 200, 500));
        betChoiceBox.setValue(50);
        betChoiceBox.setPrefWidth(70);
        betChoiceBox.setStyle("-fx-font-size: 20;");

        saveBetButton = new Button("Place Bet");
        saveBetButton.setStyle("-fx-font-size: 20; -fx-padding: 3 10;");
        saveBetButton.setOnAction(e -> placeBet());

        bettingBox.getChildren().addAll(betLabel, betChoiceBox, saveBetButton);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox actionBox = new HBox(8);
        actionBox.setAlignment(Pos.CENTER);

        hitButton = new Button("Hit");
        hitButton.setPrefWidth(60);
        hitButton.setPrefHeight(28);
        hitButton.setStyle("-fx-font-size: 20;");
        hitButton.setOnAction(e -> humanHit());
        hitButton.setDisable(true);

        standButton = new Button("Stand");
        standButton.setPrefWidth(90);
        standButton.setPrefHeight(28);
        standButton.setStyle("-fx-font-size: 20;");
        standButton.setOnAction(e -> humanStand());
        standButton.setDisable(true);

        actionBox.getChildren().addAll(hitButton, standButton);

        controlArea.getChildren().addAll(bettingBox, spacer, actionBox);
        return controlArea;
    }

    private void placeBet() {
        int betAmount = betChoiceBox.getValue();

        if (betAmount > game.getHumanPlayer().getMoney()) {
            statusLabel.setText("Bet exceeds available money!");
            return;
        }
        
        game.getHumanPlayer().placeBet(betAmount);
        
        if (game.getComputerPlayer1().getMoney() > 0) {
            game.getComputerPlayer1().placeBet(Math.min(50, game.getComputerPlayer1().getMoney()));
        }
        if (game.getComputerPlayer2().getMoney() > 0) {
            game.getComputerPlayer2().placeBet(Math.min(50, game.getComputerPlayer2().getMoney()));
        }

        game.bettingPhase();

        game.setLastStatusMessage("Dealing...");
        updateUI();

        betChoiceBox.setDisable(true);
        saveBetButton.setDisable(true);
        hitButton.setDisable(true);
        standButton.setDisable(true);

        List<Player> dealSeq = Arrays.asList(
            game.getDealer(), game.getHumanPlayer(), game.getComputerPlayer1(), game.getComputerPlayer2(),
            game.getDealer(), game.getHumanPlayer(), game.getComputerPlayer1(), game.getComputerPlayer2()
        );

        final int total = dealSeq.size();
        final AtomicInteger idx = new AtomicInteger(0);
        final Timeline[] tl = new Timeline[1];
        tl[0] = new Timeline(new KeyFrame(Duration.millis(CARD_DELAY_MS), event -> {
            int i = idx.getAndIncrement();
            if (i < total) {
                Player p = dealSeq.get(i);
                p.addCard(game.dealCard());
                updateUI();
            }
            if (idx.get() >= total) {
                tl[0].stop();
                game.finishDealing();
                if (!game.getHumanPlayer().hasBusted()) {
                    hitButton.setDisable(false);
                    standButton.setDisable(false);
                }
                updateUI();
            }
        }));
        tl[0].setCycleCount(total);
        tl[0].play();
    }

    private void humanHit() {
        game.humanPlayerHit();
        
        Card last = game.getHumanPlayer().getHand().get(game.getHumanPlayer().getHand().size() - 1);

        if (last.getRank() == Rank.PIZZA_DISCOUNT) {
            statusLabel.setText("You drew a special DISCOUNT PIZZA CARD!!!!");
            humanStand();
        }
        updateUI();
        
        if (game.getHumanPlayer().hasBusted()) {
            hitButton.setDisable(true);
            standButton.setDisable(true);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(COMPUTER_TURN_DELAY_MS), event -> {
                playComputerTurns();
            }));
            timeline.setCycleCount(1);
            timeline.play();
        }
    }

    private void humanStand() {
        game.humanPlayerStand();
        hitButton.setDisable(true);
        standButton.setDisable(true);
        updateUI();
        
        playComputerTurns();
    }

    private void playComputerTurns() {
        playNextComputerTurn(1);
    }

    private void playNextComputerTurn(int playerIndex) {
        if (playerIndex < 3) {
            Player p = game.getPlayers().get(playerIndex);
            if (!(p instanceof Computer) || p.getBetAmount() == 0) {
                playNextComputerTurn(playerIndex + 1);
                return;
            }

            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(CARD_DELAY_MS), event -> {
                game.processPlayerTurn(p);
                updateUI();
                playNextComputerTurn(playerIndex + 1);
            }));
            timeline.setCycleCount(1);
            timeline.play();
        } else {
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(DEALER_TURN_DELAY_MS), event -> {
                game.dealerTurn();
                updateUI();
            }));
            timeline.setCycleCount(1);
            timeline.play();
        }
    }

    private void updateUI() {
        if (game == null) return;

        if (statusLabel != null) {
            statusLabel.setText(game.getLastStatusMessage());
        }
        if (gameLogArea != null) {
            gameLogArea.appendText(game.getLastStatusMessage() + "\n");
        }

        if (gameStateLabel != null) {
            gameStateLabel.setText("State: " + game.getGameState().toString());
        }

        if (dealerHandBox != null) {
            updatePlayerDisplay(dealerHandBox, dealerNameLabel, dealerScoreLabel, game.getDealer(), dealerMoneyLabel, null, game.getGameState() == GameState.DEALER_TURN);
        }
        if (comp1HandBox != null) {
            updatePlayerDisplay(comp1HandBox, comp1NameLabel, comp1ScoreLabel, game.getComputerPlayer1(), comp1MoneyLabel, comp1BetLabel, game.getCurrentPlayer() == game.getComputerPlayer1());
        }
        if (comp2HandBox != null) {
            updatePlayerDisplay(comp2HandBox, comp2NameLabel, comp2ScoreLabel, game.getComputerPlayer2(), comp2MoneyLabel, comp2BetLabel, game.getCurrentPlayer() == game.getComputerPlayer2());
        }
        if (humanHandBox != null) {
            updatePlayerDisplay(humanHandBox, humanNameLabel, humanScoreLabel, game.getHumanPlayer(), humanMoneyLabel, humanBetLabel, game.getCurrentPlayer() == game.getHumanPlayer());
        }

        if (game.getGameState() == GameState.ROUND_OVER) {
            final BlackJackGame roundGame = this.game;

            if (game.isGameOver()) {
                int maxMoney = game.getMaxMoneyReached();
                try {
                    FileManager.updateGlobalScore(username, maxMoney, "blackjack");
                } 
                catch (Exception e) {
                    System.err.println("Error updating highscore: " + e.getMessage());
                }
                Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2000), event -> {
                    if (this.game != roundGame) {
                        return;
                    }
                    if (this.game == null || this.game.getGameState() != GameState.ROUND_OVER) {
                    }
                    showAlert("You are bankrupt! Maximum balance reached: $" + maxMoney);
                    switchToPreGameArea();
                }));
                timeline.setCycleCount(1);
                timeline.play();
                return;
            }

            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(3000), event -> {
                if (this.game != roundGame) {
                    return;
                }
                if (this.game == null || this.game.getGameState() != GameState.ROUND_OVER) {
                    return;
                }
                this.game.startNewRound();
                betChoiceBox.setDisable(false);
                saveBetButton.setDisable(false);
                hitButton.setDisable(true);
                standButton.setDisable(true);

                updateUI();
            }));
            timeline.setCycleCount(1);
            timeline.play();
        }
    }

    private void updatePlayerDisplay(HBox handDisplayBox, Label nameLabel, Label scoreLabel, Player player, Label moneyLabel, Label betLabel, boolean isCurrentPlayer) {
        if (handDisplayBox == null) return;

        handDisplayBox.getChildren().clear();

        List<Card> cardsToShow = new ArrayList<>();
        if (player instanceof Dealer && player.getHand().size() > 1 && 
            game.getGameState() != GameState.ROUND_OVER && 
            game.getGameState() != GameState.DEALER_TURN) {
            cardsToShow.add(player.getHand().get(0));
            Label hiddenLabel = new Label("[Hidden]");
            hiddenLabel.setStyle("-fx-font-size: 24; -fx-text-fill: #aaaaaa; -fx-alignment: center; -fx-padding: 3;");
            handDisplayBox.getChildren().add(hiddenLabel);
        } 
        else {
            cardsToShow.addAll(player.getHand());
        }

        for (Card card : cardsToShow) {
            Label cardLabel = new Label(createCardTextDisplay(card));
            cardLabel.setStyle("-fx-font-size: 24; -fx-text-fill: white; -fx-alignment: center; -fx-padding: 3; -fx-border-color: #555555; -fx-border-width: 1;");
            handDisplayBox.getChildren().add(cardLabel);
        }

        if (scoreLabel != null) {
            if (player instanceof Dealer && player.getHand().size() > 1 &&
                game.getGameState() != GameState.ROUND_OVER && game.getGameState() != GameState.DEALER_TURN) {
                scoreLabel.setText("Score: Hidden");
                scoreLabel.setStyle("-fx-font-size: 24; -fx-text-fill: #ffffaa; -fx-font-weight: bold;");
            } 
            else {
                String text = "Score: " + player.getCurrScore();
                if (player.hasBusted()) {
                    text += " (BUST)";
                    scoreLabel.setStyle("-fx-font-size: 24; -fx-text-fill: #ff6666; -fx-font-weight: bold;");
                } 
                else {
                    scoreLabel.setStyle("-fx-font-size: 24; -fx-text-fill: white; -fx-font-weight: bold;");
                }
                scoreLabel.setText(text);
            }
        }

        if (isCurrentPlayer && game.getGameState() != GameState.ROUND_OVER) {
            nameLabel.setStyle("-fx-font-size: 28; -fx-font-weight: bold; -fx-text-fill: #ffff00; -fx-background-color: #0066cc; -fx-padding: 5;");
        } 
        else {
            nameLabel.setStyle("-fx-font-size: 28; -fx-font-weight: bold; -fx-text-fill: white;");
        }

        if (moneyLabel != null) {
            moneyLabel.setText("$" + player.getMoney());
        }
        
        if (betLabel != null) {
            int currentBet = player.getBetAmount();
            if (currentBet > 0) {
                betLabel.setText("Bet: $" + currentBet);
            } 
            else {
                betLabel.setText("Bet: $0");
            }
        }
    }

    private String createCardTextDisplay(Card card) {
        String rankStr = "";
        String suitStr = "";
        switch (card.getRank()) {
            case ACE: 
                rankStr = "A"; 
                break;
            case JACK: 
                rankStr = "J"; 
                break;
            case QUEEN: 
                rankStr = "Q"; 
                break;
            case KING: 
                rankStr = "K"; 
                break;
            case TEN: 
                rankStr = "10"; 
                break;
            case TWO: 
                rankStr = "2"; 
                break;
            case THREE: 
                rankStr = "3"; 
                break;
            case FOUR: 
                rankStr = "4"; 
                break;
            case FIVE: 
                rankStr = "5"; 
                break;
            case SIX: 
                rankStr = "6"; 
                break;
            case SEVEN: 
                rankStr = "7"; 
                break;
            case EIGHT: 
                rankStr = "8"; 
                break;
            case NINE: 
                rankStr = "9"; 
                break;
            case PIZZA_DISCOUNT: 
                rankStr = "30%"; 
                break;
            default: 
                rankStr = card.getRank().toString(); 
                break;
        }
        switch (card.getSuit().toString()) {
            case "HEARTS": 
                suitStr = "♥"; 
                break;
            case "DIAMONDS": 
                suitStr = "♦"; 
                break;
            case "CLUBS": 
                suitStr = "♣"; 
                break;
            case "SPADES": 
                suitStr = "♠"; 
                break;
            case "PIZZA_COUPON": 
                suitStr = "🍕"; 
                break;
            default: 
                suitStr = card.getSuit().toString(); 
                break;
        }
        
        return rankStr + " " + suitStr;
    }

    private Image getCompPersonalityImage(Player p) {
        try {
            if (p instanceof Computer) {
                Computer c = (Computer) p;
                int idx = Math.max(1, c.getPersonality().ordinal() + 1);
                String path = String.format("/game/blackjack/image%d.png", idx);
                return new Image(getClass().getResourceAsStream(path));
            }
        } 
        catch (Exception e) {

        }
        return new Image(getClass().getResourceAsStream("/game/blackjack/image1.png"));
    }

    private boolean isSaveAvailableForUser(String username) throws Exception {
        Path p = Paths.get("txtfiles", "blackjack.txt");
        if (!Files.exists(p) || Files.size(p) == 0) return false;
        List<String> lines = Files.readAllLines(p, java.nio.charset.StandardCharsets.UTF_8);
        for (String line : lines) {
            if (line.startsWith(username + ":")) return true;
        }
        return false;
    }

    public void saveGame() {
        try {
            if (game == null) {
                showAlert("No game to save.");
                return;
            }
            String pwd = promptForPassword("Enter account password to encrypt save:");
            if (pwd == null) {
                showAlert("Save cancelled.");
                return;
            }
            String saveJson = game.generateSaveStateString();
            FileManager.saveBlackjackSave(username, saveJson, pwd);
            showAlert("Game saved successfully.");
        } 
        catch (Exception e) {
            showAlert("Error saving game: " + e.getMessage());
        }
        try {
            FileManager.updateGlobalScore(username, game.getMaxMoneyReached(), "blackjack");
        } 
        catch (Exception e) {
            System.err.println("Error updating highscore: " + e.getMessage());
        }
    }

    private String promptForPassword(String message) {
        final String[] result = new String[1];
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Password Required");

        Label lbl = new Label(message);
        PasswordField pf = new PasswordField();
        pf.setPromptText("Password");
        Button ok = new Button("OK");
        Button cancel = new Button("Cancel");
        ok.setOnAction(e -> {
            result[0] = pf.getText();
            dialog.close();
        });
        cancel.setOnAction(e -> {
            result[0] = null;
            dialog.close();
        });

        HBox buttons = new HBox(10, ok, cancel);
        buttons.setAlignment(Pos.CENTER);
        VBox box = new VBox(10, lbl, pf, buttons);
        box.setPadding(new Insets(10));
        box.setAlignment(Pos.CENTER);

        Scene s = new Scene(box, 360, 140);
        dialog.setScene(s);
        dialog.showAndWait();
        return result[0];
    }

    private void showAlert(String message) {
        Label label = new Label(message);
        label.setStyle("-fx-font-size: 28;");
        VBox vbox = new VBox(label);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);
        
        Stage alertStage = new Stage();
        alertStage.setTitle("Alert");
        alertStage.setScene(new Scene(vbox, 300, 100));
        alertStage.show();
    }
    public StackPane getRootNode() {
        if (gameScene == null) {
            return null;
        }
        if (gameScene.getRoot() instanceof StackPane) {
            return (StackPane) gameScene.getRoot();
        } 
        else if (gameScene.getRoot() instanceof BorderPane) {
            BorderPane bp = (BorderPane) gameScene.getRoot();
            if (bp.getCenter() instanceof StackPane) {
                return (StackPane) bp.getCenter();
            }
        }
        return null;
    }

    public BlackJackGame getGame() {
        return game;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("BlackJack");
        primaryStage.setWidth(1200);
        primaryStage.setHeight(800);
        ToolBar hubBar = toolbarc;
        BorderPane root = createGameSceneWithCustomToolbar(hubBar);
        gameScene = new Scene(root);
        primaryStage.setScene(gameScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
