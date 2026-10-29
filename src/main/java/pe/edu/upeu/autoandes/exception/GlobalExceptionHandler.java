package pe.edu.upeu.autoandes.exception;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import jakarta.servlet.http.HttpServletRequest;
import pe.edu.upeu.autoandes.dto.ErrorResponseDTO;
@RestControllerAdvice @lombok.extern.slf4j.Slf4j
public class GlobalExceptionHandler {
public static ErrorResponseDTO error(int s,String mensaje,String ruta,java.util.Map<String,String> campos) {
return new ErrorResponseDTO(java.time.LocalDateTime.now().toString(),s,HttpStatus.valueOf(s).getReasonPhrase(),mensaje,ruta,campos); }
@ExceptionHandler(DominioException.class) public ResponseEntity<ErrorResponseDTO> dominio(DominioException e,HttpServletRequest r) {
log.warn("Regla rechazada: {}",e.getMessage()); return ResponseEntity.status(e.getStatus()).body(error(e.getStatus(),e.getMessage(),r.getRequestURI(),null)); }
@ExceptionHandler(MethodArgumentNotValidException.class) public ResponseEntity<ErrorResponseDTO> validacion(MethodArgumentNotValidException e,HttpServletRequest r) {
var campos=new java.util.LinkedHashMap<String,String>(); e.getBindingResult().getFieldErrors().forEach(f->campos.put(f.getField(),f.getDefaultMessage()));
return ResponseEntity.badRequest().body(error(400,"Datos inválidos",r.getRequestURI(),campos)); }
@ExceptionHandler({org.springframework.http.converter.HttpMessageNotReadableException.class, org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class, org.springframework.web.bind.MissingServletRequestParameterException.class})
public ResponseEntity<ErrorResponseDTO> formato(Exception e,HttpServletRequest r) { return ResponseEntity.badRequest().body(error(400,"Formato de solicitud inválido",r.getRequestURI(),null)); }
@ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class) public ResponseEntity<ErrorResponseDTO> integridad(Exception e,HttpServletRequest r) { log.warn("Conflicto de integridad en {}",r.getRequestURI()); return ResponseEntity.status(409).body(error(409,"Registro duplicado o relacionado con otros registros",r.getRequestURI(),null)); }
@ExceptionHandler(org.springframework.security.access.AccessDeniedException.class) public ResponseEntity<ErrorResponseDTO> denegado(Exception e,HttpServletRequest r) { return ResponseEntity.status(403).body(error(403,"No tiene permiso para esta operación",r.getRequestURI(),null)); }
@ExceptionHandler(org.springframework.web.servlet.resource.NoResourceFoundException.class) public ResponseEntity<ErrorResponseDTO> ausente(Exception e,HttpServletRequest r) { return ResponseEntity.status(404).body(error(404,"Ruta no encontrada",r.getRequestURI(),null)); }
@ExceptionHandler(Exception.class) public ResponseEntity<ErrorResponseDTO> inesperado(Exception e,HttpServletRequest r) { log.error("Error inesperado",e); return ResponseEntity.internalServerError().body(error(500,"Error interno del servidor",r.getRequestURI(),null)); }
}
