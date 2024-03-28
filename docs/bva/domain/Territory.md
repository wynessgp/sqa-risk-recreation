# Method: `setPlayerInControl(Player player): boolean`

## BVA Step 1
Input: A player object to put in control of a territory

Output: A yes/no answer whether we could set the player object in control of the territory.

## BVA Step 2
Input: 
- player: Pointer
- Underlying player in control storage: Pointer

Output: Boolean (0, 1)

## BVA Step 3
Input: Pointer(s)
- player:
  - The null pointer (will not be tested, in accordance with Martin's rules)
  - A pointer to the true player object
    - Assign control of a territory to a player when it previously had no player assigned
    - Transition control from one player (PlayerA) to a different player (PlayerB)
    - Assign control to the same player as the one controlling the territory.
- Underlying player object:
  - The null pointer (will not be tested)
  - A pointer to a true player object

Output: Boolean
- 0
- 1
  
## BVA Step 4
### Test 1:
- Input: 
  - Underlying player in control = [valid player, color SETUP]
  - player = [valid player, color BLUE]
- Output: True

### Test 2:
- Input: 
  - Underlying player in control = [valid player, color RED]
  - player = [valid player, color PURPLE]
- Output: True

### Test 3:
- Input: 
  - Underlying player in control = [valid player, color GREEN]
  - player = [valid player, color GREEN]
- Output: False

# Method: `setNumArmiesPresent(int newAmount): boolean`

## BVA Step 1
Input: A positive number of armies to be placed on the territory.

Output: Yes or no answer whether we could change the amount of armies in the given territory.

## BVA Step 2
Input: Counts [1, num armies player has in total]

Output: Boolean

## BVA Step 3
Input: 
- -1 (error case)
- 0 (error case; it should only ever be 0 due to set up)
- 1
- \>1
- Num armies the player has in total 
- Num armies the player has in total + 1 (can't set)

Output: Boolean
- 0 (can't set)
- 1
- Something that is neither true nor false (Illegal arg exceptions)

## BVA Step 4
### Test 1:
- Input: newAmount = -1
- Output: IllegalArgumentException
  - message: "Number of armies to set should be greater than 0"

### Test 2:
- Input: newAmount = 0
- Output: IllegalArgumentException
  - message: "Number of armies to set should be greater than 0"
  
### Test 3:
- Input: newAmount = 1
- Output: True

### Test 4:
- Input: newAmount = 24
- Output: True

# Method: `getPlayerInControl(): Player`

## BVA Step 1
Input: N/A

Output: The current Player in control the territory

## BVA Step 2
Input: N/A

Output: Pointer

## BVA Step 3
Input: N/A
  
Output: Pointer
- A null pointer (cannot set, Martin's rules)
- A pointer to a valid player object

## BVA Step 4
### Test 1:
- Input: Territory's player = [PlayerA]
- Output: PlayerA

### Test 2:
- Input:  Territory's player = [PlayerB]
- Output: PlayerB

# Method: `getNumArmiesPresent(): int`

## BVA Step 1
Input: N/A

Output: The number of armies present on the territory

## BVA Step 2
Input: N/A

Output: Counts [1, num armies present]

## BVA Step 3
Input: N/A

Output: Counts
- -1 (can't set)
- 0 (can't set)
- 1 
- \> 1
- The number of armies present

## BVA Step 4
### Test 1:
- Input: num armies in territory = 1
- Output: 0

### Test 2:
- Input: number armies in territory = 5
- Output: 5

# Method: `getTerritoryType(): TerritoryType`

## BVA Step 1
Input: The territory type has been set.

Output: The type of territory

## BVA Step 2
Input: cases

Output: TerritoryType

## BVA Step 3
Input: Each possibility for TerritoryType (42 possibilities). Note that anything outside of these choices is not considered a valid input.
- Alaska
- WesternCanada
- ...
- WesternAustralia

Output: Cases

## BVA Step 4

### Test 1
- Input: territory type set to null
- Output: null

### Test 2
- Input:territory type set to Alaska
- Output: ALASKA

### Test 3
- Input: territory type set to Western Australia
- Output: WESTERN_AUSTRALIA
