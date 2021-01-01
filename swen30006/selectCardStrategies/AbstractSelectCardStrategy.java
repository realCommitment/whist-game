package selectCardStrategies;

import ch.aplu.jcardgame.Card;
import game.*;

import java.util.ArrayList;
import java.util.Random;

public abstract class AbstractSelectCardStrategy {
    //Abstract class used for a Strategy pattern
    //Implemented as Abstract Class because can take in a Player as a part of constructor
    //insures that no subclass instances are implemented without visibility to a Player
    //which is necessary for determining the optimal/legal/random card to pick

    // ----------------------------- Attributes ---------------------------

    protected Player player;
    protected final int thinkingTime = 2000;
    static final Random random = new RandomFactory().getRandom();

    // ----------------------------- Constructor ---------------------------

    public AbstractSelectCardStrategy(Player player){
        this.player = player;
    }

    // ----------------------------- Methods ---------------------------

    public abstract Card selectCard(Trick trick);


    // return random Card from ArrayList
    protected Card randomCard(ArrayList<Card> list){
        int x = random.nextInt(list.size());
        return list.get(x);
    }


}
