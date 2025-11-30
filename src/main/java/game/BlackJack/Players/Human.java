package game.blackjack.players;

import game.blackjack.gamelogic.BlackJackGame;

public class Human extends Player implements Gambler {
    private BlackJackGame game;

    public Human(String username, BlackJackGame game) {
        this.username = username;
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


    @Override
    public void placeBet(int amount) {
        if (amount > 0 && amount <= money) {
            setBetAmount(amount);
        }
    }  
}

