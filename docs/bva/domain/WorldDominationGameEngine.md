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
    - Note that they may also change it manually, if they so desire.

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

## TODO: Elaborate more once attack/placement phase is being developed!

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
  - The 3rd possibility (PLACEMENT)
  - ...
  - The 6th possibility (GAME_OVER)
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
        - FORTIFY
        - ATTACK(?)
- Player object: Pointer
  - The number of armies they have to place should be updated
  - Territories held should be updated if in SCRAMBLE (maybe attack too).
- Game Phase: Cases
  - SCRAMBLE (advancing out requires all territories to be claimed)
  - SETUP (advancing out requires all setup armies to be placed)
  - PLACEMENT (advancing requires all earned armies to be placed, or a manual phase end)
  - ATTACK
  - FORTIFY
  - GAME_OVER

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

### TODO: ATTACK PHASE

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
- Player object: Pointer (we care about what territories and cards they own)
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
  - ATTACK
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
- Player object = [Color = RED, ownedCards = [ Wild card, Wild card, [ALASKA, INFANTRY] ] ]
- GamePhase = PLACEMENT

Output:
- IllegalStateException
  - message: "Could not trade in cards: \< trade in parser error message \>"
### I'm using a more generic error message here so that the details given in TradeInParser can shine through.

### Test 2:
Input:
- selectedCardsToBeTradedIn = [ Wild card, [ALASKA, INFANTRY], [BRAZIL, ARTILLERY] ]
- currentPlayer = BLUE
- Player object = [Color = BLUE, ownedCards = [] ]
- GamePhase = PLACEMENT (test with all phases besides ATTACK/PLACEMENT here)

Output:
- IllegalArgumentException
  - message: "Player doesn't own all the selected cards!"
### Test 3:
Input:
- selectedCardsToBeTradedIn = [ Wild card, [ALASKA, INFANTRY], [BRAZIL, ARTILLERY] ]
- currentPlayer = GREEN
- Player object = [Color = GREEN, ownedCards = [ Wild card, [ALASKA, INFANTRY], [BRAZIL, ARTILLERY]  ]  ]
- GamePhase = SETUP

Output:
- IllegalStateException
  - message: "Can only trade in cards during the PLACEMENT or ATTACK phases"
### Test 4:
Input:
- selectedCardsToBeTradedIn = [ Wild card, [ALASKA, INFANTRY], [BRAZIL, ARTILLERY] ]
- currentPlayer = BLUE
- Player object = [Color = BLUE, numArmiesToPlace = 5, ownedCards = [Wild card, [ALASKA, INFANTRY], [BRAZIL, ARTILLERY] ] ]
  - Player owns: {ALASKA, BRAZIL}
  - Let this be the first set traded in
- GamePhase = PLACEMENT

Output:
- Method output = {ALASKA, BRAZIL}
- Player object = [Color = BLUE, numArmiesToPlace = 9, ownedCards = [] ]
- GamePhase = PLACEMENT
### Test 5:
Input:
- selectedCardsToBeTradedIn = [Wild card, [ALASKA, INFANTRY], [BRAZIL, ARTILLERY]]
- currentPlayer = PURPLE
- Player object = [Color = PURPLE, numArmiesToPlace = 0, ownedCards = [Wild card, [ALASKA, INFANTRY], [BRAZIL, ARTILLERY] ] ]
  - Player owns: {YAKUTSK, BRAZIL, JAPAN}
  - Let this be the second set traded in
- GamePhase = ATTACK

Output:
- Method output: {BRAZIL}
- Player object = [Color = PURPLE, numArmiesToPlace = 6, ownedCards = [] ]
- GamePhase = PLACEMENT
### Test 6:
Input:
- selectedCardsToBeTradedIn = {Wild card, [ALASKA, INFANTRY], [BRAZIL, ARTILLERY]}
- currentPlayer = PURPLE
- Player object = [Color = PURPLE, numArmiesToPlace = 0, ownedCards = [Wild card, [ALASKA, INFANTRY], [BRAZIL, ARTILLERY] ] ]
  - Player owns: {EASTERN_AUSTRALIA}
  - Let this be the second set traded in
- GamePhase = ATTACK

Output:
- Method output: {}
- Player object = [Color = PURPLE, numArmiesToPlace = 6, ownedCards = {}]
- GamePhase = PLACEMENT