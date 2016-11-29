@echo off

if "%1"=="pre-action" (
echo Modify build_number...
sed -i "s/{_DEBUG_}//g" .\app\src\main\java\com\qihoo\lianxian\utils\SystemInfo.java
sed -i "s/{_BUILD_NUM_}/%BUILD_NUMBER%/g" .\app\src\main\java\com\qihoo\lianxian\utils\SystemInfo.java
sed -i "s/5deb7a0983a1a2378ea0/c6e28cf384711d641e7b/g" .\app\src\main\AndroidManifest.xml
exit 0
)

if "%1"=="post-action" (
md artifacts
java -jar C:\ci-tools\resourcesproguard.jar -config C:\ci-tools\config.xml -out c:\ci-tools\temp .\app\build\outputs\apk\app-release.apk
copy c:\ci-tools\temp\app-release_signed_7zip_aligned.apk artifacts\lianxian_%BUILD_NUMBER%.apk
copy .\app\build\outputs\mapping\release\mapping.txt artifacts\mapping_%BUILD_NUMBER%.txt
del /F /Q c:\ci-tools\temp
exit 0
)
