package game.snake;

import java.util.Random;

public enum Direction {
    UP, DOWN, LEFT, RIGHT;

    private static Direction[] DIRECTIONS = values();

    public static Direction getRandomDirection(Random random){
        return DIRECTIONS[random.nextInt(DIRECTIONS.length)];
    }
}
