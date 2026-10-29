package pe.edu.upeu.autoandes.config;
import org.springframework.context.annotation.*;
import org.springframework.web.cors.*;import java.util.List;
@Configuration public class CorsConfig {
@Bean public CorsConfigurationSource corsConfigurationSource() { var c=new CorsConfiguration();c.setAllowedOrigins(List.of("http://localhost:4200"));c.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));c.setAllowedHeaders(List.of("Authorization","Content-Type"));var s=new UrlBasedCorsConfigurationSource();s.registerCorsConfiguration("/**",c);return s; }
@Bean public java.time.Clock clock(@org.springframework.beans.factory.annotation.Value("${app.zone}") String zone) { return java.time.Clock.system(java.time.ZoneId.of(zone)); }
}
