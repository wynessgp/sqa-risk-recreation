# method: `initializePlayersList(playerOrder: List<PlayerColor>, amountOfPlayers: int): boolean`

## BVA Step 1
Input: The amount of players to start the game with, and their respective turn order via their selected PlayerColor

Output: A yes/no answer whether we could successfully initialize the game's relevant player storage
given the respective order and number of players

## BVA Step 2
Input:
- playerOrder: Collection
- amountOfPlayers: Interval [2, 6]

Output: 
- Boolean (actual return statement)
- Collection (underlying player storage)

## BVA Step 3
Input: 
- playerOrder: Collection
  - An empty collection (error case)
  - A collection with 1 element (error case)
  - A collection with \> 1 element
  - A collection with 6 elements (max size)
  - A collection with the same PlayerColor twice (duplicates - error case)
  - A collection with all unique PlayerColors
  - A collection with a size not equal to the amountOfPlayers (error case)
- amountOfPlayers: Interval
  - 1 (error case)
  - 2
  - Normal value in range (4)
  - 6
  - 7 (error case)

Output:
- Actual return statement (Boolean)
  - 0 (can't set)
  - 1
  - Some value other than true or false
    - Exception:
      - The amount of players is invalid (not in [2, 6])
      - The input list is malformed (duplicates, not the same length as numPlayers, contains SETUP)
  - Some other true value (can't set)
- Underlying player storage (Collection)
  - Must be the same size as playerOrder
  - Must respect the arrangement of playerOrder

## BVA Step 4

For the non-error test cases, I will indicate the underlying player storage via their assigned PlayerColor

So the output would be something like: Collection = [RED, YELLOW], etc.

### Test 1:
- Input: 
  - amountOfPlayers = 1
  - playerOrder = []
- Output: IllegalArgumentException
  - message: "amountOfPlayers is not within: [2, 6]"
### Test 2:
- Input: 
  - amountOfPlayers = 7
  - playerOrder = []
- Output: IllegalArgumentException
  - message: "amountOfPlayers is not within: [2, 6]"
### Test 3:
- Input:
  - amountOfPlayers = 7
  - playerOrder = [BLUE, BLUE, SETUP, PURPLE, YELLOW, YELLOW, BLACK]
- Output: IllegalArgumentException
  - message: "amountOfPlayers is not within: [2, 6]"
### Test 4:
- Input:
  - amountOfPlayers = 2
  - playerOrder = []
- Output: IllegalArgumentException
  - message: "Size mismatch between playerOrder: 0 and amountOfPlayers: 2"
### Test 5:
- Input: 
  - amountOfPlayers = 2
  - playerOrder = [YELLOW, BLUE, PURPLE]
- Output: IllegalArgumentException
  - message: "Size mismatch between playerOrder: 3 and amountOfPlayers: 2"
### Test 6:
- Input:
  - amountOfPlayers = 3
  - playerOrder = [YELLOW, YELLOW, SETUP]
- Output: IllegalArgumentException
  - message: "Player order contains duplicate entries"
### Test 7:
- Input:
  - amountOfPlayers = 4
  - playerOrder = [RED, YELLOW, BLUE, RED]
- Output: IllegalArgumentException
  - message: "Player order contains duplicate entries"
### Test 8: 
- Input:
  - amountOfPlayers = 3
  - playerOrder = [RED, BLUE, SETUP]
- Output: IllegalArgumentException
  - message: "Player order contains SETUP as one of the players"
### Test 9:
- Input:
  - amountOfPlayers = 4
  - playerOrder = [RED, BLUE, PURPLE, BLACK]
- Output: 
  - Function output: 1 (true)
  - Collection = [RED, BLUE, PURPLE, BLACK]
### Test 10:
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

Output: A yes/no answer whether we were able to successfully modify each player's list to 
their setup-determined army counts.

## BVA Step 2
Input: 
- Underlying PlayerColor storage: Collection

Output: 
- Player object storage: Collection
- Boolean

## BVA Step 3
Input: 
- PlayerColor objects (Collection):
  - An empty collection (error case)
  - A collection with 2 elements -> interesting case, need to set up a "neutral" player too
  - A collection with [3, 6] elements -> ensure we give players the correct number of armies.

Note the lack of other error cases above - we expect whoever is calling us to have made the player collection, otherwise
the other methods haven't done their job.

Output: 
- function output: (Boolean)
  - 0 (can't set)
  - 1
  - Something other than true/false (Exceptions)
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
  - Player objects = [Red, Yellow]
- Output:
  - 1 (true)
  - Collection = [40, 40, 40] 
    - We account for creating the "NEUTRAL" player.
### Test 3:
- Input: 
  - Player objects = [Red, Yellow, Purple]
- Output:
  - 1 (true) 
  - Collection = [35, 35, 35]
### Test 4:
- Input:
  - Player objects = [Red, Yellow, Purple, Green]
- Output:
  - 1 (true)
  - Collection = [30, 30, 30, 30]
### Test 5:
- Input:
  - Player objects = [Red, Yellow, Purple, Green, Black]
- Output:
  - 1 (true)
  - Collection = [25, 25, 25, 25, 25]
### Test 6:
- Input:
  - Player objects = [Red, Yellow, Purple, Green, Black, Blue]
- Output:
  - 1 (true)
  - Collection = [20, 20, 20, 20, 20, 20]

# method: `checkIfTerritoryIsClaimed(territory: TerritoryType): boolean`

## BVA Step 1
Input: A territory that's in question whether it has been claimed or not (meaning, SETUP still owns it),
the state of who owns the territory

Output: A yes/no answer whether the territory has been claimed or not
- "No" here means that SETUP owns it
- "Yes" here means that another player owns it (could be NEUTRAL, etc.)

## BVA Step 2
Input: 
- territory: Cases
- player who owns the territory: Cases

Output: Boolean

## BVA Step 3
Input:
- Territory (Cases):
  - The 1st possibility (ALASKA)
  - The 2nd possibility (ARGENTINA)
  - ...
  - The 42nd possibility (YAKUTSK)
  - The 0th, 43rd possibilities (can't set)
- Player owning the territory (Cases):
  - The 1st possibility (SETUP - this is the one we're interested in)
  - The 2nd possibility (NEUTRAL)
  - ...
  - The 8th possibility (PURPLE)
  - The 0th, 9th possibilities (can't set)

Output:
- 0 (meaning, SETUP owns the territory)
- 1 (meaning, SETUP does NOT own the territory)

## BVA Step 4
### Test 1:
- Input: 
  - Territory = ALASKA
  - Player in control = SETUP
- Output = 0 (false)
### Test 2:
- Input:
  - Territory = ARGENTINA
  - Player in control = SETUP
- Output = 0 (false)
### Test 3:
- Input:
  - Territory = ALASKA
  - Player in control = BLUE
- Output = 1 (true)
### Test 4:
- Input: 
  - Territory = ALASKA
  - Player in control = NEUTRAL
- Output = 1 (true)
### Test 5:
- Input:
  - Territory = ARGENTINA
  - Player in control = BLUE
- Output = 1 (true)

consider all combinations here

# method: `validatePlayerOwnsTerritory(territory: TerritoryType, player: PlayerColor): boolean`

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
  - The 1st possibility (SETUP - not considered, will be handled by checkIfTerritoryIsClaimed)
  - The 2nd possibility (NEUTRAL)
  - The 3rd possibility (BLUE)
  - ...
  - The 8th possibility (PURPLE)
  - The 0th or 9th possibility (can't set)
- Underlying territory object:
  - Null pointer (can't set, Martin's rules)
  - A pointer to the actual object

Output:
- 0 
- 1

## BVA Step 4
### Test 1:
Input:
- territory = ALASKA
- player = BLUE
- Territory object = [valid pointer, type = ALASKA, player = NEUTRAL]

Output = 0 (false)

### Test 2:
Input:
- territory = YAKUTSK
- player = RED
- Territory object = [valid pointer, type = ALASKA, player = BLUE]

Output: 0 (false)
### Test 3:
Input:
- territory = ALASKA
- player = BLUE
- Territory object = [valid pointer, type = ALASKA, player = BLUE]

Output: 1 (true)
### And many more...consider all combinations

# method: `placeNewArmiesInTerritory(territory: TerritoryType, numArmies: int): boolean`
Note: this method will seem like it has an incredible amount of responsibility based on the test cases below.
It will be split up into separate helper functions, so much of the enumerated tests here will have their logic in code
handled elsewhere, and split by phase.

## BVA Step 1
Input: The respective territory the current user wants to place their armies in, as well as the
number of new armies to place there. 

The state of who owns the respective territory is also taken into account, in addition to the current phase of the game.

Output: A yes/no answer whether the armies were placed successfully; an exception if they were not able to be 
placed successfully (i.e. another player owns the territory).

## BVA Step 2
Input:
- territory: Cases
- numNewArmies: Counts [1, armies earned on player's turn]
- State of who owns the territory: Cases
- Game Phase: Cases 

Output: Boolean

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

Output: Boolean
- 0 (can't set)
- 1 (when the amount to place is valid, and the operation succeeds)
- Some value other than 0 or 1 (namely, exceptions - for when the amount of troops is invalid, or in the wrong phase)
- Some true value other than 0 or 1 (can't set)

## BVA Step 4

### SCRAMBLE PHASE

### Test 1:
- Input:
  - territory = CONGO
  - numArmies = num player earned this turn + 1
  - Territory owner = current player
  - Game Phase = SCRAMBLE
- Output:
  - IllegalStateException
    - message: "Cannot place armies in a claimed territory until the scramble phase is over"
### Test 2:
- Input:
  - territory = CONGO
  - numArmies = 1
  - Territory owner = NOT current player
  - Game Phase = SCRAMBLE
- Output:
  - IllegalStateException
    - message: "Cannot place armies in a claimed territory until the scramble phase is over"
### Test 3:
- Input:
  - territory = YAKUTSK
  - numArmies = 5
  - Territory owner = SETUP
  - Game Phase = SCRAMBLE
- Output:
  - IllegalArgumentException (IllegalStateException?)
    - message: "You can only place 1 army on an unclaimed territory until the scramble phase is over"
### Test 4:
- Input:
  - territory = JAPAN
  - numArmies = -1
  - Territory owner = SETUP
  - Game Phase = SCRAMBLE
- Output:
  - IllegalArgumentException (IllegalStateException?)
    - message: "You can only place 1 army on an unclaimed territory until the scramble phase is over"
### Test 5:
- Input:
  - territory = BRAZIL
  - numArmies = 1
  - Territory owner = SETUP
  - Game Phase = SCRAMBLE
- Output: 1 (true)
### Test 6:
- Input:
  - territory = EASTERN_AMERICA
  - numArmies = 1
  - Territory owner = SETUP
  - Game Phase = SCRAMBLE
- Output: 1 (true)

### SETUP PHASE

### Test 7:
- Input:
  - territory = ALASKA
  - numArmies = -1
  - Territory owner = current player
  - Game Phase = SETUP
- Output:
  - IllegalArgumentException
    - message: "Cannot place \< 1 armies in a territory."
### Test 8:
- Input:
  - territory = ARGENTINA
  - numArmies = 0
  - Territory owner = current player
  - Game Phase = SETUP
- Output:
  - IllegalArgumentException
    - message: "Cannot place \< 1 armies in a territory."
### Test 9:
- Input:
  - territory = ALASKA
  - numArmies = 2
  - Territory owner = current player
  - Game Phase = SETUP
- Output:
  - IllegalArgumentException
    - message: "Cannot place more than 1 army at a time during setup phase"
### Test 10: 
- Input:
  - territory = ALASKA
  - numArmies = 1
  - Territory owner = NOT current player
  - Game Phase = SETUP
- Output:
  - IllegalArgumentException
    - message: "Cannot place armies on a territory you do not own"
### Test 11:
- Input:
  - territory = ALASKA
  - numArmies = 1
  - Territory owner = current player
  - Game Phase = SETUP
- Output: 1 (true)
### Test 12:
- Input:
  - territory = BRAZIL
  - numArmies = 1
  - Territory owner = current player
  - Game Phase = SETUP
- Output: 1 (true)

### TODO: ATTACK PHASE
