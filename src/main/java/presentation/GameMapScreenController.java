package presentation;

import domain.GamePhase;
import domain.PlayerColor;
import domain.TerritoryType;
import domain.WorldDominationGameEngine;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
    private Button attackSkipButton;
    @FXML
    private Spinner<Integer> armyCountSpinner;
    private WorldDominationGameEngine gameEngine;
    private TerritoryType selectedTerritory;
    private Button selectedButton;
    private final Map<Button, TerritoryType> territoryButtonMap = new HashMap<>();
    private AttackLogic attackLogic;
    private Dialog errorDialogController;
    private Dialog confirmDialogController;
    private Dialog selectionDialogController;
    private Dialog attackResultsDialogController;
    private Dialog generalMessageDialogController;

    @FXML
    private void initialize() {
        gameEngine = SceneController.getInstance().getGameEngine();
        attackLogic = new AttackLogic(gameEngine);
        SceneController.setCurrentScene(this);
        setupDialogControllers();
        updateStateLabels();
        setupDialogButtons();
        setupSkipButton();
    }

    private void setupDialogControllers() {
        errorDialogController = new Dialog(territoryErrorDialog, dialogBackground);
        confirmDialogController = new Dialog(claimTerritoryDialog, dialogBackground);
        selectionDialogController = new Dialog(armyPlacementSelectionDialog, dialogBackground);
        attackResultsDialogController = new Dialog(attackResultsDialog, dialogBackground);
        generalMessageDialogController = new Dialog(generalMessageDialog, dialogBackground);
    }

    private void setupDialogButtons() {
        setupClaimTerritoryDialog();
        setupTerritoryErrorDialog();
        setupArmyPlacementDialog();
        setupAttackResultsDialog();
        setupGeneralMessageDialog();
    }

    private void setupSkipButton() {
        attackSkipButton.addEventHandler(ActionEvent.ACTION, event -> {
            attackLogic.reset();
            handleAttackButtonClick();
        });
    }

    private void handleAttackButtonClick() {
        if (attackLogic.sourceSelected()) {
            handleAttackPhaseInstructions(true);
        } else {
            gameEngine.forceGamePhaseToEnd();
            updateStateLabels();
        }
    }

    private void setupClaimTerritoryDialog() {
        confirmDialogController.setupButton(ButtonType.YES, "gameMapScreen.dialogYes", event ->
                handleClaimTerritory());
        confirmDialogController.setupButton(ButtonType.NO, "gameMapScreen.dialogNo", event ->
                confirmDialogController.toggleDisplay());
    }

    private void setupTerritoryErrorDialog() {
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
        attackResultsDialogController.setupButton(ButtonType.OK, "gameMapScreen.dialogOk", event ->
                attackResultsDialogController.toggleDisplay());
    }

    private void setupGeneralMessageDialog() {
        generalMessageDialogController.setupButton(ButtonType.OK, "gameMapScreen.dialogOk", event ->
                generalMessageDialogController.toggleDisplay());
    }

    private void handleSelectionDialogAction(int value) {
        if (gameEngine.getCurrentGamePhase() == GamePhase.PLACEMENT) {
            handlePlaceArmies(value);
        } else if (gameEngine.getCurrentGamePhase() == GamePhase.ATTACK) {
            attackPhaseLogic(value);
        }
    }

    private void attackPhaseLogic(int value) {
        if (!attackLogic.sourceArmiesSelected()) {
            attackLogic.setAttackArmies(value);
            getArmiesForDefense();
        } else {
            attackLogic.setDefendArmies(value);
            handleAttackErrors(attackLogic.performAttack());
            updateStateLabels();
        }
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

    private void updateStateLabels() {
        currentPlayerColor.setText(gameEngine.getCurrentPlayer().toString());
        currentPhase.setText(gameEngine.getCurrentGamePhase().toString());
        armiesToPlace.setText(gameEngine.getCurrentPlayerArmiesToPlace() + "");
        if (gameEngine.getCurrentGamePhase() != GamePhase.SCRAMBLE) {
            enablePlacement();
        }
        gamePhaseActions();
    }

    private void gamePhaseActions() {
        GamePhase currentPhase = gameEngine.getCurrentGamePhase();
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
            handleAttackPhaseInstructions(attackLogic.sourceSelected());
        } else if (currentPhase == GamePhase.FORTIFY) {
            handleFortifyPhaseInstructions();
        }
    }

    private void handleScramblePhaseInstructions() {
        this.instructionLabel.setText(SceneController.getString("gameMapScreen.claimInstruction",
                new Object[]{gameEngine.getCurrentPlayer()}));
    }

    private void handleSetupPhaseInstructions() {
        this.instructionLabel.setText(SceneController.getString("gameMapScreen.setupInstruction",
                new Object[]{gameEngine.getCurrentPlayer()}));
    }

    private void handlePlacementPhaseInstructions() {
        this.instructionLabel.setText(SceneController.getString("gameMapScreen.placementInstruction",
                new Object[]{gameEngine.getCurrentPlayer()}));
    }

    private void handleAttackPhaseInstructions(boolean sourceSelected) {
        instructionLabel.setText(SceneController.getString(sourceSelected ? "gameMapScreen.attackInstructionTarget"
                        : "gameMapScreen.attackInstructionSource",
                new Object[]{gameEngine.getCurrentPlayer()}));
        attackSkipButton.setVisible(true);
        attackSkipButton.setText(SceneController.getString(sourceSelected ? "gameMapScreen.resetAttackButton"
                : "gameMapScreen.cancelAttackButton", null));
    }

    private void handleFortifyPhaseInstructions() {
        this.instructionLabel.setText(SceneController.getString("gameMapScreen.fortifyInstruction",
                new Object[]{gameEngine.getCurrentPlayer()}));
    }

    private void enablePlacement() {
        this.armiesToPlacePane.setVisible(true);
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
                .filter(player -> gameEngine.checkIfPlayerOwnsTerritory(territory, player)).findFirst()
                .orElse(null);
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
        handleGamePhaseAction();
    }

    private void handleGamePhaseAction() {
        GamePhase currentPhase = gameEngine.getCurrentGamePhase();
        if (currentPhase == GamePhase.SCRAMBLE) {
            confirmDialogController.setContentText("gameMapScreen.claimAsk", new Object[]{selectedTerritory});
            confirmDialogController.toggleDisplay();
        } else {
            handlePlaceAndAttackPhases(currentPhase);
        }
    }

    private void handlePlaceAndAttackPhases(GamePhase currentPhase) {
        if (currentPhase == GamePhase.SETUP) {
            handlePlaceArmies(1);
        } else if (currentPhase == GamePhase.PLACEMENT) {
            handlePlacement();
        } else if (currentPhase == GamePhase.ATTACK) {
            handleAttack();
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
    }

    private void handlePlacement() {
        resetSelectionDialog();
        selectionDialogController.setTitleText("gameMapScreen.armyPlacementSelection",
                new Object[]{gameEngine.getCurrentPlayerArmiesToPlace()});
        selectionDialogController.showButton(ButtonType.CANCEL);
        selectionDialogController.toggleDisplay();
    }

    private void handleAttack() {
        if (!attackLogic.sourceSelected()) {
            if (!attackLogic.setSourceTerritory(territoryButtonMap.get(selectedButton))) {
                updateTerritoryErrorDialog("gameMapScreen.attackSourceError");
            }
            handleAttackPhaseInstructions(attackLogic.sourceSelected());
        } else {
            handleTargetTerritorySelection();
        }
    }

    private void resetSelectionDialog() {
        this.armyCountSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(Integer.MIN_VALUE,
                Integer.MAX_VALUE, 1));
    }

    private void handleTargetTerritorySelection() {
        if (!attackLogic.setTargetTerritory(territoryButtonMap.get(selectedButton))) {
            updateTerritoryErrorDialog("gameMapScreen.attackTargetError");
        } else {
            getArmiesForAttack();
        }
    }

    private void getArmiesForAttack() {
        resetSelectionDialog();
        selectionDialogController.setTitleText("gameMapScreen.attackArmySelection",
                new Object[]{gameEngine.getCurrentPlayer()});
        selectionDialogController.hideButton(ButtonType.CANCEL);
        selectionDialogController.toggleDisplay();
    }

    private void getArmiesForDefense() {
        resetSelectionDialog();
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
        if (this.confirmDialogController.isVisible()) {
            if (event.getCode() == KeyCode.ENTER) {
                handleClaimTerritory();
            } else if (event.getCode() == KeyCode.ESCAPE) {
                confirmDialogController.toggleDisplay();
            }
        }
    }

}
