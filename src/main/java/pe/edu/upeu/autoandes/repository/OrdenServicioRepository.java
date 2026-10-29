package pe.edu.upeu.autoandes.repository;
import pe.edu.upeu.autoandes.entity.OrdenServicio;
public interface OrdenServicioRepository extends org.springframework.data.jpa.repository.JpaRepository<OrdenServicio,Long>, org.springframework.data.jpa.repository.JpaSpecificationExecutor<OrdenServicio> { boolean existsByVehiculoId(Long id);
java.util.Optional<OrdenServicio> findFirstByVehiculoIdAndEstadoOrderByFechaDescIdDesc(Long id,String estado);
@org.springframework.data.jpa.repository.Query(value="select orden_numero_seq.nextval from dual",nativeQuery=true) Long siguienteNumero();
@org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
@org.springframework.data.jpa.repository.Query("select o from OrdenServicio o where o.id = :id")
java.util.Optional<OrdenServicio> bloquear(@org.springframework.data.repository.query.Param("id") Long id); }
