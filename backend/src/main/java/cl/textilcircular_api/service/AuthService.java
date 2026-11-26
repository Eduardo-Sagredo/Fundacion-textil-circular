package cl.textilcircular_api.service;

import cl.textilcircular_api.dto.CambiarContrasenaRequestDTO;
import cl.textilcircular_api.dto.LoginRequestDTO;
import cl.textilcircular_api.dto.LoginResponseDTO;
import cl.textilcircular_api.dto.OlvideContrasenaRequestDTO;
import cl.textilcircular_api.dto.PerfilResponseDTO;
import cl.textilcircular_api.dto.RegistroRequestDTO;
import cl.textilcircular_api.dto.RestablecerContrasenaRequestDTO;

import cl.textilcircular_api.model.Emprendedora;
import cl.textilcircular_api.model.PasswordResetToken;

import cl.textilcircular_api.repository.EmprendedoraRepository;
import cl.textilcircular_api.repository.PasswordResetTokenRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuthService {

    private final EmprendedoraRepository
            emprendedoraRepository;

    private final PasswordEncoder
            passwordEncoder;

    private final PasswordResetTokenRepository
            passwordResetTokenRepository;

    private final EmailService
            emailService;

    private final SecureRandom
            secureRandom =
            new SecureRandom();


    public AuthService(
            EmprendedoraRepository emprendedoraRepository,
            PasswordEncoder passwordEncoder,
            PasswordResetTokenRepository passwordResetTokenRepository,
            EmailService emailService
    ) {

        this.emprendedoraRepository =
                emprendedoraRepository;

        this.passwordEncoder =
                passwordEncoder;

        this.passwordResetTokenRepository =
                passwordResetTokenRepository;

        this.emailService =
                emailService;
    }


    // =========================================
    // REGISTRO
    // =========================================

    public Emprendedora registrar(
            RegistroRequestDTO request
    ) {

        String correo =
                request
                        .getCorreo()
                        .trim()
                        .toLowerCase();


        String rut =
                request
                        .getRut()
                        .trim()
                        .toUpperCase();


        if (
                emprendedoraRepository
                        .existsByCorreo(
                                correo
                        )
        ) {

            throw new IllegalArgumentException(
                    "El correo ya está registrado"
            );
        }


        if (
                emprendedoraRepository
                        .existsByRut(
                                rut
                        )
        ) {

            throw new IllegalArgumentException(
                    "El RUT ya está registrado"
            );
        }


        Emprendedora emprendedora =
                new Emprendedora();


        emprendedora.setRut(
                rut
        );


        emprendedora.setNombre(
                request
                        .getNombre()
                        .trim()
        );


        emprendedora.setCorreo(
                correo
        );


        emprendedora.setContrasena(
                passwordEncoder.encode(
                        request.getContrasena()
                )
        );


        emprendedora.setNombreEmprendimiento(
                request
                        .getNombreEmprendimiento()
                        .trim()
        );


        emprendedora.setRubro(
                request
                        .getRubro()
                        .trim()
        );


        emprendedora.setRegion(
                request
                        .getRegion()
                        .trim()
        );


        emprendedora.setComuna(
                request
                        .getComuna()
                        .trim()
        );


        emprendedora.setEdad(
                request.getEdad()
        );


        emprendedora.setGenero(
                request
                        .getGenero()
                        .trim()
        );


        emprendedora.setRol(
                "EMPRENDEDORA"
        );


        emprendedora.setEstado(
                "PENDIENTE"
        );


        emprendedora.setMotivoRechazo(
                null
        );


        return emprendedoraRepository.save(
                emprendedora
        );
    }


    // =========================================
    // LOGIN
    // =========================================

    public LoginResponseDTO login(
            LoginRequestDTO request
    ) {

        String correo =
                request
                        .getCorreo()
                        .trim()
                        .toLowerCase();


        Emprendedora emprendedora =
                emprendedoraRepository
                        .findByCorreo(
                                correo
                        )
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "Correo o contraseña incorrectos"
                                        )
                        );


        boolean contrasenaCorrecta =
                passwordEncoder.matches(
                        request.getContrasena(),
                        emprendedora.getContrasena()
                );


        if (
                !contrasenaCorrecta
        ) {

            throw new IllegalArgumentException(
                    "Correo o contraseña incorrectos"
            );
        }


        String estado =
                emprendedora
                        .getEstado()
                        .toUpperCase();


        boolean puedeIngresar =
                "APROBADA".equals(
                        estado
                );


        String mensaje =
                switch (estado) {

                    case "APROBADA" ->
                            "Inicio de sesión correcto";

                    case "PENDIENTE" ->
                            "Tu cuenta todavía está pendiente de aprobación";

                    case "RECHAZADA" ->
                            "Tu cuenta fue rechazada";

                    default ->
                            "Estado de cuenta desconocido";
                };


        return new LoginResponseDTO(

                true,

                mensaje,

                emprendedora.getId(),

                emprendedora.getNombre(),

                emprendedora.getCorreo(),

                emprendedora.getRol(),

                emprendedora.getEstado(),

                emprendedora.getMotivoRechazo(),

                puedeIngresar
        );
    }


    // =========================================
    // PERFIL
    // =========================================

    public PerfilResponseDTO obtenerPerfil(
            String correo
    ) {

        String correoNormalizado =
                correo
                        .trim()
                        .toLowerCase();


        Emprendedora emprendedora =
                emprendedoraRepository
                        .findByCorreo(
                                correoNormalizado
                        )
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "Usuario no encontrado"
                                        )
                        );


        return new PerfilResponseDTO(

                emprendedora.getId(),

                emprendedora.getRut(),

                emprendedora.getNombre(),

                emprendedora.getCorreo(),

                emprendedora
                        .getNombreEmprendimiento(),

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


    // =========================================
    // CAMBIAR CONTRASEÑA
    // =========================================

    public void cambiarContrasena(
            CambiarContrasenaRequestDTO request
    ) {

        String correo =
                request
                        .getCorreo()
                        .trim()
                        .toLowerCase();


        Emprendedora emprendedora =
                emprendedoraRepository
                        .findByCorreo(
                                correo
                        )
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "Usuario no encontrado"
                                        )
                        );


        boolean contrasenaActualCorrecta =
                passwordEncoder.matches(
                        request.getContrasenaActual(),
                        emprendedora.getContrasena()
                );


        if (
                !contrasenaActualCorrecta
        ) {

            throw new IllegalArgumentException(
                    "La contraseña actual es incorrecta"
            );
        }


        boolean mismaContrasena =
                passwordEncoder.matches(
                        request.getNuevaContrasena(),
                        emprendedora.getContrasena()
                );


        if (
                mismaContrasena
        ) {

            throw new IllegalArgumentException(
                    "La nueva contraseña debe ser diferente a la actual"
            );
        }


        String nuevaContrasenaEncriptada =
                passwordEncoder.encode(
                        request.getNuevaContrasena()
                );


        emprendedora.setContrasena(
                nuevaContrasenaEncriptada
        );


        emprendedoraRepository.save(
                emprendedora
        );
    }


    // =========================================
    // OLVIDÉ MI CONTRASEÑA
    // =========================================

    @Transactional
    public void olvideContrasena(
            OlvideContrasenaRequestDTO request
    ) {

        String correo =
                request
                        .getEmail()
                        .trim()
                        .toLowerCase();


        Emprendedora emprendedora =
                emprendedoraRepository
                        .findByCorreo(
                                correo
                        )
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "Usuario no encontrado"
                                        )
                        );


        // =====================================
        // INVALIDAR CÓDIGOS ANTERIORES
        // =====================================

        List<PasswordResetToken> tokensAnteriores =
                passwordResetTokenRepository
                        .findByEmailAndUsado(
                                emprendedora.getCorreo(),
                                0
                        );


        for (
                PasswordResetToken tokenAnterior
                : tokensAnteriores
        ) {

            tokenAnterior.setUsado(
                    1
            );
        }


        passwordResetTokenRepository.saveAll(
                tokensAnteriores
        );


        // =====================================
        // GENERAR NUEVO CÓDIGO DE 6 DÍGITOS
        // =====================================

        int numero =
                secureRandom.nextInt(
                        900000
                ) + 100000;


        String token =
                String.valueOf(
                        numero
                );


        LocalDateTime fechaExpiracion =
                LocalDateTime
                        .now()
                        .plusMinutes(
                                15
                        );


        PasswordResetToken passwordResetToken =
                new PasswordResetToken();


        passwordResetToken.setEmail(
                emprendedora.getCorreo()
        );


        passwordResetToken.setToken(
                token
        );


        passwordResetToken.setFechaExpiracion(
                fechaExpiracion
        );


        passwordResetToken.setUsado(
                0
        );


        passwordResetTokenRepository.save(
                passwordResetToken
        );


        // =====================================
        // ENVIAR CORREO
        // =====================================

        emailService.enviarCorreoRecuperacion(
                emprendedora.getCorreo(),
                token
        );
    }


    // =========================================
    // RESTABLECER CONTRASEÑA
    // =========================================

    @Transactional
    public void restablecerContrasena(
            RestablecerContrasenaRequestDTO request
    ) {

        PasswordResetToken passwordResetToken =
                passwordResetTokenRepository
                        .findByToken(
                                request.getToken()
                        )
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "Código inválido"
                                        )
                        );


        if (
                passwordResetToken.getUsado() == 1
        ) {

            throw new IllegalArgumentException(
                    "El código ya fue utilizado"
            );
        }


        if (
                passwordResetToken
                        .getFechaExpiracion()
                        .isBefore(
                                LocalDateTime.now()
                        )
        ) {

            throw new IllegalArgumentException(
                    "El código ha expirado"
            );
        }


        Emprendedora emprendedora =
                emprendedoraRepository
                        .findByCorreo(
                                passwordResetToken.getEmail()
                        )
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "Usuario no encontrado"
                                        )
                        );


        boolean mismaContrasena =
                passwordEncoder.matches(
                        request.getNuevaContrasena(),
                        emprendedora.getContrasena()
                );


        if (
                mismaContrasena
        ) {

            throw new IllegalArgumentException(
                    "La nueva contraseña debe ser diferente a la anterior"
            );
        }


        String nuevaContrasenaEncriptada =
                passwordEncoder.encode(
                        request.getNuevaContrasena()
                );


        emprendedora.setContrasena(
                nuevaContrasenaEncriptada
        );


        emprendedoraRepository.save(
                emprendedora
        );


        // =====================================
        // INVALIDAR TODOS LOS CÓDIGOS ACTIVOS
        // =====================================

        List<PasswordResetToken> tokensActivos =
                passwordResetTokenRepository
                        .findByEmailAndUsado(
                                emprendedora.getCorreo(),
                                0
                        );


        for (
                PasswordResetToken tokenActivo
                : tokensActivos
        ) {

            tokenActivo.setUsado(
                    1
            );
        }


        passwordResetTokenRepository.saveAll(
                tokensActivos
        );
    }
}