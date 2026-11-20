package cl.textilcircular_api.repository;

import cl.textilcircular_api.model.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long> {
    List<Mensaje> findByChatIdOrderByFechaEnvioAscIdAsc(String chatId);
    long countByChatIdAndEsDeUsuarioFalse(String chatId);
}
