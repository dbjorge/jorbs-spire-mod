// This takes all the "original" relic images under:
//
//     /src/main/resources/stsjorbsmodResources/images/relics/*.png
//
// ...which should be 128x128 pngs with everything but the center 50-60px square
// area transparent, and generates outline images under
//
//     /src/main/resources/stsjorbsmodResources/images/relics/generated/*_outline.png
//
// This requires "magick" be present on your path to run. On Windows, you can get
// it by installing Chocolatey and then running "choco install imagemagick"
//
// Usage (regenerate all relic outlines):
//     node generate-relic-outlines.js
//
// Usage (regenerate only relics whose paths match a given substring):
//     node generate-relic-outlines.js SomeSubstring

const child_process = require('child_process');
const fs = require('fs');
const path = require('path');
const process = require('process');

const relicFilterSubstring = process.argv.length > 2 ? process.argv[2] : null;

const inputDirectory = path.join(__dirname, '../src/main/resources/stsjorbsmodResources/images/relics');
const outputDirectory = path.join(__dirname, '../src/main/resources/stsjorbsmodResources/images/relics/generated');

const inputFileNames = fs.readdirSync(inputDirectory).filter(f => /\.png/.test(f));

skips = 0;
for (const inputFileName of inputFileNames) {
  if (relicFilterSubstring != null && !originalFullPath.includes(relicFilterSubstring)) {
    skips += 1
    continue;
  }

  const outputFileName = inputFileName.replace('.png', '_outline.png');
  const inputFullPath = path.join(inputDirectory, inputFileName)
  const outputFullPath = path.join(outputDirectory, outputFileName);

  console.log(`${inputFileName} -> ${outputFileName}`);
  child_process.execSync(`magick "${inputFullPath}" -colorspace RGB -channel RGB -threshold -1 -channel RGBA -blur "0x1" -channel A -threshold "1%" -strip "${outputFullPath}"`);  
}

console.log("Done!")
if (skips > 0) {
  console.log(`Filter parameter ${relicFilterSubstring} resulted in skipping ${skips} images`)
}