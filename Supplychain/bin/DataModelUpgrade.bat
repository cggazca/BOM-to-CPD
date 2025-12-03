@echo off
@REM *****************************************************************************
@REM Unpublished work. Copyright 2025 Siemens
@REM
@REM This material contains trade secrets or otherwise confidential information
@REM owned by Siemens Industry Software Inc. or its affiliates (collectively,
@REM "SISW"), or its licensors. Access to and use of this information is strictly
@REM limited as set forth in the Customer's applicable agreements with SISW.
@REM ******************************************************************************
@REM
@REM Script for executing Datamodel upgrade application
set curdir=%~dp0

call %curdir%..\..\..\bin\dms_env.bat

set _ccpid=SF
:ARGCHECK
if "%~1" == "-ccpid" (
    set _ccpid=%~2
    shift
)
shift
if not "%~1" == "" goto ARGCHECK

set COMMONS_CODEC=%SDD_HOME%\common\java\com\mentor\org\apache\commons\codec\commons-codec-1.17.1.jar
set COMMONS_LOGGING=%SDD_HOME%\common\java\com\mentor\org\apache\commons\logging\commons-logging-1.3.5.jar
set NASHORN=%SDD_HOME%\common\java\com\mentor\nashorn-core\*

set CLASSPATH=%curdir%..\java\*;%DBEDIR%\java\*;%DBEDIR%\java\DMSBrowser\plugins\*;%COMMONS_CODEC%;%COMMONS_LOGGING%;%NASHORN%
 
if "%_ccpid%" == "SF" (
    %JAVA_HOME%\bin\java ^
      -Xmx1024m ^
      -Dlog4j1.compatibility=true  ^
      -Dlog4j.configuration="file:/%DBEDIR%\java\config\dmsdesktop_log4j.properties" ^
      -cp "%CLASSPATH%" ^
      com.mentor.dms.contentprovider.sf.datamodel.upgrade.DatamodelUpgradeApp ^
      %*
) else (
    %JAVA_HOME%\bin\java ^
      -Xmx1024m ^
      -Dlog4j1.compatibility=true  ^
      -Dlog4j.configuration="file:/%curdir%..\config\log4j.properties" ^
      -cp "%CLASSPATH%" ^
      com.mentor.dms.contentprovider.utils.ValidateMappingApp ^
      %*
)