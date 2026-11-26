package cl.textilcircular_api.service;

import cl.textilcircular_api.dto.ProductoRequestDTO;
import cl.textilcircular_api.model.Producto;
import cl.textilcircular_api.repository.ProductoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductoService {
    private final ProductoRepository productoRepository;

    public List<Producto> listarProductos(String categoria, boolean soloDisponibles){
        if(categoria != null && !categoria.isBlank()){
            return productoRepository.findByCategoriaIgnoreCaseAndDisponibleTrueOrderByFechaCreacionDesc(categoria.trim());
        }
        return soloDisponibles
                ? productoRepository.findByDisponibleTrueOrderByFechaCreacionDesc()
                : productoRepository.findAllByOrderByFechaCreacionDesc();
    }

    public Producto obtenerPorId(Long id){
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
    }

    public List<Producto> listarPorUsuario(Long usuarioId){
        return productoRepository.findByUsuarioIdOrderByFechaCreacionDesc(usuarioId);
    }

    public Producto crearProducto(ProductoRequestDTO requestDTO){
        validarRequest(requestDTO);

        Producto producto = Producto.builder()
                .nombre(requestDTO.getNombre().trim())
                .descripcion(requestDTO.getDescripcion().trim())
                .precio(requestDTO.getPrecio())
                .categoria(requestDTO.getCategoria().trim())
                .imagenUrl(requestDTO.getImagenUrl())
                .disponible(requestDTO.getDisponible() != null ? requestDTO.getDisponible() : true)
                .usuarioId(requestDTO.getUsuarioId())
                .build();

        return productoRepository.save(producto);
    }

    public Producto actualizarProducto(Long id, ProductoRequestDTO request){
        validarRequest(request);
        Producto existente = obtenerPorId(id);

        existente.setNombre(request.getNombre().trim());
        existente.setDescripcion(request.getDescripcion().trim());
        existente.setPrecio(request.getPrecio());
        existente.setCategoria(request.getCategoria().trim());
        existente.setImagenUrl(request.getImagenUrl());
        if (request.getDisponible() != null) {
            existente.setDisponible(request.getDisponible());
        }
        if (request.getUsuarioId() != null) {
            existente.setUsuarioId(request.getUsuarioId());
        }

        return productoRepository.save(existente);
    }

    public Producto cambiarDisponibilidad(Long id, boolean disponible){
        Producto existente = obtenerPorId(id);
        existente.setDisponible(disponible);
        return productoRepository.save(existente);
    }

    public void eliminarProducto(Long id){
        Producto existente = obtenerPorId(id);
        productoRepository.delete(existente);
    }

    private void validarRequest(ProductoRequestDTO request){
        if (request.getNombre() == null || request.getNombre().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre del producto es obligatorio");
        }
        if (request.getDescripcion() == null || request.getDescripcion().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La descripción es obligatoria");
        }
        if (request.getPrecio() == null || request.getPrecio().compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El precio debe ser mayor o igual a 0");
        }
        if (request.getCategoria() == null || request.getCategoria().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La categoría es obligatoria");
        }
    }
}
