# Changelog

## [Unreleased]

### Updates

* Integration with the Relic Stats mod (thanks ForgottenArbiter)
  * https://steamcommunity.com/sharedfiles/filedetails/?id=2118491069

### Bug fixes

* Fix incorrect floor number increment for Cull on chest/boss fire/boss floors (by Coogrr)
* Fixed issue where starter deck legendary cards would still be in card pools in act 1 (by Coogrr)
* Fixed issue with not being able to play cards with Sacrifice if you had 0 energy (thanks LankSSBM)
* Fixed Assertion card text (thanks LankSSBM)

### CULL updates

* New art for Double (thanks Newigeg)
* New art for Exhale (thanks Zyalin)

### CULL balance/design changes

* Blasphemer: Smites added when drawn 5->4, Blasphemer+ now adds 4 upgraded Smites.
* Spirit Shield: Cost 1->0, Exhausts. Spirit Shield+ removes Exhaust.
* Siphon: Now adds 1 Max HP when it kills an enemy in addition to healing 4

### Wanderer Updates

## [v1.9.1] - 2020-11-01

### Bug fixes

* Fix crash when viewing CULL cards in Compendium

### CULL updates

* New art for Paralysis (thanks Fr33ly)

### Wanderer Updates

* New art for Siphon Energy (thanks Fr33ly)

## [v1.9.0] - 2020-10-31

Special thanks to LankSSBM and wang429 for handling most of this release's code review and project management!
 
### Bug fixes

* Fixed crash when transitioning from an event room to a monster room (thanks wbfw109 for reporting)
* Fixed issue where certain character-themed colors would occasionally be transparent (thanks kiooeht for reporting)
* Fixed issue where using the Thirst campfire option for CULL would not reduce card damage (thanks wang429)

### Updates

* Integration with Googly Eyes mod by twanvl (thanks wang429)
  * https://steamcommunity.com/sharedfiles/filedetails/?id=1615430126
* Spanish translation updates (thanks LankSSBM)
* Simplified Chinese translation updates (thanks yanang007)

### CULL updates

* Added the Book of Trials (thanks wang429)
* Added Metronome relic (thanks PanicPoint)
* Added Warped Glass relic (thanks wang429)
* Added Shadow relic (thanks Nafen)
* Coffee Dripper now prevents Thirsting at campfires
* Remove Calipers from CULL's relic pool
* Added voiceovers for elites/bosses/death/start (thanks Ray Chase)
* New art for Ritual (thanks Lila for art and Neablis for helping merge)
* New art for Thirsting campfire option (thanks ChDaVinci)
* New art for Withering (thanks ChDaVinci for art and Neablis for helping merge)
* New art for Decoy (thanks EscMargot for art and Neablis for helping merge)
* New art for Coda (thanks Christina Oran for art and Neablis for helping merge)

### New CULL cards

* Blasphemer (thanks nic3245)
* False Blessing (thanks PanicPoint)
* Black Tentacles (thanks LankSSBM for porting from Wanderer)
* Demonic Coup (thanks LankSSBM)
* Prodigal Memory (thanks LankSSBM)
* Silver Strike (thanks LankSSBM)
* Ticking Curse (thanks LankSSBM)
* Sacrifice (thanks LankSSBM)
* Shrapnel Bloom (thanks LankSSBM)
* Assertion (thanks LankSSBM)
* Investment (thanks LankSSBM)
* Mirrored Technique (thanks LankSSBM)
* Clarify (thanks LankSSBM)
* Focused Rage (thanks wang429)
* Reap and Sow (thanks LankSSBM)
* Old Book (thanks LankSSBM)
* Possession (thanks nic3245)
* Repressed Memory (thanks nic3245)

### Wanderer Updates

* New art for Frantic Search (thanks Chevy for art and Neablis for helping merge)
* New art for Refuse to Forget (thanks Chevy for art and Neablis for helping merge)
* New art for Animate Objects (thanks Chevy for art and Neablis for helping merge)
* New art for Index (thanks lobbien for art and Neablis for helping merge)
* New art for Toll the Dead (thanks Chevy for art and Neablis for helping merge)
* New art for Inferno (thanks Chevy for art and Neablis for helping merge)

## [v1.8.2] - 2020-04-18

* Fixed rare crash when upgrading cards outside of combat soon after loading game (thanks modargo)

## [v1.8.1] - 2020-04-07

* Fixed bug where non-CULL characters would receive 1 manifest for entering event rooms (thanks puffalo for reporting)

## [v1.8.0] - 2020-04-06

### New translations

* Chinese (Simplified) (thanks yanang007)
* Spanish (thanks ChDaVinci)

### Updates

* Integration with the Slay the Relics Twitch extension (thanks avolny)

### CULL mechanics

* The CULL can no long Rest at campfires; instead, it Thirsts, consuming Wrath from an attack to heal (thanks Lucerna)
* The CULL can no longer gain Block; instead, the block amount is dealt as damage to a random enemy (thanks Lucerna)
* The CULL can no longer gain Dexterity; instead, it gains the Dexterity amount as Strength (thanks Lucerna)
* Event rooms now cost 1 Manifest to enter
* Added a Top Panel icon to track Manifest and explain CULL mechanics

### CULL balance/design changes

* Starting HP changed to 70
* Tungsten Rod removed from CULL relic pool.
* CULL (the card): Changed to "Deal 12 damage twice. Exhaust."
* CULL (the card): Upgrade changed to "wrath applies twice"
* Haunt: Changed to "Ephemeral(Exhaust). Gain 1 intangible and draw 1 fewer card next turn."
* Abjure: text changed to represent that you will have exactly 1 Spirit Shield.
* Miracle: added Exhaust
* Siphon: changed damage, heal amount, upgrade heal amount and rarity. Removed from the starting loadout, since it is now a common.
* Ectoplasm: changed gold gained from 10 to 5.
* Wail: added Exhaust. Changed rarity from basic to uncommon, and removed from the starting loadout.
* Apparate: changed upgrade damage increase from 3 to 4. Changed rarity from basic to uncommon, and removed from the starting loadout.
* Toil: changed to work only on non-minion enemies, using the Fatal keyword.
* Wasting Essence: Renamed to Wasting Form. Changed damage per curse drawn and upgrade damage increase.
* Strife: only counts curses in discard pile. Upgrade adds 2 curses instead of 2 strength per curse. Added Exhaust.
* Drain Life: now exerts instead of exhausting
* Intervention: exerts instead of exhausting, gives Procrastination instead of Regret
(thanks Cartopol for most!)

### Removed CULL cards

* Seance
* Inhale
* Splinter Soul
* Waking Dream

### New CULL cards

* Strike (thanks Cartopol)
* Paralysis (thanks Cartopol)
* Accumulation (thanks Cartopol)
* Grim Dirge (thanks Cartopol)
* Find Weakness (thanks Cartopol)
* Gathering Evil (thanks Cartopol)
* Exhale (thanks Cartopol)
* Frantic Mind (thanks Cartopol)
* Wasting Strike (thanks Cartopol)
* Polluted Strike (thanks Cartopol)
* Cleanse (thanks Cartopol)
* Anxiety (thanks Cartopol)
* Feint (thanks PanicPoint)
* Goosebumps (thanks PanicPoint)
* Melt (thanks PanicPoint (code) and Lila (art))
* Godsbane (thanks PanicPoint)
* Pollute (thanks Cartopol)
* Summoning (thanks Cartopol)
* Toll (thanks PanicPoint)
* Concentrate (thanks Skyl3lazer)
* Frustration (thanks Cartopol)
* Discordance (thanks Skyl3lazer)
* Equinox (thanks Cartopol)
* Procrastination (thanks Cartopol)
* Overkill (thanks PanicPoint)
* Rebuttal (thanks Cartopol)
* Withering (thanks lobbien)

## [v1.7.0] - 2020-03-15

### Balance changes

* Arcane Form: removed Diligence (thanks wang429) 
* Mindworm: Reduced damage to 6(7)+6(9) (thanks wang429) 
* Greed: Reduced gold amount from 20 to 10 (thanks wang429) 
* Old Pocket: Reduced gold amount from 10 to 5 (thanks wang429) 
* Humility: Passive effected changed from "double thorns" to "+2 more thorns" (thanks wang429) 

### Wanderer updates

* Improved coloring on memory tooltips (thanks wang429) 
* Made Snap timing/damage more clear with power bar icon/tooltip and health bar effect (thanks wang429)
* New art for Entangle (thanks Cartopol)
* Updated art for Book of Tongues and Faerie Fire (thanks lobbien)

### New CULL cards

* Forced Presence (thanks Cartopol)
* Commune (thanks Cartopol)

### Bug fixes

* Fix Prayer Wheel being able to show multiple copies of legendary cards (thanks wang429)

## [v1.6.0] - 2020-03-01

### Updates

* New art for Parrying Blow (thanks lobbien)
* New art for Stalwart (thanks chevy28360)
* New art for Magic Mirror (thanks Cartopol)
* Scroll of Dimension Door now sounds like a scroll and is something you "Read" instead of "Drink"
* Brambles now shows how much damage it will deal in the Grimoire select screen

### Bug fixes

* Fix crash in ManifestPatch reported in workshop comment
* Fix issue where card text was not being updated immediately after permanent damage changes (particularly, after drawing CULL) (thanks wang429)
* Fix issue where banishing one of the two enemies in the Spear and Shield fight would prevent Unceasing Top from working (thanks wang429)
* Black Tentacles now *immediately* stops redirecting damage once the target dies

## [v1.5.0] - 2020-02-23

### Updates

* New art for Brooding (thanks lobbien)
* New art for Corroding Barrier (thanks Cartopol)
* New art for Wanderer's Defend (thanks Chevy28360)
* Touched up art for Rest and Chain Lightning (thanks Newigeg)

### Bug fixes

* Fix memory tooltips not displaying in touchscreen mode
* Amnesia removing Thorns while you have Humility will no longer result in negative Thorns (thanks wang429)
* "Add card to hand" effects when your hand is full will now exhaust Ephemeral cards instead of discarding them 

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
