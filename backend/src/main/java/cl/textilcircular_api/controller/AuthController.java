package cl.textilcircular_api.controller;

import cl.textilcircular_api.dto.CambiarContrasenaRequestDTO;
import cl.textilcircular_api.dto.LoginRequestDTO;
import cl.textilcircular_api.dto.LoginResponseDTO;
import cl.textilcircular_api.dto.OlvideContrasenaRequestDTO;
import cl.textilcircular_api.dto.PerfilResponseDTO;
import cl.textilcircular_api.dto.RegistroRequestDTO;
import cl.textilcircular_api.dto.RestablecerContrasenaRequestDTO;
import cl.textilcircular_api.model.Emprendedora;
import cl.textilcircular_api.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // =========================================
    // REGISTRO
    // =========================================

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(
            @Valid @RequestBody RegistroRequestDTO request
    ) {

        try {

            Emprendedora emprendedora =
                    authService.registrar(request);

            Map<String, Object> respuesta =
                    new HashMap<>();

            respuesta.put(
                    "mensaje",
                    "Cuenta creada correctamente"
            );

            respuesta.put(
                    "id",
                    emprendedora.getId()
            );

            respuesta.put(
                    "correo",
                    emprendedora.getCorreo()
            );

            respuesta.put(
                    "estado",
                    emprendedora.getEstado()
            );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(respuesta);

        } catch (IllegalArgumentException e) {

            Map<String, String> error =
                    new HashMap<>();

            error.put(
                    "mensaje",
                    e.getMessage()
            );

            return ResponseEntity
                    .badRequest()
                    .body(error);
        }
    }

    // =========================================
    // LOGIN
    // =========================================

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequestDTO request
    ) {

        try {

            LoginResponseDTO respuesta =
                    authService.login(request);

            return ResponseEntity.ok(respuesta);

        } catch (IllegalArgumentException e) {

            Map<String, String> error =
                    new HashMap<>();

            error.put(
                    "mensaje",
                    e.getMessage()
            );

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(error);
        }
    }

    // =========================================
    // PERFIL
    // =========================================

    @GetMapping("/perfil/{correo}")
    public ResponseEntity<?> obtenerPerfil(
            @PathVariable String correo
    ) {

        try {

            PerfilResponseDTO perfil =
                    authService.obtenerPerfil(correo);

            return ResponseEntity.ok(perfil);

        } catch (IllegalArgumentException e) {

            Map<String, String> error =
                    new HashMap<>();

            error.put(
                    "mensaje",
                    e.getMessage()
            );

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(error);
        }
    }

    // =========================================
    // CAMBIAR CONTRASEÑA
    // =========================================

    @PutMapping("/cambiar-contrasena")
    public ResponseEntity<?> cambiarContrasena(
            @Valid @RequestBody CambiarContrasenaRequestDTO request
    ) {

        try {

            authService.cambiarContrasena(request);

            Map<String, String> respuesta =
                    new HashMap<>();

            respuesta.put(
                    "mensaje",
                    "Contraseña actualizada correctamente"
            );

            return ResponseEntity.ok(respuesta);

        } catch (IllegalArgumentException e) {

            Map<String, String> error =
                    new HashMap<>();

            error.put(
                    "mensaje",
                    e.getMessage()
            );

            return ResponseEntity
                    .badRequest()
                    .body(error);
        }
    }

    // =========================================
    // OLVIDÉ MI CONTRASEÑA
    // =========================================

    @PostMapping("/olvide-contrasena")
    public ResponseEntity<?> olvideContrasena(
            @RequestBody OlvideContrasenaRequestDTO request
    ) {

        try {

            authService.olvideContrasena(request);

            Map<String, String> respuesta =
                    new HashMap<>();

            respuesta.put(
                    "mensaje",
                    "Se envió un código de recuperación a tu correo"
            );

            return ResponseEntity.ok(respuesta);

        } catch (IllegalArgumentException e) {

            Map<String, String> error =
                    new HashMap<>();

            error.put(
                    "mensaje",
                    e.getMessage()
            );

            return ResponseEntity
                    .badRequest()
                    .body(error);

        } catch (Exception e) {

            // Temporal para ver el error real de Gmail
            e.printStackTrace();

            Map<String, String> error =
                    new HashMap<>();

            error.put(
                    "mensaje",
                    "No se pudo enviar el correo de recuperación"
            );

            error.put(
                    "tipo",
                    e.getClass().getName()
            );

            error.put(
                    "detalle",
                    e.getMessage() != null
                            ? e.getMessage()
                            : "Sin detalle disponible"
            );

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(error);
        }
    }

    // =========================================
    // RESTABLECER CONTRASEÑA
    // =========================================

    @PostMapping("/restablecer-contrasena")
    public ResponseEntity<?> restablecerContrasena(
            @RequestBody RestablecerContrasenaRequestDTO request
    ) {

        try {

            authService.restablecerContrasena(request);

            Map<String, String> respuesta =
                    new HashMap<>();

            respuesta.put(
                    "mensaje",
                    "Contraseña restablecida correctamente"
            );

            return ResponseEntity.ok(respuesta);

        } catch (IllegalArgumentException e) {

            Map<String, String> error =
                    new HashMap<>();

            error.put(
                    "mensaje",
                    e.getMessage()
            );

            return ResponseEntity
                    .badRequest()
                    .body(error);
        }
    }
}