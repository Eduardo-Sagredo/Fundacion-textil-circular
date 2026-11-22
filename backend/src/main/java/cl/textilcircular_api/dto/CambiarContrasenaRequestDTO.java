package cl.textilcircular_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class CambiarContrasenaRequestDTO {

    @NotBlank(
            message = "El correo es obligatorio"
    )
    @Email(
            message = "El correo no tiene un formato válido"
    )
    private String correo;


    @NotBlank(
            message = "La contraseña actual es obligatoria"
    )
    private String contrasenaActual;


    @NotBlank(
            message = "La nueva contraseña es obligatoria"
    )
    @Size(
            min = 7,
            max = 100,
            message = "La contraseña debe tener entre 7 y 100 caracteres"
    )
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z0-9\\s]).+$",
            message = "La contraseña debe contener una letra, un número y un carácter especial"
    )
    private String nuevaContrasena;
}