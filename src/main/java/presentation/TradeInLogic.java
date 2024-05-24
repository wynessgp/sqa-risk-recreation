package presentation;

import domain.Card;
import domain.GamePhase;
import domain.PieceType;
import domain.TerritoryType;
import domain.WorldDominationGameEngine;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonType;
import org.controlsfx.control.CheckComboBox;

class TradeInLogic {
    private static final int TRADE_IN_COUNT = 3;

    private final Dialog tradeInDialog;
    private final WorldDominationGameEngine gameEngine;
    private final CheckComboBox<String> cardSelection;
    private final EventHandler<Event> performTradeIn;
    private Set<TerritoryType> extraArmyTerritories;

    @SuppressWarnings("unchecked")
    TradeInLogic(Dialog tradeInDialog, WorldDominationGameEngine gameEngine, EventHandler<Event> performTradeIn) {
        this.tradeInDialog = tradeInDialog;
        this.gameEngine = gameEngine;
        // Java doesn't like this cast, so we're suppressing the warning
        this.cardSelection = (CheckComboBox<String>) tradeInDialog.getDialog().getContent();
        this.performTradeIn = performTradeIn;
    }

    void displayIfEnoughCards() {
        if (gameEngine.getCardsOwnedByPlayer(gameEngine.getCurrentPlayer()).size() < TRADE_IN_COUNT
                || shouldForceInAttackPhase()) {
            return;
        }
        hideCancelButtonOnForced();
        displayListOfCards();
    }

    private boolean shouldForceInAttackPhase() {
        return gameEngine.getCurrentGamePhase() == GamePhase.ATTACK
                && gameEngine.getCardsOwnedByPlayer(gameEngine.getCurrentPlayer()).size() <= TRADE_IN_COUNT + 1;
    }

    private void hideCancelButtonOnForced() {
        if (gameEngine.getCardsOwnedByPlayer(gameEngine.getCurrentPlayer()).size() > TRADE_IN_COUNT + 1) {
            tradeInDialog.hideButton(ButtonType.CANCEL);
        } else {
            tradeInDialog.showButton(ButtonType.CANCEL);
        }
    }

    private void displayListOfCards() {
        extraArmyTerritories.clear();
        cardSelection.getItems().clear();
        cardSelection.getCheckModel().clearChecks();
        gameEngine.getCardsOwnedByPlayer(gameEngine.getCurrentPlayer()).forEach(this::createDisplayCard);
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
        tradeInDialog.setupButton(ButtonType.CANCEL, "gameMapScreen.dialogNotNow", event ->
                tradeInDialog.toggleDisplay());
        tradeInDialog.setupButton(ButtonType.APPLY, "gameMapScreen.dialogApply", performTradeIn);
    }

    boolean tradeIn() {
        try {
            tradeInDialog.toggleDisplay();
            extraArmyTerritories = (gameEngine.tradeInCards(cardSelection.getCheckModel().getCheckedItems().stream()
                    .map(this::getCardFromString).collect(Collectors.toSet())));
        } catch (Exception exception) {
            return false;
        }
        return true;
    }

    private Card getCardFromString(String cardString) {
        String territory = cardString.substring(0, cardString.indexOf('(') - 1);
        String piece = cardString.substring(cardString.indexOf('(') + 1, cardString.indexOf(')'));
        TerritoryType territoryType = Arrays.stream(TerritoryType.values()).filter(t -> t.toString().equals(territory))
                .collect(Collectors.toList()).get(0);
        PieceType pieceType = Arrays.stream(PieceType.values()).filter(p -> p.toString().equals(piece))
                .collect(Collectors.toList()).get(0);
        return gameEngine.getCardsOwnedByPlayer(gameEngine.getCurrentPlayer()).stream().filter(c ->
                c.matchesTerritory(territoryType) && c.matchesPieceType(pieceType)).collect(Collectors.toList()).get(0);
    }

    Set<TerritoryType> getExtraArmyTerritories() {
        return extraArmyTerritories;
    }
}
