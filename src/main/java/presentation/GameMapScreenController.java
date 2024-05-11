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

    @FXML
    private void initialize() {
        this.gameEngine = SceneController.getInstance().getGameEngine();
        SceneController.setCurrentScene(this);
        updateStateLabels();
        setupDialogButtons();
        setupSkipButton();
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
        this.claimTerritoryDialog.lookupButton(ButtonType.YES).addEventHandler(ActionEvent.ACTION, event ->
                handleClaimTerritory());
        this.claimTerritoryDialog.lookupButton(ButtonType.NO).addEventHandler(ActionEvent.ACTION, event ->
                toggleDialog(claimTerritoryDialog));
        setTerritoryDialogText();
    }

    private void setTerritoryDialogText() {
        ((Button) this.claimTerritoryDialog.lookupButton(ButtonType.YES)).setText(SceneController
                .getString("gameMapScreen.dialogYes", null));
        ((Button) this.claimTerritoryDialog.lookupButton(ButtonType.NO)).setText(SceneController
                .getString("gameMapScreen.dialogNo", null));
    }

    private void setupTerritoryErrorDialog() {
        this.territoryErrorDialog.lookupButton(ButtonType.CLOSE).addEventHandler(ActionEvent.ACTION, event ->
                toggleDialog(territoryErrorDialog));
        ((Button) this.territoryErrorDialog.lookupButton(ButtonType.CLOSE)).setText(SceneController
                .getString("gameMapScreen.dialogClose", null));
    }

    private void setupArmyPlacementDialog() {
        this.armyPlacementSelectionDialog.lookupButton(ButtonType.APPLY).addEventHandler(ActionEvent.ACTION, event -> {
            toggleDialog(armyPlacementSelectionDialog);
            handlePlaceArmies(this.armyCountSpinner.getValue());
        });
        this.armyPlacementSelectionDialog.lookupButton(ButtonType.CANCEL).addEventHandler(ActionEvent.ACTION, event ->
                toggleDialog(armyPlacementSelectionDialog));
        setArmyPlacementDialogText();
    }

    private void setArmyPlacementDialogText() {
        ((Button) this.armyPlacementSelectionDialog.lookupButton(ButtonType.APPLY)).setText(SceneController
                .getString("gameMapScreen.dialogApply", null));
        ((Button) this.armyPlacementSelectionDialog.lookupButton(ButtonType.CANCEL)).setText(SceneController
                .getString("gameMapScreen.dialogCancel", null));
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
        toggleDialog(this.claimTerritoryDialog);
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

    private void toggleDialog(DialogPane dialog) {
        boolean visible = dialog.isVisible();
        dialog.setVisible(!visible);
        this.dialogBackground.setVisible(!visible);
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
            this.claimTerritoryDialog.setContentText(SceneController.getString("gameMapScreen.claimAsk",
                    new Object[]{this.selectedTerritory}));
            toggleDialog(this.claimTerritoryDialog);
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
        } catch (Exception e) {
            this.territoryErrorDialog.setContentText(SceneController.getString("gameMapScreen.placementError", null));
            toggleDialog(this.territoryErrorDialog);
        }
        updateStateLabels();
    }

    private void handlePlacement() {
        this.armyPlacementSelectionDialog.setHeaderText(SceneController.getString(
                "gameMapScreen.armyPlacementSelection",
                new Object[]{this.gameEngine.getCurrentPlayerArmiesToPlace()}));
        this.armyCountSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(Integer.MIN_VALUE,
                Integer.MAX_VALUE, 1));
        toggleDialog(this.armyPlacementSelectionDialog);
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

    private void handleTargetTerritorySelection() {
        if (!attackLogic.setTargetTerritory(this.territoryButtonMap.get(this.selectedButton), this.gameEngine)) {
            updateTerritoryErrorDialog("gameMapScreen.attackTargetError");
        } else if (!attackLogic.territoriesAreAdjacent(this.gameEngine)) {
            updateTerritoryErrorDialog("gameMapScreen.attackAdjacentError");
        }
    }

    private void updateTerritoryErrorDialog(String error) {
        this.territoryErrorDialog.setContentText(SceneController.getString(error, null));
        toggleDialog(this.territoryErrorDialog);
    }

    @Override
    public void onKeyPress(KeyEvent event) {
        if (this.claimTerritoryDialog.isVisible()) {
            if (event.getCode() == KeyCode.ENTER) {
                handleClaimTerritory();
            } else if (event.getCode() == KeyCode.ESCAPE) {
                toggleDialog(this.claimTerritoryDialog);
            }
        }
    }

}
