# method: rollSingleDie(randomizer: Random): Integer

<!-- No, I'm not trying to tell you to die with this markdown file. -->

## BVA Step 1
Input: A randomizer object with which to use for the Die rolling calculation, the maximum number my current die may roll (field on the class)

Output: The result of the current Die getting rolled

## BVA Step 2
Input: Pointer (to a Random object)

Output: Counts 
    - Ranges from [1, maxNumberPossible]

## BVA Step 3
Input: Pointer
    - The null pointer
    - A pointer to the true object

Output: Counts
    - -1 (can't set)
    - 0 (can't set, there is no "0" on a 6-sided die) <!-- If we want to include 0s, do a minimumNumberPossible field. -->
    - 1
    - > 1
    - Max possible value: maxNumberPossible
    - One larger than maxNumberPossible (can't set)

## BVA Step 4
### Test 1:
- Input: randomizer = NULL
- Output: NullPointerException (message: Randomizer object is null, cannot roll Die!)
### Test 2:
- Input: 
    - randomizer = valid object
    - maxNumberPossible = 6
- Output: 1
### Test 3:
- Input:
    - randomizer = valid object
    - maxNumberPossible = 6
- Output: 2
### Test 4:
- Input: 
    - randomizer = valid object
    - maxNumberPossible = 6
- Output: 6
### Test 5:
(test that dice can go above 6, if we desire)
- Input:
    - randomizer = valid object
    - maxNumberPossible = 27
- Output: 14 

