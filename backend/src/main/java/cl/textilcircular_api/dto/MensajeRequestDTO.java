package cl.textilcircular_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MensajeRequestDTO {
    @NotBlank(message = "El chatId es obligatorio")
    private String chatId;

    @NotBlank(message = "El contenido del mensaje no puede estar vacío")
    @Size(max = 1000, message = "El mensaje excede los 1000 caracteres")
    private String contenido;

    private boolean esDeUsuario;
    private boolean leido;




}
