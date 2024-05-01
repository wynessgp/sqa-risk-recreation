# method: `assignSetupArmiesToPlayers(): boolean`

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
    - A collection with [3, 6] elements -> ensure we give players the correct number of armies.

Note the lack of other error cases above - we expect whoever is calling us to have made the player collection, otherwise
the other methods haven't done their job.

Output:
- function output: 
    - 1
    - Not considering other outputs as these integration tests will assume correct calling of functions
      - You would have to go through input validation in the WorldDominationGameEngine constructor first.
- Players collection:
    - Each element in the list should have their armies increased by the SETUP amount for the number of players

## BVA Step 4
I will be modeling the input by the associated player color.

Output will be modeled by a collection of numbers, as these are integration tests. We want to see that, if we are
starting up a new game, each player gets the correct amount of armies, we shouldn't have to provide anything else.

This method in particular will differ from the original tests (even if these inputs/outputs are mirrored) via the
fact that they do not provide mocked player objects to our WorldDominationGameEngine.

### Test 1:
Given a valid list of players for the current game

When the game is started

Then each player in our player list should be given `35 - (( |player list| - 3 ) * 5)` armies to place

# method: `checkIfPlayerOwnsTerritory(relevantTerritory: TerritoryType, playerColor: PlayerColor): boolean`

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

## BVA Step 4
Two main behaviors we want to test as an integration test:
1. At the start of the game, all the territories should be unclaimed (represented by SETUP owning them)
   - Any time a Risk game starts, you have to claim all the territories before you can move on to other phases.
2. If a player places an army in a territory, this method needs to accurately reflect a player claiming it.
   - This integration test is better suited for placeNewArmiesInTerritory.

### Test 1:
Given a valid list of players for the current game

When the game has just been started

Then every territory should be unclaimed (owned by SETUP)

# method: `placeNewArmiesInTerritory(relevantTerritory: TerritoryType, numArmiesToPlace: int): boolean`
This method holds the bulk of the integration tests. Here's part of the reason why:
- If you place an army in the "scramble" phase, you expect it to immediately move on to the next player.
- You also expect the territory to then be claimed by said player
- The territory should then have an army placed in it
- Player objects need to have the number of armies available to place decreased after they place them
- In some game phases, placing an army can warrant a game phase change.

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
- Player object: Pointer
  - The number of armies they have to place should be updated
  - Territories held should be updated if in SCRAMBLE (maybe attack too).

## BVA Step 4
Here are some things to consider for integration tests (since we will be using very few mocked objects):
- Placing armies on already claimed territories should throw errors in some phases
  - This is exclusive to the SCRAMBLE phase; if it's not your territory in SETUP we should also error. 
- Any time I successfully place an army in SCRAMBLE/SETUP, it should become the next player's turn.
  - Ensure that we can make a "full cycle", so everyone goes once, and it wraps back around.
- When a player claims a territory in the SCRAMBLE phase, it should be recorded.
  - The territory should have its respective controller updated to that player
  - The TerritoryType should get added to a player's owned territories.
- After all the territories are claimed in the initial scramble, I should be able to place armies in my owned territories.
  - Ensure that we make a transition into the SETUP phase once all territories are claimed.
- After all players' armies have been placed from SETUP, single turns of PLACEMENT/ATTACK/FORTIFY should start.
  - Ensure that the game phase doesn't change too soon or too late.
- When the SETUP phase ends, as the first player going, I should expect to have the new armies I will need to place already be calculated, and can take my turn right away.
  - Ensure that when SETUP does end, the calculation for new armies in PLACEMENT is already done.
  - Note that we can't do this during their first placement in the PLACEMENT phase, otherwise we'll do it every time they try to place something.
- When a player goes to place troops in the PLACEMENT phase, and they are holding on to too many cards, then the game should not let them proceed
  - Ensure that some kind of error is thrown and the player is informed they have too many cards.
- After I have placed all of my armies in the PLACEMENT phase, I should be able to start attacking other players.
  - Ensure that we don't transition phases too early.
  - Also ensure that we don't change who the currently going player is.

To properly model these behaviors, we'll be using a test "outline" (not unlike that of using Cucumber).

### SCRAMBLE Phase Integration Tests

### Test 1:
Given that the players in the current game are [RED, PURPLE, YELLOW]

And the game is in the SCRAMBLE phase

And that the RED player has already claimed ALASKA by placing an army there

The Game should make it the PURPLE player's turn to respect ordering (assertion)

And when the PURPLE player tries to claim ALASKA by placing an army there

We should throw an error, as the territory is already claimed.
- In particular, we want to see an IllegalStateException error with the message: (assertion)
  - "Cannot place armies in a claimed territory until the scramble phase is over" (assertion)

### Test 2:
Given a valid list of players for the current game

And the game is in the SCRAMBLE phase

After each player claims a valid territory

The game should now say that it is the next player's turn (assertion)
- We want to make sure that this can wrap around; so make each player claim a territory once.
- We should go from the end of the collection of player colors back to index 0 (assertion)

### Test 3:
Given a valid list of players for the current game

And the game is in the SCRAMBLE phase

When each player claims a valid territory 

Then the game should add that territory to the player's collection of controlled territories (assertion)

And the game should update the territory's controlling PlayerColor (assertion)

### Test 4:
Given a valid list of players for the current game

And the game is currently in the SCRAMBLE phase

When each player claims a territory until no unclaimed territories remain
- Make sure we're still in SCRAMBLE until the last territory is claimed to ensure we transition at the right time (assertion)

Then the game should advance into the SETUP phase (assertion)

### SETUP Phase Integration Tests

### Test 5:
Given that the players in the current game are [RED, PURPLE, YELLOW]

And that the RED player owns ALASKA

And that the game is in the SETUP phase

When the PURPLE player tries to place an army on ALASKA

Then an IllegalArgumentException should be thrown
- Should have message: "Cannot place armies on a territory you do not own"

### Test 6:
Given a valid list of players for the current game

And the game is in the SETUP phase

After each player places an army on an already owned territory

The game should now say that it is the next player's turn (assertion)
- We want to make sure that this can wrap around; so make each player claim a territory once.
- We should go from the end of the collection of player colors back to index 0 (assertion)

### Test 7:
Given a valid list of players for the current game

And the game is currently in the SETUP phase (note - we'll manually transition it into SETUP for a fair test)

When each player expends their remaining placeable armies (assertion - make sure we don't transition early!)

Then the game should transition into the Placement phase (assertion)

### PLACEMENT Phase Integration Tests

### Test 8:
Given a valid list of players for the current game

And that the SETUP phase is about to end

When the last player goes to place their final army as part of the SETUP phase

Then the game should transition into the placement phase

And the FIRST player to go should have their appropriate amount of armies calculated

And they should be able to place these newly earned armies.

### Test 9:
Given a valid list of players for the current game

And the current player has 1 army left to place

And the game is in the PLACEMENT phase

When the player places their last army in the PLACEMENT phase

Then the game should advance into the attack phase