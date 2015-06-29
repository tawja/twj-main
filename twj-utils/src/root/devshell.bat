@echo off

@rem Determine what directory it is in.
set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.\

set TAWJA_HOME=%DIRNAME%
set BSH_HOME=%TAWJA_HOME%utils\beanshell\
rem set GROOVY_HOME=%TAWJA_HOME%utils\groovy\

rem set PATH=%GROOVY_HOME%bin;%TAWJA_HOME%bin;%PATH%


echo ------------------------------------------
echo --- Welcome to Tawja Development Shell ---
echo ------------------------------------------

echo Environment Information : 
echo      - Working Directroy = %DIRNAME%
echo      - env.JAVA_HOME     = %JAVA_HOME%
echo      - env.TAWJA_HOME    = %TAWJA_HOME%
echo      - env.BSH_HOME      = %BSH_HOME%
rem echo      - env.GROOVY_HOME   = %GROOVY_HOME%
echo.
echo      - env.PATH          = %PATH%
echo.
echo ------------------------------------------


rem call groovysh.bat
call java -classpath "%BSH_HOME%bsh;%BSH_HOME%\bsh.jar" bsh.Interpreter %*

pause
