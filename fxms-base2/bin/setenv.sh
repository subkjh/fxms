#!/bin/csh

#
# define jre home
#

#
# define java options
#

set JAVA_OPTS = "-Xms1024m -Xmx1024m"
set JAVA_OPTS = "-Djava.rmi.server.hostname=localhost"
set JAVA_OPTS =  
set GC_OPTS = "-server -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+UseParNewGC"
set DEF_OPTS = -Djava.system.class.loader=com.daims.ems.EmsClassLoader
set DEF_OPTS = "-Djava.security.egd=file:///dev/urandom"
set DEF_OPTS =

if ( $?JAVA_HOME ) then
        set path = ( $JAVA_HOME/bin $path )
endif

#
# define nprism home
#

set FXMS_BIN = $FXMS_HOME/bin
set FXMS_LIB = $FXMS_HOME/deploy/lib

if ( ! $?FXMS_LIBEXT ) then
	set FXMS_LIBEXT = $FXMS_HOME/deploy/libext 
endif


#
# define class path
#

set CLASSPATH = "./:"$FXMS_HOME

set JARS_EXT = `ls $FXMS_LIB/*.jar`
foreach jar ( $JARS_EXT )
	set CLASSPATH = $CLASSPATH":"$jar
end


#
# define class path extended jar
#

set FOLDER_EXT = `ls -d $FXMS_LIBEXT/*/`
foreach folder ( $FOLDER_EXT ) 
	set JARS_EXT = `ls $folder/*.jar`
	foreach jar ( $JARS_EXT )
		set CLASSPATH = $CLASSPATH":"$jar
	end
end

set JARS_EXT = `ls $FXMS_LIBEXT/*.jar`
foreach jar ( $JARS_EXT )
        set CLASSPATH = $CLASSPATH":"$jar
end

#
# define class path extended jar
#

setenv CLASSPATH $CLASSPATH

#
# change directory to fxms home
#

cd $FXMS_HOME

#echo $CLASSPATH
