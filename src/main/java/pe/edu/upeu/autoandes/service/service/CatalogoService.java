package pe.edu.upeu.autoandes.service.service;
import pe.edu.upeu.autoandes.dto.*;
import java.util.List;
public interface CatalogoService {
List<ClienteResponseDTO> clientes(); List<ServicioResponseDTO> servicios(); ServicioResponseDTO servicio(Long id);
ServicioResponseDTO guardarServicio(Long id,ServicioRequestDTO r); void eliminarServicio(Long id); ServicioResponseDTO estadoServicio(Long id,Boolean activo);
List<VehiculoResponseDTO> vehiculos(Long clienteId); VehiculoResponseDTO vehiculo(Long id); VehiculoResponseDTO guardarVehiculo(Long id,VehiculoRequestDTO r); void eliminarVehiculo(Long id);
}
