package pe.edu.upeu.autoandes.service.impl;
import pe.edu.upeu.autoandes.dto.*;
import pe.edu.upeu.autoandes.entity.*;
import pe.edu.upeu.autoandes.repository.*;
import pe.edu.upeu.autoandes.mapper.AutoAndesMapper;
import pe.edu.upeu.autoandes.service.service.OrdenService;
import static pe.edu.upeu.autoandes.exception.DominioException.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.domain.Specification;
import java.time.*; import java.math.BigDecimal; import java.util.*;
@Service @Transactional(readOnly=true) @lombok.RequiredArgsConstructor @lombok.extern.slf4j.Slf4j
public class OrdenServiceImpl implements OrdenService {
private final OrdenServicioRepository ordenes; private final VehiculoRepository vehiculos; private final ServicioRepository servicios; private final DetalleOrdenRepository detalles; private final AutoAndesMapper mapper; private final Clock clock;
@Transactional public OrdenResponseDTO registrar(OrdenRequestDTO r,String usuario) {
if(r.detalles()==null||r.detalles().isEmpty()||r.detalles().size()>10) throw invalido("La orden requiere entre 1 y 10 detalles");
var v=vehiculos.bloquear(r.vehiculoId()).orElseThrow(()->noExiste("Vehículo"));
var anterior=ordenes.findFirstByVehiculoIdAndEstadoOrderByFechaDescIdDesc(v.getId(),"REGISTRADA");
if(anterior.isPresent()&&r.kilometraje()<anterior.get().getKilometraje()) throw conflicto("El kilometraje no puede ser menor que el último: "+anterior.get().getKilometraje());
var o=new OrdenServicio();o.setVehiculo(v);o.setFecha(LocalDateTime.now(clock));o.setEstado("REGISTRADA");o.setKilometraje(r.kilometraje());o.setUsuarioRegistro(usuario);o.setTotal(BigDecimal.ZERO);
long numero=ordenes.siguienteNumero();if(numero>999999) throw conflicto("Se agotó el correlativo de órdenes");o.setNumero(String.format("OS-%06d",numero));
// The initial insert and every detail participate in this same service transaction.
ordenes.saveAndFlush(o);var usados=new HashSet<Long>();
for(var item:r.detalles()) {
if(!usados.add(item.servicioId())) throw invalido("No se puede repetir un servicio");
if(item.cantidad()==null||item.cantidad()<1||item.cantidad()>5) throw invalido("Cantidad entre 1 y 5");
var s=servicios.findById(item.servicioId()).orElseThrow(()->noExiste("Servicio"));if(!Boolean.TRUE.equals(s.getActivo())) throw conflicto("El servicio "+s.getCodigo()+" está inactivo");
var d=new DetalleOrden();d.setOrden(o);d.setServicio(s);d.setCantidad(item.cantidad());d.setPrecioUnitario(s.getPrecio());d.setSubtotal(s.getPrecio().multiply(BigDecimal.valueOf(item.cantidad())));o.getDetalles().add(d);o.setTotal(o.getTotal().add(d.getSubtotal()));
}
ordenes.saveAndFlush(o);log.info("Orden registrada: {} total {}",o.getNumero(),o.getTotal());return mapper.orden(o); }
public OrdenResponseDTO obtener(Long id) { return mapper.orden(ordenes.findById(id).orElseThrow(()->noExiste("Orden"))); }
@Transactional public OrdenResponseDTO anular(Long id,String motivo) {
if(motivo==null||motivo.isBlank()||motivo.length()<10||motivo.length()>200) throw invalido("El motivo requiere entre 10 y 200 caracteres");
var o=ordenes.bloquear(id).orElseThrow(()->noExiste("Orden"));if(!o.getEstado().equals("REGISTRADA")) throw conflicto("Solo se anula una orden REGISTRADA");
if(!o.getFecha().toLocalDate().equals(LocalDate.now(clock))) throw conflicto("Solo se puede anular el mismo día del registro");
o.setEstado("ANULADA");o.setMotivoAnulacion(motivo);log.info("Orden anulada: {}",o.getNumero());return mapper.orden(o); }
public List<OrdenResponseDTO> listar(String placa,String estado,LocalDate desde,LocalDate hasta) {
validarFechas(desde,hasta);if(estado!=null&&!Set.of("REGISTRADA","ANULADA").contains(estado)) throw invalido("Estado inválido");
Specification<OrdenServicio> filtro=(root,q,cb)-> {
var p=new ArrayList<jakarta.persistence.criteria.Predicate>();
if(placa!=null&&!placa.isBlank()) p.add(cb.like(cb.upper(root.get("vehiculo").get("placa")),"%"+placa.toUpperCase()+"%",'\\'));
if(estado!=null) p.add(cb.equal(root.get("estado"),estado));if(desde!=null) p.add(cb.greaterThanOrEqualTo(root.get("fecha"),desde.atStartOfDay()));if(hasta!=null) p.add(cb.lessThan(root.get("fecha"),hasta.plusDays(1).atStartOfDay()));return cb.and(p.toArray(jakarta.persistence.criteria.Predicate[]::new)); };
return ordenes.findAll(filtro,org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC,"fecha","id")).stream().map(mapper::orden).toList(); }
public List<IngresoServicioDTO> ingresos(LocalDate desde,LocalDate hasta) { validarFechas(desde,hasta);return detalles.ingresos((desde==null?LocalDate.of(1900,1,1):desde).atStartOfDay(),(hasta==null?LocalDate.of(9998,12,31):hasta).plusDays(1).atStartOfDay()); }
private void validarFechas(LocalDate desde,LocalDate hasta) { if(desde!=null&&hasta!=null&&desde.isAfter(hasta)) throw invalido("Desde no puede ser posterior a hasta"); }
}
