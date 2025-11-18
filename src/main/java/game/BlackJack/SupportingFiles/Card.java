package game.BlackJack.SupportingFiles;

import game.BlackJack.SupportingFiles.CardEnums.Suit;
import game.BlackJack.SupportingFiles.CardEnums.Rank;

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
