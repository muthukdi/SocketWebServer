#!/bin/bash

# Set the location of your JDK
JAVA_HOME='/home/ec2-user/jdk1.7.0_79'

# Start the H2 database server
$JAVA_HOME/bin/java -jar h2-1.4.191.jar -tool &

# Compile all application source files
for i in $(ls -d base/apps/*/); 
do
	$JAVA_HOME/bin/javac -sourcepath . -cp ${i}*.jar ${i}*.java
done

# Run the web server
if [ -z "$1" ]
then
	$JAVA_HOME/bin/java -cp h2-1.4.191.jar:. base.SocketWebServer >> logs/server.log &
else
	$JAVA_HOME/bin/java -cp h2-1.4.191.jar:. base.SocketWebServer
fi
