package cl.textilcircular_api.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductoRequestDTO {
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private String categoria;
    private String imagenUrl;
    private Boolean disponible;
    private Long usuarioId;
}
