package pe.edu.upeu.autoandes.service.service;
import pe.edu.upeu.autoandes.dto.*;
import java.time.LocalDate;
import java.util.List;
public interface OrdenService { OrdenResponseDTO registrar(OrdenRequestDTO r,String usuario); OrdenResponseDTO anular(Long id,String motivo); OrdenResponseDTO obtener(Long id); List<OrdenResponseDTO> listar(String placa,String estado,LocalDate desde,LocalDate hasta); List<IngresoServicioDTO> ingresos(LocalDate desde,LocalDate hasta); }
