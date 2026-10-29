package pe.edu.upeu.autoandes.security;
import pe.edu.upeu.autoandes.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
@org.springframework.stereotype.Service @lombok.RequiredArgsConstructor
public class UsuarioDetailsService implements UserDetailsService {
private final UsuarioRepository usuarios;
public UserDetails loadUserByUsername(String username) {
var u=usuarios.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("Credenciales inválidas"));
var roles=u.getRoles().stream().map(r->r.getNombre()).collect(java.util.stream.Collectors.toSet());
return new User(u.getUsername(),u.getPassword(),Boolean.TRUE.equals(u.getActivo()),true,true,true,PermisosPorRol.permisos(roles).stream().map(SimpleGrantedAuthority::new).toList()); }
}
