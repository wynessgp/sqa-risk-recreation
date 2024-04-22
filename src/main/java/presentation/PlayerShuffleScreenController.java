package presentation;

import domain.WorldDominationGameEngine;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class PlayerShuffleScreenController {
    @FXML
    private Label instructionLabel;
    @FXML
    private ImageView dieImage;
    private SceneController sceneController;
    private WorldDominationGameEngine gameEngine;

    @FXML
    private void initialize() {
        this.sceneController = SceneController.getInstance();
        this.gameEngine = sceneController.getGameEngine();
        instructionLabel.setText(gameEngine.getCurrentPlayer().toString() + " player: Click the die to roll");
    }

    @FXML
    private void rollDie() {
    }
}
