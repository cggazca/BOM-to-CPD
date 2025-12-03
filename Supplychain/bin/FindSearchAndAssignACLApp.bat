@echo off
setlocal

echo.
echo Searching for SearchAndAssignACLApp in all DMS jars...
echo.

rem Load the EDM/DMS environment to get SDD_HOME and DBEDIR
set curdir=%~dp0
call "%curdir%..\..\..\bin\dms_env.bat"

if "%SDD_HOME%"=="" (
    echo ERROR: SDD_HOME is not set. Check your environment.
    exit /b 1
)

set ROOT=%SDD_HOME%\dms

echo Using SDD_HOME: %SDD_HOME%
echo Scanning under: "%ROOT%"
echo.

set FOUND=0

pushd "%ROOT%"
for /r %%J in (*.jar) do (
    rem Search the binary jar for the class name text
    findstr /m /c:"SearchAndAssignACLApp" "%%J" >nul 2>&1
    if not errorlevel 1 (
        echo ----------------------------------------
        echo Found reference in:
        echo     %%J
        echo ----------------------------------------
        set FOUND=1
    )
)
popd

if %FOUND%==0 (
    echo.
    echo No jar file containing 'SearchAndAssignACLApp' was found under:
    echo     %ROOT%
) else (
    echo.
    echo Done.
)

endlocal
