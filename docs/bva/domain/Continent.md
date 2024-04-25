# method: `matchesContinentTerritories(setInQuestion: Set<TerritoryType>): boolean`

## BVA Step 1
Input: A collection of TerritoryTypes, to be checked if it contains all TerritoryType(s) associated with a continent.

Output: A yes/no answer if the set contains all the necessary territories

## BVA Step 2
Input:
- setInQuestion: Collection
- Underlying continent's territories: Collection

Output: Boolean

## BVA Step 3
Input: 
- setInQuestion: Collection
  - An empty collection (returns false)
  - A collection with 1 element (returns false, all continents have at least 4 territories)
  - A collection with \> 1 element 
  - The maximum possible size (size 42, since there are 42 possible territory types; should always return true)
- Underlying continent's territories: Collection
  - An empty collection (can't set, each Continent will be given a fixed Set)
  - A collection with 1 element (can't set, each continent has \>1 relevant territories)
  - A collection with \> 1 element
  - A collection of max size (depends on continent; Asia is size 12, etc.)

Output: Boolean
- 0 (false)
- 1 (true)
- Some other true value (can't set)
- Some value other than true/false (can't set)

## BVA Step 4
### Test 1:
Input:
- setInQuestion = {}
- Underlying continent collection = { territories for AFRICA }

Output: 0 (false)
### Test 2:
- setInQuestion = { ALASKA }
- Underlying continent collection = { territories for AFRICA }

Output: 0 (false)
### Test 3:
- setInQuestion = { ALASKA, YAKUTSK }
- Underlying continent collection = { territories for ASIA }

Output: 0 (false)
### Test 4:
- setInQuestion = { territories for N. America }
- Underlying continent collection = { territories for N. America }

Output: 1 (true)
### Test 5:
- setInQuestion = { territories for N. America, territories for ASIA }
- Underlying continent collection = { territories for ASIA }

Output: 1 (true)

### Tests will be considered for each continent
