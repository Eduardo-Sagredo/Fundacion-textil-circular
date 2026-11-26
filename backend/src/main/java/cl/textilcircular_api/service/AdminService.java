package cl.textilcircular_api.service;

import cl.textilcircular_api.dto.AdminSolicitudDTO;
import cl.textilcircular_api.model.Emprendedora;
import cl.textilcircular_api.repository.EmprendedoraRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final EmprendedoraRepository emprendedoraRepository;


    public List<AdminSolicitudDTO> listarSolicitudes() {

        return emprendedoraRepository
                .findByEstado("PENDIENTE")
                .stream()
                .map(this::convertirADTO)
                .toList();
    }


    public AdminSolicitudDTO aprobarSolicitud(Long id) {

        Emprendedora emprendedora =
                buscarPorId(id);

        emprendedora.setEstado("APROBADA");
        emprendedora.setMotivoRechazo(null);

        Emprendedora guardada =
                emprendedoraRepository.save(emprendedora);

        return convertirADTO(guardada);
    }


    public AdminSolicitudDTO rechazarSolicitud(
            Long id,
            String motivo
    ) {

        Emprendedora emprendedora =
                buscarPorId(id);

        emprendedora.setEstado("RECHAZADA");
        emprendedora.setMotivoRechazo(
                motivo.trim()
        );

        Emprendedora guardada =
                emprendedoraRepository.save(emprendedora);

        return convertirADTO(guardada);
    }


    public List<AdminSolicitudDTO> buscarUsuariosPorNombre(
            String nombre
    ) {

        String filtro =
                nombre == null ? "" : nombre.trim().toLowerCase();

        return emprendedoraRepository
                .findAll()
                .stream()
                .filter(e -> !"ADMIN".equalsIgnoreCase(e.getRol()))
                .filter(e -> filtro.isEmpty() ||
                        (e.getNombre() != null &&
                                e.getNombre().toLowerCase().contains(filtro)))
                .map(this::convertirADTO)
                .toList();
    }

    public void eliminarUsuario(Long id) {

        Emprendedora emprendedora =
                buscarPorId(id);

        if ("ADMIN".equalsIgnoreCase(emprendedora.getRol())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No se puede eliminar una cuenta de administrador"
            );
        }

        emprendedoraRepository.delete(emprendedora);
    }

    private Emprendedora buscarPorId(Long id) {

        return emprendedoraRepository
                .findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Usuario no encontrado"
                        )
                );
    }

    private AdminSolicitudDTO convertirADTO(
            Emprendedora emprendedora
    ) {

        return new AdminSolicitudDTO(
                emprendedora.getId(),
                emprendedora.getRut(),
                emprendedora.getNombre(),
                emprendedora.getCorreo(),
                emprendedora.getNombreEmprendimiento(),
                emprendedora.getRubro(),
                emprendedora.getRegion(),
                emprendedora.getComuna(),
                emprendedora.getEdad(),
                emprendedora.getGenero(),
                emprendedora.getRol(),
                emprendedora.getEstado(),
                emprendedora.getMotivoRechazo()
        );
    }
}