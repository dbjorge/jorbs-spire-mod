# Changelog

## [Unreleased]

### Updates

* New art for Brooding (thanks lobbien)
* New art for Corroding Barrier (thanks Cartopol)
* New art for Wanderer's Defend (thanks Chevy28360)
* Touched up art for Rest and Chain Lightning (thanks Newigeg)

## [v1.4.0] - 2020-02-19

### Balance changes

* Arcane Weapon: -1 damage (again)
* Fresh Adventure+: +1 damage

### Updates

* Added secondary tooltips for Patience and Temperance describing Coil and Clarity, respectively

### Bug fixes

* Fixed crash in OnModifyGoldPatch with other mods which add gold before you enter a room (eg, Gold Saver)
* Fixed issues where Voiceover lines for elite fights would play in non-elite fights that shared certain elite enemies (eg, Colosseum event)
* Fixed memory tooltips not appearing when using Peek button from a card select screen (eg, Retain)
* Fixed Weight of Memory damage number not counting Wandering Mind cards that are in the process of being played
* Fixed Mind Palace not applying Sloth's draw down + energy up on turn 1

## [v1.3.0] - 2020-02-19

### Updates

* New art for Fractured Mind (thanks Bakuhaku)
* New art for Unseen Servant (thanks Cartopol)
* Added subtitle speech bubbles for voiceover lines
* Legendary cards now implicitly have a card tooltip for Legendary

### Bug fixes

* Fix crash in OnDamageToRedirectPatch with other mods (eg STSFriendlyMinions) that use DamageActions with null targets
* Fix Introspection HP loss still happening if Arcane Weapon ends combat before Introspection would go off

## [v1.2.0] - 2020-02-17

### Balance changes

* Arcane Weapon: -1 damage

### Updates

* Ray of Frost: Add ray visual effect (thanks @wang429)
* Arcane Weapon: Updated card/power descriptions to clarify that attack modifiers apply (thanks Gk, Crocketeer, MetaBoy for suggestions)
* Will: Card art updated to include stars/moons on Wanderer's robe (thanks @Cartopol)

### Bug fixes

* Banish: Will now be fully prevented by Artifact, rather than half-prevented (thanks @m0rph1ing for reporting)
* Time Eddy: If a buff (eg, Arcane Weapon) kills an enemy, that enemy's remaining powers will no longer still trigger (eg, dead Exploders won't explode)
* Fresh Adventure: Fixed Wrath upgrade on Snap being skipped if Snap ended the combat (thanks @wang429) 

## [v1.1.1] - 2020-02-16

* Added a "Unlock Ascension 20" button to the mod's in-game Config page
* New art for Disguise Self (by @Newigeg) and Will (by @Cartopol)

## [v1.1.0] - 2020-02-16

### Balance changes

* Fresh Adventure: -1 damage, "this card gains a wrath stack every time you snap"
* Chamomile: loses vulnerable, upgrades to have vulnerable (no retain)
* Quicksilver: costs 1
* Burning Flask: -1 burning
* Chattering Strike: upgrades to deal 4 damage, play this card 2 additional times
* Hibernate: -1 block
* Materialize: costs 1, +1 block
* Book of Tongues: upgrades to innate instead of upgraded components
* Flame Ward: -1 burning, -1 block
* Rest: -2 block
* Toll the Dead: -1 damage
* Death Throes+: no damage increase, no remember wrath, gain clarity of wrath
* Gather Power: -1 card draw
* Trauma: shuffle into draw pile instead of put on top
* Siphon Energy: draw 2 cards (3 on upgrade)
* Rediscovery: costs 1, upgrades to innate at 1 cost
* CULL: reduced starting HP to 48

### Updates

* Added first-time tutorial tips for Memories and Snapping
* New art for CULL's Siphon
* Updated art for Prestidigitation, Rediscovery, Lose Grip, and Withdraw
* Snap tooltip now clarifies that the Wanderer will Snap on turn 7
* Increased voiceover volume slightly

### Bug fixes

* Fixed crash with the Wandering Monsters mod
* Fix cards with end-of-turn effects (Burn, Decay, etc) not triggering when Diligence is active
* Fix interactions between Banish and Spheric Guardian, Awakened One, Gremlin Nob, Exploders
* Fixed Snap tooltip listing effects out of order
* Fix Will auto-targetting The Grimoire if it exhumes as a result of playing Will
* Fix The Grimoire's delayed exhume power showing up extra times when used with Quicksilver

## [v1.0.2] - 2020-02-14

Fixed crash while starting the game if language was set to French

## [v1.0.1] - 2020-02-14

Moved CULL behind an opt-in setting in Mod config

## [v1.0.0] - 2020-02-14

[Initial release on the Steam Workshop](https://steamcommunity.com/sharedfiles/filedetails/?id=1997053791) under new name, *Jorbs's Wanderer Trilogy*!

## Pre-steam releases
  
For pre-v1.0.0 release notes, see GitHub Releases 