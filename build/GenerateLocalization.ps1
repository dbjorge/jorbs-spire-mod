Param(
    [string]$LanguageCode, # as interpreted by google translate, usually 2-letter
    [string]$LanguageFolder, # to match StS's codes, usually 3-letter
    [string]$OriginalCode = "en",
    [string]$OriginalFolder = "eng", 
    [string]$LocalizationsPath = "$PSScriptRoot/../src/main/resources/stsjorbsmodResources/localization",
    [string]$ModID = "stsjorbsmod", # for keyword prefixes
    [string]$KeywordsFile = "JorbsMod-Keyword-Strings.json",
    [switch]$Force,
    [switch]$MockTranslationService
)

$script:ErrorActionPreference = 'Stop';
Set-StrictMode -Version Latest;

$ForcedTranslations = @{
    # avoid @{} because it is case-insensitive
    'fr' = New-Object System.Collections.Hashtable;
}

$ForcedTranslations['fr']['kindness'] = 'gentillesse';
$ForcedTranslations['fr']['Kindness'] = 'Gentillesse';
$ForcedTranslations['fr']['patience'] = 'patience';
$ForcedTranslations['fr']['Patience'] = 'Patience';
$ForcedTranslations['fr']['snap'] = 'claquer';
$ForcedTranslations['fr']['Snap'] = 'Claquer';
$ForcedTranslations['fr']['sloth'] = 'paresse';
$ForcedTranslations['fr']['Sloth'] = 'Paresse';

$DestPath = Join-Path $LocalizationsPath $LanguageFolder;
$SrcPath = Join-Path $LocalizationsPath $OriginalFolder;

if (-not (Test-Path -LiteralPath $SrcPath)) {
    throw "Original folder not found at $SrcPath";
}

if (-not $Force -and (Test-Path -LiteralPath $DestPath)) {
    throw "Destination folder $DestPath already exists; did you mean to use -Force?";
}

if (Test-Path $DestPath) {
    Remove-Item -Recurse -LiteralPath $DestPath;
}

mkdir $DestPath;

chcp 65001
[Console]::OutputEncoding = [Console]::InputEncoding = $OutputEncoding = New-Object System.Text.UTF8Encoding

function Translate($Text) {
    $Text = $Text -replace ' #b',' ';
    $Text = $Text -replace ' #y',' ';
    $Text = $Text -replace ' #r',' ';
    $Text = $Text -replace ' #g',' ';

    Write-Debug "<<< $Text";
    if ($ForcedTranslations.ContainsKey($LanguageCode) -and $ForcedTranslations[$LanguageCode].ContainsKey($Text)) {
        $Translation = $ForcedTranslations[$LanguageCode][$Text];
    } elseif ($MockTranslationService) {
        $Words = $Text.Split(' ');
        $Words = @($Words | ForEach-Object { $_ + "-o" })
        $Translation = [string]::Join(' ', $Words);
    } else {
        $Translation = gtran "-source=$OriginalCode" "-target=$LanguageCode" "-text=`"$Text`"";
    }

    # Google Translate preserves leading but not trailing spaces
    if ($Text.EndsWith(' ') -and -not $Translation.EndsWith(' ')) {
        $Translation += ' ';
    }
    
    Write-Debug ">>> $Translation";
    return $Translation;
}

# First, translate keywords; we'll be using them during auto-translation of everything else
$KeywordsFilePath = (Join-Path $SrcPath $KeywordsFile);
$KeywordsFileContent = Get-Content -LiteralPath $KeywordsFilePath | ConvertFrom-Json;
$KeywordMappings = @{};
foreach ($KeywordInfo in $KeywordsFileContent) {
    foreach ($KeywordName in $KeywordInfo.NAMES) {
        if ($KeywordName.Contains(' ')) {
            continue;
        }
        $Keyword = "$($ModID)ZZZZZZ$($KeywordName)";
        $FriendlyKeywordName = $KeywordName -replace '_',' ';
        $FriendlyKeywordTranslation = Translate $FriendlyKeywordName;
        $TranslatedKeywordName = $FriendlyKeywordTranslation -replace ' ','_';
        $TranslatedKeyword = "$($ModID):$($TranslatedKeywordName)";
        $KeywordMappings[$Keyword] = $TranslatedKeyword;
    }
}

function TranslateWithKeywords($Text) {
    $Text = $Text -replace "!D!","101";
    $Text = $Text -replace "!M!","102";
    $Text = $Text -replace "!B!","103";
    $Text = $Text -replace "!stsjorbsmod:MetaMagic!","104";
    $Text = $Text -replace "!stsjorbsmod:UrMagic!","105";
    $Text = $Text -replace "!C!","106";
    $Text = $Text -replace "!P!","107";
    $Text = $Text -replace "!W!","108";
    $Text = $Text -replace "!S!","109";
    $Text = $Text -replace "!G!","110";
    $Text = $Text -replace "$($ModID):","$($ModID)ZZZZZZ";

    $Text = Translate $Text;

    foreach ($KeywordPair in $KeywordMappings.GetEnumerator()) {
        $Text = $Text -replace $KeywordPair.Key,$KeywordPair.Value;
    }
    $Text = $Text -replace "101","!D!";
    $Text = $Text -replace "102","!M!";
    $Text = $Text -replace "103","!B!";
    $Text = $Text -replace "104","!stsjorbsmod:MetaMagic!";
    $Text = $Text -replace "105","!stsjorbsmod:UrMagic!";
    $Text = $Text -replace "106","!C!";
    $Text = $Text -replace "107","!P!";
    $Text = $Text -replace "108","!W!";
    $Text = $Text -replace "109","!S!";
    $Text = $Text -replace "110","!G!";

    return $Text
}

function TranslateStringsRecursively($JsonValue) {
    if ($JsonValue -is [string]) {
        return TranslateWithKeywords $JsonValue;
    } elseif ($JsonValue -is [System.Array]) {
        return [System.Array]@($JsonValue | ForEach-Object { TranslateStringsRecursively $_ });
    } elseif ($JsonValue -is [double] -or $JsonValue -is [int]) {
        return $JsonValue;
    } elseif ($JsonValue -is [PSObject]) {
        # note: we don't translate keys (they are either IDs or special NAME/DESCRIPTION type strings)
        $Map = @{}
        foreach ($Pair in $JsonValue.PSObject.Properties) {
            if ($Pair.Value -is [System.Array]) {
                $Map[$Pair.Name] = @(TranslateStringsRecursively $Pair.Value);
            } else {
                $Map[$Pair.Name] = (TranslateStringsRecursively $Pair.Value);
            }
        }
        return $Map;
    }
}

foreach ($SrcStringFile in Get-ChildItem $SrcPath) {
    $DestFilePath = Join-Path $DestPath $SrcStringFile.Name;
    Write-Host "Generating $DestFilePath..."

    $Content = $SrcStringFile | Get-Content | ConvertFrom-Json
    $Content = TranslateStringsRecursively $Content
    $Content | ConvertTo-Json | Out-File -LiteralPath $DestFilePath -Encoding 'utf8';
}
