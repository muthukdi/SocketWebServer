@echo off

Setlocal EnableDelayedExpansion

:: Set the location of your JDK
set JAVA_HOME="C:\Program Files (x86)\Java\jdk1.7.0_79"

:: Compile all application source files
for /d %%i in (base\apps\*) do %JAVA_HOME%\bin\javac -cp %%i\*;. %%i\*.java

:: Set the class search path
set CLASSPATH=.;h2-1.4.191.jar
for /d %%i in (base\apps\*) do set CLASSPATH=!CLASSPATH!;%%i\*

:: Run the web server with H2 and third-party dependencies
%JAVA_HOME%\bin\java -cp %CLASSPATH% base.SocketWebServer