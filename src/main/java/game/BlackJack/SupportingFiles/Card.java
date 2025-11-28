package game.blackjack.supportingfiles;

import game.blackjack.supportingfiles.cardenums.Rank;
import game.blackjack.supportingfiles.cardenums.Suit;

public class Card {
    private final Suit suit;
    private final Rank rank;

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    @Override
    public String toString() {
        return suit == Suit.PIZZA_COUPON ? "30% Discount Pizza" : rank + " of " + suit;
    }   
}
