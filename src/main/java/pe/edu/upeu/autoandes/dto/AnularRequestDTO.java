package pe.edu.upeu.autoandes.dto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public record AnularRequestDTO(@NotBlank @Size(min=10,max=200) String motivo) {}
