package game.blackjack.players;

import game.blackjack.gamelogic.BlackJackGame;
import game.blackjack.supportingfiles.Card;

public class Dealer extends Player {
    private BlackJackGame game;
    private static final String DEALER_NAME = "Dealer";
    private static final int DEALER_HIT_THRESHOLD = 17;

    public Dealer(BlackJackGame game) {
        this.username = DEALER_NAME;
        this.game = game;
    }

    @Override
    public void hit() {
        if (!hasBusted && !hasStood) {
            addCard(game.dealCard());
            if (currScore > 21) {
                hasBusted = true;
            }
        }
    }

    public void dealerTurn() {
        while (currScore < DEALER_HIT_THRESHOLD && !hasBusted) {
            hit();
        }
        if (!hasBusted) {
            stand();
        }
    }

    public Card dealerHiddenCard() {
        if (hand.size() > 1) {
            return hand.get(1);
        }
        return null;
    }
}
