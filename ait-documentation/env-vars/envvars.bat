rem basic profiles
configPass='AITPlatform2017!.'
svnPass='Mimojal263!.' 
export AIT_PROFILE=minimal,dev,subversion,fast 
export AIT_ENCRYPT_KEY=4LL14NZ4ITPL4TF0RM 
export AIT_CLOUD_CONFIG_URI=http://localhost:8888 
export AIT_CLOUD_CONFIG_PASS=$configPass 
export AIT_SERVER_PWD=$configPass 
export AIT_REPO_URI=http://localhost/svn/repo/rte/mincit-cloud-mincit 
export AIT_REPO_USERNAME=rcruz 
export AIT_REPO_PASSWORD=$svnPass
export AIT_REPO_DEFAULT_LABEL=  
export AIT_LOG_PATH=/opt/mincit/logs/ 
export AIT_DISCOVERY_HOST=localhost 
export AIT_ELASTICSEARCH_URI=http://127.0.0.1:9200 
export AIT_HOSTNAME=localhost 
export AIT_REDIS_HOST=localhost 
export AIT_KEY_STORE=classpath:keystore-ait.p12 

echo Recuerde ejecutar el shell con permisos de administrador
pause