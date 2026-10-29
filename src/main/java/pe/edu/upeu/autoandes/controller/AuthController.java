package pe.edu.upeu.autoandes.controller;
import pe.edu.upeu.autoandes.dto.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.validation.Valid;
@RestController @RequestMapping("/api/v1/auth") @lombok.RequiredArgsConstructor
public class AuthController { private final pe.edu.upeu.autoandes.service.service.AuthService servicio;
@PostMapping("/login") public LoginResponseDTO login(@Valid @RequestBody LoginRequestDTO r) { return servicio.login(r); }
@GetMapping("/me") public UsuarioResponseDTO me(java.security.Principal p) { return servicio.actual(p.getName()); }
}
