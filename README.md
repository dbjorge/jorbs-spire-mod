# Jorbs Mod
[![GitHub release version badge](https://img.shields.io/github/v/release/dbjorge/jorbs-spire-mod?color=blue&label=latest%20release&sort=semver)](https://github.com/dbjorge/sts-jorbs-mod/releases)
[![GitHub Release download counter badge](https://img.shields.io/github/downloads/dbjorge/jorbs-spire-mod/total?color=blue)](https://github.com/dbjorge/sts-jorbs-mod/releases)

[Slay the Spire](https://www.megacrit.com/) mod with character and game design by [Jorbs](https://twitch.tv/jorbs) and Twitch Chat.

* Design info and progress tracking: [Mod Character Package spreadsheet](https://docs.google.com/spreadsheets/d/1GY0eJsooEp361hWFL2lD-uPVa5-l-7g4f4FtyKs-k7Q/edit#gid=0)
* Discussion and updates: [Jorbs Discord](https://discord.gg/invite/jorbs) channels under "Projects that Fuel Wizardry" group

Many people have contributed code and art for this mod; thanks to all the contributors listed among the [mod authors](./src/main/resources/ModTheSpire.json).

This mod would not have been possible without the excellent documentation and guidance from all of the contributors at [ModTheSpire](https://github.com/kiooeht/ModTheSpire), [BaseMod](https://github.com/daviscook477/BaseMod), [StSLib](https://github.com/kiooeht/StSLib), [StS-DefaultModBase](https://github.com/Gremious/StS-DefaultModBase), and [Slay the Spire Discord](https://discordapp.com/invite/SlayTheSpire) **#modding**.

This mod aims to support *both* the stable and beta branches of Slay the Spire, but is primarily developed and tested against the beta branch. If you notice an incompatibility with *either* branch, please [file an issue](https://github.com/dbjorge/jorbs-spire-mod/issues/new)!

## Table of contents

* [How to install](#how-to-install)
* [How to build from source](#how-to-build-from-source)
* [How to contribute art](#how-to-contribute-art)
* [How to contribute sound effects](#how-to-contribute-sound-effects)
* [How to contribute changes](#how-to-contribute-changes)

## How to install

1. Through Steam, install Slay the Spire
1. From the Steam Workshop, install "Mod the Spire", "BaseMod", and "StSLib"
1. Download `JorbsMod.jar` from the latest release: [![GitHub release version badge](https://img.shields.io/github/v/release/dbjorge/jorbs-spire-mod?color=blue&label=latest%20release&sort=semver)](https://github.com/dbjorge/sts-jorbs-mod/releases)
1. Launch Mod the Spire by right clicking on Slay the Spire in your Steam Library "Play with Mods"
1. In the Mod the Spire launcher, select the "open mods folder" button
1. Copy `JorbsMod.jar` into this mods folder
1. Close and relaunch Mod the Spire
1. Make sure "BaseMod", "StSLib", and "JorbsMod" are all checked and in that order
1. Play!

## How to build from source

1. Create an environment variable named `STEAMAPPS_PATH`
    * On Windows, open a Command Prompt run `setx STEAMAPPS_PATH "C:\Program Files (x86)\Steam\steamapps"`
    * On Mac, open `~/.bash_profile` and add a line like `export STEAMAPPS_PATH="~/Library/Application Support/Steam/steamapps"`
    * Adjust the paths accordingly if you have installed Steam to a different location 
1. Install the Oracle Java 8 JDK (not Java 9+)
1. Install IntelliJ IDEA (the free Community edition is fine)
1. Through Steam, install Slay the Spire (stable branch)
1. From the Steam Workshop, install "Mod the Spire", "BaseMod", and "StSLib"
1. Clone the repository
1. Open the project in IntelliJ IDEA:
    1. Choose "Import Project"
    1. Choose the repository folder
    1. Select "Maven"
    1. Press next a few times, all other settings can be left at defaults 
1. Follow the [these instructions from the StS-DefaultModBase wiki](https://github.com/Gremious/StS-DefaultModBase/wiki/Step-3:-Packaging-and-Playing-the-Default;-Writing-Your-First-Mod!) to build the mod package and debug it

## How to contribute art

See the [.../images/README.md](./src/main/resources/stsjorbsmodResources/images/README.md) file in the resources folder.

## How to contribute sound effects

See the [.../audio/README.md](./src/main/resources/stsjorbsmodResources/audio/README.md) file in the resources folder.

## How to contribute translations to other languages

See the [.../localization/README.md](./src/main/resources/stsjorbsmodResources/localization/README.md) file in the resources folder.

## How to contribute changes

*Note: if this is too complicated and you just want to send us some paint art for cards, feel free to reach out on the [Jorbs Discord](https://discord.gg/invite/jorbs) in *#jorbs-spire-mod-char* - someone there will give you a hand!*

This project uses GitHub Pull Requests to handle merging contributed changes. If you're new to using GitHub or Pull Requests, here's the TL;DR for the workflow I like to use:

### First-time setup

1. Create your own fork of this repository by clicking the "Fork" button at the top right of this page (you'll need a GitHub account)
1. [Install Git](https://git-scm.com/downloads)
1. From a command prompt, run the following (replace `your_username` with your GitHub username):
    ```bash
    # This is where the folder for the mod code will be placed inside of.
    # This can be wherever you like; I like something short like "C:\code" or "C:\repos".
    # Don't use a path with spaces in it!
    cd C:/code
    
    # This will clone the repository into a new folder named "jorbs-spire-mod" inside the directory you picked above
    git clone --origin upstream https://github.com/dbjorge/jorbs-spire-mod.git
    cd jorbs-spire-mod
   
    # Replace "your_username" with your GitHub username!
    git remote add my_fork https://github.com/your_username/jorbs-spire-mod.git

    # These are "aliases", helper commands that will make it easier to use GitHub Pull Requests
    git config --global alias.newfeature "!git checkout master && git pull && git checkout -b"
    git config --global alias.pushtofork "!git push --set-upstream my_fork HEAD"
    ```

### Each time you want to start a new feature/contribution...

1. (recommended) Discuss your idea on the [Discord #jorbs-spire-mod-char channel](https://discord.gg/invite/jorbs) first, to make sure noone else is already working on the same thing
1. Run `git newfeature my-cool-feature-name`
1. Make your code changes, build and test locally
1. Use the usual `git add *` and `git commit -m 'description of changes'` commands to make local commits
    * If you're brand new to git, https://try.github.io has some good learning resources

### When you're ready to submit your changes to be reviewed and merged...

1. Run `git pushtofork` from your feature branch
1. [Create a Pull Request on GitHub](https://github.com/dbjorge/jorbs-spire-mod/compare)
