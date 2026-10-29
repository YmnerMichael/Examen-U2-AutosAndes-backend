package pe.edu.upeu.autoandes.controller;
import pe.edu.upeu.autoandes.dto.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.validation.Valid;
@RestController @RequestMapping("/api/v1/reportes") @lombok.RequiredArgsConstructor
public class ReporteController { private final pe.edu.upeu.autoandes.service.service.OrdenService servicio;
@GetMapping("/ingresos-por-servicio") @PreAuthorize("hasAuthority('REPORTE_VER')") public java.util.List<IngresoServicioDTO> ingresos(@RequestParam(required=false) java.time.LocalDate desde,@RequestParam(required=false) java.time.LocalDate hasta) { return servicio.ingresos(desde,hasta); }
}
