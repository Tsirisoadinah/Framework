@echo off
setlocal enabledelayedexpansion

REM Définition des variables
SET APP_NAME=FrameworkApp
SET SRC_DIR=src\main\java
SET WEB_DIR=src\main\webapp
SET BUILD_DIR=build
SET LIB_DIR=lib
SET TOMCAT_WEBAPPS=C:\Program Files\Apache Software Foundation\apache-tomcat-10.1.28\webapps
SET SERVLET_API_JAR=%LIB_DIR%\servlet-api.jar
SET SPRINT1_JAR=%LIB_DIR%\Sprint1.jar
SET CLASSPATH=%SERVLET_API_JAR%;%SPRINT1_JAR%

REM Nettoyage et création du répertoire temporaire
rmdir /S /Q %BUILD_DIR%
mkdir %BUILD_DIR%\WEB-INF\classes

REM Compilation des fichiers .java
for /r "%SRC_DIR%" %%f in (*.java) do (
    javac -cp "%CLASSPATH%" -d "%BUILD_DIR%\WEB-INF\classes" "%%f"
    if !ERRORLEVEL! NEQ 0 (
        echo ERREUR : La compilation de %%f a echoue !
        exit /B 1
    )
)
echo Compilation reussie !


REM Copier les fichiers web
xcopy "%WEB_DIR%\*" %BUILD_DIR%\ /E /I /Y

REM Création du fichier .war
cd %BUILD_DIR% 
jar -cvf %APP_NAME%.war *

REM Déploiement du fichier .war dans Tomcat
copy %APP_NAME%.war "%TOMCAT_WEBAPPS%"
