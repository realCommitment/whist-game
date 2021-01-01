package selectCardStrategies;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import game.*;

import java.util.ArrayList;

public class LegalSelectCardStrategy extends AbstractSelectCardStrategy {

    // ----------------------------- Attributes ---------------------------

    // ----------------------------- Constructor ---------------------------

    public LegalSelectCardStrategy(Player player){
        super(player);
    }

    // ----------------------------- Methods ---------------------------

    @Override
    public Card selectCard(Trick trick) {
        Card selected = null;
        Hand hand = player.hand;
        ArrayList<Card> cardsWithSuit;
        Suit lead = trick.getLeadSuit();
        int empty = 0;

        player.game.setStatus("Player " + player.getPlayerNumber() + " thinking... with a legal strategy");
        player.game.setDelay(thinkingTime);

        // If not leading then try to select card with the lead suit
        if (lead != null) {

            // Get a list of all cards with the same suit as the lead card
            cardsWithSuit = hand.getCardsWithSuit(lead);

            if (cardsWithSuit.size() != empty) {
                selected = randomCard(cardsWithSuit);
            }
        }


        if (selected == null) {
            selected = randomCard(hand.getCardList());
        }

        return selected;
    }

}
