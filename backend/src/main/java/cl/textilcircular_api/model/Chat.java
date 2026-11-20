package cl.textilcircular_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name="chats")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chat {

    @Id
    @Column(name = "chat_id", nullable = false, length = 150)
    private String chatId;

    @Column(nullable = false)
    private boolean cerrado = false;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

}
