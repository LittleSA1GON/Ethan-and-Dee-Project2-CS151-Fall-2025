import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import game.blackjack.gamelogic.BlackJackGame;
import game.blackjack.players.Human;
import game.blackjack.supportingfiles.Card;
import game.blackjack.supportingfiles.cardenums.Rank;
import game.blackjack.supportingfiles.cardenums.Suit;

public class BlackjackTest {

    @Test
    public void testPizzaBlackJackSetsScoreTo21() {
        BlackJackGame game = new BlackJackGame("pizzaPlayer");
        Human human = game.getHumanPlayer();
        List<Card> hand = new ArrayList<>();
        hand.add(new Card(Suit.PIZZA_COUPON, Rank.PIZZA_DISCOUNT));
        hand.add(new Card(Suit.HEARTS, Rank.ACE));
        human.setHand(hand);
        human.updateScore();
        assertEquals(21, human.getCurrScore(),
            "Hand containing PIZZA_DISCOUNT should be treated as a blackjack (21).");
    }

    @Test
    public void testAceAdjustmentInScoreCalculation() {
        BlackJackGame game = new BlackJackGame("acePlayer");
        Human human = game.getHumanPlayer();
        List<Card> hand = new ArrayList<>();
        hand.add(new Card(Suit.HEARTS, Rank.ACE));
        hand.add(new Card(Suit.SPADES, Rank.KING));
        hand.add(new Card(Suit.DIAMONDS, Rank.ACE));
        human.setHand(hand);
        human.updateScore();
        assertEquals(12, human.getCurrScore(),
            "Multiple aces should be adjusted down so total does not bust unnecessarily.");
    }

    @Test
    public void testGenerateSaveStateStringContainsUsername() throws Exception {
        String username = "saveTester";
        BlackJackGame game = new BlackJackGame(username);
        String json = game.generateSaveStateString();
        assertNotNull(json, "Save state JSON should not be null.");
        assertFalse(json.isEmpty(), "Save state JSON should not be empty.");
        assertTrue(json.contains(username),
            "Save state JSON should contain the human player's username.");
    }

    @Test
    public void testDealCardNeverReturnsNullAfterInitialization() {
        BlackJackGame game = new BlackJackGame("deckTester");
        game.initializeDeck();
        for (int i = 0; i < 200; i++) {
            Card dealt = game.dealCard();
            assertNotNull(dealt, "dealCard() should never return null, even after many deals.");
        }
    }
}
