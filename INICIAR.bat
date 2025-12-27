@echo off
title Iniciando KLK Launcher...

REM ANTIJAVAS
taskkill /F /IM java.exe >nul 2>&1
taskkill /F /IM javaw.exe >nul 2>&1

REM LANZAMIENTO

"%~dp0portable\runtimes\java+\java\bin\java.exe" -jar "%~dp0KLK_Launcher.jar"
exit

