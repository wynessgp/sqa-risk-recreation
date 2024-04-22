# Method: `setPlayerInControl(player: PlayerColor): boolean`

## BVA Step 1
Input: The PlayerColor associated with the player who should be put in control of the territory

Output: A yes/no answer whether we could appropriately set the player in control of the territory

## BVA Step 2
Input: 
- player: Cases
- Underlying PlayerColor in storage: Cases

Output: Boolean (0, 1)

## BVA Step 3
Input: 
- player (Cases)
  - The 1st possibility (SETUP - error case)
  - The 2nd possibility (RED)
  - ...
  - The 7th possibility (PURPLE)
  - The 0th, 8th possibilities (can't set)
- Underlying PlayerColor in storage:
  - Note that this is the same enumerated type as the other input
  - Matches input (error case)
    - This is the only thing we're really interested in, everything else is valid

Output: Boolean
- 0 (can't set, we throw exceptions instead)
- 1
- Something other than true/false (Exceptions)
- Some other true value (can't set)
  
## BVA Step 4
### Test 1:
- Input:
  - player = SETUP
  - Underlying color = BLUE
- Output:
  - InvalidArgumentException
    - message: "Cannot set the player in control to setup"
### Test 2:
- Input:
  - player = SETUP
  - Underlying color = SETUP
- Output:
  - InvalidArgumentException
    - message: "Cannot set the player in control to setup"
    - Note that this strictly supersedes input matching underlying storage
### Test 3:
- Input:
  - player = RED
  - Underlying color = RED
- Output:
  - InvalidArgumentException
    - message: "Territory is already controlled by that player"
### Test 4:
- Input:
  - player = BLUE
  - Underlying color = GREEN 
- Output: 1 (true)
### Test 5:
- Input:
  - player = PURPLE
  - Underlying color = RED
- Output: 1 (true)

# Method: `setNumArmiesPresent(newAmount: int): boolean`

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

# Method: `isOwnedByPlayer(playerColor: PlayerColor): boolean`

## BVA Step 1
Input: The player we are trying to check and see if they own the territory

Output: A yes/no answer whether the given player owns the territory

## BVA Step 2
Input: Cases (for both underlying territory storage and given parameter)

Output: Boolean

## BVA Step 3
- Input:
  - playerColor (Cases):
    - The 1st possibility (SETUP)
    - The 2nd possibility (RED)
    - ...
    - The 0th, 8th possibility (can't set)
  - Underlying territory's playerColor (Cases):
    - Same cases as above
    - Interested in cases where they match
    - And don't match
- Output (Boolean):
  - 0 (false) if the PlayerColors do not match
  - 1 (true) if they do match
  - Some value other than true/false (can't set)
  - Some other true value (can't set)

## BVA Step 4
### Test 1:
Input:
- playerColor = BLUE
- territory controlled by = SETUP

Output:
- 0 (false)
### Test 2:
Input:
- playerColor = RED
- territory controlled by = PURPLE

Output:
- 0 (false)
### Test 3:
- playerColor = YELLOW
- territory controlled by = YELLOW

Output:
- 1 (true)

We will test all true and false combinations
