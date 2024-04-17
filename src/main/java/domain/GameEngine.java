package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class GameEngine {

    private static final int MINIMUM_NUM_PLAYERS = 2;
    private static final int MAXIMUM_NUM_PLAYERS = 6;

    private static final int MAXIMUM_ARMIES_POSSIBLE_IN_SETUP = 40;
    private static final int ADDITIONAL_PLAYER_ARMY_OFFSET = 5;
    private static final int PLAYER_LIST_SIZE_OFFSET = 2;
    private static final int NUM_PLAYERS_WITH_NEUTRAL = 2;

    private List<PlayerColor> playersList = new ArrayList<>();
    private final Map<PlayerColor, Player> playersMap = new HashMap<>();
    private PlayerColor currentPlayer;

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
        currentPlayer = PlayerColor.RED;
        return true;
    }

    private void handleNumPlayersAndPlayerOrderSizeErrorChecking(int playerOrderSize, int amountOfPlayers) {
        if (amountOfPlayers < MINIMUM_NUM_PLAYERS || amountOfPlayers > MAXIMUM_NUM_PLAYERS) {
            throw new IllegalArgumentException(String.format("amountOfPlayers is not within: [%d, %d]",
                    MINIMUM_NUM_PLAYERS, MAXIMUM_NUM_PLAYERS));
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

    public boolean checkIfPlayerOwnsTerritory(TerritoryType relevantTerritory, PlayerColor playerColor) {
        PlayerColor currentController = getPlayerInControlOfTerritory(relevantTerritory);
        return currentController == playerColor;
    }

    private PlayerColor getPlayerInControlOfTerritory(TerritoryType relevantTerritory) {
        Territory territoryObject = territoryGraph.getTerritory(relevantTerritory);
        return territoryObject.getPlayerInControl();
    }

    public boolean placeNewArmiesInTerritory(TerritoryType relevantTerritory, int numArmiesToPlace) {
        checkIfTerritoryIsUnclaimed(relevantTerritory);
        checkNumArmiesToPlaceIsValid(numArmiesToPlace);

        Territory territoryObject = territoryGraph.getTerritory(relevantTerritory);
        territoryObject.setNumArmiesPresent(numArmiesToPlace);
        territoryObject.setPlayerInControl(currentPlayer);
        currentPlayer = PlayerColor.PURPLE;
        return true;
    }

    private void checkIfTerritoryIsUnclaimed(TerritoryType relevantTerritory) {
        if (!checkIfPlayerOwnsTerritory(relevantTerritory, PlayerColor.SETUP)) {
            throw new IllegalStateException(
                    "Cannot place armies in a claimed territory until the scramble phase is over");
        }
    }

    private void checkNumArmiesToPlaceIsValid(int numArmiesToPlace) {
        if (numArmiesToPlace != 1) {
            throw new IllegalArgumentException(
                    "You can only place 1 army on an unclaimed territory until the scramble phase is over");
        }
    }

    void provideCurrentPlayerForTurn(PlayerColor currentlyGoingPlayer) {
        currentPlayer = currentlyGoingPlayer;
    }

    public PlayerColor getCurrentPlayer() {
        return currentPlayer;
    }
}
