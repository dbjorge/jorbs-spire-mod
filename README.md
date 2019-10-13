# Jorbs Mod

Slay the Spire mod based on [Jorbs](https://twitch.tv/jorbs)' [Mod Character Package spreadsheet](https://docs.google.com/spreadsheets/d/1GY0eJsooEp361hWFL2lD-uPVa5-l-7g4f4FtyKs-k7Q/edit#gid=0). Not official.

Based on [StS-DefaultModBase](https://github.com/Gremious/StS-DefaultModBase).

Discussion in the #jorbs-spire-mod-char channel in the [Jorbs Discord](https://discord.gg/invite/jorbs).

## How to install

1. Through Steam, install Slay the Spire (stable branch)
1. From the Steam Workshop, install "Mod the Spire", "BaseMod", and "StSLib"
1. Download `JorbsMod.jar` from the latest release (https://github.com/dbjorge/sts-jorbs-mod/releases)
1. Launch Mod the Spire by right clicking on Slay the Spire in your Steam Library "Play with Mods"
1. In the Mod the Spire launcher, select the "open mods folder" button
1. Copy `JorbsMod.jar` into this mods folder
1. Close and relaunch Mod the Spire
1. Make sure "BaseMod", "StSLib", and "JorbsMod" are all checked
1. Play!

## How to build from source

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
1. Create a file named `settings.xml` in a folder named `.m2` in your home directory. On Windows, you can open your home directory by typing `%USERPROFILE%` in File Explorer. Paste the following content (or, if `settings.xml` already exists, extend it). **Replace the Steam installation path if you've installed Steam somewhere else!**:
    ```xml
    <settings>
      <profiles>
        <profile>
          <id>inject-local-paths</id>
          <properties>
            <Steam.path>C:/Program Files/Steam/steamapps</Steam.path>
            <STS.jar.path>${Steam.path}/common/SlayTheSpire/desktop-1.0.jar</STS.jar.path>
          </properties>
        </profile>
      </profiles>
     
      <activeProfiles>
        <activeProfile>inject-local-paths</activeProfile>
      </activeProfiles>
    </settings>
    ```
1. Follow the [these instructions from the StS-DefaultModBase wiki](https://github.com/Gremious/StS-DefaultModBase/wiki/Step-3:-Packaging-and-Playing-the-Default;-Writing-Your-First-Mod!) to build the mod package and debug it

## How to contribute changes

This project uses GitHub Pull Requests to handle merging contributed changes. If you're new to using GitHub or Pull Requests, here's the TL;DR for the workflow I like to use:

### First-time setup

1. Create your own fork of this repository by clicking the "Fork" button at the top right of this page (you'll need a GitHub account)
1. [Install Git](https://git-scm.com/downloads)
1. From a command prompt, run the following (replace `your_username` with your GitHub username):
    ```sh
    cd C:/path/to/code
   
    git clone --origin upstream https://github.com/dbjorge/jorbs-spire-mod.git
    cd jorbs-spire-mod
   
    git remote add my_fork https://github/your_username/jorbs-spire-mod.git
   
    git config --global alias.newfeature "!git checkout master && git pull && git checkout -b"
    git config --global alias.pushtofork "!git push --set-upstream my_fork HEAD"
    ```

### Each time you want to start a new feature/contribution...

1. (recommended) Discuss your idea on the [Discord #jorbs-spire-mod-char channel](https://discord.gg/invite/jorbs) first, to make sure noone else is already working on the same thing
1. Run `git newfeature my-cool-feature-name`
1. Make your code changes, build and test locally
1. Use `git add *` and `git commit -m 'description of changes'` to make *local* commits

### When you're ready to submit your changes to be reviewed and merged...

1. Run `git pushtofork` from your feature branch
1. [Create a Pull Request on GitHub](https://github.com/dbjorge/jorbs-spire-mod/compare)