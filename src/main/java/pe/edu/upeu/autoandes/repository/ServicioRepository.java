package pe.edu.upeu.autoandes.repository;
import pe.edu.upeu.autoandes.entity.Servicio;
public interface ServicioRepository extends org.springframework.data.jpa.repository.JpaRepository<Servicio,Long> { boolean existsByCodigo(String codigo); boolean existsByCodigoAndIdNot(String codigo,Long id); }
