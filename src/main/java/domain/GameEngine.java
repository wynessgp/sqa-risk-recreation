package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class GameEngine {

    private List<PlayerColor> playersList = new ArrayList<>();

    public boolean initializePlayersList(List<PlayerColor> playerOrder, int amountOfPlayers) {
        handleNumPlayersAndPlayerOrderSizeErrorChecking(playerOrder.size(), amountOfPlayers);
        checkDuplicatesAndTypes(playerOrder);

        this.playersList = new ArrayList<>(playerOrder);
        return true;
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

    private void checkDuplicatesAndTypes(List<PlayerColor> playerOrder) {
        if (Set.copyOf(playerOrder).size() != playerOrder.size()) {
            throw new IllegalArgumentException("Player order contains duplicate entries");
        }
        if (playerOrder.contains(PlayerColor.SETUP)) {
            throw new IllegalArgumentException("Player order contains SETUP as one of the players");
        }
    }

    protected List<PlayerColor> getPlayersList() {
        return playersList;
    }

    void setPlayerOrderList(List<PlayerColor> playersList) {
        this.playersList = playersList;
    }

    public void assignSetupArmiesToPlayers() {
        if (playersList.isEmpty()) {
            throw new IllegalStateException(
                    "No player objects exist, call initializePlayersList first with the correct arguments");
        }
    }
}
