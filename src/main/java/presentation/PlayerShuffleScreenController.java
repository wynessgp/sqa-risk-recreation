package presentation;

import domain.PlayerColor;
import domain.WorldDominationGameEngine;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PlayerShuffleScreenController {
    @FXML
    private Label instructionLabel;
    @FXML
    private Label dieRollResult;
    @FXML
    private ImageView dieImage;
    private List<PlayerColor> originalPlayerOrder;
    private List<Integer> dieRolls;
    private int currentPlayer = -1;

    @FXML
    private void initialize() {
        SceneController sceneController = SceneController.getInstance();
        WorldDominationGameEngine gameEngine = sceneController.getGameEngine();
        this.originalPlayerOrder = sceneController.getOriginalPlayerOrder();
        this.dieRolls = gameEngine.getDieRolls();
        dieRollResult.setVisible(false);
        prepareCurrentPlayerRoll();
    }

    private void prepareCurrentPlayerRoll() {
        currentPlayer++;
        if (currentPlayer >= originalPlayerOrder.size()) {
            instructionLabel.setText("Click start game to continue");
        } else {
            instructionLabel.setText(originalPlayerOrder.get(currentPlayer).toString()
                    + " player: Click the die to roll");
        }
    }

    @FXML
    private void rollDie() {
        if (currentPlayer < originalPlayerOrder.size()) {
            dieImage.setImage(DieImage.get(dieRolls.get(currentPlayer), getClass()));
            dieRollResult.setText(originalPlayerOrder.get(currentPlayer).toString() + ": You rolled a "
                    + dieRolls.get(currentPlayer) + "!");
            dieRollResult.setVisible(true);
            prepareCurrentPlayerRoll();
        }
    }

    @FXML
    private void startGame() {

    }

    private enum DieImage {
        ONE, TWO, THREE, FOUR, FIVE, SIX;

        private static final Map<Integer, DieImage> dieMap = new HashMap<>(
                Map.of(1, ONE, 2, TWO, 3, THREE, 4, FOUR, 5, FIVE, 6, SIX)
        );

        private static Image get(int roll, Class<?> className) {
            DieImage dieImage = dieMap.get(roll);
            try {
                return new Image(Objects.requireNonNull(className.getResource(String.format("images\\die-%s.png",
                        dieImage.toString().toLowerCase()))).openStream());
            } catch (Exception e) {
                System.err.println("Error loading die image: " + dieImage.toString());
                return null;
            }
        }
    }
}
