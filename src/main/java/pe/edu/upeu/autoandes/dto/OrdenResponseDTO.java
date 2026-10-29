package pe.edu.upeu.autoandes.dto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public record OrdenResponseDTO(Long id, String numero, java.time.LocalDateTime fecha, Long vehiculoId, String placa, String clienteNombre, Integer kilometraje, String estado, BigDecimal total, String usuarioRegistro, String motivoAnulacion, java.util.List<DetalleOrdenResponseDTO> detalles) {}
