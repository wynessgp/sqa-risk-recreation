# method: startTrade(Set<Card> attemptedCards):int

## BVA Step 1
Input: set of cards to be traded in and number of previously traded in sets of cards

(Only 42 cards, so maximum 14 sets of cards to trade in)

Output: number of armies to receive, or an exception

## BVA Step 2
Input: Collection, Count

Output: Cases

## BVA Step 3
Input:
- traded in sets:
  - 0, 1, 2, ..., 13, 14
- collection of Cards:
  - empty collection
  - collection with one card
  - collection with two cards
  - collection of size three with one of each card (infantry, calvary, artillery)
  - collection with three of the same card
  - collection with one wild card and two of any other card
  - collection with one infantry and two cavalry cards
  - (repeat above for each combination of infantry, calvary, and artillery cards)
  - collection with two wild cards and one of any other card
  - collection with four cards

Output:
- 4, 6, 8, 10, 12, 15, 20, 25, 30, 35, 40, 45, 50, 55 (can only trade in 14 times as per game rules)
- IllegalArgumentException

## BVA Step 4
### Test value 1
Input: 0 previous trades, empty collection

Output: IllegalArgumentException "must trade in exactly 3 cards"

### Test value 2
Input: 0 previous trades, collection with one card

Output: IllegalArgumentException "must trade in exactly 3 cards"

### Test value 3
Input: 0 previous trades, collection with two cards

Output: IllegalArgumentException "must trade in exactly 3 cards"

### Test value 4
Input: 0 previous trades, collection with four cards

Output: IllegalArgumentException "must trade in exactly 3 cards"

### Test value 5
Input: 0 previous trades, collection with one infantry, one calvary, and one artillery card

Output: 4

### Test value 6
Input: 1 previous trade, collection with three infantry cards

Output: 6

### Test value 7
Input: 2 previous trades, collection with three calvary cards

Output: 8

### Test value 8
Input: 3 previous trades, collection with three artillery cards

Output: 10

### Test value 9
Input: 4 previous trades, collection with one wild and two infantry cards

Output: 12

### Test value 10
Input: 5 previous trades, collection with one wild and two calvary cards

Output: 15

### Test value 11
Input: 6 previous trades, collection with one wild and two artillery cards

Output: 20

### Test value 12
Input: 7 previous trades, collection with one wild, one infantry, and one calvary card

Output: 25

### Test value 13
Input: 8 previous trades, collection with one wild, one infantry, and one artillery card

Output: 30

### Test value 14
Input: 9 previous trades, collection with one wild, one calvary, and one artillery card

Output: 35

### Test value 15
Input: 10 previous trades, collection with one infantry, one calvary, and one artillery card

Output: 40

### Test value 16
Input: 11 previous trades, collection with one infantry, one calvary, and one artillery card

Output: 45

### Test value 17
Input: 12 previous trades, collection with one infantry, one calvary, and one artillery card

Output: 50

### Test value 18
Input: 13 previous trades, collection with one infantry, one calvary, and one artillery card

Output: 55

### Test value 19
Input: 14 previous trades, collection with one infantry, one calvary, and one artillery card

Output: IllegalArgumentException "no more cards to trade in"

### Test value 20
Input: 2 previous trades, collection with one infantry and two calvary cards

Output: IllegalArgumentException "invalid trade in set"

### Test value 21
Input: 2 previous trades, collection with one infantry and two artillery cards

Output: IllegalArgumentException "invalid trade in set"

### Test value 22
Input: 2 previous trades, collection with one calvary and two artillery cards

Output: IllegalArgumentException "invalid trade in set"

### Test value 23
Input: 2 previous trades, collection with one cavalry and two infantry cards

Output: IllegalArgumentException "invalid trade in set"

### Test value 24
Input: 2 previous trades, collection with one artillery and two infantry cards

Output: IllegalArgumentException "invalid trade in set"

### Test value 25
Input: 2 previous trades, collection with one artillery and two cavalry cards

Output: IllegalArgumentException "invalid trade in set"

### Test value 26
Input: 2 previous trades, collection with two wild and one infantry cards

Output: IllegalArgumentException "invalid trade in set"


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