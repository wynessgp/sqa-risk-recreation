package presentation;

import domain.TerritoryType;
import domain.WorldDominationGameEngine;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;

public class GameMapScreenController {
    @FXML
    private DialogPane claimTerritoryDialog;
    @FXML
    private Label currentPlayerColor;
    @FXML
    private Label currentPhase;
    private WorldDominationGameEngine gameEngine;
    private TerritoryType selectedTerritory;
    private Button selectedButton;

    @FXML
    private void initialize() {
        this.claimTerritoryDialog.setVisible(false);
        this.gameEngine = SceneController.getInstance().getGameEngine();
        updatePlayerAndPhase();
        claimTerritoryDialog.lookupButton(ButtonType.YES).addEventHandler(ActionEvent.ACTION, event ->
                handleClaimTerritory());
        claimTerritoryDialog.lookupButton(ButtonType.NO).addEventHandler(ActionEvent.ACTION, event ->
                claimTerritoryDialog.setVisible(false));
    }

    private void updatePlayerAndPhase() {
        this.currentPlayerColor.setText(this.gameEngine.getCurrentPlayer().toString());
        this.currentPhase.setText(this.gameEngine.getCurrentGamePhase().toString());
    }

    private void handleClaimTerritory() {
        this.selectedButton.styleProperty().setValue("-fx-background-color: " + gameEngine.getCurrentPlayer());
        this.selectedButton.setText("");
        this.selectedButton.setDisable(true);
        this.gameEngine.placeNewArmiesInTerritory(this.selectedTerritory, 1);
        this.claimTerritoryDialog.setVisible(false);
        updatePlayerAndPhase();
    }

    @FXML
    private void handleTerritoryButtonClick(ActionEvent event) {
        this.selectedButton = (Button) event.getSource();
        this.selectedTerritory = TerritoryType.valueOf(this.selectedButton.getAccessibleText());
        this.claimTerritoryDialog.setContentText("Would you like to claim " + this.selectedTerritory + "?");
        this.claimTerritoryDialog.setVisible(true);
    }

}
