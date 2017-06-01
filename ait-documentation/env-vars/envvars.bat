rem basic profiles
SETX AIT_PROFILE "minimal,dev,git,fast" /M

rem cloud-config server configuration
SETX AIT_ENCRYPT_KEY "4LL14NZ4ITPL4TF0RM" /M
SETX AIT_CLOUD_CONFIG_URI "http://localhost:8888" /M
SETX AIT_CLOUD_CONFIG_PASS "AITPlatform2017!." /M
SETX AIT_SERVER_PWD "AITPlatform2017!." /M

rem configuracion de acceso al repositorio de archivos de configuracion usado por cloud-config
SETX AIT_REPO_URI "https://github.com/allianzit/ait-platform-config" /M
SETX AIT_REPO_USERNAME "" /M
SETX AIT_REPO_PASSWORD "" /M
SETX AIT_REPO_DEFAULT_LABEL "master" /M

rem ubicacion de los archivos de log de los diferentes servidores de la plataforma
SETX AIT_LOG_PATH "c:/output/logs/" /M

rem host adicionales
SETX AIT_DISCOVERY_HOST "localhost" /M
SETX AIT_ELASTICSEARCH_URI "http://127.0.0.1:9200" /M
SETX AIT_HOSTNAME "localhost" /M
SETX AIT_REDIS_HOST "localhost" /M

rem ubicacion del keystore del servidor zuul
SETX AIT_KEY_STORE "classpath:keystore-ait.p12" /M

echo Recuerde ejecutar el shell con permisos de administrador
pause