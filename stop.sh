#!/bin/bash

# Stop the web server
PID=`ps -ef | grep SocketWebServer | grep -v grep | awk '{print $2}'`
if [[ "" !=  "$PID" ]]; 
then
  echo "killing $PID"
  kill -9 $PID
fi

# Stop the H2 database
PID=`ps -ef | grep h2-1.4.191.jar.*tool | grep -v grep | awk '{print $2}'`
if [[ "" !=  "$PID" ]]; 
then
  echo "killing $PID"
  kill -9 $PID
fi