# UC1. Game Setup

### Actor: Players

### Description
Users select their colors and place their armies on the game board to claim their territory  

### Basic Flow
1. Each user selects a color 
2. The system gives each user 
    - 40 Infantry pieces if there are 2 users
    - 35 Infantry piece if there are 3 users
    - 30 Infantry pieces if there are 4 users
    - 25 Infantry pieces if there are 5 users
    - 20 Infantry pieces if there are 6 users
3. Each user rolls a die, the user who rolls the highest number places an Infantry piece onto any territory on the board to claim that territory
4. The system displays the updated board with the user’s army shown on their selected territory  
5. Each user takes turns placing an Infantry piece onto any unoccupied territory on the board 
6. The system displays the updated board
7. Users take turns placing one additional army in the territories they already occupied 
8. The system displays the updated board 
9. The system shuffles and displays the pack of cards 

### Alternate Flow - Tie For Highest Die Number During Set Up
From basic step 3: 
1. Each user roles the dice again 
2. Repeat step 1 until there is a highest number 
3. Return to Basic Flow at step 4 

### Alternate Flow – Place Army On Invalid Territory During Set Up
From basic steps 4 or 7: 
1. The system displays a warning showing that the player can’t place an army on the selected territory 
2. The user selects another territory to place the army 
3. Resume basic flow at step 4 or 7, depending on where the branch happened

### Alternate Flow - Still Have Unclaimed Territories During Set Up
From basic step 6: 
1. Each user takes turns placing an Infantry piece onto any unoccupied territory on the board 
2. The system displays the updated board
3. Repeat steps 1 and 2 until all territories are claimed
4. Return to Basic Flow at step 7

### Alternate Flow - Still Have Unplaced Armies During Set Up
From basic step 8:
1. Users take turns placing one additional army in the territories they already occupied 
2. The system displays the updated board
3. Repeat steps 1 and 2 until all users run out of armies 

### Exceptions
None

### Preconditions
There are at least two users and no more than 6 users playing in the game

### Postconditions
The game board is fully set and ready to play 

### System or subsystem
none

### Other Stakeholders
none

### Special Requirements:
none


