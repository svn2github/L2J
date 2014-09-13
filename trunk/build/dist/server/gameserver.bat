@echo off
Color 0B
title Lindvior: Game Server Console
:start
echo Starting Lindvior Game Server.
echo.

java -Dfile.encoding=UTF-8 -Xmx3g -server -cp config;../serverslibs/*; l2next.gameserver.GameServer

if ERRORLEVEL 2 goto restart
if ERRORLEVEL 1 goto error
goto end
:restart
echo.
echo Administrator Restarted ...
echo.
goto start
:error
echo.
echo GameServer Terminated Abnormaly, Please Verify Your Files.
echo.
:end
echo.
echo GameServer Terminated.
echo.
pause
