package pe.edu.upeu.autoandes.repository;
import pe.edu.upeu.autoandes.entity.Usuario;
public interface UsuarioRepository extends org.springframework.data.jpa.repository.JpaRepository<Usuario,Long> { java.util.Optional<Usuario> findByUsernameIgnoreCase(String username); }
