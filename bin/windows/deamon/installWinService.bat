cd ..
cd ..
cd ..
 
REM ########################################################
REM WARNING: Path to java installation
set PR_JVM=C:\Program Files\Java\jdk1.8.0_161\jre\bin\server\jvm.dll
REM ########################################################

set SERVICE_NAME=MentDB
set DISPLAY_NAME=MentDB Service
set PR_INSTALL=%cd%\bin\windows\deamon\mentdb.exe
 
REM Service log configuration
set PR_LOGPREFIX=%SERVICE_NAME%
set PR_LOGPATH=%cd%\bin\windows\deamon\logs
set PR_STDOUTPUT=%cd%\bin\windows\deamon\logs\stdout.txt
set PR_STDERROR=%cd%\bin\windows\deamon\logs\stderr.txt
set PR_LOGLEVEL=debug
 
REM Startup configuration
set PR_STARTUP=auto
set PR_STARTMODE=jvm
set PR_STARTCLASS=re.jpayet.mentdb.ext.server.Start
 
REM Shutdown configuration
set PR_STOPMODE=jvm
set PR_STOPCLASS=re.jpayet.mentdb.ext.server.Shutdown
 
REM JVM configuration
set PR_JVMMS=256
set PR_JVMMX=1024
set PR_JVMSS=4000
set PR_JVMOPTIONS=-Dlog4j.configuration=file:conf/log4j.xml;-Dfile.encoding=UTF-8

bin\windows\deamon\mentdb.exe //IS//%SERVICE_NAME% --DisplayName="%DISPLAY_NAME%" --LogPrefix="%PR_LOGPREFIX%" --LogPath="%PR_LOGPATH%" --StdOutput="%PR_STDOUTPUT%" --StdError="%PR_STDERROR%" --LogLevel="%PR_LOGLEVEL%" --Jvm="%PR_JVM%" --JvmMs="%PR_JVMMS%" --JvmMx="%PR_JVMMX%" --JvmSs="%PR_JVMSS%" --Install="%PR_INSTALL%" ++JvmOptions="%PR_JVMOPTIONS%" --StartMode="%PR_STARTMODE%" --StopMode="%PR_STOPMODE%" --Startup="%PR_STARTUP%" --StartClass="%PR_STARTCLASS%" --StopClass="%PR_STOPCLASS%" --StartPath="%cd%" --StopPath="%cd%" --Classpath="core;lib\*"
