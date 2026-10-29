-- =====================================================================
-- V4: contraseñas de prueba individuales (usuario + 2026)
-- Las contraseñas se guardan cifradas con BCrypt:
--   admin     -> admin2026
--   asesor    -> asesor2026
--   jefe      -> jefe2026
--   inactivo  -> inactivo2026 (cuenta deshabilitada)
-- Se actualizan cuentas existentes sin cambiar sus roles ni su estado.
-- V3 se conserva intacta para respetar su checksum de Flyway.
-- =====================================================================
update usuarios set password = '$2a$10$0tgq1msh8jZ7aIlc117YaewgrbDBw7.eJOubzt0erqUQ6yZXRNjMC'
where upper(username) = 'ADMIN';

update usuarios set password = '$2a$10$KeflnUGXN8xf3RPsLZaJ1.me5XWZfj8Wxmip1wOu.2iCJPK0skcia'
where upper(username) = 'ASESOR';

update usuarios set password = '$2a$10$e.YUPc1PI63kpt0ezvT9Bua1LzSZUIYCJRr4O600dxw5ka2aGPeRC'
where upper(username) = 'JEFE';

update usuarios set password = '$2a$10$AAGRe.xPgdAmef/AkAH3euI9tZrKT2e8Bw2vOcxSRQBoQIqgDnM0i'
where upper(username) = 'INACTIVO';

