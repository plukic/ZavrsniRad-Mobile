#$majorMinorVersionDevelop = %system.MajorMinorVersion.Develop%
# TeamCity's auto-incrementing build counter; ensures each build is unique
#$buildCounter = %build.counter% 
#$buildNumber = "${majorMinorVersionDevelop}.${buildCounter}"
#Write-Host "##teamcity[buildNumber '$buildNumber']"

$a = Get-Content 'versioning.json' -raw | ConvertFrom-Json
$a.versionCode=5
$a.versionName="2.1.1"
Write-Host $a
$a | ConvertTo-Json  | set-content 'versioning.json'