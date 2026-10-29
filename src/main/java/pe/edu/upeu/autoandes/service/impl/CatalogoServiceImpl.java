package pe.edu.upeu.autoandes.service.impl;
import pe.edu.upeu.autoandes.dto.*;
import pe.edu.upeu.autoandes.entity.*;
import pe.edu.upeu.autoandes.repository.*;
import pe.edu.upeu.autoandes.mapper.AutoAndesMapper;
import pe.edu.upeu.autoandes.service.service.CatalogoService;
import static pe.edu.upeu.autoandes.exception.DominioException.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service @Transactional(readOnly=true) @lombok.RequiredArgsConstructor @lombok.extern.slf4j.Slf4j
public class CatalogoServiceImpl implements CatalogoService {
private final ClienteRepository clientes; private final ServicioRepository servicios; private final VehiculoRepository vehiculos;
private final OrdenServicioRepository ordenes; private final DetalleOrdenRepository detalles; private final AutoAndesMapper mapper;
public List<ClienteResponseDTO> clientes() { return clientes.findAll(org.springframework.data.domain.Sort.by("nombres")).stream().map(mapper::cliente).toList(); }
public List<ServicioResponseDTO> servicios() { return servicios.findAll(org.springframework.data.domain.Sort.by("codigo")).stream().map(mapper::servicio).toList(); }
public ServicioResponseDTO servicio(Long id) { return mapper.servicio(buscarServicio(id)); }
private Servicio buscarServicio(Long id) { return servicios.findById(id).orElseThrow(()->noExiste("Servicio")); }
private Vehiculo buscarVehiculo(Long id) { return vehiculos.findById(id).orElseThrow(()->noExiste("Vehículo")); }
@Transactional public ServicioResponseDTO guardarServicio(Long id,ServicioRequestDTO r) {
Servicio s=id==null?new Servicio():buscarServicio(id);
if(id==null?servicios.existsByCodigo(r.codigo()):servicios.existsByCodigoAndIdNot(r.codigo(),id)) throw conflicto("El código de servicio ya existe");
mapper.servicio(r,s); servicios.saveAndFlush(s); log.info("Servicio guardado: {}",s.getId());return mapper.servicio(s); }
@Transactional public void eliminarServicio(Long id) { Servicio s=buscarServicio(id);if(detalles.existsByServicioId(id)) throw conflicto("El servicio tiene órdenes: use la baja mediante cambio de estado"); servicios.delete(s);log.info("Servicio eliminado: {}",id); }
@Transactional public ServicioResponseDTO estadoServicio(Long id,Boolean activo) { var s=buscarServicio(id);s.setActivo(activo);log.info("Estado servicio {}: {}",id,activo);return mapper.servicio(s); }
public List<VehiculoResponseDTO> vehiculos(Long clienteId) { if(clienteId!=null&&!clientes.existsById(clienteId)) throw noExiste("Cliente");return (clienteId==null?vehiculos.findAll(org.springframework.data.domain.Sort.by("placa")):vehiculos.findByClienteIdOrderByPlacaAsc(clienteId)).stream().map(mapper::vehiculo).toList(); }
public VehiculoResponseDTO vehiculo(Long id) { return mapper.vehiculo(buscarVehiculo(id)); }
@Transactional public VehiculoResponseDTO guardarVehiculo(Long id,VehiculoRequestDTO r) { var v=id==null?new Vehiculo():buscarVehiculo(id);
if(id==null?vehiculos.existsByPlaca(r.placa()):vehiculos.existsByPlacaAndIdNot(r.placa(),id)) throw conflicto("La placa ya existe");
var c=clientes.findById(r.clienteId()).orElseThrow(()->noExiste("Cliente"));mapper.vehiculo(r,v,c);vehiculos.saveAndFlush(v);log.info("Vehículo guardado: {}",v.getId());return mapper.vehiculo(v); }
@Transactional public void eliminarVehiculo(Long id) { var v=vehiculos.bloquear(id).orElseThrow(()->noExiste("Vehículo")); if(ordenes.existsByVehiculoId(id)) throw conflicto("El vehículo tiene órdenes registradas"); vehiculos.delete(v);log.info("Vehículo eliminado: {}",id); }
}
