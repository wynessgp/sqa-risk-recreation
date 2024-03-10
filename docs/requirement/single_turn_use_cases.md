# Single Turn Use Cases

## Notes
There are three different use cases in this file: 
- UC2: Single Turn Placement Phase during Game
- UC3: Single Turn Attack Phase during Game
- UC4: Single Turn Fortify Phase during Game <br><br>

These were split as the Attack Phase doesn't necessarily have the same actors each and
every time; namely the defender can change during the phase. It also makes it easier
to verify if each individual phase is correct. Links have been added for your convenience to jump around the markdown file.

<a id="toc"></a>

## Table of Contents
[Use Case 2 - Single Turn Placement Phase during Game](#uc-two) <br>
[Use Case 3 - Single Turn Attack Phase during Game](#uc-three) <br>
[Use Case 4 - Single Turn Fortify Phase during Game](#uc-four) <br>

<a id="uc-two"></a>

# UC2. Single Turn Placement Phase during Game

### Actor: Player

### Description
Player does the placement phase of their turn in an ongoing game

### Basic Flow
1. User receives appropriate amount of new armies
2. User selects a territory to place their armies
3. System displays the following information:
    1. How many armies the player currently has there
    2. A prompt asking how many new armies to place there
    3. An option to confirm the current placement
    4. An option to cancel the current placement
4. User does the following:
    1. Selects how many troops to place
    2. Selects to confirm the current placement
5. System does the following:
    1. Updates the board to reflect new placements
    2. Notifies the user on how many armies they must still place (if any)
6. User selects to end placement phase
7. System does the following:
    1. Notifies the user the placement phase is ending
    2. Displays an option to start the attack phase
    3. Displays an option to skip into the fortify phase

### Alternate Flow
1. Basic Flow Step 2: User selects a territory that isn't theirs
    1. System informs the user that the territory they selected is not theirs
    2. User confirms the message
    3. Resume basic flow at Step 2
2. Basic Flow Step 2: User selects to trade in RISK cards
    1. System does the following:
        1. Verifies the RISK cards are a valid trade-in
        2. Places up to 2 armies in territories matching relevant RISK cards
        3. Updates the amount of new armies the user has to place
        4. Notifies the user of their new armies
    2. Resume basic flow at Step 2
3. Basic Flow Step 4: User selects to cancel current placement
    1. Resume basic flow at Step 2
4. Basic Flow Step 4: User selects an invalid amount of armies to place
    1. System does the following:
        1. Informs the user they've selected an invalid number of troops
        2. Refunds their troops
    2. User confirms the message
    3. Resume basic flow at Step 2
5. Basic Flow Step 6: User hasn't placed all of their armies
    1. System informs the user they must place all of their armies
    2. User confirms the message
    3. Resume basic flow at Step 2
6. Basic Flow Step 6: User has 5 or 6 RISK cards in hand
    1. System informs the user they have too many RISK cards and must turn them in
    2. User does the following:
        1. Confirms the message
        2. Turns in RISK cards until they have less than 5 in hand
    3. System does the following:
        1. Verifies the RISK cards are a valid trade-in
        2. Places 2 armies in territories matching relevant RISK cards
        3. Updates the amount of armies the user must place
        4. Notifies the user of their new armies
    4. Resume basic flow at Step 2

### Exceptions

### Preconditions
System is displaying the game board

### Postconditions
The user has their new armies placed in order to begin the attack or fortify phase of 
their ongoing turn.

### System or subsystem
none

### Other Stakeholders
none

### Special Requirements:
none

[Back to TOC](#toc)


<a id="uc-three"></a>

# UC3. Single Turn Attack Phase during Game

### Actor: Attacking Player as AP, Defending Player as DP

### Description
Attacking Player is in the attacking phase of their single turn

### Basic Flow
1. AP does the following:
    1. Selects a territory to attack
    2. Selects a territory to attack from
2. System does the following:
    1. Prompts the AP for how many armies they wish to attack with
    2. Displays an option to confirm the attack
    3. Displays an option to cancel the attack
3. AP selects to confirm the attack
4. System does the following:
    1. Prompts the DP for how many armies they wish to defend with
    2. Displays an option to confirm the defense
5. DP selects to confirm the defense
6. System does the following:
    1. 

### Alternate Flow 

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

[Back to TOC](#toc)


<a id="uc-four"></a>

# UC4. Single Turn Fortify Phase during Game

### Actor: Player

### Description
A player is in the fortify phase of their single turn

### Basic Flow
1. AP does the following:
    1. Selects a territory to attack
    2. Selects a territory to attack from
2. System does the following:
    1. Prompts the AP for how many armies they wish to attack with
    2. Displays an option to confirm the attack
    3. Displays an option to cancel the attack
3. AP selects to confirm the attack
4. System does the following:
    1. Prompts the DP for how many armies they wish to defend with
    2. Displays an option to confirm that defense
5. 

### Alternate Flow 

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

[Back to TOC](#toc)