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
- Input:
    - Player objects = [Red, Yellow, Purple]
- Output:
    - 1 (true)
    - Collection = [35, 35, 35]
### Test 2:
- Input:
    - Player objects = [Red, Yellow, Purple, Green]
- Output:
    - 1 (true)
    - Collection = [30, 30, 30, 30]
### Test 3:
- Input:
    - Player objects = [Red, Yellow, Purple, Green, Black]
- Output:
    - 1 (true)
    - Collection = [25, 25, 25, 25, 25]
### Test 4:
- Input:
    - Player objects = [Red, Yellow, Purple, Green, Black, Blue]
- Output:
    - 1 (true)
    - Collection = [20, 20, 20, 20, 20, 20]

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
Input:
- relevantTerritory: Every possible TerritoryType value (iterate over them)
- playerColor: SETUP

Output: 1 (true)
- This needs to be checked for each and every territory to ensure correct initialization, in the same test.


