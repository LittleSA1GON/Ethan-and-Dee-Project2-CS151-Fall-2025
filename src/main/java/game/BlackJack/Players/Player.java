package game.BlackJack.Players;

import java.util.List;
import game.BlackJack.SupportingFiles.Card;

public abstract class Player {
    public String name;
    protected List<Card> hand;
    protected int currScore;

    abstract void hit();

    abstract void stand();

}
