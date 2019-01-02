#!/bin/csh

#
#
#

source $FXMS_HOME/bin/setenv.sh

java $JAVA_OPTS $DEF_OPTS fxms.bas.fxo.service.FxServiceUtil -port $PORT_RMI --status -service $1 

