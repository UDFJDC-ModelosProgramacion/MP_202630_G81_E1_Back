package co.edu.udistrital.mdp.pets.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class HistoriaExitoDTODetail extends HistoriaExitoDTO {
    private MascotaDTO mascota;
}
