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
    private Button attackSkipButton;
    @FXML
    private Spinner<Integer> armyCountSpinner;
    private WorldDominationGameEngine gameEngine;
    private TerritoryType selectedTerritory;
    private Button selectedButton;
    private final Map<Button, TerritoryType> territoryButtonMap = new HashMap<>();
    private final AttackLogic attackLogic = new AttackLogic();
    private Dialog errorDialogController;
    private Dialog confirmDialogController;
    private Dialog selectionDialogController;

    @FXML
    private void initialize() {
        this.gameEngine = SceneController.getInstance().getGameEngine();
        SceneController.setCurrentScene(this);
        setupDialogControllers();
        updateStateLabels();
        setupDialogButtons();
        setupSkipButton();
    }

    private void setupDialogControllers() {
        this.errorDialogController = new Dialog(territoryErrorDialog, dialogBackground);
        this.confirmDialogController = new Dialog(claimTerritoryDialog, dialogBackground);
        this.selectionDialogController = new Dialog(armyPlacementSelectionDialog, dialogBackground);
    }

    private void setupDialogButtons() {
        setupClaimTerritoryDialog();
        setupTerritoryErrorDialog();
        setupArmyPlacementDialog();
    }

    private void setupSkipButton() {
        this.attackSkipButton.addEventHandler(ActionEvent.ACTION, event -> {
            attackLogic.reset();
            handleAttackPhaseInstructions();
        });
    }

    private void setupClaimTerritoryDialog() {
        this.confirmDialogController.setupButton(ButtonType.YES, "gameMapScreen.dialogYes", event ->
                handleClaimTerritory());
        this.confirmDialogController.setupButton(ButtonType.NO, "gameMapScreen.dialogNo", event ->
                this.confirmDialogController.toggleDisplay());
    }

    private void setupTerritoryErrorDialog() {
        this.errorDialogController.setupButton(ButtonType.CLOSE, "gameMapScreen.dialogClose", event ->
                this.errorDialogController.toggleDisplay());
    }

    private void setupArmyPlacementDialog() {
        this.selectionDialogController.setupButton(ButtonType.APPLY, "gameMapScreen.dialogApply", event -> {
            this.selectionDialogController.toggleDisplay();
            handleSelectionDialogAction(this.armyCountSpinner.getValue());
        });
        this.selectionDialogController.setupButton(ButtonType.CANCEL, "gameMapScreen.dialogCancel", event ->
                this.selectionDialogController.toggleDisplay());
    }

    private void handleSelectionDialogAction(int value) {
        if (this.gameEngine.getCurrentGamePhase() == GamePhase.PLACEMENT) {
            handlePlaceArmies(value);
        } else if (this.gameEngine.getCurrentGamePhase() == GamePhase.ATTACK) {
            attackPhaseLogic(value);
        }
    }

    private void attackPhaseLogic(int value) {
        if (!attackLogic.sourceArmiesSelected()) {
            attackLogic.setAttackArmies(value);
            getArmiesForDefense();
        } else {
            attackLogic.setDefendArmies(value);
            handleAttackErrors(attackLogic.performAttack(this.gameEngine));
            updateStateLabels();
        }
    }

    private void handleAttackErrors(AttackResult result) {
        if (result == AttackResult.SUCCESS) {
            // TODO
        } else {
            showErrorMessage("gameMapScreen." + result.toKey());
        }
    }

    private void updateStateLabels() {
        this.currentPlayerColor.setText(this.gameEngine.getCurrentPlayer().toString());
        this.currentPhase.setText(this.gameEngine.getCurrentGamePhase().toString());
        this.armiesToPlace.setText(this.gameEngine.getCurrentPlayerArmiesToPlace() + "");
        if (this.gameEngine.getCurrentGamePhase() != GamePhase.SCRAMBLE) {
            enablePlacement();
        }
        gamePhaseActions();
    }

    private void gamePhaseActions() {
        GamePhase currentPhase = this.gameEngine.getCurrentGamePhase();
        if (currentPhase == GamePhase.SCRAMBLE) {
            handleScramblePhaseInstructions();
        } else if (currentPhase == GamePhase.SETUP) {
            handleSetupPhaseInstructions();
        } else {
            placementAttackPhaseActions(currentPhase);
        }
    }

    private void placementAttackPhaseActions(GamePhase currentPhase) {
        if (currentPhase == GamePhase.PLACEMENT) {
            handlePlacementPhaseInstructions();
        } else if (currentPhase == GamePhase.ATTACK) {
            handleAttackPhaseInstructions();
        }
    }

    private void handleScramblePhaseInstructions() {
        this.instructionLabel.setText(SceneController.getString("gameMapScreen.claimInstruction",
                new Object[]{this.gameEngine.getCurrentPlayer()}));
    }

    private void handleSetupPhaseInstructions() {
        this.instructionLabel.setText(SceneController.getString("gameMapScreen.setupInstruction",
                new Object[]{this.gameEngine.getCurrentPlayer()}));
    }

    private void handlePlacementPhaseInstructions() {
        this.instructionLabel.setText(SceneController.getString("gameMapScreen.placementInstruction",
                new Object[]{this.gameEngine.getCurrentPlayer()}));
    }

    private void handleAttackPhaseInstructions() {
        boolean sourceSelected = attackLogic.sourceSelected();
        this.instructionLabel.setText(SceneController.getString(sourceSelected ? "gameMapScreen.attackInstructionTarget"
                        : "gameMapScreen.attackInstructionSource",
                new Object[]{this.gameEngine.getCurrentPlayer()}));
        this.attackSkipButton.setVisible(true);
        this.attackSkipButton.setText(SceneController.getString(sourceSelected
                ? "gameMapScreen.resetAttackButton" : "gameMapScreen.cancelAttackButton", null));
    }

    private void enablePlacement() {
        this.armiesToPlacePane.setVisible(true);
        for (Entry<Button, TerritoryType> entry : this.territoryButtonMap.entrySet()) {
            Button territoryButton = entry.getKey();
            TerritoryType territory = entry.getValue();
            territoryButton.setDisable(false);
            territoryButton.setText(this.gameEngine.getNumberOfArmies(territory) + "");
        }
    }

    private void handleClaimTerritory() {
        setButtonBackgroundColor();
        this.selectedButton.setText("");
        this.selectedButton.setDisable(true);
        this.gameEngine.placeNewArmiesInTerritory(this.selectedTerritory, 1);
        this.confirmDialogController.toggleDisplay();
        this.territoryButtonMap.put(this.selectedButton, this.selectedTerritory);
        updateStateLabels();
    }

    private void setButtonBackgroundColor() {
        StringBuilder style = new StringBuilder("-fx-background-color:");
        style.append(this.gameEngine.getCurrentPlayer().getColorString());
        if (gameEngine.getCurrentPlayer() == PlayerColor.YELLOW) {
            style.append(
                    "; -fx-border-color: black; -fx-text-fill: black");
        }
        this.selectedButton.styleProperty().setValue(style.toString());
    }

    @FXML
    private void handleTerritoryButtonClick(ActionEvent event) {
        this.selectedButton = (Button) event.getSource();
        this.selectedTerritory = TerritoryType.valueOf(this.selectedButton.getAccessibleText());
        handleGamePhaseAction();
    }

    private void handleGamePhaseAction() {
        GamePhase currentPhase = this.gameEngine.getCurrentGamePhase();
        if (currentPhase == GamePhase.SCRAMBLE) {
            this.confirmDialogController.setContentText("gameMapScreen.claimAsk", new Object[]{this.selectedTerritory});
            this.confirmDialogController.toggleDisplay();
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
            this.gameEngine.placeNewArmiesInTerritory(this.territoryButtonMap.get(this.selectedButton), armies);
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
        this.errorDialogController.setContentText(key, null);
        this.errorDialogController.toggleDisplay();
    }

    private void handlePlacement() {
        resetSelectionDialog();
        this.selectionDialogController.setTitleText("gameMapScreen.armyPlacementSelection",
                new Object[]{this.gameEngine.getCurrentPlayerArmiesToPlace()});
        this.selectionDialogController.toggleDisplay();
    }

    private void handleAttack() {
        if (!attackLogic.sourceSelected()) {
            if (!attackLogic.setSourceTerritory(this.territoryButtonMap.get(this.selectedButton), this.gameEngine)) {
                updateTerritoryErrorDialog("gameMapScreen.attackSourceError");
            }
            handleAttackPhaseInstructions();
        } else {
            handleTargetTerritorySelection();
        }
    }

    private void resetSelectionDialog() {
        this.armyCountSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(Integer.MIN_VALUE,
                Integer.MAX_VALUE, 1));
    }

    private void handleTargetTerritorySelection() {
        if (!attackLogic.setTargetTerritory(this.territoryButtonMap.get(this.selectedButton), this.gameEngine)) {
            updateTerritoryErrorDialog("gameMapScreen.attackTargetError");
        } else {
            getArmiesForAttack();
        }
    }

    private void getArmiesForAttack() {
        resetSelectionDialog();
        this.selectionDialogController.setTitleText("gameMapScreen.attackArmySelection",
                new Object[]{gameEngine.getCurrentPlayer()});
        this.selectionDialogController.toggleDisplay();
    }

    private void getArmiesForDefense() {
        resetSelectionDialog();
        this.selectionDialogController.setTitleText("gameMapScreen.defendArmySelection",
                new Object[]{attackLogic.getTargetOwner(gameEngine)});
        this.selectionDialogController.toggleDisplay();
    }

    private void updateTerritoryErrorDialog(String error) {
        this.errorDialogController.setContentText(error, null);
        this.errorDialogController.toggleDisplay();
    }

    @Override
    public void onKeyPress(KeyEvent event) {
        if (this.confirmDialogController.isVisible()) {
            if (event.getCode() == KeyCode.ENTER) {
                handleClaimTerritory();
            } else if (event.getCode() == KeyCode.ESCAPE) {
                this.confirmDialogController.toggleDisplay();
            }
        }
    }

}
