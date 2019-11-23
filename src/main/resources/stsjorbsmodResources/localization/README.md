# Localization

Each subfolder contains a different language's translation. Each language has the same set of "Strings" files, but with
different language-specific contents.

## Contributing translations

We are happy to accept translations of the mod; however, note that the mod is still under rapid development (cards,
events, relics, etc are all being added and changed regularly), so be aware that translations will require ongoing
maintenance, not just a one-time contribution.

To add a new language:

1. Copy the `eng` folder (along with all of its string files) to a new folder for the new language
   (`fra`, `deu`, etc).
2. Start by translating **keywords**. Keywords are special terms that will appear highlighted with tooltips when they
   are used in other contexts (eg, card descriptions). Open `JorbsMod-Keyword-Strings.json`. Don't change the special
   terms `PROPER_NAME`, `NAMES`, or `DESCRIPTIONS`, but do translate the other strings they refer to. For example, in
   `./eng/JorbsMod-Keyword-Strings.json`, "burning"
   looks like:
   
   ```json
   {
     "NAMES": ["burning"],
     "DESCRIPTION": "Burning creatures take damage at the start of their turn and have healing reduced by 50%. Each turn, Burning is reduced by half."
   }
   ```
   
   ... and the French translation in `./fra/JorbsMod-Keyword-Strings.json` looks like...
   
   ```json
   {
     "NAMES": ["brûlure"],
     "DESCRIPTION": "Les créatures qui brûlent subissent des dégats au début de leur tour et leurs soins sont réduits de moitié. Chaque tour, la Brûlure est diminuée de moitié."
   }
   ```
   
   If a keyword's translation needs to use multiple words, you need to use `\u00a0` instead of a space between words
   in their `NAMES` section (see the english **Material Components** keyword for an example).
3. Translate all the other files similarly, taking care to use the *exact* keyword translations you used in the
   keywords file. For example, the card **Burning Flask** in `./eng/JorbsMod-Card-Strings.json` looks like:
   
   ```json
   "stsjorbsmod:BurningFlask": {
     "NAME": "Burning Flask",
     "DESCRIPTION": "Apply !M! stsjorbsmod:Burning."
   }
   ```
   
   ... and the French translation (using keyword `brûlure` in place of `burning`) looks like...
   
   ```json
   "stsjorbsmod:BurningFlask": {
     "NAME": "Flasque brûlante",
     "DESCRIPTION": "Appliquez !M! de stsjorbsmod:Brûlure."
   }
   ```

### Things to keep in mind when translating
* Numeric placeholders inside `!`s (`!D!`, `!B!`, `!M!`, `!stsjorbsmod:BaseDamage!`, `!stsjorbsmod:MetaMagic!`,
  etc) exactly as-is (do not translate them)!
* If a keyword's translation needs to use multiple words, you need to use `\u00a0` instead of a space between words
  in their `NAMES` section (see the english **Material Components** keyword for an example).
* You can add new variations of a keyword to the keyword's `NAMES` section if you need to use more types of tenses,
  pluralizations, etc than English

### How to tell what's changed since a translation was last updated

You can see all the code changes since a given version of the mod was released by using this link, but replacing the
version number in the link with the version you want to compare since:

```
https://github.com/dbjorge/jorbs-spire-mod/compare/v0.41.0...master
                                                   ^^^^^^^
```

## Generating translations

There is a script in the repo at `/scripts/GenerateLocalization.ps1` that can use Google Translate to machine-translate
the english file to other languages; however, it takes time to run and requires some manual fix-up afterward, so we
aren't using it until the mod is more stable.
