#!/bin/csh

# 1 : service name
# 2 : home
# 3 : agent id

set SERVICE = $1

setenv FXMS_HOME /home/miuser/cm-batch

source $FXMS_HOME/bin/setenv.sh

java $JAVA_OPTS $DEF_OPTS fxms.bas.fxo.service.FxServiceUtil -port $PORT_RMI --call -service CmService $*

