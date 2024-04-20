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

    private static final int INITIAL_NUM_UNCLAIMED_TERRITORIES = 42;

    private List<PlayerColor> playersList = new ArrayList<>();
    private final Map<PlayerColor, Player> playersMap = new HashMap<>();
    private PlayerColor currentPlayer;

    private int numUnclaimedTerritories;
    private int totalUnplacedArmiesLeft;

    protected TerritoryGraph territoryGraph;
    protected GamePhase currentGamePhase;

    public GameEngine() {
        territoryGraph = initializeGraph();
        currentGamePhase = GamePhase.SCRAMBLE;
        numUnclaimedTerritories = INITIAL_NUM_UNCLAIMED_TERRITORIES;
    }

    private TerritoryGraph initializeGraph() {
        TerritoryGraph graph = new TerritoryGraph();
        for (TerritoryType territoryType : TerritoryType.values()) {
            graph.addNewTerritory(new Territory(territoryType));
        }
        return graph;
    }

    public boolean initializePlayersList(List<PlayerColor> playerOrder) {

        this.playersList = new ArrayList<>(playerOrder);
        initializePlayerColorToPlayerMap(playerOrder);
        currentPlayer = playerOrder.get(0);
        return true;
    }

    private void initializePlayerColorToPlayerMap(List<PlayerColor> playerColors) {
        playerColors.forEach((playerColor) -> playersMap.put(playerColor, new Player(playerColor)));
        if (playerColors.size() == NUM_PLAYERS_WITH_NEUTRAL) { // add the neutral player, if needed.
            playersMap.put(PlayerColor.NEUTRAL, new Player(PlayerColor.NEUTRAL));
        }
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

    public boolean checkIfPlayerOwnsTerritory(TerritoryType relevantTerritory, PlayerColor playerColor) {
        PlayerColor currentController = getPlayerInControlOfTerritory(relevantTerritory);
        return currentController == playerColor;
    }

    private PlayerColor getPlayerInControlOfTerritory(TerritoryType relevantTerritory) {
        Territory territoryObject = territoryGraph.getTerritory(relevantTerritory);
        return territoryObject.getPlayerInControl();
    }

    public boolean placeNewArmiesInTerritory(TerritoryType relevantTerritory, int numArmiesToPlace) {
        if (currentGamePhase == GamePhase.SETUP) {
            handleSetupPhaseArmyPlacement(relevantTerritory, numArmiesToPlace);
        } else {
            handleScramblePhaseArmyPlacement(relevantTerritory, numArmiesToPlace);
        }
        return true;
    }

    private void handleScramblePhaseArmyPlacement(TerritoryType relevantTerritory, int numArmiesToPlace) {
        checkIfTerritoryIsUnclaimed(relevantTerritory);
        checkNumArmiesToPlaceIsValid(numArmiesToPlace);
        checkIfPlayerHasEnoughArmiesToPlace(numArmiesToPlace);
        updateTerritoryObjectWithValidSetupArguments(relevantTerritory, numArmiesToPlace);
        numUnclaimedTerritories--;
        addTerritoryToCurrentPlayerCollection(relevantTerritory);
        updateCurrentPlayer();
        checkScramblePhaseEndCondition();
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

    private void checkIfPlayerHasEnoughArmiesToPlace(int numNewArmiesToPlace) {
        int numArmiesPlayerHasToPlace = playersMap.get(currentPlayer).getNumArmiesToPlace();
        if (numArmiesPlayerHasToPlace - numNewArmiesToPlace <= -1) {
            throw new IllegalArgumentException("Player does not have enough armies to place!");
        }
    }

    private void updateTerritoryObjectWithValidSetupArguments(TerritoryType relevantTerritory, int numArmiesToPlace) {
        Territory territoryObject = territoryGraph.getTerritory(relevantTerritory);
        territoryObject.setNumArmiesPresent(numArmiesToPlace);
        territoryObject.setPlayerInControl(currentPlayer);
    }

    private void addTerritoryToCurrentPlayerCollection(TerritoryType relevantTerritory) {
        Player playerObject = playersMap.get(currentPlayer);
        playerObject.addTerritoryToCollection(relevantTerritory);
    }

    private void updateCurrentPlayer() {
        int currentPlayerIndex = playersList.indexOf(currentPlayer);
        currentPlayer = playersList.get((currentPlayerIndex + 1) % playersList.size());
    }

    private void checkScramblePhaseEndCondition() {
        if (numUnclaimedTerritories == 0) {
            currentGamePhase = GamePhase.SETUP;
            calculateNumUnplacedArmiesLeft();
        }
    }

    private void calculateNumUnplacedArmiesLeft() {
        List<Integer> numArmiesLeftByPlayer = new ArrayList<>();
        for (Player player : playersMap.values()) {
            numArmiesLeftByPlayer.add(player.getNumArmiesToPlace());
        }
        totalUnplacedArmiesLeft = numArmiesLeftByPlayer.stream().mapToInt(Integer::intValue).sum();
    }

    private void handleSetupPhaseArmyPlacement(TerritoryType relevantTerritory, int numArmiesToPlace) {
        checkNumArmiesToPlaceIsValidForSetup(numArmiesToPlace);
        checkIfCurrentPlayerOwnsTerritory(relevantTerritory);
        checkIfPlayerHasEnoughArmiesToPlace(numArmiesToPlace);
        modifyNumArmiesInTerritory(relevantTerritory, numArmiesToPlace);
        modifyNumArmiesCurrentPlayerHasToPlace(numArmiesToPlace);
        totalUnplacedArmiesLeft--;
        updateCurrentPlayer();
        checkSetupPhaseEndCondition();
    }

    private void checkSetupPhaseEndCondition() {
        if (totalUnplacedArmiesLeft == 0) {
            currentGamePhase = GamePhase.PLACEMENT;
        }
    }

    private void checkNumArmiesToPlaceIsValidForSetup(int numArmiesToPlace) {
        if (numArmiesToPlace != 1) {
            throw new IllegalArgumentException(
                    "Cannot place anything other than 1 army in a territory during setup phase");
        }
    }

    private void checkIfCurrentPlayerOwnsTerritory(TerritoryType relevantTerritory) {
        if (!checkIfPlayerOwnsTerritory(relevantTerritory, currentPlayer)) {
            throw new IllegalArgumentException("Cannot place armies on a territory you do not own");
        }
    }

    private void modifyNumArmiesInTerritory(TerritoryType relevantTerritory, int additionalArmies) {
        Territory territoryObject = territoryGraph.getTerritory(relevantTerritory);
        int previousNumArmies = territoryObject.getNumArmiesPresent();
        territoryObject.setNumArmiesPresent(previousNumArmies + additionalArmies);
    }

    private void modifyNumArmiesCurrentPlayerHasToPlace(int numArmiesToPlace) {
        int currentNumArmies = playersMap.get(currentPlayer).getNumArmiesToPlace();
        int newNumArmies = currentNumArmies - numArmiesToPlace;
        playersMap.get(currentPlayer).setNumArmiesToPlace(newNumArmies);
    }

    void provideCurrentPlayerForTurn(PlayerColor currentlyGoingPlayer) {
        currentPlayer = currentlyGoingPlayer;
        // also, add this to the players list, otherwise we'll error out on trying to swap turns.
        playersList.add(currentPlayer);
    }

    public PlayerColor getCurrentPlayer() {
        return currentPlayer;
    }

    void setGamePhase(GamePhase gamePhase) {
        currentGamePhase = gamePhase;
    }

    List<PlayerColor> getPlayersList() {
        return playersList;
    }

    void setPlayerOrderList(List<PlayerColor> playersList) {
        this.playersList = playersList;
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

    public GamePhase getCurrentGamePhase() {
        return currentGamePhase;
    }

    Set<TerritoryType> getClaimedTerritoriesForPlayer(PlayerColor playerInQuestion) {
        return playersMap.get(playerInQuestion).getTerritories();
    }
}
