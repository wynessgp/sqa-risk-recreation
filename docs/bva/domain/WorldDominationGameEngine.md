# method: `initializePlayersList(playerOrder: List<PlayerColor>): boolean`

## BVA Step 1
Input: The respective turn order of players via their assigned PlayerColor

Output: A yes/no answer whether we could successfully initialize the game's relevant player storage
given the respective order

## BVA Step 2
Input:
- playerOrder: Collection

Output: 
- Boolean (actual return statement)
- Collection (underlying player storage)

## BVA Step 3
Input: 
- playerOrder: Collection
  - An empty collection (error case)
  - A collection with \< 3 elements (error case)
  - A collection with [3, 6] elements 
  - A collection with 6 elements (max size)
  - A collection with \> 6 elements (error case)
  - A collection with the same PlayerColor twice (duplicates - error case)
  - A collection containing PlayerColor SETUP (error case)
  - A collection with all unique PlayerColors

Output:
- Actual return statement (Boolean)
  - 0 (can't set)
  - 1
  - Some value other than true or false
    - Exception:
      - The size of the list is not within [3, 6]
      - The input list is malformed (duplicates, contains SETUP)
  - Some other true value (can't set)
- Underlying player storage (Collection)
  - Must be the same size as playerOrder
  - Must respect the arrangement of playerOrder

## BVA Step 4
For the non-error test cases, I will indicate the underlying player storage via their assigned PlayerColor

So the output would be something like: Collection = [RED, YELLOW, PURPLE], etc.

### Test 1:
- Input: 
  - playerOrder = []
- Output: IllegalArgumentException
  - message: "playerOrder's size is not within: [3, 6]"
### Test 2:
- Input:
  - playerOrder = [YELLOW]
- Output: IllegalArgumentException
  - message: "playerOrder's size is not within: [3, 6]"
### Test 3:
- Input
  - playerOrder = [BLUE, RED]
- Output: IllegalArgumentException
  - message: "playerOrder's size is not within: [3, 6]"
### Test 4:
- Input:
  - playerOrder = [BLUE, BLUE, SETUP, PURPLE, YELLOW, YELLOW, BLACK]
- Output: IllegalArgumentException
  - message: "playerOrder's size is not within: [3, 6]"
### Test 5:
- Input:
  - playerOrder = [BLUE, BLUE, BLUE, BLUE, RED, RED, RED, RED, BLACK, BLACK, BLACK, BLACK, ...]
- Output: IllegalArgumentException
  - message: "playerOrder's size is not within: [3, 6]"
### Test 6:
- Input:
  - playerOrder = [YELLOW, YELLOW, SETUP]
- Output: IllegalArgumentException
  - message: "Player order contains duplicate entries"
### Test 7:
- Input:
  - playerOrder = [RED, YELLOW, BLUE, RED]
- Output: IllegalArgumentException
  - message: "Player order contains duplicate entries"
### Test 8: 
- Input:
  - playerOrder = [RED, BLUE, SETUP]
- Output: IllegalArgumentException
  - message: "Player order contains SETUP as one of the players"
### Test 9:
- Input:
  - playerOrder = [PURPLE, BLUE, BLACK, YELLOW, SETUP, RED]
- Output: IllegalArgumentException
  - message: "Player order contains SETUP as one of the players"
### Test 10:
- Input:
  - playerOrder = [RED, BLUE, PURPLE, BLACK]
- Output: 
  - Function output: 1 (true)
  - Collection = [RED, BLUE, PURPLE, BLACK]
### Test 11:
- Input:
  - playerOrder = [BLUE, BLACK, RED]
- Output: 
  - Function output: 1 (true)
  - Collection = [BLUE, BLACK, RED]

# method: `assignSetupArmiesToPlayers(): boolean`
Note: this method does field modifications. It will MODIFY the players in our players list to give them their appropriate
number of troops each for the setup phase, so if it does not yet exist, or the number of players is invalid, this will
throw an exception. Though generally, these exceptions will be handled by calling initializePlayersList.

## BVA Step 1
Input: The underlying collection representing the players (field)

Output: A yes/no answer whether we were able to successfully modify each Player object to their 
setup-determined army counts.

## BVA Step 2
Input: 
- Underlying PlayerColor storage: Collection

Output: 
- function output: Boolean
- Player object storage: Collection

## BVA Step 3
Input: 
- PlayerColor objects (Collection):
  - An empty collection (error case)
  - A collection with [3, 6] elements -> ensure we give players the correct number of armies.

Note the lack of other error cases above - we expect whoever is calling us to have made the player collection, otherwise
the other methods haven't done their job.

Output: 
- function output: (Boolean)
  - 0 (can't set)
  - 1
  - Something other than true/false (Exceptions)
    - An exception will only be thrown if the PlayerColor storage is empty (meaning initializer didn't get called)
  - Some true value other than 1 (can't set)
- Players collection:
  - Each element in the list should have their armies increased by the SETUP amount for the number of players
  - Not really interested in any other cases, as we are not modifying the Collection itself, merely the items inside.

## BVA Step 4

I will be modeling the input by the associated player color.

Output will be modeled by a collection of numbers; those indicate the NEW values that each player should
have for their number of armies. These should be verified as well.

### Test 1:
- Input:
  - Player objects = []
- Output: IllegalStateException
  - message: "No player objects exist, call initializePlayersList first with the correct arguments"
### Test 2:
- Input: 
  - Player objects = [Red, Yellow, Purple]
- Output:
  - 1 (true) 
  - Collection = [35, 35, 35]
### Test 3:
- Input:
  - Player objects = [Red, Yellow, Purple, Green]
- Output:
  - 1 (true)
  - Collection = [30, 30, 30, 30]
### Test 4:
- Input:
  - Player objects = [Red, Yellow, Purple, Green, Black]
- Output:
  - 1 (true)
  - Collection = [25, 25, 25, 25, 25]
### Test 5:
- Input:
  - Player objects = [Red, Yellow, Purple, Green, Black, Blue]
- Output:
  - 1 (true)
  - Collection = [20, 20, 20, 20, 20, 20]

# method: `checkIfPlayerOwnsTerritory(territory: TerritoryType, player: PlayerColor): boolean`
Note: communication is done STRICTLY through the graph to check for ownership.
Our player's territories owned are merely for our TradeInParser to "plug" in to.

## BVA Step 1
Input: A territory that's in question whether the given player owns it

Output: A yes/no answer if the given player owns the territory

## BVA Step 2
Input:
- territory: Cases
- player: Cases
- Underlying territory object: Pointer
  - Note that the TerritoryType provided should always align with the Territory object we grab
  - We communicate through the graph for this, so there shouldn't be mix-ups.

Output: Boolean

## BVA Step 3
Input:
- Territory (Cases):
  - The 1st possibility (ALASKA)
  - The 2nd possibility (ARGENTINA)
  - ...
  - The 42nd possibility (YAKUTSK)
  - The 0th or 43rd possibility (can't set)
- Player (Cases):
  - The 1st possibility (SETUP)
  - The 2nd possibility (BLUE)
  - ...
  - The 7th possibility (PURPLE)
  - The 0th or 8th possibility (can't set)
- Underlying territory object:
  - Null pointer (can't set, Martin's rules)
  - A pointer to the actual object
    - If the PlayerColor in the Territory object does not match, return false. 

Output:
- 0 
- 1
- Something neither true nor false (can't set)
- Some true value other than 1 (can't set)

## BVA Step 4
### Test 1:
Input:
- territory = ALASKA
- player = BLUE
- Territory object = [type = ALASKA, player = PURPLE]

Output = 0 (false)

### Test 2:
Input:
- territory = YAKUTSK
- player = RED
- Territory object = [type = ALASKA, player = BLUE]

Output: 0 (false)
### Test 3:
Input:
- territory = ALASKA
- player = BLUE
- Territory object = [type = ALASKA, player = BLUE]

Output: 1 (true)
### More tests are included, but not explicitly enumerated here. We aim to pursue all combos.

# method: `placeNewArmiesInTerritory(territory: TerritoryType, numArmies: int): boolean`
Note: this method will seem like it has an incredible amount of responsibility based on the test cases below.
It will be split up into separate helper functions, so much of the enumerated tests here will have their logic in code
handled elsewhere, and split by phase.

These will also largely be integration tests, especially on the methods that do not throw an Exception. They will
be marked as such.

## BVA Step 1
Input: The respective territory the current user wants to place their armies in, as well as the
number of new armies to place there. 

Additionally, we need to know: 
- if the current Player can place this many armies
- if the amount of armies to place is valid in our current game phase
- if the user owns the territory. (Check done via the graph)

Output: A yes/no answer whether the armies were placed successfully. The "no" 
case will not occur - instead, an exception will be thrown if the armies were 
not able to be placed successfully (i.e. another player owns the territory). 

Additionally, we care about:
- if the territory was updated correctly
  - this means claiming in the scramble phase, or just ensuring the army count was updated.
- decrementing the number of armies left to place for our player
- updating who's turn it is, if necessary.
  - This action should occur automatically for the phases:
    - SCRAMBLE, SETUP
- updating the game phase, if necessary.
  - from SCRAMBLE -> SETUP if all territories are claimed (integration test)
  - from SETUP -> PLACEMENT if all armies have been placed (integration test)
  - from PLACEMENT -> ATTACK if the player places all of their earned armies

## BVA Step 2
Input:
- territory: Cases
- numNewArmies: Counts [1, armies earned on player's turn]
- State of who owns the territory: Cases
- Game Phase: Cases 
- Current player object: Pointer

Output: 
- function return value: Boolean
- Territory: Pointer
- Current player object: Pointer
- Game Phase: Cases

## BVA Step 3
Input: 
- territory (Cases):
  - The 1st possibility (ALASKA)
  - The 2nd possibility (ARGENTINA)
  - ...
  - The 42nd possibility (YAKUTSK)
  - The 0th, 43rd possibility (can't set)
- numArmies (Counts):
  - -1 (error case)
  - 0 (error case)
  - 1
  - \> 1
  - Maximum possible value (number of armies the player earns on that turn)
  - One larger than the maximum possible value (error case)
- Player who owns the territory (Cases):
  - The 1st possibility (SETUP - should ONLY happen in SCRAMBLE)
  - ...
  - The 7th possibility (PURPLE)
  - The 0th, 8th possibilities (can't set)
- Game Phase (Cases):
  - The 1st possibility (SCRAMBLE -> restrict army placement to 1)
  - The 2nd possibility (SETUP -> restrict army placement to 1)
  - The 3rd possibility (PLACEMENT -> any amount of armies)
  - The 4th, 5th possibility (ATTACK, FORTIFY -> error case)
  - The 6th possibility (GAME_OVER -> error case)
  - The 0th, 7th possibility (can't set)
- Player object (Pointer):
  - Null pointer (can't set, per Martin's suggestions)
  - A valid pointer
    - We're mostly concerned about checking the number of armies they currently have
    - If they try to place too many and drop below 0, we should throw an error.

Output: 
- function return value: Boolean
  - 0 (can't set)
  - 1 (when the amount to place is valid, and the operation succeeds)
  - Some value other than 0 or 1 (exceptions)
    - If the game phase is SCRAMBLE or SETUP, our players can't place more than 1 army at a time.
  - Some true value other than 0 or 1 (can't set)
- Territory object: Pointer
  - Null pointer (can't set, per Martin's suggestions)
  - A pointer to the true object
    - Mostly concerned about checking two things in our Territory object:
      - The PlayerColor should be updated if we're in the SCRAMBLE phase
      - The army count should be updated in appropriate phases:
        - SCRAMBLE
        - SETUP
        - PLACEMENT
- Player object: Pointer
  - The number of armies they have to place should be updated
  - Territories held should be updated if in SCRAMBLE (maybe attack too).
- Game Phase: Cases
  - SCRAMBLE (advancing out requires all territories to be claimed)
  - SETUP (advancing out requires all setup armies to be placed)
  - PLACEMENT (advancing requires all earned armies to be placed)
  - ATTACK (can advance INTO, but cannot call this method from here)

## BVA Step 4

### SCRAMBLE PHASE

### Test 1:
- Input:
  - territory = CONGO
  - numArmies = 3
  - Territory owner = current player
  - Game Phase = SCRAMBLE
  - Current player = [Color = ANY, numArmiesToPlace is valid amount, territories owned = {CONGO} ]
- Output:
  - IllegalStateException
    - message: "Cannot place armies in a claimed territory until the scramble phase is over"
### Test 2:
- Input:
  - territory = CONGO
  - numArmies = 1
  - Territory owner = NOT current player (BLUE)
  - Game Phase = SCRAMBLE
  - Current player = [Color = RED, numArmiesToPlace is \> 1, territories owned = {}]
- Output:
  - IllegalStateException
    - message: "Cannot place armies in a claimed territory until the scramble phase is over"
### Test 3:
- Input:
  - territory = YAKUTSK
  - numArmies = 5
  - Territory owner = SETUP
  - Game Phase = SCRAMBLE
  - Current player = [Color = BLUE, numArmiesToPlace is \> 1, territories owned = {}]
- Output:
  - IllegalArgumentException
    - message: "You can only place 1 army on an unclaimed territory until the scramble phase is over"
### Test 4:
- Input:
  - territory = JAPAN
  - numArmies = -1
  - Territory owner = SETUP
  - Game Phase = SCRAMBLE
  - Current player = [Color = BLUE, numArmiesToPlace is \> 1, territories owned = {}]
- Output:
  - IllegalArgumentException
    - message: "You can only place 1 army on an unclaimed territory until the scramble phase is over"
### Test 5:
- Input:
  - territory = CHINA
  - numArmies = 1
  - Territory owner = SETUP
  - Game Phase = SCRAMBLE
  - Current player = [Color = BLUE, numArmiesToPlace = 0, territories owned = {}]
- Output: 
  - IllegalArgumentException
    - message: "Player does not have enough armies to place!"
    - They would end up with -1 armies if we allowed the operation to proceed.
### Test 6:
- Input:
  - territory = BRAZIL
  - numArmies = 1
  - Territory owner = SETUP
  - Game Phase = SCRAMBLE
  - Current player = [Color = BLUE, numArmiesToPlace = 10, territories owned = {}]
- Output: 
  - Function output: 1 (true)
  - Territory pointer: [numArmies = 1, PlayerColor = BLUE]
  - Current player = [Color = BLUE, numArmiesToPlace = 9, territories owned = {BRAZIL}]
  - Update to say we're on the next player's turn
  - Game Phase = SCRAMBLE
### Test 7:
- Input:
  - territory = EASTERN_AMERICA
  - numArmies = 1
  - Territory owner = SETUP
  - Game Phase = SCRAMBLE
  - Current player = [Color = GREEN, numArmiesToPlace = 1, territories owned = {}]
- Output: 
  - Function output: 1 (true)
  - Territory pointer: [numArmies = 1, PlayerColor = GREEN]
  - Current player = [Color = GREEN, numArmiesToPlace = 0, territories owned = {EASTERN_AMERICA}]
    - Players should have more territories than this in a scramble phase; we'll let it slip for a test.
  - Update to say we're on the next player's turn
  - Game Phase = SCRAMBLE

### SETUP PHASE

### Test 8:
- Input:
  - territory = ALASKA
  - numArmies = -1
  - Territory owner = current player
  - Game Phase = SETUP
  - Current player = [Color = BLUE, numArmiesToPlace = 12, territories owned = {ALASKA}]
- Output:
  - IllegalArgumentException
    - message: "Cannot place anything other than 1 army in a territory during setup phase"
### Test 9:
- Input:
  - territory = ARGENTINA
  - numArmies = 0
  - Territory owner = current player
  - Game Phase = SETUP
  - Current player = [Color = PURPLE, numArmiesToPlace = 6]
- Output:
  - IllegalArgumentException
    - message: "Cannot place anything other than 1 army in a territory during setup phase"
### Test 10: 
- Input:
  - territory = ALASKA
  - numArmies = 1
  - Territory owner = RED
  - Game Phase = SETUP
  - Current player = [Color = BLUE, numArmiesToPlace = 7]
- Output:
  - IllegalArgumentException
    - message: "Cannot place armies on a territory you do not own"
### Test 11:
- Input:
  - territory = ALASKA
  - numArmies = 1
  - Territory owner = current player
  - Game Phase = SETUP
  - Current player = [Color = YELLOW, numArmiesToPlace = 0]
- Output:
  - IllegalArgumentException
    - message: "Player does not have enough armies to place!"
### Test 12:
- Input:
  - territory = ALASKA
  - numArmies = 1
  - Territory owner = current player
    - Territory only has one army in it before this
  - Game Phase = SETUP
  - Current player = [Color = BLUE, numArmiesToPlace = 3]
- Output: 
  - Function output: 1 (true)
  - Territory pointer: [numArmies = 2, PlayerColor = current player]
  - Current player = [Color = BLUE, numArmiesToPlace = 2]
  - Update to say we're on the next player's turn
  - Game Phase = SETUP
### Test 13:
- Input:
  - territory = BRAZIL
  - numArmies = 1
  - Territory owner = current player
    - Territory has more than one army present before this operation (say, 5)
  - Game Phase = SETUP
  - Current player = [Color = BLACK, numArmiesToPlace = 7]
- Output: 
  - Function output: 1 (true)
  - Territory pointer: [numArmies = 6, PlayerColor = current player]
  - Current player = [Color = BLACK, numArmiesToPlace = 6]
  - Update to say we're on the next player's turn
  - Game Phase = SETUP

### PLACEMENT PHASE

### Test 14:
- Input:
  - territory = ALASKA
  - numArmies = 2
  - Territory owner = current player
  - Game Phase = PLACEMENT
  - Current player = [Color = GREEN, numArmiesToPlace = 3, |heldCards| = 5]
- Output:
  - IllegalStateException
    - message: "Player cannot place armies until they are holding \< 5 cards!"
### Test 15:
- Input:
  - territory = ALASKA
  - numArmies = 4
  - Territory owner = current player
  - Game Phase = PLACEMENT
  - Current player = [Color = RED, numArmiesToPlace = 5, |heldCards| > 5]
- Output:
  - IllegalStateException
    - message: "Player cannot place armies until they are holding \< 5 cards!"
### Test 16:
- Input:
  - territory = ALASKA
  - numArmies = 0 (or anything negative)
  - Territory owner = current player
  - Game Phase = PLACEMENT
  - Current player = [Color = RED, numArmiesToPlace = 4, |heldCards| = 2]
- Output:
  - IllegalArgumentException
    - message: "Cannot place \< 1 army on a territory during the Placement phase"
### Test 17:
- Input:
  - territory = BRAZIL
  - numArmies = 4
  - Territory owner = not current player
  - Game Phase = PLACEMENT
  - Current player = [Color = BLUE, numArmiesToPlace = 6, |heldCards| = 2]
- Output:
  - IllegalArgumentException
    - message: "Cannot place armies on a territory you do not own"
### Test 18:
- Input:
  - territory = URAL
  - numArmies = 5
  - Territory owner = current player
  - Game Phase = PLACEMENT
  - Current player = [Color = PURPLE, numArmiesToPlace = 3, |heldCards| = 2]
- Output:
  - IllegalArgumentException
    - message: "Player does not have enough armies to place!"
### Test 19:
- Input:
  - territory = ALASKA
  - numArmies = 6
  - Territory owner = current player
  - Game Phase = PLACEMENT
  - Current player = [Color = RED, numArmiesToPlace = 7, |heldCards| = 1]
- Output:
  - Function output: 1 (true)
  - Territory pointer = [numArmies = 7, playerColor = RED]
  - Current player = [Color = RED, numArmiesToPlace = 1]
    - Still on the current player's turn until they have no armies to place
  - Game Phase = PLACEMENT
### Test 20:
- Input:
  - territory = BRAZIL
  - numArmies = 5
  - Territory owner = current player
  - Game Phase = PLACEMENT
  - Current player = [Color = BLUE, numArmiesToPlace = 5, |heldCards| = 2]
- Output:
  - Function output: 1 (true)
  - Territory pointer = [numArmies = 8, playerColor = BLUE]
  - Current player = [Color = BLUE, numArmiesToPlace = 0]
  - Game Phase = ATTACK
    - Need to be on the same player's turn, but on the attack phase.

### REMAINING PHASES

### Test 21:
- Input:
  - territory = CONGO
  - numArmies = 5
  - Territory owner = current player
  - Game phase = ATTACK
  - Current player = [Color = YELLOW, numArmiesToPlace = 6, |heldCards| = 4]
- Output:
  - IllegalStateException
    - message: "Valid phases to call placeNewArmiesInTerritory from are: SCRAMBLE, SETUP, PLACEMENT"

### Test 22:
- Input:
  - territory = CONGO
  - numArmies = 5
  - Territory owner = current player
  - Game phase = FORTIFY
  - Current player = [Color = YELLOW, numArmiesToPlace = 6, |heldCards| = 4]
- Output:
  - IllegalStateException
    - message: "Valid phases to call placeNewArmiesInTerritory from are: SCRAMBLE, SETUP, PLACEMENT"

### Test 23:
- Input:
  - territory = CONGO
  - numArmies = 5
  - Territory owner = current player
  - Game phase = GAME_OVER
  - Current player = [Color = YELLOW, numArmiesToPlace = 6, |heldCards| = 4]
- Output:
  - IllegalStateException
    - message: "Valid phases to call placeNewArmiesInTerritory from are: SCRAMBLE, SETUP, PLACEMENT"

# method: `shufflePlayers(parser: DieRollParser): void`

## BVA Step 1
Input: The underlying list of players and the die roller to use

Output: The die rolls for the original list (internal), and the updated list of players (internal)

## BVA Step 2
Input: Collection, pointer

Output: Collection (players and dice)

## BVA Step 3
Input:
- Collection:
  - Empty collection (should never happen)
  - Collection with 1-2 elements (should never happen)
  - Collection with 3-6 elements
  - Collection with 7+ elements (should never happen)
  - Collection with duplicates (should never happen)
- Pointer:
  - Null pointer (should never happen)
  - Valid DieRollParser object

Output:
- Dice collection
  - Same size as input collection, no duplicates
  - Order matches the previous player order, and numbers determine the new order
- Players collection
  - Same size as input collection with different order
  - There is a possibility that the order does not change
  - Number of players don't match (error case, should never happen)
  - Collection size is different (error case, should never happen)

### Note: We do not handle error cases because the method will only be called internally, after the player list is initialized

## BVA Step 4
### Test value 1
Input: Collection = [RED, YELLOW, GREEN], Pointer = valid DieRollParser object

Output: Players = [GREEN, YELLOW, RED], Dice = [1, 2, 6]
### Test value 2
Input: Collection = [RED, YELLOW, GREEN, BLUE], Pointer = valid DieRollParser object

Output: Players = [GREEN, YELLOW, RED, BLUE], Dice = [2, 3, 5, 1]
### Test value 3
Input: Collection = [RED, YELLOW, GREEN, BLUE, PURPLE], Pointer = valid DieRollParser object

Output: Players = [GREEN, RED, YELLOW, BLUE, PURPLE], Dice = [5, 4, 6, 2, 1]
### Test value 4
Input: Collection = [RED, YELLOW, GREEN, BLUE, PURPLE, BLACK], Pointer = valid DieRollParser object

Output: Players = [GREEN, RED, YELLOW, BLACK, BLUE, PURPLE], Dice = [5, 4, 6, 2, 1, 3]
### Test value 5
Input: Collection = [RED, YELLOW, GREEN], Pointer = valid DieRollParser object

Output: Players = [RED, YELLOW, GREEN], Dice = [3, 2, 1]

# method: `calculatePlacementPhaseArmiesForCurrentPlayer(): int`

## BVA Step 1
Input: The territories that our current player owns at the start of Placement phase, per the TerritoryGraph

Output: The amount of armies our current player will receive this phase dependent on the number
of territories, as well as any continent troop bonuses they should receive.

## BVA Step 2
Input:
- PlayerColor of the current person who's going (cases)
  - Our player object should have the same internal color as our PlayerColor from our currentPlayer tracking

Output:
- Counts
  - The amount of armies you receive should never be \< 3, per Risk rules

## BVA Step 3
Input:
- current PlayerColor (Cases):
  - The 1st possibility: SETUP (can't set, should never happen)
  - The 2nd possibility: RED
  - ...
  - The 7th possibility: PURPLE
  - The 0th, 8th possibilities (can't set)
    - Should directly align with the Player object we pull
    - If it doesn't this is an error. 
- owned territories (Collection):
  - Size 0 or 42 (error cases)
  - Any amount (0, 42) is valid
  - If the player owns any continents, we need to add a bonus for owning those. 
    - Just check if they own any continent each time this method is called.

Output: (Counts)
- Depends largely on what continents are owned...
  - Bonuses are as follows:
    - 2 for South America
    - 2 for Oceania
    - 3 for Africa
    - 5 for Europe
    - 5 for North America
    - 7 for Asia
  - Note that if you own all the continents, the game is over, therefore the maximal bonus you could achieve here is:
    - 7 + 5 + 5 + 3 + 2 = 22 (Asia, North America, Europe, Africa, either of Oceania/South America)
- If the player owns \< 12 territories, they will earn 3 armies from *owning territories* 
  - This figure does NOT include any continent bonuses they may have. 
  - This also means that the absolute MINIMUM amount of armies a player may earn on a turn is **3**.
- If the player owns \>= 12 territories, give them: floor(num territories owned / 3)
  - This means you can get a MAXIMAL amount of armies from territories by owning *41* territories (if a player owns 42 the game is over)
  - This will award you with **13** armies BEFORE any continent bonuses are taken into account.
- Add the results together for continent bonuses and number of armies based on how many territories are owned
- The absolute minimum you could expect to see from this function is **3**
- The absolute maximum you could expect to see from this function is **22 + 13 = 35**

## BVA Step 4

### Test 1:
Input:
- current player = RED
- |owned territories| = 0

Output: 
- IllegalStateException
  - message: "The current player should no longer exist!"
### Test 2:
Input:
- current player = PURPLE
- |owned territories| = 42

Output:
- IllegalStateException
  - message: "Given player owns every territory, the game should be over!"

### These tests will strictly check armies gained while owning NO continents

### Test 3:
Input:
- current player = BLUE
- |ownedTerritories| = 1, ownedTerritories = {ALASKA}

Output: 3
### Test 4:
Input:
- current player = PURPLE
- owned territories = {ALASKA, ..., CONGO}, |owned territories| = 11

Output: 3
### Test 5:
Input:
- current player = YELLOW
- owned territories = {ALASKA, ..., EASTERN_AMERICA}, |owned territories| = 12

Output: 4
### Test 6:
Input:
- current player = GREEN
- owned territories = {ALASKA, ..., JAPAN}, |owned territories| = 14

Output: 4
### Test 7:
Input:
- current player = BLACK
- owned territories = {ALASKA, ..., CHINA}, |owned territories| = 15

Output: 5
### Test 8:
Input:
- current player = BLUE
- owned territories = {ALASKA, ..., YAKUTSK}, |owned territories| = 36

Output: 12

### More tests are present for in between cases, just not enumerated here.

### These tests will strictly check continent bonuses + num territories associated with the continent.

### Test 9:
Input: 
- current player = RED
- owned territories = { all for OCEANIA }, |owned territories| = 4

Output: 5 (3 from size of owned territories, 2 from owning OCEANIA)
### Test 10:
Input:
- current player = RED
- owned territories = { all for ASIA }, |owned territories| = 12

Output: 11 (4 from size of owned territories, 7 from owning ASIA)
### Test 11:
- current player = RED
- owned territories = { all for EUROPE }, |owned territories| = 7]

Output: 8 (3 from size of owned territories, 5 from owning EUROPE)

### Test 12:
- current player = RED
- owned territories = { all for AFRICA }, |owned territories|

Output: 6 (3 from size of owned territories, 3 from owning AFRICA)
### Test 13:
- current player = RED
- owned territories = { all for S. America }, |owned territories| = 4

Output: 5 (3 from size of owned territories, 2 from owning S. America)
### Test 14:
- current player = RED
- owned territories = { all for N. America }, |owned territories| = 9

Output: 8 (3 from size of owned territories, 5 from owning N. America)
### Test 15:
- current player = RED
- Player object = owned territories = { all for ASIA, EUROPE }, |owned territories| = 19

Output: 18 (6 from size of owned territories, 5 from owning EUROPE, 7 from owning ASIA)

### More tests for owned continent combinations, from 2 owned continents up to 5.

# method: `tradeInCards(selectedCardsToTradeIn: Set<Card>): Set<TerritoryType>`

## BVA Step 1
Input: A collection of Risk cards that the current player would like to turn in and the underlying state of what GamePhase we are currently in.

For input, we also care about some attributes about our player. Specifically:
- If the player owns the cards they are attempting to trade in
- If the player owns any territories MATCHING the territories on the respective cards they trade in
- The amount of cards the player has (particularly as it applies to ATTACK phase trade-ins)

Output: A collection of territories that the player owns and can place a bonus +2 armies on if the cards match them,
or an error if the set of cards is not valid to trade in, or the player doesn't own the given cards.

Additionally, we care about:
- The GamePhase being forced back to the placement phase
  - We want to emphasize that these armies MUST be placed before the player can continue
- The player receiving the bonus armies equivalent to the set's trade in value
  - So if I trade in the first set, I should get 4 more armies. 
- The respective cards being removed from the player's owned cards
  - I shouldn't be able to trade in the same cards over and over
  - Cards are removed from the game once turned in

## BVA Step 2
Input:
- selectedCardsToTradeIn: Collection
- currentPlayer: Cases
- Player object: Pointer (we care about what territories and cards they own, and how many cards they own)
- Underlying GamePhase: Cases
  - Should either be PLACEMENT/ATTACK. 

Output:
- Method output: Collection
- Underlying player object: Pointer
- GamePhase: Cases
  - Only care about setting it back to PLACEMENT

## BVA Step 3
Input:
- selectedCardsToTradeIn (Collection):
  - Any set to be deemed invalid by the TradeInParser (error case)
  - A set of cards that is valid, but is not owned by the current player (error case)
  - A valid set of trade in cards (see TradeInParser's rules about this)
- currentPlayer (Cases):
  - Should always line up directly with the GameEngine's tracking
  - If it doesn't, this is an error.
  - Valid colors are anything BESIDES `SETUP`
- Player object (Pointer):
  - Care about what cards they own at the time this method is called
    - If they don't own the cards, throw an error.
  - Also care about what territories they own (so they can get a +2 bonus armies in a territory if it matches a card)
- Underlying GamePhase (Cases):
  - PLACEMENT
  - ATTACK (only allowed if the player holds \> 5 cards; the forced trade-in threshold in Risk)
  - Any other phase (error case)

Output:
- Method output (Collection):
  - An empty collection (given no territories are matched)
  - A collection containing 1 element 
  - A collection containing 2 elements
  - A collection containing 3 elements
  - Any size outside [0, 3] (can't be set, per calling TradeInParser's methods)
- Underlying player object (Pointer):
  - numArmiesToPlace should be updated according to the current set's trade in bonus
  - Remove the relevant cards from their underlying collection
- GamePhase:
  - Set it back to PLACEMENT
  - Should never set it to anything else
  
## BVA Step 4
### Test 1:
Input:
- selectedCardsToBeTradedIn = {}
- currentPlayer = RED
- Player object = [Color = RED, ownedCards = [ Wild card, Wild card, [ALASKA, INFANTRY] ], |ownedCards| = 3 ]
- GamePhase = PLACEMENT

Output:
- IllegalStateException
  - message: "Could not trade in cards: \< trade in parser error message \>"
### I'm using a more generic error message here so that the details given in TradeInParser can shine through.

### Test 2:
Input:
- selectedCardsToBeTradedIn = [ Wild Card, [ALASKA, INFANTRY], [BRAZIL, ARTILLERY] ]
- currentPlayer = BLUE
- Player object = [Color = BLUE, ownedCards = [Wild Card, [ALASKA, INFANTRY], [BRAZIL, ARTILLERY] ], |ownedCards| = 3 ]
- GamePhase = ATTACK

Output:
- IllegalStateException
  - message: "Cannot trade in cards in the ATTACK phase unless you have \> 5 held!"

### Test 3:
Input:
- selectedCardsToBeTradedIn = [ Wild card, [ALASKA, INFANTRY], [BRAZIL, ARTILLERY] ]
- currentPlayer = BLUE
- Player object = [Color = BLUE, ownedCards = [], |ownedCards| = 0 ]
- GamePhase = PLACEMENT (test with all phases besides ATTACK/PLACEMENT here)

Output:
- IllegalArgumentException
  - message: "Player doesn't own all the selected cards!"

### Test 4:
Input:
- selectedCardsToBeTradedIn = [ Wild card, [ALASKA, INFANTRY], [BRAZIL, ARTILLERY] ]
- currentPlayer = GREEN
- Player object = [Color = GREEN, ownedCards = [ Wild card, [ALASKA, INFANTRY], [BRAZIL, ARTILLERY]  ], |ownedCards| = 3  ]
- GamePhase = SETUP

Output:
- IllegalStateException
  - message: "Can only trade in cards during the PLACEMENT or ATTACK phases"

### Test 5:
Input:
- selectedCardsToBeTradedIn = [ Wild card, [ALASKA, INFANTRY], [BRAZIL, ARTILLERY] ]
- currentPlayer = BLUE
- Player object = [Color = BLUE, numArmiesToPlace = 5, ownedCards = [Wild card, [ALASKA, INFANTRY], [BRAZIL, ARTILLERY] ], |ownedCards| = 3 ]
  - Player owns: {ALASKA, BRAZIL}
  - Let this be the first set traded in
- GamePhase = PLACEMENT

Output:
- Method output = {ALASKA, BRAZIL}
- Player object = [Color = BLUE, numArmiesToPlace = 9, ownedCards = [] ]
- GamePhase = PLACEMENT

### Test 6:
Input:
- selectedCardsToBeTradedIn = [Wild card, [ALASKA, INFANTRY], [BRAZIL, ARTILLERY]]
- currentPlayer = PURPLE
- Player object = [Color = PURPLE, numArmiesToPlace = 0, ownedCards = [Wild card, [ALASKA, INFANTRY], [BRAZIL, ARTILLERY], Wild card, [YAKUTSK, INFANTRY], [CHINA, CAVALRY] ], |ownedCards| = 6 ]
  - Player owns: {YAKUTSK, BRAZIL, JAPAN}
  - Let this be the second set traded in
- GamePhase = ATTACK

Output:
- Method output: {BRAZIL}
- Player object = [Color = PURPLE, numArmiesToPlace = 6, ownedCards = [Wild card, [YAKUTSK, INFANTRY], [CHINA, CAVALRY] ] ]
- GamePhase = PLACEMENT

### Test 7:
Input:
- selectedCardsToBeTradedIn = {Wild card, [ALASKA, INFANTRY], [BRAZIL, ARTILLERY]}
- currentPlayer = PURPLE
- Player object = [Color = PURPLE, numArmiesToPlace = 0, ownedCards = [Wild card, [ALASKA, INFANTRY], [BRAZIL, ARTILLERY], Wild card, [YAKUTSK, INFANTRY], [CHINA, CAVALRY] ], |ownedCards| = 6 ]
  - Player owns: {EASTERN_AUSTRALIA}
  - Let this be the second set traded in
- GamePhase = ATTACK

Output:
- Method output: {}
- Player object = [Color = PURPLE, numArmiesToPlace = 6, ownedCards = [Wild card, [YAKUTSK, INFANTRY], [CHINA, CAVALRY] ] ]
- GamePhase = PLACEMENT

# method: `handleErrorCasesForAttackingTerritory(sourceTerritory: TerritoryType, destTerritory: TerritoryType, numAttackers: int, numDefenders: int): void`

This is part **1** of a series of method calls that constitute attacking in Risk. This will enumerate just the error cases.

## BVA Step 1
Input: The respective territory that the attacker is moving troops FROM, the territory they are moving their armies INTO
to attack, and the respective number of attackers and defenders to use in the battle.

Since this method is all about error handling, here are some reasons we'll run into errors:
- The current phase the game is in (should only ever be attack if we're here!)
- The number of armies present in BOTH the source AND destination territory
  - If you're left with 0 armies in the territory you're attacking from as a result, you shouldn't be allowed to do this.
  - Need to know if you can use that many attackers legally
  - Need to know how many defenders are in the battle via the destination territory
- Who owns the respective territories
  - You can't attack yourself!
  - If you don't own the territory you've selected, you also can't attack from there...
- If the territories border each other
  - You can't attack a territory that's halfway across the map; they should be adjacent.
- If the current player is holding on to too many cards
  - This should only happen as a result of taking out a player; this is still a forced trade in.

Output: An error if the player has too many cards, provides an invalid amount of armies, tries to attack between 
territories that are not adjacent, or if we're in the wrong phase. If we don't run into an error case, this will 
not return anything.

## BVA Step 2
Input:
- sourceTerritory: Cases
- destTerritory: Cases
- numAttackers: Interval [1, 3]
- numDefenders: Interval [1, 2]
- currentGamePhase: Cases
- currentlyGoingPlayer: Cases
- player object: Pointer
  - We only care about the amount of cards they're holding on to
- source & destination territory objects: Pointer
  - Primarily care that:
    - current player owns the source territory
    - A different player owns the destination territory
    - Number of armies in each territory is SUFFICIENT to meet the attackers/defenders
      - Must be \>= 1 army left in source territory after attackers are accounted for
      - Must be \>= 0 armies left in destination territory after defenders are accounted for

Output:
- Method output: N/A (void method)
- Exceptions if above conditions are not met
  - IllegalStateException if a Player has too many cards, or we are in the wrong game phase (not attack)
  - IllegalArgumentException for all other input errors

## BVA Step 3
Input:
- sourceTerritory, destTerritory (Cases):
  - ALASKA
  - ARGENTINA
  - ...
  - YAKUTSK
  - The 0th, 43rd possibility (can't set, Java enums)
  - Source & destination territories are not adjacent on the Risk map (error case)
- numAttackers (Interval):
  - \<= 0 (error case)
  - 1 (minimum amount of attackers)
  - 2
  - 3 (maximal amount of attackers)
  - \>= 4 (error case)
- numDefenders (Interval):
  - \<= 0 (error case)
  - 1 (minimum amount of defenders)
  - 2 (maximal amount of attackers)
  - \>= 3 (error case)
- currentGamePhase (Cases):
  - SCRAMBLE (error case)
  - SETUP (error case)
  - PLACEMENT (error case)
  - ATTACK 
  - FORTIFY (error case)
  - GAME_OVER (error case)
  - The 0th, 7th possibilities (can't set, Java enum)
- currentlyGoingPlayer (Cases):
  - SETUP (error case)
  - Any other PlayerColor is fine, but it MUST match the territory input (so colors like):
    - BLACK
    - RED
    - YELLOW
    - BLUE
    - GREEN
    - PURPLE
  - The 0th, 8th possibilities (can't set, Java enum)
- Player object (Pointer):
  - Null pointer (can't set, Martin's rules)
  - Pointer to the true object
    - We care about the number of cards they are holding
      - \>= 5 cards (error case)
      - Anywhere in [0, 4] is fine. |ownedCards| should never be negative due to it being a collection.
- sourceTerritory, destTerritory objects (Pointer):
  - Null pointer (can't set, Martin's rules)
  - Pointer to the true object
    - We want to consider ownership of the source, destination territories
      - Source not owned by current player (error case)
      - Destination owned by current player (error case, anybody else is fine)
    - Need to consider the number of armies stationed in each
      - `numAttackers` > `numArmiesInSourceTerritory - 1` (error case)
      - `numDefenders` > `numArmiesInDefenderTerritory` (error case)

Output:
- method output (N/A)
- Exceptions
  - IllegalStateException if:
    - GamePhase is NOT ATTACK
    - Player is holding \>= 5 cards
  - IllegalArgumentException if:
    - numAttackers is not in [1, 3]
    - numDefenders is not in [1, 2]
    - sourceTerritory and destTerritory are NOT adjacent on the Risk map
    - sourceTerritory is not owned by the current player
    - destTerritory is owned by the current player

## BVA Step 4
### Test 1:
Input:
- sourceTerritory, destTerritory = ALASKA
- numAttackers = 3
- numDefenders = 2
- current game phase = ATTACK
- currently going player = PURPLE
- player pointer = [Color = PURPLE, |ownedCards| = 3]
- source, destination territory = [ALASKA, numArmiesInTerritory = 4, ownedBy = PURPLE]
  - equal to each other on purpose

Output:
- IllegalArgumentException
  - message: "Source and destination territory must be two adjacent territories!"

This above test will have be repeated will all territories as input

### Test 2:
Input:
- sourceTerritory = BRAZIL
- destTerritory = INDIA
- numAttackers = 2
- numDefenders = 1
- current game phase = ATTACK
- currently going player = PURPLE
- player pointer = [Color = PURPLE, |ownedCards| = 3]
- source territory = [BRAZIL, numArmiesInTerritory = 6, ownedBy = PURPLE]
- destination territory = [INDIA, numArmiesInTerritory = 2, ownedBy = GREEN]

Output:
- IllegalArgumentException
  - message: "Source and destination territory must be two adjacent territories!"

The above test will be repeated with several non-adjacent territories.

### Test 3:
Input:
- sourceTerritory = BRAZIL
- destTerritory = VENEZUELA
- numAttackers = 3
- numDefenders = 2
- current game phase = ATTACK
- currently going player = PURPLE
- player pointer = [Color = PURPLE, |ownedCards| = 3]
- source territory = [BRAZIL, numArmiesInTerritory = 6, ownedBy = YELLOW]
- destination territory = [VENEZUELA, numArmiesInTerritory = 3, ownedBy = GREEN]

Output:
- IllegalArgumentException
  - message: "Source territory is not owned by the current player!"

### Test 4:
Input:
- sourceTerritory = BRAZIL
- destTerritory = VENEZUELA
- numAttackers = 3
- numDefenders = 2
- current game phase = ATTACK
- currently going player = PURPLE
- player pointer = [Color = PURPLE, |ownedCards| = 3]
- source territory = [BRAZIL, numArmiesInTerritory = 6, ownedBy = PURPLE]
- destination territory = [VENEZUELA, numArmiesInTerritory = 3, ownedBy = PURPLE]

Output:
- IllegalArgumentException
  - message: "Destination territory is owned by the current player!"

### Test 5:
Input:
- sourceTerritory = BRAZIL
- destTerritory = VENEZUELA
- numAttackers = 3
- numDefenders = 2
- current game phase = SETUP
- currently going player = PURPLE
- player pointer = [Color = PURPLE, |ownedCards| = 3]
- source territory = [BRAZIL, numArmiesInTerritory = 6, ownedBy = PURPLE]
- destination territory = [VENEZUELA, numArmiesInTerritory = 3, ownedBy = BLUE]

Output:
- IllegalStateException
  - message: "Attacking territories is not allowed in any phase besides attack!"

### Test 6:
Input:
- sourceTerritory = BRAZIL
- destTerritory = VENEZUELA
- numAttackers = 3
- numDefenders = 2
- current game phase = FORTIFY
- currently going player = PURPLE
- player pointer = [Color = PURPLE, |ownedCards| = 3]
- source territory = [BRAZIL, numArmiesInTerritory = 6, ownedBy = PURPLE]
- destination territory = [VENEZUELA, numArmiesInTerritory = 3, ownedBy = BLUE]

Output:
- IllegalStateException
  - message: "Attacking territories is not allowed in any phase besides attack!"

### Test 7:
Input:
- sourceTerritory = BRAZIL
- destTerritory = VENEZUELA
- numAttackers = -1
- numDefenders = 2
- current game phase = ATTACK
- currently going player = PURPLE
- player pointer = [Color = PURPLE, |ownedCards| = 3]
- source territory = [BRAZIL, numArmiesInTerritory = 6, ownedBy = PURPLE]
- destination territory = [VENEZUELA, numArmiesInTerritory = 3, ownedBy = BLUE]

Output:
- IllegalArgumentException
  - message: "Number of armies to attack with must be within [1, 3]!"

### Test 8:
Input:
- sourceTerritory = BRAZIL
- destTerritory = VENEZUELA
- numAttackers = 10
- numDefenders = 2
- current game phase = ATTACK
- currently going player = PURPLE
- player pointer = [Color = PURPLE, |ownedCards| = 3]
- source territory = [BRAZIL, numArmiesInTerritory = 6, ownedBy = PURPLE]
- destination territory = [VENEZUELA, numArmiesInTerritory = 3, ownedBy = BLUE]

Output:
- IllegalArgumentException
  - message: "Number of armies to attack with must be within [1, 3]!"

### Test 9:
Input:
- sourceTerritory = BRAZIL
- destTerritory = VENEZUELA
- numAttackers = 3
- numDefenders = -1
- current game phase = ATTACK
- currently going player = PURPLE
- player pointer = [Color = PURPLE, |ownedCards| = 3]
- source territory = [BRAZIL, numArmiesInTerritory = 6, ownedBy = PURPLE]
- destination territory = [VENEZUELA, numArmiesInTerritory = 3, ownedBy = BLUE]

Output:
- IllegalArgumentException
  - message: "Number of armies to defend with must be within [1, 2]!"

### Test 10:
Input:
- sourceTerritory = BRAZIL
- destTerritory = VENEZUELA
- numAttackers = 3
- numDefenders = 10
- current game phase = ATTACK
- currently going player = PURPLE
- player pointer = [Color = PURPLE, |ownedCards| = 3]
- source territory = [BRAZIL, numArmiesInTerritory = 6, ownedBy = PURPLE]
- destination territory = [VENEZUELA, numArmiesInTerritory = 3, ownedBy = BLUE]

Output:
- IllegalArgumentException
  - message: "Number of armies to defend with must be within [1, 2]!"

### Test 11:
Input:
- sourceTerritory = BRAZIL
- destTerritory = VENEZUELA
- numAttackers = 3
- numDefenders = 0
- current game phase = ATTACK
- currently going player = PURPLE
- player pointer = [Color = PURPLE, |ownedCards| = 3]
- source territory = [BRAZIL, numArmiesInTerritory = 6, ownedBy = PURPLE]
- destination territory = [VENEZUELA, numArmiesInTerritory = 3, ownedBy = BLUE]

Output:
- IllegalArgumentException
  - message: "Number of armies to defend with must be within [1, 2]!"

### Test 12:
Input:
- sourceTerritory = BRAZIL
- destTerritory = VENEZUELA
- numAttackers = 1
- numDefenders = 2
- current game phase = ATTACK
- currently going player = PURPLE
- player pointer = [Color = PURPLE, |ownedCards| = 5]
- source territory = [BRAZIL, numArmiesInTerritory = 6, ownedBy = PURPLE]
- destination territory = [VENEZUELA, numArmiesInTerritory = 3, ownedBy = BLUE]

Output:
- IllegalStateException
  - message: "Player must trade in cards before they can attack!"

### Test 13:
Input:
- sourceTerritory = BRAZIL
- destTerritory = VENEZUELA
- numAttackers = 1
- numDefenders = 2
- current game phase = ATTACK
- currently going player = PURPLE
- player pointer = [Color = PURPLE, |ownedCards| = 6 (any amount > 5)]
- source territory = [BRAZIL, numArmiesInTerritory = 6, ownedBy = PURPLE]
- destination territory = [VENEZUELA, numArmiesInTerritory = 3, ownedBy = BLUE]

Output:
- IllegalStateException
  - message: "Player must trade in cards before they can attack!"

### Test 14:
Input:
- sourceTerritory = BRAZIL
- destTerritory = VENEZUELA
- numAttackers = 3
- numDefenders = 2
- current game phase = ATTACK
- currently going player = PURPLE
- player pointer = [Color = PURPLE, |ownedCards| = 4]
- source territory = [BRAZIL, numArmiesInTerritory = 2, ownedBy = PURPLE]
- destination territory = [VENEZUELA, numArmiesInTerritory = 3, ownedBy = BLUE]

Output:
- IllegalArgumentException
  - message: "Source territory has too few armies to use in this attack!"

### Test 15:
Input:
- sourceTerritory = BRAZIL
- destTerritory = VENEZUELA
- numAttackers = 1
- numDefenders = 2
- current game phase = ATTACK
- currently going player = PURPLE
- player pointer = [Color = PURPLE, |ownedCards| = 4]
- source territory = [BRAZIL, numArmiesInTerritory = 1, ownedBy = PURPLE]
- destination territory = [VENEZUELA, numArmiesInTerritory = 3, ownedBy = BLUE]

Output:
- IllegalArgumentException
  - message: "Source territory has too few armies to use in this attack!"

### Test 16:
Input:
- sourceTerritory = BRAZIL
- destTerritory = VENEZUELA
- numAttackers = 3
- numDefenders = 2
- current game phase = ATTACK
- currently going player = PURPLE
- player pointer = [Color = PURPLE, |ownedCards| = 4]
- source territory = [BRAZIL, numArmiesInTerritory = 3, ownedBy = PURPLE]
- destination territory = [VENEZUELA, numArmiesInTerritory = 3, ownedBy = BLUE]

Output:
- IllegalArgumentException
  - message: "Source territory has too few armies to use in this attack!"

### Test 17:
Input:
- sourceTerritory = BRAZIL
- destTerritory = VENEZUELA
- numAttackers = 2
- numDefenders = 2
- current game phase = ATTACK
- currently going player = PURPLE
- player pointer = [Color = PURPLE, |ownedCards| = 4]
- source territory = [BRAZIL, numArmiesInTerritory = 3, ownedBy = PURPLE]
- destination territory = [VENEZUELA, numArmiesInTerritory = 1, ownedBy = BLUE]

Output:
- IllegalArgumentException
  - message: "Source territory has too few defenders for this defense!"

### Test 18:
Input:
- sourceTerritory = BRAZIL
- destTerritory = VENEZUELA
- numAttackers = 2
- numDefenders = 2
- current game phase = ATTACK
- currently going player = YELLOW
- player pointer = [Color = YELLOW, |ownedCards| = 3, ownedTerritories = {BRAZIL} ]
- sourceTerritory = [BRAZIL, numArmiesInTerritory = 7, ownedBy = YELLOW ]
- destinationTerritory = [VENEZUELA, numArmiesInTerritory = 3, ownedBy = PURPLE ]

Output: N/A (input has no errors!)

# method: `rollDiceForBattle(numAttackers: int, numDefenders: int): List<BattleResult>`

This is part **2** of a series of method calls that constitute attacking in Risk. This is meant to be invoked AFTER
calling the error handling method. Note that this isn't completely defenseless to bad input either, as this will
interact with the DieRollParser, which has input validation of its own.

## BVA Step 1
Input: The number of attackers that will be participating in the battle, namely, the amount of attacker dice to roll,
and the amount of defender dice to roll in the battle. Additionally, we will utilize our underlying dice manager to
roll the dice, and parse the results for us.

Output: A collection detailing the results of each "dice pairing", ordered in the way Risk calculates casualties.
Namely, you always pair the highest dice against each other, then next highest, etc. Defender wins on ties.

We also want to store the dice roll results for our users, as they likely want to know what they rolled as opposed to
just seeing numbers go down. This means we should store:
- The attacker dice roll results
- The defender dice roll results
- The overall battle result in its pairwise form
This will be done utilizing fields / getters on the class.

## BVA Step 2
Input:
- numAttackers: Interval [1, 3]
- numDefenders: Interval [1, 2]
- die roll parser: Pointer

Output:
- attackerDiceRolls: Collection
- defenderDiceRolls: Collection
- battleResults (will match method output): Collection 

## BVA Step 3
Input:
- numAttackers (Interval):
  - \<= 0 (error case, will be caught by DieRollParser)
  - 1 (minimal amount of attackers allowed)
  - 2
  - 3 (maximal amount of attackers allowed)
  - \>= 4 (error case, will be caught by DieRollParser)
- numDefenders (Interval):
  - \<= 0 (error case, will be caught by DieRollParser)
  - 1 (minimal amount of defenders allowed)
  - 2 (maximal amount of defenders allowed)
  - \>= 3 (error case, will be caught by DieRollParser)
- DieRollParser (Pointer):
  - A null pointer (can't set, Martin's rules)
  - A pointer to the true object

Output:
- attackerDiceRolls (Collection):
  - Size should always match the number of attackers
    - Assuming number of attackers was valid input
  - Sorted in non-decreasing order (done by DieRollParser)
  - Must be able to contain duplicates 
- defenderDiceRolls (Collection):
  - Size should always match the number of defenders
    - Assuming number of defenders was valid input
  - Sorted in non-decreasing order (done by DieRollParser)
  - Must be able to contain duplicates
- battleResults (Collection):
  - Should have the same size as the minimum of the two inputs
    - So if numDefenders is smaller, it has the same size as numDefenders.
  - Must be able to contain duplicates

## BVA Step 4
### Test 1:
Input:
- numAttackers = 0
- numDefenders = 2
- die roll parser = A pointer to the true object

Output:
- IllegalArgumentException
  - message: "Valid amount of dice is in the range [1, 3]"

### Test 2:
Input:
- numAttackers = 4
- numDefenders = 2
- die roll parser = A pointer to the true object

Output:
- IllegalArgumentException
  - message: "Valid amount of dice is in the range [1, 3]"

### Test 3:
Input:
- numAttackers = 1
- numDefenders = 0
- die roll parser = A pointer to the true object

Output:
- IllegalArgumentException
  - message: "Valid amount of dice is in the range [1, 2]"

### Test 4:
Input:
- numAttackers = 1
- numDefenders = 3
- die roll parser = A pointer to the true object

Output:
- IllegalArgumentException
  - message: "Valid amount of dice is in the range [1, 2]"

### Test 5:
Input:
- numAttackers = 1
- numDefenders = 1
- die roll parser = A pointer to the true object

Output:
- attackerDice = [5]
- defenderDice = [6]
- battleResults = [DEFENDER_VICTORY]

### Test 6:
- numAttackers = 1
- numDefenders = 2
- die roll parser = A pointer to the true object

Output:
- attackerDice = [4]
- defenderDice = [3, 2]
- battleResults = [ATTACKER_VICTORY]

### Test 7:
- numAttackers = 2
- numDefenders = 1
- die roll parser = A pointer to the true object

Output:
- attackerDice = [5, 2]
- defenderDice = [5]
- battleResults = [DEFENDER_VICTORY]

### Test 8:
- numAttackers = 2
- numDefenders = 2
- die roll parser = A pointer to the true object

Output:
- attackerDice = [4, 3]
- defenderDice = [5, 2]
- battleResults = [DEFENDER_VICTORY, ATTACKER_VICTORY]

### Test 9:
- numAttackers = 3
- numDefenders = 1
- die roll parser = A pointer to the true object

Output:
- attackerDice = [2, 1, 1]
- defenderDice = [5]
- battleResults = [DEFENDER_VICTORY]

### Test 10:
- numAttackers = 3
- numDefenders = 2
- die roll parser = A pointer to the valid object

Output:
- attackerDice = [4, 2, 1]
- defenderDice = [5, 5]
- battleResults = [DEFENDER_VICTORY, DEFENDER_VICTORY]

# method: `handleArmyLosses(srcTerritory: TerritoryType, destTerritory: TerritoryType, battleResults: List<BattleResult>): AttackConsequence`

This is part **3** of a series of method calls that constitute attacking in Risk. 

## BVA Step 1
Input: The respective territories the attack occurred between, and the results from rolling the dice in the current battle.

Output: An enum that tells us whether the destination should change hands as a result of the attack.

Additionally, this method will modify the number of armies in the respective territories just based on the battleResults.
So if a defender goes down to 0 armies, a later method about taking over the territory will handle putting the respective
attackers into the destination.

## BVA Step 2
Input:
- srcTerritory, destTerritory: Cases
- battleResults: Collection
- source, dest territory objects: Pointer
  - only care about the number of armies in each territory here, since we'll modify it

Output:
- method output: Cases
- source, dest territory objects: Pointer
  - Modifying the number of armies in each is the most important part

## BVA Step 3
Input:
- srcTerritory, destTerritory (Cases):
  - ALASKA
  - ARGENTINA
  - ...
  - YAKUTSK 
  - The 0th, 43rd possibilities (can't set, Java enum)
  - These should be adjacent if we did error validation correctly.
- battleResults (Collection):
  - An empty collection (can't set)
  - A collection with 1 element
  - A collection with 2 elements
  - A collection with \> 2 elements (can't set)
  - A collection with duplicates
  - A collection without duplicates

Output:
- method output (Cases):
  - DEFENDER_LOSES_TERRITORY
  - NO_CHANGE
  - The 0th, 3rd cases (can't set, Java enum)
- source, dest territory objects (Pointer):
  - A null pointer (can't set, Martin's rules)
  - A pointer to the true object
    - We care about modifying the number of armies present in the source, destination territory
    - They will be modified according to the battle results 
      - If an attacker wins twice, the defender loses two troops.

## BVA Step 4
### Test 1:
Input:
- srcTerritory = SIAM
- destTerritory = CHINA
- battleResults = [ATTACKER_VICTORY]
- source territory pointer = [SIAM, numArmies = 3]
- dest territory pointer = [CHINA, numArmies = 3]

Output:
- method output: NO_CHANGE
- source territory pointer = [SIAM, numArmies = 3]
- dest territory pointer = [CHINA, numArmies = 2]

### Test 2:
Input:
- srcTerritory = SIAM
- destTerritory = CHINA
- battleResults = [ATTACKER_VICTORY, ATTACKER_VICTORY]
- source territory pointer = [SIAM, numArmies = 3]
- dest territory pointer = [CHINA, numArmies = 3]

Output:
- method output: NO_CHANGE
- source territory pointer = [SIAM, numArmies = 3]
- dest territory pointer = [CHINA, numArmies = 1]

### Test 3:
Input:
- srcTerritory = SIAM
- destTerritory = CHINA
- battleResults = [ATTACKER_VICTORY]
- source territory pointer = [SIAM, numArmies = 3]
- dest territory pointer = [CHINA, numArmies = 1]

Output:
- method output: DEFENDER_LOSES_TERRITORY
- source territory pointer = [SIAM, numArmies = 3] (note that we don't move any attackers yet!)
- dest territory pointer = [CHINA, numArmies = 0]

### Test 4:
Input:
- srcTerritory = SIAM
- destTerritory = CHINA
- battleResults = [ATTACKER_VICTORY, ATTACKER_VICTORY]
- source territory pointer = [SIAM, numArmies = 3]
- dest territory pointer = [CHINA, numArmies = 2]

Output:
- method output: DEFENDER_LOSES_TERRITORY
- source territory pointer = [SIAM, numArmies = 3] (note that we don't move any attackers yet!)
- dest territory pointer = [CHINA, numArmies = 0]

### Test 5:
Input:
- srcTerritory = BRAZIL
- destTerritory = PERU
- battleResults = [ATTACKER_VICTORY, DEFENDER_VICTORY]
- source territory pointer = [BRAZIL, numArmies = 3]
- dest territory pointer = [PERU, numArmies = 3]

Output:
- method output: NO_CHANGE
- source territory pointer = [BRAZIL, numArmies = 2]
- dest territory pointer = [PERU, numArmies = 2]

### Test 6:
Input: 
- srcTerritory = BRAZIL
- destTerritory = PERU
- battleResults = [DEFENDER_VICTORY]
- source territory pointer = [BRAZIL, numArmies = 3]
- dest territory pointer = [PERU, numArmies = 3]

Output:
- method output: NO_CHANGE
- source territory pointer = [BRAZIL, numArmies = 2]
- dest territory pointer = [PERU, numArmies = 3]

### Test 7:
Input:
- srcTerritory = URAL
- destTerritory = UKRAINE
- battleResults = [DEFENDER_VICTORY, DEFENDER_VICTORY]
- source territory pointer = [URAL, numArmies = 3]
- dest territory pointer = [UKRAINE, numArmies = 3]

Output:
- method output: NO_CHANGE
- source territory pointer = [URAL, numArmies = 1]
- dest territory pointer = [UKRAINE, numArmies = 3]

# method: `handleAttackerTakingTerritory(destTerritory: TerritoryType, numAttackers: int): PlayerColor`

This is part **4** of a series of method calls that constitute attacking in Risk. It is intended to be called
if we determine that the defending player will lose their territory as a result of the attack. 

## BVA Step 1
Input: The destination territory of the attack, the number of attackers utilized to take over the territory, as well
as the underlying state of who's turn it is in the game. 

Additionally, we'll need to know some information about the underlying state of the territory itself; as we want to 
modify the amount of armies there and change who owns it. 

Output: The PlayerColor of the Player who controlled the territory before us; and the underlying state of the
territory object associated with the destination territory. 

We're interested in increasing the number of armies present (bumping it up to `numAttackers`), and changing the
PlayerColor for who owns it to the current player. We also want to modify the Player object associated with
the current player to say they now own this territory; and say the defender no longer owns it.

## BVA Step 2
Input:
- destTerritory: Cases
- numAttackers: Interval [1, 3]
- currently going player: Cases
- attacking player object: Pointer
- defending player object: Pointer
- destination territory object: Pointer

Output:
- method output: Cases
- destination territory object: Pointer
  - This falls under both input and output because we want to make sure it changes.
- attacking player object: Pointer
- defending player object: Pointer

## BVA Step 3
Input:
- destTerritory (Cases):
  - ALASKA
  - ARGENTINA
  - ...
  - YAKUTSK
  - The 0th, 43rd cases (can't set, Java enums)
  - Should always line up with the destination territory used in all the other attacking related methods.
- numAttackers (Interval):
  - \<= 0 (error case, should be handled by former methods)
  - 1
  - 2
  - 3
  - \>= 4 (error case, should be handled by former methods)
- currently going player (Cases):
  - SETUP (error case)
  - YELLOW
  - PURPLE
  - ...
  - BLACK 
  - The 0th, 8th possibility (can't set, Java enum)
- destination territory object (Pointer):
  - A null pointer (can't set, Martin's rules)
  - A pointer to the true object
    - Need to get information about WHO owns the territory beforehand (ownedBy = ?)
    - So we'll have to cycle through the players in the game to determine this FIRST
    - Should always be a player who is in the current game or something went really wrong before this method.
    - Army modifications occur before output as well
- player object(s) (Pointer):
  - A null pointer (can't set, Martin's rules)
  - A pointer to the true object
    - Need to add this territory to the attacking player's collection of owned territories
    - Need to remove this territory from the defending player's collection of owned territories
    - Cards cannot be properly used to award the 2 army bonus for owning a territory otherwise

Output:
- method output (Cases):
  - SETUP (error case, should never happen if we're in the ATTACK phase)
  - YELLOW
  - ...
  - BLACK
  - The 0th, 8th possibility (can't set, Java enum)
- destination territory object (Pointer):
  - A null pointer (can't set, Martin's rules)
  - A pointer to the true object
    - Need to see that the number of armies in the territory = `numAttackers`
    - Need to see that the owner's PlayerColor has changed to be the current player
- player object(s) (Pointer):
  - A null pointer (can't set, Martin's rules)
  - A pointer to the true object
    - Make sure the newest territory is added to the attacker's owned territories; removed from defending player's territories.

## BVA Step 4
### Test 1:
Input:
- destTerritory = ALASKA
- numAttackers = 1
- currently going player = YELLOW
- dest territory object = [ownedBy = PURPLE, numArmies = 0, ALASKA] 
  - Note that numArmies should always be 0 otherwise we this has been called in the WRONG place.
- attacking player object = [YELLOW, ownedTerritories = {YAKUTSK} ]
- defending player object = [PURPLE, ownedTerritories = {ALASKA, NORTHWEST_TERRITORIES} ]

Output: 
- method output: PURPLE
- dest territory object = [ownedBy = YELLOW, numArmies = 1, ALASKA]
- attacking player object = [YELLOW, ownedTerritories = {YAKUTSK, ALASKA} ]
- defending player object = [PURPLE, ownedTerritories = {NORTHWEST_TERRITORIES} ]

### Test 2:
Input:
- destTerritory = VENEZUELA
- numAttackers = 2
- currently going player = GREEN
- dest territory object = [ownedBy = BLACK, numArmies = 0, VENEZUELA]
- attacking player object = [GREEN, ownedTerritories = {PERU, BRAZIL, ARGENTINA} ]
- defending player object = [BLACK, ownedTerritories = {VENEZUELA, CENTRAL_AMERICA, WESTERN_UNITED_STATES} ]

Output:
- method output: BLACK
- dest territory object = [ownedBy = GREEN, numArmies = 2, VENEZUELA]
- attacking player object = [GREEN, ownedTerritories = {PERU, BRAZIL, ARGENTINA, VENEZUELA} ]
- defending player object = [BLACK, ownedTerritories = {CENTRAL_AMERICA, WESTERN_UNITED_STATES} ]

### Test 3:
Input:
- destTerritory = EGYPT
- numAttackers = 3
- currently going player = BLUE
- dest territory object = [ownedBy = RED, numArmies = 0, EGYPT]
- attacking player object = [BLUE, ownedTerritories = {SOUTHERN_EUROPE, CONGO, NORTH_AFRICA, MIDDLE_EAST} ]
- defending player object = [RED, ownedTerritories = {EGYPT, YAKUTSK} ]

Output:
- method output: RED
- dest territory object = [ownedBy = BLUE, numArmies = 3, EGYPT]
- attacking player object = [BLUE, ownedTerritories = {SOUTHERN_EUROPE, CONGO, NORTH_AFRICA, MIDDLE_EAST, EGYPT} ]
- defending player object = [RED, ownedTerritories = {YAKUTSK} ]

# method: `handlePlayerLosingGameIfNecessary(player: PlayerColor): void`

This is part **5** of a series of method calls that constitute attacking in Risk. If a defending player is left with 0
territories as a result of an attack, this method will remove them from the game.

## BVA Step 1
Input: The player who has lost a territory as a result of the current attack, and may lose the game as a result. 

We also need to know about: 
- the underlying state of the entire map (as we need to know if this player owns NO territories).
- the underlying state of the defending/attacking player objects as a player gives up the cards they own if they lose
- the underlying state of the players list 
  - This should already be NICELY set since we're in the ATTACK phase
- the underlying state of the players map
  - This should already be NICELY set since we're in the ATTACK phase

Output: If the player owns no territories, the list of players (and map of players) will no longer contain the losing 
player; their cards are transferred to the attacking player (namely the currently going player). 

## BVA Step 2
Input:
- player: Cases
- ALL territory objects held by the game: Pointer
- defending/attacking player object: Pointer

Output:
- method output: N/A
- playersList: Collection (only if player owns no territories will this be modified)
- playersMap: Collection (only if player owns no territories will this be modified)
- defending/attacking player object: Pointer

## BVA Step 3
Input: 
- player: Cases
  - SETUP (error case, can't set - should not happen if we are in the ATTACK phase)
  - YELLOW
  - PURPLE
  - ...
  - BLACK
  - The 0th, 8th possibilities (can't set, Java enum)
- territory objects (Pointer):
  - A null pointer (can't set, Martin's rules)
  - A pointer to the true object
    - For each territory, we're looking to see who it's owned by.
      - If the given player owns even one territory, then we don't handle anything related to deleting/transferring cards.
      - So if the player owns [1, 42] nothing special happens with this method.
- defending/attacking player object (Pointer):
  - A null pointer (can't set, Martin's rules)
  - A pointer to the true object
    - If the defending player is to be eliminated, we care about:
      - The cards the attacking player currently holds
      - The cards the defending player currently holds
    - The defender's cards will be added to the attacker's collection before they are removed from the game.
- playersList, playersMap (Collection):
  - Both have a minimum size of 2 elements (whether it be just elements or key-value pairs)
  - Both have a maximal size of 6 elements
  - No duplicates
  - Any other cases are an error that should've been handled BEFORE ever calling this one.

Output:
- playersList (Collection):
  - If the player should in fact be removed, the number of elements in the collection should decrease by 1.
- playersMap (Collection):
  - If the player should in fact be removed, the number of elements in the collection should decrease by 1.
- defending/attacking player object (Pointer):
  - If the player should in fact be removed:
    - Take the cards that the losing player owns
    - Transfer them to attacking player's collection

## BVA Step 4
### Test 1:
Input:
- player: BLUE
- territory objects:
  - 41 are NOT owned by BLUE
  - 1 IS owned by BLUE
- defending/attacking player objects:
  - attacker = [RED, ownedCards = {Wild Card, TerritoryCard = [BRAZIL, INFANTRY] } ]
  - defender = [BLUE, ownedCards = {TerritoryCard = [ARGENTINA, ARTILLERY] } ]
- playersList = [BLUE, BLACK, RED, YELLOW]
- playersMap = [BLUE -> playerObject (defender), BLACK -> playerObject, RED -> playerObject (attacker), Yellow -> playerObject]

Output:
- playersList remains the same
- playersMap remains the same
- No cards are transferred

### Test 2:
Input:
- player: GREEN
- territory objects:
  - 40 are NOT owned by GREEN
  - 2 ARE owned by GREEN
- defending/attacking player objects:
  - attacker = [PURPLE, ownedCards = {} ]
  - defender = [GREEN, ownedCards = {Wild Card} ]
- playersList = [GREEN, PURPLE]
- playersMap = [GREEN -> playerObject (defender), PURPLE -> playerObject (attacker)]

Output:
- playersList remains the same
- playersMap remains the same
- No cards are transferred

### Test 3:
Input:
- player: YELLOW
- territory objects: 
  - 2 is NOT owned by YELLOW (think: 1 not owned by yellow should not be possible in the context of ATTACK and having yellow lose)
  - 40 ARE owned by YELLOW
- defending/attacking player objects:
  - attacker = [BLACK, ownedCards = {} ]
  - defender = [YELLOW, ownedCards = {} ]
- playersList = [YELLOW, BLUE, RED, PURPLE, GREEN, BLACK]
- playersMap = [YELLOW -> playerObject (defender), ..., BLACK -> playerObject (attacker)]

Output:
- playersList remains the same
- playersMap remains the same
- No cards are transferred

### Test 4:
Input:
- player: BLUE
- territory objects:
  - All 42 are NOT owned by BLUE
- defending/attacking player objects:
  - attacker = [RED, ownedCards = {Wild Card}]
  - defender = [BLUE, ownedCards = {}]
- playersList = [RED, PURPLE, BLUE, GREEN]
- playersMap = [RED -> playerObject (attacker), ..., BLUE -> playerObject (defender), ...]

Output:
- playersList = [RED, PURPLE, GREEN] (BLUE is eliminated)
- playersMap = [RED -> playerObject (attacker), PURPLE -> playerObject, GREEN -> playerObject] (BLUE is eliminated)
- Since BLUE (defender) had no cards, the attacker player object looks like how it started:
  - attacker = [RED, ownedCards = {Wild Card}]
  - defender = [BLUE, ownedCards = {} ] (though this object will no longer exist, so it doesn't really matter)

### Test 5:
Input:
- player: GREEN
- territory objects:
  - All 42 are NOT owned by GREEN
- defending/attacking player objects:
  - attacker = [YELLOW, ownedCards = {Wild Card, Territory Card = [CONGO, ARTILLERY] } ]
  - defender = [GREEN, ownedCards = {Territory Card = [IRKUTSK, INFANTRY], Territory Card = [UKRAINE, CAVALRY] } ]
- playersList = [GREEN, PURPLE, YELLOW]
- playersMap = [GREEN -> playerObject (defender), PURPLE -> playerObject, YELLOW -> playerObject (attacker)]

Output:
- playersList = [PURPLE, YELLOW] (GREEN is eliminated)
- playersMap = [PURPLE -> playerObject, YELLOW -> playerObject (attacker)] (GREEN is eliminated)
- attacker player object = [YELLOW, ownedCards = {Wild Card, Territory Card = [CONGO, ARTILLERY], Territory Card = [IRKUTSK, INFANTRY], Territory Card = [UKRAINE, CAVALRY] } ]
- defender player object = [GREEN, ownedCards = {} ] (this will also no longer exist, so we don't really care to track it)

# method: `handleCurrentPlayerWinningGameIfNecessary(): void`

This is part **6** of a series of method calls that constitute attacking in Risk. This handles the win condition for 
the class World Domination game mode.

## BVA Step 1
Input: The state of the whole game board, and who is currently going. We want to see if the current player owns all  
the territories on the board.

Output: The game phase will be changed to reflect that the game is over if the player owns all the territories, 
otherwise it will remain the same. 

## BVA Step 2
Input:
- ALL territory objects: Pointer
- currently going player: Cases

Output:
- method output: N/A
- current game phase: Cases

## BVA Step 3
Input:
- ALL territory objects (Pointer):
  - A null pointer (can't set, Martin's rules)
  - A pointer to the true object
    - Want to check and see if the territory is owned by the currently going player.
      - Attackers should own [2, 42] as input here...
    - Don't care about any other fields with the territory.
- currently going player (Cases):
  - SETUP (can't set, per error checking being done beforehand)
  - YELLOW
  - PURPLE
  - ...
  - BLACK
  - The 0th, 8th possibilities (can't set, Java enum)

Output:
- current game phase (Cases):
  - Only two possible results:
    - ATTACK (Player does NOT own all territories)
    - GAME_OVER (Player OWNS all territories)
  - Any other case should be an error, but we won't set it to other things either.

## BVA Step 4
### Test 1:
Input:
- all territory objects:
  - current player OWNS 2 (attacker shouldn't be able to only own 1 after an attack)
  - current player does NOT own 40
- currently going player = PURPLE

Output:
- current game phase = ATTACK

### Test 2:
Input:
- all territory objects:
  - current player OWNS 3
  - current player does NOT own 39
- currently going player = GREEN

Output:
- current game phase = ATTACK

### Test 3:
Input:
- all territory objects:
  - current player OWNS 41
  - current player does NOT own 1
- currently going player = YELLOW

Output:
- current game phase = ATTACK

### Test 4:
Input:
- all territory objects: 
  - current player OWNS all 42
- currently going player = BLACK

Output:
- current game phase = GAME_OVER

### Test 5:
Input:
- all territory objects
  - current player OWNS all 42
- currently going player = RED

Output:
- current game phase = GAME_OVER

# method: `moveArmiesBetweenFriendlyTerritories(srcTerritory: TerritoryType, destTerritory: TerritoryType, numArmies: int): void`

## BVA Step 1
Input: The two territories that the current player wants to move armies between, and the number of armies to move 
between the two territories. This will interact with the underlying territory objects, so those must be considered as well.

We also need to consider the game phase: if we're in the ATTACK phase, this can only be done after a territory is taken 
over, and it HAS to utilize the territories that were just used in the attack.
If we're in the FORTIFY phase, the phase needs to swing back around to PLACEMENT.

Output: An exception if the movement is not able to be done (too many armies, territories are not adjacent, in the
incorrect game phase, window to split armies in the attack phase has ended, either territory is not owned by current player). 

If the movement is able to be done, then the source and destination territories will be modified.
If this movement is done in the attack phase, then the player should lose the ability to split armies between the most
RECENTLY attacked territories.
(Namely, we can just remove those two territories from being recently attacked)
If this movement is done in the fortify phase, then the fortify phase should end, and we should be on the next player's
PLACEMENT phase.

## BVA Step 2
Input:
- srcTerritory, destTerritory: Cases
- numArmies: Interval [1, num armies present in territory - 1]
- source, destination territory objects: Pointer
- current game phase: Cases
- recently attacked source, destination: Cases
- currently going player: Cases

Output:
- method output: N/A
- source, destination territory objects: Pointer
- current game phase: Cases
- recently attacked source, destination: Cases
- currently going player: Cases

## BVA Step 3
Input:
- srcTerritory, destTerritory (Cases):
  - ALASKA
  - ARGENTINA
  - ...
  - YAKUTSK
  - The 0th, 43rd possibilities (can't set, Java enum)
- numArmies (Interval):
  - \<= 0 (error case)
  - Anything in [1, num armies present in territory - 1] 
    - 1
    - num armies present in territory - 1
  - \>= num armies present in territory (error case)
- source, destination territory objects (Pointer):
  - A null pointer (can't set, Martin's rules)
  - A pointer to the true object
    - If either territory is not owned by the current player (error case)
    - Source territory doesn't have enough armies to support the move (error case)
    - Territories are not adjacent on the Risk map (error case)
- current game phase (Cases):
  - SCRAMBLE (error case)
  - SETUP (error case)
  - PLACEMENT (error case)
  - ATTACK 
  - FORTIFY
  - GAME_OVER (error case)
  - The 0th, 7th possibilities (error case)
- recently attacked source, dest (Cases):
  - ALASKA
  - ARGENTINA
  - ...
  - YAKUTSK
  - The 0th, 43rd possibilities (can't set, Java enum)
  - These NEED to match up with the inputs provided as source & destination territory
    - If we are in the attack phase and they do not, this is an error.
- currently going player (Cases):
  - Should always line up with the GameEngine's tracking.

Output:
- IllegalArgumentException if:
  - Territories were not adjacent (message: "Source and destination territory must be two adjacent territories!")
  - Territories are not owned by the current player (message: "Provided territories are not owned by the current player!")
  - `numArmies` >= num armies in territory (message: "Source territory does not have enough armies to support this movement!")
  - Called in attack, but armies are not able to be split between the two territories (message: "Cannot split armies between this source and destination!")
- IllegalStateException if:
  - Done in an incorrect game phase (message: "Friendly army movement can only be done in the ATTACK or FORTIFY phase!")
- source, destination territory objects (Pointer):
  - A null pointer (can't set, Martin's rules)
  - A pointer to the true object
    - Looking to see that `numArmies` was removed from source territory
    - And that `numArmies` was added to the destination territory
- current game phase (Cases):
  - If we started in ATTACK, keep it in ATTACK, but remove the ability to split armies
    - Assuming they had the ability to split armies at first
    - Namely, clear the recently attacked source and dest
  - If we started in FORTIFY, move to the PLACEMENT phase of the next player.
- recently attacked source, destination: Cases
  - These values NEED to be CLEARED if we are in the attack phase.
- currently going player (Cases):
  - If we are in the ATTACK phase, should remain the same player.
  - If we are in the FORTIFY phase, should ADVANCE to the next player in turn order.

## BVA Step 4
### Test 1:
Input:
- srcTerritory = ALASKA
- destTerritory = BRAZIL
- numArmies = 1
- current game phase = ATTACK
- source territory pointer = [ALASKA, numArmiesInTerritory = 2, ownedBy = PURPLE]
- dest territory pointer = [BRAZIL, numArmiesInTerritory = 3, ownedBy = PURPLE]
- recently attacked source = ALASKA
- recently attacked dest = BRAZIL
- currently going player = PURPLE

Output:
- IllegalArgumentException
  - message: "Source and destination territory must be two adjacent territories!"

### Test 2:
Input:
Input:
- srcTerritory = CONGO
- destTerritory = CHINA
- current game phase = ATTACK
- numArmies = 1
- source territory pointer = [CONGO, numArmiesInTerritory = 2, ownedBy = PURPLE]
- dest territory pointer = [CHINA, numArmiesInTerritory = 3, ownedBy = PURPLE]
- recently attacked source = CONGO
- recently attacked dest = CHINA
- currently going player = PURPLE

Output:
- IllegalArgumentException
  - message: "Source and destination territory must be two adjacent territories!"

### Test 3:
Input:
- srcTerritory = ALASKA
- destTerritory = KAMCHATKA
- current game phase = ATTACK
- numArmies = 1
- source territory pointer = [ALASKA, numArmiesInTerritory = 2, ownedBy = PURPLE]
- dest territory pointer = [KAMCHATKA, numArmiesInTerritory = 3, ownedBy = YELLOW]
- recently attacked source = ALASKA
- recently attacked dest = KAMCHATKA
- currently going player = PURPLE

Output:
- IllegalArgumentException
  - message: "Provided territories are not owned by the current player!"

### Test 4:
Input:
- srcTerritory = ALASKA
- destTerritory = KAMCHATKA
- current game phase = ATTACK
- numArmies = 1
- source territory pointer = [ALASKA, numArmiesInTerritory = 2, ownedBy = GREEN]
- dest territory pointer = [KAMCHATKA, numArmiesInTerritory = 3, ownedBy = PURPLE]
- recently attacked source = ALASKA
- recently attacked dest = KAMCHATKA
- currently going player = PURPLE

Output:
- IllegalArgumentException
  - message: "Provided territories are not owned by the current player!"

### Test 5:
Input:
- srcTerritory = ALASKA
- destTerritory = KAMCHATKA
- current game phase = ATTACK
- numArmies = 1
- source territory pointer = [ALASKA, numArmiesInTerritory = 2, ownedBy = GREEN]
- dest territory pointer = [KAMCHATKA, numArmiesInTerritory = 3, ownedBy = BLUE]
- recently attacked source = ALASKA
- recently attacked dest = KAMCHATKA
- currently going player = PURPLE

Output:
- IllegalArgumentException
  - message: "Provided territories are not owned by the current player!"

### Test 6:
Input:
- srcTerritory = ALASKA
- destTerritory = KAMCHATKA
- current game phase = ATTACK
- numArmies = 2
- source territory pointer = [ALASKA, numArmiesInTerritory = 2, ownedBy = PURPLE]
- dest territory pointer = [KAMCHATKA, numArmiesInTerritory = 3, ownedBy = PURPLE]
- recently attacked source = ALASKA
- recently attacked dest = KAMCHATKA
- currently going player = PURPLE

Output:
- IllegalArgumentException
  - message: "Source territory does not have enough armies to support this movement!"

### Test 7:
Input:
- srcTerritory = ALASKA
- destTerritory = KAMCHATKA
- current game phase = ATTACK
- numArmies = 3
- source territory pointer = [ALASKA, numArmiesInTerritory = 2, ownedBy = PURPLE]
- dest territory pointer = [KAMCHATKA, numArmiesInTerritory = 3, ownedBy = PURPLE]
- recently attacked source = ALASKA
- recently attacked dest = KAMCHATKA
- currently going player = PURPLE

Output:
- IllegalArgumentException
  - message: "Source territory does not have enough armies to support this movement!"

### Test 8:
Input:
- srcTerritory = ALASKA
- destTerritory = KAMCHATKA
- current game phase = ATTACK
- numArmies = 12
- source territory pointer = [ALASKA, numArmiesInTerritory = 2, ownedBy = PURPLE]
- dest territory pointer = [KAMCHATKA, numArmiesInTerritory = 3, ownedBy = PURPLE]
- recently attacked source = ALASKA
- recently attacked dest = KAMCHATKA
- currently going player = PURPLE

Output:
- IllegalArgumentException
  - message: "Source territory does not have enough armies to support this movement!"

### Test 9:
Input:
- srcTerritory = ALASKA
- destTerritory = KAMCHATKA
- current game phase = SCRAMBLE
- numArmies = 1
- source territory pointer = [ALASKA, numArmiesInTerritory = 2, ownedBy = PURPLE]
- dest territory pointer = [KAMCHATKA, numArmiesInTerritory = 3, ownedBy = PURPLE]
- recently attacked source = ALASKA
- recently attacked dest = KAMCHATKA
- currently going player = PURPLE

Output:
- IllegalStateException
  - message: "Friendly army movement can only be done in the ATTACK or FORTIFY phase!"

### Test 10:
Input:
- srcTerritory = ALASKA
- destTerritory = KAMCHATKA
- current game phase = SETUP
- numArmies = 1
- source territory pointer = [ALASKA, numArmiesInTerritory = 2, ownedBy = PURPLE]
- dest territory pointer = [KAMCHATKA, numArmiesInTerritory = 3, ownedBy = PURPLE]
- recently attacked source = ALASKA
- recently attacked dest = KAMCHATKA
- currently going player = PURPLE

Output:
- IllegalStateException
  - message: "Friendly army movement can only be done in the ATTACK or FORTIFY phase!"

### Test 11:
Input:
- srcTerritory = ALASKA
- destTerritory = KAMCHATKA
- current game phase = PLACEMENT
- numArmies = 1
- source territory pointer = [ALASKA, numArmiesInTerritory = 2, ownedBy = PURPLE]
- dest territory pointer = [KAMCHATKA, numArmiesInTerritory = 3, ownedBy = PURPLE]
- recently attacked source = ALASKA
- recently attacked dest = KAMCHATKA
- currently going player = PURPLE

Output:
- IllegalStateException
  - message: "Friendly army movement can only be done in the ATTACK or FORTIFY phase!"

### Test 12:
Input:
- srcTerritory = ALASKA
- destTerritory = KAMCHATKA
- current game phase = ATTACK
- numArmies = 1
- source territory pointer = [ALASKA, numArmiesInTerritory = 2, ownedBy = GREEN]
- dest territory pointer = [KAMCHATKA, numArmiesInTerritory = 3, ownedBy = GREEN]
- recently attacked source = ALASKA
- recently attacked dest = NORTHWEST_TERRITORIES
- currently going player = GREEN

Output:
- IllegalArgumentException
  - message: "Cannot split armies between this source and destination!"

### Test 13:
Input:
- srcTerritory = ALASKA
- destTerritory = KAMCHATKA
- current game phase = ATTACK
- numArmies = 1
- source territory pointer = [ALASKA, numArmiesInTerritory = 2, ownedBy = GREEN]
- dest territory pointer = [KAMCHATKA, numArmiesInTerritory = 3, ownedBy = GREEN]
- recently attacked source = IRKUTSK
- recently attacked dest = KAMCHATKA
- currently going player = GREEN

Output:
- IllegalArgumentException
  - message: "Cannot split armies between this source and destination!"

### Test 14:
Input:
- srcTerritory = ALASKA
- destTerritory = KAMCHATKA
- current game phase = ATTACK
- numArmies = 1
- source territory pointer = [ALASKA, numArmiesInTerritory = 2, ownedBy = GREEN]
- dest territory pointer = [KAMCHATKA, numArmiesInTerritory = 3, ownedBy = GREEN]
- recently attacked source = UKRAINE
- recently attacked dest = URAL
- currently going player = GREEN

Output:
- IllegalArgumentException
  - message: "Cannot split armies between this source and destination!"

### Test 15:
Input:
- srcTerritory = ALASKA
- destTerritory = KAMCHATKA
- current game phase = ATTACK
- numArmies = 1
- source territory pointer = [ALASKA, numArmiesInTerritory = 2, ownedBy = PURPLE]
- dest territory pointer = [KAMCHATKA, numArmiesInTerritory = 3, ownedBy = PURPLE]
- recently attacked source = ALASKA
- recently attacked dest = KAMCHATKA
- currently going player = PURPLE

Output:
- source territory pointer = [ALASKA, numArmiesInTerritory = 1, ownedBy = PURPLE]
- dest territory pointer = [KAMCHATKA, numArmiesInTerritory = 4, ownedBy = PURPLE]
- current game phase = ATTACK
- recently attacked source, destination = NULL (only time we'll use this!)
- currently going player = PURPLE

### Test 16:
Input:
- srcTerritory = ALASKA
- destTerritory = KAMCHATKA
- current game phase = ATTACK
- numArmies = 2
- source territory pointer = [ALASKA, numArmiesInTerritory = 3, ownedBy = PURPLE]
- dest territory pointer = [KAMCHATKA, numArmiesInTerritory = 3, ownedBy = PURPLE]
- recently attacked source = ALASKA
- recently attacked dest = KAMCHATKA
- currently going player = PURPLE

Output:
- source territory pointer = [ALASKA, numArmiesInTerritory = 1, ownedBy = PURPLE]
- dest territory pointer = [KAMCHATKA, numArmiesInTerritory = 5, ownedBy = PURPLE]
- current game phase = ATTACK
- recently attacked source, destination = NULL (only time we'll use this!)
- currently going player = PURPLE

### Test 17:
Input:
- srcTerritory = ALASKA
- destTerritory = KAMCHATKA
- current game phase = FORTIFY
- numArmies = 1
- source territory pointer = [ALASKA, numArmiesInTerritory = 2, ownedBy = PURPLE]
- dest territory pointer = [KAMCHATKA, numArmiesInTerritory = 3, ownedBy = PURPLE]
- recently attacked source = ANY (likely null)
- recently attacked dest = ANY (likely null)
- currently going player = PURPLE

Output:
- source territory pointer = [ALASKA, numArmiesInTerritory = 1, ownedBy = PURPLE]
- dest territory pointer = [KAMCHATKA, numArmiesInTerritory = 4, ownedBy = PURPLE]
- current game phase = PLACEMENT
- recently attacked source, destination = NULL (only time we'll use this!)
- currently going player = GREEN
  - imagine that player order was [PURPLE, GREEN, ...]

# method: `forceGamePhaseToEnd(): void`

## BVA Step 1
Input: The current phase that the game is in, who's turn it is in the game.

If we're in the FORTIFY phase, we need to know if the current player can claim a card, and how many territories
the next going player owns given the current state of the game (so we can get their bonus army count for PLACEMENT)

If we're in the ATTACK phase, the player cannot forcibly end the phase if they are holding \> 5 cards. This is the 
forced trade-in threshold during the attacking portion of someone's turn in Risk.

Output: An updated form of the current game phase, who's turn it is. An IllegalStateException if this
is called from a phase that the player cannot forcibly end; and must do it via game actions instead.

Additionally, we have to respect some rules regarding transitioning phases in Risk. 

When you move out of the ATTACK phase, you lose the option to "split" armies and instead of move armies using the one 
movement in FORTIFY.

When you move out of the FORTIFY phase, you claim a card (given that one is available, player has earned one), 
then move on to the next player in turn order, and give them the bonus armies they should earn based on how many 
territories they own. This means we need access to the next player's pointer object. So, we want players to be able to
gain cards, next player to gain armies, etc.

## BVA Step 2
Input: 
- current game phase (Cases)
- currently going player (Cases)
- current player object (Pointer)
- ability to claim card (Boolean)
- current state of all territories (Pointer)

Output:
- current game phase (Cases)
- currently going player (Cases)
- next player object (Pointer)
- recently attacked source / destination (Cases)
- ability to claim card (Boolean)
- IllegalStateException 

## BVA Step 3
Input:
- current game phase (Cases):
  - SCRAMBLE (error case)
  - SETUP (error case)
  - PLACEMENT (error case)
  - ATTACK
  - FORTIFY
  - GAME_OVER (error case)
  - The 0th, 7th possibilities (can't set, Java enum)
- currently going player (Cases):
  - SETUP (error case; should be handled by this point)
  - YELLOW
  - PURPLE
  - ...
  - BLACK
  - The 0th, 8th possibilities (can't set, Java enum)
- current player object (Pointer):
  - A null pointer (can't set, Martin's rules)
  - A pointer to the true object
    - Care about the amount of cards they hold
    - If they own \> 5 cards in the ATTACK phase, they're above the forced turn in threshold, so they MUST turn in cards before they can end the ATTACK phase.
- Ability to claim card (Boolean):
  - 0 (false)
  - 1 (true)
  - Something other than true/false (can't set)
- current state of all territories (Pointer):
  - A null pointer (can't set, Martin's rules)
  - A pointer to the true object
    - Only care about if the next going player owns the territory or not.

Output:
- IllegalStateException if:
  - called from game phases:
    - SCRAMBLE
    - SETUP
    - PLACEMENT
    - GAME_OVER
- current game phase (Cases):
  - FORTIFY, if we started in ATTACK
  - PLACEMENT, if we started in FORTIFY
    - Update the currently going player, too.
- currently going player (Cases):
  - The exact same player we had before calling this method if in ATTACK
  - The next player to go in turn order if we called in FORTIFY
    - Note that we'll also draw the current player's card
      - This BVA will be elaborated on in a different method.
- recently attacked dest/source (Cases):
  - Should be set to NULL if we're in the ATTACK phase
  - Should already be NULL if we are in the FORTIFY phase
  - Note that this is about the only place we're using NULL anywhere
- Next player object (Pointer):
  - If we are in the FORTIFY phase:
    - Update the number of armies they have to place
  - Nothing if we are in the ATTACK phase
  - Note that "next" player object means NEXT as we come INTO the method; not the "next" after we change players already.
- Current player object (Pointer):
  - If we are in the FORTIFY phase:
    - Claim a card for them if possible
  - Nothing if we are in the ATTACK phase
- Ability to claim card (Boolean):
  - Set to false if we are in the FORTIFY phase
  - Remains the same as it was for input if we were in ATTACK

## BVA Step 4
### Test 1:
Input:
- current game phase = SCRAMBLE
- currently going player = GREEN
- current player object = [GREEN, |ownedCards| = 3]
- ability to claim card = false (does not matter; wrong phase)
- current state of all territories (does not matter; wrong phase)

Output:
- IllegalStateException
  - message: "Cannot forcibly end this game phase!"

### Test 2:
Input:
- current game phase = SETUP
- currently going player = GREEN
- current player object = [GREEN, |ownedCards| = 3]
- ability to claim card = false (does not matter; wrong phase)
- current state of all territories (does not matter; wrong phase)

Output:
- IllegalStateException
  - message: "Cannot forcibly end this phase!"

### Test 3:
Input:
- current game phase = PLACEMENT
- currently going player = GREEN
- current player object = [GREEN, |ownedCards| = 3]
- ability to claim card = false (does not matter; wrong phase)
- current state of all territories (does not matter; wrong phase)

Output:
- IllegalStateException
  - message: "Cannot forcibly end this phase!"

### Test 4:
Input:
- current game phase = GAME_OVER
- currently going player = GREEN
- current player object = [GREEN, |ownedCards| = 3]
- ability to claim card = false (does not matter; wrong phase)
- current state of all territories (does not matter; wrong phase)

Output:
- IllegalStateException
  - message: "Cannot forcibly end this phase!"

### Test 5:
Input:
- current game phase = ATTACK
- currently going player = GREEN
- current player object = [GREEN, |ownedCards| = 6]
- ability to claim card = false (does not matter; wrong phase)
- current state of all territories (does not matter; wrong phase)

Output:
- IllegalStateException
  - message: "Cannot forcibly end the ATTACK phase while current player is holding > 5 cards!"

### Test 6:
Input:
- current game phase = ATTACK
- currently going player = GREEN
- current player object = [GREEN, |ownedCards| = 7]
- ability to claim card = false (does not matter; wrong phase)
- current state of all territories (does not matter; wrong phase)

Output:
- IllegalStateException
  - message: "Cannot forcibly end the ATTACK phase while current player is holding > 5 cards!"

### Test 7:
Input:
- current game phase = ATTACK
- currently going player = RED
- current player object = [RED, |ownedCards| = 5]
- ability to claim card = false
- current state of all territories (does not matter; wrong phase)

Output:
- current game phase = FORTIFY
- currently going player = RED
- recently attacked dest/source = NULL
- current, next player objects: No change
- ability to claim card = false

### Test 8:
Input:
- current game phase = ATTACK
- currently going player = YELLOW
- current player object = [YELLOW, |ownedCards| = 5]
- ability to claim card = true 
- current state of all territories (does not matter; wrong phase)

Output:
- current game phase = FORTIFY
- currently going player = YELLOW
- recently attacked dest/source = NULL
- current, next player objects: No change
- ability to claim card = true

### Test 9:
Input:
- current game phase = FORTIFY
- currently going player = GREEN
- current player object = [GREEN, |ownedCards| = 5]
- ability to claim card = false 
- current state of all territories 
  - black should earn 3 new armies (owns 1 territory)

Output:
- current game phase = PLACEMENT
- currently going player = BLACK
  - player order might've been [... -> GREEN -> BLACK -> ...]
- recently attacked dest/source = NULL
- next player object = [BLACK, numArmiesToPlace = 3]
- current player object = [GREEN, no change in cards]
- ability to claim card = false

### Test 10:
Input:
- current game phase = FORTIFY
- currently going player = BLACK
- current player object = [BLACK, |ownedCards| = 4]
- ability to claim card = false
- current state of all territories 
  - purple should earn 5 armies (owns all of OCEANIA, 4 territories)

Output:
- current game phase = PLACEMENT
- currently going player = PURPLE
  - player order might've been [... -> GREEN -> BLACK -> PURPLE -> ...]
- recently attacked dest/source = NULL
- next player object = [PURPLE, numArmiesToPlace = 5]
- current player object = [BLACK, no change in cards]
- ability to claim card = false

### Test 11:
Input:
- current game phase = FORTIFY
- currently going player = BLACK
- current player object = [BLACK, |ownedCards| = 3]
- ability to claim card = true
- current state of all territories
  - purple should earn 6 armies (owns all of AFRICA, 6 territories)
  - Consult the calculatePlacementPhaseArmiesForCurrentPlayer BVA if you want more details

Output:
- current game phase = PLACEMENT
- currently going player = PURPLE
  - player order might've been [... -> GREEN -> BLACK -> PURPLE -> ...]
- recently attacked dest/source = NULL
- next player object = [PURPLE, numArmiesToPlace = 7]
- current player object = [BLACK, old card set + new card of [BRAZIL, ARTILLERY] ]
  - We don't really care what the particular card is, we just care that they gained one; assuming one is available.
  - See BVA for claimCardForCurrentPlayerIfPossible to know more details on if they can get a card or not.
    - This is the method that'll be invoked to handle gaining cards.
- ability to claim card = false

# method: `claimCardForCurrentPlayerIfPossible(): void`

Note: this method will automatically be called at the end of the single turn cycle. Once a player ends their fortify
phase, they should automatically receive a card given they captured a territory.

## BVA Step 1
Input: The underlying state of if a player is able to claim a card or not (AKA, they took over a territory this turn),
as well as the current cards the player owns and who the current player is. Additionally, we need to consider the state
of the Card Deck (if there are no cards to draw, tough luck!)

Output: If the player is unable to claim a card, nothing will happen. If they are, a card will be added to their
collection from the deck of available cards. If there are cards to claim, the Deck will change. If there are none,
nothing happens.

## BVA Step 2
Input:
- currently going player: Cases
- ability to claim a card: Boolean
- current player object: Pointer
- risk card deck: Pointer

Output:
- current player object: Pointer
- risk card deck: Pointer

## BVA Step 3
Input:
- currently going player (Cases):
  - SETUP (error case, should not happen at this stage of the game)
  - YELLOW
  - PURPLE
  - ...
  - BLACK
  - The 0th, 8th possibilities (can't set, Java enum)
- ability to claim a card (Boolean):
  - 0 (false)
  - 1 (true -> means claim a card if there are still some available!)
  - A value other than true/false (can't set)
- current player object (Pointer):
  - A null pointer (can't set, Martin's rules)
  - A pointer to the true object
    - Want to know what cards they currently possess as their collection might change as a result of this method.
- risk card deck (Pointer):
  - A null pointer (can't set, Martin's rules)
  - A pointer to the true object
    - if the deck has no more cards to give, we can't give a card to the player.

Output:
- current player object (Pointer):
  - Assuming claiming a card was possible, the player should have an additional card.
- risk card deck (Pointer):
  - Removes the drawn card from the deck if there were cards to begin with
  - Remains empty if there weren't any
- ability to claim card (Boolean):
  - Only needs to be set to false if the person did claim a card, we'll set it to false regardless.

## BVA Step 4
### Test 1:
Input:
- currently going player = GREEN
- ability to claim a card = false
- current player object = [GREEN, ownedCards = {} ]
- risk card deck = [ all possible risk cards ]

Output:
- current player object = [GREEN, ownedCards = {} ]
- risk card deck = [ all possible owned cards ]
- ability to claim a card = false

### Test 2:
Input:
- currently going player = PURPLE
- ability to claim a card = false
- current player object = [PURPLE, ownedCards = {} ]
- risk card deck = [ all possible risk cards ]

Output:
- current player object = [PURPLE, ownedCards = {} ]
- risk card deck = [ all possible owned cards ]
- ability to claim a card = false

### Test 3:
Input:
- currently going player = PURPLE
- ability to claim a card = false
- current player object = [PURPLE, ownedCards = { wild card } ]
- risk card deck = [ all risk cards - purple's owned ones ]

Output:
- current player object = [PURPLE, ownedCards = { wild card } ]
- risk card deck = [ all risk cards - purple's owned ones ] 
- ability to claim a card = false

### Test 4:
Input:
- currently going player = PURPLE
- ability to claim a card = false
- current player object = [PURPLE, ownedCards = { wild card, territory card = [ALASKA, INFANTRY] } ]
- risk card deck = [ 1 remaining card; not including the ones purple owns ]

Output:
- current player object = [PURPLE, ownedCards = { wild card, territory card = [ALASKA, INFANTRY] } ]
- risk card deck = [ 1 remaining card; not including the ones purple owns ]
- ability to claim a card = false

### Test 5:
Input:
- currently going player = PURPLE
- ability to claim a card = false
- current player object = [PURPLE, ownedCards = { wild card, territory card = [ALASKA, INFANTRY] } ]
- risk card deck = [ no remaining cards ]

Output:
- current player object = [PURPLE, ownedCards = { wild card, territory card = [ALASKA, INFANTRY] } ]
- risk card deck = [ no remaining cards ]
- ability to claim a card = false

### Test 6:
Input:
- currently going player = PURPLE
- ability to claim a card = false
- current player object = [PURPLE, ownedCards = { wild card, territory card = [ALASKA, INFANTRY] } ]
- risk card deck = [ all risk cards - purple's owned ones ]

Output:
- current player object = [PURPLE, ownedCards = { wild card, territory card = [ALASKA, INFANTRY] } ]
- risk card deck = [ all risk cards - purple's owned ones ]
- ability to claim a card = false

### Test 7:
Input:
- currently going player = YELLOW
- ability to claim a card = true
- current player object = [YELLOW, ownedCards = {} ]
- risk card deck = [ all risk cards ]

Output:
- current player object = [YELLOW, ownedCards = { territory card = [SOUTHERN_EUROPE, CAVALRY] } ]
- risk card deck = [ all risk cards - southern europe card]
- ability to claim a card = false

### Test 8:
Input:
- currently going player = YELLOW
- ability to claim a card = true
- current player object = [YELLOW, ownedCards = { territory card = [BRAZIL, ARTILLERY], territory card = [IRKUTSK, ARTILLERY] }
- risk card deck = [only southern europe card]

Output:
- current player object = [YELLOW, ownedCards = { territory card = [BRAZIL, ARTILLERY], territory card = [IRKUTSK, ARTILLERY], territory card = [SOUTHERN_EUROPE, CAVALRY] } ]
- risk card deck = []
- ability to claim a card = false

### Test 9:
Input:
- currently going player = YELLOW
- ability to claim a card = true
- current player object = [YELLOW, ownedCards = { territory card = [BRAZIL, ARTILLERY], territory card = [IRKUTSK, ARTILLERY] }
- risk card deck = []

Output:
- current player object = [YELLOW, ownedCards = { territory card = [BRAZIL, ARTILLERY], territory card = [IRKUTSK, ARTILLERY] } ]
  - They just don't receive a card since there isn't a new one to get.
- risk card deck = []
- ability to claim a card = false

# method: `getCardsOwnedByPlayer(player: PlayerColor): Set<Card>`

## BVA Step 1
Input: The provided player color and associated underlying Player object, which contains the set of cards owned by the player

Output: A collection of cards currently in the player's possession

## BVA Step 2
Input: Cases, Pointer

Output: Collection

## BVA Step 3
Input:
- Cases:
  - BLACK
  - RED
  - YELLOW
  - BLUE
  - GREEN
  - PURPLE
  - SETUP (error case)
- Pointer:
  - Null pointer (can't set, Martin's rules)
  - A pointer to a Player object
    - We care about the cards they own

Output:
- Empty set of cards
- Set containing 1 card
- Set containing > 1 card
- Set containing all 44 cards
- Set containing both wild cards
- Set containing 2 of the same territory cards (not possible)

## BVA Step 4
### Test value 1
Input: BLACK, [BLACK, ownedCards = {}]

Output: {}
#### Repeat for each PlayerColor
### Test value 2
Input: BLACK, [BLACK, ownedCards = { [ALASKA, INFANTRY] }]

Output: { [ALASKA, INFANTRY] }
#### Repeat for each PlayerColor and each card
### Test value 3
Input: BLACK, [BLACK, ownedCards = { [ALASKA, INFANTRY], [BRAZIL, ARTILLERY] }]

Output: { [ALASKA, INFANTRY], [BRAZIL, ARTILLERY] }
#### Repeat for each PlayerColor and each card
### Test value 4
Input: BLACK, [BLACK, ownedCards = { all 44 cards }]

Output: { all 44 cards }
#### Repeat for each PlayerColor
### Test value 5
Input: BLACK, [BLACK, ownedCards = { WILD, WILD }]

Output: { WILD, WILD }
#### Repeat for each PlayerColor