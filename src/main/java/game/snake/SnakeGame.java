package game.snake;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class SnakeGame {

    private Label currScoreLabel = new Label(); //restart from 0 everytime game restarts -->use currScoreLabel.setText(String.valueOf(newScore)); to update it with real time
    private int score = 0;
    private VBox root; //top = toolbar, right beneath the toolbar = score (Hbox), the rest = canvas
    private ToolBar toolBar;
    private HBox scoreHBox;
    private Canvas canvas;
    private GraphicsContext gc;
    private final int CELL_SIZE = 28; 
    private int[][] gridCanvas; 
    private Random random = new Random(); 
    private List<SnakeBody> snake = new ArrayList<>();
    private Direction currDirection = Direction.RIGHT;
    private Food food;
    private Image snakeHead = new Image("/game/snake/ChatGPTGeneratedSnakeHead.png");
    private AnimationTimer animationTimer;
    /*
     * TODO: Paste the ToolBar logic + UI and add it to the root (VBox)
     */

    public SnakeGame(){ 
        this.toolBar = initToolBar(); //incorporating abstraction everywhere possible to make code readable
        this.scoreHBox = initScoreHBox();
        this.canvas = constructCanvas(); //assuming we let Stage Size be 800 x 600
        this.root = constructRoot();
        animationTimer = new AnimationTimer() {
            
            long lastFrameTimeStamp = 0; //in nanoseconds
            long timeInterval = 0;

            @Override
            public void handle(long nowFrameTimeStamp){
                
                if(lastFrameTimeStamp == 0){ //first frame: initialize clock
                    lastFrameTimeStamp = nowFrameTimeStamp;
                    renderSnake();
                    renderFood();
                    return;
                }

                timeInterval = nowFrameTimeStamp - lastFrameTimeStamp;

                if(timeInterval > 155_000_000){

                    moveSnake(Direction.RIGHT);
                    //movement

                    renderSnake();
                    renderFood();
                    lastFrameTimeStamp = nowFrameTimeStamp;
                }
            }
        };
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
         currScoreLabel.setText(String.valueOf(score));
         hBox.getChildren().addAll(scoreLabel, currScoreLabel);
         return hBox;
    } //end of initScoreHBox

    private Canvas constructCanvas(){
        Canvas canvasL = new Canvas(700, 448);
        this.gc = canvasL.getGraphicsContext2D();

        this.gridCanvas = new int[448 / CELL_SIZE][700 / CELL_SIZE]; //first index = row, second index = column 

        gc.setFill(Color.web("#FADA5E"));
        gc.fillRect(0, 0, canvasL.getWidth(), canvasL.getHeight());

        //set visible border
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
    }//end of constructRoot

    private void createFood(){ 
        int[] rowColPair = new int [2];

        int row = random.nextInt(16 - 2); //subtract available pixel coordinates - 2 to avoid displaying food on the border
        int col = random.nextInt(25 - 2); 

        FoodType randomFoodType = FoodType.values()[random.nextInt(FoodType.values().length)];
        this.food = new Food(randomFoodType, row, col);
    }//end of createFood

    public void renderFood(){
        double cellSizeTimes = 1.5; //make food a bit bigger

        int y = this.food.getRow() * CELL_SIZE; //convert food position from grid coordinates to pixel
        int x = this.food.getColumn() * CELL_SIZE;

        gc.drawImage(this.food.getFoodType().getImage(), x, y, CELL_SIZE * cellSizeTimes, CELL_SIZE * cellSizeTimes);
    }//end of renderFood

    public void renderSnake(){

        int x, y; //to convert grid coordinates to pixel

        gc.clearRect(0,0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.web("#FADA5E"));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        //set visible border
        gc.setLineWidth(10); 
        gc.strokeRect(5, 5, canvas.getWidth() - 10, canvas.getHeight() - 10);

        for(int i = 0; i < snake.size(); i++){

            SnakeBody part = snake.get(i);
            x = part.getCol() * CELL_SIZE;
            y = part.getRow() * CELL_SIZE;

            if(i == 0){
                gc.drawImage(snakeHead, x, y, CELL_SIZE, CELL_SIZE);
            }
            else{
                gc.setFill(Color.GREEN);
                gc.fillRoundRect(x+2, y+2, CELL_SIZE-4, CELL_SIZE-4, 10, 10); //Rounded green body segment (inset 2 pixel for smoothness and corner radius = 10)
            }
        }

    }//end of renderSnake()

    public void initSnake(){
        int startRow = 8;
        int startCol = 12;

        SnakeBody head = new SnakeBody(startRow, startCol--);
        snake.add(head);

        for(int i = 0; i < 2; i++){
            snake.add(new SnakeBody(startRow, startCol--));
        }

        
    }//end of initSnake

    public void moveSnake(Direction newDirection){
        SnakeBody head = snake.get(0);

        if(currDirection == newDirection){ // keep moving straight 
            moveSnakeStraight(head, newDirection);
        } 
        else if (isOppositeDirection(newDirection)){ // ignore the change: Snake can't reverse into itself
            moveSnakeStraight(head, currDirection);  // keep moving in the currDirection
        }
        else { //currDirection and newDirection are perpendicular (valid move: perpendicular direction) 
            moveSnakePerpendicular(head, newDirection);
        } 
    }//end of moveSnake

    public boolean isOppositeDirection(Direction newDirection){ 
        return (currDirection == Direction.UP && newDirection == Direction.DOWN) || 
        (currDirection == Direction.DOWN && newDirection == Direction.UP) || 
        (currDirection == Direction.RIGHT && newDirection == Direction.LEFT) || 
        (currDirection == Direction.LEFT && newDirection == Direction.RIGHT);
    }

    public void moveSnakeStraight(SnakeBody head, Direction currDirection){
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

    public void moveSnakePerpendicular(SnakeBody head, Direction newDirection){
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
        this.currDirection = newDirection;
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
                foodIsEaten();
            }
        }
        else{
            //TODO: Collision occurs, handle GameOver here
        }
    }

    public void foodIsEaten(){
        this.score++;
        currScoreLabel.setText(String.valueOf(this.score));
        createFood();

    }

    public void startGame(){
        initSnake();
        createFood();
        this.animationTimer.start(); //start the loop
    }

    public VBox getRootNode(){ return this.root; }


}
