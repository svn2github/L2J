@echo off
color 0B
title ][ Lindvior: Compiler Server Console ][

        :Step1
        cls
        echo.
        echo. ][ Compilation of Server ][ 
        echo.===============================
        echo. [1] - Compile Line][age Server 
        echo.===============================

        set Step1prompt=x
        set /p Step1prompt= Your choise : 
        if /i %Step1prompt%==1 goto Compile
        goto Step1
        
        
        :Compile
        @cls
        title Compile Server 
        color 0B
        echo.
        echo Compilation process. Please wait...
        ant -f build.xml -l Compile.log
        echo Compilation successful!!!
        pause

        
:fullend