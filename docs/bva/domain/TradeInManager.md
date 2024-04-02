# method: startTrade(Set<Card> attemptedCards):int

## BVA Step 1
Input: a set of cards that the user selects to be traded in for troops, and number of already traded-in set of cards

Output: the number of armies that the player should receive

## BVA Step 2
Input: Collection, count (cases)

Output: Count (cases)

## BVA Step 3
Input:
- traded-in sets:
  - 0, 1, 2, 3, 4, 5, 6, 13, 14
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
  - collection containing one of each cards (infantry, calvary, artillery)

Output: 
- 0, 4, 6, 8, 10, 12, 15, 20, 25, 30, 35, 40, 45, 50, 55
- Illegal Argument Exception, Illegal State Exception

## BVA Step 4
(Note: will be using Each-choice to test, instead of All-combination)
### Test value 1
Input: 0 traded-in sets, with one infantry card and one artillery card

Output: illegal argument exception

### Test value 2
Input: 1 traded-in set, collection with one cavalry card and one wild card

Output: illegal argument exception

### Test value 3
Input: 5 traded-in sets, collection with two infantry card

Output: illegal argument exception

### Test value 4
Input: 13 traded-in sets, collection with two cavalry card

Output: illegal argument exception

### Test value 5
Input: 14 traded-in sets, collection with two artillery card

Output: illegal state exception

### Test value 6
Input: 2 traded-in sets, collection with one infantry card and two cavalry cards

Output: 0

### Test value 7
Input: 0 traded-in sets, collection with one wild card and two infantry cards

Output: 4

### Test value 8
Input: 13 traded-in sets, collection with one wild card, one cavalry card, and one artillery card

Output: 55

### Test value 9
Input: 14 traded-in sets, collection with one wild card, one cavalry card, and one artillery card

Output: illegal state exception

### Test value 10
Input: 3 traded-in sets, collection with two wild card and one infantry cards

Output: 10

### Test value 11
Input: 5 traded-in sets, collection with one infantry card, one calvary card, and one artillery card

Output: 15

### Test value 12
Input: 6 traded-in sets, size 3 collection with only infantry

Output: 20

### Test value 13
Input: 13 traded-in sets, size 3 collection with only cavalry

Output: 55

### Test value 14
Input: 14 traded-in sets, size 3 collection with only artillery

Output: illegal state exception

### Test value 15
Input: 0 traded-in sets, collection with two wild cards and two infantry cards

Output: illegal argument exception

### Test value 16
Input: 3 traded-in sets, collection with two infantry cards and two cavalry cards

Output: illegal argument exception

### Test value 17
Input: 13 traded-in sets, collection with one artillery card and three infantry cards

Output: illegal argument exception

### Test value 18
Input: 14 traded-in sets, collection with one wild card, one infantry card, one calvary card, and one artillery card

Output: illegal state exception


# method: verifyValidCombo(attemptedCards: Set<Card>):boolean

## BVA Step 1
Input: the set of 3 cards that the player chooses to trade in for troops

Output: "yes" if the set is a valid set to be traded, else "no"

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
- Illegal argument exception

## BVA Step 4
### Test value 1
Input: collection with two wild cards

Output: illegal argument exception

### Test value 2
Input: collection with one wild card and one infantry card

Output: illegal argument exception

### Test value 3
Input: collection with two infantry card

Output: illegal argument exception

### Test value 4
Input: collection with two calvary card

Output: illegal argument exception

### Test value 5
Input: collection with two artillery card

Output: illegal argument exception

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

Output: illegal argument exception

### Test value 16
Input: collection with two infantry cards and two calvary cards

Output: illegal argument exception

### Test value 17
Input: collection with one artillery card and three infantry cards

Output: illegal argument exception

### Test value 18
Input: collection with one wild card, one infantry card, one calvary card, and one infantry card

Output: illegal argument exception


# method: calculateNumNewPieces():int

## BVA Step 1
Input: the number of already traded-in sets

Output: the number of new army pieces the player should receive

## BVA Step 2
Input: Count (cases)

Output: Count (cases)

## BVA Step 3
Input: 0, 1, 2, 3, 4, 5, 6, 13, 14

Output: 4, 6, 8, 10, 12, 15, 20, 55, illegal state exception

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
Input: 13

Output: 55

### Test value 9
Input: 14

Output: illegal state exception


# method: updateSetsTradedIn():boolean

## BVA Step 1
Input: the number of already traded-in sets

Output: true or false based on whether the number of traded-in sets is updated

## BVA Step 2
Input: Count

Output: Boolean

## BVA Step 3
Input: 0, 1, >1, 13 (max - 1), 14 (max)

Output: true or false

## BVA Step 4
### Test value 1
Input: 0

Output: true

### Test value 2
Input: 1

Output: true

### Test value 3
Input: 10

Output: true

### Test value 4
Input: 13

Output: true

### Test value 5
Input: 14

Output: false