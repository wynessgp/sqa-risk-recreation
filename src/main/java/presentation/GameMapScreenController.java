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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

public class GameMapScreenController implements GameScene {
    @FXML
    private DialogPane claimTerritoryDialog;
    @FXML
    private DialogPane placeArmiesErrorDialog;
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
    private WorldDominationGameEngine gameEngine;
    private TerritoryType selectedTerritory;
    private Button selectedButton;
    private final Map<Button, TerritoryType> territoryButtonMap = new HashMap<>();

    @FXML
    private void initialize() {
        this.gameEngine = SceneController.getInstance().getGameEngine();
        SceneController.setCurrentScene(this);
        updateStateLabels();
        setupDialogButtons();
    }

    private void setupDialogButtons() {
        this.claimTerritoryDialog.lookupButton(ButtonType.YES).addEventHandler(ActionEvent.ACTION, event ->
                handleClaimTerritory());
        this.claimTerritoryDialog.lookupButton(ButtonType.NO).addEventHandler(ActionEvent.ACTION, event ->
                toggleDialog(claimTerritoryDialog));
        setTerritoryDialogText();
        this.placeArmiesErrorDialog.lookupButton(ButtonType.CLOSE).addEventHandler(ActionEvent.ACTION, event ->
                toggleDialog(placeArmiesErrorDialog));
        setArmiesErrorDialogText();
    }

    private void setTerritoryDialogText() {
        ((Button) this.claimTerritoryDialog.lookupButton(ButtonType.YES)).setText(SceneController
                .getString("gameMapScreen.dialogYes", null));
        ((Button) this.claimTerritoryDialog.lookupButton(ButtonType.NO)).setText(SceneController
                .getString("gameMapScreen.dialogNo", null));
    }

    private void setArmiesErrorDialogText() {
        ((Button) this.placeArmiesErrorDialog.lookupButton(ButtonType.CLOSE)).setText(SceneController
                .getString("gameMapScreen.dialogClose", null));
    }

    private void updateStateLabels() {
        this.currentPlayerColor.setText(this.gameEngine.getCurrentPlayer().toString());
        this.currentPhase.setText(this.gameEngine.getCurrentGamePhase().toString());
        this.armiesToPlace.setText(this.gameEngine.getCurrentPlayerArmiesToPlace() + "");
        gamePhaseActions();
    }

    private void gamePhaseActions() {
        GamePhase currentPhase = this.gameEngine.getCurrentGamePhase();
        if (currentPhase == GamePhase.SCRAMBLE) {
            handleScramblePhase();
        } else if (currentPhase == GamePhase.SETUP) {
            handleSetupPhase();
        } else if (currentPhase == GamePhase.ATTACK) {
            handleAttackPhase();
        }
    }

    private void handleScramblePhase() {
        this.instructionLabel.setText(SceneController.getString("gameMapScreen.claimInstruction",
                new Object[]{this.gameEngine.getCurrentPlayer()}));
    }

    private void handleSetupPhase() {
        this.instructionLabel.setText(SceneController.getString("gameMapScreen.setupInstruction",
                new Object[]{this.gameEngine.getCurrentPlayer()}));
        enablePlacement();
    }

    private void handleAttackPhase() {
        this.instructionLabel.setText(SceneController.getString("gameMapScreen.attackInstruction",
                new Object[]{this.gameEngine.getCurrentPlayer()}));
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
        if (this.gameEngine.getCurrentGamePhase() == GamePhase.SCRAMBLE) {
            this.claimTerritoryDialog.setContentText(SceneController.getString("gameMapScreen.claimAsk",
                    new Object[]{this.selectedTerritory}));
            toggleDialog(this.claimTerritoryDialog);
        } else {
            handlePlaceArmies();
        }
    }

    private void handlePlaceArmies() {
        try {
            this.gameEngine.placeNewArmiesInTerritory(this.territoryButtonMap.get(this.selectedButton), 1);
        } catch (Exception e) {
            toggleDialog(this.placeArmiesErrorDialog);
        }
        updateStateLabels();
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
