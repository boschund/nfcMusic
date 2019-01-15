@echo off

HOME=/
JAVA_HOME=/opt/java

export PATH=$PATH:$JAVA_HOME

set JTATTOO=$HOME/lib/JTattoo-1.6.11.jar
set JLAYER=$HOME/lib/jlayer-1.0.1.jar
set NFC_TOOL=$HOME/lib/nfctools-api-1.0.M8.jar:$HOME/lib/nfctools-core-1.0.M8.jar:$HOME/lib/nfctools-ndef-1.0.M8.jar
set SLF=$HOME/lib/slf4j-api-1.7.25.jar

echo java -version:
$JAVA_HOME/bin/java -version

set CLASSPATH=$CLASSPATH:$JTATTOO:$JLAYER:$NFC_TOOL:$SLF:$HOME/target/classes

echo .
echo CLASSPATH:
echo $CLASSPATH
echo .

$JAVA_HOME/bin/java.exe ch.bod.nfcMusic.MusicController

pause