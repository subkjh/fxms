#!/bin/csh

# 1 : service name
# 2 : home
# 3 : agent id

set SERVICE = $1

if ( $SERVICE == "" ) then
	set SERVICE = "ms"
endif

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
else if  ( $SERVICE == "MoService" ) then
        set JAVA_OPTS = "-Xms1024m -Xmx1024m"
endif


#java $JAVA_OPTS $DEF_OPTS FX.MS $* &

java $JAVA_OPTS $DEF_OPTS FX.MS $* >& /dev/null &
