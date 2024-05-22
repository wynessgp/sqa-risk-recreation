# method: `assignSetupArmiesToPlayers(): boolean`

## BVA Step 1
Input: The underlying collection representing the players (field)

Output: A yes/no answer whether we were able to successfully modify each Player object to their
setup-determined army counts.

## BVA Step 2
Input:
- Underlying PlayerColor storage: Collection

Output:
- function output: Boolean
- Player object storage: Collection

## BVA Step 3
Input:
- PlayerColor objects (Collection):
    - A collection with [3, 6] elements -> ensure we give players the correct number of armies.

Note the lack of other error cases above - we expect whoever is calling us to have made the player collection, otherwise
the other methods haven't done their job.

Output:
- function output: 
    - 1
    - Not considering other outputs as these integration tests will assume correct calling of functions
      - You would have to go through input validation in the WorldDominationGameEngine constructor first.
- Players collection:
    - Each element in the list should have their armies increased by the SETUP amount for the number of players

## BVA Step 4
I will be modeling the input by the associated player color.

Output will be modeled by a collection of numbers, as these are integration tests. We want to see that, if we are
starting up a new game, each player gets the correct amount of armies, we shouldn't have to provide anything else.

This method in particular will differ from the original tests (even if these inputs/outputs are mirrored) via the
fact that they do not provide mocked player objects to our WorldDominationGameEngine.

### Test 1:
Given a valid list of players for the current game

When the game is started

Then each player in our player list should be given `35 - (( |player list| - 3 ) * 5)` armies to place

# method: `checkIfPlayerOwnsTerritory(relevantTerritory: TerritoryType, playerColor: PlayerColor): boolean`

## BVA Step 1
Input: A territory that's in question whether the given player owns it

Output: A yes/no answer if the given player owns the territory

## BVA Step 2
Input:
- territory: Cases
- player: Cases
- Underlying territory object: Pointer
  - Note that the TerritoryType provided should always align with the Territory object we grab
  - We communicate through the graph for this, so there shouldn't be mix-ups.

Output: Boolean

## BVA Step 3
Input:
- Territory (Cases):
  - The 1st possibility (ALASKA)
  - The 2nd possibility (ARGENTINA)
  - ...
  - The 42nd possibility (YAKUTSK)
  - The 0th or 43rd possibility (can't set)
- Player (Cases):
  - The 1st possibility (SETUP)
  - The 2nd possibility (BLUE)
  - ...
  - The 7th possibility (PURPLE)
  - The 0th or 8th possibility (can't set)
- Underlying territory object:
  - Null pointer (can't set, Martin's rules)
  - A pointer to the actual object
    - If the PlayerColor in the Territory object does not match, return false.

Output:
- 0
- 1

## BVA Step 4
Two main behaviors we want to test as an integration test:
1. At the start of the game, all the territories should be unclaimed (represented by SETUP owning them)
   - Any time a Risk game starts, you have to claim all the territories before you can move on to other phases.
2. If a player places an army in a territory, this method needs to accurately reflect a player claiming it.
   - This integration test is better suited for placeNewArmiesInTerritory.

### Test 1:
Given a valid list of players for the current game

When the game has just been started

Then every territory should be unclaimed (owned by SETUP)

# method: `placeNewArmiesInTerritory(relevantTerritory: TerritoryType, numArmiesToPlace: int): boolean`
This method holds the bulk of the integration tests. Here's part of the reason why:
- If you place an army in the "scramble" phase, you expect it to immediately move on to the next player.
- You also expect the territory to then be claimed by said player
- The territory should then have an army placed in it
- Player objects need to have the number of armies available to place decreased after they place them
- In some game phases, placing an army can warrant a game phase change.

## BVA Step 1
Input: The respective territory the current user wants to place their armies in, as well as the
number of new armies to place there.

Additionally, we need to know:
- if the current Player can place this many armies
- if the amount of armies to place is valid in our current game phase
- if the user owns the territory. (Check done via the graph)

Output: A yes/no answer whether the armies were placed successfully. The "no"
case will not occur - instead, an exception will be thrown if the armies were
not able to be placed successfully (i.e. another player owns the territory).

Additionally, we care about:
- if the territory was updated correctly
  - this means claiming in the scramble phase, or just ensuring the army count was updated.
- decrementing the number of armies left to place for our player
- updating who's turn it is, if necessary.
  - This action should occur automatically for the phases:
    - SCRAMBLE, SETUP

## BVA Step 2
Input:
- territory: Cases
- numNewArmies: Counts [1, armies earned on player's turn]
- State of who owns the territory: Cases
- Game Phase: Cases
- Current player object: Pointer

Output:
- function return value: Boolean
- Territory: Pointer
- Current player object: Pointer

## TODO: Elaborate more once attack/placement phase is being developed!

## BVA Step 3
Input:
- territory (Cases):
  - The 1st possibility (ALASKA)
  - The 2nd possibility (ARGENTINA)
  - ...
  - The 42nd possibility (YAKUTSK)
  - The 0th, 43rd possibility (can't set)
- numArmies (Counts):
  - -1 (error case)
  - 0 (error case)
  - 1
  - \> 1
  - Maximum possible value (number of armies the player earns on that turn)
  - One larger than the maximum possible value (error case)
- Player who owns the territory (Cases):
  - The 1st possibility (SETUP - should ONLY happen in SCRAMBLE)
  - ...
  - The 7th possibility (PURPLE)
  - The 0th, 8th possibilities (can't set)
- Game Phase (Cases):
  - The 1st possibility (SCRAMBLE -> restrict army placement to 1)
  - The 2nd possibility (SETUP -> restrict army placement to 1)
  - The 3rd possibility (PLACEMENT)
  - ...
  - The 6th possibility (GAME_OVER)
  - The 0th, 7th possibility (can't set)
- Player object (Pointer):
  - Null pointer (can't set, per Martin's suggestions)
  - A valid pointer
    - We're mostly concerned about checking the number of armies they currently have
    - If they try to place too many and drop below 0, we should throw an error.

Output:
- function return value: Boolean
  - 0 (can't set)
  - 1 (when the amount to place is valid, and the operation succeeds)
  - Some value other than 0 or 1 (exceptions)
    - If the game phase is SCRAMBLE or SETUP, our players can't place more than 1 army at a time.
  - Some true value other than 0 or 1 (can't set)
- Territory object: Pointer
  - Null pointer (can't set, per Martin's suggestions)
  - A pointer to the true object
    - Mostly concerned about checking two things in our Territory object:
      - The PlayerColor should be updated if we're in the SCRAMBLE phase
      - The army count should be updated in appropriate phases:
        - SCRAMBLE
        - SETUP
        - PLACEMENT
- Player object: Pointer
  - The number of armies they have to place should be updated
  - Territories held should be updated if in SCRAMBLE (maybe attack too).

## BVA Step 4
Here are some things to consider for integration tests (since we will be using very few mocked objects):
- Placing armies on already claimed territories should throw errors in some phases
  - This is exclusive to the SCRAMBLE phase; if it's not your territory in SETUP we should also error. 
- Any time I successfully place an army in SCRAMBLE/SETUP, it should become the next player's turn.
  - Ensure that we can make a "full cycle", so everyone goes once, and it wraps back around.
- When a player claims a territory in the SCRAMBLE phase, it should be recorded.
  - The territory should have its respective controller updated to that player
  - The TerritoryType should get added to a player's owned territories.
- After all the territories are claimed in the initial scramble, I should be able to place armies in my owned territories.
  - Ensure that we make a transition into the SETUP phase once all territories are claimed.
- After all players' armies have been placed from SETUP, single turns of PLACEMENT/ATTACK/FORTIFY should start.
  - Ensure that the game phase doesn't change too soon or too late.
- When the SETUP phase ends, as the first player going, I should expect to have the new armies I will need to place already be calculated, and can take my turn right away.
  - Ensure that when SETUP does end, the calculation for new armies in PLACEMENT is already done.
  - Note that we can't do this during their first placement in the PLACEMENT phase, otherwise we'll do it every time they try to place something.
- When a player goes to place troops in the PLACEMENT phase, and they are holding on to too many cards, then the game should not let them proceed
  - Ensure that some kind of error is thrown and the player is informed they have too many cards.
- After I have placed all of my armies in the PLACEMENT phase, I should be able to start attacking other players.
  - Ensure that we don't transition phases too early.
  - Also ensure that we don't change who the currently going player is.
- After placing an army in the SCRAMBLE/SETUP phases, players should lose the ability to place that army again.
  - Namely, decrement the number of armies they have left to place.

To properly model these behaviors, we'll be using a test "outline" (not unlike that of using Cucumber).

### SCRAMBLE Phase Integration Tests

### Test 1:
Given that the players in the current game are [RED, PURPLE, YELLOW]

And the game is in the SCRAMBLE phase

And that the RED player has already claimed ALASKA by placing an army there

The Game should make it the PURPLE player's turn to respect ordering (assertion)

And when the PURPLE player tries to claim ALASKA by placing an army there

We should throw an error, as the territory is already claimed.
- In particular, we want to see an IllegalStateException error with the message: (assertion)
  - "Cannot place armies in a claimed territory until the scramble phase is over" (assertion)

### Test 2:
Given a valid list of players for the current game

And the game is in the SCRAMBLE phase

After each player claims a valid territory

The game should now say that it is the next player's turn (assertion)
- We want to make sure that this can wrap around; so make each player claim a territory once.
- We should go from the end of the collection of player colors back to index 0 (assertion)

### Test 3:
Given a valid list of players for the current game

And the game is in the SCRAMBLE phase

When each player claims a valid territory 

Then the game should add that territory to the player's collection of controlled territories (assertion)

And the game should update the territory's controlling PlayerColor (assertion)

### Test 4:
Given a valid list of players for the current game

And the game is currently in the SCRAMBLE phase

When each player claims a territory until no unclaimed territories remain
- Make sure we're still in SCRAMBLE until the last territory is claimed to ensure we transition at the right time (assertion)

Then the game should advance into the SETUP phase (assertion)

### Test 5:
Given a valid list of players for the current game

And the game is currently in the SCRAMBLE phase

When the current player claims a valid territory

Then the current player should have one less army left to place

### SETUP Phase Integration Tests

### Test 6:
Given that the players in the current game are [RED, PURPLE, YELLOW]

And that the RED player owns ALASKA

And that the game is in the SETUP phase

When the PURPLE player tries to place an army on ALASKA

Then an IllegalArgumentException should be thrown
- Should have message: "Cannot place armies on a territory you do not own"

### Test 7:
Given a valid list of players for the current game

And the game is in the SETUP phase

After each player places an army on an already owned territory

The game should now say that it is the next player's turn (assertion)
- We want to make sure that this can wrap around; so make each player claim a territory once.
- We should go from the end of the collection of player colors back to index 0 (assertion)

### Test 8:
Given a valid list of players for the current game

And the game is currently in the SETUP phase (note - we'll manually transition it into SETUP for a fair test)

When each player expends their remaining placeable armies (assertion - make sure we don't transition early!)

Then the game should transition into the Placement phase (assertion)

### Test 9:
Given a valid list of players for the current game

And the game is currently in the SETUP phase

When the current player places another army on a valid territory (i.e. their owned territory)

Then the current player should have one less army left to place

### PLACEMENT Phase Integration Tests

### Test 10:
Given a valid list of players for the current game

And that the SETUP phase is about to end

When the last player goes to place their final army as part of the SETUP phase

Then the game should transition into the placement phase

And the FIRST player to go should have their appropriate amount of armies calculated

And they should be able to place these newly earned armies.

### Test 11:
Given a valid list of players for the current game

And the current player has 1 army left to place

And the game is in the PLACEMENT phase

When the player places their last army in the PLACEMENT phase

Then the game should advance into the attack phase

# method: `tradeInCards(selectedCardsToTradeIn: Set<Card>): Set<TerritoryType>`

## BVA Step 1
Input: A collection of Risk cards that the current player would like to turn in and the underlying state of what GamePhase we are currently in.

For input, we also care about some attributes about our player. Specifically:
- If the player owns the cards they are attempting to trade in
- If the player owns any territories MATCHING the territories on the respective cards they trade in
- The amount of cards the player has (particular as it applies to ATTACK phase trade-ins)

Output: A collection of territories that the player owns and can place a bonus +2 armies on if the cards match them,
or an error if the set of cards is not valid to trade in, or the player doesn't own the given cards.

Additionally, we care about:
- The GamePhase being forced back to the placement phase
  - We want to emphasize that these armies MUST be placed before the player can continue
- The player receiving the bonus armies equivalent to the set's trade in value
  - So if I trade in the first set, I should get 4 more armies.
- The respective cards being removed from the player's owned cards
  - I shouldn't be able to trade in the same cards over and over
  - Cards are removed from the game once turned in

## BVA Step 2
Input:
- selectedCardsToTradeIn: Collection
- currentPlayer: Cases
- Player object: Pointer (we care about what territories and cards they own, and how many cards they own)
- Underlying GamePhase: Cases
  - Should either be PLACEMENT/ATTACK.

Output:
- Method output: Collection
- Underlying player object: Pointer
- GamePhase: Cases
  - Only care about setting it back to PLACEMENT

## BVA Step 3
Input:
- selectedCardsToTradeIn (Collection):
  - Any set to be deemed invalid by the TradeInParser (error case)
  - A set of cards that is valid, but is not owned by the current player (error case)
  - A valid set of trade in cards (see TradeInParser's rules about this)
- currentPlayer (Cases):
  - Should always line up directly with the GameEngine's tracking
  - If it doesn't, this is an error.
  - Valid colors are anything BESIDES `SETUP`
- Player object (Pointer):
  - Care about what cards they own at the time this method is called
    - If they don't own the cards, throw an error.
  - Also care about what territories they own (so they can get a +2 bonus armies in a territory if it matches a card)
- Underlying GamePhase (Cases):
  - PLACEMENT
  - ATTACK (only allowed if they have \> 5 cards, forced trade in only)
  - Any other phase (error case)

Output:
- Method output (Collection):
  - An empty collection (given no territories are matched)
  - A collection containing 1 element
  - A collection containing 2 elements
  - A collection containing 3 elements
  - Any size outside [0, 3] (can't be set, per calling TradeInParser's methods)
- Underlying player object (Pointer):
  - numArmiesToPlace should be updated according to the current set's trade in bonus
  - Remove the relevant cards from their underlying collection
- GamePhase:
  - Set it back to PLACEMENT
  - Should never set it to anything else

## BVA Step 4
Some things to consider for integration tests:
- Preventing players from trading in an invalid set
  - Error handling comes from the trade-in parser; but also based on card ownership
- When a player goes to trade in cards, they should not be able to trade in the same set again
  - Namely, we do want to ensure we actually take away their cards.
- When a player trades in cards, they should be stopped from doing whatever they were doing previously and be forced to put down armies
  - So if they were in the attack phase, move them back into the PLACEMENT phase.
- If I am told that I have too many cards to place my armies, calling this should fix it for me and give me my bonus armies.
  - Check that we give players their associated armies and that they can call placement again after trading in
- If I am trying to trade in cards during the ATTACK phase, then I must hold \> 5 cards for this to be valid
  - A forced trade in only happens if you have \> 5 cards, otherwise you're not allowed to trade in

### Test 1
Given a valid list of players for the current game

And the current player does not have enough cards to trade in

When that player tries to trade in their cards

Then the player should be told they cannot trade in the given cards

### Test 2
Given a valid list of players for the current game

And the current player does not own the cards they are trying to trade in

When the player tries to trade in their cards

Then the player should be told that they do not own the given cards

### Test 3
Given a valid list of players for the current game

And the current player is trying to trade in a malformed set of cards
- Too many / too few cards

When the player tries to trade in their cards

Then the player should be told that the set to trade in is invalid

### Test 4
Given a valid list of players for the current game

And the current player is trying to trade in a valid set of cards

And the current game phase is ATTACK

And the player holds \< 6 cards

When the player tries to trade in their cards

Then the player should be told that they can only trade in cards during the attack phase if they hold \> 5 cards

### Test 5
Given a valid list of players for the current game

And the player has a valid set of cards to trade in

And the current game phase is the PLACEMENT phase

When the player tries to trade in their cards

Then the player should get bonus armies

And the player's cards they traded in should be removed

### Test 6
Given a valid list of players for the current game

And the game is in the attack phase

And the current player has a valid set of cards to trade in

And the current player has \> 5 cards currently held

When the player tries to trade in their cards

Then the player should get bonus armies

And the game phase should be moved to PLACEMENT

### Test 7
Given a valid list of players for the current game

And the current player is holding too many cards to be able to place armies

When the player tries to place armies

Then the game should tell the player they have too many cards

Now the player chooses to trade in cards

When the player tries to trade in cards

Then the player should get bonus armies

And when the player attempts to place their armies

Then the new armies should be placed in the respective territory

# method: `attackTerritory(srcTerritory: TerritoryType, destTerritory: TerritoryType, numAttackers: int, numDefenders: int): int`

Note: this method was purposefully excluded from the non-integration test file because it calls the different
"parts" that are unit tests in the non-integration test file. The BVA for this will include the inputs / outputs to ALL
the different parts, though the tests will NOT elaborate on the outputs using mocked objects. We will use normal assertions.

## BVA Step 1
Input: The respective territory that the attacker is moving troops FROM, the territory they are moving their armies INTO
to attack, and the number of armies they are using for this attack.

There are a lot more things to consider besides the parameters, namely:
- The current phase the game is in (should only ever be attack if we're here!)
- The number of armies present in BOTH the source AND destination territory
  - If you're left with 0 armies in the territory you're attacking from as a result, you shouldn't be allowed to do this.
  - Need to know if you can use that many attackers legally
  - Need to know how many defenders are in the battle via the destination territory. If you can't use that many, error!
- Who owns the respective territories
  - You can't attack yourself!
  - If you don't own the territory you've selected, you also can't attack from there...
- If the territories border each other
  - You can't attack a territory that's halfway across the map; they should be adjacent.
- If the current player is holding on to too many cards
  - This should only happen as a result of taking out a player; this is still a forced trade in.

Output: The MAXIMUM amount of armies that a player may choose to move between these two territories as a result of WINNING the attacking
battle. If you do not take over the opposing territory as a result of this battle, this number will be **0**.

In the event that you DO take over the territory as a result of the attack, here's what will happen:
- The armies utilized in the attack will automatically be moved into the destination territory.
  - So if you attack with 3 armies (assuming 3 is valid), and lose none in the attack, then all **3** will be automatically moved to that new territory.
- If more than **1** army remains in the source territory, you may:
  - Move anywhere from [0, num armies in source territory - 1] armies into the newly conquered (destination) territory
  - This `num armies in source territory - 1` figure is calculated AFTER moving the attacker armies involved in the dice roll.

With all of that being said, there's still more things that we care about as a result of this function:
- With each attack, the amount of armies present in the defender AND the attacker's territory should decrease, if necessary
  - i.e. if I lose 1 army while attacking, and the defender loses 1, both territories need to be updated.
- If my attack results in me taking over the territory, we want who controls the territory to be updated
  - Set the number of armies in the territory equal to the number of armies I attacked with
  - Say that I own the territory now
  - Add it to my owned territories
- If I take over a territory with an attack, I am eligible to claim a card from the deck after my attack phase ends
  - We should only be able to claim 1 territory card PER attack phase (Boolean)
  - Say this source, destination were recently attacked to be eligible to split troops between them.
- If I eliminate another player as a result of winning an attack, I should get the cards they were holding on to
  - Additionally, the game needs to remove the player from the turn order
  - If this puts the player over 5 cards, they will be forced to turn them in before going again.
- If this is the last territory I needed to take over in order to win the game, then the game should end
- Since you can traditionally see the dice rolls in a regular risk game, we need to hold on to these things:
  - The results of rolling the attack dice
  - The results of rolling the defense dice
  - The results of each "die" comparison; i.e. if a defender won one roll, attacker the other, etc.

## BVA Step 2
Input:
- sourceTerritory: Cases
- destTerritory: Cases
- numAttackers: Counts
- numDefenders: Counts
- current game phase: Cases
- currently going player: Cases
- player object: Pointer
  - Care about the number of cards they hold
- source, destination territory objects: Pointer
  - We care primarily about the number of armies and who owns it here

Output:
- method output: Interval [0, valid amount of transferable armies]
  - It's hard to put a strict upper bound on this because there is a lot of variation here.
- source, destination territory objects: Pointer
  - Again, we primarily care about ownership / army counts.
- player object: Pointer
  - Care about the territories they own as a result, and the cards they own
- current game phase: Cases
  - Only changes if I win the game, or run out of armies to attack with.
  - Otherwise, this will be manually changed by the player if they wish to end the phase early.
- Ability to claim a card this turn: Boolean
  - true if and only if you successfully take a territory during the attack phase
- Attacker dice results, defender dice results, battle results: Collection

## BVA Step 3
Input:
- sourceTerritory, destTerritory (Cases):
  - The 1st possibility (ALASKA)
  - The 2nd possibility ...
  - ...
  - The 0th, 43rd possibilities (can't set)
  - Both are the same territory (error case)
  - Territories are not adjacent (error case)
- numAttackers (Counts):
  - Any value \< 1 (error case)
  - Any value in [1, 3] (provided this does not EXCEED the number of armies in the origin territory)
  - \> Num armies in origin territory - 1 (error case, must leave at least 1 army in a territory)
- numDefenders (Counts):
  - Any value \< 1 (error case)
  - Any value in [1, 2] (provided this does not EXCEED the number of armies in the destination territory)
  - Note that defenders CAN go to down to 0 armies in their territory while "defending" (i.e. in the roll)
- current game phase (Cases):
  - SCRAMBLE (error case)
  - SETUP (error case)
  - PLACEMENT (error case)
  - ATTACK (method should only EVER be called in ATTACK)
  - FORTIFY (error case)
  - GAME_OVER (error case)
- currently going player (Cases):
  - Should always line up with the game engine's tracking
  - All colors besides `SETUP` are valid, just depends on what the list looks like.
- player object (Pointer):
  - Null pointer (won't consider, per Martin's rules)
  - A pointer to the true object
    - Want to look at how many cards they hold; if it exceeds 5 we throw an error and force them to trade cards in.
- source, destination territory objects (Pointer):
  - Null pointer (won't consider, per Martin's rules)
  - A pointer to the true object
    - If `numAttackers > numArmiesInTerritory - 1`, we should error (player is attempting to use too many armies)
    - Source territory is not owned by current player (error case)
    - Destination territory is owned by current player (error case)

Output:
- method output (Interval):
  - -1 (can't set)
  - 0 (this will be a frequent result; other numbers only happen when a territory is taken)
  - valid amount of transferable armies
    - Should be the `numArmiesInTerritory - num armies used in attack - 1`
  - valid amount of transferable armies + 1 (can't set)
- source, destination territory objects (Pointer):
  - Null pointer (won't consider, per Martin's rules)
  - A pointer to the true object
    - The source territory object should have its armies decremented by the amount LOST in the attack
    - The destination territory object should have its armies decremented by the amount LOST in the defense
    - If the number of armies that would be left in the destination territory would be \< 0, update the destination territory to:
      - Indicate the current player is in control
      - Place `numAttacker` armies in there; and take them from the source territory
- player object (Pointer):
  - Null pointer (won't consider, per Martin's rules)
  - A pointer to the true object
    - If the player does successfully take over the territory, update this in their owned territories (and remove from the opposing player's territories)
    - If this attack wiped out the other player, add the other player's cards to our current player's card collection
- current game phase (Cases):
  - Progress from ATTACK -> FORTIFY if:
    - The currently going player only has 1 army in every territory they own
  - Progress from ATTACK -> GAME_OVER if:
    - The current player now owns every territory on the board as a result of an attack
- Ability to claim a card this turn (Boolean):
  - 0 (false) if the user did not take over a territory (and did not PREVIOUSLY this turn)
  - 1 if the user did take over a territory
- Attacker, defender dice results & battle results (Collection):
  - All will have a minimum size of 1
  - Attacker rolls can have a maximum size of 3 (should match numAttackers)
  - Defender, battle results have a maximum size of 2 (should match numDefenders)
  - These lists are sorted in non-increasing order; to make it easier to visualize the results for players.

## BVA Step 4
Here are the things we want to consider:

Error items:
Preventing players from providing territories that are not adjacent
- Check that we still throw the typical error
Preventing players from attacking territories they shouldn't be able to
- Launching an attack from a territory they don't own should error
- Launching an attack from a territory they DO own to a territory they ALSO own should error
Prevent players from trying to use too many attackers
- Providing a number not in [1, 3] as a parameter should error
- Providing a number of attackers >= num armies in source territory should error
Prevent players from trying to use too many defenders
- Providing a number not in [1, 2] as a parameter should error
- Providing a number of defenders > num armies in destination territory should error
Preventing players from attacking in an unsuitable phase
- If the game isn't in the attack phase, then it's an error

Non-error items:
If a player attacks a territory, but they **DO NOT** take OVER the territory
- The board state should reflect the troops lost in the attack
- The board state should reflect that no territories changed hands
- The player should NOT be able to split armies between any recently won attacks
  - Set recently attacked dest / source to null...
If a player attacks a territory, and they **DO** take over the territory
- The board state should reflect that the number of attackers have been moved into the destination
- The board state should reflect that the destination territory has changed hands
- The player SHOULD be able to split armies between these two territories if they wish
  - Recently attacked dest / source should be equal to the territories used in the attack
- The player should be eligible to claim a card at the end of the turn
- If this is the last territory they need to win the game, the game should be over!
- If this is the last territory another player was holding on to, they should lose the game.
  - The attacking player takes their cards.

### Test 1:
Given that the current player is PURPLE

When PURPLE tries to attack FROM Ukraine TO ALASKA with a valid number of attackers and defenders

Then an error should be thrown

And the user should be informed that the territories are not adjacent

### Test 2:
Given that the current player is PURPLE

And that the territories they are attacking are adjacent (ALASKA, KAMCHATKA)

And that PURPLE does not own the source territory

When PURPLE tries to attack with a valid number of attackers and defenders

Then an error should be thrown

And the user should be informed that they cannot attack from a territory they do not own

### Test 3:
Given that the current player is GREEN

And that the territories they are attacking are adjacent (ALASKA, KAMCHATKA)

And that GREEN owns the SOURCE *and* DESTINATION territories

When GREEN tries to attack with a valid number of attackers and defenders

Then an error should be thrown

And the user should be informed that they cannot attack a territory they own

### Test 4:
Given that the current player is YELLOW

And that the territories they are attacking are adjacent (CONGO, EGYPT)

And that YELLOW owns the SOURCE territory

And that YELLOW does **NOT** own the DESTINATION territory

And that the game is NOT in the ATTACK phase

When YELLOW tries to attack with a valid number of attackers and defenders

Then an error should be thrown

And the user should be informed that they cannot attack a territory outside the attack phase

### Test 5:
Given that the current player is BLUE

And that the territories they are attacking are adjacent (CHINA, SIAM)

And that BLUE owns the SOURCE territory

And that BLUE does **NOT** own the DESTINATION territory

And that the game is IN the ATTACK phase

When BLUE tries to attack with an invalid number of attackers and a valid number of defenders
- numAttackers parameter is outside [1, 3]

Then an error should be thrown 

And the user should be informed that the valid number of attackers is a number in [1, 3]

### Test 6:
Given that the current player is BLUE

And that the territories they are attacking are adjacent (CHINA, SIAM)

And that BLUE owns the SOURCE territory

And that BLUE does **NOT** own the DESTINATION territory

And that the game is IN the ATTACK phase

When BLUE tries to attack with an invalid number of attackers and a valid number of defenders
- not enough attackers in source territory to support attack

Then an error should be thrown

And the user should be informed that there are too few armies in the source territory to support the attack

### Test 7:
Given that the current player is RED

And that the territories they are attacking are adjacent (CHINA, SIAM)

And that RED owns the SOURCE territory

And that RED does **NOT** own the DESTINATION territory

And that the game is IN the ATTACK phase

When RED tries to attack with a valid number of attackers and an invalid number of defenders
- numDefenders parameter is outside [1, 2]

Then an error should be thrown

And the user should be informed that the valid number of defenders is a number in [1, 2]

### Test 8:
Given that the current player is RED

And that the territories they are attacking are adjacent (CHINA, SIAM)

And that RED owns the SOURCE territory

And that RED does **NOT** own the DESTINATION territory

And that the game is IN the ATTACK phase

When RED tries to attack with a valid number of attackers and an invalid number of defenders
- not enough defenders in destination territory to support defense

Then an error should be thrown

And the user should be informed that there are too few defenders in the destination territory to support the defense

### Test 9:
Given that the current player is PURPLE

And that the territories they are attacking are adjacent (PERU, ARGENTINA)

And that PURPLE owns the SOURCE territory

And that PURPLE does **NOT** own the DESTINATION territory

And that the game is IN the ATTACK phase

When PURPLE tries to attack with a valid number of attackers and defenders

And the attack ends with PURPLE not taking over the territory

Then the armies in the SOURCE and DESTINATION territory should be updated

And the destination territory should **NOT** be controlled by Purple

And PURPLE should not be allowed to claim a card yet

### Test 10:
Given that the current player is PURPLE

And that the territories they are attacking are adjacent (PERU, ARGENTINA)

And that PURPLE owns the SOURCE territory

And that PURPLE does **NOT** own the DESTINATION territory

And that the game is IN the ATTACK phase

When PURPLE tries to attack with a valid number of attackers and defenders

And the attack ends with PURPLE taking over the territory

Then the armies in the SOURCE and DESTINATION territory should be updated

And the destination territory **SHOULD** be controlled by Purple

And PURPLE **SHOULD** be allowed to claim a card at the end of their turn

And PURPLE **SHOULD** be allowed to split the attacking force if they choose

### Test 11:
Given that the current player is PURPLE

And that the territories they are attacking are adjacent

And that PURPLE owns the SOURCE territory

And that PURPLE does **NOT** own the DESTINATION territory

And that the game is IN the ATTACK phase

And that this is the last territory PURPLE needs to WIN the game

When PURPLE tries to attack with a valid number of attackers and defenders

And the attack ends with PURPLE taking over the territory

Then the GAME should END (game phase should be GAME_OVER)

### Test 12:
Given that the current player is PURPLE

And that the territories they are attacking are adjacent

And that PURPLE owns the SOURCE territory

And that PURPLE does **NOT** own the DESTINATION territory

And that the game is IN the ATTACK phase

And that this is the last territory YELLOW owns

When PURPLE tries to attack with a valid number of attackers and defenders

And the attack ends with PURPLE taking over the territory

Then YELLOW should LOSE the game

And PURPLE should get any Risk cards that YELLOW owned

# method: `moveArmiesBetweenFriendlyTerritories(srcTerritory: TerritoryType, destTerritory: TerritoryType, numArmies: int): void`

## BVA Step 1
Input: The two territories that the current player wants to move armies between, and the number of armies to move
between the two territories. This will interact with the underlying territory objects, so those must be considered as well.

We also need to consider the game phase: if we're in the ATTACK phase, this can only be done after a territory is taken
over, and it HAS to utilize the territories that were just used in the attack.
If we're in the FORTIFY phase, the phase needs to swing back around to PLACEMENT.

Output: An exception if the movement is not able to be done (too many armies, territories are not adjacent, in the
incorrect game phase, window to split armies in the attack phase has ended, either territory is not owned by current player).

If the movement is able to be done, then the source and destination territories will be modified.
If this movement is done in the attack phase, then the player should lose the ability to split armies between the most
RECENTLY attacked territories.
(Namely, we can just remove those two territories from being recently attacked)
If this movement is done in the fortify phase, then the fortify phase should end, and we should be on the next player's
PLACEMENT phase.

## BVA Step 2
Input:
- srcTerritory, destTerritory: Cases
- numArmies: Interval [1, num armies present in territory - 1]
- source, destination territory objects: Pointer
- current game phase: Cases
- recently attacked source, destination: Cases
- currently going player: Cases

Output:
- method output: N/A
- source, destination territory objects: Pointer
- current game phase: Cases
- recently attacked source, destination: Cases
- currently going player: Cases

## BVA Step 3
Input:
- srcTerritory, destTerritory (Cases):
  - ALASKA
  - ARGENTINA
  - ...
  - YAKUTSK
  - The 0th, 43rd possibilities (can't set, Java enum)
- numArmies (Interval):
  - \<= 0 (error case)
  - Anything in [1, num armies present in territory - 1]
    - 1
    - num armies present in territory - 1
  - \>= num armies present in territory (error case)
- source, destination territory objects (Pointer):
  - A null pointer (can't set, Martin's rules)
  - A pointer to the true object
    - If either territory is not owned by the current player (error case)
    - Source territory doesn't have enough armies to support the move (error case)
    - Territories are not adjacent on the Risk map (error case)
- current game phase (Cases):
  - SCRAMBLE (error case)
  - SETUP (error case)
  - PLACEMENT (error case)
  - ATTACK
  - FORTIFY
  - GAME_OVER (error case)
  - The 0th, 7th possibilities (error case)
- recently attacked source, dest (Cases):
  - ALASKA
  - ARGENTINA
  - ...
  - YAKUTSK
  - The 0th, 43rd possibilities (can't set, Java enum)
  - These NEED to match up with the inputs provided as source & destination territory
    - If we are in the attack phase and they do not, this is an error.
- currently going player (Cases):
  - Should always line up with the GameEngine's tracking.

Output:
- IllegalArgumentException if:
  - Territories were not adjacent (message: "Source and destination territory must be two adjacent territories!")
  - Territories are not owned by the current player (message: "Provided territories are not owned by the current player!")
  - `numArmies` >= num armies in territory (message: "Source territory does not have enough armies to support this movement!")
  - Called in attack, but armies are not able to be split between the two territories (message: "Cannot split armies between this source and destination!")
- IllegalStateException if:
  - Done in an incorrect game phase (message: "Friendly army movement can only be done in the ATTACK or FORTIFY phase!")
- source, destination territory objects (Pointer):
  - A null pointer (can't set, Martin's rules)
  - A pointer to the true object
    - Looking to see that `numArmies` was removed from source territory
    - And that `numArmies` was added to the destination territory
- current game phase (Cases):
  - If we started in ATTACK, keep it in ATTACK, but remove the ability to split armies
    - Assuming they had the ability to split armies at first
    - Namely, clear the recently attacked source and dest
  - If we started in FORTIFY, move to the PLACEMENT phase of the next player.
- recently attacked source, destination: Cases
  - These values NEED to be CLEARED if we are in the attack phase.
- currently going player (Cases):
  - If we are in the ATTACK phase, should remain the same player.
  - If we are in the FORTIFY phase, should ADVANCE to the next player in turn order.

## BVA Step 4
Here are some things to consider here:

ATTACK Phase: <br>
This action should only be precisely available immediately AFTER someone takes over a territory. If they take any other
actions, they should lose the ability to split armies via this method. The possible actions a player can take are:
- Trading in cards
- Selecting a new territory / destination pair to attack
- Actually splitting armies between said territories
- Forcibly end the attack phase

Note that we should only clear the ability to split if these actions are SUCCESSFUL, meaning they successfully trade in
cards (and make it back to ATTACK phase) or split between territories / etc.

FORTIFY Phase: <br>
This is *effectively* the end of the FORTIFY phase if they do choose to move armies once. This means swapping to the next
player in turn order and having it be their PLACEMENT phase, so we need to respect the rules of swapping to PLACEMENT.
This is also when we'll let people claim their card, since their turn will end. Note that a card should be claimed any
time we swap from FORTIFY -> PLACEMENT, so we should respect that elsewhere as well (namely forcefully ending phase).

### Test 1:
Given that the current player is PURPLE

And the current phase is FORTIFY

And they do not own the source territory

And they own the destination territory

And they try to move a valid number of armies

When they try to move armies between the source and destination territory

Then they should be unable to move the armies

And the player should be informed that they do not own both the source and destination territory

### Test 2:
Given that the current player is PURPLE

And the current phase is FORTIFY

And they own the source territory

And they do not own the destination territory

And they try to move a valid number of armies

When they try to move armies between the source and destination territory

Then they should be unable to move the armies

And the player should be informed that they do not own both the source and destination territory

### Test 3:
Given that the current player is PURPLE

And the current phase is FORTIFY

And they own the source territory

And they own the destination territory

And they try to move an invalid number of armies

When they try to move armies between the source and destination territory

Then they should be unable to move the armies

And the player should be informed that they are trying to move an invalid number of armies

### Test 4:
Given that the current player is PURPLE

And the current phase is PLACEMENT

And they own the source territory

And they own the destination territory

And they try to move a valid number of armies

When they try to move armies between the source and destination territory

Then they should be unable to move the armies

And the player should be informed that they are trying to move armies in an invalid phase

### Test 5:
Given that the current player is PURPLE

And the current phase is ATTACK

And they own the source territory

And they own the destination territory

And that the player just traded in cards
- Note that they'll have to make their way back out of PLACEMENT phase before getting back here

And they try to move a valid number of armies

When they try to move armies between the source and destination territory

Then they should be unable to move the armies

And the player should be informed that they cannot split armies between this source and destination
- Namely, because they've taken other actions before choosing to split.

### Test 6:
Given that the current player is PURPLE

And the current phase is ATTACK

And they own the source territory 

And they own the destination territory

And that the player just recently started an attack on a new destination territory

And they try to move a valid number of armies

When they try to move armies between the source and destination territory

Then they should be unable to move the armies

And the player should be informed that they cannot split armies between this source and destination
- Namely, because they've taken other actions before choosing to split.

### Test 7:
Given that the current player is GREEN

And the current phase is ATTACK

And they own the source territory 

And they own the destination territory 

And that the player just recently split armies between these two territories

And they try to move a valid number of armies

When they try to move armies between the source and destination territory

Then they should be unable to move the armies

And the player should be informed that they cannot split armies between this source and destination
- Namely, because they've taken other actions before choosing to split.

### Test 8:
Given that the current player is GREEN

And the current phase is ATTACK

And they own the source territory

And they own the destination territory

And that the player just recently took over the destination territory

And they try to move a valid number of armies

When they try to move armies between the source and destination territory

Then they should be able to move these armies

And the map should reflect that the armies have been moved

And the player should lose the ability to split armies between these territories again

### Test 9:
Given that the current player is YELLOW

And the current phase is FORTIFY

And they own the source territory

And they own the destination territory

And they try to move a valid number of armies

When they try to move armies between the source and destination territory

Then they should be able to move these armies

And the map should reflect that the armies have been removed

And it should be the next player's turn
- Claim a card for YELLOW if they earned it

And the next player should receive their armies for the PLACEMENT phase

And the game phase should be PLACEMENT

