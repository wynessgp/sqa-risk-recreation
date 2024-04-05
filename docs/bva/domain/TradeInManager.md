# method: startTrade(Set<Card> attemptedCards):int

## BVA Step 1
Input: a set of cards that the user selects to be traded in for troops, and number of previously traded-in set of cards

(Only 42 cards, so maximum 14 sets of cards to trade in)

Output: 
- the number of armies that the player will receive
- illegal argument exception
- illegal state exception

## BVA Step 2
Input: Collection, Count

Output: Cases

## BVA Step 3
Input:
- traded-in sets:
  - 0, 4, 5, 13, 14 (Based on the behavior of expected output in different cases)
- collection size:
  - collection with exactly 2 card
  - collection with exactly 3 cards
  - collection with exactly 4 cards
- collection content:
  - collection containing one wild card
  - collection containing more than one wild cards (based on my understanding should pass if size == 3)
  - collection containing no infantry cards
  - collection containing no calvary cards
  - collection containing no artillery cards
  - collection containing only infantry cards
  - collection containing only cavalry cards
  - collection containing only artillery cards
  - collection containing one of each card (infantry, calvary, artillery)

Output: (4, 12, 15, 55, illegal state exception)
- for traded-in sets in [0, 4]: output = 4 + (2 * input)
- for traded-in sets in [5, 13]: output = 15 + (5 * (input-5))
- for traded-in sets > 13: illegal state exception (Should not have enough cards to be traded in more than 14 times)
- Illegal Argument Exception (Has to have exactly 3 cards to trade in)

## BVA Step 4
### Test value 1
Input: one infantry card and one artillery card collection

Output: illegal argument exception (Has to have exactly 3 cards to trade in)

### Test value 2
Input: one wild card and one cavalry card collection

Output: illegal argument exception (Has to have exactly 3 cards to trade in)

### Test value 3
Input: collection with two wild cards and two infantry cards

Output: illegal argument exception (Has to have exactly 3 cards to trade in)

### Test value 4
Input: collection with two infantry cards and two cavalry cards

Output: illegal argument exception (Has to have exactly 3 cards to trade in)

### Test value 5
Input: collection with one artillery card and three infantry cards

Output: illegal argument exception (Has to have exactly 3 cards to trade in)

### Test value 6
Input: collection with one wild card, one infantry card, one calvary card, and one artillery card

Output: illegal argument exception (Has to have exactly 3 cards to trade in)

### Test value 7-10
Input: 0, 4, 5, 13 traded-in sets, collection with one infantry card and two cavalry cards

Output: 0

### Test value 11-14
Input: 0, 4, 5, 13 traded-in sets, collection with one wild card and two infantry cards

Output: 4, 12, 15, 55

### Test value 15-18
Input: 0, 4, 5, 13 traded-in sets, collection with one wild card, one cavalry card, and one artillery card

Output: 4, 12, 15, 55

### Test value 19-22
Input: 0, 4, 5, 13 traded-in sets, collection with two wild card and one infantry cards

Output: 4, 12, 15, 55

### Test value 23-26
Input: 0, 4, 5, 13 traded-in sets, collection with one infantry card, one calvary card, and one artillery card

Output: 4, 12, 15, 55

### Test value 27-30
Input: 0, 4, 5, 13 traded-in sets, size 3 collection with only infantry

Output: 4, 12, 15, 55

### Test value 31-34
Input: 0, 4, 5, 13 traded-in sets, size 3 collection with only cavalry

Output: 4, 12, 15, 55

### Test value 35-38
Input: 0, 4, 5, 13 traded-in sets, size 3 collection with only artillery

Output: 4, 12, 15, 55

### Test value 39
Input: 14 traded-in sets, collection with one infantry card and two cavalry cards

Output: illegal state exception (Should not have enough cards to be traded in more than 14 times)

### Test value 40
Input: 14 traded-in sets, collection with one wild card and two infantry cards

Output: illegal state exception (Should not have enough cards to be traded in more than 14 times)

### Test value 41
Input: 14 traded-in sets, collection with one wild card, one cavalry card, and one artillery card

Output: illegal state exception (Should not have enough cards to be traded in more than 14 times)

### Test value 42
Input: 14 traded-in sets, collection with two wild card and one infantry cards

Output: illegal state exception (Should not have enough cards to be traded in more than 14 times)

### Test value 43
Input: 14 traded-in sets, collection with one infantry card, one calvary card, and one artillery card

Output: illegal state exception (Should not have enough cards to be traded in more than 14 times)

### Test value 44
Input: 14 traded-in sets, size 3 collection with only infantry

Output: illegal state exception (Should not have enough cards to be traded in more than 14 times)

### Test value 45
Input: 14 traded-in sets, size 3 collection with only cavalry

Output: illegal state exception (Should not have enough cards to be traded in more than 14 times)

### Test value 46
Input: 14 traded-in sets, size 3 collection with only artillery

Output: illegal state exception (Should not have enough cards to be traded in more than 14 times)


# method: verifyValidCombo(attemptedCards: Set<Card>):boolean

## BVA Step 1
Input: the set of 3 cards that the player chooses to trade in for troops

Output: 
- "yes" if the set is a valid set to be traded, else "no"
- Illegal Argument Exception

## BVA Step 2
Input: Collection

Output: Boolean

## BVA Step 3
Input:
- collection with exactly 2 card
- collection with exactly 3 cards
- collection with exactly 4 cards

- collection containing any wild card
- collection containing more than one wild card (based on my understanding should pass if set size == 3)
- collection containing no infantry cards
- collection containing no calvary cards
- collection containing no artillery cards
- collection containing only infantry cards
- collection containing only cavalry cards
- collection containing only artillery cards
- collection containing one of each card (infantry, calvary, artillery)

Output:
- true or false
- Illegal argument exception (Has to have exactly 3 cards to trade in)

## BVA Step 4
### Test value 1
Input: collection with two wild cards

Output: illegal argument exception (Has to have exactly 3 cards to trade in)

### Test value 2
Input: collection with one wild card and one infantry card

Output: illegal argument exception (Has to have exactly 3 cards to trade in)

### Test value 3
Input: collection with two infantry card

Output: illegal argument exception (Has to have exactly 3 cards to trade in)

### Test value 4
Input: collection with two calvary card

Output: illegal argument exception (Has to have exactly 3 cards to trade in)

### Test value 5
Input: collection with two artillery card

Output: illegal argument exception (Has to have exactly 3 cards to trade in)

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
Input: size 3 collection with only infantry

Output: true

### Test value 13
Input: size 3 collection with only cavalry

Output: true

### Test value 14
Input: size 3 collection with only artillery

Output: true

### Test value 15
Input: collection with two wild cards and two infantry cards

Output: illegal argument exception (Has to have exactly 3 cards to trade in)

### Test value 16
Input: collection with two infantry cards and two calvary cards

Output: illegal argument exception (Has to have exactly 3 cards to trade in)

### Test value 17
Input: collection with one artillery card and three infantry cards

Output: illegal argument exception (Has to have exactly 3 cards to trade in)

### Test value 18
Input: collection with one wild card, one infantry card, one calvary card, and one infantry card

Output: illegal argument exception (Has to have exactly 3 cards to trade in)


# method: calculateNumNewPieces():int

## BVA Step 1
Input: the number of previously traded-in sets (maximum 13 for the call to be successful since only 44 cards in total)

Output: 
- the number of new army pieces the player will receive
- Illegal State Exception

## BVA Step 2
Input: Count

Output: Cases

## BVA Step 3
Input: 0, 4, 5, 13, 14 (based on the interval for expected output characteristic)

Output: (4, 12, 15, 55, illegal state exception)
- for input in [0, 4]: output = 4 + (2 * input)
- for input in [5, 13]: output = 15 + (5 * (input-5))
- for input > 13: illegal state exception (Should not have enough cards to be traded in more than 14 times)
## BVA Step 4
### Test value 1
Input: 0

Output: 4

### Test value 2
Input: 4

Output: 12

### Test value 3
Input: 5

Output: 15

### Test value 4
Input: 13

Output: 55

### Test value 5
Input: 14

Output: illegal state exception (Should not have enough cards to be traded in more than 14 times)


# method: updateSetsTradedIn():boolean

## BVA Step 1
Input: the number of previously traded-in sets

Output: true or false based on whether the number of traded-in sets is updated

## BVA Step 2
Input: Count

Output: Boolean

## BVA Step 3
Input: 0, 1, 5 (> 1), 13 (max - 1), 14 (max)

Output: true or false

## BVA Step 4
### Test value 1
Input: 0

Output: true

### Test value 2
Input: 1

Output: true

### Test value 3
Input: 5

Output: true

### Test value 4
Input: 13

Output: true

### Test value 5
Input: 14

Output: false