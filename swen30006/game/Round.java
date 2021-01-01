package game;

import ch.aplu.jcardgame.*;

import java.util.*;

public class Round {


    // ----------------------------- Attributes ---------------------------

    public static final Random random = new RandomFactory().getRandom();

    // return random Enum value
    public static <T extends Enum<?>> T randomEnum(Class<T> clazz){
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

    public WhistGame game;
    private Trick trick;
    private Suit trumps;

    // ----------------------------- Constructor ---------------------------

    public Round(WhistGame game) {
        this.game = game;

        // initialise hand
        initHand();

        // set a random trump suit
        setTrumps(randomEnum(Suit.class));

        // randomly select player to lead for this round
        int randomPlayer = random.nextInt(game.nbPlayers);
        Player startingPlayer = game.players.get(randomPlayer);

        // play the round and set the winner
        game.setWinner(playRound(startingPlayer, trumps));

        // remove the trump suit at the end of the round
        setTrumps(null);
    }

    // ----------------------------- Methods ---------------------------

    private void initHand() {
        // deal cards to each players hand
        Hand[] hands = getHandsWithSeed();

        for (Player player: game.players) {

            int playerNumber = player.getPlayerNumber();

            // reset cards played within the Smart Strategy of each Smart NPC Player
            if (player instanceof NPCPlayer) {
                if (((NPCPlayer) player).isSmart()) {
                    ((NPCPlayer) player).getSmartStrategy().resetPlayedCards();
                }
            }

            // sort the hand in ascending order
            hands[playerNumber].sort(Hand.SortType.SUITPRIORITY, true);

            // store the hand in the player class
            // updating the hand renders it
            player.setHand(hands[playerNumber]);
        }
    }

    private Hand[] getHandsWithSeed(){
        //If there is no seed in properties file do completely random shuffling
        if (PropertyReaderSingleton.getInstance().seed == null){
            return game.deck.dealingOut(game.nbPlayers, game.nbStartCards); // Last element of hands is leftover cards; these are ignored
        }

        //Otherwise do controlled shuffling

        //Getting all cards in to one hand
        ArrayList<Card> deckInArrayList = new ArrayList();
        //Moving cards in to ArrayList is necessary to be processed later by Collections.shuffle()
       
        for (Suit suit: Suit.values()) {
        	for (Rank rank: Rank.values()) {
        		Card card = new Card (game.deck, suit, rank);
        		deckInArrayList.add(card);
        	}
        }
      
        //Shuffling cards randomly or based on a seed
        Collections.shuffle(deckInArrayList, random);
        //Create hands for each player
        Hand[] hands = new Hand[game.nbPlayers];
        for (int i = 0; i<hands.length; i++){
            hands[i] = new Hand(game.deck);
        }
        //Distribute cards evenly to n hands
        ListIterator<Card> j = deckInArrayList.listIterator();
        
        int k = 0;
        while (j.hasNext() && k < game.nbPlayers*game.nbStartCards){
            for (Hand hand : hands){
                hand.insert(j.next(), false);
                j.remove();
                k++;
            }
        }

        return hands;
    }

    // plays the round and returns if there is a winner
    private Optional<Player> playRound(Player nextPlayer, Suit trumps) {
        for (int i = 0; i < game.nbStartCards; i++) {

            // play the trick and store the trickWinner
            trick = new Trick(game, trumps);
            Player trickWinner = trick.playTrick(nextPlayer, trumps);

            game.setStatus("Player " + trickWinner.getPlayerNumber() + " wins trick.");

            game.setDelay(trick.endOfTrickDelay);

            trickWinner.increaseScore();

            // Set nextPlayer so that the trickWinner starts the next round
            nextPlayer = trickWinner;

            // return if there is a gameWinner
            if (game.winningScore == trickWinner.getScore()) {
                return Optional.of(trickWinner);
            }
        }
        return Optional.empty();
    }

    public void setTrumps(Suit trumps) {
        this.trumps = trumps;
        boolean hasTrumps = hasTrumps();
        int ordinal;

        // store the trumps index if required, otherwise store a null value of -1
        if (hasTrumps) {
            ordinal = trumps.ordinal();
        } else {
            ordinal = -1;
        }

        // render the trump suit
        WhistDisplayFacade.getInstance().displayTrumps(hasTrumps, ordinal);
    }

    public Suit getTrumps() {
        return trumps;
    }

    public boolean hasTrumps() {
        return (trumps != null);
    }
}
