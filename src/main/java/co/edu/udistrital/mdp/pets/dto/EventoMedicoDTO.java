package co.edu.udistrital.mdp.pets.dto;

import java.util.Date;

import lombok.Data;
@Data
public class EventoMedicoDTO {
    private Long id;
    private Date fecha;
    private String descripcion;
    private String diagnostico;
    private String tratamiento;
    private MascotaDTO mascota;
}