# method: `startGame(amountOfPlayers: int): void`

## BVA Step 1
Input: The amount of players to start the game with

Output: N/A (exception if the amount of players is not in [2, 6])

## BVA Step 2
Input: Interval [2, 6]

Output: N/A (or exception; this method simply assigns the numPlayers integer in GameEngine, if possible)

## BVA Step 3
Input: Interval
- 2 - some number (can't set)
- 2
- 6
- 6 + some number (can't set)
- Normal value in range (4)

Output:
- Exception for input case 1
- Exception for input case 4

## BVA Step 4
### Test 1:
- Input: amountOfPlayers = 1
- Output: IllegalArgumentException
  - message: "Amount of valid players is in [2, 6]"
### Test 2:
- Input: amountOfPlayers = 7
- Output: IllegalArgumentException
  - message: "Amount of valid players is in [2, 6]"
### Test 3:
- Input: amountOfPlayers = 2
- Output: None (valid input)
### Test 4:
- Input: amountOfPlayers = 4
- Output: None (valid input)
### Test 5: 
- Input: amountOfPlayers = 6
- Output: None (valid input)

# method: `assignSetupArmiesToPlayers(int numPlayers): List<Integer>`
Note: this method assumes you've already called the corresponding startGame method from above,
so exception handling will instead be handled by setupGame. I simply enumerate the BVA
test cases here, so you can see their corresponding output. 

## BVA Step 1
Input: The amount of players that will be in the current game

Output: A collection of length `numPlayers`, filled with the amount of armies each player
is to receive for the setup phase of the game at each index.

## BVA Step 2
Input: Cases

Output: Collection, with specific values at each index.
  - Think of it as a Collection of Cases.

## BVA Step 3
Input: Cases
- The 1st possibility (2 players - special variant of Risk)
- The 2nd possibility (3 players)
- The 3rd possibility (4 players)
- The 4th possibility (5 players)
- The 5th possibility (6 players)
- The 0th, 6th possibilities (error cases; error checking occurs when player assignment is done).

Output: Collection
- An empty collection (error case; an exception would be thrown before this happens)
- A collection with 1 or 2 elements (error case; exception would be thrown before this happens)
- A collection \> 3 elements (valid: 3, 4, 5, 6)
- A collection with 7 elements (error case; exception would be thrown before this happens)

## BVA Step 4
### Test 1:
- Input: numPlayers = 2
- Output: Collection = [40, 40, 40]
  - It may not seem apparent, but the rules are slightly different for two players. 
  - There is a "third" player in that the NEUTRAL "player" gets 40 armies to start, and things proceed from there.
### Test 2:
- Input: numPlayers = 3
- Output: Collection = [35, 35, 35]
### Test 3:
- Input: numPlayers = 4
- Output: Collection = [30, 30, 30, 30]
### Test 4:
- Input: numPlayers = 5
- Output: Collection = [25, 25, 25, 25, 25]
### Test 5:
- Input: numPlayers = 6
- Output: Collection = [20, 20, 20, 20, 20, 20]

Any other input/output combination is an exception.

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

## BVA Step 1
Input: The respective territory the current user wants to place their armies in, as well as the
number of new armies to place there. The state of who owns the respective territory is also taken into account.

Output: A yes/no answer whether the armies were placed successfully; an exception if they were not able to be 
placed successfully (i.e. another player owns the territory).

## BVA Step 2
Input:
- territory: Cases
- numNewArmies: Counts [1, armies earned on player's turn]
- State of who owns the territory: Cases

Output: Boolean

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

Output: Boolean
- 0 (can't set)
- 1 (when the amount to place is valid, and the operation succeeds)
- Some value other than 0 or 1 (namely, exceptions - for when the amount of troops is invalid)
- Some true value other than 0 or 1 (can't set)

## BVA Step 4
### Test 1:
- Input:
  - territory = ALASKA
  - numArmies = -1
  - Territory owner = SETUP
- Output:
  - IllegalArgumentException 
    - message: "Cannot place \< 1 armies in a territory."
### Test 2:
- Input:
  - territory = ARGENTINA
  - numArmies = 0
  - Territory owner = SETUP
- Output:
  - IllegalArgumentException
    - message: "Cannot place \< 1 armies in a territory."
### Test 3:
- Input:
  - territory = CONGO
  - numArmies = num player earned this turn + 1
  - Territory owner = current player
- Output:
  - IllegalArgumentException
    - message: "Cannot place more armies than you earned"
### Test 4:
- Input:
  - Territory = 
### Test 4: 
- Input:
  - territory = ALASKA
  - numArmies = 1
  - Territory owner = NOT current player
- Output:
  - IllegalArgumentException
    - message: "Player does not own the selected territory"
### Test 5:
- Input:
  - territory = ALASKA
  - numArmies = 1
  - Territory owner = current player
- Output: 1 (true)
### Test 6:
- Input:
  - territory = ALASKA
  - numArmies = 5
  - Territory owner = current player
- Output: 1 (true)
### Test 7:
- Input:
  - territory = YAKUTSK
  - numArmies = num player earned this turn
  - Territory owner = current player
- Output: 1 (true)

# method: `calculateNewGamePhase(): GamePhase`

## BVA Step 1
Input: The current phase of the game

Output: The next game phase that the game should enter
(troop placement -> attack phase -> fortify, etc.)

## BVA Step 2
Input: Cases

Output: Cases

## BVA Step 3
Input: Cases
- The first possibility (game start)
- The second possibility (setup)
- The third possibility (placement)
- The fourth possibility (attack)
- The fifth possibility (fortify)
- The sixth possibility (game over)
- The 0th or 7th possibility (can't set)

Output: Cases
- The first possibility (game start)
- The second possibility (setup)
- The third possibility (placement)
- The fourth possibility (attack)
- The fifth possibility (fortify)
- The sixth possibility (game over)
- The 0th or 7th possibility (can't set)

## BVA Step 4
### Test 1:
- Input: current phase = [GAME_START]
- Output: [SETUP]
### Test 2:
- Input: current phase = [SETUP]
- Output: [PLACEMENT]
### Test 3:
- Input: current phase = [PLACEMENT]
- Output: [ATTACK]
### Test 4: (a player wins the game)
- Input: current phase = [ATTACK]
- Output: [GAME_OVER]
### Test 5: (player doesn't win the game)
- Input: current phase = [ATTACK]
- Output: [FORTIFY]
### Test 6:
- Input: current phase = [FORTIFY]
- Output: [PLACEMENT]

