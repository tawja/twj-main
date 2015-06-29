#!/bin/sh

# Resolve links - $0 may be a soft-link
PRG="$0"

while [ -h "$PRG" ] ; do
    ls=`ls -ld "$PRG"`
    link=`expr "$ls" : '.*-> \(.*\)$'`
    if expr "$link" : '/.*' > /dev/null; then
        PRG="$link"
    else
        PRG=`dirname "$PRG"`/"$link"
    fi
done

DIRNAME=`dirname "$PRG"`
DIRNAME="`( cd \"$DIRNAME\" && pwd )`"

TAWJA_HOME=$DIRNAME
BSH_HOME="$TAWJA_HOME"/utils/beanshell
#GROOVY_HOME="$TAWJA_HOME"/utils/groovy

#PATH="$GROOVY_HOME"/bin,"$TAWJA_HOME"/bin;$PATH

CLASSPATH=""$BSH_HOME"/bsh;"$BSH_HOME"/bsh.jar"

# For Cygwin, ensure paths are in UNIX format before anything is touched
if $cygwin ; then
  [ -n "$JAVA_HOME" ] &&
    JAVA_HOME=`cygpath --unix "$JAVA_HOME"`
  [ -n "$CLASSPATH" ] &&
    CLASSPATH=`cygpath --path --unix "$CLASSPATH"`
fi

echo ------------------------------------------
echo --- Welcome to Tawja Development Shell ---
echo ------------------------------------------

echo Environment Information : 
echo      - Working Directroy = $DIRNAME
echo      - env.JAVA_HOME     = $JAVA_HOME
echo      - env.TAWJA_HOME    = $TAWJA_HOME
echo      - env.BSH_HOME      = $BSH_HOME
#echo      - env.GROOVY_HOME   = $GROOVY_HOME
echo ""
echo      - env.PATH          = $PATH
echo ""
echo ------------------------------------------

# For Cygwin, switch paths to Windows format before running java
if $cygwin; then
  [ -n "$JAVA_HOME" ] &&
    JAVA_HOME=`cygpath --path --windows "$JAVA_HOME"`
  [ -n "$CLASSPATH" ] &&
    CLASSPATH=`cygpath --path --windows "$CLASSPATH"`
fi

#groovysh.bat
"$JAVA_HOME"/bin/java -classpath ""$CLASSPATH"" bsh.Interpreter "$@"
