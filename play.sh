@echo off

HOME=.
JAVA_HOME=/opt/java???

export PATH=$JAVA_HOME:$PATH

echo java -version:
$JAVA_HOME/bin/java -version

set CLASSPATH=$HOME:$HOME/lib/controlsfx-9.0.0.jar:$HOME/lib/jlayer-1.0.1.jar$HOME/lib/rxtx-2.1.7.jar:$HOME/lib/slf4j-api-1.5.10.jar:$HOME/lib/slf4j-api-1.7.25.jar:$HOME/target/classes:$CLASSPATH

echo .
echo CLASSPATH
echo $CLASSPATH
echo .

$JAVA_HOME/bin/java.exe ch.bod.nfcMusic.MusicController dev

pause