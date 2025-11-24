package game.BlackJack.Players;

import java.util.List;
import java.util.ArrayList;
import game.BlackJack.SupportingFiles.Card;
import game.BlackJack.SupportingFiles.CardEnums.Rank;

public abstract class Player {
    public String username;
    protected List<Card> hand = new ArrayList<>();
    protected int currScore;
    protected int betAmount;
    protected int money = 1000;
    protected boolean isBankrupt = false;
    protected boolean hasStood = false;
    protected boolean hasBusted = false;

    public void resetHand() {
        hand.clear();
        currScore = 0;
        betAmount = 0;
    }

    abstract void hit();

    abstract void stand();
    
    public void updateScore() {
        if (pizzaBlackJack()) {
            currScore = 21;
            return;
        }
        int score = 0;
        int aceCount = 0;
        for (Card card : hand) {
            Rank rank = card.getRank();
            switch (rank) {
                case TWO: {
                    score += 2;
                } 
                case THREE: {
                    score += 3;
                }
                case FOUR: {
                    score += 4;
                }
                case FIVE: {
                    score += 5;
                }
                case SIX: {
                    score += 6;
                }
                case SEVEN: {
                    score += 7;
                }
                case EIGHT: {
                    score += 8;
                }
                case NINE: {
                    score += 9;
                }
                case TEN: 
                case JACK: 
                case QUEEN: 
                case KING: {
                    score += 10;
                }
                case ACE: {
                    score += 11;
                    aceCount++;
                }
                default: { 
                    break; 
                }
            }
        }
        while (score > 21 && aceCount > 0) {
            score -= 10;
            aceCount--;
        }
        currScore = score;
    }

    public boolean pizzaBlackJack(){
        for (Card card : hand){
            if (card.getRank() == Rank.PIZZA_DISCOUNT){
                return true;
            }
        }
        return false;
    }
    


    
    public String getUsername(){
        return this.username;
    }
    public List<Card> getHand(){
        return this.hand;
    }
    public int getCurrScore(){
        return this.currScore;
    }

    public int getBetAmount(){
        return this.betAmount;
    }

    public int getMoney(){
        return this.money;
    }

    public boolean getIsBankrupt(){
        return this.isBankrupt;
    }
}
