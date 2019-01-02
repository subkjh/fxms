#!/bin/bash

# 1 : service name
# 2 : home
# 3 : agent id

export SERVICE="Broker"
export FXMS_HOME="/home/miuser/cm-gw"

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

#java $JAVA_OPTS $DEF_OPTS subkjh.bas.net.broker.BrokerServer $*
java $JAVA_OPTS $DEF_OPTS subkjh.bas.net.broker.BrokerServer $* >& /dev/null &

