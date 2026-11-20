package cl.textilcircular_api.service;

import cl.textilcircular_api.dto.MensajeRequestDTO;
import cl.textilcircular_api.model.Chat;
import cl.textilcircular_api.model.Mensaje;
import cl.textilcircular_api.repository.ChatRepository;
import cl.textilcircular_api.repository.MensajeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MensajeService {

    private final ChatRepository chatRepository;
    private final MensajeRepository mensajeRepository;

    public List<Mensaje> procesarMensaje(MensajeRequestDTO mensajeRequestDTO){
        Chat chat = chatRepository.findById(mensajeRequestDTO.getChatId())
                .orElseGet(() -> {
                    Chat nuevoChat = new Chat();
                    nuevoChat.setChatId(mensajeRequestDTO.getChatId());
                    nuevoChat.setCerrado(false);
                    nuevoChat.setFechaCreacion(LocalDateTime.now());
                    return chatRepository.save(nuevoChat);
                });

        if (chat.isCerrado()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este ticket ya se encuentra cerrado");
        }

        List<Mensaje> mensajesGenerados = new ArrayList<>();

        Mensaje mensaje = new Mensaje();
        mensaje.setChatId(mensajeRequestDTO.getChatId());
        mensaje.setContenido(mensajeRequestDTO.getContenido());
        mensaje.setEsDeUsuario(mensajeRequestDTO.isEsDeUsuario());
        mensaje.setLeido(mensajeRequestDTO.isLeido());
        mensaje.setFechaEnvio(LocalDateTime.now());

        mensajesGenerados.add(mensajeRepository.save(mensaje));

        if (mensajeRequestDTO.isEsDeUsuario() && mensajeRepository.countByChatIdAndEsDeUsuarioFalse(mensajeRequestDTO.getChatId()) == 0){
            Mensaje respuestaAutomatica = new Mensaje();
            respuestaAutomatica.setChatId(mensajeRequestDTO.getChatId());
            respuestaAutomatica.setContenido("Hemos recibido tu mensaje. Un administrador te responderá a la brevedad.");
            respuestaAutomatica.setEsDeUsuario(false);
            respuestaAutomatica.setLeido(false);
            respuestaAutomatica.setFechaEnvio(LocalDateTime.now().plusSeconds(1));

            mensajesGenerados.add(mensajeRepository.save(respuestaAutomatica));
        }

        return mensajesGenerados;
    }

    public List<Mensaje> obtenerHistorialChat(String chatId){
        return mensajeRepository.findByChatIdOrderByFechaEnvioAscIdAsc(chatId);
    }

    public List<Chat> listarTodosLosChats(){
        return chatRepository.findAllByOrderByFechaCreacionDesc();
    }

    public Chat cerrarChat(String chatId){
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat no encontrado"));
        chat.setCerrado(true);
        return chatRepository.save(chat);
    }
}
