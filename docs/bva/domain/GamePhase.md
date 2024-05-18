# method: `toString(): String`

## BVA Step 1
Input: Underlying GamePhase enum

Output: The string representation of the enum with only the first letter capitalized

## BVA Step 2
Input: Cases

Output: Strings

## BVA Step 3
Input:
- SETUP
- PLACEMENT
- SCRAMBLE
- ATTACK
- FORTIFY
- GAME_OVER
- No other inputs possible

Output:
- "Setup"
- "Placement"
- "Scramble"
- "Attack"
- "Fortify"
- "Game over"
- No other outputs possible

## BVA Step 4
### Test value 1
Input: SETUP

Output: "Setup"
### Test value 2
Input: PLACEMENT

Output: "Placement"
### Test value 3
Input: SCRAMBLE

Output: "Scramble"
### Test value 4
Input: ATTACK

Output: "Attack"
### Test value 5

Input: FORTIFY
### Test value 6
Input: GAME_OVER

Output: "Game over"