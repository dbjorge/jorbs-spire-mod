# Wanderer art instructions

In order to make art for the new class, read this guide. The list of art we need can be found in the [Mod Character Package spreadsheet](https://docs.google.com/spreadsheets/d/1GY0eJsooEp361hWFL2lD-uPVa5-l-7g4f4FtyKs-k7Q/edit#gid=0) and in the readme for each card catagory.

# How to make card images

Every card needs one 500x380 .png image, placed in `./cards/originals/`.

For example, the card "Fresh Adventure" has a 500x380 image at `./cards/originals/Damage_Commons/FreshAdventure.png`.

We generate the other sizes of images and crop them to fit card borders using
a script at `<repository_root>/scripts/generate-card-images.js`. The resulting
generated images live under `./cards/generated/`; they are checked in for ease
of building the project, but should not be modified directly (use the script).

Please feel free to create images for the cards and send a Pull Request adding
them to `./cards/originals`, so long as they are in the correct size/format.
Alternatively, if you want to make art but don't want to process it or merge it
in, you can send it to neablis@gmail.com (or @Neablis on discord) to have it
formatted and merged for you. 

# How to make power icons

Powers (aka conditions or statuses) need 2 sizes - 
1. 32px x 32px: This is the static image on the buff bar
2. 84px x 84px: This is the visual glow effect when a power 'activates'. 

The background should be transparent and the power itself roughly circular. See examples [here](https://github.com/neablis-7/jorbs-spire-mod/blob/master/src/main/resources/stsjorbsmodResources/images/powers/placeholder_power32.png) and [here](https://github.com/neablis-7/jorbs-spire-mod/blob/master/src/main/resources/stsjorbsmodResources/images/powers/placeholder_power84.png). 

# Other Misc. stuff: 

See the bottom of the Wanderer sheet at [Mod Character Package spreadsheet](https://docs.google.com/spreadsheets/d/1GY0eJsooEp361hWFL2lD-uPVa5-l-7g4f4FtyKs-k7Q/edit#gid=0)
