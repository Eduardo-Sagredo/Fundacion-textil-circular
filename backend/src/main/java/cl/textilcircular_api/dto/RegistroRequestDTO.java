package cl.textilcircular_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class RegistroRequestDTO {

    @NotBlank(
            message = "El RUT es obligatorio"
    )
    @Size(
            max = 20,
            message = "El RUT no puede superar los 20 caracteres"
    )
    private String rut;


    @NotBlank(
            message = "El nombre es obligatorio"
    )
    @Size(
            max = 150,
            message = "El nombre no puede superar los 150 caracteres"
    )
    private String nombre;


    @NotBlank(
            message = "El correo es obligatorio"
    )
    @Email(
            message = "El correo no tiene un formato válido"
    )
    @Size(
            max = 150,
            message = "El correo no puede superar los 150 caracteres"
    )
    private String correo;


    @NotBlank(
            message = "La contraseña es obligatoria"
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
    private String contrasena;


    @NotBlank(
            message = "El nombre del emprendimiento es obligatorio"
    )
    @Size(
            max = 150,
            message = "El nombre del emprendimiento no puede superar los 150 caracteres"
    )
    private String nombreEmprendimiento;


    @NotBlank(
            message = "El rubro es obligatorio"
    )
    @Size(
            max = 120,
            message = "El rubro no puede superar los 120 caracteres"
    )
    private String rubro;


    @NotBlank(
            message = "La región es obligatoria"
    )
    @Size(
            max = 120,
            message = "La región no puede superar los 120 caracteres"
    )
    private String region;


    @NotBlank(
            message = "La comuna es obligatoria"
    )
    @Size(
            max = 120,
            message = "La comuna no puede superar los 120 caracteres"
    )
    private String comuna;


    @NotNull(
            message = "La edad es obligatoria"
    )
    @Min(
            value = 1,
            message = "La edad debe ser mayor a 0"
    )
    private Integer edad;


    @NotBlank(
            message = "El género es obligatorio"
    )
    @Size(
            max = 50,
            message = "El género no puede superar los 50 caracteres"
    )
    private String genero;
}