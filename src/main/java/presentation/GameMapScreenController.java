package presentation;

import domain.TerritoryType;
import domain.WorldDominationGameEngine;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;

public class GameMapScreenController {
    @FXML
    private DialogPane claimTerritoryDialog;
    private WorldDominationGameEngine gameEngine;
    private TerritoryType selectedTerritory;
    private Button selectedButton;

    @FXML
    private void initialize() {
        claimTerritoryDialog.setVisible(false);
        this.gameEngine = SceneController.getInstance().getGameEngine();
        claimTerritoryDialog.lookupButton(ButtonType.YES).addEventHandler(ActionEvent.ACTION, event ->
                handleClaimTerritory());
        claimTerritoryDialog.lookupButton(ButtonType.NO).addEventHandler(ActionEvent.ACTION, event ->
                claimTerritoryDialog.setVisible(false));
    }

    private void handleClaimTerritory() {
        selectedButton.styleProperty().setValue("-fx-background-color: " + gameEngine.getCurrentPlayer());
        selectedButton.setText("1");
        selectedButton.setDisable(true);
        gameEngine.placeNewArmiesInTerritory(selectedTerritory, 1);
        claimTerritoryDialog.setVisible(false);
    }

    @FXML
    private void handleTerritoryButtonClick(ActionEvent event) {
        selectedButton = (Button) event.getSource();
        selectedTerritory = TerritoryType.valueOf(selectedButton.getAccessibleText());
        claimTerritoryDialog.setContentText("Would you like to claim " + selectedTerritory + "?");
        claimTerritoryDialog.setVisible(true);
    }

}
