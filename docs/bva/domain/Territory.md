## Method: `public boolean setPlayerInControl(Player player)`

### BVA Step 1
- **Input**: A valid `Player` object.
- **Output**: True if the player was successfully set as the controller of the territory; otherwise, false.

### BVA Step 2
- **Input**: collection of player objects
- **Output**: Boolean

### BVA Step 3
- **Input**: Switching control from one player to another valid player.
- **Output**: True,or false.
  
### BVA Step 4
### Test 1
- **Input**: `setPlayerInControl(playerA)`
- **Output**: True

### Test 2
- **Input**: First, `setPlayerInControl(playerA)`, then `setPlayerInControl(playerB)`
- **Output**: True


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
- **Input**: `setNumArmiesPresent(-1)`
- **Output**: flase



## Method: `public Player getPlayerInControl()`

### BVA Step 1
- **Input**: The state of after setting a valid player in control.
- **Output**: The `Player` object that was set as in control.

### BVA Step 2
- **Input**: collection of player object.
- **Output**: Null or a player.

### BVA Step 3
- **Input**: No input.
- **Output**: The new `Player` object that has been set as in control.

### BVA Step 4

### Test 1
- **Input**: After `setPlayerInControl(playerA)`
- **Output**: `playerA`

### Test 2
- **Input**: no player was set
- **Output**: Null.


## Method: `public int getNumArmiesPresent()`

### BVA Step 1
- **Input**: the state of after setting numbers of armies on the territory.
- **Output**: The number of armies set.

### BVA Step 2
- **Input**: no input.
- **Output**: int.

### BVA Step 3
- **Input**: valid and invalid state.
- **Output**: number of armies.

  
### BVA Step 4
no test yet.

