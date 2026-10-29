package pe.edu.upeu.autoandes.config;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import java.nio.charset.StandardCharsets;import java.util.*;
@org.springframework.stereotype.Service
public class JwtService {
private final javax.crypto.SecretKey key;
public JwtService(@Value("${app.jwt.secret}") String secret) { key=Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); }
public String crear(String usuario,Set<String> roles,Set<String> permisos) { var ahora=new Date();return Jwts.builder().subject(usuario).issuer("autoandes").issuedAt(ahora).expiration(new Date(ahora.getTime()+3600000)).claim("roles",roles).claim("permisos",permisos).signWith(key).compact(); }
public String validar(String token) { return Jwts.parser().verifyWith(key).requireIssuer("autoandes").build().parseSignedClaims(token).getPayload().getSubject(); }
}
