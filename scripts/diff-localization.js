// Usage: node diff-localization.js old-lang new-lang old-git-tag new-git-tag
// * langs should match directory name under
//   src/main/resources/stsjorbsmodResources/localization/
// * old-git-tag and new-git-tag are "git commit-ish"es

const child_process = require('child_process');
const fs = require('fs');
const jsonDiff = require('json-diff');
const path = require('path');
const process = require('process');
const util = require('util');

if (process.argv.length != 6) {
    console.log('Usage: node diff-localization.js old-lang new-lang old-git-tag new-git-tag');
    process.exit(1);
}

const oldLang = process.argv[2];
const newLang = process.argv[3];
const oldCommit = process.argv[4];
const newCommit = process.argv[5];

const structureOnly = (oldLang !== newLang);

console.log(`COMPARING ${oldLang}@${oldCommit} vs ${newLang}@${newCommit}`);
console.log(structureOnly ? 'COMPARING STRUCTURE ONLY' : 'COMPARING STRUCTURE AND VALUES');
console.log('');

const locDir = path.join(__dirname, '../src/main/resources/stsjorbsmodResources/localization');

function getFiles(commit, lang) {
    const gitOutput = child_process.execSync(`git ls-tree -r ${commit} --name-only ./${lang}`, {cwd: locDir});
    const filesRelativeToLocDir = gitOutput.toString().trim().split('\n');
    return filesRelativeToLocDir.map(f => f.substring(lang.length + 1)).sort();
}

const oldFiles = getFiles(oldCommit, oldLang);
const newFiles = getFiles(newCommit, newLang);
const fileDiff = jsonDiff.diff(oldFiles, newFiles);

function diffLinesWithCode(diffLines, code /* ' ', '+', '-' */) {
    return diffLines
        .filter(([diffCode, _]) => diffCode === code)
        .map(([_, val]) => val);
}
function diffLinesWithCodes(diffLines, codes /* ' ', '+', '-' */) {
    return diffLines
        .filter(([diffCode, _]) => codes.contains(diffCode))
        .map(([_, val]) => val);
}

let commonFiles = oldFiles;
if (fileDiff != undefined) { // there exist differences
    commonFiles = diffLinesWithCode(fileDiff, ' ');
    removedFiles = diffLinesWithCodes(fileDiff, ['-', '~']);
    addedFiles = diffLinesWithCodes(fileDiff, ['+', '~']);
    if (removedFiles.length > 0) {
        console.log('FILES REMOVED:');
        removedFiles.forEach(f => console.log('  - ' + f));
        console.log('');
    }
    if (addedFiles.length > 0) {
        console.log('FILES ADDED:');
        addedFiles.forEach(f => console.log('  + ' + f));
        console.log('');
    }
}

function getFile(commit, lang, file) {
    const gitOutput = child_process.execSync(`git show ${commit}:./${lang}/${file}`, {cwd: locDir});
    return JSON.parse(gitOutput.toString());
}

const fileDiffOpts = { keysOnly: structureOnly };

for (file of commonFiles) {
    oldFile = getFile(oldCommit, oldLang, file);
    newFile = getFile(newCommit, newLang, file);
    const diff = jsonDiff.diff(oldFile, newFile, fileDiffOpts);
    if (diff == undefined) {
        console.log (`NO CHANGE FOR ${file}`);
        continue;
    }

    console.log(`CHANGES IN ${file}:\n----------\n`)
    console.log(jsonDiff.diffString(oldFile, newFile, {}, fileDiffOpts));
    console.log('\n----------\n');
}

/*
const tempDir = path.join(process.env.TEMP, fs.mkdtempSync('stsjorbsmod-diff-localization'));
fs.mkdirSync(tempDir);

const startDir = path.join(locDir, startLang);
const endDir = path.join(locDir, endLang);



fs.readdirSync()

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
*/