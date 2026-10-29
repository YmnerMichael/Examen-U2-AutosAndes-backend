# AutoAndes API
Abre pom.xml en IntelliJ. Consulta ../LEEME-PRIMERO.md para configurar Oracle sin afectar Pharma.
Ejecutar: `mvn spring-boot:run`. Pruebas aisladas: `mvn test`.

## Contrato bajo /api/v1
POST /auth/login; GET /auth/me.
GET /clientes.
GET y POST /servicios; GET, PUT y DELETE /servicios/{id}; PATCH /servicios/{id}/estado.
GET y POST /vehiculos (filtro clienteId); GET, PUT y DELETE /vehiculos/{id}.
GET y POST /ordenes; GET /ordenes/{id}; PATCH /ordenes/{id}/anular.
Filtros de órdenes: placa, estado, desde, hasta.
GET /reportes/ingresos-por-servicio con desde y hasta.

JWT requerido salvo login, Swagger y OPTIONS. Los permisos se verifican mediante @PreAuthorize.
Los errores incluyen timestamp, status, error, mensaje, ruta y campos cuando corresponde.
Los usuarios admin, asesor y jefe usan su nombre de usuario seguido de 2026. El usuario inactivo rechaza el login.
La migración aplica el modelo del examen en el esquema configurado (MECANICADB en desarrollo; AUTOANDES por defecto en producción). No usa ni modifica el historial Flyway de PHARMADB.

## Resources
- application.yml: configuración común; perfil dev activo.
- application-dev.yaml: conexión Oracle de desarrollo. Completa aquí tus credenciales o usa variables de entorno.
- application-prod.yaml: configuración mediante DB_URL, DB_USERNAME, DB_PASSWORD y JWT_SECRET; activa SPRING_PROFILES_ACTIVE=prod.
- db/V1__esquema_inicial.sql: migración Flyway.

JWT: clave Base64 en app.jwt.secret, duración en app.jwt.expiracion-minutos. CORS lee app.cors.allowed-origins. Desarrollo: usuario MECANICADB; DB_URL, DB_USERNAME y DB_PASSWORD permiten cambiar la conexión. Hibernate y Flyway usan el esquema del usuario conectado, salvo que se indique DB_SCHEMA explícitamente.

Referencia actual: health-check-service. Resources contiene application.yml, application-dev.yaml, application-prod.yaml, db y db-dev. Desarrollo usa classpath:db,classpath:db-dev; producción usa classpath:db. Se conserva el contenido de la migración obligatoria del examen. La conexión de AutoAndes se conserva independiente de Pharma.

Las migraciones se separaron siguiendo el ejemplo: V1 negocio, V2 seguridad con empleados y V3 datos de acceso BCrypt. Si ya aplicaste la versión anterior, consulta backendAutoAndes/database/MIGRACIONES.md (database/MIGRACIONES.md desde el backend) antes de arrancar.

Credenciales vigentes tras V4: admin/admin2026, asesor/asesor2026, jefe/jefe2026 e inactivo/inactivo2026. La cuenta inactivo continúa deshabilitada. Reinicia el backend para que Flyway aplique V4 en la base configurada. No se modifica la V3 ya aplicada. La cadena de compatibilidad también incluye V4.
