package presentation;

import domain.TerritoryType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;

public class GameMapScreenController {
    @FXML
    private DialogPane claimTerritoryDialog;

    @FXML
    private void initialize() {
        claimTerritoryDialog.setVisible(false);
        claimTerritoryDialog.lookupButton(ButtonType.YES).addEventHandler(ActionEvent.ACTION, event ->
                claimTerritoryDialog.setVisible(false));
        claimTerritoryDialog.lookupButton(ButtonType.NO).addEventHandler(ActionEvent.ACTION, event ->
                claimTerritoryDialog.setVisible(false));
    }

    @FXML
    private void handleTerritoryButtonClick(ActionEvent event) {
        Button button = (Button) event.getSource();
        TerritoryType territory = TerritoryType.valueOf(button.getAccessibleText());
        claimTerritoryDialog.setContentText("Would you like to claim " + territory + "?");
        claimTerritoryDialog.setVisible(true);
    }

}
