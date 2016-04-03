#!/bin/bash

# Stop the web server
PID=`ps -ef | grep SocketWebServer | grep -v grep | awk '{print $2}'`
if [[ "" !=  "$PID" ]]; 
then
  echo "killing $PID"
  kill -9 $PID
fi