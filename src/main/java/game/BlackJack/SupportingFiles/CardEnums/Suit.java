package game.BlackJack.SupportingFiles.CardEnums;

public enum Suit {
    HEARTS("Hearts"), 
    DIAMONDS("Diamonds"), 
    CLUBS("Clubs"), 
    SPADES("Spades"),
    PIZZA_COUPON("Pizza Coupon");

    private final String suitString;

    Suit(String suitString) {
        this.suitString = suitString;
    }

    public String getSuitString() {
        return suitString;
    }
}
