package pe.edu.upeu.autoandes.controller;
import pe.edu.upeu.autoandes.dto.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.validation.Valid;
@RestController @RequestMapping("/api/v1/servicios") @lombok.RequiredArgsConstructor
public class ServicioController { private final pe.edu.upeu.autoandes.service.service.CatalogoService servicio;
@GetMapping @PreAuthorize("hasAuthority('SERVICIO_LEER')") public java.util.List<ServicioResponseDTO> listar() { return servicio.servicios(); }
@GetMapping("/{id}") @PreAuthorize("hasAuthority('SERVICIO_LEER')") public ServicioResponseDTO obtener(@PathVariable Long id) { return servicio.servicio(id); }
@PostMapping @PreAuthorize("hasAuthority('SERVICIO_ESCRIBIR')") public ResponseEntity<ServicioResponseDTO> crear(@Valid @RequestBody ServicioRequestDTO r) { var creado=servicio.guardarServicio(null,r);return ResponseEntity.created(java.net.URI.create("/api/v1/servicios/"+creado.id())).body(creado); }
@PutMapping("/{id}") @PreAuthorize("hasAuthority('SERVICIO_ESCRIBIR')") public ServicioResponseDTO editar(@PathVariable Long id,@Valid @RequestBody ServicioRequestDTO r) { return servicio.guardarServicio(id,r); }
@DeleteMapping("/{id}") @PreAuthorize("hasAuthority('SERVICIO_ESCRIBIR')") public ResponseEntity<Void> eliminar(@PathVariable Long id) { servicio.eliminarServicio(id);return ResponseEntity.noContent().build(); }
@PatchMapping("/{id}/estado") @PreAuthorize("hasAuthority('SERVICIO_ESCRIBIR')") public ServicioResponseDTO estado(@PathVariable Long id,@Valid @RequestBody EstadoRequestDTO r) { return servicio.estadoServicio(id,r.activo()); }
}
