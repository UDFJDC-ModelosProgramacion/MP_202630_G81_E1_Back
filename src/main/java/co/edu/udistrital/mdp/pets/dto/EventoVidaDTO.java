package co.edu.udistrital.mdp.pets.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class EventoVidaDTO {
    private Long id;
    private String tipo;
    private Date fecha;
    private String descripcion;
    private Long mascotaId;
}
