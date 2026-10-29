package pe.edu.upeu.autoandes.exception;
import lombok.Getter;
@Getter public class DominioException extends RuntimeException {
private final int status;
public DominioException(int status,String mensaje) { super(mensaje); this.status=status; }
public static DominioException noExiste(String recurso) { return new DominioException(404,recurso+" no encontrado"); }
public static DominioException conflicto(String mensaje) { return new DominioException(409,mensaje); }
public static DominioException invalido(String mensaje) { return new DominioException(400,mensaje); }
}
