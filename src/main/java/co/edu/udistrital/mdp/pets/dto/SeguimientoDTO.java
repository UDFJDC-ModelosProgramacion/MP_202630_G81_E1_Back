package co.edu.udistrital.mdp.pets.dto;

import java.sql.Date;

import lombok.Data;

/**
 * DTO de Seguimiento. "mascota" y "veterinario" solo necesitan traer
 * el id para que el controller pueda asociarlos antes de llamar al Service.
 */
@Data
public class SeguimientoDTO {

    private Long id;
    private Date fechaAsignacion;
    private Date proximaCita;
    private String observacion;
    private String estado;
    private MascotaDTO mascota;
    private VeterinarioDTO veterinario;
}
