@ECHO OFF

title AllShotSMS Client NT-Service Install

echo ### AllShotSMS Client Install ... ...

rem 설치된 디렉토리 셋팅
set ALLSHOT_SMS_HOME=D:\OShot_MMS_Client

rem 서비스에 등록할 이름 셋팅
set SERVICE_NAME="OShotMMS Client"

rem 서비스에 등록할 설명 셋팅
set SERVICE_DESC="OShotMMS Client NT Service"

rem JVM이 설치된 경로 셋팅
set JVM_FILE="C:\Program Files\Java\jre1.5.0_18\bin\client\jvm.dll"

rem JDBC Driver 셋팅
rem 사용하고자 하는 DB 만 주석(맨앞 rem 삭제)을 풀고 나머지는 주석 처리 한다.

rem ORACLE
rem set JDBC_DRIVERS=%ALLSHOT_SMS_HOME%\lib\jdbc\oracle\ojdbc14.jar

rem MSSQL
rem set JDBC_DRIVERS=%ALLSHOT_SMS_HOME%\lib\jdbc\mssql\sqljdbc.jar

rem MYSQL
set JDBC_DRIVERS=%ALLSHOT_SMS_HOME%\lib\jdbc\mysql\mysql-connector-java-3.1.14-bin.jar

rem ALTIBASE
rem set JDBC_DRIVERS=%ALLSHOT_SMS_HOME%\lib\jdbc\altibase\Altibase.jar

rem INFORMIX
rem set JDBC_DRIVERS=
rem set JDBC_DRIVERS=%JDBC_DRIVERS%;%ALLSHOT_SMS_HOME%\lib\jdbc\informix\ifxjdbc.jar
rem set JDBC_DRIVERS=%JDBC_DRIVERS%;%ALLSHOT_SMS_HOME%\lib\jdbc\informix\ifxjdbcx.jar
rem set JDBC_DRIVERS=%JDBC_DRIVERS%;%ALLSHOT_SMS_HOME%\lib\jdbc\informix\ifxlang.jar
rem set JDBC_DRIVERS=%JDBC_DRIVERS%;%ALLSHOT_SMS_HOME%\lib\jdbc\informix\ifxlsupp.jar
rem set JDBC_DRIVERS=%JDBC_DRIVERS%;%ALLSHOT_SMS_HOME%\lib\jdbc\informix\ifxsqlj.jar
rem set JDBC_DRIVERS=%JDBC_DRIVERS%;%ALLSHOT_SMS_HOME%\lib\jdbc\informix\ifxtools.jar

rem 이하 그대로 셋팅.
set CLASSPATH=%ALLSHOT_SMS_HOME%\bin;%ALLSHOT_SMS_HOME%\lib\AllShotSMS_Client.jar;%JDBC_DRIVERS%

JavaService -install %SERVICE_NAME% %JVM_FILE% -Djava.class.path=%CLASSPATH% -start com.allshot.sms.client.Messenger -description %SERVICE_DESC%

@PAUSE
