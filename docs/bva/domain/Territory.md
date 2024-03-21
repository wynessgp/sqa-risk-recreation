## Method: `public boolean setPlayerInControl(Player player)`

### BVA Step 1
- **Input**: A valid `Player` object.
- **Output**: Yes or no answer

### BVA Step 2
- **Input**: a pointer to a player object
- **Output**: Boolean

### BVA Step 3
- **Input**: Switching control from one player to another valid player contain the following situations:
  - The first possibility: Assigning control of a territory to a player when it previously had no player assigned
  - The second possibility: Transitioning control from one player (PlayerA) to a different player (PlayerB)
  - The third possibility: Attempting to change control from a player to the same player.
  - The fourth possibility: Assign the territory's control to null after it has been controlled by a valid player
  - The fifth possibility: Assign the territory's control to the same player after it has been controlled by a valid player
- **Output**: True,or false.
  
### BVA Step 4

### Test 1
- **Input**: Assigning control of a territory to a player when it previously had no player assigned (PlayerA)
- **Output**: True

### Test 2
- **Input**: assign and check the player in control is setted up correctly (PlayerA)
- **Output**: True


### Test 3
- **Input**: Transitioning control from one player (PlayerA) to a different player (PlayerB)
- - **Output**: true

### Test 4
- **Input**: check the player in control is setted up correctly after the transition from one player to another (PlayerA) to (PlayerB)
- - **Output**: true

- ### Test 5
- **Input**: Assign the territory's control to null after it has been controlled by a valid player (PlayerA)
- - **Output**: False

- ### Test 6
- **Input**: Assign the territory's control to the same player(PlayerA) after it has been controlled by a valid player (PlayerA)
- - **Output**: False

## Method: `public boolean setNumArmiesPresent(int newAmount)`

### BVA Step 1
- **Input**: A positive number of armies to be placed on the territory.
- **Output**: Yes or no answer

### BVA Step 2
- **Input**: count
- **Output**: Boolean

### BVA Step 3
- **Input**: -1,0, 1,>1, (No max due to rules)
- **Output**: true and false.

### BVA Step 4
### Test 1
- **Input**: set 5 armies present
- **Output**: True

### Test 2
- **Input**: set 0 armies present
- **Output**: True
  
### Test 3
- **Input**: set 1 army present
- **Output**: True

### Test 4
- **Input**: invalid input  (negative number -1)
- **Output**: false


### Test 5
- **Input**: check the number of armies present after setting(to 5) is 5
- **Output**: true

### Test 6
- **Input**: check the number of armies present after setting(to 0) is 0
- **Output**: true

### Test 7
- **Input**: check the number of armies present after setting(to 1) is 1
- **Output**: true


## Method: `getPlayerInControl()`

### BVA Step 1
- **Input**: the player in control has been set.
- **Output**: The current Player in control or null if no player is in control

### BVA Step 2
- **Input**: None
- **Output**: Player object or null

### BVA Step 3
- **Input**: Cases
  - The first possibility: The territory has no player in control, call getter
  - The second possibility: The territory has a player in control,call getter
  
- **Output**: Player object or null

### BVA Step 4
### Test 1
- **Input**: get player in control without any player being set
- **Output**: null

### Test 2
- **Input**: set playerA in control. Then, retrieve the current player in control
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
- **Input**: get number of armies present without any armies being set
- **Output**: 0

### Test 2
- **Input**: set 5 armies present. Then, retrieve the number of armies present
- **Output**: 5
