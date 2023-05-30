#!/bin/csh

# 1 : service name
# 2 : home
# 3 : agent id

set SERVICE = AGENT

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

if ( $SERVICE == "AppService" ) then
        set JAVA_OPTS = "-Xms1024m -Xmx1024m"
else if  ( $SERVICE == "NotiService" ) then
        set JAVA_OPTS = "-Xms1024m -Xmx1024m"
endif


#java $JAVA_OPTS $DEF_OPTS FX.MS $SERVICE &

java $JAVA_OPTS $DEF_OPTS ext.agent.test.RunHoleAgent 8120 125.7.128.42 8110 CON001 

