package cl.textilcircular_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminSolicitudDTO {

    private Long id;
    private String rut;
    private String nombre;
    private String correo;
    private String nombreEmprendimiento;
    private String rubro;
    private String region;
    private String comuna;
    private Integer edad;
    private String genero;
    private String rol;
    private String estado;
    private String motivoRechazo;
}