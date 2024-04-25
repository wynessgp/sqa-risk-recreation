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

