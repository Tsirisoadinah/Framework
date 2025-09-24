@echo off
:: ======================================
:: Script de compilation et création de JAR
:: ======================================

:: Configuration des variables
set BUILD_DIR=build
set SRC_DIR=src
set MAIN_CLASS=servlets.FrontServlet
set SERVLET_API_JAR=lib\jakarta.servlet-api-5.0.0.jar
set JAR_NAME=Test.jar
set DEPLOY_DIR="C:\S5\MrNaina\Framework\Framework\CodeTest\webapps\WEB-INF\lib"

echo ======================================
echo Déploiement de projet Java (.jar)
echo ======================================

:: Étape 0 - Nettoyer le dossier build s'il existe
if exist %BUILD_DIR% (
    echo Le dossier %BUILD_DIR% existe déjà. Suppression...
    rmdir /s /q %BUILD_DIR%
)

:: Étape 1 - Créer le dossier build
echo Création du dossier %BUILD_DIR%...
mkdir %BUILD_DIR%

:: Étape 2 - Compilation des fichiers Java
echo Compilation des fichiers Java...
dir /b /s %SRC_DIR%\*.java > sources.txt

javac -cp %SERVLET_API_JAR% -d %BUILD_DIR% @sources.txt
if errorlevel 1 (
    echo Erreur lors de la compilation. Vérifiez vos fichiers Java.
    del sources.txt
    pause
    exit /b 1
)
del sources.txt

:: Étape 3 - Création du fichier JAR
echo Création du fichier %JAR_NAME%...
cd %BUILD_DIR%
jar cfe %JAR_NAME% %MAIN_CLASS% *
cd ..

:: Étape 4 - Copier le JAR vers le dossier de déploiement
echo Déploiement du fichier %JAR_NAME% vers %DEPLOY_DIR%...
if not exist %DEPLOY_DIR% mkdir %DEPLOY_DIR%
copy %BUILD_DIR%\%JAR_NAME% %DEPLOY_DIR%

:: Étape 5 - Terminé
echo ======================================
echo Déploiement terminé avec succès !
echo Le fichier .jar a été copié dans %DEPLOY_DIR%.
echo Pour exécuter : java -jar %DEPLOY_DIR%\%JAR_NAME%
echo ======================================
pause
