@echo off
cd /d "%~dp0"

javac -cp ".;lib\sqlite-jdbc.jar;lib\jfreechart-1.0.1\lib\jfreechart-1.0.1.jar;lib\jfreechart-1.0.1\lib\jcommon-1.0.0.jar" src\*.java

java -cp ".;src;lib\sqlite-jdbc.jar;lib\jfreechart-1.0.1\lib\jfreechart-1.0.1.jar;lib\jfreechart-1.0.1\lib\jcommon-1.0.0.jar" ExpenseTracker

pause
