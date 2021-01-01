package game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardAdapter;
import ch.aplu.jcardgame.CardListener;

public class HumanPlayer extends Player{

    // ----------------------------- Attributes ---------------------------

    // ----------------------------- Constructor ---------------------------

    public HumanPlayer(WhistGame game, int num) {
        super(game);
        this.playerNumber = num;

    }

    // ----------------------------- Methods ---------------------------

    @Override
    // Used when not leading
    public Card selectCard(Trick trick) {
        selected = null;
        hand.setTouchEnabled(true);
        game.setStatus("Player " + playerNumber + " double-click on card to lead.");
        while (null == selected) {
            game.setDelay(100);
        }
        return selected;
    }


    // Set up human player for interaction
    public void setupCardListener() {
        CardListener cardListener = new CardAdapter()  // Human game.Player plays card
        {
            public void leftDoubleClicked(Card card) {
                selected = card;
                hand.setTouchEnabled(false);
            }
        };
        hand.addCardListener(cardListener);
    }
}

