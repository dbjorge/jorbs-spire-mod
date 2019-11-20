// Usage: node sort-localization-file.js ./JorbsMod-Card-Strings.json
const fs = require('fs');
const process = require('process');

function compareByFirstName(a, b) {
    var nameA = a.NAMES[0].toUpperCase();
    var nameB = a.NAMES[0].toUpperCase();
    if (nameA < nameB) {
        return -1;
    }
    if (nameA > nameB) {
        return 1;
    }
    return 0;
}

const file = process.argv[2];
const rawContents = fs.readFileSync(file, { encoding: 'utf8'});
const originalContents = JSON.parse(rawContents);
const sortOrder = ["PROPER_NAME", "NAME", "NAMES", "FLAVOR", "DESCRIPTIONS", "DESCRIPTION", "UPGRADE_DESCRIPTION", "EXTENDED_DESCRIPTION", "OPTIONS", "TEXT"];
if (Array.isArray(originalContents)) {
    console.log("IGNORING KEYWORD FILE");
    process.exit(1);
} else {
    sortOrder.push(...Object.keys(originalContents).sort());
}
const sortedContents = JSON.stringify(originalContents, sortOrder, 2);
const formattedContents = sortedContents.replace(/\u00a0/g, '\\u00a0');
fs.writeFileSync(file, formattedContents, { encoding: 'utf8' });
