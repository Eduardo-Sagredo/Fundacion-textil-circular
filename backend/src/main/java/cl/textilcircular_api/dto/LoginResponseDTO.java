package cl.textilcircular_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO {

    private boolean exito;

    private String mensaje;

    private Long id;

    private String nombre;

    private String correo;

    private String rol;

    private String estado;

    private String motivoRechazo;

    private boolean puedeIngresar;
}