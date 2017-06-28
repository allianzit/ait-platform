--STORE PROCEDURE PARA ASIGNACION DE PRIVILEGIOS DE REFERENCIACION Y SELECT SOBRE LAS TABLAS DE SUSI_CORE A LOS DEMAS ESQUEMAS (PEF, PQR, PEF, LEGACY...)
CREATE OR REPLACE PROCEDURE aitGrants( schemaName in varchar2 ) IS
BEGIN
	FOR rec IN (SELECT TABLE_NAME as tableName FROM user_tables WHERE TABLE_NAME LIKE 'AIT_%')
	LOOP
    	DBMS_OUTPUT.put_line ('Actual table: ' || rec.tableName);
    execute immediate 'GRANT SELECT, REFERENCES ON AIT.' || rec.tableName || ' TO '|| schemaName;
	END LOOP;
	DBMS_OUTPUT.put_line ('Done');
END;
    

--ejemplo de ejecución para asignar permisos a un usuario TEST
EXECUTE aitGrants('TEST');