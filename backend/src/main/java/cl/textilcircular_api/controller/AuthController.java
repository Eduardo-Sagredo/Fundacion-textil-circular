package cl.textilcircular_api.controller;

import cl.textilcircular_api.dto.LoginRequestDTO;
import cl.textilcircular_api.dto.LoginResponseDTO;
import cl.textilcircular_api.dto.RegistroRequestDTO;

import cl.textilcircular_api.model.Emprendedora;

import cl.textilcircular_api.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;


    public AuthController(
            AuthService authService
    ) {

        this.authService =
                authService;
    }


    // ===============================
    // REGISTRO
    // ===============================

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(

            @Valid
            @RequestBody
            RegistroRequestDTO request
    ) {

        try {

            Emprendedora emprendedora =
                    authService.registrar(
                            request
                    );


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
                    .status(
                            HttpStatus.CREATED
                    )
                    .body(
                            respuesta
                    );


        } catch (
                IllegalArgumentException e
        ) {

            Map<String, String> error =
                    new HashMap<>();


            error.put(
                    "mensaje",
                    e.getMessage()
            );


            return ResponseEntity
                    .badRequest()
                    .body(
                            error
                    );
        }
    }


    // ===============================
    // LOGIN
    // ===============================

    @PostMapping("/login")
    public ResponseEntity<?> login(

            @Valid
            @RequestBody
            LoginRequestDTO request
    ) {

        try {

            LoginResponseDTO respuesta =
                    authService.login(
                            request
                    );


            return ResponseEntity.ok(
                    respuesta
            );


        } catch (
                IllegalArgumentException e
        ) {

            Map<String, String> error =
                    new HashMap<>();


            error.put(
                    "mensaje",
                    e.getMessage()
            );


            return ResponseEntity
                    .status(
                            HttpStatus.UNAUTHORIZED
                    )
                    .body(
                            error
                    );
        }
    }
}