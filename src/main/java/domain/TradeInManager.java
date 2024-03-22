package domain;

import java.util.Set;
import java.util.List;

public class TradeInManager {

    private int setsTradedInSoFar;

    public TradeInManager(){
        setsTradedInSoFar = 0;
    }

    public boolean verifyValidCombo(List<Card> attemptedCards) {
        //check if size is 3
        if(attemptedCards.size() == 3){
            boolean hasInfantry = false;
            boolean hasCavalry = false;
            boolean hasArtillery = false;

            for(Card card : attemptedCards){
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


    public int calculateNumNewPieces(){
        if(this.setsTradedInSoFar < 5){
            return 2*this.setsTradedInSoFar + 4;
        }
        else{
            return 15;
        }
    }

    public boolean updateSetsTradedIn(){
        this.setsTradedInSoFar++;
        return true;
    }
}
