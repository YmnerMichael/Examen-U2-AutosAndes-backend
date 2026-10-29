package pe.edu.upeu.autoandes.config;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pe.edu.upeu.autoandes.security.*;
@Configuration @org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
public class SecurityConfig {
@Bean public org.springframework.security.crypto.password.PasswordEncoder passwordEncoder() { return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder(); }
@Bean public SecurityFilterChain filterChain(HttpSecurity http,JwtAuthenticationFilter jwt,RestAuthenticationEntryPoint entry,RestAccessDeniedHandler denied) throws Exception {
return http.csrf(c->c.disable()).cors(org.springframework.security.config.Customizer.withDefaults()).sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).exceptionHandling(e->e.authenticationEntryPoint(entry).accessDeniedHandler(denied)).authorizeHttpRequests(a->a.requestMatchers(org.springframework.http.HttpMethod.OPTIONS,"/**").permitAll().requestMatchers("/api/v1/auth/login","/swagger-ui/**","/swagger-ui.html","/v3/api-docs/**").permitAll().anyRequest().authenticated()).addFilterBefore(jwt,UsernamePasswordAuthenticationFilter.class).build(); }
@Bean public org.springframework.boot.web.servlet.FilterRegistrationBean<JwtAuthenticationFilter> disableDuplicateFilter(JwtAuthenticationFilter filter) { var bean=new org.springframework.boot.web.servlet.FilterRegistrationBean<>(filter);bean.setEnabled(false);return bean; }
}
