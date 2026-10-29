package pe.edu.upeu.autoandes.repository;
import pe.edu.upeu.autoandes.entity.DetalleOrden;
public interface DetalleOrdenRepository extends org.springframework.data.jpa.repository.JpaRepository<DetalleOrden,Long> { boolean existsByServicioId(Long id);
@org.springframework.data.jpa.repository.Query("""
select new pe.edu.upeu.autoandes.dto.IngresoServicioDTO(s.codigo,s.nombre,sum(d.cantidad),sum(d.subtotal))
from DetalleOrden d join d.servicio s join d.orden o
where o.estado = 'REGISTRADA' and o.fecha >= :desde and o.fecha < :hasta
group by s.codigo,s.nombre order by sum(d.subtotal) desc
""")
java.util.List<pe.edu.upeu.autoandes.dto.IngresoServicioDTO> ingresos(
@org.springframework.data.repository.query.Param("desde") java.time.LocalDateTime desde,
@org.springframework.data.repository.query.Param("hasta") java.time.LocalDateTime hasta); }
