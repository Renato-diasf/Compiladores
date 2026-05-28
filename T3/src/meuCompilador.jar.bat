@echo off
setlocal
set "DIR=%~dp0"
java -jar "%DIR%..\target\meuCompilador.jar" %*
