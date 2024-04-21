# method: `ownsTerritory(territory: TerritoryType): boolean`

## BVA Step 1
Input: TerritoryType to check against the ones claimed by the player and the underlying set of territories

Output: yes or no answer if the TerritoryType exists in the set

## BVA Step 2
Input:
- territory: Cases
- underlying set: Collection

Output: Boolean

## BVA Step 3
Input:
- territory: Cases
    - ALASKA
    - ALBERTA
    - ...
    - WESTERN_AUSTRALIA
    - Any other option is not possible
- underlying set: Collection
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
