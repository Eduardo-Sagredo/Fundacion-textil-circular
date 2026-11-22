package cl.textilcircular_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "emprendedoras"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Emprendedora {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;


    @Column(
            nullable = false,
            unique = true,
            length = 20
    )
    private String rut;


    @Column(
            nullable = false,
            length = 150
    )
    private String nombre;


    @Column(
            nullable = false,
            unique = true,
            length = 150
    )
    private String correo;


    @Column(
            nullable = false,
            length = 255
    )
    private String contrasena;


    @Column(
            name = "nombre_emprendimiento",
            nullable = false,
            length = 150
    )
    private String nombreEmprendimiento;


    @Column(
            nullable = false,
            length = 120
    )
    private String rubro;


    @Column(
            nullable = false,
            length = 120
    )
    private String region;


    @Column(
            nullable = false,
            length = 120
    )
    private String comuna;


    @Column(
            nullable = false
    )
    private Integer edad;


    @Column(
            nullable = false,
            length = 50
    )
    private String genero;


    @Column(
            nullable = false,
            length = 30
    )
    private String rol = "EMPRENDEDORA";


    @Column(
            nullable = false,
            length = 20
    )
    private String estado = "PENDIENTE";


    @Column(
            name = "motivo_rechazo",
            length = 500
    )
    private String motivoRechazo;
}