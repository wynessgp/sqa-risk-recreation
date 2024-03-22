package domain;

import java.util.Set;
import java.util.List;

public class TradeInManager {

    public TradeInManager(){

    }

    public boolean verifyValidCombo(List<Card> cards) {
        if(cards.size() == 3){
            for(Card card : cards){
                if(card.isWild()) return true;
            }
        }
        return false;
    }
}
