// This takes all the "original" images under:
//
//     /src/main/resources/stsjorbsmodResources/images/cards/originals/**/*.png
//
// ...which should be *uncropped* image at 500x380, and for each of them, applies the appropriate masking and resizing
// based on card type to generate images to be used by the game in:
//
//     /src/main/resources/stsjorbsmodResources/images/cards/generated/*.png
//
// This requires "magick" be present on your path to run. On Windows, you can get it by installing Chocolatey and
// then running "choco install imagemagick"
//
// Usage:
//     node update-card-images.js

const fs = require('fs');
const path = require('path');
const child_process = require('child_process');

const cardMasksByCardType = {
  'ATTACK': path.join(__dirname, 'card_masks/attack.png'),
  'SKILL': path.join(__dirname, 'card_masks/skill.png'),
  'POWER': path.join(__dirname, 'card_masks/power.png'),
  'STATUS': path.join(__dirname, 'card_masks/skill.png'),
  'CURSE': path.join(__dirname, 'card_masks/skill.png'),
};

const inputDirectory = path.join(__dirname, '../src/main/resources/stsjorbsmodResources/images/cards/originals');
const outputDirectory = path.join(__dirname, '../src/main/resources/stsjorbsmodResources/images/cards/generated');
const srcDirectory = path.join(__dirname, '../src/main/java/stsjorbsmod/cards');

function findFilesRecursiveSync(directory, filePattern) {
  const results = [];
  const shallowFiles = fs.readdirSync(directory);
  for (const shallowFile of shallowFiles) {
    const fullPath = path.join(directory, shallowFile);
    const stat = fs.statSync(fullPath);
    if (stat.isDirectory()) {
      results.push(...findFilesRecursiveSync(fullPath, filePattern));
    } else if (filePattern.test(shallowFile)) {
      results.push(fullPath);
    }
  }
  return results;
}

const cardTypeRegex = /static final CardType TYPE = CardType\.((ATTACK)|(SKILL)|(POWER)|(STATUS)|(CURSE));/
function readCardTypeFromSource(imgBaseName) {
  const baseNameNoExt = imgBaseName.replace('.png', '');
  const srcFiles = findFilesRecursiveSync(srcDirectory, new RegExp(`^${baseNameNoExt}\.java$`));
  if (srcFiles.length !== 1) {
    console.warn(`!!! Can't find source file ${baseNameNoExt}.java, skipping card generation`)
    return null;
  }
  const srcContents = fs.readFileSync(srcFiles[0]).toString();
  const matches = cardTypeRegex.exec(srcContents)
  if (matches == undefined || matches[1] == undefined) {
    throw `Couldn't identify CardType TYPE in source file ${srcFiles[0]}`
  }
  return matches[1];
}

const originalFullPaths = findFilesRecursiveSync(inputDirectory, /\.png$/);
for (originalFullPath of originalFullPaths) {
  const baseName = path.basename(originalFullPath);
  const cardType = readCardTypeFromSource(baseName);
  if (cardType == null) {
    continue;
  }
  const smallMask = cardMasksByCardType[cardType];
  const smallOutput = path.join(outputDirectory, baseName);
  const bigMask = smallMask.replace(/\.png$/, '_p.png');
  const bigOutput = smallOutput.replace(/\.png$/, '_p.png');

  console.log(`[${cardType}] ${originalFullPath}`);
  child_process.execSync(`magick "${originalFullPath}" -resize 500x380 "${bigMask}" -alpha Off -compose CopyOpacity -composite "${bigOutput}"`);
  child_process.execSync(`magick "${originalFullPath}" -resize 250x190 "${smallMask}" -alpha Off -compose CopyOpacity -composite  "${smallOutput}"`);
}
