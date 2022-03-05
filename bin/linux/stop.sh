#!/bin/bash
BASEDIR=$(dirname "$0")
cd "$BASEDIR"
cd "../.."
echo " "
jdk/linux_64/jdk1.8.0_181/bin/java -server -cp "core:lib/*" "-Dlog4j2.configurationFile=conf/log4j.xml" "-Dfile.encoding=UTF-8" -server re.jpayet.mentdb.ext.server.Shutdown
