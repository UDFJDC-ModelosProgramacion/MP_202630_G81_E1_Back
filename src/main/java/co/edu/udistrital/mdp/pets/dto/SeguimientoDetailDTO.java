package co.edu.udistrital.mdp.pets.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Detalle de Seguimiento: todas sus asociaciones (mascota y veterinario)
 * tienen cardinalidad 1, por lo que ya estan en SeguimientoDTO. Se crea
 * para mantener la misma estructura DTO/DetailDTO en todos los recursos.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SeguimientoDetailDTO extends SeguimientoDTO {
}
