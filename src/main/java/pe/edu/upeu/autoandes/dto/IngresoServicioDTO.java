package pe.edu.upeu.autoandes.dto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public record IngresoServicioDTO(String codigo, String servicio, Long cantidadTotal, BigDecimal importeTotal) {}
