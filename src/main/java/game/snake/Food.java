package game.snake;

public class Food {
    
    private FoodType foodType;
    private int row;
    private int column;


    public Food(FoodType foodType, int row, int column){
        this.foodType = foodType;
        this.row = row;
        this.column = column;
    }

    public FoodType getFoodType(){ return this.foodType; }
    public int getRow() { return this.row; }
    public int getColumn() { return this.column; }
}