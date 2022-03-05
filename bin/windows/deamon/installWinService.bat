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

bin\windows\deamon\mentdb.exe //IS//%SERVICE_NAME% --DisplayName="%DISPLAY_NAME%" --LogPrefix="%PR_LOGPREFIX%" --LogPath="%PR_LOGPATH%" --StdOutput="%PR_STDOUTPUT%" --StdError="%PR_STDERROR%" --LogLevel="%PR_LOGLEVEL%" --Jvm="%PR_JVM%" --JvmMs="%PR_JVMMS%" --JvmMx="%PR_JVMMX%" --JvmSs="%PR_JVMSS%" --Install="%PR_INSTALL%" ++JvmOptions="%PR_JVMOPTIONS%" --StartMode="%PR_STARTMODE%" --StopMode="%PR_STOPMODE%" --Startup="%PR_STARTUP%" --StartClass="%PR_STARTCLASS%" --StopClass="%PR_STOPCLASS%" --StartPath="%cd%" --StopPath="%cd%" --Classpath="core;lib\Ab.jar;lib\JTattoo-1.6.11.jar;lib\accessors-smart-1.2.jar;lib\activation-1.1.jar;lib\annotations-2.0.1.jar;lib\artoolkitplus-2.3.1-1.3.jar;lib\asm-5.0.4.jar;lib\aspectjweaver-1.8.13.jar;lib\autocomplete-2.6.0.jar;lib\bcmail-jdk15on-158.jar;lib\bcpg-jdk15on-158.jar;lib\bcpkix-jdk15on-158.jar;lib\bcprov-ext-jdk15on-158.jar;lib\bcprov-jdk15on-158.jar;lib\bctls-jdk15on-158.jar;lib\c3p0-0.9.5.2.jar;lib\collections-generic-4.01.jar;lib\colt-1.2.0.jar;lib\com.fasterxml.jackson.annotations.jar;lib\com.fasterxml.jackson.databind.jar;lib\common-image-3.1.1.jar;lib\common-io-3.1.1.jar;lib\common-lang-3.1.1.jar;lib\commons-codec-1.11.jar;lib\commons-collections4-4.1.jar;lib\commons-compress-1.15.jar;lib\commons-exec-1.3.jar;lib\commons-fileupload-1.3.3.jar;lib\commons-io-2.4.jar;lib\commons-lang3-3.4.jar;lib\commons-logging-1.2.jar;lib\commons-math3-3.6.1.jar;lib\commons-net-3.3.jar;lib\commons-vfs2-2.0.jar;lib\concurrent-1.3.4.jar;lib\content-type-2.0.jar;lib\db2jcc4-jdbc-9.7.jar;lib\debugger-app-2.0.8.jar;lib\derby.jar;lib\derbyLocale_cs.jar;lib\derbyLocale_de_DE.jar;lib\derbyLocale_es.jar;lib\derbyLocale_fr.jar;lib\derbyLocale_hu.jar;lib\derbyLocale_it.jar;lib\derbyLocale_ja_JP.jar;lib\derbyLocale_ko_KR.jar;lib\derbyLocale_pl.jar;lib\derbyLocale_pt_BR.jar;lib\derbyLocale_ru.jar;lib\derbyLocale_zh_CN.jar;lib\derbyLocale_zh_TW.jar;lib\derbyclient.jar;lib\derbynet.jar;lib\derbyoptionaltools.jar;lib\derbyrun.jar;lib\derbytools.jar;lib\encog-core-3.4.jar;lib\fastutil-6.5.7.jar;lib\ffmpeg-3.2.1-1.3.jar;lib\findbugs-annotations-1.3.9-1.jar;lib\flandmark-1.07-1.3.jar;lib\flycapture-2.9.3.43-1.3.jar;lib\fontbox-2.0.7.jar;lib\fontbox-2.0.8.jar;lib\freemarker-2.3.23.jar;lib\ftp4j-1.7.2.jar;lib\graphics2d-0.7.jar;lib\gs-algo-1.3.jar;lib\gs-core-1.3.jar;lib\gs-ui-1.3.jar;lib\gson-2.2.3.jar;lib\guava-20.0.jar;lib\h2-1.4.200.jar;lib\hipster-core-1.0.1.jar;lib\hipster-extensions-1.0.1.jar;lib\hipster-third-party-graphs-1.0.1.jar;lib\hsqldb.jar;lib\j3d-core-1.3.1.jar;lib\jackson-0.9.1.jar;lib\jackson-annotations-2.10.1.jar;lib\jackson-core-2.10.1.jar;lib\jackson-databind-2.10.1.jar;lib\java-diff-utils-1.2.jar;lib\java-websocket-1.3.0.jar;lib\javacpp-1.3.3.jar;lib\javacv-1.3.3.jar;lib\javassist-3.19.0-GA.jar;lib\javax.el-3.0.0.jar;lib\javax.mail.jar;lib\javax.servlet-api-3.1.0.jar;lib\javax.servlet.jsp-2.3.2.jar;lib\javax.servlet.jsp-api-2.3.1.jar;lib\jaybird-full-3.0.3.jar;lib\jcifs-1.3.18.jar;lib\jcip-annotations-1.0-1.jar;lib\jetty-all-9.2.2.v20140723.jar;lib\jetty-jsp-jdt-2.3.3.jar;lib\jfreechart-1.5.0.jar;lib\jjwt-0.8.0.jar;lib\jline-2.12.1.jar;lib\joda-time-2.2.jar;lib\jsch-0.1.55.jar;lib\jscience-4.3.jar;lib\json-simple-1.1.1.jar;lib\json-smart-2.3.jar;lib\jsoup-1.11.3.jar;lib\jt400.jar;lib\junit-4.8.2.jar;lib\lang-tag-1.4.4.jar;lib\libdc1394-2.2.4-1.3.jar;lib\libfreenect-0.5.3-1.3.jar;lib\libfreenect2-0.2.0-1.3.jar;lib\librealsense-1.9.6-1.3.jar;lib\log4j-api-2.17.1.jar;lib\log4j-core-2.17.1.jar;lib\log4j-slf4j-impl-2.17.1.jar;lib\lombok-1.16.16.jar;lib\mongo-java-driver-3.12.7.jar;lib\msal4j-1.8.0.jar;lib\mysql-connector-java-8.0.21.jar;lib\neoitertools-1.0.0.jar;lib\nimbus-jose-jwt-8.14.1.jar;lib\oauth2-oidc-sdk-7.4.jar;lib\opencsv-3.5.jar;lib\openhtmltopdf-core-0.0.1-RC12.jar;lib\openhtmltopdf-java2d-0.0.1-RC12.jar;lib\openhtmltopdf-jsoup-dom-converter-0.0.1-RC12.jar;lib\openhtmltopdf-log4j-0.0.1-RC12.jar;lib\openhtmltopdf-pdfbox-0.0.1-RC12.jar;lib\openhtmltopdf-rtl-support-0.0.1-RC12.jar;lib\openhtmltopdf-slf4j-0.0.1-RC12.jar;lib\openhtmltopdf-svg-support-0.0.1-RC12.jar;lib\opennlp-brat-annotator-1.9.0.jar;lib\opennlp-morfologik-addon-1.9.0.jar;lib\opennlp-tools-1.9.0.jar;lib\opennlp-uima-1.9.0.jar;lib\oracle-jdbc6-11.2.0.4.jar;lib\org.eclipse.jgit-5.9.0.202009080501-r.jar;lib\org.eclipse.jgit.ssh.jsch-5.9.0.202009080501-r.jar;lib\pdfbox-2.0.8.jar;lib\pdfbox-app-2.0.8.jar;lib\pdfbox-debugger-2.0.8.jar;lib\pdfbox-tools-2.0.8.jar;lib\poi-3.17.jar;lib\poi-examples-3.17.jar;lib\poi-excelant-3.17.jar;lib\poi-ooxml-3.17.jar;lib\poi-ooxml-schemas-3.17.jar;lib\poi-scratchpad-3.17.jar;lib\postgresql-jdbc-9.3-1100.jar;lib\preflight-2.0.8.jar;lib\preflight-app-2.0.8.jar;lib\quartz-2.3.0-SNAPSHOT.jar;lib\quartz-jobs-2.3.0-SNAPSHOT.jar;lib\reflections-0.9.10.jar;lib\rsyntaxtextarea-2.6.1.jar;lib\slf4j-api-1.7.7.jar;lib\snakeyaml-1.12.jar;lib\sqlserver-jdbc4-4.0.2206.100.jar;lib\stax-api-1.0.1.jar;lib\stax2-api-3.1.4.jar;lib\stream-2.7.0.jar;lib\stripe-java-20.50.0.jar;lib\vecmath-1.3.1.jar;lib\videoinput-0.200-1.3.jar;lib\wstx-asl-3.2.6.jar;lib\xmlbeans-2.6.0.jar;lib\xmlbeans-xpath-2.6.0.jar;lib\xmpbox-2.0.8.jar;lib\xz-1.5.jar;lib_dl4j\*"
