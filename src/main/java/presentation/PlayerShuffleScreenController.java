package presentation;

import datasource.FileLoader;
import datasource.ImageFileLoader;
import domain.PlayerColor;
import domain.WorldDominationGameEngine;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PlayerShuffleScreenController {
    @FXML
    private Label instructionLabel;
    @FXML
    private Label dieRollResult;
    @FXML
    private Label rollOrderLabel;
    @FXML
    private ImageView dieImage;
    @FXML
    private Button startGameButton;
    private final SceneController sceneController = SceneController.getInstance();
    private List<PlayerColor> originalPlayerOrder;
    private List<PlayerColor> updatedPlayerOrder;
    private List<Integer> dieRolls;
    private int currentPlayer = -1;

    @FXML
    private void initialize() {
        WorldDominationGameEngine gameEngine = sceneController.getGameEngine();
        this.originalPlayerOrder = sceneController.getOriginalPlayerOrder();
        this.dieRolls = gameEngine.getDieRolls();
        this.updatedPlayerOrder = gameEngine.getPlayerOrder();
        dieRollResult.setVisible(false);
        startGameButton.setVisible(false);
        prepareCurrentPlayerRoll();
    }

    private void prepareCurrentPlayerRoll() {
        currentPlayer++;
        if (currentPlayer >= originalPlayerOrder.size()) {
            prepareStartGame();
        } else {
            instructionLabel.setText(originalPlayerOrder.get(currentPlayer).toString()
                    + " player: Click the die to roll");
        }
    }

    private void prepareStartGame() {
        startGameButton.setVisible(true);
        instructionLabel.setText("Click start game to continue");
        rollOrderLabel.setText("Player order: " + stringifyPlayerOrder());
        dieImage.setCursor(Cursor.DEFAULT);
    }

    private String stringifyPlayerOrder() {
        StringBuilder playerOrder = new StringBuilder();
        for (PlayerColor player : updatedPlayerOrder) {
            playerOrder.append(player.toString()).append(", ");
        }
        playerOrder.delete(playerOrder.length() - 2, playerOrder.length());
        return playerOrder.toString();
    }

    @FXML
    private void rollDie() {
        if (currentPlayer < originalPlayerOrder.size()) {
            dieImage.setImage(DieImage.get(dieRolls.get(currentPlayer)));
            dieRollResult.setText(originalPlayerOrder.get(currentPlayer).toString() + ": You rolled a "
                    + dieRolls.get(currentPlayer) + "!");
            dieRollResult.setVisible(true);
            prepareCurrentPlayerRoll();
        }
    }

    @FXML
    private void startGame() {
        sceneController.activate(SceneType.GAME);
    }

    private enum DieImage {
        ONE, TWO, THREE, FOUR, FIVE, SIX;

        private static final Map<Integer, DieImage> dieMap = new HashMap<>(
                Map.of(1, ONE, 2, TWO, 3, THREE, 4, FOUR, 5, FIVE, 6, SIX)
        );

        private static Image get(int roll) {
            FileLoader imageLoader = new ImageFileLoader();
            try {
                imageLoader.open(String.format("die-%s.png", dieMap.get(roll).toString().toLowerCase()));
                return new Image(imageLoader.getFileUrl().openStream());
            } catch (Exception e) {
                System.err.println("Error loading image for die roll " + roll);
                return null;
            }
        }
    }

}
