@echo off
setlocal
set "SRC_DIR=%~dp0."
"C:\Program Files\Eclipse Adoptium\jdk-25.0.2.10-hotspot\bin\java.exe" -cp "%SRC_DIR%" Main %*
endlocal
