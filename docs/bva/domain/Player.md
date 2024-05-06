# method: `ownsTerritory(territory: TerritoryType): boolean`

## BVA Step 1
Input: TerritoryType to check against the ones claimed by the player and the underlying set of territories

Output: yes or no answer if the TerritoryType exists in the set (of territories the player owns)

## BVA Step 2
Input:
- territory: Cases
- Player's owned territories: Collection

Output: Boolean

## BVA Step 3
Input:
- territory: Cases
    - ALASKA
    - ALBERTA
    - ...
    - WESTERN_AUSTRALIA
    - Any other option is not possible
- Player's owned territories: Collection
    - Empty collection
    - Collection with one TerritoryType
    - Collection with two TerritoryTypes
    - ...
    - Collection with all 42 TerritoryTypes
    - Duplicates are not possible because it's a set

Output:
- 0 (false)
- 1 (true)

## BVA Step 4
### Test value 1
Input: ALASKA, []

Output: 0 (false)

### Test value 2
Input: ALASKA, [ALASKA]

Output: 1 (true)

### Test value 3
Input: ALASKA, [ALBERTA, CENTRAL_AMERICA, ..., WESTERN_AUSTRALIA]

Output: 0

### Test value 4
Input: ALASKA, [ALASKA, ALBERTA, ..., WESTERN_AUSTRALIA]

Output: 1

### Repeat above tests for each TerritoryType

### Test value 5
Input: ALASKA, [ALASKA, ALBERTA]

Output: 1

### Repeat above test for each combination

# method: `ownsAllGivenCards(givenCards: Set<Card>): boolean`
Note: we will be utilizing a naive pointer equals for Cards here, since the cards should ONLY come from one
source in our code; namely, the RiskCardDeck

## BVA Step 1
Input: A collection of cards to check and see if the player actually owns all of them

Output: A yes/no answer whether the player does own all the cards in question

## BVA Step 2
Input:
- givenCards: Collection
- Cards the player owns: Collection

Output: Boolean

## BVA Step 3
Input:
- givenCards (Collection):
  - An empty collection
  - Collection with 1 element
  - Collection with \> 1 element
  - Collection with 44 elements (number of cards in the deck)
  - Duplicates are not possible
- Cards the player owns (Collection):
  - An empty collection (returns false)
  - Collection with 1 element
  - Collection with \> 1 element
  - Collection with 44 elements (max amount of cards in the deck)

Output: Boolean
- 0 (false)
- 1 (true)

## BVA Step 4
### Test 1:
Input: 
- givenCards = []
- Cards the player owns = []

Output: 1 (true)
### Test 2:
Input:
- givenCards = [ Wild Card ]
- Cards the player owns = []

Output: 0 (false)
### Test 3:
Input:
- givenCards = [ TerritoryCard = {ALASKA, INFANTRY} ]
- Cards the player owns = []

Output: 0 (false)
### Test 4:
Input:
- givenCards = [ Wild Card ]
- Cards the player owns = [ Wild Card ]

Output: 1 (true)
### Test 5:
Input:
- givenCards = [Wild Card, TerritoryCard = {ALASKA, INFANTRY}, TerritoryCard = {BRAZIL, ARTILLERY}]
- Cards the player owns = [ Wild Card, Territory Card = {BRAZIL, ARTILLERY} ]

Output: 0 (false)
### Test 6:
Input:
- givenCards = [TerritoryCard = {ALASKA, CAVALRY}, TerritoryCard = {CONGO, INFANTRY}]
- Cards the player owns = [Wild Card, TerritoryCard = {CONGO, INFANTRY}, TerritoryCard = {ALASKA, CAVALRY}]

Output: 1 (true)
### Test 7:
Input:
- givenCards = [ all cards ]
- Cards the player owns = []

Output: 0 (false)
### Test 8:
- givenCards = [ all cards ]
- Cards the player owns = [ Wild Card ]

Output: 0 (false)
### Test 9:
- given cards = [ all cards ]
- Cards the player owns = [ Wild Card, Territory Card = {BRAZIL, ARTILLERY} ]

Output: 0 (false)
### Test 10:
- givenCards = [ all cards ]
- Cards the player owns = [ all cards ]

Output: 1 (true)
### Test 11:
- given cards = []
- Cards the player owns = [ all cards ]

Output: 1 (true)
### Test 12:
- given cards = [Wild Card]
- Cards the player owns = [ all cards ]

Output: 1 (true)
### Test 13:
- given cards = [ Wild Card, Territory Card = {BRAZIL, ARTILLERY} ]
- Cards the player owns = [ all cards ]

Output: 1 (true)

# method: `removeAllGivenCards(cardsToBeRemoved: Set<Card>): void`
Note: we will be utilizing a naive pointer equals for Cards here, since the cards should ONLY come from one
source in our code; namely, the RiskCardDeck

## BVA Step 1
Input: A collection of cards to be removed from the Player's underlying collection

Output: The underlying state of the Player's card collection (modifies a field)

## BVA Step 2
Input:
- cardsToBeRemoved: Collection
- Cards the player owns: Collection

Output: Collection (but not from function output)

## BVA Step 3
Input:
- cardsToBeRemoved: (Collection)
  - An empty collection
  - Collection with \> 1 element
  - Collection of max size (44 possible cards)
  - Cannot have duplicates with a set
- Cards the player owns (Collection)
  - An empty collection (cannot remove anything, so it'll stay empty)
  - Collection with \> 1 element (remove matching cards)
  - Collection of max size (all 44 cards)
  - Cannot have duplicates with a set

Output: (Collection)
- Matches the input underlying collection, but with the relevant cards removed from cardsToBeRemoved

## BVA Step 4
### Test 1
Input:
- cardsToBeRemoved = {}
- Cards the player owns = {}

Output:
Cards the player owns = {}
### Test 2
Input:
- cardsToBeRemoved = {}
- Cards the player owns = {Wild Card, TerritoryCard = [ALASKA, INFANTRY]}

Output:
Cards the player owns = {Wild Card, TerritoryCard = [ALASKA, INFANTRY]}
### Test 3
Input:
- cardsToBeRemoved = {Wild Card, TerritoryCard = [ALASKA, INFANTRY]}
- Cards the player owns = {}

Output:
Cards the player owns = {}
### Test 4
Input:
- cardsToBeRemoved = {Wild Card, TerritoryCard = [BRAZIL, ARTILLERY]}
- Cards the player owns = {TerritoryCard = [BRAZIL, ARTILLERY]}

Output:
Cards the player owns = {}
### Test 5
- cardsToBeRemoved = {Wild Card, TerritoryCard = [BRAZIL, ARTILLERY]}
- Cards the player owns = {Wild Card, TerritoryCard = [BRAZIL, ARTILLERY], TerritoryCard = [CONGO, INFANTRY]}

Output:
Underlying card collection = {[CONGO, INFANTRY]}
### Test 6
- cardsToBeRemoved = { all cards }
- Cards the player owns = {}

Output:
Cards the player owns = {}
### Test 7
- cardsToBeRemoved = { all cards }
- Cards the player owns = { wild card }

Output:
Underlying card collection = {}
### Test 8
- cardsToBeRemoved = { all cards }
- Cards the player owns = { Wild Card, [BRAZIL, ARTILLERY] }

Output:
Cards the player owns = {}
### Test 9
- cardsToBeRemoved = { all cards }
- Cards the player owns = { all cards }

Output:
Cards the player owns = {}
### Test 10:
- cardsToBeRemoved = { all cards minus a wild card }
- Cards the player owns = { all cards }

Output:
Cards the player owns = { wild card }