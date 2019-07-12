#!/bin/csh

# 1 : service name
# 2 : home
# 3 : agent id

set SERVICE=AGENT

setenv AGENT_ID $3

set FXMS_LIBEXT = $FXMS_HOME/tmp/$SERVICE
if ( ! -e $FXMS_HOME/tmp ) then
	mkdir $FXMS_HOME/tmp
endif
if ( ! -e $FXMS_LIBEXT ) then
	mkdir $FXMS_LIBEXT
endif

rm -fr $FXMS_LIBEXT/*
cp -fr $FXMS_HOME/deploy/libext/* $FXMS_LIBEXT
cp -fr $FXMS_HOME/deploy/lib/* $FXMS_LIBEXT


source $FXMS_HOME/bin/setenv.sh

#java $JAVA_OPTS $DEF_OPTS FX.MS $SERVICE &

java $JAVA_OPTS $DEF_OPTS com.fxms.bio.agent.BioFxAgent 63801 125.7.128.42 63800 CON001 >& /dev/null &

