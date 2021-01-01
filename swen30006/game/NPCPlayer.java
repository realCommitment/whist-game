package game;

import ch.aplu.jcardgame.Card;

import selectCardStrategies.*;


public class NPCPlayer extends Player{

    // ----------------------------- Attributes ---------------------------

    private AbstractSelectCardStrategy strategy;

    // ----------------------------- Constructor ---------------------------

    public NPCPlayer(WhistGame game, int num) {
        super(game);
        this.playerNumber = num;
    }

    // ----------------------------- Methods ---------------------------

    @Override
    public Card selectCard(Trick trick) {
        return strategy.selectCard(trick);
    }

    public void setSelectCardStrategy(AbstractSelectCardStrategy abstractSelectCardStrategy){
        this.strategy = abstractSelectCardStrategy;
    }

    public boolean isSmart() {
        return (strategy instanceof SmartSelectCardStrategy);
    }

    public SmartSelectCardStrategy getSmartStrategy() {
        return (SmartSelectCardStrategy) strategy;
    }
}
