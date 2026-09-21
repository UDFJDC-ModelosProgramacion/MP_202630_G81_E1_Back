package co.edu.udistrital.mdp.pets.dto;

import lombok.Data;

/**
 * DTO de Veterinario. El campo "refugio" solo necesita traer el id
 * para que el controller pueda asociarlo antes de llamar al Service.
 */
@Data
public class VeterinarioDTO {

    private Long id;
    private String nombre;
    private String telefono;
    private String email;
    private String especialidad;
    private String disponibilidad;
    private RefugioDTO refugio;
}
