package presentation;

import domain.GamePhase;
import domain.PlayerColor;
import domain.TerritoryType;
import domain.WorldDominationGameEngine;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

public class GameMapScreenController implements GameScene {
    @FXML
    private DialogPane claimTerritoryDialog;
    @FXML
    private DialogPane territoryErrorDialog;
    @FXML
    private DialogPane armyPlacementSelectionDialog;
    @FXML
    private DialogPane attackResultsDialog;
    @FXML
    private DialogPane generalMessageDialog;
    @FXML
    private DialogPane tradeInDialog;
    @FXML
    private DialogPane extraArmiesDialog;
    @FXML
    private AnchorPane dialogBackground;
    @FXML
    private AnchorPane armiesToPlacePane;
    @FXML
    private Label currentPlayerColor;
    @FXML
    private Label currentPhase;
    @FXML
    private Label instructionLabel;
    @FXML
    private Label armiesToPlace;
    @FXML
    private Label attackerRollsLabel;
    @FXML
    private Label defenderRollsLabel;
    @FXML
    private Label attackResultsLabel;
    @FXML
    private Button attackFortifySkipButton;
    @FXML
    private Button tradeInButton;
    @FXML
    private Spinner<Integer> armyCountSpinner;
    private WorldDominationGameEngine gameEngine;
    private TerritoryType selectedTerritory;
    private Button selectedButton;
    private final Map<Button, TerritoryType> territoryButtonMap = new HashMap<>();
    private AttackLogic attackLogic;
    private FortifyLogic fortifyLogic;
    private TradeInLogic tradeInLogic;
    private Dialog errorDialogController;
    private Dialog confirmDialogController;
    private Dialog selectionDialogController;
    private Dialog attackResultsDialogController;
    private Dialog generalMessageDialogController;
    private Dialog extraArmiesDialogController;
    private boolean placementStarted;

    @FXML
    private void initialize() {
        gameEngine = SceneController.getInstance().getGameEngine();
        attackLogic = new AttackLogic(gameEngine);
        fortifyLogic = new FortifyLogic(gameEngine);
        SceneController.setCurrentScene(this);
        prepareStates();
    }

    private void prepareStates() {
        setupDialogControllers();
        updateStateLabels();
        setupDialogButtons();
        setupSkipButton();
        setupTradeInButton();
    }

    private void setupDialogControllers() {
        errorDialogController = new Dialog(territoryErrorDialog, dialogBackground);
        confirmDialogController = new Dialog(claimTerritoryDialog, dialogBackground);
        selectionDialogController = new Dialog(armyPlacementSelectionDialog, dialogBackground);
        attackResultsDialogController = new Dialog(attackResultsDialog, dialogBackground);
        generalMessageDialogController = new Dialog(generalMessageDialog, dialogBackground);
        extraArmiesDialogController = new Dialog(extraArmiesDialog, dialogBackground);
        tradeInLogic = new TradeInLogic(new Dialog(tradeInDialog, dialogBackground), gameEngine, event -> tradeIn());
    }

    private void tradeIn() {
        if (!tradeInLogic.tradeIn()) {
            tradeInErrorAction();
            showErrorMessage("gameMapScreen.tradeInError");
        } else {
            handleExtraArmies();
        }
        updateStateLabels();
    }

    private void tradeInErrorAction() {
        errorDialogController.setupButton(ButtonType.CLOSE, "gameMapScreen.dialogClose", event -> {
            tradeInLogic.displayIfEnoughCards();
            errorDialogController.toggleDisplay();
        });
    }

    private void handleExtraArmies() {
        Set<TerritoryType> extraArmies = tradeInLogic.getExtraArmyTerritories();
        if (extraArmies.size() == 1) {
            gameEngine.placeBonusArmies(extraArmies.iterator().next(), extraArmies);
        } else if (extraArmies.size() > 1) {
            displayExtraArmiesChoice(extraArmies);
        }
    }

    @SuppressWarnings("unchecked")
    private void displayExtraArmiesChoice(Set<TerritoryType> extraArmies) {
        ChoiceBox<String> territoryChoices = (ChoiceBox<String>) extraArmiesDialog.getContent();
        territoryChoices.getItems().clear();
        territoryChoices.getItems().addAll(extraArmies.stream().map(TerritoryType::toString)
                .collect(Collectors.toSet()));
        extraArmiesDialogController.toggleDisplay();
    }

    private void setupDialogButtons() {
        setupClaimTerritoryDialog();
        setupErrorDialog();
        setupArmyPlacementDialog();
        setupAttackResultsDialog();
        setupGeneralMessageDialog();
        setupExtraArmiesDialog();
    }

    private void setupSkipButton() {
        attackFortifySkipButton.addEventHandler(ActionEvent.ACTION, event -> {
            if (gameEngine.getCurrentGamePhase() == GamePhase.ATTACK) {
                handleAttackButtonClick();
            } else if (gameEngine.getCurrentGamePhase() == GamePhase.FORTIFY) {
                handleFortifyButtonClick();
            }
        });
    }

    private void handleAttackButtonClick() {
        if (attackLogic.isSourceSelected()) {
            handleAttackPhaseInstructions(false);
        } else {
            gameEngine.forceGamePhaseToEnd();
            updateStateLabels();
        }
        attackLogic.reset();
    }

    private void handleFortifyButtonClick() {
        if (fortifyLogic.isSourceSelected()) {
            handleFortifyPhaseInstructions(false);
        } else {
            gameEngine.forceGamePhaseToEnd();
            updateStateLabels();
        }
        fortifyLogic.reset();
    }

    private void setupTradeInButton() {
        tradeInButton.addEventHandler(ActionEvent.ACTION, event -> {
            if (!tradeInLogic.displayIfEnoughCards()) {
                showErrorMessage("gameMapScreen.unableToTradeIn");
            }
        });
    }

    private void setupClaimTerritoryDialog() {
        confirmDialogController.setupButton(ButtonType.YES, "gameMapScreen.dialogYes", event ->
                handleClaimTerritory());
        confirmDialogController.setupButton(ButtonType.NO, "gameMapScreen.dialogNo", event ->
                confirmDialogController.toggleDisplay());
    }

    private void setupErrorDialog() {
        errorDialogController.setupButton(ButtonType.CLOSE, "gameMapScreen.dialogClose", event ->
                errorDialogController.toggleDisplay());
    }

    private void setupArmyPlacementDialog() {
        selectionDialogController.setupButton(ButtonType.APPLY, "gameMapScreen.dialogApply", event -> {
            selectionDialogController.toggleDisplay();
            handleSelectionDialogAction(armyCountSpinner.getValue());
        });
        selectionDialogController.setupButton(ButtonType.CANCEL, "gameMapScreen.dialogCancel", event ->
                selectionDialogController.toggleDisplay());
    }

    private void setupAttackResultsDialog() {
        attackResultsDialogController.setupButton(ButtonType.OK, "gameMapScreen.dialogOk", event -> {
            attackResultsDialogController.toggleDisplay();
            if (attackLogic.didDefenderLoseTerritory()) {
                promptForAdditionalArmyTransfer();
            }
        });
    }

    private void promptForAdditionalArmyTransfer() {
        resetSelectionDialog(0);
        selectionDialogController.setTitleText("gameMapScreen.additionalArmyTransfer", new Object[]{
                gameEngine.getCurrentPlayer(), gameEngine.getNumberOfArmies(attackLogic.getSourceTerritory()) - 1,
                attackLogic.getTargetTerritory()});
        selectionDialogController.toggleDisplay();
    }

    private void setupGeneralMessageDialog() {
        generalMessageDialogController.setupButton(ButtonType.OK, "gameMapScreen.dialogOk", event ->
                generalMessageDialogController.toggleDisplay());
    }

    @SuppressWarnings("unchecked")
    private void setupExtraArmiesDialog() {
        extraArmiesDialogController.setupButton(ButtonType.OK, "gameMapScreen.dialogApply", event -> {
            gameEngine.placeBonusArmies(TerritoryType.valueOf(((ChoiceBox<String>) extraArmiesDialog.getContent())
                    .getValue()), tradeInLogic.getExtraArmyTerritories());
            extraArmiesDialogController.toggleDisplay();
            updateStateLabels();
        });
    }

    private void handleSelectionDialogAction(int value) {
        if (gameEngine.getCurrentGamePhase() == GamePhase.PLACEMENT) {
            handlePlaceArmies(value);
        } else if (gameEngine.getCurrentGamePhase() == GamePhase.ATTACK) {
            attackPhaseLogic(value);
        } else if (gameEngine.getCurrentGamePhase() == GamePhase.FORTIFY) {
            fortifyPhaseLogic(value);
        }
    }

    private void attackPhaseLogic(int value) {
        if (attackLogic.isAttackComplete()) {
            transferArmiesAttackPhase(value);
        } else if (!attackLogic.sourceArmiesSelected()) {
            attackLogic.setAttackArmies(value);
            getArmiesForDefense();
        } else {
            performAttack(value);
        }
    }

    private void transferArmiesAttackPhase(int value) {
        fortifyLogic.setSourceTerritory(attackLogic.getSourceTerritory());
        fortifyLogic.setDestinationTerritory(attackLogic.getTargetTerritory());
        fortifyLogic.setArmiesToTransfer(value);
        handleArmyTransfer(fortifyLogic.performFortify());
        updateStateLabels();
    }

    private void handleArmyTransfer(FortifyResult result) {
        if (result == FortifyResult.SUCCESS) {
            tradeInLogic.displayIfEnoughCards();
            fortifyLogic.reset();
        } else {
            showArmyTransferError(result);
        }
    }

    private void showArmyTransferError(FortifyResult result) {
        errorDialogController.setupButton(ButtonType.CLOSE, "gameMapScreen.dialogClose", event -> {
            errorDialogController.toggleDisplay();
            promptForAdditionalArmyTransfer();
        });
        showErrorMessage("gameMapScreen." + result.toKey());
    }

    private void performAttack(int value) {
        attackLogic.setDefendArmies(value);
        handleAttackErrors(attackLogic.performAttack());
        updateStateLabels();
    }

    private void handleAttackErrors(AttackResult result) {
        if (result == AttackResult.SUCCESS) {
            displayResults();
            checkForLoss();
            checkForWin();
        } else {
            showErrorMessage("gameMapScreen." + result.toKey());
        }
        attackLogic.reset();
    }

    private void displayResults() {
        displayAttackerRolls();
        displayDefenderRolls();
        displayAttackResults();
        attackResultsDialogController.toggleDisplay();
    }

    private void displayAttackerRolls() {
        StringBuilder rolls = new StringBuilder();
        gameEngine.getAttackerDiceRolls().forEach(roll -> rolls.append(roll).append(", "));
        attackerRollsLabel.setText(SceneController.getString("gameMapScreen.attackerRolls", new Object[]{
                gameEngine.getCurrentPlayer(), rolls.delete(rolls.length() - 2, rolls.length()).toString()}));
    }

    private void displayDefenderRolls() {
        StringBuilder rolls = new StringBuilder();
        gameEngine.getDefenderDiceRolls().forEach(roll -> rolls.append(roll).append(", "));
        defenderRollsLabel.setText(SceneController.getString("gameMapScreen.defenderRolls", new Object[]{
                attackLogic.getTargetOwner(), rolls.delete(rolls.length() - 2, rolls.length()).toString()}));
    }

    private void displayAttackResults() {
        StringBuilder results = new StringBuilder();
        gameEngine.getBattleResults().forEach(result -> results.append(result).append(", "));
        attackResultsLabel.setText(SceneController.getString("gameMapScreen.attackResults",
                new Object[]{results.delete(results.length() - 2, results.length()).toString()}));
    }

    private void checkForLoss() {
        int players = gameEngine.getPlayerOrder().size();
        if (players < SceneController.getInstance().getNumberOfPlayers()) {
            SceneController.getInstance().setNumberOfPlayers(players);
            generalMessageDialogController.setTitleText("gameMapScreen.playerEliminated", null);
            generalMessageDialogController.setContentText("gameMapScreen.playerEliminatedMessage",
                    new Object[]{attackLogic.getTargetOwner()});
            generalMessageDialogController.toggleDisplay();
        }
    }

    private void checkForWin() {
        if (gameEngine.getPlayerOrder().size() == 1) {
            SceneController.getInstance().setWinner(gameEngine.getCurrentPlayer());
            SceneController.getInstance().activate(SceneType.WIN);
        }
    }

    private void fortifyPhaseLogic(int value) {
        fortifyLogic.setArmiesToTransfer(value);
        FortifyResult result = fortifyLogic.performFortify();
        if (result == FortifyResult.SUCCESS) {
            updateStateLabels();
        } else {
            showErrorMessage("gameMapScreen." + result.toKey());
        }
        fortifyLogic.reset();
    }

    private void updateStateLabels() {
        currentPlayerColor.setText(gameEngine.getCurrentPlayer().toString());
        currentPhase.setText(gameEngine.getCurrentGamePhase().toString());
        armiesToPlace.setText(gameEngine.getCurrentPlayerArmiesToPlace() + "");
        if (gameEngine.getCurrentGamePhase() != GamePhase.SCRAMBLE) {
            enablePlacement();
        }
        tradeInButton.setVisible(false);
        gamePhaseActions(gameEngine.getCurrentGamePhase());
    }

    private void gamePhaseActions(GamePhase currentPhase) {
        attackFortifySkipButton.setVisible(false);
        if (currentPhase == GamePhase.SCRAMBLE) {
            handleScramblePhaseInstructions();
        } else if (currentPhase == GamePhase.SETUP) {
            handleSetupPhaseInstructions();
        } else {
            placementAttackFortifyPhaseActions(currentPhase);
        }
    }

    private void placementAttackFortifyPhaseActions(GamePhase currentPhase) {
        if (currentPhase == GamePhase.PLACEMENT) {
            handlePlacementPhaseInstructions();
        } else if (currentPhase == GamePhase.ATTACK) {
            handleAttackPhaseInstructions(attackLogic.isSourceSelected());
        } else if (currentPhase == GamePhase.FORTIFY) {
            handleFortifyPhaseInstructions(fortifyLogic.isSourceSelected());
        }
    }

    private void handleScramblePhaseInstructions() {
        instructionLabel.setText(SceneController.getString("gameMapScreen.claimInstruction",
                new Object[]{gameEngine.getCurrentPlayer()}));
    }

    private void handleSetupPhaseInstructions() {
        instructionLabel.setText(SceneController.getString("gameMapScreen.setupInstruction",
                new Object[]{gameEngine.getCurrentPlayer()}));
    }

    private void handlePlacementPhaseInstructions() {
        instructionLabel.setText(SceneController.getString("gameMapScreen.placementInstruction",
                new Object[]{gameEngine.getCurrentPlayer()}));
        tradeInButton.setVisible(true);
        if (!placementStarted) {
            tradeInLogic.displayIfEnoughCards();
            placementStarted = true;
        }
    }

    private void handleAttackPhaseInstructions(boolean sourceSelected) {
        placementStarted = false;
        tradeInButton.setVisible(true);
        instructionLabel.setText(SceneController.getString(sourceSelected ? "gameMapScreen.attackInstructionTarget"
                        : "gameMapScreen.attackInstructionSource",
                new Object[]{gameEngine.getCurrentPlayer()}));
        attackFortifySkipButton.setVisible(true);
        attackFortifySkipButton.setText(SceneController.getString(sourceSelected ? "gameMapScreen.resetAttackButton"
                : "gameMapScreen.cancelAttackButton", null));
    }

    private void handleFortifyPhaseInstructions(boolean sourceSelected) {
        instructionLabel.setText(SceneController.getString(!sourceSelected ? "gameMapScreen.fortifyInstruction"
                : "gameMapScreen.fortifySourceInstruction", new Object[]{gameEngine.getCurrentPlayer()}));
        attackFortifySkipButton.setVisible(true);
        attackFortifySkipButton.setText(SceneController.getString(sourceSelected ? "gameMapScreen.resetAttackButton"
                : "gameMapScreen.cancelAttackButton", null));
    }

    private void enablePlacement() {
        armiesToPlacePane.setVisible(true);
        for (Entry<Button, TerritoryType> entry : territoryButtonMap.entrySet()) {
            Button territoryButton = entry.getKey();
            TerritoryType territory = entry.getValue();
            territoryButton.setDisable(false);
            territoryButton.setText(gameEngine.getNumberOfArmies(territory) + "");
            setButtonBackgroundColor(getTerritoryOwner(territory), territoryButton);
        }
    }

    private PlayerColor getTerritoryOwner(TerritoryType territory) {
        return gameEngine.getPlayerOrder().stream()
                .filter(player -> gameEngine.checkIfPlayerOwnsTerritory(territory, player)).findFirst().orElse(null);
    }

    private void handleClaimTerritory() {
        setButtonBackgroundColor(gameEngine.getCurrentPlayer(), selectedButton);
        selectedButton.setText("");
        selectedButton.setDisable(true);
        gameEngine.placeNewArmiesInTerritory(selectedTerritory, 1);
        confirmDialogController.toggleDisplay();
        territoryButtonMap.put(selectedButton, selectedTerritory);
        updateStateLabels();
    }

    private void setButtonBackgroundColor(PlayerColor player, Button button) {
        StringBuilder style = new StringBuilder("-fx-background-color:");
        style.append(player.getColorString());
        if (player == PlayerColor.YELLOW) {
            style.append("; -fx-border-color: black; -fx-text-fill: black");
        }
        button.styleProperty().setValue(style.toString());
    }

    @FXML
    private void handleTerritoryButtonClick(ActionEvent event) {
        selectedButton = (Button) event.getSource();
        selectedTerritory = TerritoryType.valueOf(selectedButton.getAccessibleText());
        handleGamePhaseAction(gameEngine.getCurrentGamePhase());
    }

    private void handleGamePhaseAction(GamePhase currentPhase) {
        if (currentPhase == GamePhase.SCRAMBLE) {
            confirmDialogController.setContentText("gameMapScreen.claimAsk", new Object[]{selectedTerritory});
            confirmDialogController.toggleDisplay();
        } else if (currentPhase == GamePhase.SETUP) {
            handlePlaceArmies(1);
        } else {
            handlePlacementAttackFortifyPhases(currentPhase);
        }
    }

    private void handlePlacementAttackFortifyPhases(GamePhase currentPhase) {
        if (currentPhase == GamePhase.PLACEMENT) {
            handlePlacement();
        } else if (currentPhase == GamePhase.ATTACK) {
            handleAttack();
        } else if (currentPhase == GamePhase.FORTIFY) {
            handleFortify();
        }
    }

    private void handlePlaceArmies(int armies) {
        try {
            gameEngine.placeNewArmiesInTerritory(territoryButtonMap.get(selectedButton), armies);
        } catch (IllegalArgumentException e) {
            selectPlacementErrorMessage(e.getMessage());
        } catch (IllegalStateException e) {
            showErrorMessage("gameMapScreen.scrambleError");
        }
        updateStateLabels();
    }

    private void selectPlacementErrorMessage(String message) {
        String key = message.contains("enough") ? "notEnoughArmiesError" : message.contains("< 1") ? "tooFewArmiesError"
                : "generalPlacementError";
        showErrorMessage("gameMapScreen." + key);
    }

    private void showErrorMessage(String key) {
        errorDialogController.setContentText(key, null);
        errorDialogController.toggleDisplay();
        setupErrorDialog();
    }

    private void handlePlacement() {
        resetSelectionDialog(1);
        selectionDialogController.setTitleText("gameMapScreen.armyPlacementSelection",
                new Object[]{gameEngine.getCurrentPlayerArmiesToPlace()});
        selectionDialogController.showButton(ButtonType.CANCEL);
        selectionDialogController.toggleDisplay();
    }

    private void handleAttack() {
        if (!attackLogic.isSourceSelected()) {
            if (!attackLogic.setSourceTerritory(territoryButtonMap.get(selectedButton))) {
                updateTerritoryErrorDialog("gameMapScreen.attackSourceError");
            }
            handleAttackPhaseInstructions(attackLogic.isSourceSelected());
        } else {
            handleTargetTerritorySelection();
        }
    }

    private void handleFortify() {
        if (!fortifyLogic.isSourceSelected()) {
            fortifyLogic.setSourceTerritory(territoryButtonMap.get(selectedButton));
            handleFortifyPhaseInstructions(fortifyLogic.isSourceSelected());
        } else {
            fortifyLogic.setDestinationTerritory(territoryButtonMap.get(selectedButton));
            handleFortifyAction();
        }
    }

    private void handleFortifyAction() {
        resetSelectionDialog(1);
        selectionDialogController.setTitleText("gameMapScreen.fortifyArmySelection", null);
        selectionDialogController.showButton(ButtonType.CANCEL);
        selectionDialogController.toggleDisplay();
    }

    private void resetSelectionDialog(int startingValue) {
        armyCountSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE,
                startingValue));
    }

    private void handleTargetTerritorySelection() {
        if (!attackLogic.setTargetTerritory(territoryButtonMap.get(selectedButton))) {
            updateTerritoryErrorDialog("gameMapScreen.attackTargetError");
        } else {
            getArmiesForAttack();
        }
    }

    private void getArmiesForAttack() {
        resetSelectionDialog(1);
        selectionDialogController.setTitleText("gameMapScreen.attackArmySelection",
                new Object[]{gameEngine.getCurrentPlayer()});
        selectionDialogController.hideButton(ButtonType.CANCEL);
        selectionDialogController.toggleDisplay();
    }

    private void getArmiesForDefense() {
        resetSelectionDialog(1);
        selectionDialogController.setTitleText("gameMapScreen.defendArmySelection",
                new Object[]{attackLogic.getTargetOwner()});
        selectionDialogController.toggleDisplay();
    }

    private void updateTerritoryErrorDialog(String error) {
        errorDialogController.setContentText(error, null);
        errorDialogController.toggleDisplay();
    }

    @Override
    public void onKeyPress(KeyEvent event) {
        if (confirmDialogController.isVisible()) {
            if (event.getCode() == KeyCode.ENTER) {
                handleClaimTerritory();
            } else if (event.getCode() == KeyCode.ESCAPE) {
                confirmDialogController.toggleDisplay();
            }
        }
    }

}
