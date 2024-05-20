package presentation;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;

public class WinScreenController implements GameScene {
    @FXML
    private Label winLabel;

    @FXML
    private void initialize() {
        winLabel.setText(SceneController.getString("winScreen.playerWinner",
                new Object[]{SceneController.getInstance().getWinner()}));
    }

    @FXML
    private void onPlayAgain() {
        SceneController.getInstance().activate(SceneType.START);
    }

    @Override
    public void onKeyPress(KeyEvent event) {
    }

}
