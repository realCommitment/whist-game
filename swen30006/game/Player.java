package game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

public abstract class Player {

    // ----------------------------- Attributes ---------------------------

    public Hand hand;
    protected int score;
    public WhistGame game;
    public Card selected;
    protected int playerNumber;

    // ----------------------------- Constructor ---------------------------

    public Player(WhistGame game) {
        hand = null;
        selected = null;
        this.game = game;
    }

    // ----------------------------- Methods ---------------------------

    public abstract Card selectCard(Trick trick);

    public int getScore() {
        return score;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void resetScore() {
        this.score = 0;

        WhistDisplayFacade.getInstance().displayScore(playerNumber, score);
    }

    // Adds score to player when they win a trick and notify the the observer
    public void increaseScore() {
        score += 1;

        WhistDisplayFacade.getInstance().displayScore(playerNumber, score);
    }

    public void setHand(Hand hand) {
        this.hand = hand;

        WhistDisplayFacade.getInstance().displayHand(hand, playerNumber, isHuman());
    }

    private boolean isHuman() {
        return (this instanceof HumanPlayer);
    }
}
