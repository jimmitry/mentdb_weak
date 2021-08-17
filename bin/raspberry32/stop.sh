#!/bin/bash
BASEDIR=$(dirname "$0")
cd "$BASEDIR"
cd "../.."
echo " "
jdk/raspberry_32/jdk1.8.0_251/bin/java -server -cp "core:lib/*" "-Dlog4j.configuration=file:conf/log4j.xml" "-Dfile.encoding=UTF-8" -server re.jpayet.mentdb.ext.server.Shutdown
