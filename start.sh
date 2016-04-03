#!/bin/bash

# Set the location of your JDK
JAVA_HOME='/home/ec2-user/jdk1.7.0_79'

# Compile all application source files and 
# set the class search path
CLASSPATH=.:h2-1.4.191.jar
for i in $(ls -d base/apps/*/); 
do
	$JAVA_HOME/bin/javac -cp ${i}*:. ${i}*.java
	CLASSPATH=$CLASSPATH:${i}*
done

# Run the web server with H2 and third-party dependencies
if [ -z "$1" ]
then
	$JAVA_HOME/bin/java -cp $CLASSPATH base.SocketWebServer >> logs/server.log &
else
	$JAVA_HOME/bin/java -cp $CLASSPATH base.SocketWebServer
fi
