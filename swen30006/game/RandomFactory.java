package game;

import java.util.Random;

public class RandomFactory {

    // ----------------------------- Attributes ---------------------------

    private Random random;

    // ----------------------------- Constructor ---------------------------

    public RandomFactory(){
        String seedProp =  PropertyReaderSingleton.getInstance().getSeed();

        //Generation based on seed
        if(seedProp != null){
            random = new Random((long) Integer.parseInt(seedProp));
        }
        //Generation if seed was not specified in the document
        else{
            random = new Random();
        }
    }

    // ----------------------------- Methods ---------------------------

    public Random getRandom() {
        return random;
    }

}
