package game.snake;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import game.gamemanager.BackgroundSetUp;
import game.gamemanager.ToolBarC;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class SnakeGame extends BackgroundSetUp {

    private Label currScoreLabel = new Label(); //restart from 0 everytime game restarts -->use currScoreLabel.setText(String.valueOf(newScore)); to update it with real time
    private int score = 0;
    private VBox vBox; //top = toolbar, right beneath the toolbar = score (Hbox), the rest = canvas
    private ToolBarC toolBarC;

    private StackPane stackPaneRoot;
    private BorderPane borderPane;

    private HBox scoreHBox;
    private HBox gameOverScoreHBox;

    private StackPane canvasHolderStackPane;
    private Button restartButton = new Button("Play Again");

    private int[] snakeGameScores;
    private Path highScoresFilePath;
    private String username;

    private Canvas canvas;
    private GraphicsContext gc;
    private final int CELL_SIZE = 28; 
    private int[][] gridCanvas; 
    private Random random = new Random(); 
    private List<SnakeBody> snake = new ArrayList<>();
    private Direction currDirection = Direction.RIGHT;
    private Direction newDirection;
    private Food food;
    private AnimationTimer animationTimer;

    private boolean isGameOver = false;
    private boolean playerHitFirstKey = false;
    private boolean gamePaused = false;

    private Image snakeHead = new Image("/game/snake/ChatGPTGeneratedSnakeHead.png");
    private Image arrowEmoji = new Image("/game/snake/ChatGPTGeneratedArrowKey.png");
    private Image gameOverImage = new Image("/game/snake/ChatGPTGeneratedGameOver.png");

    private Image gamePauseImage = new Image("/game/snake/ChatGPTGeneratedGamePause.png");
    private ImageView pauseOverlay = new ImageView(gamePauseImage);

    public SnakeGame(Path highestScoresFilePath, int[] snakeGameScores, String username, ToolBarC toolBarC){ 
        this.toolBarC = toolBarC; 
        this.snakeGameScores = snakeGameScores;
        this.highScoresFilePath = highestScoresFilePath;
        this.username = username;
        this.scoreHBox = initScoreHBox(); //incorporating abstraction everywhere possible to make code readable
        this.canvas = constructCanvas(); 
        constructVBox(); 
        restartButton.setVisible(false);
        pauseOverlay.setVisible(false);

        constructBackgroundPane("/game/gamemanager/gamepad_bg_800x600.png");
        constructDimAndBlurBackgroundImage();

        StackPane centerStack = new StackPane(this.vBox); //to make vBox goes in the middle of the scene
        StackPane.setAlignment(this.vBox, Pos.CENTER);


        this.borderPane = new BorderPane();

        borderPane.setTop(toolBarC.getToolBar());
        borderPane.setCenter(centerStack);

        this.stackPaneRoot = new StackPane(backgroundPane, dimOverlay, this.borderPane);
        StackPane.setAlignment(this.borderPane, Pos.TOP_CENTER);

        this.stackPaneRoot.setOnKeyPressed(event -> {

            KeyCode keyCode = event.getCode();
            
            if(keyCode.isArrowKey()){
                playerHitFirstKey = true;
            }
            switch(keyCode){
                case KeyCode.UP -> newDirection = Direction.UP;
                case KeyCode.DOWN -> newDirection = Direction.DOWN;
                case KeyCode.RIGHT -> newDirection = Direction.RIGHT;
                case KeyCode.LEFT -> newDirection = Direction.LEFT;
                case KeyCode.ESCAPE -> {
                    if(!gamePaused && playerHitFirstKey && !isGameOver){
                        pauseGame();
                    }
                    else{
                        animationTimer.start();
                        gamePaused = false;
                        pauseOverlay.setVisible(false);
                    }
                } 
                default -> {}   
            }
        });
        restartButton.setOnAction(event -> {
            startGame();
        });
    }//end of constructor

    private HBox initScoreHBox(){
         HBox hBox = new HBox(15);
        // hBox.setPadding(new Insets(20));
         hBox.setAlignment(Pos.TOP_RIGHT);

         Label scoreLabel = new Label("Score: ");
         scoreLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
         currScoreLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
         currScoreLabel.setText(String.valueOf(score));
         hBox.getChildren().addAll(scoreLabel, currScoreLabel);
         return hBox;
    } //end of initScoreHBox

    private Canvas constructCanvas(){
        Canvas canvasL = new Canvas(700, 448);
        this.gc = canvasL.getGraphicsContext2D();

        this.gridCanvas = new int[448 / CELL_SIZE][700 / CELL_SIZE]; //first index = row, second index = column 

        gc.setFill(Color.web("#b38effff"));
        gc.fillRect(0, 0, canvasL.getWidth(), canvasL.getHeight());

        return canvasL;
    }//end of constructCanvas

    @Override
    public void constructVBox(){
        this.vBox = new VBox(10);
        vBox.setPadding(new Insets(15));

        this.canvasHolderStackPane = new StackPane(canvas);
        this.canvasHolderStackPane.setStyle("-fx-border-color: black; -fx-border-width: 10;"); //add visible border
        this.canvasHolderStackPane.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE); //FORCES StackPane not to stretch the canvas
        this.canvasHolderStackPane.setPadding(Insets.EMPTY);
        this.canvasHolderStackPane.getChildren().addAll(restartButton, pauseOverlay);

        pauseOverlay.setManaged(false);
        pauseOverlay.setFitWidth(720);
        pauseOverlay.setFitHeight(460);
        pauseOverlay.setPreserveRatio(false);
       
        vBox.setAlignment(Pos.TOP_CENTER); 
        vBox.getChildren().addAll(this.scoreHBox, canvasHolderStackPane);
    }//end of constructRoot

    private void createFood(){ 
        int row = 1 + random.nextInt(16 - 2); //subtract available pixel coordinates - 2 to avoid displaying food on the border
        int col = 1 + random.nextInt(25 - 2); 

        FoodType randomFoodType = FoodType.values()[random.nextInt(FoodType.values().length)];
        this.food = new Food(randomFoodType, row, col);
    }//end of createFood

    public void renderLines(){
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(1);

        for(int row = 0; row < 16; row++){
            this.gc.strokeLine(0, row * CELL_SIZE, 720, row * CELL_SIZE);//syntax: gc.strokeLine(x1, y1, x2, y2);
        }
        for(int col = 0; col < 25; col++){
            this.gc.strokeLine(col * CELL_SIZE, 0, col * CELL_SIZE, 460);
        }
    }

    public void renderFood(){
        double cellSizeTimes = 1.3; //make food a bit bigger

        int y = this.food.getRow() * CELL_SIZE; //convert food position from grid coordinates to pixel
        int x = this.food.getColumn() * CELL_SIZE;

        gc.drawImage(this.food.getFoodType().getImage(), x, y, CELL_SIZE * cellSizeTimes, CELL_SIZE * cellSizeTimes);
    }//end of renderFood

    public void renderSnake(){

        int x, y; //to convert grid coordinates to pixel

        gc.clearRect(0,0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.web("#FADA5E"));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        renderLines();

        for(int i = 0; i < snake.size(); i++){

            SnakeBody part = snake.get(i);
            x = part.getCol() * CELL_SIZE;
            y = part.getRow() * CELL_SIZE;

            if(i == 0){
                gc.save(); //store the current, normal orientation
                gc.translate(x + CELL_SIZE/2.0, y + CELL_SIZE/2.0); //move pibot to the center of the cell //to rotate around the head's center (not the whole canvas)
                switch(currDirection){ //apply rotation 
                    case RIGHT -> {} //align with the given snakehead image
                    case DOWN -> gc.rotate(90);
                    case LEFT -> gc.scale(-1,1); //flip the right-headed snakehead horizontally 
                    case UP -> gc.rotate(270);
                }
                gc.drawImage(snakeHead, -CELL_SIZE / 2.0, -CELL_SIZE / 2.0, CELL_SIZE, CELL_SIZE); //draw the snakeHead centered at the pivot
                gc.restore(); //returns everything to normal after drawing the rotated head 
            }
            else{
                gc.setFill(Color.GREEN);
                gc.fillRoundRect(x+2, y+2, CELL_SIZE-4, CELL_SIZE-4, 10, 10); //Rounded green body segment (inset 2 pixel for smoothness and corner radius = 10)
            }
        }

    }//end of renderSnake()

    public void initSnake(){
        int startRow = 4 + random.nextInt(8); //snake start square near the center of the map
        int startCol = 3 + random.nextInt(13);

        //snake.clear();

        SnakeBody head = new SnakeBody(startRow, startCol);
        snake.add(head);

        for(int i = 0; i < 2; i++){
            switch(currDirection){
                case UP -> startRow++;
                case DOWN -> startRow--;
                case RIGHT -> startCol--;
                case LEFT -> startCol++;
            }
            snake.add(new SnakeBody(startRow, startCol));
        }
    }//end of initSnake

    public void moveSnake(Direction newDirection){
        SnakeBody head = snake.get(0);

        if(currDirection == newDirection){ // keep moving straight 
            moveSnakeStraight(head);
        } 
        else if (isOppositeDirection(newDirection)){ // ignore the change: Snake can't reverse into itself
            this.newDirection = this.currDirection; // keep moving in the currDirection
            moveSnakeStraight(head);  
        }
        else { //currDirection and newDirection are perpendicular (valid move: perpendicular direction) 
            moveSnakePerpendicular(head);
        } 
    }//end of moveSnake

    public boolean isOppositeDirection(Direction newDirection){ 
        return (currDirection == Direction.UP && newDirection == Direction.DOWN) || 
        (currDirection == Direction.DOWN && newDirection == Direction.UP) || 
        (currDirection == Direction.RIGHT && newDirection == Direction.LEFT) || 
        (currDirection == Direction.LEFT && newDirection == Direction.RIGHT);
    }

    public void moveSnakeStraight(SnakeBody head){
        int newHeadRow = head.getRow();
        int newHeadCol = head.getCol();
        SnakeBody newHead;

        switch(currDirection){
            case UP -> newHeadRow--;
            case DOWN -> newHeadRow++;
            case RIGHT -> newHeadCol++;
            case LEFT -> newHeadCol--;
        }

        newHead = new SnakeBody(newHeadRow, newHeadCol);
        updateSnakeArrayList(newHead);
    }//end of moveSnakeStraight

    public void moveSnakePerpendicular(SnakeBody head){
        int newHeadRow = head.getRow();
        int newHeadCol = head.getCol();
        SnakeBody newHead;

        if(currDirection == Direction.UP || currDirection == Direction.DOWN) { // UP -> LEFT/RIGHT and DOWN -> LEFT/RIGHT
            switch(newDirection){
                case LEFT -> newHeadCol--;
                case RIGHT -> newHeadCol++;
                default -> {}
            }
        } //end UP -> LEFT/RIGHT and DOWN -> LEFT/RIGHT

        else if (currDirection == Direction.LEFT || currDirection == Direction.RIGHT){ //LEFT -> UP/DOWN and RIGHT -> UP/DOWN
            switch(newDirection) {
                case UP -> newHeadRow--;
                case DOWN -> newHeadRow++;
                default -> {} 
            }
        }//end LEFT -> UP/DOWN and RIGHT -> UP/DOWN

        newHead = new SnakeBody(newHeadRow, newHeadCol);
        updateSnakeArrayList(newHead);
    }// end of moveSnakePerpendicular

    public boolean collideWithWall(SnakeBody newPart){
        int row = newPart.getRow();
        int col = newPart.getCol();

        if(row < 1 || row > 14){ return true; }
        if(col < 1 || col > 23){ return true; }
        return false;
    }

    public boolean collideWithSelf(SnakeBody newPart){
        int row = newPart.getRow();
        int col = newPart.getCol();

        for(int i = 0; i < snake.size(); i++){ 
            if((i == snake.size() - 1) && !isEating(row, col)){ //if the snake don't eat any food at the newHead position, the prev tail will be removed (the snake is not growing)
                break;
            }

            SnakeBody self = snake.get(i);
            if(self.getRow() == row && self.getCol() == col) { 
                return true; 
            }
        }

        return false;
    }

    public boolean isEating(int row, int col){
        if(row == this.food.getRow() && col == this.food.getColumn()){ return true; }
        return false;
    }

    public boolean collide(SnakeBody newPart){
        return collideWithWall(newPart) || collideWithSelf(newPart);
    }

    public void updateSnakeArrayList(SnakeBody newPart){
        if(!collide(newPart)){
            snake.add(0, newPart);
            
            if(!isEating(newPart.getRow(), newPart.getCol())){ //check if the newHead eats a food, in that case, keep the tail, and vice versa
                snake.remove(snake.size() - 1); //normal move, no growth
            }
            else{ //food is eaten, keep the previous tail
                updateStateSinceFoodIsEaten();
            }
            this.currDirection = this.newDirection;
        }
        else{
            //Collision occurs, handle GameOver here
            gameOver();
        }
    }//end of updateSnakeArrayList

    public void updateStateSinceFoodIsEaten(){
        this.score++;
        currScoreLabel.setText(String.valueOf(this.score));
        createFood();
    }

    public void gameOver(){
        if(needToUpdateSnakeGameScoresArray(this.score)){  //Instantly write to the file here so that even when user close the program after the game is over, the file will always get updated 
            writeToFile();
        }
        this.animationTimer.stop();
        this.isGameOver = true;
        gamePaused = false;
        pauseOverlay.setVisible(false);
        gc.clearRect(0,0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(gameOverImage, 0, 0, 700, 448);

        displayScoreOnGameOver();
    }//end of gameover()

    private void writeToFile(){
        if(Files.exists(highScoresFilePath) && Files.isReadable(highScoresFilePath) && Files.isWritable(highScoresFilePath)){
            try{
                List<String> lines = Files.readAllLines(highScoresFilePath);
                for(int i=0; i < lines.size(); i++) {
                    String line = lines.get(i);
                    String[] parts = line.split(":", 11);
                    if(parts[0].equals(this.username)){
                        //update this user's snake game scores
                        String updatedString = 
                        parts[0] + ":" +
                        snakeGameScores[0] + ":" +
                        snakeGameScores[1] + ":" +
                        snakeGameScores[2] + ":" +
                        snakeGameScores[3] + ":" +
                        snakeGameScores[4] + ":" +
                        parts[6] + ":" +
                        parts[7] + ":" +
                        parts[8] + ":" +
                        parts[9] + ":" +
                        parts[10];
                        lines.set(i, updatedString);
                        break;
                    }
                }
                Files.write(highScoresFilePath, lines); //replace the old high_scores.txt
            }catch(IOException e){
                System.out.println("Failed to update high scores."); //should not happen TT
                e.printStackTrace();
            }
        }
    }

    public void displayScoreOnGameOver(){

        Label yourScoreLabel = new Label("Your Score: ");
        yourScoreLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold;");
        
        Label actualScoreLabel = new Label(String.valueOf(score));
        actualScoreLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold;");

        gameOverScoreHBox = new HBox(15);
        gameOverScoreHBox.setPadding(new Insets(10));
        gameOverScoreHBox.setMaxWidth(Region.USE_PREF_SIZE); //to avoid HBox stretching/interfering with Pos
        gameOverScoreHBox.getChildren().addAll(yourScoreLabel, actualScoreLabel);
        

        StackPane.setAlignment(gameOverScoreHBox, Pos.TOP_CENTER);
        StackPane.setMargin(gameOverScoreHBox, new Insets(220, 0, 0, 0));

        StackPane.setAlignment(restartButton, Pos.TOP_CENTER);
        StackPane.setMargin(restartButton, new Insets(300, 0, 0, 0));

     
        restartButton.setStyle("-fx-background-color: slateblue; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 10 20;");

        canvasHolderStackPane.getChildren().add(gameOverScoreHBox);

        gameOverScoreHBox.setVisible(true);
        restartButton.setVisible(true);

        gameOverScoreHBox.setMouseTransparent(true); //scoreHBox don't consume mouse events
    }

    public void pauseGame(){
        gamePaused = true;
        pauseOverlay.setVisible(true);
        animationTimer.stop();
    }

    public void startGame(){

        //reset states
        //clear the canvas
        gc.clearRect(0,0, canvas.getWidth(), canvas.getHeight());
        if(this.animationTimer != null) {
            this.animationTimer.stop(); 
        }
        if(gameOverScoreHBox != null){
            this.gameOverScoreHBox.setVisible(false);
        }
        isGameOver = false;
        playerHitFirstKey = false;
        currDirection = Direction.getRandomDirection(random);
        newDirection = currDirection;
        restartButton.setVisible(false);
        this.score = 0;
        currScoreLabel.setText(String.valueOf(score));
        snake.clear();
        

        //initialize
        initSnake();
        createFood();
        
        animationTimer = new AnimationTimer() {
            
            long lastFrameTimeStamp = 0; //in nanoseconds
            long timeInterval = 0;

            @Override
            public void handle(long nowFrameTimeStamp){
                if(isGameOver){
                    return;
                }
    
                renderSnake(); //render snake and food even when player hasn't started playing
                renderFood();
                if(!playerHitFirstKey){
                    gc.drawImage(arrowEmoji, 500, 20, 200, 100);
                    return;
                }

                if(playerHitFirstKey && lastFrameTimeStamp == 0){ //first frame: initialize clock //only start the game when player first pressed a valid arrow key
                    lastFrameTimeStamp = nowFrameTimeStamp;
                    return;
                }

                timeInterval = nowFrameTimeStamp - lastFrameTimeStamp;

                if(timeInterval > 159_000_000){

                    moveSnake(newDirection);

                    if(isGameOver){ return; } 
                    lastFrameTimeStamp = nowFrameTimeStamp;
                }
            }
        };
        this.animationTimer.start(); //start the loop
        this.stackPaneRoot.requestFocus();
    }//end of startGame()

    private boolean needToUpdateSnakeGameScoresArray(int currScore){
        boolean firstSmallerScore = true;
        int leastHighestScore = this.snakeGameScores[4];
        int temp = 0;
        if(currScore > leastHighestScore){
            //keep comparing
            for(int i = 0; i < snakeGameScores.length; i++){
                if(this.snakeGameScores[i] < currScore && firstSmallerScore){ //shift down
                    temp = this.snakeGameScores[i];
                    this.snakeGameScores[i] = currScore;
                    firstSmallerScore = false;
                }
                if(!firstSmallerScore && currScore > snakeGameScores[i]){
                    int tempT = this.snakeGameScores[i];
                    this.snakeGameScores[i] = temp;
                    temp = tempT;
                } //then just drop the last one 
            }
        }
        return !firstSmallerScore;
    }   
    public int[] getUpdatedSnakeGameScores(){
        return this.snakeGameScores;
    }

    public StackPane getRootNode(){ return this.stackPaneRoot; }

}
