# method: `buildDiceLists(): boolean`

## BVA Step 1
Input: N/A

Output: A yes/no answer whether the dice lists for attacker, defender were properly initialized.

## BVA Step 2
Input: N/A

Output: Boolean (0, 1)

## BVA Step 3
Input: N/A

Output: Boolean
- 0 (should never happen)
- 1

## BVA Step 4
### Test 1:
- Output = 1 (true)

# method: `rollDiceToDeterminePlayerOrder(amountOfDiceToRoll: int): List<Integer>`

## BVA Step 1
Input: The amount of dice the game needs to roll to determine when each player goes

Output: A list which determines when each player goes, by index. 

## BVA Step 2
Input: 
- amountOfDiceToRoll: Counts [2, 6]
- Set up die: Pointer

Output: Collection 

## BVA Step 3
Input: 
- amountOfDiceToRoll (Counts)
  - -1 (error case)
  - 0 (error case)
  - 1 (error case)
  - 2 (RISK rules minimum allowed amount)
  - \> 2
  - Maximum possible value: 6 (RISK rules maximum allowed amount)
- Setup die: Pointer
  - Null pointer (will not be considered; die is generated upon request)
  - A pointer to the true object

Output: Collection
- An empty collection (ignored, will always have set up dice)
- Contains exactly one element (ignored)
- Contains exactly 2 elements
- Contains \> 2 elements
- Maximum possible size (Limited to 6 by RISK rules)
- No duplicates (REQUIRED!)

## BVA Step 4
### Test 1:
- Input:
  - amountOfDiceToRoll: -1
  - Set up die = [] (doesn't matter)
- Output: IllegalArgumentException
  - message: "Valid amount of dice for player setup roll must be in the range [3, 6]"
### Test 2:
- Input:
  - amountOfDiceToRoll: 0
  - Set up die = [] (doesn't matter)
- Output: IllegalArgumentException
  - message: "Valid amount of dice for player setup roll must be in the range [3, 6]"
### Test 3:
- Input:
  - amountOfDiceToRoll: 7
  - Set up die = [] (doesn't matter)
- Output: IllegalArgumentException
  - message: "Valid amount of dice for player setup roll must be in the range [3, 6]"
### Test 4:
- Input:
  - amountOfDiceToRoll: 2
  - Set up die = [] (doesn't matter)
- Output: IllegalArgumentException
  - message: "Valid amount of dice for player setup roll must be in the range [3, 6]"
### Test 5:
- Input:
  - amountOfDiceToRoll: 3
  - Set up die = valid 6-sided die
- Output:
  - Must contain numbers from (0, 6]
  - Must not have any duplicates
  - Output = [1, 2, 6]
### Test 6:
- Input:
  - amountOfDiceToRoll: 4
  - Set up die = valid 6-sided die
- Output:
  - Must contain numbers from (0, 6]
  - Must not have any duplicates
  - Output = [2, 6, 4, 1]
### Test 7:
- Input:
  - amountOfDiceToRoll: 5
  - Set up die = valid 6-sided die
- Output:
  - Must contain numbers from (0, 6]
  - Must not have any duplicates
  - Output = [5, 4, 3, 2, 6]
### Test 8:
- Input:
  - amountOfDiceToRoll: 6
  - Set up die = valid 6-sided die
- Output:
  - Must contain numbers from (0, 6]
  - Must not have any duplicates
  - Output = [6, 1, 2, 5, 4, 3]
### Test 9
- Input:
  - amountOfDiceToRoll: 4
  - Set up die = valid 6-sided die (with mocked roll order [1, 2, 3, 3, 4])
- Output:
  - Must contain numbers from (0, 6]
  - Must not have any duplicates
  - Output = [1, 2, 3, 4]

# method: `rollAttackerDice(amountOfDiceToRoll: int): List<Integer>`

## BVA Step 1
Input: The amount of dice the attacker is rolling

Output: The results of the dice rolls, sorted in non-increasing order.

## BVA Step 2
Input: 
- amountOfDiceToRoll: Counts [1, 3] <!-- consider more -->
- Underlying attacker dice storage: Collection (of pointers)

Output:
- A sorted collection (structured collection?)

## BVA Step 3
Input: 
- amountOfDiceToRoll: Counts
  - -1 (error case)
  - 0 (error case)
  - 1 
  - \> 1
  - Maximum possible value: 3
  - One more than max possible value: 4 (error case)
- Underlying dice storage: Collection
  - An empty collection (ignored, should always be 3 by domain)
  - Contains exactly one element (ignored)
  - Contains \> 1 element (ignored)
  - Maximum possible size (Limited to 3 by RISK rules) 
  - Using the first element (index 0)
  - Using the last element (index 2)

Output: Collection
- An empty collection (shouldn't be possible; error case)
- Contains exactly one element 
- Contains \> 1 element
- Maximum possible size (Limited to 3 by RISK rules)
- Is sorted in non-increasing order 
- Unsorted (can't set, error case)

## BVA Step 4
### Test 1:
- Input:
  - amountOfDiceToRoll = -1 
  - Collection = [valid 6-sided die, valid 6-sided die, valid 6-sided die]
- Output: IllegalArgumentException 
  - message: "Valid amount of dice for attacker roll must be in the range [1, 3]"
### Test 2:
- Input:
  - amountOfDiceToRoll = 0
  - Collection = [valid 6-sided die, valid 6-sided die, valid 6-sided die]
- Output: IllegalArgumentException
  - message: "Valid amount of dice for attacker roll must be in the range [1, 3]"
### Test 3:
- Input:
  - amountOfDiceToRoll = 4
  - Collection = [valid 6-sided die, valid 6-sided die, valid 6-sided die]
- Output: IllegalArgumentException
  - message: "Valid amount of dice for attacker roll must be in the range [1, 3]"
### Test 4:
- Input:
  - amountOfDiceToRoll = 3
  - Collection = [valid 6-sided die, valid 6-sided die, valid 6-sided die]
- Output: Collection = [3, 2, 1]
### Test 5:
- Input:
  - amountOfDiceToRoll = 2
  - Collection = [valid 6-sided die, valid 6-sided die, valid 6-sided die]
- Output: Collection = [4, 4]
### Test 6:
- Input:
  - amountOfDiceToRoll = 1
  - Collection = [valid 6-sided die, valid 6-sided die, valid 6-sided die]
- Output: Collection = [3]
### Test 7: (testing without pre-chosen values for dice)
- Input:
  - amountOfDiceToRoll = 2
  - Collection = [valid 6-sided die, valid 6-sided die, valid 6-sided die]
- Output: Collection = [ [1-6], number less than 1st one]

# method: `rollDefenderDice(amountOfDiceToRoll: int): List<Integer>`

## BVA Step 1
Input: The amount of dice the defender is rolling

Output: The results of the dice rolls, sorted in non-increasing order.

## BVA Step 2
Input: 
- amountOfDiceToRoll: Counts [1, 2]
- Underlying attacker dice storage: Collection

Output:
- A sorted collection (structured collection?)

## BVA Step 3
Input:
- amountOfDiceToRoll: Counts
  - -1 (error case)
  - 0 (error case)
  - 1
  - Maximum possible value: 2
  - One more than max possible value: 3 (error case)
- Underlying dice storage: Collection
  - An empty collection (ignored, should always have 2 defender dice)
  - Contains exactly one element (ignored)
  - Contains \> 1 element (ignored)
  - Maximum possible size (Limited to 2 by RISK rules) 
  - Using the first element (index 0)
  - Using the last element (index 1)

Output: Collection
- An empty collection (shouldn't be possible; error case)
- Contains exactly one element
- Contains \> 1 element
- Maximum possible size (Limited to 2 by RISK rules)
- Is sorted in non-increasing order
- Unsorted (can't set, error case)

## BVA Step 4
### Test 1:
- Input:
  - amountOfDiceToRoll = -1
  - Collection = [valid 6-sided die, valid 6-sided die]
- Output: IllegalArgumentException
  - message: "Valid amount of dice for defender roll must be in the range [1, 2]"
### Test 2:
- Input:
  - amountOfDiceToRoll = 0
  - Collection = [valid 6-sided die, valid 6-sided die]
- Output: IllegalArgumentException
  - message: "Valid amount of dice for defender roll must be in the range [1, 2]"
### Test 3:
- Input:
  - amountOfDiceToRoll = 3
  - Collection = [valid 6-sided die, valid 6-sided die]
- Output: IllegalArgumentException
  - message: "Valid amount of dice for defender roll must be in the range [1, 2]"
### Test 4:
- Input:
  - amountOfDiceToRoll = 2
  - Collection = [valid 6-sided die, valid 6-sided die]
- Output: Collection = [3, 2]
### Test 5:
- Input:
  - amountOfDiceToRoll = 2
  - Collection = [valid 6-sided die, valid 6-sided die]
- Output: Collection = [4, 4]
### Test 6:
- Input:
  - amountOfDiceToRoll = 1
  - Collection = [valid 6-sided die, valid 6-sided die]
- Output: Collection = [3]
### Test 7: (testing without pre-chosen values for dice)
- Input:
  - amountOfDiceToRoll = 2
  - Collection = [valid 6-sided die, valid 6-sided die]
- Output: Collection = [ [1-6], number less than 1st one]

# method: `generateBattleResults(defenderRolls: List<Integer>, attackerRolls: List<Integer>): List<BattleResult>`

## BVA Step 1
Input: The results of both the defender and attacker rolling their respective dice in the current battle

Output: The RISK rules determined result for what happened in each individual dice battle
- Namely, do a pairwise comparison of each side's dice when sorted in non-increasing order. 
  - If the defender's roll \>= attacker's roll, the defender wins that battle.
  - If the defender's roll \< attacker's roll, the attacker wins that battle.
  - This should be done until the defender/attacker runs out of dice to pair with the opposing side.

## BVA Step 2
Input: 
- defenderRolls: Collection
- attackerRolls: Collection

Output:
- (overall) Collection
- BattleResult: Cases

## BVA Step 3
Input: 
- defenderRolls, attackerRolls (Collection):
  - An empty collection (error case)
  - Contains exactly one element
  - Contains \> 1 element
  - Maximum possible size (Limit of 2 for defenderRolls, 3 for attackerRolls by RISK rules)
  - Is sorted in non-increasing order (error case)
  - Unsorted (error case)
  - Duplicate entries
  - No duplicates

Output:
- Collection:
  - An empty collection (shouldn't be possible; error case)
  - Contains exactly one element
  - Maximum possible size (Limited to 2 by RISK rules; or number of defenderDice)
  - Has duplicates
  - No duplicates
- BattleResult (Cases):
  - The 1st possibility: ATTACKER_VICTORY
  - The 2nd possibility: DEFENDER_VICTORY
  - The 0th, 3rd possibility: (can't set, won't be considered due to it being an Enum)

## BVA Step 4
### Test 1:
- Input:
  - defenderRolls = [5], attackerRolls = []
- Output: IllegalArgumentException
  - message: "Both arguments must have at least 1 element"
### Test 2:
- Input:
  - defenderRolls = [], attackerRolls = [5]
- Output: IllegalArgumentException
  - message: "Both arguments must have at least 1 element"
### Test 3:
- Input:
  - defenderRolls = [], attackerRolls = []
- Output: IllegalArgumentException
  - message: "Both arguments must have at least 1 element"
### Test 4:
- Input:
  - defenderRolls = [1, 2], attackerRolls = [4]
- Output: IllegalArgumentException
  - message: "defenderRolls are not sorted in non-increasing order"
### Test 5:
- Input:
  - defenderRolls = [5], attackerRolls = [1, 2]
- Output: IllegalArgumentException
  - message: "attackerRolls are not sorted in non-increasing order"
### Test 6:
- Input:
  - defenderRolls = [5], attackerRolls = [5]
- Output: 
  - Collection = [DEFENDER_VICTORY]
### Test 7:
- Input:
  - defenderRolls = [6, 6], attackerRolls = [1]
- Output:
  - Collection = [DEFENDER_VICTORY]
### Test 8:
- Input:
  - defenderRolls = [6, 6], attackerRolls = [4, 3]
- Output:
  - Collection = [DEFENDER_VICTORY, DEFENDER_VICTORY]
### Test 9:
- Input:
  - defenderRolls = [6, 6], attackerRolls = [5, 3, 2]
- Output:
  - Collection = [DEFENDER_VICTORY, DEFENDER_VICTORY]
### Test 10:
- Input:
  - defenderRolls = [6, 2], attackerRolls = [4, 4, 3]
- Output:
  - Collection = [DEFENDER_VICTORY, ATTACKER_VICTORY]
### Test 11:
- Input:
  - defenderRolls = [5, 4], attackerRolls = [6, 2, 1]
- Output:
  - Collection = [ATTACKER_VICTORY, DEFENDER_VICTORY]
### Test 12:
- Input:
  - defenderRolls = [3], attackerRolls = [4]
- Output: 
  - Collection = [ATTACKER_VICTORY]
### Test 13:
- Input:
  - defenderRolls = [5, 5], attackerRolls = [6]
- Output: 
  - Collection = [ATTACKER_VICTORY]
### Test 14:
- Input:
  - defenderRolls = [2], attackerRolls = [4, 4]
- Output:
  - Collection = [ATTACKER_VICTORY]
### Test 15:
- Input:
  - defenderRolls = [2], attackerRolls = [5, 4, 3]
- Output:
  - Collection = [ATTACKER_VICTORY]
### Test 16:
- Input:
  - defenderRolls = [3, 2], attackerRolls = [5, 4]
- Output:
  - Collection = [ATTACKER_VICTORY, ATTACKER_VICTORY]
### Test 17:
- Input:
  - defenderRolls = [3, 2], attackerRolls = [6, 4, 4]
- Output:
  - Collection = [ATTACKER_VICTORY, ATTACKER_VICTORY]

# method: `validateSortIsInNonIncreasingOrder(List<Integer> listToCheck): boolean`

## BVA Step 1
Input: A list of integers which may not necessarily be in sorted order

Output: A yes/no answer if the provided list is in fact, sorted in non-increasing order.

## BVA Step 2
Input: Collection

Output: Boolean (0, 1)

## BVA Step 3
Input: Collection
- An empty collection (can't set, will be checked in generateBattleResults)
- Contains exactly one element 
- Contains \> 1 element 
- Maximum possible size (3 if attacker list, 2 if defender list) <!-- Consider expanding for flexiblity -->
- Using the first element (index 0)
- Using the last element (index 1 or 2)
- Contains duplicates
- No duplicates

Output: Boolean
- 0 
- 1

## BVA Step 4
### Test 1:
- Input: listToCheck = [5]
- Output: 1
### Test 2:
- Input: listToCheck = [4, 2]
- Output: 1
### Test 3:
- Input: listToCheck = [3, 3]
- Output: 1
### Test 4:
- Input: listToCheck = [6, 5, 1]
- Output: 1
### Test 5:
- Input: listToCheck = [2, 4]
- Output: 0
### Test 6:
- Input: listToCheck = [1, 3, 5]
- Output: 0