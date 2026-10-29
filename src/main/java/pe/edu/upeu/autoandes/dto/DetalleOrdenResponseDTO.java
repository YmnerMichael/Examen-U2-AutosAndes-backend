package pe.edu.upeu.autoandes.dto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public record DetalleOrdenResponseDTO(Long id, Long servicioId, String codigo, String servicio, Integer cantidad, BigDecimal precioUnitario, BigDecimal subtotal) {}
