#!/bin/zsh

echo "Compiling source code..."
javac src/*.java
echo "Compiled successfully!"

echo "Simulating the result..."
java src/Main $1 $2
echo "Finished!"