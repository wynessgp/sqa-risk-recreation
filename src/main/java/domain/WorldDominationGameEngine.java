package domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class WorldDominationGameEngine {

    private static final int MINIMUM_NUM_PLAYERS = 3;
    private static final int MAXIMUM_NUM_PLAYERS = 6;

    private static final int MAXIMUM_ARMIES_POSSIBLE_IN_SETUP = 35;
    private static final int ADDITIONAL_PLAYER_ARMY_OFFSET = 5;
    private static final int PLAYER_LIST_SIZE_OFFSET = 3;

    private static final int INITIAL_NUM_UNCLAIMED_TERRITORIES = 42;

    private static final int REQUIRED_NUM_TERRITORIES_TO_EARN_MORE_THAN_THREE_ARMIES = 12;
    private static final int THREE_ARMIES = 3;
    private static final int REQUIRED_TERRITORIES_PER_EXTRA_ARMY = 3;

    private static final int FORCED_CARD_TURN_IN_THRESHOLD = 5;

    private List<PlayerColor> playersList = new ArrayList<>();
    private final Map<PlayerColor, Player> playersMap = new HashMap<>();
    private PlayerColor currentPlayer;

    private int numUnclaimedTerritories;
    private int totalUnplacedArmiesLeft;

    private TerritoryGraph territoryGraph;
    private GamePhase currentGamePhase;

    private DieRollParser dieRollParser;
    private TradeInParser tradeInParser;
    private List<Integer> dieRolls;

    WorldDominationGameEngine(List<PlayerColor> playerOrder, DieRollParser parser) {
        currentGamePhase = GamePhase.SCRAMBLE;
        numUnclaimedTerritories = INITIAL_NUM_UNCLAIMED_TERRITORIES;
        handleOtherDependentObjectCreation(parser);
        initializePlayersList(playerOrder);
        shufflePlayers();
        currentPlayer = playersList.get(0);
        assignSetupArmiesToPlayers();
    }

    private void handleOtherDependentObjectCreation(DieRollParser parser) {
        this.territoryGraph = initializeGraph();
        this.dieRollParser = parser;
        this.tradeInParser = new TradeInParser();
    }

    void shufflePlayers() {
        this.dieRolls = dieRollParser.rollDiceToDeterminePlayerOrder(playersList.size());
        sortPlayersListByDieRoll();
    }

    private void sortPlayersListByDieRoll() {
        List<Integer> sortedDieRolls = new ArrayList<>(dieRolls);
        sortedDieRolls.sort(Comparator.reverseOrder());
        List<PlayerColor> newPlayerOrder = new ArrayList<>();
        for (int i : sortedDieRolls) {
            int index = dieRolls.indexOf(i);
            newPlayerOrder.add(playersList.get(index));
        }
        this.playersList = newPlayerOrder;
    }

    public WorldDominationGameEngine(List<PlayerColor> playerOrder) {
        this(playerOrder, new DieRollParser());
    }

    private TerritoryGraph initializeGraph() {
        TerritoryGraph graph = new TerritoryGraph();
        for (TerritoryType territoryType : TerritoryType.values()) {
            graph.addNewTerritory(new Territory(territoryType));
        }
        return graph;
    }

    boolean initializePlayersList(List<PlayerColor> playerOrder) {
        handleErrorCheckingForOrderSize(playerOrder);
        handleErrorCheckingForOrderContents(playerOrder);

        this.playersList = new ArrayList<>(playerOrder);
        initializePlayerColorToPlayerMap(playerOrder);
        return true;
    }

    private void handleErrorCheckingForOrderSize(List<PlayerColor> playerOrder) {
        if (playerOrder.size() < MINIMUM_NUM_PLAYERS || playerOrder.size() > MAXIMUM_NUM_PLAYERS) {
            throw new IllegalArgumentException(String.format(
                    "playerOrder's size is not within: [%d, %d]", MINIMUM_NUM_PLAYERS, MAXIMUM_NUM_PLAYERS));
        }
    }

    private void handleErrorCheckingForOrderContents(List<PlayerColor> playerOrder) {
        if (Set.copyOf(playerOrder).size() != playerOrder.size()) {
            throw new IllegalArgumentException("playerOrder contains duplicate entries");
        }
        if (playerOrder.contains(PlayerColor.SETUP)) {
            throw new IllegalArgumentException("playerOrder contains SETUP as one of the players");
        }
    }

    private void initializePlayerColorToPlayerMap(List<PlayerColor> playerColors) {
        playerColors.forEach((playerColor) -> playersMap.put(playerColor, new Player(playerColor)));
    }

    boolean assignSetupArmiesToPlayers() {
        checkIfPlayersListIsEmpty();

        handleArmyAssignment(playersList.size());
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
        Territory territoryObject = getTerritoryFromType(relevantTerritory);
        return territoryObject.isOwnedByPlayer(playerColor);
    }

    private Territory getTerritoryFromType(TerritoryType relevantTerritory) {
        return territoryGraph.getTerritory(relevantTerritory);
    }

    public boolean placeNewArmiesInTerritory(TerritoryType relevantTerritory, int numArmiesToPlace) {
        if (currentGamePhase == GamePhase.SETUP) {
            handleSetupPhaseArmyPlacement(relevantTerritory, numArmiesToPlace);
        } else if (currentGamePhase == GamePhase.SCRAMBLE) {
            handleScramblePhaseArmyPlacement(relevantTerritory, numArmiesToPlace);
        } else {
            handlePlacementPhaseArmyPlacement(relevantTerritory, numArmiesToPlace);
        }
        return true;
    }

    private void handleScramblePhaseArmyPlacement(TerritoryType relevantTerritory, int numArmiesToPlace) {
        checkIfTerritoryIsUnclaimed(relevantTerritory);
        checkIfNumArmiesToPlaceIsValidForScramblePhase(numArmiesToPlace);
        checkIfPlayerHasEnoughArmiesToPlace(numArmiesToPlace);
        updateTerritoryObjectWithValidSetupArguments(relevantTerritory, numArmiesToPlace);
        decreaseNumArmiesCurrentPlayerHasToPlace(numArmiesToPlace);
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

    private void checkIfNumArmiesToPlaceIsValidForScramblePhase(int numArmiesToPlace) {
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
        numUnclaimedTerritories--;
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
        decreaseNumArmiesCurrentPlayerHasToPlace(numArmiesToPlace);
        totalUnplacedArmiesLeft--;
        updateCurrentPlayer();
        checkSetupPhaseEndCondition();
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

    private void decreaseNumArmiesCurrentPlayerHasToPlace(int numToDecreaseBy) {
        int currentNumArmies = playersMap.get(currentPlayer).getNumArmiesToPlace();
        int newNumArmies = currentNumArmies - numToDecreaseBy;
        playersMap.get(currentPlayer).setNumArmiesToPlace(newNumArmies);
    }

    private void checkSetupPhaseEndCondition() {
        if (totalUnplacedArmiesLeft == 0) {
            currentGamePhase = GamePhase.PLACEMENT;
            currentPlayer = playersList.get(0);
            int numArmiesToAssign = calculatePlacementPhaseArmiesForCurrentPlayer();
            increaseNumArmiesCurrentPlayerHasToPlace(numArmiesToAssign);
        }
    }

    private void increaseNumArmiesCurrentPlayerHasToPlace(int numToIncreaseBy) {
        // little shortcut: we can multiply numToIncreaseBy by -1 and call the decrease method.
        decreaseNumArmiesCurrentPlayerHasToPlace(-1 * numToIncreaseBy);
    }

    int calculatePlacementPhaseArmiesForCurrentPlayer() {
        int numOwnedTerritories = getTerritoriesCurrentPlayerOwns().size();
        checkIfPlayerShouldExistOrGameIsOverWithTerritoryCount(numOwnedTerritories);

        int numOwnedTerritoriesAmount = (numOwnedTerritories < REQUIRED_NUM_TERRITORIES_TO_EARN_MORE_THAN_THREE_ARMIES)
                ? THREE_ARMIES : numOwnedTerritories / REQUIRED_TERRITORIES_PER_EXTRA_ARMY;
        return calculateBonusForOwnedContinents() + numOwnedTerritoriesAmount;
    }

    private Set<TerritoryType> getTerritoriesCurrentPlayerOwns() {
        Set<TerritoryType> ownedTerritories = new HashSet<>();
        for (TerritoryType territory : TerritoryType.values()) {
            if (checkIfPlayerOwnsTerritory(territory, currentPlayer)) {
                ownedTerritories.add(territory);
            }
        }
        return ownedTerritories;
    }

    private void checkIfPlayerShouldExistOrGameIsOverWithTerritoryCount(int numOwnedTerritories) {
        if (numOwnedTerritories == TerritoryType.values().length) {
            throw new IllegalStateException("Given player owns every territory, the game should be over!");
        }
        if (numOwnedTerritories == 0) {
            throw new IllegalStateException("The current player should no longer exist!");
        }
    }

    private int calculateBonusForOwnedContinents() {
        Set<TerritoryType> ownedTerritories = getTerritoriesCurrentPlayerOwns();
        int totalBonus = 0;
        for (Continent continent : Continent.values()) {
            totalBonus += continent.getContinentBonusIfPlayerHasTerritories(ownedTerritories);
        }
        return totalBonus;
    }

    private void handlePlacementPhaseArmyPlacement(TerritoryType relevantTerritory, int numArmiesToPlace) {
        checkIfPlayerIsHoldingTooManyCards();
        checkIfNumArmiesToPlaceIsValidForPlacement(numArmiesToPlace);
        checkIfCurrentPlayerOwnsTerritory(relevantTerritory);
        checkIfPlayerHasEnoughArmiesToPlace(numArmiesToPlace);
        decreaseNumArmiesCurrentPlayerHasToPlace(numArmiesToPlace);
        modifyNumArmiesInTerritory(relevantTerritory, numArmiesToPlace);
        checkAttackPhaseEndCondition();
    }

    private void checkIfPlayerIsHoldingTooManyCards() {
        if (playersMap.get(currentPlayer).getNumCardsHeld() >= FORCED_CARD_TURN_IN_THRESHOLD) {
            throw new IllegalStateException("Player cannot place armies while they are holding more than 5 cards!");
        }
    }

    private void checkIfNumArmiesToPlaceIsValidForPlacement(int numArmiesToPlace) {
        if (numArmiesToPlace < 1) {
            throw new IllegalArgumentException("Cannot place < 1 army on a territory during the Placement phase");
        }
    }

    private void checkAttackPhaseEndCondition() {
        if (playersMap.get(currentPlayer).getNumArmiesToPlace() == 0) {
            currentGamePhase = GamePhase.ATTACK;
        }
    }

    public Set<TerritoryType> tradeInCards(Set<Card> selectedCardsToTradeIn) {
        checkIfInValidGamePhase(Set.of(GamePhase.ATTACK, GamePhase.PLACEMENT));

        Player currentPlayerObject = playersMap.get(currentPlayer);
        checkIfPlayerOwnsCards(selectedCardsToTradeIn, currentPlayerObject);
        return addArmiesToPlayerStockpileIfValidSet(selectedCardsToTradeIn, currentPlayerObject);
    }

    private void checkIfInValidGamePhase(Set<GamePhase> validGamePhases) {
        if (!validGamePhases.contains(currentGamePhase)) {
            throw new IllegalStateException("Can only trade in cards during the PLACEMENT or ATTACK phase");
        }
    }

    private void checkIfPlayerOwnsCards(Set<Card> givenCardSet, Player currentPlayerObject) {
        if (!currentPlayerObject.ownsAllGivenCards(givenCardSet)) {
            throw new IllegalArgumentException("Player doesn't own all the selected cards!");
        }
    }

    private Set<TerritoryType> addArmiesToPlayerStockpileIfValidSet(Set<Card> selectedCards, Player playerObject) {
        try {
            int numArmiesToReceive = tradeInParser.startTrade(selectedCards);
            Set<TerritoryType> matchedTerritories = tradeInParser.getMatchedTerritories(playerObject, selectedCards);
            modifyPlayerObjectAndGamePhaseToAccountForTradeIn(playerObject, numArmiesToReceive, selectedCards);
            return matchedTerritories;
        } catch (Exception tradeError) {
            throw new IllegalArgumentException(String.format("Could not trade in cards: %s", tradeError.getMessage()));
        }
    }

    private void modifyPlayerObjectAndGamePhaseToAccountForTradeIn(
            Player playerObject, int numArmiesToReceive, Set<Card> selectedCards) {
        increaseNumArmiesCurrentPlayerHasToPlace(numArmiesToReceive);
        playerObject.removeAllGivenCards(selectedCards);
        currentGamePhase = GamePhase.PLACEMENT;
    }

    public void attackTerritory(TerritoryType sourceTerritory, TerritoryType destTerritory,
                                int numAttackers, int numDefenders) {
        if (!territoryGraph.areTerritoriesAdjacent(sourceTerritory, destTerritory)) {
            throw new IllegalArgumentException("Source and destination territory must be two adjacent territories!");
        }
        if (!checkIfPlayerOwnsTerritory(sourceTerritory, currentPlayer)) {
            throw new IllegalArgumentException("Source territory is not owned by the current player!");
        }
        throw new IllegalArgumentException("Source and destination territory must be two adjacent territories!");
    }

    public PlayerColor getCurrentPlayer() {
        return currentPlayer;
    }

    public GamePhase getCurrentGamePhase() {
        return currentGamePhase;
    }

    public List<PlayerColor> getPlayerOrder() {
        return new ArrayList<>(playersList);
    }

    public List<Integer> getDieRolls() {
        return new ArrayList<>(dieRolls);
    }

    public int getCurrentPlayerArmiesToPlace() {
        return playersMap.get(currentPlayer).getNumArmiesToPlace();
    }

    public int getNumberOfArmies(TerritoryType territoryType) {
        return territoryGraph.getTerritory(territoryType).getNumArmiesPresent();
    }

    void provideCurrentPlayerForTurn(PlayerColor currentlyGoingPlayer) {
        currentPlayer = currentlyGoingPlayer;
        // also, add this to the players list, otherwise we'll error out on trying to swap turns.
        playersList.add(currentPlayer);
    }

    void setGamePhase(GamePhase gamePhase) {
        currentGamePhase = gamePhase;
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

    Set<TerritoryType> getClaimedTerritoriesForPlayer(PlayerColor playerInQuestion) {
        return playersMap.get(playerInQuestion).getTerritories();
    }

    WorldDominationGameEngine() {
        territoryGraph = initializeGraph();
        currentGamePhase = GamePhase.SCRAMBLE;
        numUnclaimedTerritories = INITIAL_NUM_UNCLAIMED_TERRITORIES;
    }

    void setParser(DieRollParser parser) {
        this.dieRollParser = parser;
    }

    void claimAllTerritoriesForCurrentPlayer(Set<TerritoryType> territoriesToClaim) {
        for (TerritoryType territory : territoriesToClaim) {
            territoryGraph.getTerritory(territory).setPlayerInControl(currentPlayer);
        }
    }

    void provideMockedTradeInParser(TradeInParser mockedParser) {
        this.tradeInParser = mockedParser;
    }

    void setCardsForPlayer(PlayerColor playerColor, Set<Card> cardsPlayerOwns) {
        playersMap.get(playerColor).setOwnedCards(cardsPlayerOwns);
    }

    int getNumCardsForPlayer(PlayerColor playerColor) {
        return playersMap.get(playerColor).getNumCardsHeld();
    }
}
