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
  - amountOfPlayers = 3
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
    - SCRAMBLE, SETUP, PLACEMENT (if numArmiesToPlace for the player IS 0)

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
  - The 1st possibility (SETUP - interesting case; restricts placement to 1)
  - The 2nd possibility (NEUTRAL)
  - ...
  - The 8th possibility (PURPLE)
  - The 0th, 9th possibilities (can't set)
- Game Phase (Cases):
  - The 1st possibility (SCRAMBLE)
  - The 2nd possibility (SETUP)
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
  - Some value other than 0 or 1 (namely, exceptions - for when the amount of troops is invalid, or in the wrong phase)
  - Some true value other than 0 or 1 (can't set)
- Territory object: Pointer
  - Null pointer (can't set, per Martin's suggestions)
  - A pointer to the true object
    - Mostly concerned about checking two things in our Territory object:
      - The PlayerColor should be updated if we're in the SCRAMBLE phase
      - The army count should be updated regardless of phase, if possible
- Player object: Pointer
  - The number of armies they have to place should be updated
  - Territories held should be updated if in SCRAMBLE (maybe attack too).

## BVA Step 4

### SCRAMBLE PHASE

### Test 1:
- Input:
  - territory = CONGO
  - numArmies = num player earned this turn + 1
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

### Scramble phase integration tests

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

### SETUP PHASE

### Test 7:
- Input:
  - territory = ALASKA
  - numArmies = -1
  - Territory owner = current player
  - Game Phase = SETUP
  - Current player = [Color = BLUE, numArmiesToPlace = 12, territories owned = {ALASKA}]
- Output:
  - IllegalArgumentException
    - message: "Cannot place anything other than 1 army in a territory during setup phase"
### Test 8:
- Input:
  - territory = ARGENTINA
  - numArmies = 0
  - Territory owner = current player
  - Game Phase = SETUP
  - Current player = [Color = PURPLE, numArmiesToPlace = 6]
- Output:
  - IllegalArgumentException
    - message: "Cannot place anything other than 1 army in a territory during setup phase"
### Test 9: 
- Input:
  - territory = ALASKA
  - numArmies = 1
  - Territory owner = RED
  - Game Phase = SETUP
  - Current player = [Color = BLUE, numArmiesToPlace = 7]
- Output:
  - IllegalArgumentException
    - message: "Cannot place armies on a territory you do not own"
### Test 10:
- Input:
  - territory = ALASKA
  - numArmies = 1
  - Territory owner = current player
  - Game Phase = SETUP
  - Current player = [Color = YELLOW, numArmiesToPlace = 0]
- Output:
  - IllegalArgumentException
    - message: "Player does not have enough armies to place!"
### Test 10:
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
### Test 11:
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

### TODO: ATTACK PHASE
