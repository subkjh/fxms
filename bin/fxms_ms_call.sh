#!/bin/csh

# 1 : service name
# 2 : home
# 3 : agent id

set SERVICE = $1

source $FXMS_HOME/bin/setenv.sh

java $JAVA_OPTS $DEF_OPTS FX.MsCall localhost 63804 $*


