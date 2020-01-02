@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  api startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and API_OPTS to pass JVM options to this script.
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

set CLASSPATH=%APP_HOME%\lib\api-1.0.jar;%APP_HOME%\lib\biz-1.0.jar;%APP_HOME%\lib\spring-boot-starter-web-2.1.2.RELEASE.jar;%APP_HOME%\lib\pay-wx-1.24.jar;%APP_HOME%\lib\dal-1.0.jar;%APP_HOME%\lib\spring-boot-starter-aop-2.1.2.RELEASE.jar;%APP_HOME%\lib\spring-boot-starter-data-redis-2.1.2.RELEASE.jar;%APP_HOME%\lib\java-jwt-3.7.0.jar;%APP_HOME%\lib\oss-1.22.jar;%APP_HOME%\lib\qiniu-java-sdk-7.2.11.jar;%APP_HOME%\lib\qcloudsms-1.0.6.jar;%APP_HOME%\lib\spring-boot-starter-json-2.1.2.RELEASE.jar;%APP_HOME%\lib\spring-boot-starter-security-2.1.2.RELEASE.jar;%APP_HOME%\lib\mybatis-plus-boot-starter-3.1.0.jar;%APP_HOME%\lib\spring-boot-starter-jdbc-2.1.2.RELEASE.jar;%APP_HOME%\lib\spring-boot-starter-2.1.2.RELEASE.jar;%APP_HOME%\lib\spring-boot-starter-tomcat-2.1.2.RELEASE.jar;%APP_HOME%\lib\hibernate-validator-6.0.14.Final.jar;%APP_HOME%\lib\spring-webmvc-5.1.4.RELEASE.jar;%APP_HOME%\lib\spring-security-web-5.1.3.RELEASE.jar;%APP_HOME%\lib\spring-web-5.1.4.RELEASE.jar;%APP_HOME%\lib\pay-common-1.24.jar;%APP_HOME%\lib\guava-27.0.1-jre.jar;%APP_HOME%\lib\pinyin4j-2.5.1.jar;%APP_HOME%\lib\cos_api-5.2.4.jar;%APP_HOME%\lib\jackson-datatype-jdk8-2.9.8.jar;%APP_HOME%\lib\jackson-datatype-jsr310-2.9.8.jar;%APP_HOME%\lib\jackson-module-parameter-names-2.9.8.jar;%APP_HOME%\lib\jackson-databind-2.9.8.jar;%APP_HOME%\lib\jackson-annotations-2.9.0.jar;%APP_HOME%\lib\bcprov-jdk15on-1.55.jar;%APP_HOME%\lib\mysql-connector-java-8.0.13.jar;%APP_HOME%\lib\druid-spring-boot-starter-1.1.10.jar;%APP_HOME%\lib\spring-data-redis-2.1.4.RELEASE.jar;%APP_HOME%\lib\spring-boot-autoconfigure-2.1.2.RELEASE.jar;%APP_HOME%\lib\spring-boot-2.1.2.RELEASE.jar;%APP_HOME%\lib\spring-security-config-5.1.3.RELEASE.jar;%APP_HOME%\lib\spring-data-keyvalue-2.1.4.RELEASE.jar;%APP_HOME%\lib\spring-context-support-5.1.4.RELEASE.jar;%APP_HOME%\lib\spring-security-core-5.1.3.RELEASE.jar;%APP_HOME%\lib\spring-context-5.1.4.RELEASE.jar;%APP_HOME%\lib\spring-aop-5.1.4.RELEASE.jar;%APP_HOME%\lib\aspectjweaver-1.9.2.jar;%APP_HOME%\lib\lettuce-core-5.1.3.RELEASE.jar;%APP_HOME%\lib\aliyun-sdk-oss-2.8.3.jar;%APP_HOME%\lib\httpmime-4.5.6.jar;%APP_HOME%\lib\httpclient-4.5.6.jar;%APP_HOME%\lib\commons-codec-1.11.jar;%APP_HOME%\lib\json-20180130.jar;%APP_HOME%\lib\qcloud-java-sdk-2.0.6.jar;%APP_HOME%\lib\okhttp-3.9.1.jar;%APP_HOME%\lib\gson-2.8.5.jar;%APP_HOME%\lib\happy-dns-java-0.1.6.jar;%APP_HOME%\lib\spring-boot-starter-logging-2.1.2.RELEASE.jar;%APP_HOME%\lib\javax.annotation-api-1.3.2.jar;%APP_HOME%\lib\spring-oxm-5.1.4.RELEASE.jar;%APP_HOME%\lib\spring-jdbc-5.1.4.RELEASE.jar;%APP_HOME%\lib\spring-data-commons-2.1.4.RELEASE.jar;%APP_HOME%\lib\spring-tx-5.1.4.RELEASE.jar;%APP_HOME%\lib\spring-beans-5.1.4.RELEASE.jar;%APP_HOME%\lib\spring-expression-5.1.4.RELEASE.jar;%APP_HOME%\lib\spring-core-5.1.4.RELEASE.jar;%APP_HOME%\lib\snakeyaml-1.23.jar;%APP_HOME%\lib\tomcat-embed-websocket-9.0.14.jar;%APP_HOME%\lib\tomcat-embed-core-9.0.14.jar;%APP_HOME%\lib\tomcat-embed-el-9.0.14.jar;%APP_HOME%\lib\validation-api-2.0.1.Final.jar;%APP_HOME%\lib\jboss-logging-3.3.2.Final.jar;%APP_HOME%\lib\classmate-1.4.0.jar;%APP_HOME%\lib\fastjson-1.2.41.jar;%APP_HOME%\lib\core-3.3.1.jar;%APP_HOME%\lib\mybatis-plus-3.1.0.jar;%APP_HOME%\lib\failureaccess-1.0.1.jar;%APP_HOME%\lib\listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar;%APP_HOME%\lib\jsr305-3.0.2.jar;%APP_HOME%\lib\checker-qual-2.5.2.jar;%APP_HOME%\lib\error_prone_annotations-2.2.0.jar;%APP_HOME%\lib\j2objc-annotations-1.1.jar;%APP_HOME%\lib\animal-sniffer-annotations-1.17.jar;%APP_HOME%\lib\jackson-core-2.9.8.jar;%APP_HOME%\lib\druid-1.1.10.jar;%APP_HOME%\lib\logback-classic-1.2.3.jar;%APP_HOME%\lib\log4j-to-slf4j-2.11.1.jar;%APP_HOME%\lib\jul-to-slf4j-1.7.25.jar;%APP_HOME%\lib\HikariCP-3.2.0.jar;%APP_HOME%\lib\slf4j-api-1.7.25.jar;%APP_HOME%\lib\reactor-core-3.2.5.RELEASE.jar;%APP_HOME%\lib\netty-handler-4.1.31.Final.jar;%APP_HOME%\lib\netty-codec-4.1.31.Final.jar;%APP_HOME%\lib\netty-transport-4.1.31.Final.jar;%APP_HOME%\lib\netty-buffer-4.1.31.Final.jar;%APP_HOME%\lib\netty-resolver-4.1.31.Final.jar;%APP_HOME%\lib\netty-common-4.1.31.Final.jar;%APP_HOME%\lib\jdom-1.1.jar;%APP_HOME%\lib\joda-time-2.10.1.jar;%APP_HOME%\lib\okio-1.13.0.jar;%APP_HOME%\lib\httpcore-4.4.10.jar;%APP_HOME%\lib\spring-jcl-5.1.4.RELEASE.jar;%APP_HOME%\lib\mybatis-plus-extension-3.1.0.jar;%APP_HOME%\lib\reactive-streams-1.0.2.jar;%APP_HOME%\lib\logback-core-1.2.3.jar;%APP_HOME%\lib\log4j-api-2.11.1.jar;%APP_HOME%\lib\mybatis-plus-core-3.1.0.jar;%APP_HOME%\lib\mybatis-spring-2.0.0.jar;%APP_HOME%\lib\mybatis-plus-annotation-3.1.0.jar;%APP_HOME%\lib\mybatis-3.5.0.jar;%APP_HOME%\lib\jsqlparser-1.4.jar

@rem Execute api
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %API_OPTS%  -classpath "%CLASSPATH%" com.byy.api.Application %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable API_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%API_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
