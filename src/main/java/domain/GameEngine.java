package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class GameEngine {

    private static final int MAXIMUM_ARMIES_POSSIBLE_IN_SETUP = 40;
    private static final int ADDITIONAL_PLAYER_ARMY_OFFSET = 5;
    private static final int PLAYER_LIST_SIZE_OFFSET = 2;
    private static final int NUM_PLAYERS_WITH_NEUTRAL = 2;

    private List<PlayerColor> playersList = new ArrayList<>();
    private final Map<PlayerColor, Player> playersMap = new HashMap<>();

    protected TerritoryGraph territoryGraph;

    public GameEngine() {
        territoryGraph = initializeGraph();
    }

    private TerritoryGraph initializeGraph() {
        TerritoryGraph graph = new TerritoryGraph();
        for (TerritoryType territoryType : TerritoryType.values()) {
            graph.addNewTerritory(new Territory(territoryType));
        }
        return graph;
    }

    public boolean initializePlayersList(List<PlayerColor> playerOrder, int amountOfPlayers) {
        handleNumPlayersAndPlayerOrderSizeErrorChecking(playerOrder.size(), amountOfPlayers);
        checkDuplicatesAndTypes(playerOrder);

        this.playersList = new ArrayList<>(playerOrder);
        initializePlayerColorToPlayerMap(playerOrder);
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

    private void initializePlayerColorToPlayerMap(List<PlayerColor> playerColors) {
        playerColors.forEach((playerColor) -> playersMap.put(playerColor, new Player(playerColor)));
        if (playerColors.size() == NUM_PLAYERS_WITH_NEUTRAL) { // add the neutral player, if needed.
            playersMap.put(PlayerColor.NEUTRAL, new Player(PlayerColor.NEUTRAL));
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
            handleArmyAssignment(NUM_PLAYERS_WITH_NEUTRAL);
        } else {
            handleArmyAssignment(playersList.size());
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
            player.setNumArmiesToPlace(MAXIMUM_ARMIES_POSSIBLE_IN_SETUP - (
                    (numPlayersInGame - PLAYER_LIST_SIZE_OFFSET) * ADDITIONAL_PLAYER_ARMY_OFFSET));
        }
    }

    void provideMockedPlayerObjects(List<Player> mockedPlayers) {
        mockedPlayers.forEach((player) -> playersMap.put(player.getColor(), player));
    }

    int getNumArmiesByPlayerColor(PlayerColor playerColor) {
        return playersMap.get(playerColor).getNumArmiesToPlace();
    }

    void provideMockedTerritoryGraph(TerritoryGraph mockedGraph) {
        territoryGraph = mockedGraph;
    }

    public boolean checkIfTerritoryIsClaimed(TerritoryType territory) {
        Territory territoryObject = territoryGraph.getTerritory(territory);
        return territoryObject.getPlayerInControl() != PlayerColor.SETUP;
    }

    public boolean checkIfPlayerOwnsTerritory(TerritoryType relevantTerritory, PlayerColor playerColor) {
        return checkIfTerritoryIsClaimed(relevantTerritory);
    }
}
