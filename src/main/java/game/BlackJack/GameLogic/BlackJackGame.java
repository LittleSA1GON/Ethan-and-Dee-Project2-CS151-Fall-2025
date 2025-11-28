package game.blackjack.gamelogic;

import java.util.Stack;

import game.blackjack.supportingfiles.Card;
import game.blackjack.supportingfiles.cardenums.Rank;
import game.blackjack.supportingfiles.cardenums.Suit;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class BlackJackGame {
    private Stack<Card> shuffledDeck;
    private List<Card> unshuffledDeck;

    public BlackJackGame() {
        this.shuffledDeck = new Stack<>();
        this.unshuffledDeck = new ArrayList<>();
        initializeDeck();
    }
    public void initializeDeck() {
        
        for (int i = 0; i < 2; i++) {
            for (Suit suit : Suit.values()) {
                if (suit == Suit.PIZZA_COUPON) {
                    unshuffledDeck.add(new Card(suit, Rank.PIZZA_DISCOUNT));
                    continue;
                }

                for (Rank rank : Rank.values()) {
                    if (rank == Rank.PIZZA_DISCOUNT) continue;
                    unshuffledDeck.add(new Card(suit, rank));
                }
            }
        }
        Collections.shuffle(unshuffledDeck);
        shuffledDeck.addAll(unshuffledDeck);
        unshuffledDeck.clear();
    }
    public void readDeck(){
        for (Card card : shuffledDeck) {
            System.out.println(card);
        }
    }
    public Card dealCard() {
        if (shuffledDeck.isEmpty() || shuffledDeck.size() < 25) {
            initializeDeck();
        }
        return shuffledDeck.pop();
    }
}
