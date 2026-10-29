package pe.edu.upeu.autoandes.dto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public record ClienteResponseDTO(Long id, String documento, String nombres, String telefono) {}
