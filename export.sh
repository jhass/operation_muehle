#!/bin/sh
rm export/**/*.jar
ant create_run_jar
ant create_alex_ai_jar
cp lib/AlexAI.jar export/lib/
