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
- **Input**: a valid Player object, The territory has no player in control
- **Output**: True

### Test 2
- **Input**: A vaild player objects.the territory has been controlled by another player
- - **Output**: true

- ### Test 3
- **Input**: null, The territory has been controlled by a valid player
- - **Output**: False

- ### Test 4
- **Input**: a valid Player object, The territory has been controlled by the same player
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
- **Input**: 5
- **Output**: True

### Test 2
- **Input**: 0
- **Output**: True
  
### Test 3
- **Input**: 1
- **Output**: True

### Test 4
- **Input**: invalid input (negative number -1)
- **Output**: false



## Method: `getPlayerInControl()`

### BVA Step 1
- **Input**: the underlying Player object associated with the Territory.
- **Output**: The current Player in control or null if no player is in control

### BVA Step 2
- **Input**: None
- **Output**: Player object or null

### BVA Step 3
- **Input**: Cases
  - The first possibility: The territory has no player in control, call getter
  - The second possibility: The territory has a player in control, call getter
  
- **Output**: Player object or null

### BVA Step 4
### Test 1
- **Input**: get player in control without any player being set
- **Output**: null

### Test 2
- **Input**: get the current player in control when PlayerA was in control and PlayerB is set to control the territory
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
- **Input**: retrieve the number of armies present when 5 armies were set to be present
- **Output**: 5


## Method: `public TerritoryType getTerritoryType()`

### BVA Step 1
- **Input**: The territory type has been set.
- **Output**: The type of territory

### BVA Step 2
- **Input**: None
- **Output**: TerritoryType

### BVA Step 3
- **Input**: Each possibility for TerritoryType (42 possibilities). Note that anything outside of these choices is not considered a valid input.
  - Alaska
  - WesternCanada
  - ...
  - WesternAustralia

- **Output**: TerritoryType

### BVA Step 4

### Test 1
- **Input**: get territory type without any territory type being set
- **Output**: null

### Test 2
- **Input**:retrieve the territory type when it is set to Alaska
- **Output**: ALASKA

### Test 3
- **Input**: retrieve the territory type when it is set to Western Australia
- **Output**: WESTERN_AUSTRALIA