%~d0
SET workspace=%AIT_WORKSPACE%
SET m2repo= "%USERPROFILE%\.m2\repository\com\ait\platform"
SET aitVersion=1.0
SET output= "c:\output\build"

rmdir /s /q %m2repo%
del /s /q %output%


cd %workspace%/management/cloud-config
call mvn clean
call mvn install -Dmaven.test.skip=true

cd %workspace%/management/discovery
call mvn clean
call mvn install -Dmaven.test.skip=true

cd %workspace%/management/zuul
call mvn clean
call mvn install -Dmaven.test.skip=true

cd %workspace%/apps/common/common-vo
call mvn clean
call mvn install -Dmaven.test.skip=true

cd %workspace%/apps/common/common-entity
call mvn clean
call mvn install -Dmaven.test.skip=true

cd %workspace%/apps/common/common-utils
call mvn clean
call mvn install -Dmaven.test.skip=true

cd %workspace%/apps/common/common-api-client
call mvn clean
call mvn install -Dmaven.test.skip=true

cd %workspace%/apps/common/common-app
call mvn clean
call mvn install -Dmaven.test.skip=true

copy  /B %workspace%\management\cloud-config\target\cloud-config-%aitVersion%.jar %output%\cloud-config.jar 
copy  /B %workspace%\management\discovery\target\discovery-%aitVersion%.jar %output%\discovery.jar 
copy  /B %workspace%\management\zuul\target\zuul-%aitVersion%.jar %output%\zuul.jar 
copy  /B %workspace%\apps\common\common-app\target\common-%aitVersion%.jar %output%\common.jar 
pause
