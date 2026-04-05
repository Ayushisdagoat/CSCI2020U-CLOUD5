@echo off
echo ==========================
echo Building CLOUD5 Project...
echo ==========================

mvn clean install

if %errorlevel% neq 0 (
    echo Build failed!
    pause
    exit /b 1
)

echo ==========================
echo Running Application...
echo ==========================

mvn exec:java -Dexec.mainClass="org.example.FrontEnd"

pause
