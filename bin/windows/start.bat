@echo off
cd ..
cd ..

echo
"jdk/win_64/jdk1.8.0_181/bin/java.exe" -cp "core;lib\*;lib_dl4j\*" "-Dorg.eclipse.jetty.server.Request.maxFormContentSize=500000000" "-Dlog4j.configuration=file:conf/log4j.xml" "-Dfile.encoding=UTF-8" re.jpayet.mentdb.ext.server.Start
pause