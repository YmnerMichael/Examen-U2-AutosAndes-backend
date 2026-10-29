-- Ejecutar UNA VEZ con SYS o SYSTEM en FREEPDB1. No elimina datos de PHARMADB.
-- Conserva la conexión PHARMADB del proyecto original, y aísla AutoAndes en su esquema.
CREATE USER AUTOANDES NO AUTHENTICATION DEFAULT TABLESPACE USERS QUOTA UNLIMITED ON USERS;
GRANT CREATE SESSION, CREATE TABLE, CREATE SEQUENCE TO AUTOANDES;
ALTER USER AUTOANDES GRANT CONNECT THROUGH PHARMADB;
-- JDBC usa PHARMADB[AUTOANDES] con la misma contraseña local de PHARMADB.
-- No concede permisos ANY ni modifica las tablas existentes de PHARMADB.
