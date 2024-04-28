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

# method `toString(): String`

## BVA Step 1
Input: The underlying Continent enum

Output: The string representation of the enum with the first letter capitalized

## BVA Step 2
Input: Cases

Output: Strings

## BVA Step 3
Input:
- AFRICA
- ASIA
- EUROPE
- NORTH_AMERICA
- SOUTH_AMERICA
- OCEANIA

Output:
- "Africa"
- "Asia"
- "Europe"
- "North America"
- "South America"
- "Oceania"

## BVA Step 4
### Test 1:
Input: AFRICA

Output: "Africa"
### Test 2:
Input: ASIA

Output: "Asia"
### Test 3:
Input: EUROPE

Output: "Europe"
### Test 4:
Input: NORTH_AMERICA

Output: "North America"
### Test 5:
Input: SOUTH_AMERICA

Output: "South America"
### Test 6:
Input: OCEANIA

Output: "Oceania"

# method: `getContinentBonusIfPlayerHasTerritories(playerTerritories: Set<TerritoryType>): int`

## BVA Step 1
Input: The territories that the current player owns, the underlying bonus amount of armies for a given continent

Output: If the player owns the entire continent in question, the number of armies associated with the continent as bonus.
If the player doesn't own the entire continent, this will be 0.

## BVA Step 2
Input:
- playerTerritories: Collection
- Underlying bonus: Counts

Output: Counts
## BVA Step 3
Note that we'll be calling matchesContinentTerritories to determine if the player
should actually receive said armies

Input:
- playerTerritories (Collection):
  - A collection containing all African territories
  - A collection containing all Asian territories
  - A collection containing all European territories
  - A collection containing all North American territories
  - A collection containing all South American territories
  - A collection containing all Oceania territories
  - Collections containing more than one continent's worth of territories
  - Any other collection that doesn't meet one of the above should return 0
- Underlying bonus (Counts):
  - 2 if the continent is Oceania
  - 2 if the continent is South America
  - 3 if the continent is Africa
  - 5 if the continent is Europe
  - 5 if the continent is North America
  - 7 if the continent is Asia
  - Any other cases won't be checked as it's associated with the Enum for Continent.
Output:
  - Matches only Oceania: 2
  - Matches only South America: 2
  - Matches only Africa: 3
  - Matches only Europe: 5
  - Matches only North America: 5
  - Matches only Asia: 7
  - Matches multiple continents: Sum of the above bonuses based on what it matches
  - Matches none: 0

## BVA Step 4
### Test 1:
Input:
- playerTerritories = {}
- Underlying bonus = ANY of the above

Output: 0
### Test 2:
Input: 
- playerTerritories = {ALASKA}
- Underlying bonus = ANY of the above

Output: 0
### Test 3:
- playerTerritories = { AFRICA's territories }
- Underlying bonus = 3

Output: 3
### Test 4:
- playerTerritories = { ASIA's territories }
- Underlying bonus = 7

Output: 7
### Test 5:
- playerTerritories = { EUROPE's territories }
- Underlying bonus = 5

Output: 5
### Test 6:
- playerTerritories = { NORTH AMERICA's territories }
- Underlying bonus = 5

Output: 5
### Test 7:
- playerTerritories = { SOUTH AMERICA's territories }
- Underlying bonus = 2

Output: 2
### Test 8:
- playerTerritories = { OCEANIA's territories }
- Underlying bonus = 2

Output: 2
### Test 9:
- playerTerritories = { EUROPE's territories + ASIA's territories }
- Underlying bonus = 7 (continent is ASIA)

Output: 7
### Test 10:
- playerTerritories = { EUROPE's territories + ASIA's territories }
- Underlying bonus = 5 (continent is EUROPE)

Output: 5

### All 2 continent pairs will be tested
