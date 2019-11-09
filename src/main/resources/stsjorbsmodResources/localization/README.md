# Localization

This folder contains "strings" files used for localization. Until the mod is more stable, we will only be supporting English.

There is a script in the repo at `/build/GenerateLocalization.ps1` that can use Google Translate to machine-translate the english file to other languages; however, it takes time to run and requires some manual fix-up afterward, so we aren't using it until the mod is more stable.

## Sort information for the string localization files
* eng files are used as the target order.
* eng sections are alphabetized.
* Other localizations (when we add them) will follow the eng ordering.

## Keyword-specific info

**Keywords** are special terms that get highlighted when they appear in card descriptions. They are defined in
`JorbsMod-Keyword-Strings.json`. They are sorted as:

* Status effects that can apply to enemies.
* Memory mechanics.
* Memory groups.
* Individual memories.