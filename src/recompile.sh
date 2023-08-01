#!/bin/bash
#chmod u+x recompile.sh

#javac doesnt always capture all changes
#full delete and recompile solves this
cd "$(dirname "$0")"
chmod u+x run.sh
clear
cp -rTu "res" "../bin/res"
find "../bin" -type f -name "*.class" -delete
javac -d "../bin" Game.java