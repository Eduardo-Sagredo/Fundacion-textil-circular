package cl.textilcircular_api.repository;

import cl.textilcircular_api.model.Emprendedora;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmprendedoraRepository
        extends JpaRepository<Emprendedora, Long> {

    Optional<Emprendedora> findByCorreo(
            String correo
    );


    Optional<Emprendedora> findByRut(
            String rut
    );


    boolean existsByCorreo(
            String correo
    );


    boolean existsByRut(
            String rut
    );


    List<Emprendedora> findByEstado(
            String estado
    );
}