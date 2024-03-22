package domain;

import java.util.Set;
import java.util.List;

public class TradeInManager {

    public TradeInManager(){

    }

    public boolean verifyValidCombo(List<Card> cards) {
        //check if size is 3
        if(cards.size() == 3){
            boolean hasInfantry = false;
            boolean hasCavalry = false;
            boolean hasArtillery = false;

            for(Card card : cards){
                //check for wild card
                if(card.isWild()) return true;

                //check for "one of each" condition
                if(card.matchesPieceType(PieceType.INFANTRY))  hasInfantry = true;
                else if(card.matchesPieceType(PieceType.CAVALRY))  hasCavalry = true;
                else if(card.matchesPieceType(PieceType.ARTILLERY))  hasArtillery = true;
            }

            return hasInfantry && hasCavalry && hasArtillery;
        }
        return false;
    }
}
