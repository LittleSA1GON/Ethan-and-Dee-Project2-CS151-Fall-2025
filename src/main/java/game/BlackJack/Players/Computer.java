package game.blackjack.players;

import java.util.Random;
import game.blackjack.gamelogic.BlackJackGame;

public class Computer extends Player implements Gambler{
    private CompPersonalities personality;
    private BlackJackGame game;
    private Random rnd = new Random();
    
    public Computer(String username, CompPersonalities personality, BlackJackGame game) {
        this.username = username;
        this.game = game;
        this.money = 1000;
        this.personality = selectRandomCompPersonalities();
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

    @Override
    public void stand() {
        hasStood = true;
    }

    @Override
    public void placeBet(int amount) {
        int chosen = personality.chooseBet(rnd);
        if (chosen > money) {
            if (money >= 100) {
                chosen = 100;
            }
            else chosen = 0;
        }
        if (chosen > 0) setBetAmount(chosen);
    }
    
    public void makeDecision() {
        boolean hit = personality.shouldHit(currScore, rnd);
        if (hit) {
            hit();
         } 
        else {
            stand();
        }
    }
    
    public CompPersonalities getPersonality() { 
        return personality; 
    }

    public void setPersonality(CompPersonalities p) { 
        this.personality = p; 
    }

    public CompPersonalities selectRandomCompPersonalities(){
        CompPersonalities[] personalities = CompPersonalities.values();
        int index = rnd.nextInt(personalities.length);
        return personalities[index];
    }

}