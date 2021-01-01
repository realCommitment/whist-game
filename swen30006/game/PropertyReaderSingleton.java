package game;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Properties;

//Class reads from properties file and server retrieved data to another classes
public class PropertyReaderSingleton {

    // ----------------------------- Attributes ---------------------------

    private static PropertyReaderSingleton instance = null;

    Properties whistProperties = null;
    String seed = null;

    public int getNbStartCards() {
        return nbStartCards;
    }

    public int getWinningScore() {
        return winningScore;
    }

    private int nbStartCards;
    private int winningScore;
    private boolean areRulesEnforced;



    ArrayList<String> playersFromProperties;

    // ----------------------------- Constructor ---------------------------

    public boolean areRulesEnforced() {
        return areRulesEnforced;
    }

    private PropertyReaderSingleton(){
        this.whistProperties = new Properties();
        playersFromProperties = new ArrayList<String>();
        FileReader inStream = null;
        try {
            inStream = new FileReader("whist.properties");
            whistProperties.load(inStream);

            //Reading players information
            playersFromProperties.add(whistProperties.getProperty("player1"));
            playersFromProperties.add(whistProperties.getProperty("player2"));
            playersFromProperties.add(whistProperties.getProperty("player3"));
            playersFromProperties.add(whistProperties.getProperty("player4"));

            //Reading seed information
            seed = whistProperties.getProperty("Seed");

            //Reading nbStartCards
            nbStartCards = Integer.parseInt(whistProperties.getProperty("nbStartCards"));

            //#Winning winningScore
            winningScore = Integer.parseInt(whistProperties.getProperty("winningScore"));

            //#Enforce rules areRulesEnforced
            areRulesEnforced = Boolean.parseBoolean(whistProperties.getProperty("areRulesEnforced"));


        }catch (Exception e){
            e.printStackTrace();
        }

        finally {
            if (inStream != null) {
                try {
                    inStream.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    // ----------------------------- Methods ---------------------------

    public static PropertyReaderSingleton getInstance(){
        if (instance == null){
            instance = new PropertyReaderSingleton();
        }
        return instance;
    }

    public String getSeed() {
        return seed;
    }

}
