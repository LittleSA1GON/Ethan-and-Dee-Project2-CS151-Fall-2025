package game.snake;

import javafx.scene.image.Image;

public enum FoodType {

    //FISH_BONES("/game/snake/fish-bones.png", -5),
    STRAWBERRY("/game/snake/strawberry.png", 1),
    ORANGE("/game/snake/orange.png", 1),
    RICE_BALL("/game/snake/rice-ball.png", 1),
    APPLE("/game/snake/apple.png", 1),
    SUSHI_EGG("/game/snake/sushi-egg.png", 1),
    PUMPKIN("/game/snake/pumpkin.png", 1),
    SODA("/game/snake/soda.png", 1),
    ICE_CREAM("/game/snake/ice-cream.png", 1),
    DONUT_CHOCOLATE("/game/snake/donut-chocolate.png", 1),
    TACO("/game/snake/taco.png", 1),
    PIZZA("/game/snake/pizza.png", 1);


    private final Image image;
    private final int score;

    FoodType(String imagePath, int score){
        this.image = new Image(FoodType.class.getResourceAsStream(imagePath));
        this.score = score;
    }

    public Image getImage(){ return this.image; }
    public int getScore(){ return this.score; }
}



