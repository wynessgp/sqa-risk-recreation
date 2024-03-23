## Method: `public boolean setPlayerInControl(Player player)`: boolean

### BVA Step 1
- **Input**: A valid `Player` object.
- **Output**: Yes or no answer

### BVA Step 2
- **Input**: Pointer, Territory object is initialized
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

## Method: `public boolean setNumArmiesPresent(int newAmount)`: boolean

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



## Method: `getPlayerInControl()`: Player

### BVA Step 1
- **Input**: the underlying Player object associated with the Territory.
- **Output**: The current Player in control or null if no player is in control

### BVA Step 2
- **Input**: the state of the relying object (Player object) is set
- **Output**: pointer or null

### BVA Step 3
- **Input**: Cases
  - The first possibility: The territory has no player in control, call getter
  - The second possibility: The territory has a player in control, call getter
  
- **Output**: Pointers of Player object or null

### BVA Step 4
### Test 1
- **Input**: assigned player is null
- **Output**: null

### Test 2
- **Input**: a valid Player object (PlayerA)
- **Output**: PlayerA

### Test 3
- **Input**: a valid Player object (PlayerB), when another valid Player object was previously in control (PlayerA)
- **Output**: PlayerB


## Method: `getNumArmiesPresent()`: int

### BVA Step 1
- **Input**: the underlying count of numArmiesPresent associated with the Territory.
- **Output**: The number of armies present on the territory

### BVA Step 2
- **Input**: the count of armies present on the territory
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
- **Input**: number of armies set to 0
- **Output**: 0

### Test 2
- **Input**: number of armies set to 5
- **Output**: 5


## Method: `public TerritoryType getTerritoryType()`: TerritoryType

### BVA Step 1
- **Input**: The territory type has been set.
- **Output**: The type of territory

### BVA Step 2
- **Input**: cases
- **Output**: TerritoryType

### BVA Step 3
- **Input**: Each possibility for TerritoryType (42 possibilities). Note that anything outside of these choices is not considered a valid input.
  - Alaska
  - WesternCanada
  - ...
  - WesternAustralia

- **Output**: Pointer of TerritoryType

### BVA Step 4

### Test 1
- **Input**: territory type set to null
- **Output**: null

### Test 2
- **Input**:territory type set to Alaska
- **Output**: ALASKA

### Test 3
- **Input**: territory type set to Western Australia
- **Output**: WESTERN_AUSTRALIA
