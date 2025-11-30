package game.blackjack.players;

import java.util.List;

import game.blackjack.supportingfiles.Card;
import game.blackjack.supportingfiles.cardenums.Rank;

import java.util.ArrayList;

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
        hasStood = false;
        hasBusted = false;
        currScore = 0;
        betAmount = 0;
    }

    public abstract void hit();

    public void stand() {
        hasStood = true;
    }
    
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
                case TWO:
                    score += 2;
                    break;
                case THREE:
                    score += 3;
                    break;
                case FOUR:
                    score += 4;
                    break;
                case FIVE:
                    score += 5;
                    break;
                case SIX:
                    score += 6;
                    break;
                case SEVEN:
                    score += 7;
                    break;
                case EIGHT:
                    score += 8;
                    break;
                case NINE:
                    score += 9;
                    break;
                case TEN:
                case JACK:
                case QUEEN:
                case KING:
                    score += 10;
                    break;
                case ACE:
                    score += 11;
                    aceCount++;
                    break;
                default:
                    break;
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
    
    public void setHasStood(boolean stood) {
        this.hasStood = stood;
    }

    public void setHasBusted(boolean busted) {
        this.hasBusted = busted;
    }

    public void setCurrScore(int score) {
        this.currScore = score;
    }

    public void setMoney(int amount) {
        this.money = amount;
    }

    public void setHand(List<Card> hand) {
        this.hand = new ArrayList<>(hand);
    }

    public void addCard(Card card) {
        hand.add(card);
        updateScore();
    }

    public void setBetAmount(int amount) {
        this.betAmount = amount;
    }

    public void winBet() {
        money += (betAmount * 2); 
        betAmount = 0;
    }

    public void loseBet() {
        if (money <= 0) {
            isBankrupt = true;
            money = 0;
        }
    }

    public void tieRound() {
        money += betAmount;
        betAmount = 0;
    }

    public boolean hasStood() {
        return hasStood;
    }

    public boolean hasBusted() {
        return hasBusted;
    }
}
