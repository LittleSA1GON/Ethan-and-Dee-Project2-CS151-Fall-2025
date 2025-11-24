package game.snake;

public class SnakeBody {
    
    private int row;
    private int col; 

    public SnakeBody(int row, int col){
        this.row = row;
        this.col = col;
    }

    public int getRow() {return this.row;}
    public int getCol() {return this.col;}
     
}
