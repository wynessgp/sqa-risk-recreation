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
- 0 (error case) 
- 1
- \>1
- Num armies the player has in total (can't set)
- Num armies the player has in total + 1 (can't set)

Output: Boolean
- 0
- 1
- Something that is neither true nor false (can't set)

## BVA Step 4
### Test 1:
- Input: 5
- Output: True

### Test 2:
- Input: 0
- Output: True
  
### Test 3:
- Input: 1
- Output: True

### Test 4:
- Input: invalid input (negative number -1)
- Output: false

# Method: `getPlayerInControl(): Player`

## BVA Step 1
Input: the underlying Player object associated with the Territory.

Output: The current Player in control or null if no player is in control

## BVA Step 2
Input: the state of the underlying object (Player object) is set

Output: pointer

## BVA Step 3
Input: Cases
- The first possibility: The territory has no player in control
- The second possibility: The territory has a player in control
  
Output: the assigned Player object pointer or null

## BVA Step 4
### Test 1:
- Input: assigned player is null
- Output: null

### Test 2:
- Input: a valid Player object (PlayerA)
- Output: PlayerA

### Test 3:
- Input: a valid Player object (PlayerB), when another valid Player object was previously in control (PlayerA)
- Output: PlayerB

# Method: `getNumArmiesPresent(): int`

## BVA Step 1
Input: the underlying count of numArmiesPresent associated with the Territory.

Output: The number of armies present on the territory

## BVA Step 2
Input: the count of armies present on the territory

Output: Integer

## BVA Step 3
Input: count

Output: Integer
- The first possibility: 0
- The second possibility: >0 (the number of armies present)

## BVA Step 4
### Test 1:
- Input: number of armies set to 0
- Output: 0

### Test 2:
- Input: number of armies set to 5
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
