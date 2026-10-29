package pe.edu.upeu.autoandes.controller;
import pe.edu.upeu.autoandes.dto.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.validation.Valid;
@RestController @RequestMapping("/api/v1/ordenes") @lombok.RequiredArgsConstructor
public class OrdenController { private final pe.edu.upeu.autoandes.service.service.OrdenService servicio;
@PostMapping @PreAuthorize("hasAuthority('ORDEN_REGISTRAR')") public ResponseEntity<OrdenResponseDTO> crear(@Valid @RequestBody OrdenRequestDTO r,java.security.Principal p) {var o=servicio.registrar(r,p.getName());return ResponseEntity.created(java.net.URI.create("/api/v1/ordenes/"+o.id())).body(o); }
@GetMapping @PreAuthorize("hasAuthority('ORDEN_LEER')") public java.util.List<OrdenResponseDTO> listar(@RequestParam(required=false) String placa,@RequestParam(required=false) String estado,@RequestParam(required=false) java.time.LocalDate desde,@RequestParam(required=false) java.time.LocalDate hasta) { return servicio.listar(placa,estado,desde,hasta); }
@GetMapping("/{id}") @PreAuthorize("hasAuthority('ORDEN_LEER')") public OrdenResponseDTO obtener(@PathVariable Long id) { return servicio.obtener(id); }
@PatchMapping("/{id}/anular") @PreAuthorize("hasAuthority('ORDEN_ANULAR')") public OrdenResponseDTO anular(@PathVariable Long id,@Valid @RequestBody AnularRequestDTO r) { return servicio.anular(id,r.motivo()); }
}
