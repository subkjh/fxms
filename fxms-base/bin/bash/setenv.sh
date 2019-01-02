#!/bin/csh

#
# define jre home
#

#
# define java options
#

JAVA_OPTS="-Xms1024m -Xmx1024m"
JAVA_OPTS="-Djava.rmi.server.hostname=localhost"
JAVA_OPTS=  
GC_OPTS="-server -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+UseParNewGC"
DEF_OPTS="-Djava.system.class.loader=com.daims.ems.EmsClassLoader"
DEF_OPTS="-Djava.security.egd=file:///dev/urandom"
DEF_OPTS=

FXMS_BIN=$FXMS_HOME/bin
FXMS_LIB=$FXMS_HOME/deploy/lib

#
# define class path
#

CLASSPATH="./:"$FXMS_HOME
JARS_EXT=`ls $FXMS_LIB/*.jar`

for jar in $JARS_EXT
do
	CLASSPATH=$CLASSPATH":"$jar
done


#
# define class path extended jar
#

FOLDER_EXT=`ls -d $FXMS_LIBEXT/*/`
for folder in $FOLDER_EXT 
do
	JARS_EXT=`ls $folder/*.jar`
	for jar in $JARS_EXT
	do
		CLASSPATH=$CLASSPATH":"$jar
	done
done

JARS_EXT=`ls $FXMS_LIBEXT/*.jar`
for jar in $JARS_EXT
do
	CLASSPATH=$CLASSPATH":"$jar
done

#
# define class path extended jar
#

export CLASSPATH=$CLASSPATH

#
# change directory to fxms home
#

cd $FXMS_HOME

#echo $CLASSPATH
