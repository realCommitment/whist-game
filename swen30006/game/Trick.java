package game;

import ch.aplu.jcardgame.*;
import exceptions.BrokeRuleException;
import selectCardStrategies.IObserver;

import java.util.ArrayList;

public class Trick implements IObservable{

    // ----------------------------- Attributes ---------------------------

    public boolean rankGreater(Card card1, Card card2) {
        return card1.getRankId() < card2.getRankId(); // Warning: Reverse rank order of cards (see comment on enum)
    }

    private boolean enforceRules = PropertyReaderSingleton.getInstance().areRulesEnforced();
    private Card winningCard;
    private Hand cardsInTrick;
    Suit trumps;
    private Suit leadSuit;
    private Card selectedCard;
    private boolean isActive;
    private Player trickWinner;
    private WhistGame game;

    // for observer pattern
    private ArrayList<IObserver> playerCardsObservers = new ArrayList<IObserver>();

    protected final int endOfTrickDelay = 600;

    // ----------------------------- Constructor ---------------------------

    public Trick(WhistGame game, Suit trumps) {
        leadSuit = null;
        this.trumps = trumps;
        this.game = game;

        
        for (Player player: game.players) {
        	// for each smart NPC player
            if (player instanceof NPCPlayer) {
                if (((NPCPlayer) player).isSmart()) {
                    
                	// add each instance of SmartSelectCardStrategy as an observer of Trick
                	addObserver(((NPCPlayer) player).getSmartStrategy());
                }
            }
            
            // for each human player add a card listener that will record clicks
            if (player instanceof HumanPlayer) {
                ((HumanPlayer) player).setupCardListener();
            }
        }

    }

    // ----------------------------- Methods ---------------------------

    public Player playTrick(Player nextPlayer, Suit trumps ) {

        // create a new hand for the trick
        cardsInTrick = new Hand(game.deck);

        for (int turn = 1; turn <= game.nbPlayers; turn++) {
            // this displays the trick
            setTrickIsActive(true);

            selectCardToPlay(nextPlayer, turn);

            // updates attributes
            setLeadSuitIfRequired(turn);

            checkForTrickWinner(nextPlayer, turn);
            nextPlayer = setNextPlayer(nextPlayer);
        }


        game.setDelay(endOfTrickDelay);

        setTrickIsActive(false);

        return trickWinner;
    }



    private void selectCardToPlay(Player nextPlayer, int turn) {

        selectedCard = null;
        setSelectedCard(nextPlayer);
        applyRulesIfRequired(nextPlayer, turn);
        moveSelectedCard();

        // notify observers that cards in trick has been played
        notifyObservers();

    }

    private Player setNextPlayer(Player nextPlayer) {
        int nextPlayerNumber = nextPlayer.playerNumber;

        // increment next player, then if outside range go from last back to first
        if (++nextPlayerNumber >= game.nbPlayers) {
            nextPlayerNumber = 0;
        }
        nextPlayer = game.players.get(nextPlayerNumber);

        return nextPlayer;
    }

    private void applyRulesIfRequired(Player nextPlayer, int turn) {
        // if first turn
        if (turn == 1) {
            // No restrictions on the card being lead for first player
        } else {
            // following card must follow suit if possible
            checkBrokenRule(nextPlayer);
        }
    }

    private void checkBrokenRule(Player nextPlayer){
        if (selectedCard.getSuit() != leadSuit && nextPlayer.hand.getNumberOfCardsWithSuit(leadSuit) > 0) {
            // Rule violation
            String violation = "Follow rule broken by player " + nextPlayer + " attempting to play " + selectedCard;
            System.out.println(violation);
            if (enforceRules)
                try {
                    throw(new BrokeRuleException(violation));
                } catch (BrokeRuleException e) {
                    e.printStackTrace();
                    System.out.println("A cheating player spoiled the game!");
                    System.exit(0);
                }
        }
    }

    private void checkForTrickWinner(Player nextPlayer, int turn) {
        // if first turn
        if (turn == 1) {

            // set this card and player as the current winner
            setWinner(nextPlayer);
        } else {

            // print comparison of winning card and played card
            System.out.println("winning: suit = " + winningCard.getSuit() + ", rank = " + winningCard.getRankId());
            System.out.println(" played: suit = " +    selectedCard.getSuit() + ", rank = " +    selectedCard.getRankId());

            if (isNewWinner()){
                System.out.println("NEW WINNER");
                setWinner(nextPlayer);
            }
        }
    }

    private void setWinner(Player nextPlayer) {
        trickWinner = nextPlayer;
        winningCard = selectedCard;
    }

    private boolean isNewWinner() {
        if ( // beat current trickWinner with higher card
                (selectedCard.getSuit() == winningCard.getSuit() && rankGreater(selectedCard, winningCard)) ||
                        // trumped when non-trump was winning
                        (selectedCard.getSuit() == trumps && winningCard.getSuit() != trumps)) {
            return true;
        } else {
            return false;
        }
    }

    public Suit getLeadSuit() {
        return leadSuit;
    }

    // plays the turn of the first player
    private void setLeadSuitIfRequired(int turn) {
        // if first turn
        if (turn == 1) {
            // The card played by the first player becomes the lead suit for the trick
            leadSuit = (Suit) selectedCard.getSuit();
        }
    }

    public Card getWinningCard() {
        return winningCard;
    }

    public int numberOfCardsPlayed() {
        return cardsInTrick.getNumberOfCards();
    }

    public Suit getTrumps() {
        return trumps;
    }

    private void setSelectedCard(Player nextPlayer) {

        if (nextPlayer instanceof HumanPlayer) {  // Select lead depending on player type
            selectedCard = ((HumanPlayer)nextPlayer).selectCard(this);
        } else {
            selectedCard = nextPlayer.selectCard(this);
        }
    }

    private void moveSelectedCard() {

        // move the card from hand to trick (includes rendering graphics)
        selectedCard.setVerso(false);  // In case it is upside down
        selectedCard.transfer(cardsInTrick, true);
    }

    public void setTrickIsActive(boolean isActive) {
        this.isActive = isActive;

        WhistDisplayFacade.getInstance().displayCardsInTrick(cardsInTrick, isActive);
    }

    @Override
    public void addObserver(IObserver observer) {
        playerCardsObservers.add(observer);
    }

    @Override
    public void removeObserver(IObserver observer) {
        playerCardsObservers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (IObserver playedCardsObserver: playerCardsObservers) {
            playedCardsObserver.update(selectedCard);
        }
    }
}
