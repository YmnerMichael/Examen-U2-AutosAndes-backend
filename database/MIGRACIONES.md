# Migraciones siguiendo health-check-service

Instalación nueva (configuración normal):
- db/V1__esquema_inicial.sql: tablas de negocio, correlativo y semilla del examen.
- db/V2__seguridad_empleados_usuarios.sql: empleados, roles, usuarios y relación de roles.
- db/V3__datos_iniciales_seguridad.sql: empleados y usuarios con BCrypt ya generado.

ADMIN, ASESOR y JEFE_TALLER conservan la matriz del examen. No se crean VENDEDOR ni ALMACENERO.
admin, asesor, jefe e inactivo usan admin2026, asesor2026, jefe2026 e inactivo2026, respectivamente. El último está deshabilitado.
Se agregó la entidad Empleado y la relación Usuario -> Empleado; el API sigue devolviendo nombreCompleto.
Los hashes no se calculan durante el arranque. El login ignora mayúsculas/minúsculas del usuario.

## Reiniciar una base que tenía la migración anterior
Se eliminó la cadena anterior de compatibilidad. Solo se conserva la cadena actual V1, V2 y V3.
Antes de arrancar, limpia manualmente las tablas del esquema de AutoAndes, incluida
"flyway_schema_history", y la secuencia orden_numero_seq. Borrar solo las tablas de
negocio deja registrado el checksum anterior y mantiene el error de validación.
Después ejecuta el backend con las ubicaciones actuales classpath:db,classpath:db-dev.

Credenciales vigentes tras V4: admin/admin2026, asesor/asesor2026, jefe/jefe2026 e inactivo/inactivo2026. La cuenta inactivo continúa deshabilitada. Reinicia el backend para que Flyway aplique V4 en la base configurada. No se modifica la V3 ya aplicada. La cadena de compatibilidad también incluye V4.
