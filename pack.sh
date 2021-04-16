#!/bin/zsh

echo "Main-Class: src.Main" > MainClass.txt

jar cmfv MainClass.txt Main.jar src/*.class