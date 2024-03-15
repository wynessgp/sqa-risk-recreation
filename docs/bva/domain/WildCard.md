# Method: `public boolean matchesTerritory(TerritoryType territory)`

## BVA Step 1
Input: Passes in a selected territory from the Risk board

Output: True/false as to whether it matches the territory of the wild card (will always be true)

## BVA Step 2
Input: Cases (TerritoryType is an enum)

Output: Boolean (0, 1)

## BVA Step 3
Input: Cases (Enum)
- The first possibility: Afghanistan
- The second possibility: Alaska
- Possibilities [3, 41]: ...
- The 42nd possibility: Yakutsk
- The 0th, 43rd possibility (not possible toset)

Output: Boolean
- 0 (will not be possible to achieve)
- 1

## BVA Step 4
### Note:
I am not explicitly enumerating all 42 territories. I will
be doing each choice for a territory on each continent; since the result should always be true.
### Test 1:
- Input: Territory = Afghanistan
- Output: Boolean = 1
### Test 2:
- Input: Territory = Alaska
- Output: Boolean = 1
### Test 3: 
- Input: Territory = Western Europe
- Output: Boolean = 1
### Test 4:
- Input: Territory = Western Australia
- Output: Boolean = 1
### Test 5:
- Input: Territory = Argentina
- Output: Boolean = 1
### Test 6:
- Input: Territory = Egypt
- Output: Boolean = 1

# Method: `public boolean matchesPieceType(PieceType pieceType)`

## BVA Step 1
Input: Passes in an army type (Infantry, Cavalry, Artillery) from the game 

Output: True/false as to whether the army type matches the one on the card (will always be true for wild cards)

## BVA Step 2
Input: Cases (PieceType is an enum)

Output: Boolean (0, 1)

## BVA Step 3
Input: Cases (enum)
- The first possibility: Infantry
- The second possibility: Cavalry
- The third possibility: Artillery
- The 0th, 4th possibility (not possible to set)

Output: Boolean
- 0 (will not be possible to achieve)
- 1

## BVA Step 4
### Test 1:
- Input: Army = Infantry
- Output: Boolean = 1
### Test 2:
- Input: Army = Cavalry
    - Output: Boolean = 1
### Test 3:
    - Input: Army = Artillery
    - Output: Boolean = 1

# Method: `public boolean isWild()`

## BVA Step 1
Input: Card (object)

Output: True/false as to whether the card is a wild card (will always be true for wild cards)

## BVA Step 2
Input: None

Output: Boolean (0, 1)

## BVA Step 3
Input: N/A

Output: Boolean
- 0 (will not be possible to achieve)
- 1

## BVA Step 4
### Test 1:
- Input: N/A
- Output: Boolean = 1
