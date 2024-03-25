# method: DeckManager():void

## BVA Step 1
Input: N/A

Output: 
- The initialized and shuffled list of cards (Note: contains only 42 territory cards and 2 wild cards for now)

## BVA Step 2
Input: N/A

Output: Collection

## BVA Step 3
Input: N/A

Output: A collection of 42 territory cards and 2 wild cards where cards are in different orders

## BVA Step 4
### Test value 1
Input: N/A

Output: A collection of 44 cards in a certain arrangement

### Test value 2
Input: N/A

Output: A collection of 44 cards in a different arrangement


# method: drawCard():Card

## BVA Step 1
Input: The current list of cards in the deck

Output: The drawn card, or exception if the deck is empty

## BVA Step 2
Input: Collection

Output: Pointer

## BVA Step 3
Input: empty collection, 1 element collection, 43 element collection, 44 element collection

Output: exception, card at the end of list

## BVA Step 4
### Test value 1
Input: empty list

Output: exception

### Test value 2
Input: 1 element list

Output: pointer to the card at position 0

### Test value 3
Input: 43 element list

Output: pointer to the card at position 42

### Test value 4
Input: 44 element list

Output: pointer to the card at position 43


# method: shuffle():boolean

## BVA Step 1
Input:  Current list of cards in a certain arrangement

Output: True or false value, and the collection with same element but different arrangements

## BVA Step 2
Input: Collection

Output: Boolean, Collection

## BVA Step 3
Input: empty collection, 1 element collection, 43 element collection, 44 element collection

Output: 
- Boolean: 
  - true 
  - false
- Collection:
  - empty collection, 
  - 1 element collection, 
  - 43 element collection with different arrangement, 
  - 44 element collection with different arrangement

## BVA Step 4
### Test value 1
Input: empty list

Output: false, empty list

### Test value 2
Input: 1 element list

Output: false, the same 1 element list

### Test value 3
Input: 43 element list

Output: true, a 43 element list with same elements but different arrangements

### Test value 4
Input: 44 element list

Output: true, a 44 element list with same elements but different arrangements

# method: initDeck():boolean

## BVA Step 1
Input: An empty list that needs to be filled with cards

Output:
- Success or failure of the deck initialization
- List of cards, containing 42 different territory cards and 2 wild cards

## BVA Step 2
Input: Collection

Output: Boolean, Collection

## BVA Step 3
Input: Empty collection, 1 element collection, 44 element collection, 45 element collection

Output:
- Boolean:
  - true
  - false
- Collection:
  - List of cards, containing 42 different territory cards and 2 wild cards

## BVA Step 4
### Test value 1
Input: Empty collection

Output: true, List of cards containing 42 different territory cards and 2 wild cards

### Test value 2
Input: 1 element collection

Output: false, exception

### Test value 3
Input: 44 element collection

Output: false, exception

### Test value 4
Input: 45 element collection

Output: false, exception