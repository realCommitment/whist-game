package selectCardStrategies;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import game.*;


public class RandomSelectCardStrategy extends AbstractSelectCardStrategy {


    // ----------------------------- Attributes ---------------------------

    // ----------------------------- Constructor ---------------------------

    public RandomSelectCardStrategy(Player player){
        super(player);
    }

    // ----------------------------- Methods ---------------------------

    @Override
    public Card selectCard(Trick trick) {
        Card selected = null;
        Hand hand = player.hand;

        player.game.setStatus("Player " + player.getPlayerNumber() + " thinking...");
        player.game.setDelay(thinkingTime);

        selected = randomCard(hand.getCardList());

        return selected;
    }

    }
