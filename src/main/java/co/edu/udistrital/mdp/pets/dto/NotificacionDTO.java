package co.edu.udistrital.mdp.pets.dto;

import java.util.Date;

import lombok.Data;

@Data
public class NotificacionDTO {
    private Long id;
    private String tipo;
    private String mensaje;
    private Date fecha;
    private String canal;
    private MascotaDTO mascota;
    private AdoptanteDTO adoptante;
}