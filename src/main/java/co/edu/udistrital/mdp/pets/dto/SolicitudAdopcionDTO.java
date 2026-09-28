package co.edu.udistrital.mdp.pets.dto;

import java.util.Date;

import lombok.Data;

@Data
public class SolicitudAdopcionDTO {

    private Long id;
    private Date fecha;
    private String estado;
    private String tipoSolicitud;
    private String observacion;
    private AdoptanteDTO adoptante;
    private MascotaDTO mascota;
    private AdopcionDTO adopcion;

}