package pe.edu.upeu.autoandes.config;
import pe.edu.upeu.autoandes.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
@org.springframework.stereotype.Component @lombok.RequiredArgsConstructor
public class PasswordSeed implements org.springframework.boot.CommandLineRunner {
private final UsuarioRepository usuarios;private final PasswordEncoder encoder;
@Override @org.springframework.transaction.annotation.Transactional public void run(String... args) { for(var u:usuarios.findAll()) { if(u.getPassword()==null||u.getPassword().isBlank()) { u.setPassword(encoder.encode("Auto2026*"));usuarios.save(u); } } }
}
