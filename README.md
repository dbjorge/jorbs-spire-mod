# sts-jorbs-mod
Slay the Spire mod based on [Jorbs](https://twitch.tv/jorbs)' [Mod Character Package spreadsheet](https://docs.google.com/spreadsheets/d/1GY0eJsooEp361hWFL2lD-uPVa5-l-7g4f4FtyKs-k7Q/edit#gid=0). Not official.

Heavily inspired by [gygrazok/witchmod](https://github.com/gygrazok/witchmod).

## How to install

1. Install Slay the Spire via Steam
1. From the Steam Workshop, install "BaseMod" and "Mod the Spire"
1. Download JorbsMod.jar from the latest release (https://github.com/dbjorge/sts-jorbs-mod/releases)
1. Launch Mod the Spire by right clicking on the game in Steam and selecting "Play with Mods"
1. In the Mod the Spire launcher, select the "open mods folder
1. Move JorbsMod.jar into this mods folder
1. Relaunch Mod the Spire
1. Enjoy!

## How to build from source

1. Install the Oracle Java 8 JDK (not a higher major version)
1. Follow the directions in [/lib/README.md](./lib/README.md) to install the prerequisites
1. Set an environment variable named `STS_MOD_DIR` that points to `D:\Games\Steam\steamapps\common\SlayTheSpire\mods` (or wherever you have Slay the Spire installed)
1. Open the project in IntelliJ IDEA
1. Build -> Build Module 'sts-jorbs-mod'

Building will update `JorbsMod.jar` in your Slay the Spire mods folder. To play, choose "Play with mods" from the Steam right click menu for the game.