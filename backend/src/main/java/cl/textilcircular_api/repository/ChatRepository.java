package cl.textilcircular_api.repository;

import cl.textilcircular_api.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, String> {
    List<Chat> findAllByOrderByFechaCreacionDesc();
}
