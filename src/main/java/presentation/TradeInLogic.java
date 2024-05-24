package presentation;

import domain.Card;
import domain.PieceType;
import domain.TerritoryType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import domain.WorldDominationGameEngine;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

class TradeInLogic {
    private final List<Pane> content = new ArrayList<>();
    private final Dialog tradeInDialog;
    private final WorldDominationGameEngine gameEngine;

    TradeInLogic(Dialog tradeInDialog, WorldDominationGameEngine gameEngine) {
        this.tradeInDialog = tradeInDialog;
        this.gameEngine = gameEngine;
    }

    void displayListOfCards() {
        for (Card card : gameEngine.getCardsOwnedByPlayer(gameEngine.getCurrentPlayer())) {
            createDisplayCard(card);
        }
        setDialogContent();
        setupDialogButtons();
        tradeInDialog.toggleDisplay();
    }

    private void createDisplayCard(Card card) {
        TerritoryType territoryType = getTerritoryType(card);
        PieceType pieceType = getPieceType(card);
        Pane cardPane = new AnchorPane();
        cardPane.getChildren().add(new Label(territoryType.toString()));
        cardPane.getChildren().add(new Label(pieceType.toString()));
        content.add(cardPane);
    }

    private TerritoryType getTerritoryType(Card card) {
        return Arrays.stream(TerritoryType.values()).filter(card::matchesTerritory).collect(Collectors.toList()).get(0);
    }

    private PieceType getPieceType(Card card) {
        return Arrays.stream(PieceType.values()).filter(card::matchesPieceType).collect(Collectors.toList()).get(0);
    }

    private void setDialogContent() {
        Pane contentPane = new Pane();
        for (Pane cardPane : content) {
            contentPane.getChildren().add(cardPane);
        }
        tradeInDialog.setDialogContent(contentPane);
    }

    private void setupDialogButtons() {
        tradeInDialog.setupButton(ButtonType.CANCEL, "gameMapScreen.dialogCancel", event ->
                tradeInDialog.toggleDisplay());
        tradeInDialog.setupButton(ButtonType.APPLY, "gameMapScreen.dialogApply", event ->
                tradeInDialog.toggleDisplay());
    }
}
