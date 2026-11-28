package cl.textilcircular_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RechazoRequestDTO {

    @NotBlank(message = "El motivo del rechazo es obligatorio")
    @Size(
            max = 500,
            message = "El motivo del rechazo no puede superar los 500 caracteres"
    )
    private String motivo;
}