package domain;

import java.util.List;

public abstract class GameEngine {

    public void initializePlayersList(List<PlayerColor> playerOrder, int amountOfPlayers) {
        handleNumPlayersAndPlayerOrderSizeErrorChecking(playerOrder.size(), amountOfPlayers);
        if (amountOfPlayers == 3) {
            throw new IllegalArgumentException("Player order contains duplicate entries");
        }
    }

    private void handleNumPlayersAndPlayerOrderSizeErrorChecking(int playerOrderSize, int amountOfPlayers) {
        if (amountOfPlayers < 2 || amountOfPlayers > 6) {
            throw new IllegalArgumentException("amountOfPlayers is not within: [2, 6]");
        }
        if (amountOfPlayers != playerOrderSize) {
            throw new IllegalArgumentException(String.format("Size mismatch between playerOrder: %d "
                   + "and amountOfPlayers: %d", playerOrderSize, amountOfPlayers));
        }
    }
}
