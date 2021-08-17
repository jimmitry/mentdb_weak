@echo off
cd ..
cd ..
cd MentDB_Editor_3
echo
echo ###################################################
echo # Start MentDB Trigger .......................... #
echo ###################################################
echo Wait please ...
"../jdk/win_64/jdk1.8.0_181/bin/java.exe" -cp "core;lib\*" "-Dlog4j.configuration=file:conf/log4j.xml" "-Duser.language=en" "-Dfile.encoding=UTF-8" re.jpayet.mentdb.editor.Mentalese_Trigger
