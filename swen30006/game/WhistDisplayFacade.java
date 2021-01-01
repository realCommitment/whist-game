package game;

import ch.aplu.jcardgame.Hand;
import display.WhistDisplay;

public class WhistDisplayFacade {

    // ----------------------------- Attributes ---------------------------

    private static WhistDisplayFacade instance = null;
    private WhistDisplay whistDisplay;

    // ----------------------------- Constructor ---------------------------

    private WhistDisplayFacade(){
        whistDisplay = new WhistDisplay();
    }

    // ----------------------------- Methods ---------------------------

    public static WhistDisplayFacade getInstance(){

        if (instance == null){
            instance = new WhistDisplayFacade();
        }
        return instance;
    }

    public void displayWinner() {
        whistDisplay.renderWinner();
    }

    public void displayStatus(final String status) {
        whistDisplay.renderStatus(status);
    }

    public void displayDelay(final int delay) {
        whistDisplay.renderDelay(delay);
    }

    public void displayScore(int playerNumber, int score) {
        whistDisplay.renderScore(playerNumber, score);
    }

    public void displayTrumps(boolean hasTrumps, final int ordinal) {
        whistDisplay.renderTrumps(hasTrumps, ordinal);
    }

    public void displayCardsInTrick(Hand cardsInTrick, boolean isActive) {
        whistDisplay.renderCardsInTrick(cardsInTrick, isActive);
    }

    public void displayHand(Hand playerHand, int playerNumber, boolean isHuman) {
        whistDisplay.renderHand(playerHand, playerNumber, isHuman);
    }
}
