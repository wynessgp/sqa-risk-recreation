package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
    private static final int ATTACK_PHASE_FORCED_CARD_TURN_IN_THRESHOLD = 6;

    private static final int MINIMUM_NUMBER_OF_ATTACKING_ARMIES = 1;
    private static final int MAXIMUM_NUMBER_OF_ATTACKING_ARMIES = 3;

    private static final int MINIMUM_NUMBER_OF_DEFENDING_ARMIES = 1;
    private static final int MAXIMUM_NUMBER_OF_DEFENDING_ARMIES = 2;

    private List<PlayerColor> playersList = new ArrayList<>();
    private Map<PlayerColor, Player> playersMap = new HashMap<>();
    private PlayerColor currentPlayer;

    private int numUnclaimedTerritories;
    private int totalUnplacedArmiesLeft;

    private TerritoryGraph territoryGraph;
    private GamePhase currentGamePhase;

    private DieRollParser dieRollParser;
    private TradeInParser tradeInParser;
    private List<Integer> dieRolls;

    private List<Integer> attackerRolls;
    private List<Integer> defenderRolls;
    private List<BattleResult> battleResults;

    private TerritoryType recentlyAttackedSource = null;
    private TerritoryType recentlyAttackedDestination = null;
    private boolean currentPlayerCanClaimCard = false;
    private RiskCardDeck cardDeck;

    public WorldDominationGameEngine(List<PlayerColor> playerOrder) {
        this(playerOrder, new DieRollParser());
    }

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
        addAllEdgesToTerritoryGraph();
        this.dieRollParser = parser;
        this.tradeInParser = new TradeInParser();
        this.cardDeck = new RiskCardDeck();
    }

    private TerritoryGraph initializeGraph() {
        TerritoryGraph graph = new TerritoryGraph();
        for (TerritoryType territoryType : TerritoryType.values()) {
            graph.addNewTerritory(new Territory(territoryType));
        }
        return graph;
    }

    private void addAllEdgesToTerritoryGraph() {
        addNorthAmericanGraphEdges();
        addSouthAmericanGraphEdges();
        addAfricanGraphEdges();
        addEuropeanGraphEdges();
        addAsianGraphEdges();
        addOceanicGraphEdges();
    }

    private void addNorthAmericanGraphEdges() {
        addAlaskaEdges();
        addNorthwestTerritoryEdges();
        addGreenlandEdges();
        addAlbertaEdges();
        addOntarioEdges();
        addRemainingNorthAmericanGraphEdges();
    }

    private void addAlaskaEdges() {
        territoryGraph.addSetOfAdjacencies(TerritoryType.ALASKA,
                Set.of(TerritoryType.KAMCHATKA, TerritoryType.NORTHWEST_TERRITORY, TerritoryType.ALBERTA));
    }

    private void addNorthwestTerritoryEdges() {
        territoryGraph.addSetOfAdjacencies(TerritoryType.NORTHWEST_TERRITORY,
                Set.of(TerritoryType.GREENLAND, TerritoryType.ALBERTA, TerritoryType.ONTARIO));
    }

    private void addGreenlandEdges() {
        territoryGraph.addSetOfAdjacencies(TerritoryType.GREENLAND,
                Set.of(TerritoryType.ICELAND, TerritoryType.ONTARIO, TerritoryType.QUEBEC));
    }

    private void addAlbertaEdges() {
        territoryGraph.addSetOfAdjacencies(TerritoryType.ALBERTA,
                Set.of(TerritoryType.ONTARIO, TerritoryType.WESTERN_UNITED_STATES));
    }

    private void addOntarioEdges() {
        territoryGraph.addSetOfAdjacencies(TerritoryType.ONTARIO,
                Set.of(TerritoryType.QUEBEC, TerritoryType.WESTERN_UNITED_STATES, TerritoryType.EASTERN_UNITED_STATES));
    }

    private void addRemainingNorthAmericanGraphEdges() {
        addQuebecEdges();
        addWesternUnitedStatesEdges();
        addEasternUnitedStatsEdges();
        addCentralAmericaEdges();
    }

    private void addQuebecEdges() {
        territoryGraph.addSetOfAdjacencies(TerritoryType.QUEBEC, Set.of(TerritoryType.EASTERN_UNITED_STATES));
    }

    private void addWesternUnitedStatesEdges() {
        territoryGraph.addSetOfAdjacencies(TerritoryType.WESTERN_UNITED_STATES,
                Set.of(TerritoryType.EASTERN_UNITED_STATES, TerritoryType.CENTRAL_AMERICA));
    }

    private void addEasternUnitedStatsEdges() {
        territoryGraph.addSetOfAdjacencies(TerritoryType.EASTERN_UNITED_STATES, Set.of(TerritoryType.CENTRAL_AMERICA));
    }

    private void addCentralAmericaEdges() {
        territoryGraph.addSetOfAdjacencies(TerritoryType.CENTRAL_AMERICA, Set.of(TerritoryType.VENEZUELA));
    }

    private void addSouthAmericanGraphEdges() {
        addVenezuelaEdges();
        addPeruEdges();
        addBrazilEdges();
    }

    private void addVenezuelaEdges() {
        territoryGraph.addSetOfAdjacencies(TerritoryType.VENEZUELA, Set.of(TerritoryType.PERU, TerritoryType.BRAZIL));
    }

    private void addPeruEdges() {
        territoryGraph.addSetOfAdjacencies(TerritoryType.PERU, Set.of(TerritoryType.BRAZIL, TerritoryType.ARGENTINA));
    }

    private void addBrazilEdges() {
        territoryGraph.addSetOfAdjacencies(TerritoryType.BRAZIL,
                Set.of(TerritoryType.ARGENTINA, TerritoryType.NORTH_AFRICA));
    }

    private void addAfricanGraphEdges() {
        addNorthAfricaEdges();
        addEgyptEdges();
        addCongoEdges();
        addEastAfricaEdges();
        addSouthAfricaEdges();
    }

    private void addNorthAfricaEdges() {
        territoryGraph.addSetOfAdjacencies(TerritoryType.NORTH_AFRICA,
                Set.of(TerritoryType.WESTERN_EUROPE, TerritoryType.EGYPT, TerritoryType.CONGO,
                        TerritoryType.EAST_AFRICA, TerritoryType.SOUTHERN_EUROPE));
    }

    private void addEgyptEdges() {
        territoryGraph.addSetOfAdjacencies(TerritoryType.EGYPT,
                Set.of(TerritoryType.SOUTHERN_EUROPE, TerritoryType.MIDDLE_EAST, TerritoryType.EAST_AFRICA));
    }

    private void addCongoEdges() {
        territoryGraph.addSetOfAdjacencies(TerritoryType.CONGO,
                Set.of(TerritoryType.EAST_AFRICA, TerritoryType.SOUTH_AFRICA));
    }

    private void addEastAfricaEdges() {
        territoryGraph.addSetOfAdjacencies(TerritoryType.EAST_AFRICA,
                Set.of(TerritoryType.SOUTH_AFRICA, TerritoryType.MADAGASCAR, TerritoryType.MIDDLE_EAST));
    }

    private void addSouthAfricaEdges() {
        territoryGraph.addSetOfAdjacencies(TerritoryType.SOUTH_AFRICA, Set.of(TerritoryType.MADAGASCAR));
    }

    private void addEuropeanGraphEdges() {
        addGreatBritainEdges();
        addIcelandEdges();
        addScandinaviaEdges();
        addNorthernEuropeEdges();
        addSouthernEuropeEdges();
        addUkraineEdges();
    }

    private void addGreatBritainEdges() {
        territoryGraph.addSetOfAdjacencies(TerritoryType.GREAT_BRITAIN,
                Set.of(TerritoryType.ICELAND, TerritoryType.SCANDINAVIA, TerritoryType.NORTHERN_EUROPE,
                        TerritoryType.WESTERN_EUROPE));
    }

    private void addIcelandEdges() {
        territoryGraph.addSetOfAdjacencies(TerritoryType.ICELAND, Set.of(TerritoryType.SCANDINAVIA));
    }

    private void addScandinaviaEdges() {
        territoryGraph.addSetOfAdjacencies(TerritoryType.SCANDINAVIA,
                Set.of(TerritoryType.NORTHERN_EUROPE, TerritoryType.UKRAINE));
    }

    private void addNorthernEuropeEdges() {
        territoryGraph.addSetOfAdjacencies(TerritoryType.NORTHERN_EUROPE,
                Set.of(TerritoryType.UKRAINE, TerritoryType.SOUTHERN_EUROPE, TerritoryType.WESTERN_EUROPE));
    }

    private void addSouthernEuropeEdges() {
        territoryGraph.addSetOfAdjacencies(TerritoryType.SOUTHERN_EUROPE,
                Set.of(TerritoryType.MIDDLE_EAST, TerritoryType.UKRAINE, TerritoryType.WESTERN_EUROPE));
    }

    private void addUkraineEdges() {
        territoryGraph.addSetOfAdjacencies(TerritoryType.UKRAINE,
                Set.of(TerritoryType.MIDDLE_EAST, TerritoryType.URAL, TerritoryType.AFGHANISTAN));
    }

    private void addAsianGraphEdges() {
        addAfghanistanEdges();
        addMiddleEastEdges();
        addUralEdges();
        addIndiaEdges();
        addChinaEdges();
        addSiberiaEdges();
        addRemainingAsianGraphEdges();
    }

    private void addRemainingAsianGraphEdges() {
        addSiamEdges();
        addMongoliaEdges();
        addIrkutskEdges();
        addYakutskEdges();
        addJapanEdges();
    }

    private void addAfghanistanEdges() {
        territoryGraph.addSetOfAdjacencies(TerritoryType.AFGHANISTAN,
                Set.of(TerritoryType.MIDDLE_EAST, TerritoryType.INDIA, TerritoryType.CHINA, TerritoryType.URAL));
    }

    private void addMiddleEastEdges() {
        territoryGraph.addSetOfAdjacencies(TerritoryType.MIDDLE_EAST, Set.of(TerritoryType.INDIA));
    }

    private void addUralEdges() {
        territoryGraph.addSetOfAdjacencies(TerritoryType.URAL, Set.of(TerritoryType.CHINA, TerritoryType.SIBERIA));
    }

    private void addIndiaEdges() {
        territoryGraph.addSetOfAdjacencies(TerritoryType.INDIA, Set.of(TerritoryType.CHINA, TerritoryType.SIAM));
    }

    private void addChinaEdges() {
        territoryGraph.addSetOfAdjacencies(TerritoryType.CHINA,
                Set.of(TerritoryType.SIAM, TerritoryType.SIBERIA, TerritoryType.MONGOLIA));
    }

    private void addSiberiaEdges() {
        territoryGraph.addSetOfAdjacencies(TerritoryType.SIBERIA,
                Set.of(TerritoryType.YAKUTSK, TerritoryType.IRKUTSK, TerritoryType.MONGOLIA));
    }

    private void addSiamEdges() {
        territoryGraph.addSetOfAdjacencies(TerritoryType.SIAM, Set.of(TerritoryType.INDONESIA));
    }

    private void addMongoliaEdges() {
        territoryGraph.addSetOfAdjacencies(TerritoryType.MONGOLIA,
                Set.of(TerritoryType.IRKUTSK, TerritoryType.KAMCHATKA, TerritoryType.JAPAN));
    }

    private void addIrkutskEdges() {
        territoryGraph.addSetOfAdjacencies(TerritoryType.IRKUTSK,
                Set.of(TerritoryType.YAKUTSK, TerritoryType.KAMCHATKA));
    }

    private void addYakutskEdges() {
        territoryGraph.addSetOfAdjacencies(TerritoryType.YAKUTSK, Set.of(TerritoryType.KAMCHATKA));
    }

    private void addJapanEdges() {
        territoryGraph.addSetOfAdjacencies(TerritoryType.JAPAN, Set.of(TerritoryType.KAMCHATKA));
    }

    private void addOceanicGraphEdges() {
        addIndonesiaEdges();
        addNewGuineaEdges();
        addWesternAustraliaEdges();
    }

    private void addIndonesiaEdges() {
        territoryGraph.addSetOfAdjacencies(TerritoryType.INDONESIA,
                Set.of(TerritoryType.NEW_GUINEA, TerritoryType.WESTERN_AUSTRALIA));
    }

    private void addNewGuineaEdges() {
        territoryGraph.addSetOfAdjacencies(TerritoryType.NEW_GUINEA,
                Set.of(TerritoryType.EASTERN_AUSTRALIA, TerritoryType.WESTERN_AUSTRALIA));
    }

    private void addWesternAustraliaEdges() {
        territoryGraph.addSetOfAdjacencies(TerritoryType.WESTERN_AUSTRALIA, Set.of(TerritoryType.EASTERN_AUSTRALIA));
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
        Territory territoryObject = territoryGraph.getTerritory(relevantTerritory);
        return territoryObject.isOwnedByPlayer(playerColor);
    }

    public boolean placeNewArmiesInTerritory(TerritoryType relevantTerritory, int numArmiesToPlace) {
        checkIfGameIsInValidPhaseForAction(Set.of(GamePhase.SCRAMBLE, GamePhase.SETUP, GamePhase.PLACEMENT),
                "Valid phases to call placeNewArmiesInTerritory from are: SCRAMBLE, SETUP, PLACEMENT");
        handleValidPhaseParsing(relevantTerritory, numArmiesToPlace);

        return true;
    }

    private void checkIfGameIsInValidPhaseForAction(Set<GamePhase> validPhases, String errorMessage) {
        if (!validPhases.contains(currentGamePhase)) {
            throw new IllegalStateException(errorMessage);
        }
    }

    private void handleValidPhaseParsing(TerritoryType relevantTerritory, int numArmiesToPlace) {
        if (currentGamePhase == GamePhase.SETUP) {
            handleSetupPhaseArmyPlacement(relevantTerritory, numArmiesToPlace);
        } else if (currentGamePhase == GamePhase.SCRAMBLE) {
            handleScramblePhaseArmyPlacement(relevantTerritory, numArmiesToPlace);
        } else {
            handlePlacementPhaseArmyPlacement(relevantTerritory, numArmiesToPlace);
        }
    }

    private void handleSetupPhaseArmyPlacement(TerritoryType relevantTerritory, int numArmiesToPlace) {
        checkNumArmiesToPlaceIsValidForSetup(numArmiesToPlace);
        checkIfCurrentPlayerOwnsTerritory(relevantTerritory);
        checkIfPlayerHasEnoughArmiesToPlace(numArmiesToPlace);
        increaseNumArmiesInTerritory(relevantTerritory, numArmiesToPlace);
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

    private void checkIfPlayerHasEnoughArmiesToPlace(int numNewArmiesToPlace) {
        int numArmiesPlayerHasToPlace = playersMap.get(currentPlayer).getNumArmiesToPlace();
        if (numArmiesPlayerHasToPlace - numNewArmiesToPlace <= -1) {
            throw new IllegalArgumentException("Player does not have enough armies to place!");
        }
    }

    private void increaseNumArmiesInTerritory(TerritoryType relevantTerritory, int additionalArmies) {
        Territory territoryObject = territoryGraph.getTerritory(relevantTerritory);
        int previousNumArmies = territoryObject.getNumArmiesPresent();
        territoryObject.setNumArmiesPresent(previousNumArmies + additionalArmies);
    }

    private void decreaseNumArmiesCurrentPlayerHasToPlace(int numToDecreaseBy) {
        int currentNumArmies = playersMap.get(currentPlayer).getNumArmiesToPlace();
        int newNumArmies = currentNumArmies - numToDecreaseBy;
        playersMap.get(currentPlayer).setNumArmiesToPlace(newNumArmies);
    }

    private void updateCurrentPlayer() {
        int currentPlayerIndex = playersList.indexOf(currentPlayer);
        currentPlayer = playersList.get((currentPlayerIndex + 1) % playersList.size());
    }

    private void checkSetupPhaseEndCondition() {
        if (totalUnplacedArmiesLeft == 0) {
            currentGamePhase = GamePhase.PLACEMENT;
            currentPlayer = playersList.get(0);
            int numArmiesToAssign = calculatePlacementPhaseArmiesForCurrentPlayer();
            increaseNumArmiesCurrentPlayerHasToPlace(numArmiesToAssign);
        }
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

    private void increaseNumArmiesCurrentPlayerHasToPlace(int numToIncreaseBy) {
        // little shortcut: we can multiply numToIncreaseBy by -1 and call the decrease method.
        decreaseNumArmiesCurrentPlayerHasToPlace(-1 * numToIncreaseBy);
    }

    private void handleScramblePhaseArmyPlacement(TerritoryType relevantTerritory, int numArmiesToPlace) {
        checkIfTerritoryIsUnclaimed(relevantTerritory);
        checkIfNumArmiesToPlaceIsValidForScramblePhase(numArmiesToPlace);
        checkIfPlayerHasEnoughArmiesToPlace(numArmiesToPlace);
        updateTerritoryObjectWithValidScrambleArguments(relevantTerritory, numArmiesToPlace);
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

    private void updateTerritoryObjectWithValidScrambleArguments(
            TerritoryType relevantTerritory, int numArmiesToPlace) {
        Territory territoryObject = territoryGraph.getTerritory(relevantTerritory);
        territoryObject.setNumArmiesPresent(numArmiesToPlace);
        territoryObject.setPlayerInControl(currentPlayer);
        numUnclaimedTerritories--;
    }

    private void addTerritoryToCurrentPlayerCollection(TerritoryType relevantTerritory) {
        Player playerObject = playersMap.get(currentPlayer);
        playerObject.addTerritoryToCollection(relevantTerritory);
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

    private void handlePlacementPhaseArmyPlacement(TerritoryType relevantTerritory, int numArmiesToPlace) {
        checkIfPlayerIsHoldingTooManyCards("Player cannot place armies while they are holding more than 5 cards!");
        checkIfNumArmiesToPlaceIsValidForPlacement(numArmiesToPlace);
        checkIfCurrentPlayerOwnsTerritory(relevantTerritory);
        checkIfPlayerHasEnoughArmiesToPlace(numArmiesToPlace);
        decreaseNumArmiesCurrentPlayerHasToPlace(numArmiesToPlace);
        increaseNumArmiesInTerritory(relevantTerritory, numArmiesToPlace);
        checkPlacementPhaseEndCondition();
    }

    private void checkIfPlayerIsHoldingTooManyCards(String errorMessage) {
        if (playersMap.get(currentPlayer).getNumCardsHeld() >= FORCED_CARD_TURN_IN_THRESHOLD) {
            throw new IllegalStateException(errorMessage);
        }
    }

    private void checkIfNumArmiesToPlaceIsValidForPlacement(int numArmiesToPlace) {
        if (numArmiesToPlace < 1) {
            throw new IllegalArgumentException("Cannot place < 1 army on a territory during the Placement phase");
        }
    }

    private void checkPlacementPhaseEndCondition() {
        if (playersMap.get(currentPlayer).getNumArmiesToPlace() == 0) {
            currentGamePhase = GamePhase.ATTACK;
        }
    }

    public Set<TerritoryType> tradeInCards(Set<Card> selectedCardsToTradeIn) {
        checkIfGameIsInValidPhaseForAction(Set.of(GamePhase.PLACEMENT, GamePhase.ATTACK),
                "Can only trade in cards during the PLACEMENT or ATTACK phase");
        checkForForcedTradeInForAttackPhase();

        Player currentPlayerObject = playersMap.get(currentPlayer);
        checkIfPlayerOwnsCards(selectedCardsToTradeIn, currentPlayerObject);
        return addArmiesToPlayerStockpileIfValidSet(selectedCardsToTradeIn, currentPlayerObject);
    }

    private void checkForForcedTradeInForAttackPhase() {
        if (playersMap.get(currentPlayer).getNumCardsHeld() < ATTACK_PHASE_FORCED_CARD_TURN_IN_THRESHOLD
                && currentGamePhase == GamePhase.ATTACK) {
            throw new IllegalStateException("Cannot trade in cards in the ATTACK phase unless you have > 5 held!");
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
        clearRecentlyAttackedTerritories();
    }

    public void moveArmiesBetweenFriendlyTerritories(
            TerritoryType sourceTerritory, TerritoryType destTerritory, int numArmies) {
        checkIfTerritoriesAreAdjacent(sourceTerritory, destTerritory);
        checkIfPlayerOwnsBothSourceAndDestinationTerritories(sourceTerritory, destTerritory);
        handleInputArmyValidation(sourceTerritory, numArmies);
        checkIfInValidGamePhaseForMovement();
        checkIfTerritoriesWereRecentlyAttacked(sourceTerritory, destTerritory);
        decreaseNumArmiesInTerritory(sourceTerritory, numArmies);
        increaseNumArmiesInTerritory(destTerritory, numArmies);
        handleFortifyPhaseEndingIfNecessary();
    }

    private void checkIfPlayerOwnsBothSourceAndDestinationTerritories(TerritoryType source, TerritoryType dest) {
        if (!checkIfPlayerOwnsTerritory(source, currentPlayer)) {
            throw new IllegalArgumentException("Provided territories are not owned by the current player!");
        }
        if (!checkIfPlayerOwnsTerritory(dest, currentPlayer)) {
            throw new IllegalArgumentException("Provided territories are not owned by the current player!");
        }
    }

    private void handleInputArmyValidation(TerritoryType sourceTerritory, int numArmiesToMove) {
        if (numArmiesToMove < 0) {
            throw new IllegalArgumentException("Cannot move a negative number of armies between territories");
        }
        int numArmiesInSource = getNumberOfArmies(sourceTerritory);
        if (numArmiesToMove >= numArmiesInSource) {
            throw new IllegalArgumentException(
                    "Source territory does not have enough armies to support this movement!");
        }
    }

    private void checkIfInValidGamePhaseForMovement() {
        checkIfGameIsInValidPhaseForAction(Set.of(GamePhase.ATTACK, GamePhase.FORTIFY),
                "Friendly army movement can only be done in the ATTACK or FORTIFY phase!");
    }

    private void checkIfTerritoriesWereRecentlyAttacked(TerritoryType sourceTerritory, TerritoryType destTerritory) {
        if ((recentlyAttackedSource != sourceTerritory || recentlyAttackedDestination != destTerritory)
                && currentGamePhase == GamePhase.ATTACK) {
            throw new IllegalArgumentException("Cannot split armies between this source and destination!");
        }
        clearRecentlyAttackedTerritories();
    }

    private void handleFortifyPhaseEndingIfNecessary() {
        if (currentGamePhase == GamePhase.FORTIFY) {
            handleFortifyPhaseEnding();
        }
    }

    public void forceGamePhaseToEnd() {
        checkIfGameIsInValidPhaseForAction(Set.of(GamePhase.ATTACK, GamePhase.FORTIFY),
                "Cannot forcibly end this game phase!");
        if (currentGamePhase == GamePhase.ATTACK) {
            handleAttackPhaseEnding();
        } else {
            handleFortifyPhaseEnding();
        }
        clearRecentlyAttackedTerritories();
    }

    private void handleAttackPhaseEnding() {
        checkIfPlayerHasTooManyCardsAttackPhase();
        currentGamePhase = GamePhase.FORTIFY;
    }

    private void checkIfPlayerHasTooManyCardsAttackPhase() {
        if (playersMap.get(currentPlayer).getNumCardsHeld() > 5) {
            throw new IllegalStateException(
                    "Cannot forcibly end the ATTACK phase while the current player is holding > 5 cards!");
        }
    }

    private void handleFortifyPhaseEnding() {
        currentGamePhase = GamePhase.PLACEMENT;
        claimCardForCurrentPlayerIfPossible();
        updateCurrentPlayer();
        int bonusArmies = calculatePlacementPhaseArmiesForCurrentPlayer();
        increaseNumArmiesCurrentPlayerHasToPlace(bonusArmies);
    }

    void claimCardForCurrentPlayerIfPossible() {
        if (currentPlayerCanClaimCard) {
            addCardToCurrentPlayersCollection();
        }
        currentPlayerCanClaimCard = false;
    }

    private void addCardToCurrentPlayersCollection() {
        Player playerObject = playersMap.get(currentPlayer);
        Card drawnCard;
        try {
            drawnCard = cardDeck.drawCard();
        } catch (Exception exception) {
            return;
        }
        playerObject.addCardsToCollection(Set.of(drawnCard));
    }

    public int attackTerritory(
            TerritoryType sourceTerritory, TerritoryType destTerritory, int numAttackers, int numDefenders) {
        handleErrorCasesForAttackingTerritory(sourceTerritory, destTerritory, numAttackers, numDefenders);
        List<BattleResult> dieResults = rollDiceForBattle(numAttackers, numDefenders);
        if (handleArmyLosses(sourceTerritory, destTerritory, dieResults) == AttackConsequence.NO_CHANGE) {
            clearRecentlyAttackedTerritories();
            return 0;
        }
        handleDefenderLosingTerritoryConsequences(sourceTerritory, destTerritory, numAttackers);
        return getNumberOfArmies(sourceTerritory) - 1;
    }

    void handleErrorCasesForAttackingTerritory(
            TerritoryType sourceTerritory, TerritoryType destTerritory, int numAttackers, int numDefenders) {
        checkIfNumAttackersIsValid(numAttackers);
        checkIfNumDefendersIsValid(numDefenders);
        checkIfGameIsInAttackPhase();
        checkIfTerritoriesAreAdjacent(sourceTerritory, destTerritory);
        checkIfAppropriatePlayersOwnTerritories(sourceTerritory, destTerritory);
        checkIfPlayerIsHoldingTooManyCards("Player must trade in cards before they can attack!");
        checkIfSourceTerritoryHasEnoughArmiesToSupportAttack(sourceTerritory, numAttackers);
        checkIfDestTerritoryHasEnoughArmiesToSupportDefense(destTerritory, numDefenders);
    }

    private void checkIfNumAttackersIsValid(int numAttackers) {
        if (numAttackers < MINIMUM_NUMBER_OF_ATTACKING_ARMIES || numAttackers > MAXIMUM_NUMBER_OF_ATTACKING_ARMIES) {
            throw new IllegalArgumentException(String.format("Number of armies to attack with must be within [%d, %d]!",
                    MINIMUM_NUMBER_OF_ATTACKING_ARMIES, MAXIMUM_NUMBER_OF_ATTACKING_ARMIES));
        }
    }

    private void checkIfNumDefendersIsValid(int numDefenders) {
        if (numDefenders < MINIMUM_NUMBER_OF_DEFENDING_ARMIES || numDefenders > MAXIMUM_NUMBER_OF_DEFENDING_ARMIES) {
            throw new IllegalArgumentException(String.format("Number of armies to defend with must be within [%d, %d]!",
                    MINIMUM_NUMBER_OF_DEFENDING_ARMIES, MAXIMUM_NUMBER_OF_DEFENDING_ARMIES));
        }
    }

    private void checkIfGameIsInAttackPhase() {
        checkIfGameIsInValidPhaseForAction(Set.of(GamePhase.ATTACK),
                "Attacking territories is not allowed in any phase besides attack!");
    }

    private void checkIfTerritoriesAreAdjacent(TerritoryType source, TerritoryType dest) {
        if (!territoryGraph.areTerritoriesAdjacent(source, dest)) {
            throw new IllegalArgumentException("Source and destination territory must be two adjacent territories!");
        }
    }

    private void checkIfAppropriatePlayersOwnTerritories(TerritoryType source, TerritoryType dest) {
        if (!checkIfPlayerOwnsTerritory(source, currentPlayer)) {
            throw new IllegalArgumentException("Source territory is not owned by the current player!");
        }
        if (checkIfPlayerOwnsTerritory(dest, currentPlayer)) {
            throw new IllegalArgumentException("Destination territory is owned by the current player!");
        }
    }

    private void checkIfSourceTerritoryHasEnoughArmiesToSupportAttack(TerritoryType sourceTerritory, int numAttackers) {
        int numArmiesPresent = territoryGraph.getTerritory(sourceTerritory).getNumArmiesPresent();
        if (numAttackers > numArmiesPresent - 1) {
            throw new IllegalArgumentException("Source territory has too few armies to use in this attack!");
        }
    }

    private void checkIfDestTerritoryHasEnoughArmiesToSupportDefense(TerritoryType destTerritory, int numDefenders) {
        int numArmiesPresent = territoryGraph.getTerritory(destTerritory).getNumArmiesPresent();
        if (numDefenders > numArmiesPresent) {
            throw new IllegalArgumentException("Destination territory has too few defenders for this defense!");
        }
    }

    List<BattleResult> rollDiceForBattle(int numAttackers, int numDefenders) {
        this.attackerRolls = dieRollParser.rollAttackerDice(numAttackers);
        this.defenderRolls = dieRollParser.rollDefenderDice(numDefenders);
        this.battleResults = dieRollParser.generateBattleResults(this.defenderRolls, this.attackerRolls);
        return this.battleResults;
    }

    AttackConsequence handleArmyLosses(
            TerritoryType sourceTerritory, TerritoryType destinationTerritory, List<BattleResult> battleResults) {
        int numAttackersLost = Collections.frequency(battleResults, BattleResult.DEFENDER_VICTORY);
        int numDefendersLost = Collections.frequency(battleResults, BattleResult.ATTACKER_VICTORY);
        decreaseNumArmiesInTerritory(sourceTerritory, numAttackersLost);
        decreaseNumArmiesInTerritory(destinationTerritory, numDefendersLost);

        return getNumberOfArmies(destinationTerritory) == 0
                ? AttackConsequence.DEFENDER_LOSES_TERRITORY : AttackConsequence.NO_CHANGE;
    }

    private void decreaseNumArmiesInTerritory(TerritoryType territory, int numArmiesLost) {
        increaseNumArmiesInTerritory(territory, -1 * numArmiesLost);
    }

    public int getNumberOfArmies(TerritoryType territoryType) {
        return territoryGraph.getTerritory(territoryType).getNumArmiesPresent();
    }

    private void clearRecentlyAttackedTerritories() {
        this.recentlyAttackedDestination = null;
        this.recentlyAttackedSource = null;
    }

    PlayerColor handleAttackerTakingTerritory(TerritoryType territory, int numAttackers) {
        PlayerColor playerLosingTerritory = getPlayerInControlOfTerritory(territory);
        territoryGraph.getTerritory(territory).setNumArmiesPresent(numAttackers);
        territoryGraph.getTerritory(territory).setPlayerInControl(currentPlayer);
        playersMap.get(currentPlayer).addTerritoryToCollection(territory);
        playersMap.get(playerLosingTerritory).removeTerritoryFromCollection(territory);
        return playerLosingTerritory;
    }

    private PlayerColor getPlayerInControlOfTerritory(TerritoryType territory) {
        return playersList.stream().filter(player -> checkIfPlayerOwnsTerritory(territory, player))
                .collect(Collectors.toList()).get(0);
    }

    public void handlePlayerLosingGameIfNecessary(PlayerColor potentiallyLosingPlayer) {
        if (!playerOwnsAtLeastOneTerritory(potentiallyLosingPlayer)) {
            Set<Card> losingPlayerCards = playersMap.get(potentiallyLosingPlayer).getOwnedCards();
            playersMap.get(currentPlayer).addCardsToCollection(losingPlayerCards);
            playersList.remove(potentiallyLosingPlayer);
            playersMap.remove(potentiallyLosingPlayer);
        }
    }

    private boolean playerOwnsAtLeastOneTerritory(PlayerColor player) {
        for (TerritoryType territory : TerritoryType.values()) {
            if (checkIfPlayerOwnsTerritory(territory, player)) {
                return true;
            }
        }
        return false;
    }

    void handleCurrentPlayerWinningGameIfNecessary() {
        for (TerritoryType territory : TerritoryType.values()) {
            if (!checkIfPlayerOwnsTerritory(territory, currentPlayer)) {
                return;
            }
        }
        this.currentGamePhase = GamePhase.GAME_OVER;
    }

    private void handleDefenderLosingTerritoryConsequences(
            TerritoryType sourceTerritory, TerritoryType destTerritory, int numAttackers) {
        PlayerColor potentiallyLosingPlayer = handleAttackerTakingTerritory(destTerritory, numAttackers);
        decreaseNumArmiesInTerritory(sourceTerritory, numAttackers);
        handlePlayerLosingGameIfNecessary(potentiallyLosingPlayer);
        handleCurrentPlayerWinningGameIfNecessary();
        currentPlayerCanClaimCard = true;
    }

    public List<Integer> getAttackerDiceRolls() {
        return new ArrayList<>(this.attackerRolls);
    }

    public List<Integer> getDefenderDiceRolls() {
        return new ArrayList<>(this.defenderRolls);
    }

    public List<BattleResult> getBattleResults() {
        return new ArrayList<>(this.battleResults);
    }

    public boolean getIfCurrentPlayerCanClaimCard() {
        return currentPlayerCanClaimCard;
    }

    public TerritoryType getRecentlyAttackedSource() {
        return recentlyAttackedSource;
    }

    public TerritoryType getRecentlyAttackedDest() {
        return recentlyAttackedDestination;
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

    Map<PlayerColor, Player> getPlayerMap() {
        return playersMap;
    }

    void provideMockedPlayersMap(Map<PlayerColor, Player> mockedPlayersMap) {
        this.playersMap = mockedPlayersMap;
    }

    void setNumArmiesForPlayer(PlayerColor playerColor, int numArmies) {
        playersMap.get(playerColor).setNumArmiesToPlace(numArmies);
    }

    Set<Card> getCardsForPlayer(PlayerColor playerColor) {
        return playersMap.get(playerColor).getOwnedCards();
    }

    void setAbilityToClaimCard() {
        this.currentPlayerCanClaimCard = true;
    }

    void provideMockedCardDeck(RiskCardDeck mockedDeck) {
        this.cardDeck = mockedDeck;
    }

    void provideDieRollParser(DieRollParser dieRollParser) {
        this.dieRollParser = dieRollParser;
    }

    void setRecentlyAttackedSource(TerritoryType recentlyAttackedSrc) {
        this.recentlyAttackedSource = recentlyAttackedSrc;
    }

    void setRecentlyAttackedDest(TerritoryType recentlyAttackedDest) {
        this.recentlyAttackedDestination = recentlyAttackedDest;
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

    void provideCurrentPlayerForTurn(PlayerColor currentlyGoingPlayer) {
        currentPlayer = currentlyGoingPlayer;
        if (!playersList.contains(currentPlayer)) { // make sure we don't add them twice.
            playersList.add(currentPlayer);
        }
    }

    void setGamePhase(GamePhase gamePhase) {
        currentGamePhase = gamePhase;
    }

    void setPlayerOrderList(List<PlayerColor> playersList) {
        this.playersList = new ArrayList<>(playersList);
        if (!playersList.isEmpty()) {
            this.currentPlayer = playersList.get(0);
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

    Set<TerritoryType> getClaimedTerritoriesForPlayer(PlayerColor playerInQuestion) {
        return playersMap.get(playerInQuestion).getTerritories();
    }

    WorldDominationGameEngine() {
        territoryGraph = initializeGraph();
        currentGamePhase = GamePhase.SCRAMBLE;
        numUnclaimedTerritories = INITIAL_NUM_UNCLAIMED_TERRITORIES;
    }
}
