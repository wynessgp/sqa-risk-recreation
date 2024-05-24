package presentation;

import domain.Card;
import domain.GamePhase;
import domain.PieceType;
import domain.TerritoryType;
import domain.WorldDominationGameEngine;
import java.util.Arrays;
import java.util.stream.Collectors;
import javafx.scene.control.ButtonType;
import org.controlsfx.control.CheckComboBox;

class TradeInLogic {
    private final Dialog tradeInDialog;
    private final WorldDominationGameEngine gameEngine;
    private final CheckComboBox<String> cardSelection;

    @SuppressWarnings("unchecked")
    TradeInLogic(Dialog tradeInDialog, WorldDominationGameEngine gameEngine) {
        this.tradeInDialog = tradeInDialog;
        this.gameEngine = gameEngine;
        this.cardSelection = (CheckComboBox<String>) tradeInDialog.getDialog().getContent();
    }

    void displayIfEnoughCards() {
        if (gameEngine.getCardsOwnedByPlayer(gameEngine.getCurrentPlayer()).size() < 3
                || shouldForceInAttackPhase()) {
            return;
        }
        hideCancelButtonOnForced();
        displayListOfCards();
    }

    private boolean shouldForceInAttackPhase() {
        return gameEngine.getCurrentGamePhase() == GamePhase.ATTACK
                && gameEngine.getCardsOwnedByPlayer(gameEngine.getCurrentPlayer()).size() > 4;
    }

    private void hideCancelButtonOnForced() {
        if (gameEngine.getCardsOwnedByPlayer(gameEngine.getCurrentPlayer()).size() > 4) {
            tradeInDialog.hideButton(ButtonType.CANCEL);
        } else {
            tradeInDialog.showButton(ButtonType.CANCEL);
        }
    }

    private void displayListOfCards() {
        cardSelection.getItems().clear();
        for (Card card : gameEngine.getCardsOwnedByPlayer(gameEngine.getCurrentPlayer())) {
            createDisplayCard(card);
        }
        setupDialogButtons();
        tradeInDialog.toggleDisplay();
    }

    private void createDisplayCard(Card card) {
        TerritoryType territoryType = getTerritoryType(card);
        PieceType pieceType = getPieceType(card);
        cardSelection.getItems().add(territoryType + " (" + pieceType + ")");

    }

    private TerritoryType getTerritoryType(Card card) {
        return Arrays.stream(TerritoryType.values()).filter(card::matchesTerritory).collect(Collectors.toList()).get(0);
    }

    private PieceType getPieceType(Card card) {
        return Arrays.stream(PieceType.values()).filter(card::matchesPieceType).collect(Collectors.toList()).get(0);
    }

    private void setupDialogButtons() {
        tradeInDialog.setupButton(ButtonType.CANCEL, "gameMapScreen.dialogCancel", event ->
                tradeInDialog.toggleDisplay());
        tradeInDialog.setupButton(ButtonType.APPLY, "gameMapScreen.dialogApply", event -> {
            for (String card : cardSelection.getCheckModel().getCheckedItems()) {
                System.out.println(card);
            }
            tradeInDialog.toggleDisplay();
        });
    }
}
