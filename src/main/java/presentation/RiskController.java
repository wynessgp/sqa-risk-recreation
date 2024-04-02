package presentation;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class RiskController {

    @FXML
    private Label errorText;

    @FXML
    private TextField playerNumberBox;

    @FXML
    private void parsePeoplePlaying() {
        String userInput = playerNumberBox.getText();
        if (userInput.matches("[2-6]") && userInput.length() == 1) {
            // create the player choice scene.
            System.out.println("bruh bruh bruh");
        } else {
            errorText.setText("Valid input is a number [2, 6]");
        }
    }
}
