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

                //check which piece is in the list
                if(card.matchesPieceType(PieceType.INFANTRY))  hasInfantry = true;
                else if(card.matchesPieceType(PieceType.CAVALRY))  hasCavalry = true;
                else if(card.matchesPieceType(PieceType.ARTILLERY))  hasArtillery = true;
            }

            return (hasInfantry && hasCavalry && hasArtillery)      //one of each
                    || (hasInfantry && !hasCavalry && !hasArtillery)  //3 infantry
                    || (!hasInfantry && hasCavalry && !hasArtillery)  //3 cavalry
                    || (!hasInfantry && !hasCavalry && hasArtillery); //3 artillery
        }
        return false;
    }
}
