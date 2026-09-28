package co.edu.udistrital.mdp.pets.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Detalle de Mascota: agrega la asociacion de cardinalidad "muchos"
 * (seguimientos). El refugio (cardinalidad 1) ya esta en MascotaDTO.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MascotaDetailDTO extends MascotaDTO {

    private List<SeguimientoDTO> seguimientos = new ArrayList<>();
}
