package cl.miespacio.dto;

import cl.miespacio.model.EstadoCotizacion;
import cl.miespacio.model.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public final class Dtos {
    private Dtos() {
    }

    public record RegistroRequest(
            @NotBlank(message = "El nombre es obligatorio.")
            @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres.") String nombre,
            @NotBlank(message = "El correo es obligatorio.")
            @Email(message = "El correo no es válido.")
            @Size(max = 150, message = "El correo es demasiado largo.") String email,
            @NotBlank(message = "La contraseña es obligatoria.")
            @Size(min = 6, max = 72, message = "La contraseña debe tener entre 6 y 72 caracteres.") String password) {
    }

    public record LoginRequest(
            @NotBlank(message = "El correo es obligatorio.")
            @Email(message = "El correo no es válido.") String email,
            @NotBlank(message = "La contraseña es obligatoria.") String password) {
    }

    public record UsuarioResponse(Long id, String nombre, String email, Rol rol) {
    }

    public record AuthResponse(String token, long expiraEn, UsuarioResponse usuario) {
    }

    public record ServicioRequest(
            @NotBlank(message = "El nombre es obligatorio.")
            @Size(max = 100, message = "El nombre no puede superar 100 caracteres.") String nombre,
            @NotBlank(message = "La descripción es obligatoria.")
            @Size(max = 500, message = "La descripción no puede superar 500 caracteres.") String descripcion,
            @NotBlank(message = "La categoría es obligatoria.")
            @Size(max = 100, message = "La categoría no puede superar 100 caracteres.") String categoria,
            @NotBlank(message = "El tamaño es obligatorio.")
            @Size(max = 50, message = "El tamaño no puede superar 50 caracteres.") String tamano,
            @Size(max = 200, message = "La ruta de imagen no puede superar 200 caracteres.") String imagen) {
    }

    public record ServicioResponse(Long id, String nombre, String descripcion, String categoria, String tamano,
                                   String imagen) {
    }

    public record SucursalRequest(
            @NotBlank(message = "El nombre es obligatorio.")
            @Size(max = 100, message = "El nombre no puede superar 100 caracteres.") String nombre,
            @NotBlank(message = "La dirección es obligatoria.")
            @Size(max = 200, message = "La dirección no puede superar 200 caracteres.") String direccion,
            @NotBlank(message = "El horario es obligatorio.")
            @Size(max = 100, message = "El horario no puede superar 100 caracteres.") String horario,
            @NotBlank(message = "El acceso es obligatorio.")
            @Size(max = 100, message = "El acceso no puede superar 100 caracteres.") String acceso,
            @NotBlank(message = "El teléfono es obligatorio.")
            @Size(max = 30, message = "El teléfono no puede superar 30 caracteres.") String telefono,
            @Size(max = 1500, message = "La URL del mapa es demasiado larga.") String mapaUrl) {
    }

    public record SucursalResponse(Long id, String nombre, String direccion, String horario, String acceso,
                                   String telefono, String mapaUrl) {
    }

    public record ResenaRequest(
            @NotBlank(message = "La reseña es obligatoria.")
            @Size(min = 10, max = 500, message = "La reseña debe tener entre 10 y 500 caracteres.") String texto,
            @NotNull(message = "La evaluación es obligatoria.")
            @Min(value = 1, message = "La evaluación mínima es 1.")
            @Max(value = 5, message = "La evaluación máxima es 5.") Integer evaluacion) {
    }

    public record ResenaResponse(Long id, String autor, String texto, Integer evaluacion, LocalDateTime fecha,
                                 Long usuarioId) {
    }

    public record CotizacionRequest(
            @NotNull(message = "Debes elegir un servicio.") Long servicioId,
            @NotBlank(message = "El mensaje es obligatorio.")
            @Size(min = 10, max = 200, message = "El mensaje debe tener entre 10 y 200 caracteres.") String mensaje) {
    }

    public record CotizacionUpdateRequest(
            @Size(min = 10, max = 200, message = "El mensaje debe tener entre 10 y 200 caracteres.") String mensaje,
            EstadoCotizacion estado) {
    }

    public record CotizacionResponse(Long id, Long servicioId, String servicioNombre, String usuarioNombre,
                                     String usuarioEmail, String mensaje, EstadoCotizacion estado,
                                     LocalDateTime creadaEn) {
    }
}
