package co.edu.udistrital.mdp.pets.dto;

import java.util.Date;

import lombok.Data;
@Data
public class ResenaDTO {
    private Long id;
    private Integer calificacion;
    private String comentario;
    private Date fecha;
    private MascotaDTO mascota;
    private AdoptanteDTO adoptante;
}