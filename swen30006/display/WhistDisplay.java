package display;

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;
import ch.aplu.jgamegrid.TextActor;

import java.awt.*;

public class WhistDisplay extends CardGame {

    // ----------------------------- Attributes ---------------------------

    // for displaying title
    private final String version = "1.1";

    // for displaying winner
    private final Location textLocation = new Location(350, 450);

    // for displaying score
    private final Location[] scoreLocations = {
            new Location(575, 675),
            new Location(25, 575),
            new Location(575, 25),
            new Location(650, 575)
    };
    private Actor[] scoreActors = {null, null, null, null };
    Font bigFont = new Font("Serif", Font.BOLD, 36);

    // for displaying trumps
    private final String trumpImage[] = {"bigspade.gif","bigheart.gif","bigdiamond.gif","bigclub.gif"};
    private Location trumpsActorLocation = new Location(50, 50);
    private Actor trumpsActor = null;

    // for displaying cards in trick
    private static final Location trickLocation = new Location(350, 350);
    private Location hideLocation = new Location(-500, - 500);
    private final int trickWidth = 40;

    // for displaying hand
    private final int handWidth = 400;
    private final Location[] handLocations = {
            new Location(350, 625),
            new Location(75, 350),
            new Location(350, 75),
            new Location(625, 350)
    };

    // ----------------------------- Constructor ---------------------------

    public WhistDisplay() {
        super(700, 700, 30);
        renderTitle();
    }

    // ----------------------------- Methods ---------------------------

    public void renderTitle() {
        setTitle("Whist (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
    }

    public void renderWinner() {
        addActor(new Actor("sprites/gameover.gif"), textLocation);
    }

    public void renderStatus(final String status) {
        setStatusText(status);
    }

    public void renderDelay(final int delay) {
        delay(delay);
    }

    public void renderScore(int playerNumber, int score){
        // if score already exists then a trick has been won, so wait then remove

        if (scoreActors[playerNumber] != null) {
            removeActor(scoreActors[playerNumber]);
        }

        String scoreText = String.valueOf(score);
        scoreActors[playerNumber] = new TextActor(scoreText, Color.WHITE, bgColor, bigFont);
        addActor(scoreActors[playerNumber], scoreLocations[playerNumber]);
    }

    public void renderTrumps(boolean hasTrumps, final int ordinal) {
        if (hasTrumps) {
            trumpsActor = new Actor("sprites/"+trumpImage[ordinal]);
            addActor(trumpsActor, trumpsActorLocation);
        } else {
            removeActor(trumpsActor);
        }
    }

    public void renderCardsInTrick(Hand cardsInTrick, boolean isActive) {
        if (isActive) {

            // show trick
            cardsInTrick.setView(this, new RowLayout(trickLocation, (cardsInTrick.getNumberOfCards() + 2) * trickWidth));
        } else {
            cardsInTrick.setView(this, new RowLayout(hideLocation, 0));
        }
        cardsInTrick.draw();
    }

    public void renderHand(Hand playerHand, int playerNumber, boolean isHuman) {

        RowLayout layout = new RowLayout(handLocations[playerNumber], handWidth);

        // Rotate each hand by 90 degrees
        layout.setRotationAngle(90 * playerNumber);

        // adds fancy graphical effect -- optional
        // layouts[playerNumber].setStepDelay(10);

        setHandVisibility(playerHand, isHuman);

        // Now that the settings have been set, display the hand
        playerHand.setView(this, layout);
        playerHand.setTargetArea(new TargetArea(trickLocation));
        playerHand.draw();
    }

    private void setHandVisibility(Hand playerHand, boolean isHuman) {
        // If the hand belongs to a human player, make it visible on screen
        if (isHuman) {
            playerHand.setVerso(false);
        } else {
            // TODO This hides the cards for NPC players -- (Set to true to hide cards)
            playerHand.setVerso(false);
        }
    }
}


