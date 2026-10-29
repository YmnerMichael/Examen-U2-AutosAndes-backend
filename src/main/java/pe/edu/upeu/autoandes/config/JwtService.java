package pe.edu.upeu.autoandes.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Set;

@Service
public class JwtService {
    private final SecretKey key;
    private final long expiraEnSegundos;

    public JwtService(@Value("${app.jwt.secret}") String secret,
                      @Value("${app.jwt.expiracion-minutos:60}") long minutos) {
        if (minutos <= 0) throw new IllegalArgumentException("La duración del JWT debe ser positiva");
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.expiraEnSegundos = Math.multiplyExact(minutos, 60L);
    }

    public long expiraEnSegundos() { return expiraEnSegundos; }

    public String crear(String usuario, Set<String> roles, Set<String> permisos) {
        var ahora = new Date();
        return Jwts.builder().subject(usuario).issuer("autoandes").issuedAt(ahora)
            .expiration(new Date(Math.addExact(ahora.getTime(), Math.multiplyExact(expiraEnSegundos, 1000L))))
            .claim("roles", roles).claim("permisos", permisos).signWith(key).compact();
    }

    public String validar(String token) {
        return Jwts.parser().verifyWith(key).requireIssuer("autoandes").build()
            .parseSignedClaims(token).getPayload().getSubject();
    }
}
