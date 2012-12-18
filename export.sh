#!/bin/sh
export_dir="export"
zipname="operation_muehle"
export_ais="Benni Richard  Alex"


rm $export_dir/**/*.jar
rm "$zipname.zip"

ant create_run_jar
ant create_alex_ai_jar
ant create_richard_ai_jar
ant create_benni_ai_jar
ant create_test_ais_jar

#cp lib/*AI*.jar "$export_dir/lib/"

for ai in $export_ais; do
  cp "lib/${ai}AI.jar" "$export_dir/lib/"
done

cp README.md LICENSE "$export_dir/"

mv "$export_dir" "$zipname"
zip "$zipname.zip" $zipname/**/* $zipname/*
mv "$zipname" "$export_dir"
