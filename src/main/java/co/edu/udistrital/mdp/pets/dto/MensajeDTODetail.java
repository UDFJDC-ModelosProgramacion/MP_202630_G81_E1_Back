package co.edu.udistrital.mdp.pets.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MensajeDTODetail extends MensajeDTO {
    private MascotaDTO mascota;
    private AdoptanteDTO adoptante;
}
