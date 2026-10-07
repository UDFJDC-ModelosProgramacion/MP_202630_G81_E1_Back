package co.edu.udistrital.mdp.pets.dto;

import lombok.Data;

/**
 * DTO de Mascota. El campo "refugio" solo necesita traer el id
 * (los demas campos son opcionales) para que el controller pueda
 * asociarla al refugio correspondiente antes de llamar al Service.
 */
@Data
public class MascotaDTO {

    private Long id;
    private RefugioDTO refugio;
}
