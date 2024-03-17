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
1. User selects to trade in RISK cards
2. System does the following:
    1. Updates the amount of armies the user must place
    2. Notifies the user they have received new armies
        1. Includes ones placed in territories, if necessary
        2. Includes ones gained from trading in the set
    3. Displays an option to turn in more RISK cards
3. User selects a territory to place their armies
4. System displays the following information:
    1. How many armies the player currently has there
    2. A prompt asking how many new armies to place there
    3. An option to confirm the current placement
    4. An option to cancel the current placement
5. User does the following:
    1. Enters how many armies to place
        1. Bounded by [1, num new armies)
    2. Selects to confirm the current placement
6. System does the following:
    1. Updates the board to reflect new placements
    2. Notifies the user on how many armies they must still place
7. User selects to end placement phase
8. System does the following:
    1. Notifies the user the placement phase is ending
    2. Displays an option to start the attack phase
    3. Displays an option to skip into the fortify phase

### Alternate Flow
1. Basic Flow Step 1: User has no RISK cards to trade in
    1. System informs the user they have no RISK cards to trade in
    2. User confirms the message
    3. Resume basic flow at Step 3
2. Basic Flow Step 1: User does not select to trade in RISK cards
    1. Resume basic flow at Step 3
3. Basic Flow Step 1: User has 5 or 6 RISK cards in hand (forced trade-in)
    1. System informs the user they have too many RISK cards and must turn them in
    2. User does the following:
        1. Confirms the message
        2. Turns in RISK cards until they have less than 5 in hand
    3. Resume basic flow at Step 2
4. Basic Flow Step 3: User selects to turn in more RISK cards
    1. Resume basic flow at Step 2
5. Basic Flow Step 3: User selects a territory that isn't theirs
    1. System informs the user that the territory they selected is not theirs
    2. User confirms the message
    3. Resume basic flow at Step 1
6. Basic Flow Step 5: User selects to cancel current placement
    1. Resume basic flow at Step 1
7. Basic Flow Step 5: User selects an invalid amount of armies to place
    1. System does the following:
        1. Informs the user they've selected an invalid number of troops
        2. Notifies them that the valid range is bounded by [1, num new armies)
    2. User confirms the message
    3. Resume basic flow at Step 1
8. Basic Flow Step 7: User hasn't placed all of their armies
    1. System informs the user they must place all of their armies
    2. User confirms the message
    3. Resume basic flow at Step 1

### Exceptions
1. User selects the exit application option
    1. System closes the application

### Preconditions
1. System is displaying the game board
2. It is the user's turn
3. The game is not over
4. System has calculated the amount of armies the current player (user) needs to place

### Postconditions
1. System is displaying the game board
2. It is the attack phase of the user's turn
3. The game board is appropriately updated with the newly placed troops

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
3. AP does the following:
    1. Enters how many armies they wish to use for the attack
    2. Selects to confirm the attack
4. System does the following:
    1. Prompts the DP for how many armies they wish to defend with
    2. Displays an option to confirm the defense
5. DP does the following:
    1. Enters how many armies they wish to use for the defense
    2. Selects to confirm the defense
6. System does the following:
    1. Displays the casualty results with die rolls
7. AP confirms the results of the battle
8. System displays the following:
    1. The remaining number of armies in each territory involved
    2. An option to continue the attack
    3. An option to cancel the attack
9. AP selects to cancel the attack
10. System does the following:
    1. Updates the board to reflect army count changes
    2. Enables the AP to continue attacking
    3. Displays an option to end the attack phase
11. AP selects to end the attack phase
12. System notifies the AP that the fortify phase is beginning

### Alternate Flow 
1. Basic Flow Step 1: AP selects a territory to attack that is their own
    1. System informs the AP that the territory they've selected to attack is their own
    2. AP confirms the message
    3. Resume basic flow at Step 1
2. Basic Flow Step 1: AP selects a territory to attack from that is not their own
    1. System informs the AP that the territory they've selected to attack from is not their own
    2. AP confirms the message
    3. Resume basic flow at Step 1.1
3. Basic Flow Step 3: AP provides an invalid number of armies to use for the attack
    1. System informs the AP they've selected an invalid number of armies to use in the attack
        1. System notes that it must be from [1, 3]
        2. Must leave an army behind in the territory they are attacking from
    2. AP confirms the message
    3. Resume basic flow at Step 3
4. Basic Flow Step 5: DP provides an invalid number of armies to use for the defense
    1. System informs the DP they've selected an invalid number of armies to use in the defense
        1. System notes that it must be from [1, 2]
        2. System notes that it cannot exceed the number of armies in the territory
    2. DP confirms the message
    3. Resume basic flow at Step 5 
5. Basic Flow Step 8 (9?): There are no remaining defending armies
    1. System does the following:
        1. Updates who the territory belongs to
        2. Displays the remaining number of armies in each territory involved
        3. Displays an option to continue
    2. AP selects the option to continue
    3. Resume basic flow at Step 11 (9?)
6. Basic Flow Step 9: AP chooses to continue the attack
    1. Resume basic flow at Step 3
7. Basic Flow Step 11: AP chooses to attack again
    1. Resume basic flow at Step 3

### Exceptions
1. User selects the exit application option
    1. System closes the application

### Preconditions
1. System is displaying the game board
2. It is the attack phase of the AP's turn (AP selected to enter the attack phase from UC2)
3. The game is not over

### Postconditions
1. System is displaying the game board
2. It is the fortify phase of the AP's turn, or the game has ended.

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
1. User does the following:
    1. Selects a territory to move armies from
    2. Selects a territory to move armies to
2. System does the following:
    1. Prompts the user for how many armies they wish to move from the selected territory
    2. Displays an option to confirm the movement
    3. Displays an option to cancel the movement
3. User does the following:
    1. Provides the relevant information to the prompt
    2. Selects to confirm the movement
4. System does the following:
    1. Updates the board to reflect the movement
    2. Displays the results of the action
    3. Informs the user that their only fortification movement has been used
5. User confirms the message
6. System proceeds with the next player's move from the placement phase.

### Alternate Flow 

-- 
1. Basic Flow Step 1: User selects a territory to transfer from that is not their own
    1. System informs the User that the territory they've selected to transfer from is not their own
    2. User confirms the message
    3. Resume basic flow at Step 1
2. Basic Flow Step 1: User selects a territory to transfer to that is not their own
    1. System informs the User that the territory they've selected to transfer to is not their own
    2. User confirms the message
    3. Resume basic flow at Step 1.1
3. Basic Flow Step 3: User provides an invalid amount of armies to transfer
    1. System informs the user that they've selected an invalid amount of troops to transfer
        1. Notes that it must be bounded by [1, armies in territory)
    2. User confirms the message
    3. Return to basic flow step 1

### Exceptions
1. User selects the exit application option
    1. System closes the application

### Preconditions
1. System is displaying the game board
2. It is the fortify phase of the user's turn

### Postconditions
1. System is displaying the game board
2. It is the next player's turn

### System or subsystem
none

### Other Stakeholders
none

### Special Requirements:
none

[Back to TOC](#toc)