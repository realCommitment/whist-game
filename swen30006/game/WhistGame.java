package game;

import ch.aplu.jcardgame.*;

import java.util.ArrayList;
import java.util.Optional;

public class WhistGame {

    // ----------------------------- Attributes ---------------------------

    // for deck
    public final Deck deck = new Deck(Suit.values(), Rank.values(), "cover");

    // for allocating cards
    public final int nbStartCards = PropertyReaderSingleton.getInstance().getNbStartCards();

    // for creating player
    public final int nbPlayers = 4;
    public ArrayList<Player> players;

    // for game logic
    public final int winningScore = PropertyReaderSingleton.getInstance().getWinningScore();

    public Optional<Player> winner = null;

    private String status;

    private int delay;

    // ----------------------------- Constructor ---------------------------
    public WhistGame() {

        setStatus("Initializing...");

        initPlayers();

        // will in turn notify observer and render all scores
        resetAllScores();

        playGame();
    }

    // ----------------------------- Methods ---------------------------

    private void initPlayers() {
        players = new PlayerFactory(this).getPlayers();
    }

    private void resetAllScores() {
        for (Player player : players) {
            player.resetScore();
        }
    }

    private void playGame() {
        // create and play rounds until there is a winner
        do {
            new Round(this);
        } while (!winner.isPresent());
    }

    public Optional<Player> getWinner() {
        return winner;
    }

    public void setWinner(Optional<Player> winner) {
        this.winner = winner;

        if (winner.isPresent()) {
            setStatus("Game over. Winner is player: " + winner.get().getPlayerNumber());

            WhistDisplayFacade.getInstance().displayWinner();
        }

    }

    public void setStatus(final String status) {
        this.status = status;

        WhistDisplayFacade.getInstance().displayStatus(status);
    }

    public void setDelay(final int delay) {
        this.delay = delay;

        WhistDisplayFacade.getInstance().displayDelay(delay);
    }
}