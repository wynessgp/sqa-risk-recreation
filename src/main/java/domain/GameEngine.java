package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class GameEngine {

    private List<PlayerColor> playersList = new ArrayList<>();
    private final Map<PlayerColor, Player> playersMap = new HashMap<>();

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

    public boolean assignSetupArmiesToPlayers() {
        checkIfPlayersListIsEmpty();

        if (playersList.contains(PlayerColor.NEUTRAL)) {
            handleArmyAssignment(2);
        } else {
            handleArmyAssignment(3);
        }
        return true;
    }

    private void checkIfPlayersListIsEmpty() {
        if (playersList.isEmpty()) {
            throw new IllegalStateException(
                    "No player objects exist, call initializePlayersList first with the correct arguments");
        }
    }

    private void handleArmyAssignment(int numPlayersInGame) {
        for (Player player : playersMap.values()) {
            player.setNumArmiesToPlace(40 - ((numPlayersInGame - 2) * 5));
        }
    }

    void provideMockedPlayerObjects(List<Player> mockedPlayers) {
        mockedPlayers.forEach((player) -> playersMap.put(player.getColor(), player));
    }

    int getNumArmiesByPlayerColor(PlayerColor playerColor) {
        return playersMap.get(playerColor).getNumArmiesToPlace();
    }
}
