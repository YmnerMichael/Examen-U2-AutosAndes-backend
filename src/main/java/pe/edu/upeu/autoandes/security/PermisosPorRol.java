package pe.edu.upeu.autoandes.security;
import java.util.*;
public final class PermisosPorRol {
private PermisosPorRol() {}
private static final Map<String,Set<Permiso>> MATRIZ=Map.of(
"ADMIN",Set.of(Permiso.values()),
"ASESOR",Set.of(Permiso.SERVICIO_LEER,Permiso.VEHICULO_LEER,Permiso.VEHICULO_ESCRIBIR,Permiso.ORDEN_LEER,Permiso.ORDEN_REGISTRAR),
"JEFE_TALLER",Set.of(Permiso.SERVICIO_LEER,Permiso.SERVICIO_ESCRIBIR,Permiso.VEHICULO_LEER,Permiso.ORDEN_LEER,Permiso.ORDEN_ANULAR,Permiso.REPORTE_VER));
public static Set<String> permisos(Set<String> roles) { var r=new TreeSet<String>();roles.forEach(rol->MATRIZ.getOrDefault(rol,Set.of()).forEach(p->r.add(p.name())));return r; }
}
