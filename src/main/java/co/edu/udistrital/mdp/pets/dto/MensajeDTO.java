package co.edu.udistrital.mdp.pets.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class MensajeDTO {
    private Long id;
    private Date fecha;
    private String asunto;
    private String contenido;
    private boolean leido;
    private Long mascotaId;
    private Long adoptanteId;
}
