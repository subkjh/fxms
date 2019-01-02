#!/bin/bash

export FXMS_HOME="/home/miuser/cm-gw"

$FXMS_HOME/bin/cmgw-stop.sh

sleep 3

$FXMS_HOME/bin/cmgw-start.sh
