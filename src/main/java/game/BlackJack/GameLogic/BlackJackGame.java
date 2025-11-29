package game.blackjack.gamelogic;

import java.util.Stack;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import java.nio.file.Files;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.ObjectMapper;

import game.blackjack.supportingfiles.Card;
import game.blackjack.supportingfiles.cardenums.Rank;
import game.blackjack.supportingfiles.cardenums.Suit;
import game.blackjack.players.Player;
import game.blackjack.players.Human;
import game.blackjack.players.Computer;
import game.blackjack.players.Dealer;
import game.blackjack.players.CompPersonalities;
import java.util.Random;
import game.blackjack.gamelogic.gameenums.GameState;
import game.gamemanager.FileManager;

public class BlackJackGame {
    private Stack<Card> shuffledDeck;
    private List<Card> unshuffledDeck;
    private Human humanPlayer;
    private Computer computerPlayer1;
    private Computer computerPlayer2;
    private Dealer dealer;
    private List<Player> players;
    private GameState gameState;
    private int currentPlayerIndex;
    private String lastStatusMessage;
    private int maxMoneyReached;
    private static final String SAVE_FILE = "txtfiles/BlackJackSave.txt";

    public BlackJackGame(String username) {
        this.shuffledDeck = new Stack<>();
        this.unshuffledDeck = new ArrayList<>();
        this.humanPlayer = new Human(username, this);
        Random rnd = new Random();
        CompPersonalities[] vals = CompPersonalities.values();
        CompPersonalities p1 = vals[rnd.nextInt(vals.length)];
        CompPersonalities p2 = vals[rnd.nextInt(vals.length)];
        this.computerPlayer1 = new Computer("Computer 1", p1, this);
        this.computerPlayer2 = new Computer("Computer 2", p2, this);
        this.dealer = new Dealer(this);
        this.players = new ArrayList<>();
        this.lastStatusMessage = "Game initialized";
        this.maxMoneyReached = this.humanPlayer.getMoney();
        initializePlayers();
        initializeDeck();
    }

    public BlackJackGame(String username, boolean isLoading) throws Exception {
        this.shuffledDeck = new Stack<>();
        this.unshuffledDeck = new ArrayList<>();
        this.players = new ArrayList<>();
        if (isLoading) {
            String saveStateString = loadFromFile();
            loadFromSaveState(saveStateString);
        } 
        else {
            Random rnd = new Random();
            CompPersonalities[] vals = CompPersonalities.values();
            CompPersonalities p1 = vals[rnd.nextInt(vals.length)];
            CompPersonalities p2 = vals[rnd.nextInt(vals.length)];
            this.humanPlayer = new Human(username, this);
            this.computerPlayer1 = new Computer("Computer 1", p1, this);
            this.computerPlayer2 = new Computer("Computer 2", p2, this);
            this.dealer = new Dealer(this);
            initializePlayers();
        }
        initializeDeck();
    }

    /**
     * Construct and load from per-user encrypted save using provided password.
     */
    public BlackJackGame(String username, boolean isLoading, String password) throws Exception {
        this.shuffledDeck = new Stack<>();
        this.unshuffledDeck = new ArrayList<>();
        this.players = new ArrayList<>();
        if (isLoading) {
            String saveStateString = FileManager.loadBlackjackSave(username, password);
            if (saveStateString == null) {
                throw new Exception("No save found for user or incorrect password");
            }
            loadFromSaveState(saveStateString);
        } 
        else {
            Random rnd = new Random();
            CompPersonalities[] vals = CompPersonalities.values();
            CompPersonalities p1 = vals[rnd.nextInt(vals.length)];
            CompPersonalities p2 = vals[rnd.nextInt(vals.length)];
            this.humanPlayer = new Human(username, this);
            this.computerPlayer1 = new Computer("Computer 1", p1, this);
            this.computerPlayer2 = new Computer("Computer 2", p2, this);
            this.dealer = new Dealer(this);
            initializePlayers();
        }
        initializeDeck();
    }

    private void initializePlayers() {
        players.clear();
        players.add(humanPlayer);
        players.add(computerPlayer1);
        players.add(computerPlayer2);
        players.add(dealer);
        gameState = GameState.NEW_ROUND;
        currentPlayerIndex = 0;
    }

    public void initializeDeck() {
        shuffledDeck.clear();
        unshuffledDeck.clear();
        
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
        if (lastStatusMessage == null || lastStatusMessage.isEmpty()) {
        lastStatusMessage = "Deck reshuffled.";
        } 
        else {
            lastStatusMessage += "\nDeck reshuffled.";
        }
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

    public void startNewRound() {
        for (Player player : players) {
            player.resetHand();
        }
        currentPlayerIndex = 0;
        gameState = GameState.NEW_ROUND;
        lastStatusMessage = "New round starting. Place bets!";
    }

    public void bettingPhase() {
        for (Player player : players) {
            if (!(player instanceof Dealer) && player.getBetAmount() > 0) {
                int currentMoney = player.getMoney();
                player.setMoney(currentMoney - player.getBetAmount());
            }
        }
    }

    public void dealInitialCards() {
        dealer.addCard(dealCard());

        if (humanPlayer.getBetAmount() > 0) {
            humanPlayer.addCard(dealCard());
        }
        if (computerPlayer1.getBetAmount() > 0) {
            computerPlayer1.addCard(dealCard());
        }
        if (computerPlayer2.getBetAmount() > 0) {
            computerPlayer2.addCard(dealCard());
        }
        
        dealer.addCard(dealCard());
        if (humanPlayer.getBetAmount() > 0) {
            humanPlayer.addCard(dealCard());
        }
        if (computerPlayer1.getBetAmount() > 0) {
            computerPlayer1.addCard(dealCard());
        }
        if (computerPlayer2.getBetAmount() > 0) {
            computerPlayer2.addCard(dealCard());
        }
        
        gameState = GameState.PLAYER_TURN;
        lastStatusMessage = "Cards dealt. " + humanPlayer.getUsername() + " to play.";
    }

    public void finishDealing() {
        this.gameState = GameState.PLAYER_TURN;
        this.lastStatusMessage = "Cards dealt. " + humanPlayer.getUsername() + " to play.";
    }

    public void processPlayerTurn(Player player) {
        if (player.hasBusted()) {
            moveToNextPlayer();
            return;
        }

        if (player instanceof Computer) {
            Computer comp = (Computer) player;
            comp.makeDecision();
            if (comp.hasBusted()) {
                lastStatusMessage = comp.getUsername() + " busts!";
            } 
            else if (comp.hasStood()) {
                lastStatusMessage = comp.getUsername() + " stands with " + comp.getCurrScore();
            } 
            else {
                lastStatusMessage = comp.getUsername() + " hits!";
            }
            moveToNextPlayer();
        } 
        else if (player instanceof Dealer) {
            Dealer d = (Dealer) player;
            d.dealerTurn();
            if (d.hasBusted()) {
                lastStatusMessage = "Dealer busts!";
            } 
            else if (d.hasStood()) {
                lastStatusMessage = "Dealer stands with " + d.getCurrScore();
            } 
            else {
                lastStatusMessage = "Dealer hits!";
            }
        }
    }

    public void humanPlayerHit() {
        if (humanPlayer instanceof Human) {
            Human human = (Human) humanPlayer;
            human.hit();
            if (humanPlayer.hasBusted()) {
                lastStatusMessage = humanPlayer.getUsername() + " busts! Hand value: " + humanPlayer.getCurrScore();
                moveToNextPlayer();
            } 
            else {
                lastStatusMessage = humanPlayer.getUsername() + " hits! Hand value: " + humanPlayer.getCurrScore();
            }
        }
    }

    public void humanPlayerStand() {
        if (humanPlayer instanceof Human) {
            Human human = (Human) humanPlayer;
            human.stand();
            lastStatusMessage = humanPlayer.getUsername() + " stands with " + humanPlayer.getCurrScore();
            moveToNextPlayer();
        }
    }

    private void moveToNextPlayer() {
        currentPlayerIndex++;
        if (currentPlayerIndex < players.size() - 1) {
            Player nextPlayer = players.get(currentPlayerIndex);
            // Skip players who are bankrupt or have no bet
            while (currentPlayerIndex < players.size() - 1 && (nextPlayer.getMoney() <= 0 || nextPlayer.getBetAmount() == 0)) {
                currentPlayerIndex++;
                nextPlayer = players.get(currentPlayerIndex);
            }
            // If we've reached the dealer or beyond, start dealer turn
            if (currentPlayerIndex >= players.size() - 1) {
                dealerTurn();
            }
        } 
        else {
            dealerTurn();
        }
    }

    public void dealerTurn() {
        gameState = GameState.DEALER_TURN;
        lastStatusMessage = "Dealer's turn - Dealer reveals hidden card!";
        
        while (!dealer.hasStood() && !dealer.hasBusted()) {
            processPlayerTurn(dealer);
        }
        
        endRound();
    }

    private void endRound() {
        gameState = GameState.ROUND_OVER;
        calculateResults();
    }

    private void calculateResults() {
        int dealerScore = dealer.getCurrScore();
        boolean dealerBusted = dealer.hasBusted();
        
        StringBuilder results = new StringBuilder("\n=== ROUND RESULTS ===\n");

        for (Player player : players) {
            if (player instanceof Dealer) continue;
            if (player.getBetAmount() == 0) continue;

            int playerScore = player.getCurrScore();
            boolean playerBusted = player.hasBusted();

            if (playerBusted) {
                results.append(player.getUsername()).append(" busts with ").append(playerScore).append(" - loses $").append(player.getBetAmount()).append("\n");
            } 
            else if (dealerBusted) {
                player.winBet();
                results.append(player.getUsername()).append(" wins! (Dealer busts) +$").append(player.getBetAmount()).append("\n");
            } 
            else if (playerScore > dealerScore) {
                player.winBet();
                results.append(player.getUsername()).append(" ").append(playerScore).append(" beats Dealer ").append(dealerScore).append(" +$").append(player.getBetAmount()).append("\n");
            } 
            else if (playerScore < dealerScore) {
                results.append(player.getUsername()).append(" ").append(playerScore).append(" loses to Dealer ").append(dealerScore).append(" -$").append(player.getBetAmount()).append("\n");
            } 
            else {
                player.tieRound();
                results.append(player.getUsername()).append(" ").append(playerScore).append(" ties with Dealer - push\n");
            }
        }
        
        lastStatusMessage = results.toString();
        
        if (humanPlayer.getMoney() > maxMoneyReached) {
            maxMoneyReached = humanPlayer.getMoney();
        }
        
        checkGameOver();
    }

    private void checkGameOver() {
        int activePlayers = 0;
        for (Player player : players) {
            if (!(player instanceof Dealer) && player.getMoney() > 0) {
                activePlayers++;
            }
        }

        if (activePlayers == 0) {
            lastStatusMessage += "\nGame Over! All players are bankrupt.";
        }
    }

    public String generateSaveStateString() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SaveState state = new SaveState();
        
        state.gameState = gameState.toString();
        state.currentPlayerIndex = currentPlayerIndex;
        state.maxMoneyReached = maxMoneyReached;
        state.humanUsername = humanPlayer.getUsername();
        state.humanScore = humanPlayer.getCurrScore();
        state.humanMoney = humanPlayer.getMoney();
        state.humanBet = humanPlayer.getBetAmount();
        state.humanHand = cardsToString(humanPlayer.getHand());
        state.humanHasStood = humanPlayer.hasStood();
        state.humanHasBusted = humanPlayer.hasBusted();

        state.comp1Score = computerPlayer1.getCurrScore();
        state.comp1Money = computerPlayer1.getMoney();
        state.comp1Bet = computerPlayer1.getBetAmount();
        state.comp1Hand = cardsToString(computerPlayer1.getHand());
        state.comp1HasStood = computerPlayer1.hasStood();
        state.comp1HasBusted = computerPlayer1.hasBusted();
        state.comp1Personality = computerPlayer1.getPersonality().name();

        state.comp2Score = computerPlayer2.getCurrScore();
        state.comp2Money = computerPlayer2.getMoney();
        state.comp2Bet = computerPlayer2.getBetAmount();
        state.comp2Hand = cardsToString(computerPlayer2.getHand());
        state.comp2HasStood = computerPlayer2.hasStood();
        state.comp2HasBusted = computerPlayer2.hasBusted();
        state.comp2Personality = computerPlayer2.getPersonality().name();

        state.dealerScore = dealer.getCurrScore();
        state.dealerMoney = dealer.getMoney();
        state.dealerBet = dealer.getBetAmount();
        state.dealerHand = cardsToString(dealer.getHand());
        state.dealerHasStood = dealer.hasStood();
        state.dealerHasBusted = dealer.hasBusted();

        return mapper.writeValueAsString(state);
    }

    public void saveToFile() throws Exception {
        String saveState = generateSaveStateString();
        Files.write(Paths.get(SAVE_FILE), saveState.getBytes());
    }

    public static String loadFromFile() throws Exception {
        return new String(Files.readAllBytes(Paths.get(SAVE_FILE)));
    }

    private void loadFromSaveState(String saveStateString) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SaveState state = mapper.readValue(saveStateString, SaveState.class);

        gameState = GameState.valueOf(state.gameState);
        currentPlayerIndex = state.currentPlayerIndex;
        maxMoneyReached = state.maxMoneyReached;

        humanPlayer = new Human(state.humanUsername, this);
        humanPlayer.setCurrScore(state.humanScore);
        humanPlayer.setMoney(state.humanMoney);
        humanPlayer.setBetAmount(state.humanBet);
        humanPlayer.setHand(stringToCards(state.humanHand));
        humanPlayer.setHasStood(state.humanHasStood);
        humanPlayer.setHasBusted(state.humanHasBusted);

        computerPlayer1 = new Computer("Computer 1", CompPersonalities.valueOf(state.comp1Personality), this);
        computerPlayer1.setCurrScore(state.comp1Score);
        computerPlayer1.setMoney(state.comp1Money);
        computerPlayer1.setBetAmount(state.comp1Bet);
        computerPlayer1.setHand(stringToCards(state.comp1Hand));
        computerPlayer1.setHasStood(state.comp1HasStood);
        computerPlayer1.setHasBusted(state.comp1HasBusted);

        computerPlayer2 = new Computer("Computer 2", CompPersonalities.valueOf(state.comp2Personality), this);
        computerPlayer2.setCurrScore(state.comp2Score);
        computerPlayer2.setMoney(state.comp2Money);
        computerPlayer2.setBetAmount(state.comp2Bet);
        computerPlayer2.setHand(stringToCards(state.comp2Hand));
        computerPlayer2.setHasStood(state.comp2HasStood);
        computerPlayer2.setHasBusted(state.comp2HasBusted);

        dealer = new Dealer(this);
        dealer.setCurrScore(state.dealerScore);
        dealer.setMoney(state.dealerMoney);
        dealer.setBetAmount(state.dealerBet);
        dealer.setHand(stringToCards(state.dealerHand));
        dealer.setHasStood(state.dealerHasStood);
        dealer.setHasBusted(state.dealerHasBusted);

        // Populate players list without resetting gameState (preserve loaded state)
        players.clear();
        players.add(humanPlayer);
        players.add(computerPlayer1);
        players.add(computerPlayer2);
        players.add(dealer);
        
        lastStatusMessage = "Game loaded from save state!";
    }

    private String cardsToString(List<Card> cards) {
        StringBuilder sb = new StringBuilder();
        for (Card card : cards) {
            sb.append(card.getRank().name()).append("_").append(card.getSuit().name()).append(";");
        }
        return sb.toString();
    }

    private List<Card> stringToCards(String cardString) {
        List<Card> cards = new ArrayList<>();
        if (cardString == null || cardString.isEmpty()) {
            return cards;
        }
        String[] cardParts = cardString.split(";");
        for (String part : cardParts) {
            if (!part.isEmpty()) {
                String[] rankSuit = part.split("_");
                if (rankSuit.length == 2) {
                    Rank rank = Rank.valueOf(rankSuit[0]);
                    Suit suit = Suit.valueOf(rankSuit[1]);
                    cards.add(new Card(suit, rank));
                }
            }
        }
        return cards;
    }

    // Getters
    public Human getHumanPlayer() { return humanPlayer; }
    public Computer getComputerPlayer1() { return computerPlayer1; }
    public Computer getComputerPlayer2() { return computerPlayer2; }
    public Dealer getDealer() { return dealer; }
    public List<Player> getPlayers() { return players; }
    public GameState getGameState() { return gameState; }
    public int getCurrentPlayerIndex() { return currentPlayerIndex; }
    public Player getCurrentPlayer() { 
        if (currentPlayerIndex < players.size()) {
            return players.get(currentPlayerIndex);
        }
        return null;
    }
    public String getLastStatusMessage() { return lastStatusMessage; }

    public void setLastStatusMessage(String msg) { this.lastStatusMessage = msg; }
    public boolean isGameOver() {
        return humanPlayer.getMoney() <= 0;
    }

    public int getHumanPlayerMoney() {
        return humanPlayer.getMoney();
    }

    public int getMaxMoneyReached() {
        return maxMoneyReached;
    }

    public static class SaveState {
        public String gameState;
        public int currentPlayerIndex;
        public String humanUsername;
        public int maxMoneyReached;
        
        public int humanScore;
        public int humanMoney;
        public int humanBet;
        public String humanHand;
        public boolean humanHasStood;
        public boolean humanHasBusted;
        
        public int comp1Score;
        public int comp1Money;
        public int comp1Bet;
        public String comp1Hand;
        public boolean comp1HasStood;
        public boolean comp1HasBusted;
        public String comp1Personality;
        
        public int comp2Score;
        public int comp2Money;
        public int comp2Bet;
        public String comp2Hand;
        public boolean comp2HasStood;
        public boolean comp2HasBusted;
        public String comp2Personality;
        
        public int dealerScore;
        public int dealerMoney;
        public int dealerBet;
        public String dealerHand;
        public boolean dealerHasStood;
        public boolean dealerHasBusted;
    }
}

