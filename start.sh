#!/bin/bash

# Set the location of your JDK
JAVA_HOME='/home/ec2-user/jdk1.7.0_79'

# Compile all application source files
for i in $(ls -d base/apps/*/); 
do
	$JAVA_HOME/bin/javac -sourcepath . -cp ${i}*.jar ${i}*.java
done

# Run the web server
if [ -z "$1" ]
then
	$JAVA_HOME/bin/java base.SocketWebServer &
else
	$JAVA_HOME/bin/java base.SocketWebServer
fi
