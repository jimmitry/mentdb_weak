#!/bin/bash
BASEDIR=$(dirname "$0")
cd $BASEDIR
cd "../.."
echo " "
echo "###################################################"
echo "# Start Mentalese Trigger                         #"
echo "###################################################"
echo "Wait please ..."
../jdk/osx_64/jdk1.8.0_181.jdk/Contents/Home/bin/java -cp "core:lib/*" "-Dlog4j.configuration=file:conf/log4j.xml" -Duser.language=en "-Dfile.encoding=UTF-8" -server re.jpayet.mentdb.editor.Mentalese_Trigger &
