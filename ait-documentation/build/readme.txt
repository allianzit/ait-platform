El script build.bat se encarga de construir los proyectos en el orden adecuado para permitir su correcto funcionamiento.

Antes de su ejecuci�n se deben realizar los siguientes pasos:

1. Crear la carpeta que contendr� los proyectos construidos y listos para despliegue:
mkdir c:\output\build\

2 Crear la variable de entorno que define la ubicaci�n de los fuentes de los proyectos (AIT_WORKSPACE)
El siguiente es un shell de ejemplo que crea dicha variable. Se debe ejecutar en una consola con permisos de administraci�n
SETX AIT_WORKSPACE "C:\Users\msi-gs60\git\ait-platform" /M

3. Modifique el numero de la versi�n creada de ser necesario. (variable del script: aitVersion)


	
	
