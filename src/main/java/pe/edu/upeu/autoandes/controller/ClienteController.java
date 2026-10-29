package pe.edu.upeu.autoandes.controller;
import pe.edu.upeu.autoandes.dto.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.validation.Valid;
@RestController @RequestMapping("/api/v1/clientes") @lombok.RequiredArgsConstructor
public class ClienteController { private final pe.edu.upeu.autoandes.service.service.CatalogoService servicio;
@GetMapping @PreAuthorize("hasAuthority('VEHICULO_LEER')") public java.util.List<ClienteResponseDTO> listar() { return servicio.clientes(); }
}
