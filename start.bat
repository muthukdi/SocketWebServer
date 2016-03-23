@echo off

:: Set the location of your JDK
set JAVA_HOME="C:\Program Files (x86)\Java\jdk1.7.0_75"

:: Compile all application source files
for /d %%i in (base\apps\*) do %JAVA_HOME%\bin\javac -sourcepath . -cp %%i\*.jar %%i\*.java

:: Run the web server
%JAVA_HOME%\bin\java base.SocketWebServer