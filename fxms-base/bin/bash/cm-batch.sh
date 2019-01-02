#!/bin/bash

# 1 : service name
# 2 : home
# 3 : agent id

export SERVICE="CmService"
export FXMS_HOME="/home/miuser/cm-batch"
export AGENT_ID=$3

FXMS_LIBEXT=$FXMS_HOME/tmp/$SERVICE
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


#java $JAVA_OPTS $DEF_OPTS com.skb.adams.cm.CmServiceImpl
java $JAVA_OPTS $DEF_OPTS com.skb.adams.cm.CmServiceImpl >& /dev/null &

#java $JAVA_OPTS $DEF_OPTS FX.MS $SERVICE >& /dev/null &