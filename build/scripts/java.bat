@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  java startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Add default JVM options here. You can also use JAVA_OPTS and JAVA_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\java.jar;%APP_HOME%\lib\javax.inject-1.jar;%APP_HOME%\lib\value-2.8.8.jar;%APP_HOME%\lib\gson-2.8.8.jar;%APP_HOME%\lib\org.eclipse.paho.client.mqttv3-1.2.4.jar;%APP_HOME%\lib\joda-time-2.10.6.jar;%APP_HOME%\lib\iot-2.13.24.jar;%APP_HOME%\lib\sts-2.13.24.jar;%APP_HOME%\lib\gson-2.8.5.jar;%APP_HOME%\lib\aws-json-protocol-2.13.24.jar;%APP_HOME%\lib\aws-query-protocol-2.13.24.jar;%APP_HOME%\lib\protocol-core-2.13.24.jar;%APP_HOME%\lib\aws-core-2.13.24.jar;%APP_HOME%\lib\auth-2.13.24.jar;%APP_HOME%\lib\regions-2.13.24.jar;%APP_HOME%\lib\sdk-core-2.13.24.jar;%APP_HOME%\lib\apache-client-2.13.24.jar;%APP_HOME%\lib\netty-nio-client-2.13.24.jar;%APP_HOME%\lib\http-client-spi-2.13.24.jar;%APP_HOME%\lib\profiles-2.13.24.jar;%APP_HOME%\lib\utils-2.13.24.jar;%APP_HOME%\lib\annotations-2.13.24.jar;%APP_HOME%\lib\jackson-databind-2.10.4.jar;%APP_HOME%\lib\jackson-core-2.10.4.jar;%APP_HOME%\lib\slf4j-api-1.7.28.jar;%APP_HOME%\lib\netty-reactive-streams-http-2.0.4.jar;%APP_HOME%\lib\netty-reactive-streams-2.0.4.jar;%APP_HOME%\lib\reactive-streams-1.0.3.jar;%APP_HOME%\lib\eventstream-1.0.1.jar;%APP_HOME%\lib\jackson-annotations-2.10.4.jar;%APP_HOME%\lib\httpclient-4.5.9.jar;%APP_HOME%\lib\httpcore-4.4.11.jar;%APP_HOME%\lib\netty-codec-http2-4.1.46.Final.jar;%APP_HOME%\lib\netty-codec-http-4.1.46.Final.jar;%APP_HOME%\lib\netty-handler-4.1.46.Final.jar;%APP_HOME%\lib\netty-codec-4.1.46.Final.jar;%APP_HOME%\lib\netty-transport-native-epoll-4.1.46.Final-linux-x86_64.jar;%APP_HOME%\lib\netty-transport-native-unix-common-4.1.46.Final.jar;%APP_HOME%\lib\netty-transport-4.1.46.Final.jar;%APP_HOME%\lib\netty-buffer-4.1.46.Final.jar;%APP_HOME%\lib\netty-resolver-4.1.46.Final.jar;%APP_HOME%\lib\netty-common-4.1.46.Final.jar;%APP_HOME%\lib\commons-logging-1.2.jar;%APP_HOME%\lib\commons-codec-1.11.jar

@rem Execute java
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %JAVA_OPTS%  -classpath "%CLASSPATH%" not-applicable %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable JAVA_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%JAVA_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
