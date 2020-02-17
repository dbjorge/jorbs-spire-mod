// Usage: node scripts/release.js 1.2.3
const child_process = require('child_process');
const fs = require('fs');
const path = require('path');
const process = require('process');
const readline = require('readline');

const version = process.argv[2];

const d = new Date();
const monthStr = `${d.getMonth() + 1}`.padStart(2, '0');
const dateStr = `${d.getDate()}`.padStart(2, '0');
const dateStamp = `${d.getFullYear()}-${monthStr}-${dateStr}`;

console.log('Verifying git status is clean');
const status = child_process.execSync('git status --porcelain');
if (status.toString() !== '') {
    console.log('"git status --procelain" gave non-empty stdout; aborting release');
    process.exit(1);
}

console.log(`Beginning update to v${version} - ${dateStamp}...`);
const versionRegex = /^[1-9]\d*\.[1-9]\d*\.[1-9]\d*$/;
if (!versionRegex.test(version)) {
    console.log(`Invalid version number "${version}", should look like "1.2.3"`);
    process.exit(2);
}

console.log('Updating version number in pom.xml...');
const pomXmlPath = path.join(__dirname, '../pom.xml');
let pomXmlContent = fs.readFileSync(pomXmlPath).toString();
pomXmlContent = pomXmlContent.replace(/<version>\d+\.\d+\.\d+<\/version>/, `<version>${version}</version>`);
fs.writeFileSync(pomXmlPath, pomXmlContent);

console.log('Extracting changelog from CHANGELOG.md...');
const changelogPath = path.join(__dirname, '../CHANGELOG.md');
let changelogContent = fs.readFileSync(changelogPath).toString();
const unreleasedChangelogContent = /## \[Unreleased\]\n((.|\n)*?)\n## \[/m.exec(changelogContent)[1].trim();
console.log(`---\n${unreleasedChangelogContent}\n---`);

console.log(`Moving CHANGELOG.md [Unreleased] section to new section for v${version}...`)
changelogContent = changelogContent.replace('## [Unreleased]', `## [Unreleased]\n\n## [v${version}] - ${dateStamp}`)
fs.writeFileSync(changelogPath, changelogContent);

console.log('Generating Steam Workshop changeNote from version + CHANGELOG...');
const steamChangeNoteContent = `v${version}\n\n` + unreleasedChangelogContent
    .replace(/^#### (.*)$/mg, (s, text) => `[h4]${text}[/h4]`)
    .replace(/^### (.*)$/mg, (s, text) => `[h3]${text}[/h3]`)
    .replace(/^\* /mg, '[*] ')
    .replace(/\n\n\[\*\]/mg, (s) => '\n[list]\n\[*\]')
    .replace(/(\[\*\][^\n]*)\n\n/mg, (s, line) => `${line}\n[/list]\n`)
    .replace(/\[(.*)\]\((.*)\)/g, (s, label, url) => `[url=${url}]${label}[/url]`)
    .replace(/\*\*\*(.*)\*\*\*/g, (s, text) => `[b][i]${text}[/i][/b]`)
    .replace(/\*\*(.*)\*\*/g, (s, text) => `[b]${text}[/b]`)
    .replace(/\*(.*)\*/g, (s, text) => `[i]${text}[/i]`);
console.log(`---\n${steamChangeNoteContent}\n---`);

console.log('Updating Steam Workshop config.json changeNote field with version + changelog...');
const configJsonPath = path.join(__dirname, '../steam_workshop/config.json');
let configJsonContent = JSON.parse(fs.readFileSync(configJsonPath).toString());
configJsonContent.changeNote = steamChangeNoteContent;
fs.writeFileSync(configJsonPath, JSON.stringify(configJsonContent, null, 2));

console.log('"git add" updated files...')
child_process.exec(`git add "${changelogPath}" "${pomXmlPath}" "${configJsonPath}"`, {stdio: 'inherit'});

const javaPath = path.join(process.env.STEAMAPPS_PATH, 'common/SlayTheSpire/jre/bin/java.exe');
const modUploaderPath = path.join(process.env.STEAMAPPS_PATH, 'common/SlayTheSpire/mod-uploader.jar');
const steamWorkspacePath = path.join(__dirname, '../steam_workshop');
const hubReleaseInput = `v${version}\n\n${unreleasedChangelogContent}`;
const jarPath = path.join(steamWorkspacePath, 'content/JorbsMod.jar');

console.log('Cleaning obsolete jar file (must be rebuilt with new version)');
fs.unlinkSync(jarPath);

console.log(`Successfully prepared release files. Next steps:
  * Verify that 'git status' looks reasonable
  * Run a build to pick up the new version number
  * Open the build and start a run in main + beta branches

Do not commit/tag/push/upload anything.
`)

const rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout,
});

function promptToContinue() {
  rl.question("When you're ready to go, hit enter", () => {
    if (!fs.existsSync(jarPath)) {
        console.log(`Error: jar not built at ${jarPath}`);
        promptToContinue();
        return;
    }

    console.log('CHOO CHOO');
    /*
    console.log(`Commiting/tagging/pushing updated files as tag v${version}`);
    child_process.exec(`git commit -m 'Bump to v${version}'`, {stdio: 'inherit'});
    child_process.exec(`git tag v${version}`, {stdio: 'inherit'});
    child_process.exec(`git push origin v${version}`, {stdio: 'inherit'});

    console.log(`Uploading new version to the steam workshop...`);
    child_process.execSync(`"${javaPath}" -jar "${modUploaderPath}" upload -w "${steamWorkspacePath}"`, {stdio: 'inherit'});

    console.log(`Uploading new version as a GitHub release...`);
    child_process.execSync(`hub release create --attach "${jarPath}" --file - v${version}`, {input: hubReleaseInput, stdio: 'inherit'});
    */
  });
}

promptToContinue();
