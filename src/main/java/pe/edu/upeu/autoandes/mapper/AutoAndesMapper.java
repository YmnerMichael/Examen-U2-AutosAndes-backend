package pe.edu.upeu.autoandes.mapper;
import pe.edu.upeu.autoandes.entity.*;
import pe.edu.upeu.autoandes.dto.*;
@org.springframework.stereotype.Component
public class AutoAndesMapper {
public ClienteResponseDTO cliente(Cliente c) { return new ClienteResponseDTO(c.getId(),c.getDocumento(),c.getNombres(),c.getTelefono()); }
public ServicioResponseDTO servicio(Servicio s) { return new ServicioResponseDTO(s.getId(),s.getCodigo(),s.getNombre(),s.getPrecio(),s.getDuracionMin(),s.getActivo()); }
public void servicio(ServicioRequestDTO r,Servicio s) { s.setCodigo(r.codigo());s.setNombre(r.nombre());s.setPrecio(r.precio());s.setDuracionMin(r.duracionMin());s.setActivo(r.activo()); }
public VehiculoResponseDTO vehiculo(Vehiculo v) { return new VehiculoResponseDTO(v.getId(),v.getPlaca(),v.getMarca(),v.getModelo(),v.getAnio(),v.getCliente().getId(),v.getCliente().getNombres()); }
public void vehiculo(VehiculoRequestDTO r,Vehiculo v,Cliente c) { v.setPlaca(r.placa());v.setMarca(r.marca());v.setModelo(r.modelo());v.setAnio(r.anio());v.setCliente(c); }
public OrdenResponseDTO orden(OrdenServicio o) { return new OrdenResponseDTO(o.getId(),o.getNumero(),o.getFecha(),o.getVehiculo().getId(),o.getVehiculo().getPlaca(),o.getVehiculo().getCliente().getNombres(),o.getKilometraje(),o.getEstado(),o.getTotal(),o.getUsuarioRegistro(),o.getMotivoAnulacion(),o.getDetalles().stream().map(d->new DetalleOrdenResponseDTO(d.getId(),d.getServicio().getId(),d.getServicio().getCodigo(),d.getServicio().getNombre(),d.getCantidad(),d.getPrecioUnitario(),d.getSubtotal())).toList()); }
}
