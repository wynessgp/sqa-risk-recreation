# UC2. Single Turn of Game

### Actor: Player

### Descrption
Player takes their turn in an ongoing game

### Basic Flow
1. User receives appropriate amount of new armies
2. User selects a territory to place their armies
3. System displays the following information:
    1. How many armies the player currently has there
    2. A prompt asking how many new armies to place there
    3. An option to confirm the current placement
    4. An option to cancel the current placement
4. User selects to confirm the current placement
5. System updates the board with the relevant info
6. User selects to end set up phase
7. 

### Alternate Flow
1. Basic Flow Step 2: User selects a territory that isn't theirs
    1. System informs the user that the territory they selected is not theirs
    2. User confirms the message
    3. Resume basic flow at Step 2
2. Basic Flow Step 4: User selects to cancel placement
    1. Resume basic flow at Step 2
3. Basic Flow Step 6: User hasn't placed all of their armies
    1. System informs the user they must place all of their armies
    2. User confirms the message
    3. Resume basic flow at Step 2

### Exceptions

### Preconditions
System is displaying the game board

### Postconditions
ABC

### System or subsystem
none

### Other Stakeholders
none

### Special Requirements:
none


