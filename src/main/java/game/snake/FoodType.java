package game.snake;

import javafx.scene.image.Image;

public enum FoodType {
    APPLE("/game/snake/apple.png", 1); 

    private Image image;
    private int score;

    FoodType(String imagePath, int score){
        this.image = new Image(FoodType.class.getResourceAsStream(imagePath));
        this.score = score;
    }
}



