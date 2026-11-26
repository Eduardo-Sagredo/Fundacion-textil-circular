package cl.textilcircular_api.controller;

import cl.textilcircular_api.dto.AdminSolicitudDTO;
import cl.textilcircular_api.dto.RechazoRequestDTO;
import cl.textilcircular_api.service.AdminService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;


    @GetMapping("/solicitudes")
    public ResponseEntity<List<AdminSolicitudDTO>>
    listarSolicitudes() {

        return ResponseEntity.ok(
                adminService.listarSolicitudes()
        );
    }


    @PutMapping("/aprobar/{id}")
    public ResponseEntity<AdminSolicitudDTO>
    aprobarSolicitud(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                adminService.aprobarSolicitud(id)
        );
    }


    @PutMapping("/rechazar/{id}")
    public ResponseEntity<AdminSolicitudDTO>
    rechazarSolicitud(

            @PathVariable Long id,

            @Valid
            @RequestBody
            RechazoRequestDTO request
    ) {

        return ResponseEntity.ok(
                adminService.rechazarSolicitud(
                        id,
                        request.getMotivo()
                )
        );
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<AdminSolicitudDTO>>
    buscarUsuarios(
            @RequestParam(defaultValue = "") String nombre
    ) {

        return ResponseEntity.ok(
                adminService.buscarUsuariosPorNombre(nombre)
        );
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Void>
    eliminarUsuario(
            @PathVariable Long id
    ) {

        adminService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}