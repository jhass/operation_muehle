#!/bin/sh

if [ ! -e "$(which java)" ]; then
  echo "Java not found" 2>&1
  exit 1
fi

if [ ! -e "operation_muehle.jar" ]; then
  echo "Main jar not found" 2>&1
  exit 2
fi

if [ ! -d "lib" ]; then
  echo "Warning, ./lib/ is not a directory, no AIs will be available" 2>&1
fi

java -Dsun.java2d.opengl=True -jar ./operation_muehle.jar
