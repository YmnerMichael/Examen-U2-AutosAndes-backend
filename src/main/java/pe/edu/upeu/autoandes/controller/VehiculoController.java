package pe.edu.upeu.autoandes.controller;
import pe.edu.upeu.autoandes.dto.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.validation.Valid;
@RestController @RequestMapping("/api/v1/vehiculos") @lombok.RequiredArgsConstructor
public class VehiculoController { private final pe.edu.upeu.autoandes.service.service.CatalogoService servicio;
@GetMapping @PreAuthorize("hasAuthority('VEHICULO_LEER')") public java.util.List<VehiculoResponseDTO> listar(@RequestParam(required=false) Long clienteId) { return servicio.vehiculos(clienteId); }
@GetMapping("/{id}") @PreAuthorize("hasAuthority('VEHICULO_LEER')") public VehiculoResponseDTO obtener(@PathVariable Long id) { return servicio.vehiculo(id); }
@PostMapping @PreAuthorize("hasAuthority('VEHICULO_ESCRIBIR')") public ResponseEntity<VehiculoResponseDTO> crear(@Valid @RequestBody VehiculoRequestDTO r) { var creado=servicio.guardarVehiculo(null,r);return ResponseEntity.created(java.net.URI.create("/api/v1/vehiculos/"+creado.id())).body(creado); }
@PutMapping("/{id}") @PreAuthorize("hasAuthority('VEHICULO_ESCRIBIR')") public VehiculoResponseDTO editar(@PathVariable Long id,@Valid @RequestBody VehiculoRequestDTO r) { return servicio.guardarVehiculo(id,r); }
@DeleteMapping("/{id}") @PreAuthorize("hasAuthority('VEHICULO_ESCRIBIR')") public ResponseEntity<Void> eliminar(@PathVariable Long id) { servicio.eliminarVehiculo(id);return ResponseEntity.noContent().build(); }
}
