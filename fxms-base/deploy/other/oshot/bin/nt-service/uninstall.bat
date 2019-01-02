@ECHO OFF

set SERVICE_NAME="OShotMMS Client"

JavaService -uninstall %SERVICE_NAME%

@PAUSE
