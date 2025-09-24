REM Définition des variables
SET APP_NAME=Authentifier
SET SRC_DIR=src\main\java
SET WEB_DIR=src\main\webapp
SET BUILD_DIR=build
SET LIB_DIR=lib
SET TOMCAT_WEBAPPS=C:\Program Files\Apache Software Foundation\Tomcat 10.1\webapps
SET SERVLET_API_JAR=%LIB_DIR%\servlet-api.jar

REM Nettoyage et création du répertoire temporaire
rmdir /S /Q %BUILD_DIR%
mkdir %BUILD_DIR%\WEB-INF\classes

REM Compilation des fichiers .java
javac -cp "%SERVLET_API_JAR%" -d "%BUILD_DIR%\WEB-INF\classes" "%SRC_DIR%\*.java"
if %ERRORLEVEL% NEQ 0 (
    echo ERREUR : La compilation a echoue !
    exit /B 1
) else (
    echo Compilation reussie !
)


REM Copier les fichiers web
xcopy "%WEB_DIR%\*" %BUILD_DIR%\ /E /I /Y

REM Création du fichier .war
cd %BUILD_DIR% 
jar -cvf %APP_NAME%.war *

REM Déploiement du fichier .war dans Tomcat
copy %APP_NAME%.war "%TOMCAT_WEBAPPS%"
