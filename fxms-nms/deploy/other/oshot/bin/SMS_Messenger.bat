@ECHO OFF

title OShotSMS Client[오샷MMS 클라이언트]

echo ### OShotSMS Client Starting ... ...

rem 설치된 디렉토리 셋팅
set ALLSHOT_SMS_HOME=D:\OShot_MMS_Client

rem 사용하고자 하는 DB 만 주석(맨앞 rem 삭제)을 풀고 나머지는 주석 처리 한다.

rem ORACLE
rem set JDBC_DRIVERS=%ALLSHOT_SMS_HOME%\lib\jdbc\oracle\ojdbc14.jar

rem MSSQL
set JDBC_DRIVERS=%ALLSHOT_SMS_HOME%\lib\jdbc\mssql\sqljdbc4.jar

rem MYSQL
rem set JDBC_DRIVERS=%ALLSHOT_SMS_HOME%\lib\jdbc\mysql\mysql-connector-java-3.1.14-bin.jar

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


rem 여기는 그냥 그대로 나두면 됨.
set CLASSPATH=%ALLSHOT_SMS_HOME%\bin;%ALLSHOT_SMS_HOME%\lib\AllShotMMS_Client.jar;%JDBC_DRIVERS%

java com.allshot.sms.client.Messenger
