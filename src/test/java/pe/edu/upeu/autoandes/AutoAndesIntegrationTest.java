package pe.edu.upeu.autoandes;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import java.net.URI;
import com.fasterxml.jackson.databind.*;

/** Full MVC, security filters and real persistence; H2 is exclusively a test database. */
@SpringBootTest(properties="spring.profiles.active=test")
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
@ActiveProfiles("test")
@org.springframework.test.context.ContextConfiguration(initializers=H2MigrationInitializer.class)
@org.springframework.test.context.TestExecutionListeners(
    listeners=org.springframework.test.context.support.DependencyInjectionTestExecutionListener.class,
    inheritListeners=false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AutoAndesIntegrationTest {
    @Autowired JdbcTemplate db;
    @Autowired org.springframework.test.web.servlet.MockMvc mvc;
    final ObjectMapper json=new ObjectMapper();
    record Response(int statusCode,String body) {}
    Response call(String method,String path,String token,String body) throws Exception {
        var request=org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request(
            org.springframework.http.HttpMethod.valueOf(method),"/api/v1"+path);
        if(token!=null)request.header("Authorization","Bearer "+token);
        if(body!=null)request.contentType(org.springframework.http.MediaType.APPLICATION_JSON).content(body);
        var response=mvc.perform(request).andReturn().getResponse();
        return new Response(response.getStatus(),response.getContentAsString(java.nio.charset.StandardCharsets.UTF_8));
    }
    String login(String username) throws Exception {
        var res=call("POST","/auth/login",null,"{\"username\":\""+username+"\",\"password\":\""+username.toLowerCase(java.util.Locale.ROOT)+"2026\"}");
        assertEquals(200,res.statusCode(),res.body());
        assertEquals(3600,json.readTree(res.body()).get("expiraEnSegundos").asLong());
        return json.readTree(res.body()).get("token").asText();
    }
    @Test @Order(1) void authenticationAndPermissions() throws Exception {
        assertEquals(401,call("GET","/servicios",null,null).statusCode());
        String asesor=login("asesor"),jefe=login("jefe");login("admin");login("AsEsOr");
        assertEquals(403,call("GET","/reportes/ingresos-por-servicio",asesor,null).statusCode());
        assertEquals(403,call("PATCH","/ordenes/1/anular",asesor,"{\"motivo\":\"Solicitud del cliente\"}").statusCode());
        assertEquals(403,call("POST","/ordenes",jefe,"{\"vehiculoId\":1,\"kilometraje\":46000,\"detalles\":[{\"servicioId\":1,\"cantidad\":1}]}").statusCode());
        assertEquals(401,call("GET","/servicios",asesor+"alterado",null).statusCode());
        var inactive=call("POST","/auth/login",null,"{\"username\":\"inactivo\",\"password\":\"inactivo2026\"}");
        var unknown=call("POST","/auth/login",null,"{\"username\":\"nadie\",\"password\":\"nadie2026\"}");
        assertEquals(401,inactive.statusCode());assertEquals(json.readTree(inactive.body()).get("mensaje"),json.readTree(unknown.body()).get("mensaje"));
        assertTrue(db.queryForObject("select password from AUTOANDES.usuarios where username='admin'",String.class).startsWith("$2"));
        assertEquals(5,db.queryForObject("select count(*) from AUTOANDES.empleados",Integer.class));
        assertEquals(4,db.queryForObject("select count(*) from AUTOANDES.usuarios where empleado_id is not null",Integer.class));
        assertEquals(4,db.queryForObject("select count(*) from AUTOANDES.\"flyway_schema_history\" where \"success\"=true and \"version\" is not null",Integer.class));
        var me=json.readTree(call("GET","/auth/me",asesor,null).body());
        assertEquals("Lucia Fernandez Quispe",me.get("nombreCompleto").asText());
        assertFalse(me.has("password"));
    }
    @Test @Order(2) void catalogAndValidationContract() throws Exception {
        String admin=login("admin");
        var invalid=call("POST","/servicios",admin,"{\"codigo\":\"mal\",\"nombre\":\"x\",\"precio\":0,\"duracionMin\":2,\"activo\":true}");
        assertEquals(400,invalid.statusCode());assertTrue(json.readTree(invalid.body()).get("campos").has("codigo"));
        assertEquals(404,call("GET","/vehiculos/999999",admin,null).statusCode());
        assertEquals(409,call("POST","/vehiculos",admin,"{\"placa\":\"AAA-111\",\"marca\":\"Toyota\",\"modelo\":\"Corolla\",\"anio\":2020,\"clienteId\":1}").statusCode());
        assertEquals(409,call("DELETE","/servicios/1",admin,null).statusCode());
        assertEquals(409,call("DELETE","/vehiculos/1",admin,null).statusCode());
        String payload="{\"codigo\":\"SRV-099\",\"nombre\":\"Servicio prueba\",\"precio\":19.95,\"duracionMin\":30,\"activo\":true}";
        var created=call("POST","/servicios",admin,payload);assertEquals(201,created.statusCode(),created.body());
        long id=json.readTree(created.body()).get("id").asLong();assertEquals(200,call("PUT","/servicios/"+id,admin,payload).statusCode());assertEquals(204,call("DELETE","/servicios/"+id,admin,null).statusCode());
        var car=call("POST","/vehiculos",admin,"{\"placa\":\"XYZ-999\",\"marca\":\"Kia\",\"modelo\":\"Rio\",\"anio\":2020,\"clienteId\":1}");
        assertEquals(201,car.statusCode(),car.body());var cv=json.readTree(car.body());assertEquals("Cliente AutoAndes 1",cv.get("clienteNombre").asText());assertFalse(cv.has("cliente"));assertEquals(204,call("DELETE","/vehiculos/"+cv.get("id").asLong(),admin,null).statusCode());
    }
    @Test @Order(3) void thirdDetailFailureRollsBackTheHeaderAndAllDetails() throws Exception {
        String asesor=login("asesor");
        long before=db.queryForObject("select count(*) from AUTOANDES.orden_servicio",Long.class);
        long details=db.queryForObject("select count(*) from AUTOANDES.detalle_orden",Long.class);
        var res=call("POST","/ordenes",asesor,"{\"vehiculoId\":1,\"kilometraje\":46000,\"detalles\":[{\"servicioId\":1,\"cantidad\":1},{\"servicioId\":2,\"cantidad\":1},{\"servicioId\":8,\"cantidad\":1}]}");
        assertEquals(409,res.statusCode(),res.body());assertTrue(res.body().contains("inactivo"));
        assertEquals(before,db.queryForObject("select count(*) from AUTOANDES.orden_servicio",Long.class));
        assertEquals(details,db.queryForObject("select count(*) from AUTOANDES.detalle_orden",Long.class));
        assertEquals(409,call("POST","/ordenes",asesor,"{\"vehiculoId\":1,\"kilometraje\":44999,\"detalles\":[{\"servicioId\":1,\"cantidad\":1}]}").statusCode());
        assertEquals(400,call("POST","/ordenes",asesor,"{\"vehiculoId\":1,\"kilometraje\":46000,\"detalles\":[{\"servicioId\":1,\"cantidad\":1},{\"servicioId\":1,\"cantidad\":1}]}").statusCode());
    }
    @Test @Order(4) void serverTotalsFiltersAndCancellationReport() throws Exception {
        String asesor=login("asesor"),jefe=login("jefe");
        var res=call("POST","/ordenes",asesor,"{\"vehiculoId\":1,\"kilometraje\":47000,\"total\":0.01,\"detalles\":[{\"servicioId\":1,\"cantidad\":2,\"precioUnitario\":0.01},{\"servicioId\":2,\"cantidad\":1}]}");
        assertEquals(201,res.statusCode(),res.body());JsonNode o=json.readTree(res.body());assertEquals(100,o.get("total").asDouble());assertEquals(25,o.get("detalles").get(0).get("precioUnitario").asDouble());long id=o.get("id").asLong();
        var filtered=call("GET","/ordenes?placa=AAA-111&estado=REGISTRADA&desde=2000-01-01&hasta=2099-12-31",asesor,null);assertEquals(200,filtered.statusCode());assertEquals(2,json.readTree(filtered.body()).size());
        var report=call("GET","/reportes/ingresos-por-servicio",jefe,null);assertEquals(200,report.statusCode(),report.body());assertFalse(report.body().contains("SRV-004"));
        assertEquals(400,call("PATCH","/ordenes/"+id+"/anular",jefe,"{\"motivo\":\"corto\"}").statusCode());
        assertEquals(200,call("PATCH","/ordenes/"+id+"/anular",jefe,"{\"motivo\":\"Solicitud de cancelación del cliente\"}").statusCode());
        assertEquals(409,call("PATCH","/ordenes/"+id+"/anular",jefe,"{\"motivo\":\"Solicitud de cancelación del cliente\"}").statusCode());
        var after=json.readTree(call("GET","/reportes/ingresos-por-servicio",jefe,null).body());
        double total=0;for(var r:after)total+=r.get("importeTotal").asDouble();assertEquals(150,total);
        db.update("update AUTOANDES.orden_servicio set fecha=TIMESTAMP '2020-01-01 10:00:00' where id=2");
        assertEquals(409,call("PATCH","/ordenes/2/anular",jefe,"{\"motivo\":\"Solicitud de cancelación del cliente\"}").statusCode());
    }
    @Test @Order(5) void yamlOptionsControlCorsJwtDurationAndIntegerParsing() throws Exception {
        var allowed=mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options("/api/v1/servicios")
            .header("Origin","http://localhost:4200").header("Access-Control-Request-Method","GET")
            .header("Access-Control-Request-Headers","Authorization")).andReturn().getResponse();
        assertEquals(200,allowed.getStatus());
        assertEquals("http://localhost:4200",allowed.getHeader("Access-Control-Allow-Origin"));
        var denied=mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options("/api/v1/servicios")
            .header("Origin","https://otro.example").header("Access-Control-Request-Method","GET")).andReturn().getResponse();
        assertEquals(403,denied.getStatus());
        assertEquals(400,call("POST","/ordenes",login("asesor"),"{\"vehiculoId\":1,\"kilometraje\":49000,\"detalles\":[{\"servicioId\":1,\"cantidad\":1.5}]}").statusCode());
        String secret=java.util.Base64.getEncoder().encodeToString("ClaveDePruebaBase64ConMasDeTreintaYDosBytes2026".getBytes(java.nio.charset.StandardCharsets.UTF_8));
        var service=new pe.edu.upeu.autoandes.config.JwtService(secret,30);
        var claims=io.jsonwebtoken.Jwts.parser().verifyWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor(io.jsonwebtoken.io.Decoders.BASE64.decode(secret))).build()
            .parseSignedClaims(service.crear("asesor",java.util.Set.of("ASESOR"),java.util.Set.of("ORDEN_LEER"))).getPayload();
        assertEquals(1800,service.expiraEnSegundos());
        assertEquals(1800,(claims.getExpiration().getTime()-claims.getIssuedAt().getTime())/1000);
    }
}
