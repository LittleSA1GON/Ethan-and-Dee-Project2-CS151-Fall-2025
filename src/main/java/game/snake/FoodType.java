package game.snake;

import javafx.scene.image.Image;

public enum FoodType {

    //FISH_BONES("/game/snake/fish-bones.png", -5),
    STRAWBERRY("/game/snake/strawberry.png", 1),
    ORANGE("/game/snake/orange.png", 2),
    RICE_BALL("/game/snake/rice-ball.png", 3),
    APPLE("/game/snake/apple.png", 4),
    SUSHI_EGG("/game/snake/sushi-egg.png", 5),
    PUMPKIN("/game/snake/pumpkin.png", 6),
    SODA("/game/snake/soda.png", 7),
    ICE_CREAM("/game/snake/ice-cream.png", 8),
    DONUT_CHOCOLATE("/game/snake/donut-chocolate.png", 9),
    TACO("/game/snake/taco.png", 10),
    PIZZA("/game/snake/pizza.png", 11);


    private final Image image;
    private final int score;

    FoodType(String imagePath, int score){
        this.image = new Image(FoodType.class.getResourceAsStream(imagePath));
        this.score = score;
    }

    public Image getImage(){ return this.image; }
    public int getScore(){ return this.score; }
}



