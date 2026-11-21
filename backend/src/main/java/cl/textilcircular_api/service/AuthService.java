package cl.textilcircular_api.service;

import cl.textilcircular_api.dto.LoginRequestDTO;
import cl.textilcircular_api.dto.LoginResponseDTO;
import cl.textilcircular_api.dto.RegistroRequestDTO;

import cl.textilcircular_api.model.Emprendedora;

import cl.textilcircular_api.repository.EmprendedoraRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final EmprendedoraRepository
            emprendedoraRepository;

    private final PasswordEncoder
            passwordEncoder;


    public AuthService(
            EmprendedoraRepository emprendedoraRepository,
            PasswordEncoder passwordEncoder
    ) {

        this.emprendedoraRepository =
                emprendedoraRepository;

        this.passwordEncoder =
                passwordEncoder;
    }


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


        // La contraseña NO se guarda directamente.
        // Primero se encripta mediante BCrypt.
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


        // BCrypt compara la contraseña enviada
        // con la contraseña encriptada de Oracle.
        boolean contrasenaCorrecta =
                passwordEncoder.matches(

                        request.getContrasena(),

                        emprendedora.getContrasena()
                );


        if (!contrasenaCorrecta) {

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
}