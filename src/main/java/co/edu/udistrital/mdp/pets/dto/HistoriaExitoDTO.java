package co.edu.udistrital.mdp.pets.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class HistoriaExitoDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private Date fecha;
    private Long mascotaId;
}
