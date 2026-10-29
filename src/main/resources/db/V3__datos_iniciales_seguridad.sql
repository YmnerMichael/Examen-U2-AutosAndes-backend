-- =====================================================================
-- V3: datos iniciales de empleados y usuarios de AutoAndes
-- Contraseñas BCrypt, ya cifradas en el script:
-- admin, asesor, jefe e inactivo -> Auto2026*
-- inactivo rechaza el login. El último empleado no tiene cuenta de usuario.
-- =====================================================================
insert into empleados (dni, nombres, apellidos, cargo, email, telefono, estado, fecha_creacion)
values ('70123456', 'Carlos', 'Ramirez Torres', 'Administrador', 'carlos.ramirez@autoandes.local', '987654321', true, systimestamp);

insert into empleados (dni, nombres, apellidos, cargo, email, telefono, estado, fecha_creacion)
values ('71234567', 'Lucia', 'Fernandez Quispe', 'Asesora de servicio', 'lucia.fernandez@autoandes.local', '987111222', true, systimestamp);

insert into empleados (dni, nombres, apellidos, cargo, email, telefono, estado, fecha_creacion)
values ('72345678', 'Jorge', 'Mendoza Huaman', 'Jefe de taller', 'jorge.mendoza@autoandes.local', '987333444', true, systimestamp);

insert into empleados (dni, nombres, apellidos, cargo, email, telefono, estado, fecha_creacion)
values ('73456789', 'Rosa', 'Castillo Vargas', 'Asesora de servicio', 'rosa.castillo@autoandes.local', null, false, systimestamp);

insert into empleados (dni, nombres, apellidos, cargo, email, telefono, estado, fecha_creacion)
values ('74567890', 'Luis', 'Quispe Rojas', 'Mecánico', 'luis.quispe@autoandes.local', null, true, systimestamp);

insert into usuarios (username, password, estado, empleado_id, fecha_creacion)
select 'admin', '$2a$10$khP.rJiUJfcaVh9zgaXYu.sNniQyi84X4A.XhzTkCoidxUalMoEzS', true, id, systimestamp
from empleados where dni = '70123456';

insert into usuario_roles (usuario_id, rol_id)
select u.id, r.id from usuarios u cross join roles r
where u.username = 'admin' and r.nombre = 'ADMIN';

insert into usuarios (username, password, estado, empleado_id, fecha_creacion)
select 'asesor', '$2a$10$khP.rJiUJfcaVh9zgaXYu.sNniQyi84X4A.XhzTkCoidxUalMoEzS', true, id, systimestamp
from empleados where dni = '71234567';

insert into usuario_roles (usuario_id, rol_id)
select u.id, r.id from usuarios u cross join roles r
where u.username = 'asesor' and r.nombre = 'ASESOR';

insert into usuarios (username, password, estado, empleado_id, fecha_creacion)
select 'jefe', '$2a$10$khP.rJiUJfcaVh9zgaXYu.sNniQyi84X4A.XhzTkCoidxUalMoEzS', true, id, systimestamp
from empleados where dni = '72345678';

insert into usuario_roles (usuario_id, rol_id)
select u.id, r.id from usuarios u cross join roles r
where u.username = 'jefe' and r.nombre = 'JEFE_TALLER';

insert into usuarios (username, password, estado, empleado_id, fecha_creacion)
select 'inactivo', '$2a$10$khP.rJiUJfcaVh9zgaXYu.sNniQyi84X4A.XhzTkCoidxUalMoEzS', false, id, systimestamp
from empleados where dni = '73456789';

insert into usuario_roles (usuario_id, rol_id)
select u.id, r.id from usuarios u cross join roles r
where u.username = 'inactivo' and r.nombre = 'ASESOR';

