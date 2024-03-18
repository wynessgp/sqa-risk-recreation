# method: startTrade(Set<Card> attemptedCards):boolean

## BVA Step 1
Input: a set of cards that the user selects to be traded in for troops

Output: yes and no values for whether the "trade" operation succeeds and finishes

## BVA Step 2
Input: Collection (should be a collection of 3 elements)

Output: Boolean

## BVA Step 3
Input: 

Output:

## BVA Step 4
### Test value 1
...
### Test value 2
...
### Test value 3
...


# method: verifyValidCombo():boolean

## BVA Step 1
Input: the set of cards

Output: "yes" if the set is a valid set to be traded, else "no"

## BVA Step 2
Input: Collection

Output: Boolean

## BVA Step 3
Input: 
- empty collection
- collection with exactly 1 card
- collection with exactly 3 cards
- collection with exactly 4 cards

- collection containing any wild card
- collection containing no infantry cards
- collection containing no calvary cards
- collection containing no artillery cards
- collection containing only infantry cards
- collection containing only cavalry cards
- collection containing only artillery cards
- collection containing one of each cards (infantry, calvary, artillery)

Output: true/false

## BVA Step 4
### Test value 1
Input: empty collection

Output: false

### Test value 2
Input: collection with one wild card

Output: false

### Test value 3
Input: collection with one infantry card

Output: false

### Test value 4
Input: collection with one calvary card

Output: false

### Test value 5
Input: collection with one artillery card

Output: false

### Test value 6
Input: collection with one infantry card and two calvary cards

Output: false

### Test value 7
Input: collection with one calvary card and two artillery cards

Output: false

### Test value 8
Input: collection with one artillery card and two infantry cards

Output: false

### Test value 9
Input: collection with one wild card and two infantry cards

Output: true

### Test value 10
Input: collection with two wild card and one infantry cards

Output: true

### Test value 11
Input: collection with one infantry card, one calvary card, and one infantry card

Output: true

### Test value 12
Input: collection with two wild cards and two infantry cards

Output: false

### Test value 13
Input: collection with two infantry cards and two calvary cards

Output: false

### Test value 14
Input: collection with one artillery card and three infantry cards

Output: false

### Test value 15
Input: collection with one wild card, one infantry card, one calvary card, and one infantry card


# method: calculateNumNewPieces():int

## BVA Step 1
Input: The set of cards, and the number of already traded-in sets

Output: the number of new army pieces the player should receive

## BVA Step 2
Input: Collection, Count

Output: Count

## BVA Step 3
Input: 

Output:

## BVA Step 4
### Test value 1
...
### Test value 2
...
### Test value 3
...


# method: updateSetsTradedIn():boolean

## BVA Step 1
Input: the number of already traded-in sets

Output: true or false based on whether the action succeeds

## BVA Step 2
Input: Count

Output: Boolean

## BVA Step 3
Input: 

Output:

## BVA Step 4
### Test value 1
...
### Test value 2
...
### Test value 3
...


# method: clearSavedSet():boolean

## BVA Step 1
Input: 

Output:

## BVA Step 2
Input: 

Output:

## BVA Step 3
Input: 

Output:

## BVA Step 4
### Test value 1
...
### Test value 2
...
### Test value 3
...


