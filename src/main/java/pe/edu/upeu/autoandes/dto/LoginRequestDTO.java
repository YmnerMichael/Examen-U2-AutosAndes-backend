package pe.edu.upeu.autoandes.dto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public record LoginRequestDTO(@NotBlank String username, @NotBlank String password) {}
