package co.edu.udistrital.mdp.pets.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Detalle de Veterinario: agrega la asociacion de cardinalidad "muchos"
 * (seguimientos). El refugio (cardinalidad 1) ya esta en VeterinarioDTO.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class VeterinarioDetailDTO extends VeterinarioDTO {

    private List<SeguimientoDTO> seguimientos = new ArrayList<>();
}
