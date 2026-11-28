package cl.textilcircular_api.controller;

import cl.textilcircular_api.dto.MensajeRequestDTO;
import cl.textilcircular_api.model.Chat;
import cl.textilcircular_api.model.Mensaje;
import cl.textilcircular_api.service.MensajeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class MensajeriaController {

    private final MensajeService mensajeService;

    @PostMapping("/mensajes")
    public ResponseEntity<List<Mensaje>> enviarMensaje(@Valid @RequestBody MensajeRequestDTO requestDTO){
        List<Mensaje> respuesta = mensajeService.procesarMensaje(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @GetMapping("/chats/{chatId}/mensajes")
    public ResponseEntity<List<Mensaje>> obtenerMensajesPorChat(@PathVariable String chatId) {
        return ResponseEntity.ok(mensajeService.obtenerHistorialChat(chatId));
    }

    @GetMapping("/chats")
    public ResponseEntity<List<Chat>> listarChats() {
        return ResponseEntity.ok(mensajeService.listarTodosLosChats());
    }

    @PutMapping("/chats/{chatId}/cerrar")
    public ResponseEntity<Chat> cerrarChat(@PathVariable String chatId) {
        return ResponseEntity.ok(mensajeService.cerrarChat(chatId));
    }
}
