package presentation;

import domain.PlayerColor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class PlayerSelectScreenController {

    @FXML
    private void onBackButtonClick() {
        SceneController.getInstance().activate(SceneType.START);
    }

    @FXML
    private void onPlayerSelect(ActionEvent e) {
        String buttonText = ((Button) e.getSource()).getText();
        PlayerColor player = PlayerColor.valueOf(buttonText.toUpperCase());
        System.out.println("Player selected: " + player);
    }

}
