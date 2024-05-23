package presentation;

import domain.Card;
import domain.PieceType;
import domain.TerritoryType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class TradeInLogic {
    private final List<Pane> content = new ArrayList<>();

    void setListOfCards(List<Card> cards) {
        for (Card card : cards) {
            TerritoryType territoryType = getTerritoryType(card);
            PieceType pieceType = getPieceType(card);
            Pane cardPane = new AnchorPane();
            cardPane.getChildren().add(new Label(territoryType.toString()));
            cardPane.getChildren().add(new Label(pieceType.toString()));
            content.add(cardPane);
        }
    }

    private TerritoryType getTerritoryType(Card card) {
        return Arrays.stream(TerritoryType.values()).filter(card::matchesTerritory).collect(Collectors.toList()).get(0);
    }

    private PieceType getPieceType(Card card) {
        return Arrays.stream(PieceType.values()).filter(card::matchesPieceType).collect(Collectors.toList()).get(0);
    }

    List<Pane> getContent() {
        return content;
    }
}
