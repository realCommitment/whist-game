package selectCardStrategies;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import game.*;

import java.util.ArrayList;

public class SmartSelectCardStrategy extends AbstractSelectCardStrategy implements IObserver {


    // ----------------------------- Attributes ---------------------------

    protected Hand cardsPlayedInRound;

    // ----------------------------- Constructor ---------------------------

    public SmartSelectCardStrategy(Player player){
        super(player);
    }

    // ----------------------------- Methods ---------------------------

    @Override
    public Card selectCard(Trick trick) {
        Card selected = null;
        Hand hand = player.hand;
        Suit lead = trick.getLeadSuit();
        Suit trumps = trick.getTrumps();

        player.game.setStatus("Player " + player.getPlayerNumber() + " thinking... with a smart strategy");
        player.game.setDelay(thinkingTime);

        if (lead != null) {

            // Returns a card using the strategy when not leading
            selected = notLeadingStrategy(trumps, trick, hand, lead);

        } else {

            // Returns a card using the strategy when leading
            selected = LeadingStrategy(trumps, hand);
        }

        return selected;
    }

    protected Card LeadingStrategy(Suit trumps, Hand hand) {
        ArrayList<Card> playedTrumps = cardsPlayedInRound.getCardsWithSuit(trumps);
        ArrayList<Card> ownedTrumpCards = hand.getCardsWithSuit(trumps);
        ownedTrumpCards.sort(Card::compareTo);
        Card selected = null;
        int nbStartCards =  player.game.nbStartCards;


        // If all trumps have been played
        if (playedTrumps.size() + ownedTrumpCards.size() == nbStartCards) {

            // Selects a card using a strategy which is used when all trumps have been played
            selected = getBestCard(trumps, hand, "all trumps played");


        } else {

            // Selects a card using a strategy which is used when all trumps have not been played
            selected = getBestCard(trumps, hand, "all trumps not played");

        }

        if (selected != null) {
            return selected;
        }

        // Last resort (Shouldn't get to this stage)
        return hand.get(0);

    }

    protected Card getBestCard(Suit trumps, Hand hand, String strategy) {
        Card selected = null;

        // int and List used to keep track of the biggest & smallest suit in the class
        int biggestSuitSize = 0;
        ArrayList<Card> biggestSuit = null;
        int smallestSuitSize = 13;
        ArrayList<Card> smallestSuit = null;

        for (Suit s : Suit.values()) {
            // Ignore trump suit (Should be none)
            if (s == trumps) {
                continue;
            }
            ArrayList<Card> ownedCards = hand.getCardsWithSuit(s);

            // No cards with the suit
            if (ownedCards.size() == 0) {
                continue;
            }

            // Get a list of all cards played in the suit, as well as your own in the same suit. Sort them
            ArrayList<Card> playedCards = cardsPlayedInRound.getCardsWithSuit(s);
            playedCards.sort(Card::compareTo);
            ownedCards.sort(Card::compareTo);


            switch(strategy) {
                case "all trumps played":

                    System.out.println("Gets to all trumps played");

                    // Update biggest suit
                    if (ownedCards.size() > biggestSuitSize) {
                        biggestSuitSize = ownedCards.size();
                        biggestSuit = ownedCards;
                    }

                    // Guaranteed win
                    if (ownedCards.size() + playedCards.size() == 13) {
                        return ownedCards.get(0);
                    }

                    // Play the top card of a suit, guaranteed win
                    selected = playTopCard(ownedCards, playedCards);
                    if (selected != null) {
                        return selected;
                    }
                    break;

                case "all trumps not played":

                    int halfCards = ( player.game.nbPlayers) / 2;

                    // Update smallest suit
                    if (ownedCards.size() < smallestSuitSize) {
                        smallestSuitSize = ownedCards.size();
                        smallestSuit = ownedCards;
                    }

                    // When it is likely that no-one can trump you, play highest card that has guaranteed win
                    if (hand.getNumberOfCards() >= halfCards) {
                        selected = playTopCard(ownedCards, playedCards);
                        if (selected != null) {
                            return selected;
                        }
                    }
                    break;
            }

        }
        if (biggestSuit != null) {
            return biggestSuit.get(smallestSuitSize - 1);
        }
        // Since win is not guaranteed, play smallest card in the suit with the lowest number of cards
        if (smallestSuit != null) {
            return smallestSuit.get(0);
        }

        return null;
    }


    // Returns a card if it is the highest card remaining in the suit
    protected Card playTopCard(ArrayList<Card> ownedCards, ArrayList<Card> playedCards) {
        boolean foundBreak = false;
        int i = 0;

        // ACE is rank 0, so start with that
        int rank = 0;
        do {

            if (ownedCards.get(0).getRankId() == rank) {
                return ownedCards.get(0);
            }
            if (playedCards.size() > i ) {
                if (playedCards.get(i).getRankId() == rank) {
                    System.out.println("Gets to iteration");
                } else {
                    foundBreak = true;
                    System.out.println("Gets to true statement");
                }
            }
            i++;
            rank++;
            if (i == 13) {
                foundBreak = true;
            }

        } while(!foundBreak);
        return null;
    }

    // Strategy when not leading
    protected Card notLeadingStrategy(Suit trumps, Trick trick, Hand hand, Suit lead) {
        Card selected = null;
        ArrayList<Card> cardsWithSuit;
        int empty = 0;


        // Get a list of all cards with the same suit as the lead card & sort it
        cardsWithSuit = hand.getCardsWithSuit(lead);
        cardsWithSuit.sort(Card::compareTo);
        Card currentCardWinner = trick.getWinningCard();

        if (cardsWithSuit.size() != empty) {

            // If you must follow suit, use this strategy to select a card
            selected = strategyWithSuitRestriction(trumps, trick, cardsWithSuit, currentCardWinner, lead);
            if (selected != null) {
                return selected;
            }

        } else {

            // Don't have to follow the suit, so use this strategy to select a card
            return strategyWithoutSuitRestriction(trumps, hand, cardsWithSuit, empty, currentCardWinner);

        }

        // Last resort (Shouldn't get to this stage)
        return hand.get(0);
    }

    protected Card strategyWithoutSuitRestriction(Suit trumps, Hand hand, ArrayList<Card> cardsWithSuit, int empty, Card currentCardWinner) {
        ArrayList<Card> trumpCards = hand.getCardsWithSuit(trumps);
        trumpCards.sort(Card::compareTo);

        if (trumpCards.size() != empty) {
            if (currentCardWinner.getSuit() == trumps) {

                // If can beat the current trump card
                for (int i = trumpCards.size() - 1; i > 0; i--) {

                    // Select next highest trump card
                    if (currentCardWinner.compareTo(trumpCards.get(i)) == -1) {
                        return trumpCards.get(i);
                    }
                }
            } else {
                // return lowest trump card
                return trumpCards.get(trumpCards.size() - 1);
            }
        }
        // Cant win at all
        return hand.getLast();
    }


    protected Card strategyWithSuitRestriction(Suit trumps, Trick trick, ArrayList<Card> cardsWithSuit, Card currentCardWinner, Suit lead) {

        if (currentCardWinner.getSuit() == trumps && trumps != lead) {

            // Select lowest card of the suit (Impossible to win)
            return getLowestCard(cardsWithSuit);



        } else if (currentCardWinner.compareTo(cardsWithSuit.get(0)) == -1) {
            // If the highest card for us in the right suit can't beat the current winning card, return lowest card
            return getLowestCard(cardsWithSuit);
        } else {

            if (trick.numberOfCardsPlayed() == 3) {

                // Finds lowest value card that secures the win
                for (int i = cardsWithSuit.size() - 1; i >= 0; i--) {

                    if (currentCardWinner.compareTo(cardsWithSuit.get(i)) == 1) {



                        return cardsWithSuit.get(i);
                    }
                }
            } else {
                // return highestCard, biggest chance of win
                return cardsWithSuit.get(0);
            }
        }
        return null;
    }

    protected Card getLowestCard(ArrayList<Card> cardsWithSuit) {
        return cardsWithSuit.get((cardsWithSuit.size() - 1));
    }

    public void resetPlayedCards() {
        cardsPlayedInRound = new Hand(player.game.deck);
    }

    public void addToPlayedCards(Card card) {
        cardsPlayedInRound.insert(card.clone(), false);
        cardsPlayedInRound.sort(Hand.SortType.SUITPRIORITY, false);
    }

    @Override
    public void update(Card card) {
        addToPlayedCards(card);
    }
}
