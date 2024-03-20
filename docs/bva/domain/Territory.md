## Method: `public boolean setPlayerInControl(Player player)`

### BVA Step 1
- **Input**: A valid `Player` object.
- **Output**: True if the player was successfully set as the controller of the territory; otherwise, false.

### BVA Step 2
- **Input**: a pointer to a player object
- **Output**: Boolean

### BVA Step 3
- **Input**: Switching control from one player to another valid player contain the following situations:
  - The first possibility: Assigning control of a territory to a player when it previously had no player assigned
  - The second possibility: Transitioning control from one player (PlayerA) to a different player (PlayerB)
  - The third possibility: Attempting to change control from a player to the same player.
  - The fourth possibility: Assign the territory's control to null after it has been controlled by a valid player
- **Output**: True,or false.
  
### BVA Step 4
### Test 1
- **Input**: `setPlayerInControl(playerA)`
- **Output**: True

### Test 2
- **Input**: First, `setPlayerInControl(playerA)`, then `getPlayerInControl()  == playerA`
- **Output**: True


### Test 3
- **Input**: `setPlayerInControl(playerA)`, then `setPlayerInControl(playerB)`
- - **Output**: true

### Test 4
- **Input**: `setPlayerInControl(playerA)`, then `setPlayerInControl(playerB)`, then `getPlayerInControl()  == playerB`
- - **Output**: true

- ### Test 5
- **Input**: `setPlayerInControl(playerA)`, then `setPlayerInControl(null)`
- - **Output**: False

## Method: `public boolean setNumArmiesPresent(int newAmount)`

### BVA Step 1
- **Input**: A positive number of armies to be placed on the territory.
- **Output**: return True if the number of armies was successfully set; otherwise, false.

### BVA Step 2
- **Input**: count
- **Output**: Boolean

### BVA Step 3
- **Input**: -1,0, 1,>1, (No max due to rules)
- **Output**: true and false.

### BVA Step 4

### Test 1
- **Input**: `setNumArmiesPresent(5)`
- **Output**: True

### Test 2
- **Input**: `setNumArmiesPresent(0)`
- **Output**: True
  
### Test 3
- **Input**: `setNumArmiesPresent(1)`
- **Output**: True

### Test 4
- **Input**: `SetNumArmiesPresent(-1)` 
- **Output**: false

### Test 5
- **Input**: `SetNumArmiesPresent(5)` then `territory.getNumArmiesPresent()` == 5
- **Output**: true

### Test 6
- **Input**: `SetNumArmiesPresent(0)` then `territory.getNumArmiesPresent()` == 0
- **Output**: true

### Test 7
- **Input**: `SetNumArmiesPresent(1)` then `territory.getNumArmiesPresent()` == 1
- **Output**: true


## Method: `getPlayerInControl()`

### BVA Step 1

- **Input**: the player in control has been set.
- **Output**: The current Player in control or null if no player is in control

### BVA Step 2

- **Input**: None
- **Output**: Player object or null

### BVA Step 3
//To do this, you can show what the object looks like before calling the method, then give the expected output.
- **Input**: Cases
  - The first possibility: The territory has no player in control, call getter
  - The second possibility: The territory has a player in control,call getter
  
- **Output**: Player object or null

### BVA Step 4

### Test 1
- **Input**: `getPlayerInControl()`
- **Output**: null

### Test 2
- **Input**: `setPlayerInControl(playerA)`, then `getPlayerInControl() == playerA`
- **Output**: PlayerA


## Method: `getNumArmiesPresent()`

### BVA Step 1

- **Input**: The number of armies present on the territory has been set.
- **Output**: The number of armies present on the territory

### BVA Step 2

- **Input**: None
- **Output**: Integer

### BVA Step 3

- **Input**: Cases
  - The first possibility: The territory has no armies present, call getter
  - The second possibility: The territory has armies present, call getter

- **Output**: Integer
- - The first possibility: 0
- - The second possibility: >0 (the number of armies present)

### BVA Step 4

### Test 1
- **Input**: `getNumArmiesPresent()`
- **Output**: 0

### Test 2
- **Input**: `setNumArmiesPresent(5)`, then `getNumArmiesPresent() == 5`
- **Output**: 5













