package presentation;

import domain.PlayerColor;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class PlayerSelectScreenController {
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
        button.setStyle("-fx-background-color: " + button.getText() + "; -fx-text-fill: "
                + (button.getText().equals("Yellow") ? "black" : "white") + ";");
    }

    @FXML
    private void onStartGameButtonClick() {
        for (PlayerColor player : playersOrder) {
            System.out.println(player);
        }
    }

    @FXML
    private void onPlayerSelect(ActionEvent e) {
        Button button = (Button) e.getSource();
        PlayerColor player = PlayerColor.valueOf(button.getText().toUpperCase());
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
        int numPlayers = playersOrder.size();
        if (numPlayers < MIN_PLAYERS) {
            instructionLabel.setText("Select " + (MIN_PLAYERS - numPlayers) + " or more players to continue");
        } else if (numPlayers < MAX_PLAYERS) {
            instructionLabel.setText("Start the game or continue adding players");
        } else {
            instructionLabel.setText("Click start game to begin");
        }
    }

}
