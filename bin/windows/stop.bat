@echo off
cd ..
cd ..

echo
"jdk/win_64/jdk1.8.0_181/bin/java.exe" -cp "core;lib\*" "-Dlog4j.configuration=file:conf/log4j.xml" "-Dfile.encoding=UTF-8" re.jpayet.mentdb.ext.server.Shutdown
