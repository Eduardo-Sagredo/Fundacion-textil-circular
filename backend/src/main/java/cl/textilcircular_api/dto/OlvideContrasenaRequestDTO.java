package cl.textilcircular_api.dto;

public class OlvideContrasenaRequestDTO {

    private String email;

    public OlvideContrasenaRequestDTO() {
    }

    public OlvideContrasenaRequestDTO(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}