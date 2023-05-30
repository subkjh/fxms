#!/bin/csh

echo "################################################################"

foreach PROG ( FX.MS ) 

	set pids = `ps -ef | grep $PROG | grep -v grep | grep -v tail | awk '{print $1,$2}' | awk '{print $2}'`

echo $pids

	if($#pids > 0) then
		echo "$PROG STOPPING ... ($pids)"
	    kill -9 $pids
	else
		echo "$PROG NOT RUNNING"
	endif

end

echo "################################################################"
