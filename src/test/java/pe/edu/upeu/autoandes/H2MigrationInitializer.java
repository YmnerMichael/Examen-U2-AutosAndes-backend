package pe.edu.upeu.autoandes;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.boot.test.util.TestPropertyValues;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/** H2 lacks Oracle function-based indexes. Only their equivalent syntax is adapted. */
public class H2MigrationInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext context) {
        try {
            var folder = Path.of("target", "h2-migrations").toAbsolutePath();
            Files.createDirectories(folder);
            for (String name : new String[]{"V1__esquema_inicial.sql", "V2__seguridad_empleados_usuarios.sql", "V3__datos_iniciales_seguridad.sql", "V4__credenciales_por_usuario.sql"}) {
                try (var in = getClass().getResourceAsStream("/db/" + name)) {
                    if (in == null) throw new IllegalStateException("Migración ausente: " + name);
                    String sql = new String(in.readAllBytes(), StandardCharsets.UTF_8);
                    sql = sql.replace("create unique index ux_empleados_email on empleados (upper(email));",
                        "alter table empleados add email_normalizado varchar2(150 char) generated always as (upper(email));\ncreate unique index ux_empleados_email on empleados (email_normalizado);");
                    sql = sql.replace("create unique index ux_usuarios_username on usuarios (upper(username));",
                        "alter table usuarios add username_normalizado varchar2(50 char) generated always as (upper(username));\ncreate unique index ux_usuarios_username on usuarios (username_normalizado);");
                    Files.writeString(folder.resolve(name), sql, StandardCharsets.UTF_8);
                }
            }
            TestPropertyValues.of("spring.flyway.locations=filesystem:" + folder.toString().replace('\\', '/')).applyTo(context);
        } catch (java.io.IOException ex) {
            throw new IllegalStateException("No se pudieron preparar las migraciones de prueba", ex);
        }
    }
}
