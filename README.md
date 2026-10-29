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
Los usuarios admin, asesor y jefe usan Auto2026*. El usuario inactivo rechaza el login.
La migración aplica el modelo del examen en AUTOANDES. No usa ni modifica el historial Flyway de PHARMADB.
