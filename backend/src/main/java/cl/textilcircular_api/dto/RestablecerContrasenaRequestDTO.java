package cl.textilcircular_api.dto;

public class RestablecerContrasenaRequestDTO {

    private String token;
    private String nuevaContrasena;

    public RestablecerContrasenaRequestDTO() {
    }

    public RestablecerContrasenaRequestDTO(String token, String nuevaContrasena) {
        this.token = token;
        this.nuevaContrasena = nuevaContrasena;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNuevaContrasena() {
        return nuevaContrasena;
    }

    public void setNuevaContrasena(String nuevaContrasena) {
        this.nuevaContrasena = nuevaContrasena;
    }
}