#!/bin/bash

# 1 : service name
# 2 : home
# 3 : agent id

PORT_RMI=8804
export FXMS_HOME="/home/miuser/cm-gw"

FXMS_LIBEXT=$FXMS_HOME/tmp/call
if [ ! -d $FXMS_HOME/tmp ]; then
	mkdir $FXMS_HOME/tmp
fi

if [ ! -d $FXMS_LIBEXT ]; then
	mkdir $FXMS_LIBEXT
fi

rm -fr $FXMS_LIBEXT/*
cp -fr $FXMS_HOME/deploy/libext/* $FXMS_LIBEXT
cp -fr $FXMS_HOME/deploy/lib/* $FXMS_LIBEXT

source $FXMS_HOME/bin/setenv.sh

pids=`ps -ef | grep 'CmGwService' | grep -v grep | grep -v tail | awk '{print $1,$2}' | awk '{print $2}'`

echo ""
echo ""
echo "PID=$pids"
echo "================================================================="
echo ""
echo ""

java $JAVA_OPTS $DEF_OPTS fxms.bas.fxo.service.FxServiceUtil -port $PORT_RMI --call -service CmGwService getStatus debug
