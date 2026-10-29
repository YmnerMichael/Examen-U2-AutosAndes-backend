-- Ejecutar UNA VEZ con SYS o SYSTEM conectado a FREEPDB1.
-- SQL Developer solicitará una contraseña para el nuevo usuario.
-- Usa la misma en application-dev.yaml o en DB_PASSWORD.
-- No elimina ni modifica tablas de PHARMADB ni PROYECTO_MANA.
CREATE USER AUTOANDES IDENTIFIED BY "&autoandes_password"
    DEFAULT TABLESPACE USERS QUOTA UNLIMITED ON USERS;
GRANT CREATE SESSION, CREATE TABLE, CREATE SEQUENCE TO AUTOANDES;
