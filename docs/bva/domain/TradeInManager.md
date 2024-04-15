# method: `startTrade(attemptedCards: Set<Card>): int`

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
- IllegalStateException

## BVA Step 4
### Test value 1
Input: 0 previous trades, empty collection

Output: IllegalStateException "must trade in exactly 3 cards"

### Test value 2
Input: 0 previous trades, collection with one card

Output: IllegalStateException "must trade in exactly 3 cards"

### Test value 3
Input: 0 previous trades, collection with two cards

Output: IllegalStateException "must trade in exactly 3 cards"

### Test value 4
Input: 0 previous trades, collection with four cards

Output: IllegalStateException "must trade in exactly 3 cards"

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

Output: IllegalStateException "no more cards to trade in"

### Test value 20
Input: 2 previous trades, collection with one infantry and two calvary cards

Output: IllegalStateException "invalid trade in set"

### Test value 21
Input: 2 previous trades, collection with one infantry and two artillery cards

Output: IllegalStateException "invalid trade in set"

### Test value 22
Input: 2 previous trades, collection with one calvary and two artillery cards

Output: IllegalStateException "invalid trade in set"

### Test value 23
Input: 2 previous trades, collection with one cavalry and two infantry cards

Output: IllegalStateException "invalid trade in set"

### Test value 24
Input: 2 previous trades, collection with one artillery and two infantry cards

Output: IllegalStateException "invalid trade in set"

### Test value 25
Input: 2 previous trades, collection with one artillery and two cavalry cards

Output: IllegalStateException "invalid trade in set"

### Test value 26
Input: 2 previous trades, collection with two wild and one infantry cards

Output: IllegalStateException "invalid trade in set"


# method: `getMatchedTerritories(player: Player, cards: Set<Card>): Set<TerritoryType>`

## BVA Step 1
Input: set of cards to check and the player who traded them in

Output: set of territories from the cards that match what the player occupies or an exception

## BVA Step 2
Input: Pointer, Collection

Output: Collection

## BVA Step 3
Input:
- Player pointer:
  - occupies 0 territories
  - occupies 1 territory
  - occupies 2 territories
  - occupies 3 territories
  - occupies 41 territories
- collection of Cards:
  - empty collection
  - collection with one card
  - collection with two cards
  - collection of size three with one wild and two different cards (by TerritoryType)
    - contains one and two cards that match a territory the player occupies
    - none of the cards match territories the player occupies
  - collection of size three with three different non-wild cards
    - contains one, two, and three cards that match a territory the player occupies
    - none of the cards match territories the player occupies
  - collection with four cards

Output:
- empty collection
- collection with one TerritoryType
- collection with two TerritoryTypes
- collection with three TerritoryTypes
- IllegalStateException

## BVA Step 4
### Test value 1
Input: player with 1 territory, empty collection

Output: IllegalStateException "invalid number of cards"

### Test value 2
Input: player with 1 territory, collection with one card

Output: IllegalStateException "invalid number of cards"

### Test value 3
Input: player with 1 territory, collection with two cards

Output: IllegalStateException "invalid number of cards"

### Test value 4
Input: player with 1 territory, collection with four cards

Output: IllegalStateException "invalid number of cards"

### Test value 6
Input: player that occupies no territories, collection with [WILD, ALASKA, ALBERTA]

Output: [] (empty collection)

### Test value 7
Input: player that occupies [ALASKA], collection with [WILD, ALASKA, ALBERTA]

Output: [ALASKA]

### Test value 8
Input: player that occupies [ALASKA], collection with [ALBERTA, CENTRAL_AMERICA, EASTERN_UNITED_STATES]

Output: []

### Test value 9
Input: player that occupies [ALASKA, ALBERTA], collection with [WILD, ALASKA, ALBERTA]

Output: [ALASKA, ALBERTA]

### Test value 10
Input: player that occupies [ALBERTA, CENTRAL_AMERICA, EASTERN_UNITED_STATES], collection with [ALBERTA, CENTRAL_AMERICA, EASTERN_UNITED_STATES]

Output: [ALBERTA, CENTRAL_AMERICA, EASTERN_UNITED_STATES]

### Test value 11
Input: player that occupies [ALBERTA, CENTRAL_AMERICA], collection with [ALASKA, ALBERTA, CENTRAL_AMERICA]

Output: [ALBERTA, CENTRAL_AMERICA]

### Test value 12
Input: player that occupies [ALASKA], collection with [ALASKA, ALBERTA, EASTERN_UNITED_STATES]

Output: [ALASKA]

### Test value 13
Input: player that occupies [], collection with [ALASKA, ALBERTA, EASTERN_UNITED_STATES]

Output: []

### Test value 14
Input: player that occupies [ALASKA], collection with [WILD, ALBERTA, EASTERN_UNITED_STATES]

Output: []

### Test value 15
Input: player that occupies all territories, collection with [WILD, ALASKA, ALBERTA]

Output: [ALASKA, ALBERTA]

### Test value 16
Input: player that occupies all territories, collection with [ALASKA, ALBERTA, CENTRAL_AMERICA]

Output: [ALASKA, ALBERTA, CENTRAL_AMERICA]