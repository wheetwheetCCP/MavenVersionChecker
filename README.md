# MavenVersionChecker
Just a quick Java app/script to help keep track of versions that a project is using.

Run this from the top level directory of a project and it will recurse downwards to find all the pom files it can, and then create a csv file with a list of the artifactIds and the versions of those artifacts. It will also make a column for "latest version".

Possible upgrades:
* Take in a path of a directory
* Web scrape Maven repo for latest version
* Allow naming of the csv
