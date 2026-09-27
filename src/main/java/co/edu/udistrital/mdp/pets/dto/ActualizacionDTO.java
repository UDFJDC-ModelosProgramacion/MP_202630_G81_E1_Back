package co.edu.udistrital.mdp.pets.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ActualizacionDTO {
    private Long id;
    private Date fecha;
    private String tipo;
    private String descripcion;
    private String archivoUrl;
    private MascotaDTO mascota;
    private AdoptanteDTO adoptante;
}