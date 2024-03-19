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





