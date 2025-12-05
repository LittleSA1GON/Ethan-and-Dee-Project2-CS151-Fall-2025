import static org.junit.jupiter.api.Assertions.*;

import java.util.EnumSet;
import java.util.Random;
import java.util.Set;

import org.junit.jupiter.api.Test;

import game.snake.Direction;
import game.snake.Food;
import game.snake.FoodType;
import game.snake.SnakeBody;

public class SnakeTest {

    @Test
    public void testSnakeBodyStoresRowAndColumn() {
        SnakeBody body = new SnakeBody(3, 7);
        assertEquals(3, body.getRow(), "SnakeBody row should be stored correctly.");
        assertEquals(7, body.getCol(), "SnakeBody column should be stored correctly.");
    }

    @Test
    public void testFoodStoresTypeAndPosition() {
        Food food = new Food(FoodType.STRAWBERRY, 5, 10);
        assertEquals(FoodType.STRAWBERRY, food.getFoodType(), "Food type should be stored correctly.");
        assertEquals(5, food.getRow(), "Food row should be stored correctly.");
        assertEquals(10, food.getColumn(), "Food column should be stored correctly.");
    }

    @Test
    public void testDirectionGetRandomDirectionReturnsValidEnum() {
        Random random = new Random(12345);
        Set<Direction> seen = EnumSet.noneOf(Direction.class);
        for (int i = 0; i < 100; i++) {
            Direction dir = Direction.getRandomDirection(random);
            assertNotNull(dir, "Random direction should never be null.");
            assertTrue(EnumSet.allOf(Direction.class).contains(dir),
                "Random direction must be one of the defined enum values.");
            seen.add(dir);
        }
        assertTrue(seen.size() > 1,
            "Random direction generator should produce more than one distinct value.");
    }
}
