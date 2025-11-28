package game.blackjack.supportingfiles.cardenums;

public enum Rank {
    ACE("Ace"),
    TWO("Two"),
    THREE("Three"),
    FOUR("Four"),
    FIVE("Five"),
    SIX("Six"),
    SEVEN("Seven"),
    EIGHT("Eight"),
    NINE("Nine"),
    TEN("Ten"),
    JACK("Jack"),
    QUEEN("Queen"),
    KING("King"),
    PIZZA_DISCOUNT("30% Discount Pizza");

    private final String valueString;

    Rank(String valueString) {
        this.valueString = valueString;
    }
    public String getValueString() {
        return valueString;
    }
}
