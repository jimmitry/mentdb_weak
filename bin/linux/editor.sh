#!/bin/bash
BASEDIR=$(dirname "$0")
cd "$BASEDIR"
cd "../../MentDB_Editor_3"
echo " "
echo "###################################################"
echo "# Start Mentalese Trigger                         #"
echo "###################################################"
echo "Wait please ..."
../jdk/linux_64/jdk1.8.0_181/bin/java -cp "core:lib/*" "-Dlog4j2.configurationFile=conf/log4j.xml" -Duser.language=en "-Dfile.encoding=UTF-8" -server re.jpayet.mentdb.editor.Mentalese_Trigger &
