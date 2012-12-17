#!/bin/sh
rm export/**/*.jar
ant create_run_jar
ant create_alex_ai_jar
ant create_richard_ai_jar
ant create_benni_ai_jar
ant create_test_ais_jar
cp lib/*AI*.jar export/lib/
