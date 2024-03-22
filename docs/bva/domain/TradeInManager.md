# method: startTrade(Set<Card> attemptedCards):boolean

## BVA Step 1
Input: a set of cards that the user selects to be traded in for troops

Output: yes and no values for whether the "trade" operation succeeds and finishes

## BVA Step 2
Input: Collection (should be a collection of 3 Card objects)

Output: Boolean

## BVA Step 3
Input:
- empty collection
- collection with exactly 1 card
- collection with exactly 3 cards
- collection with exactly 4 cards

- collection containing any wild card
- collection containing 2 wild cards (based on my understanding should pass)
- collection containing no infantry cards
- collection containing no calvary cards
- collection containing no artillery cards
- collection containing only infantry cards
- collection containing only cavalry cards
- collection containing only artillery cards
- collection containing one of each cards (infantry, calvary, artillery)

Output: true or false

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
- collection containing 2 wild cards (based on my understanding should pass)
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

Output: false


# method: calculateNumNewPieces():int

## BVA Step 1
Input: the number of already traded-in sets

Output: the number of new army pieces the player should receive

## BVA Step 2
Input: Count

Output: Count (cases)

## BVA Step 3
Input: 0, 1, 2, 3, 4, 5, 6, maximum possible value 429496731 (theoretically: (Integer.MaxInt-15)/5 + 5), max value +1 = 429496732

Output: 4, 6, 8, 10, 12, 15, 20, greatest multiple of 5 less than MaxInt = 2147483645, exception

## BVA Step 4
### Test value 1
Input: 0

Output: 4

### Test value 2
Input: 1

Output: 6

### Test value 3
Input: 2

Output: 8

### Test value 4
Input: 3

Output: 10

### Test value 5
Input: 4

Output: 12

### Test value 6
Input: 5

Output: 15

### Test value 7
Input: 6

Output: 20

### Test value 8
Input: 429496731

Output: 2147483645

### Test value 9
Input: 429496732

Output: exception


# method: updateSetsTradedIn():boolean

## BVA Step 1
Input: the number of already traded-in sets

Output: true or false based on whether the number of traded-in sets is updated

## BVA Step 2
Input: Count

Output: Boolean

## BVA Step 3
Input: 0, 1, >1, MaxInt -1, MaxInt

Output: integers >= 1, exception

## BVA Step 4
### Test value 1
Input: 0

Output: 1

### Test value 2
Input: 1

Output: 2

### Test value 3
Input: 10

Output: 11

### Test value 4
Input: MaxInt - 1

Output: MaxInt

### Test value 5
Input: MaxInt

Output: exception