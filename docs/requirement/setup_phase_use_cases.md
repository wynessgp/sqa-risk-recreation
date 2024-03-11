# UC1. Game Setup

### Actor: Players

### Description
Users select their colors and place their armies on the game board to claim their territory  

### Basic Flow
1. Each user selects a color 
2. The system gives each user 40 Infantry pieces, decreased by 5 for each additional user above 2 (up to 6 users) 
3. Each user rolls a die, the user who rolls the highest number places an Infantry piece onto any territory on the board to claim that territory 
4. Each user takes turns placing an Infantry piece onto any unoccupied territory on the board 
5. The system displays the updated board with the user’s army shown on their selected territory 
6. Repeat steps 4 and 5 until all 42 territories have been claimed 
7. Users take turns placing one additional army in the territories they already occupied 
8. The system displays the updated board 
9. Repeat steps 7 and 8 until everyone runs out of armies 
10. The system shuffles and displays the pack of cards 

### Alternate Flow - Tie For Highest Die Number
From basic step 3: 
1. Each user roles the dice again 
2. Repeat step 1 until there is a highest number 
3. Return to Basic Flow at step 4 

### Alternate Flow – Place Army On Invalid Territory 
From basic steps 4 or 7: 
1. The system displays a warning showing that the player can’t place an army on the selected territory 
2. The user selects another territory to place the army 
3. Resume basic flow at step 4 or 7, depending on where the branch happened 

### Exceptions
None

### Preconditions
There are at least two players and no more than 6 players participating in the game

### Postconditions
The game board is fully set and ready to play 

### System or subsystem
none

### Other Stakeholders
none

### Special Requirements:
none


