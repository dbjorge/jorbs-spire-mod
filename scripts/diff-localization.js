// Usage: node diff-localization.js old-lang new-lang old-git-tag new-git-tag
// * langs should match directory name under
//   src/main/resources/stsjorbsmodResources/localization/
// * old-git-tag and new-git-tag are "git commit-ish"es

const child_process = require('child_process');
const jsonDiff = require('json-diff');
const path = require('path');
const process = require('process');

if (process.argv.length < 6 || process.argv.length > 7 || (process.argv.length === 7 && process.argv[6] !== '--full')) {
    console.log('Usage: node diff-localization.js old-lang new-lang old-git-tag new-git-tag [--full]');
    process.exit(1);
}

const oldLang = process.argv[2];
const newLang = process.argv[3];
const oldCommit = process.argv[4];
const newCommit = process.argv[5];

const outputFullDiff = process.argv.includes('--full');
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

function getKeywordName(keywordObj) {
    if (keywordObj.PROPER_NAME != undefined) {
        return keywordObj.PROPER_NAME.__old ? keywordObj.PROPER_NAME.__old : keywordObj.PROPER_NAME;
    } else {
        return keywordObj.NAMES[0];
    }
}

function logKeys(code, keys) {
    if (keys.length > 0) {
        console.log(code === '-' ? '  DELETED:' : code === '+' ? '  ADDED:' : '  CHANGED:');
        keys.forEach(k => console.log(`${code}   ${k.replace(/\u00a0/g, '\\u00a0')}`));
    }
}

const colorDiffOpts = { color: false };
const fileDiffOpts = { keysOnly: structureOnly };

for (file of commonFiles) {
    oldFile = getFile(oldCommit, oldLang, file);
    newFile = getFile(newCommit, newLang, file);
    const diff = jsonDiff.diff(oldFile, newFile, fileDiffOpts);
    if (diff == undefined) {
        console.log (`\nNO CHANGE FOR ${file}`);
        continue;
    }

    console.log(`\nCHANGES IN ${file}:`);

    if (outputFullDiff) {
        console.log(jsonDiff.diffString(oldFile, newFile, colorDiffOpts, fileDiffOpts).replace(/\u00a0/g, '\\u00a0'));
    } else { // only show entries which changed
        if (Array.isArray(diff)) {
            // keywords file
            logKeys('-', diffLinesWithCode(diff, '-').map(getKeywordName));
            logKeys('+', diffLinesWithCode(diff, '+').map(getKeywordName))
   
            let changed = [];
            let oldFileIndex = 0;
            for (let diffIndex = 0; diffIndex < diff.length; diffIndex++) {
                const code = diff[diffIndex][0];
                if (code === '~') {
                    changed.push(getKeywordName(oldFile[oldFileIndex]));
                    oldFileIndex++;
                } else if (code === '+') {
                    continue;
                } else {
                    oldFileIndex++;
                }
            }
            logKeys('~', changed);
        } else { // object
            const keys = Object.keys(diff);
            const deleted = keys.filter(k => k.endsWith('__deleted')).map(k => k.replace('__deleted', ''));
            logKeys('-', deleted);
            const added = keys.filter(k => k.endsWith('__added')).map(k => k.replace('__added', ''));
            logKeys('+', added);
            const changed = keys.filter(k => !k.endsWith('__added') && !k.endsWith('__deleted'));
            logKeys('~', changed);
        }
    }
}
