package cl.textilcircular_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class LoginRequestDTO {

    @NotBlank(
            message = "El correo es obligatorio"
    )
    @Email(
            message = "El correo no tiene un formato válido"
    )
    private String correo;


    @NotBlank(
            message = "La contraseña es obligatoria"
    )
    private String contrasena;
}