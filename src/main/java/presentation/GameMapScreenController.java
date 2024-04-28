package presentation;

import domain.PlayerColor;
import domain.TerritoryType;
import domain.WorldDominationGameEngine;
import java.util.HashMap;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class GameMapScreenController {
    @FXML
    private DialogPane claimTerritoryDialog;
    @FXML
    private AnchorPane dialogBackground;
    @FXML
    private Label currentPlayerColor;
    @FXML
    private Label currentPhase;
    private WorldDominationGameEngine gameEngine;
    private TerritoryType selectedTerritory;
    private Button selectedButton;
    private final Map<Button, TerritoryType> territoryButtonMap = new HashMap<>();

    @FXML
    private void initialize() {
        this.gameEngine = SceneController.getInstance().getGameEngine();
        updatePlayerAndPhase();
        claimTerritoryDialog.lookupButton(ButtonType.YES).addEventHandler(ActionEvent.ACTION, event ->
                handleClaimTerritory());
        claimTerritoryDialog.lookupButton(ButtonType.NO).addEventHandler(ActionEvent.ACTION, event ->
                toggleDialog(claimTerritoryDialog));
    }

    private void updatePlayerAndPhase() {
        this.currentPlayerColor.setText(this.gameEngine.getCurrentPlayer().toString());
        String nextPhase = this.gameEngine.getCurrentGamePhase().toString();
        if (!this.currentPhase.getText().equals(nextPhase)) {
            this.currentPhase.setText(nextPhase);
            enableButtons();
        }
    }

    private void enableButtons() {
        for (Button territoryButton : this.territoryButtonMap.keySet()) {
            territoryButton.setDisable(false);
            // TODO: Set number of armies when method added to GameEngine
            territoryButton.setText("1");
        }
    }

    private void handleClaimTerritory() {
        setButtonBackgroundColor();
        this.selectedButton.setText("");
        this.selectedButton.setDisable(true);
        this.gameEngine.placeNewArmiesInTerritory(this.selectedTerritory, 1);
        toggleDialog(this.claimTerritoryDialog);
        this.territoryButtonMap.put(this.selectedButton, this.selectedTerritory);
        updatePlayerAndPhase();
    }

    private void setButtonBackgroundColor() {
        StringBuilder style = new StringBuilder("-fx-background-color:");
        style.append(this.gameEngine.getCurrentPlayer());
        if (gameEngine.getCurrentPlayer() == PlayerColor.YELLOW) {
            style.append(
                    "; -fx-border-color: black; -fx-text-fill: black; -fx-background-color: yellow");
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
        this.claimTerritoryDialog.setContentText("Would you like to claim " + this.selectedTerritory + "?");
        toggleDialog(this.claimTerritoryDialog);
    }

}
