package presentation;

import domain.PlayerColor;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;

public class PlayerSelectScreenController implements GameScene {
    private static final int MIN_PLAYERS = 3;
    private static final int MAX_PLAYERS = 6;

    @FXML
    private Button startGameButton;
    @FXML
    private Button resetButton;
    @FXML
    private Label instructionLabel;
    @FXML
    private Button blackButton;
    @FXML
    private Button redButton;
    @FXML
    private Button yellowButton;
    @FXML
    private Button blueButton;
    @FXML
    private Button greenButton;
    @FXML
    private Button purpleButton;
    List<PlayerColor> playersOrder = new ArrayList<>();
    SceneController controller = SceneController.getInstance();

    @FXML
    private void initialize() {
        SceneController.setCurrentScene(this);
        updateInstructionLabel();
    }

    @FXML
    private void onBackButtonClick() {
        SceneController.getInstance().activate(SceneType.START);
    }

    @FXML
    private void onResetButtonClick() {
        playersOrder.clear();
        resetButtonStates();
        startGameButton.setDisable(true);
        resetButton.setDisable(true);
        updateInstructionLabel();
    }

    private void resetButtonStates() {
        resetPlayerButton(blackButton);
        resetPlayerButton(redButton);
        resetPlayerButton(yellowButton);
        resetPlayerButton(blueButton);
        resetPlayerButton(greenButton);
        resetPlayerButton(purpleButton);
    }

    private void resetPlayerButton(Button button) {
        button.setDisable(false);
        button.setStyle("-fx-background-color: " + button.getAccessibleText() + "; -fx-text-fill: "
                + (button.getText().equals(SceneController.getString("global.yellow", null)) ? "black"
                : "white"));
    }

    @FXML
    private void onStartGameButtonClick() {
        controller.initializePlayers(playersOrder);
        controller.activate(SceneType.SHUFFLE);
    }

    @FXML
    private void onPlayerSelect(ActionEvent e) {
        Button button = (Button) e.getSource();
        PlayerColor player = PlayerColor.valueOf(button.getAccessibleText());
        playersOrder.add(player);
        handlePlayerSelectUpdate(button);
        if (playersOrder.size() >= 3) {
            startGameButton.setDisable(false);
        }
    }

    private void handlePlayerSelectUpdate(Button button) {
        resetButton.setDisable(false);
        button.setDisable(true);
        button.setStyle("-fx-background-color: #8b8b8b; -fx-text-fill: white; -fx-cursor: default;");
        updateInstructionLabel();
    }

    private void updateInstructionLabel() {
        if (playersOrder.size() < MIN_PLAYERS) {
            instructionLabel.setText(SceneController.getString("playerSelectScreen.selectPrompt",
                    new Object[]{MIN_PLAYERS - playersOrder.size()}));
        } else if (playersOrder.size() < MAX_PLAYERS) {
            instructionLabel.setText(SceneController.getString("playerSelectScreen.ready", null));
        } else {
            instructionLabel.setText(SceneController.getString("playerSelectScreen.maxPlayers", null));
        }
    }

    @Override
    public void onKeyPress(KeyEvent event) {}

}
