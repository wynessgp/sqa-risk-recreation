# method: matchesTerritory(territory: TerritoryType): boolean

## BVA Step 1
Input: A territory option from the Risk board and territory pictured on the card

Output: Yes or no answer

## BVA Step 2
Input: Cases (TerritoryType enum) for both inputs

Output: Boolean

## BVA Step 3
Input: Each possibility for TerritoryType (42 possibilities). Note that anything outside of these choices is not considered a valid input.
- Alaska
- WesternCanada
- ...
- WesternAustralia

Output: 0 (false) or 1 (true)

## BVA Step 4
The tests will follow the following table. The card's current territory is in the first row, and the territory to check is in the first column. There will be 42^2 tests in total.

|   Card \ To check    | Alaska | WesternCanada | ... | WesternAustralia |
|:--------------------:|:------:|:-------------:|:---:|:----------------:|
|        Alaska        |   1    |       0       | ... |        0         |
|    WesternCanada     |   0    |       1       | ... |        0         |
|         ...          |  ...   |      ...      | ... |       ...        |
|   WesternAustralia   |   0    |       0       | ... |        1         |


# method: matchesPieceType(piece: PieceType): boolean

## BVA Step 1
Input: One of the three game piece types and the piece type shown on the card

Output: Yes or no answer

## BVA Step 2
Input: Cases (PieceType enum) for both inputs

Output: Boolean

## BVA Step 3
Input: Each possibility for PieceType (artillery, cavalry, infantry). Note that anything outside of these choices is not considered a valid input.

Output: 0 (false) or 1 (true)

## BVA Step 4
### Test value 1
Input: Card shows artillery, checking for artillery

Output: 1
### Test value 2
Input: Card shows artillery, checking for cavalry

Output: 0
### Test value 3
Input: Card shows artillery, checking for infantry

Output: 0
### Test value 4
Input: Card shows cavalry, checking for artillery

Output: 0
### Test value 5
Input: Card shows cavalry, checking for cavalry

Output: 1
### Test value 6
Input: Card shows cavalry, checking for infantry

Output: 0
### Test value 7
Input: Card shows infantry, checking for artillery

Output: 0
### Test value 8
Input: Card shows infantry, checking for cavalry

Output: 0
### Test value 9
Input: Card shows infantry, checking for infantry

Output: 1

# method: isWild(): boolean

## BVA Step 1
Input: None

Output: Yes or no answer

## BVA Step 2
Input: None

Output: Boolean

## BVA Step 3
Input: None

Output: 0 (false) or 1 (true, unobtainable)

## BVA Step 4
### Test value 1
Input: None

Output: 0
