package cl.textilcircular_api.controller;

import cl.textilcircular_api.dto.ProductoRequestDTO;
import cl.textilcircular_api.model.Producto;
import cl.textilcircular_api.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductoController {
    private final ProductoService productoService;

    //Listar productos solo disponibles
    @GetMapping
    public ResponseEntity<List<Producto>> listar(
            @RequestParam(required = false) String categoria,
            @RequestParam(defaultValue = "false") boolean soloDisponibles) {
        return ResponseEntity.ok(productoService.listarProductos(categoria, soloDisponibles));
    }

    //Listar productos por usuario
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Producto>> listarPorUsuario(@PathVariable Long usuarioId){
        return ResponseEntity.ok(productoService.listarPorUsuario(usuarioId));
    }

    //Crear producto
    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody ProductoRequestDTO requestDTO){
        Producto creado = productoService.crearProducto(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    //Editar Producto
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @RequestBody ProductoRequestDTO request) {
        return ResponseEntity.ok(productoService.actualizarProducto(id, request));
    }

    //Cambiar Disponibilidad
    @PatchMapping("/{id}/disponibilidad")
    public ResponseEntity<Producto> cambiarDisponibilidad(
            @PathVariable Long id,
            @RequestParam boolean disponible) {
        return ResponseEntity.ok(productoService.cambiarDisponibilidad(id, disponible));
    }

    //Eliminar producto
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Producto Eliminado correctamente");
    }








}
