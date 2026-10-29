package pe.edu.upeu.autoandes.dto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public record UsuarioResponseDTO(String username, String nombreCompleto, java.util.Set<String> roles, java.util.Set<String> permisos) {}
