# method: `addNewTerritory(territory: Territory): boolean`

## BVA Step 1
Input: A Territory object (with TerritoryType field), the underlying set of keys we already have

Output: A true/false whether the TerritoryType could be added as a valid key

## BVA Step 2
Input: Pointer, Collection (underlying storage on keys)

Output: Boolean (0, 1)

## BVA Step 3
Input: 
- territory: Pointer
    - Null object (will not test)
    - Valid Territory object
- Underlying list: Collection
    - Empty collection
    - Collection with 1 element
    - Collection with > 1 element
    - Maximum possible size of the collection (42)
    - Collection with duplicates
    - Collection with no duplicates

Output: 
- Boolean. Need to test:
    - 0 
    - 1 

## BVA Step 4
### Test 1:
- Input: Territory object initialized to ALASKA, Collection = []
- Output: 1
### Test 2:
- Input: Territory object initialized to ALASKA, Collection = [ARGENTINA]
- Output: 1
### Test 3:
- Input: Territory object initialized to ALASKA, Collection = [BRAZIL, CONGO]
- Output: 1 
### Test 4:
- Input: Territory object initialized to ALASKA, Collection = [ALASKA, ..., YAKUTSK]
- Output: 0 (no matter what you add, it'll be a duplicate)
### Test 5:
- Input: Territory object initialized to ARGENTINA, Collection = []
- Output: 1 
### Test 6: 
- Input: Territory object initialized to BRAZIL, Collection = []
- Output: 1
### Enumerate over the rest of TerritoryType for tests 7-46
- Input: Collection = []
- Output: 1

# method: `addNewAdjacency(firstTerritory: TerritoryType, secondTerritory: TerritoryType): boolean`

## BVA Step 1
Input: The existing undirected graph, two territories to add an edge between

Output: Yes or no answer, depending on whether the add was successful

## BVA Step 2
Input: Collection, cases (for both TerritoryType enums)

Output: 0 (false) or 1 (true)

## BVA Step 3
Input:
- Collection:
  - empty graph
  - graph with 1 vertex
  - graph with 2 vertices and 0 edges
  - graph with 2 vertices and 1 edge
  - graph with 42 vertices and 0 edges
  - complete graph with 42 vertices and 861 edges
- Cases: each possibility from the TerritoryType enum (42 in total, all other options are impossible)

Output: true when edge can be added, false otherwise (already exists or case not yet a vertex)

## BVA Step 4
### Test values 1
Input: empty graph, each possible TerritoryType combination

Output: 0 (false)
### Test value 2
Input: graph with single vertex of TerritoryType, each possible TerritoryType combination

Output: 0
### Test value 3
Input: graph with two unique TerritoryType vertices (no edges), the two territories from the graph

Output: 1 (true)
### Test value 4
Input: graph with two unique TerritoryType vertices and edge between, two territories not from the graph

Output: 0
### Test value 5
Input: graph with two unique TerritoryType vertices and edge between, the two territories from the graph

Output: 0
### Test value 6
Input: graph with all 42 TerritoryType vertices (no edges), each possible TerritoryType combination

Output: 1
### Test value 7
Input: complete graph with all 42 TerritoryType vertices, each possible TerritoryType combination

Output: 0
### Test value 8
Input: graph with single vertex of TerritoryType, the same TerritoryType

Output: 0

# method: `addSetOfAdjacencies(keyToAdd: TerritoryType, adjTerritories: Set<TerritoryType>): boolean`

## BVA Step 1
Input: 
- A TerritoryType enum (territories in Risk) 
- A collection of TerritoryType objects to associate as adjacencies with a given TerritoryType 
- The underlying collection (graph) storing current adjacencies of TerritoryTypes (so the map from TerritoryType to a set of TerritoryTypes; more precisely, to a collection of TerritoryTypes)

Output: A yes/no answer on whether we could add the adjacencies or not

## BVA Step 2
Input: 
- keyToAdd: Cases (TerritoryType is an enum)
- adjTerritories: Collection
- Underlying data structure: Collection (Map can be traversed like a collection)

Output: Boolean (0, 1)

## BVA Step 3
Input: 
- newKey: Cases
    - The first possibility
    - The second possibility
    - ...
    - The 42nd possibility
    - The 0th or 43rd case (impossible to set)
- adjTerritories, underlying data structure: Collection
    - Empty collection
    - Collection with 1 element
    - Collection with > 1 element
    - Maximum possible size of the collection (42)
    - Collection with duplicates (impossible because the underlying structures are sets)

Output: Boolean
- 0
- 1

## BVA Step 4
### Test value 1:
Input: empty graph, each possible TerritoryType, empty set

Output: 0 (false)
### Test value 2:
Input: empty graph, each possible TerritoryType, set containing the same TerritoryType

Output: 0
### Test value 3:
Input: empty graph, each possible TerritoryType, set containing all other TerritoryTypes

Output: 0
### Test value 4:
Input: graph with one TerritoryType, the corresponding TerritoryType, empty set

Output: 0
### Test value 5:
Input: graph with one TerritoryType, a different TerritoryType, empty set

Output: 0
### Test value 6:
Input: graph with one TerritoryType, the corresponding TerritoryType, set containing all other TerritoryTypes

Output: 0
### Test value 7:
Input: graph with one TerritoryType, a different TerritoryType, set containing the TerritoryType from the graph

Output: 0
### Test value 8
Input: graph with two TerritoryTypes (no edges), the first TerritoryType vertex, set containing the second TerritoryType vertex

Output: 1 (true)
### Test value 9
Input: graph with two TerritoryTypes and an edge between, the first TerritoryType vertex, set containing the second TerritoryType vertex

Output: 0
### Test value 10:
Input: graph with all 42 TerritoryTypes (no edges), one of the vertices, set containing one different vertex

Output: 1
### Test value 11:
Input: graph with all 42 TerritoryTypes (no edges), one of the vertices, set containing two different vertices

Output: 1
### Test value 12:
Input: graph with all 42 TerritoryTypes (no edges), one of the vertices, set containing remaining vertices

Output: 1
### Test value 13:
Input: graph with all 42 TerritoryTypes (no edges), one of the vertices, the same vertex

Output: 0
### Test value 14:
Input: complete graph, each possible TerritoryType, a different TerritoryType

Output: 0
### Test value 15:
Input: complete graph, each possible TerritoryType, the same TerritoryType

Output: 0
### Test value 16:
Input: complete graph, each possible TerritoryType, the remaining TerritoryTypes

Output: 0

# method: `getTerritory(territory: TerritoryType): Territory`

## BVA Step 1
Input: Map from TerritoryType enums to Territory objects, a TerritoryType enum

Output: The Territory object

## BVA Step 2
Input: Collection, cases

Output: Pointer

## BVA Step 3
Input:
- Collection: empty map, map with one entry, map with 42 entries
- Cases: each possibility from the TerritoryType enum (42 in total, all other options are impossible)

Output: associated Territory object or exception

## BVA Step 4
### Test value 1
Input: empty map, each possible TerritoryType

Output: NullPointerException "Territory does not exist"
### Test value 2
Input: map with single entry from TerritoryType to its Territory objet, the same TerritoryType from the map

Output: associated Territory object
### Test value 3
Input: map with single entry from TerritoryType to its Territory object, each possible TerritoryType not in the map

Output: NullPointerException "Territory does not exist"
### Test value 4
Input: maps with all 42 entries from TerritoryType to respective Territory objects, each possible TerritoryType

Output: associated Territory object

# method: `findAdjacentTerritories(territory: TerritoryType): Set<Territory>`

## BVA Step 1
Input: The existing undirected graph, a TerritoryType enum

Output: All the Territory objects that are adjacent to the input TerritoryType

## BVA Step 2
Input: Collection, cases

Output: Collection

## BVA Step 3
Input:
- Collection:
  - empty graph
  - graph with 1 vertex
  - graph with 2 vertices and 1 edge
  - graph with 42 vertices and 0 edges
  - complete graph with 42 vertices and 861 edges
- Cases: each possibility from the TerritoryType enum (42 in total, all other options are impossible)

Output: Empty set, set with one Territory, set with all 42 territories

## BVA Step 4
### Test value 1
Input: empty graph, each possible TerritoryType

Output: empty set
### Test value 2
Input: graph with one territory (no edges), the corresponding TerritoryType (for each possible TerritoryType)

Output: empty set
### Test value 3
Input: graph with two unique territories and an edge between them, one of the TerritoryTypes present in the graph (for each possible combination)

Output: set containing the other Territory from the graph
### Test value 4
Input: graph with all 42 territories and no edges, each possible TerritoryType

Output: empty set
### Test value 5
Input: complete graph with all 42 territories, each possible TerritoryType

Output: set containing all Territory objects except the one that corresponds with the input TerritoryType

# method: `areTerritoriesAdjacent(startingTerritory: TerritoryType, endingTerritory: TerritoryType): boolean`
## BVA Step 1
Input: The existing undirected graph and two TerritoryType enums

Output: Yes or no answer if there is an edge between the two TerritoryTypes

## BVA Step 2
Input: Collection, cases

Output: Boolean

## BVA Step 3
Input:
- Collection:
  - empty graph
  - graph with 1 vertex
  - graph with 2 vertices and 1 edge
  - graph with 42 vertices and 0 edges
  - complete graph with 42 vertices and 861 edges
- Cases: each set possibilities from the TerritoryType enum (42 in total, all other options are impossible)
  - The same TerritoryType
  - Two different TerritoryTypes with an edge between them
  - Two different TerritoryTypes without an edge between them

Output:
- 0 (false)
- 1 (true)

## BVA Step 4
### Test value 1
Input: empty graph, each possible set of TerritoryTypes

Output: 0 (false)
### Test value 2
Input: graph with one territory (no edges), the corresponding TerritoryType (for each possible TerritoryType) and a different TerritoryType (for each possible TerritoryType)

Output: 0
### Test value 3
Input: graph with one territory (no edges), the same TerritoryType (for each possible TerritoryType)

Output: 0
### Test value 4
Input: graph with two unique territories and an edge between them, both of the TerritoryTypes present in the graph

Output: 1 (true)
### Test value 5
Input: graph with all 42 territories and no edges, each possible set of TerritoryTypes

Output: 0
### Test value 6
Input: complete graph with all 42 territories, each possible set of TerritoryTypes

Output: 1
### Test value 6
Input: complete graph with all 42 territories, the same TerritoryType (for each possible TerritoryType)

Output: 0