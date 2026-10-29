package pe.edu.upeu.autoandes.service.impl;
import pe.edu.upeu.autoandes.dto.*;
import pe.edu.upeu.autoandes.repository.UsuarioRepository;
import pe.edu.upeu.autoandes.config.JwtService;
import pe.edu.upeu.autoandes.security.PermisosPorRol;
import pe.edu.upeu.autoandes.exception.DominioException;
import org.springframework.security.crypto.password.PasswordEncoder;
@org.springframework.stereotype.Service @lombok.RequiredArgsConstructor
@org.springframework.transaction.annotation.Transactional(readOnly=true)
public class AuthServiceImpl implements pe.edu.upeu.autoandes.service.service.AuthService {
private final UsuarioRepository usuarios;private final PasswordEncoder encoder;private final JwtService jwt;
private final String dummy=new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode("dummy");
public LoginResponseDTO login(LoginRequestDTO r) {
var u=usuarios.findByUsernameIgnoreCase(r.username()).orElse(null);boolean coincide=encoder.matches(r.password(),u==null?dummy:u.getPassword());
if(u==null||!coincide||!Boolean.TRUE.equals(u.getActivo())) throw new DominioException(401,"Credenciales inválidas");
var me=actual(u.getUsername());return new LoginResponseDTO(jwt.crear(me.username(),me.roles(),me.permisos()),"Bearer",jwt.expiraEnSegundos(),me.nombreCompleto(),me.roles(),me.permisos()); }
public UsuarioResponseDTO actual(String username) { var u=usuarios.findByUsernameIgnoreCase(username).orElseThrow(()->new DominioException(401,"Credenciales inválidas"));var roles=u.getRoles().stream().map(r->r.getNombre()).collect(java.util.stream.Collectors.toSet());return new UsuarioResponseDTO(u.getUsername(),u.getNombreCompleto(),roles,PermisosPorRol.permisos(roles)); }
}
