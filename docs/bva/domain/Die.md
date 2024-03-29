# method: rollSingleDie(randomizer: Random): Integer

## BVA Step 1
Input: A randomizer object with which to use for the Die rolling calculation, the maximum/minimum number the die may roll

Output: The result of the current Die getting rolled

## BVA Step 2
Input: Pointer (to a Random object)

Output: Counts 
- Ranges from [1, maxNumberPossible]

## BVA Step 3
Input: Pointer
- The null pointer (will not be considered)
- A pointer to the true object

Output: Counts
- -1 (can't set)
- 0 (can't set, there is no "0" on a 6-sided die) 
- 1
- \> 1
- Max possible value: maxNumberPossible
- One larger than maxNumberPossible (can't set)

## BVA Step 4
### Test 1:
- Input: 
    - randomizer = valid object
    - maxNumberPossible = 6
    - minNumberPossible = 1
- Output: 1
### Test 2:
- Input:
    - randomizer = valid object
    - maxNumberPossible = 6
    - minNumberPossible = 1
- Output: 2
### Test 3:
- Input: 
    - randomizer = valid object
    - maxNumberPossible = 6
    - minNumberPossible = 1
- Output: 6
### Test 4:
(test that dice can go above 6, if we desire)
- Input:
    - randomizer = valid object
    - maxNumberPossible = 27
- Output: 14 

