package game;

import exceptions.IncorrectPlayerNameException;
import selectCardStrategies.AbstractSelectCardStrategy;
import selectCardStrategies.LegalSelectCardStrategy;
import selectCardStrategies.RandomSelectCardStrategy;
import selectCardStrategies.SmartSelectCardStrategy;

import java.util.ArrayList;

public class PlayerFactory {

    // ----------------------------- Attributes ---------------------------

    private WhistGame whistGame;

    // ----------------------------- Constructor ---------------------------

    public PlayerFactory(WhistGame whistGame){
        this.whistGame = whistGame;
    }

    // ----------------------------- Methods ---------------------------

    // Create players and assigns relevant strategies for picking the card. Returns players.
    public ArrayList<Player> getPlayers() {
        ArrayList<Player> players = new ArrayList<Player>();
        ArrayList<String> playersFromPropertyFile = PropertyReaderSingleton.getInstance().playersFromProperties;

        // Go through each player in the property file and create it
        for (int i = 0; i < playersFromPropertyFile.size(); i++) {

            String playerType = playersFromPropertyFile.get(i);

            if (playerType.equals("humanPlayer")) {
                createHumanPlayer(players, i);
            } else {
                createNPCPlayer(players, i, playerType);
            }
        }
        return players;
    }

    private void createHumanPlayer(ArrayList<Player> players, int playerNumber) {
        // Create Human Player
        Player humanPlayer = new HumanPlayer(whistGame, playerNumber);

        // No Strategy Required

        // Store
        players.add(humanPlayer);
    }

    private void createNPCPlayer(ArrayList<Player> players, int playerNumber, String playerType) {
        // Create NPC Player
        NPCPlayer npcPlayer = new NPCPlayer(whistGame, playerNumber);

        // Set Strategy
        setStrategy(playerType, npcPlayer);

        // Store
        players.add(npcPlayer);
    }

    private void setStrategy(String playerType, NPCPlayer npcPlayer) {
        // Select Strategy
        AbstractSelectCardStrategy strategy = null;
        switch(playerType) {
            case "randomNPCPlayer":
                strategy = new RandomSelectCardStrategy(npcPlayer);
                break;
            case "legalNPCPlayer":
                strategy = new LegalSelectCardStrategy(npcPlayer);
                break;
            case "smartNPCPlayer":
                strategy = new SmartSelectCardStrategy(npcPlayer);
                break;

            default:
                try {
                    throw(new IncorrectPlayerNameException("Player type not found: " + playerType));
                } catch (IncorrectPlayerNameException e) {
                    e.printStackTrace();
                    System.out.println("Unusual player name, please check spelling. Correct names include:" +
                            "\nhumanPlayer\nrandomNPCPlayer\nlegalNPCPlayer\nsmartNPCPlayer");
                    System.exit(0);
                }

        }

        // Set strategy
        npcPlayer.setSelectCardStrategy(strategy);
    }
}
