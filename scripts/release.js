// Usage: node scripts/release.js 1.2.3
//
// Prerequisites:
//   * Have the Steam client installed and logged into the jorbsmods account
//   * Have the GitHub CLI installed and auth'd as an account that can push GitHub releases
//   * Have a clean "git status" (no files staged or modified)
//   * Have the "master" branch checked out
//
// This script will first prepare the release:
//
//   * Extract changelog info from CHANGELOG.md's [Unreleased] section
//   * Produce GitHub, Steam, and Discord-formatted release notes
//   * Update pom.xml, steam_workshop/config.json, and CHANGELOG.md
//   * Clean up any stale JorbsMod.jar file
//
// Then, it will prompt you to check its work, rebuild the mod, and test it.
// Once you confirm you've done that, it will:
//
//   * Commit/push the updated files
//   * Create/push a git tag
//   * Upload a new Steam Workshop release
//   * Upload a new GitHub release
//   * Output a suggested Discord release message to the console

const child_process = require('child_process');
const fs = require('fs');
const os = require('os');
const path = require('path');
const process = require('process');
const readline = require('readline');

const version = process.argv[2];

const d = new Date();
const monthStr = `${d.getMonth() + 1}`.padStart(2, '0');
const dateStr = `${d.getDate()}`.padStart(2, '0');
const dateStamp = `${d.getFullYear()}-${monthStr}-${dateStr}`;

function logNote(noteLabel, noteContent) {
    console.log(`--- vvv ${noteLabel} vvv ---`);
    console.log(noteContent);
    console.log(`--- ^^^ ${noteLabel} ^^^ ---`);
}

console.log(`Beginning update to v${version} - ${dateStamp}...`);

console.log('Verifying necessary tools are available...');
const javaPath = path.join(process.env.STEAMAPPS_PATH, 'common/SlayTheSpire/jre/bin/java.exe');
const modUploaderPath = path.join(process.env.STEAMAPPS_PATH, 'common/SlayTheSpire/mod-uploader.jar');
if (!fs.existsSync(javaPath)) {
    console.log(`Cannot find ${javaPath}; is STEAMAPPS_PATH set and Slay the Spire installed?`)
}
if (!fs.existsSync(modUploaderPath)) {
  console.log(`Cannot find ${modUploaderPath}; is STEAMAPPS_PATH set and Slay the Spire installed?`)
}
child_process.execSync('gh release list --limit 1'); // this verifies that GitHub CLI is installed

console.log('Verifying git status is clean...');
const status = child_process.execSync('git status --porcelain');
if (status.toString() !== '') {
    console.log('"git status --procelain" gave non-empty stdout; aborting release');
    process.exit(1);
}

console.log('Verifying format of version number...');
const versionRegex = /^[1-9]\d*\.(0|([1-9]\d*))\.(0|([1-9]\d*))*$/;
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
logNote('CHANGELOG [Unreleased] SECTION', unreleasedChangelogContent);

console.log(`Moving CHANGELOG.md [Unreleased] section to new section for v${version}...`)
changelogContent = changelogContent.replace('## [Unreleased]', `## [Unreleased]\n\n## [v${version}] - ${dateStamp}`)
fs.writeFileSync(changelogPath, changelogContent);

console.log('Generating Steam Workshop changeNote from version + CHANGELOG...');
const steamChangeNoteContent = `v${version}\n\n` + unreleasedChangelogContent
    .replace(/^#### (.*)$/mg, (s, text) => `[h4]${text}[/h4]`)
    .replace(/^### (.*)$/mg, (s, text) => `[h3]${text}[/h3]`)
    .replace(/^\* /mg, '[*] ')
    .replace(/\n\n\[\*\]/mg, (s) => '\n[list]\n\[*\]')
    .replace(/(\[\*\][^\n]*)(\n\n|$)/g, (s, line) => `${line}\n[/list]\n`)
    .replace(/\[(.*)\]\((.*)\)/g, (s, label, url) => `[url=${url}]${label}[/url]`)
    .replace(/\*\*\*(.*)\*\*\*/g, (s, text) => `[b][i]${text}[/i][/b]`)
    .replace(/\*\*(.*)\*\*/g, (s, text) => `[b]${text}[/b]`)
    .replace(/\*(.*)\*/g, (s, text) => `[i]${text}[/i]`);
logNote('STEAM', steamChangeNoteContent);

console.log('Updating Steam Workshop config.json changeNote field with version + changelog...');
const configJsonPath = path.join(__dirname, '../steam_workshop/config.json');
let configJsonContent = JSON.parse(fs.readFileSync(configJsonPath).toString());
configJsonContent.changeNote = steamChangeNoteContent;
fs.writeFileSync(configJsonPath, JSON.stringify(configJsonContent, null, 2));

console.log('Cleaning obsolete jar file...');
const steamWorkspacePath = path.join(__dirname, '../steam_workshop');
const jarPath = path.join(steamWorkspacePath, 'content/JorbsMod.jar');
try {
    fs.unlinkSync(jarPath);
} catch(err) {
    if (err.code !== 'ENOENT') { throw err; }
}

console.log('Generating alternate formats of release notes...')
const githubReleaseChangelogPath = path.join(os.tmpdir(), 'jorbsmod-github-changelog.md');
const githubReleaseChangelogContent = `v${version}\n\n${unreleasedChangelogContent}`;
fs.writeFileSync(githubReleaseChangelogPath, githubReleaseChangelogContent);
logNote('GITHUB', githubReleaseChangelogContent);
const suggestedDiscordPost = `**Released v${version}**, available at https://mod.jorbs.tv/steam\n\n${unreleasedChangelogContent}`;
logNote('DISCORD', suggestedDiscordPost);

console.log('"git add" updated files...')
child_process.exec(`git add "${changelogPath}" "${pomXmlPath}" "${configJsonPath}"`, {stdio: 'inherit'});

console.log(`Successfully prepared release files. Next steps:
  * Verify that 'git diff' looks reasonable
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

    console.log(`Commiting/tagging/pushing updated files as tag v${version}`);
    child_process.execSync(`git commit -m "Bump to v${version}"`, {stdio: 'inherit'});
    child_process.execSync(`git push`, {stdio: 'inherit'});
    child_process.execSync(`git tag v${version}`, {stdio: 'inherit'});
    child_process.execSync(`git push origin v${version}`, {stdio: 'inherit'});

    console.log(`Uploading new version to the steam workshop...`);
    child_process.execSync(`"${javaPath}" -jar "${modUploaderPath}" upload -w "${steamWorkspacePath}"`, {stdio: 'inherit'});

    console.log(`Uploading new version as a GitHub release...`);
    child_process.execSync(`gh release create v${version} "${jarPath}#JorbsMod.jar" --notes-file "${githubReleaseChangelogPath}"`);

    console.log('Done! Suggested Discord post:\n');
    // Repeating so it'll be below the fold of "uploading..." spam
    logNote('DISCORD', suggestedDiscordPost);
  });
}

promptToContinue();
