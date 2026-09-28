package co.edu.udistrital.mdp.pets.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DTO detallado de Vacuna. Agrega la coleccion de registros de vacunacion
 * asociados (asociacion "1" --> "*" con RegistroVacunacion).
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class VacunaDTODetail extends VacunaDTO {

    private List<RegistroVacunacionDTO> registrosVacunacion = new ArrayList<>();
}
