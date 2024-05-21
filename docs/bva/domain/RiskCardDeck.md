# method: drawCard(): Card

## BVA Step 1
Input: Underlying list of cards in the deck

Output: The drawn card, or exception if the deck is empty

## BVA Step 2
Input: Collection

Output: Pointer

## BVA Step 3
Input: empty collection, 1 element collection, 43 element collection, 44 element collection (max number of cards in a Risk game)

Output: NoSuchElementException, valid Card object (which is removed from the underlying list)

## BVA Step 4
### Test value 1
Input: empty list

Output: NoSuchElementException (Cannot draw card from an empty deck)

### Test value 2
Input: list containing 44 Cards

Output: pointer to the card at index 43, list size is now 43

### Test value 3
Input: list containing 43 Cards

Output: pointer to the card at index 42, list size is now 42

### Test values 4-44
Input: list containing one Card less than the previous test

Output: pointer to the card at index size - 1, list size is now size - 1

### Test value 45
Input: list containing one Card

Output: pointer to the Card at index 0, list is now empty


# method: shuffle(): boolean
*Note:* This will use the package private method setRandom() during testing. This allows us to mock the Random class and ensure shuffling the list actually works. It is possible that running shuffle() will result in the exact same order as before, which is not something we will test here.

## BVA Step 1
Input: Underlying list of Cards

Output: True or false depending on whether the list was shuffled (size > 1), updated list of Cards

## BVA Step 2
Input: Collection

Output: Boolean, Collection

## BVA Step 3
Input: empty collection, 1 element collection, 2 element collection, 43 element collection, 44 element collection

Output: 
- Boolean: 
  - 1 (true)
  - 0 (false)
- Collection:
  - empty collection
  - 1 element collection
  - 2 element collection with different arrangement
  - 43 element collection with different arrangement
  - 44 element collection with different arrangement

## BVA Step 4
### Test value 1
Input: empty list

Output: 0 (false), empty list

### Test value 2
Input: list containing one Card

Output: 0, the same 1 element list

### Test value 3
Input: list containing 2 Cards

Output: 1 (true), same list with a different arrangement

### Test value 4
Input: list containing 43 Cards

Output: 1, same list with a different arrangement

### Test value 5
Input: list containing 44 Cards

Output: 1, same list with a different arrangement


# method: initDeck(): boolean

## BVA Step 1
Input: An empty list of Cards

Output:
- Success (true) or failure (false) of the deck initialization
- List of cards, containing 42 different territory cards and 2 wild cards

## BVA Step 2
Input: Collection

Output: Boolean, Collection

## BVA Step 3
Input: Empty collection, > 1 element collection

Output:
- Boolean:
  - 1 (true)
  - 0 (false)
- Collection:
  - List of cards, containing 42 different territory cards and 2 wild cards

## BVA Step 4
### Test value 1
Input: empty list

Output: 1 (true), list of cards containing 42 unique TerritoryCards and 2 WildCards

### Test value 2
Input: list containing one Card

Output: 0 (false), deck was previously initialized

### Test value 3
Input: list containing 44 Cards

Output: 0