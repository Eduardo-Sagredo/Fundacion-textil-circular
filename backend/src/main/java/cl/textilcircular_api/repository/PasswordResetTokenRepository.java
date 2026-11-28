package cl.textilcircular_api.repository;

import cl.textilcircular_api.model.PasswordResetToken;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PasswordResetTokenRepository
        extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(
            String token
    );

    List<PasswordResetToken> findByEmailAndUsado(
            String email,
            Integer usado
    );
}