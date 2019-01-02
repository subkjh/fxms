#!/bin/bash

pids=`ps -ef | grep 'CmGwService' | grep -v grep | grep -v tail | awk '{print $1,$2}' | awk '{print $2}'`

for pid in $pids
do
	kill $pid
done

pids=`ps -ef | grep 'CmGwService' | grep -v grep | grep -v tail | awk '{print $1,$2}' | awk '{print $2}'`

for pid in $pids
do
	echo $pid
done
