#!/bin/bash

# Set the location of Java home
JAVA_HOME='/home/ec2-user/jdk1.7.0_79'

# Compile web server source files
$JAVA_HOME/bin/javac base/SocketWebServer.java

# Run the web server
$JAVA_HOME/bin/java base.SocketWebServer
