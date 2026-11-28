package cl.textilcircular_api.repository;

import cl.textilcircular_api.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findAllByOrderByFechaCreacionDesc();

    List<Producto> findByDisponibleTrueOrderByFechaCreacionDesc();

    List<Producto> findByCategoriaIgnoreCaseAndDisponibleTrueOrderByFechaCreacionDesc(String categoria);

    List<Producto> findByUsuarioIdOrderByFechaCreacionDesc(Long usuarioId);

}
