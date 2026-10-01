package co.edu.udistrital.mdp.pets.dto;

import lombok.Data;

/**
 * DTO basico de Vacuna. No incluye la coleccion de registros de vacunacion
 * (esa asociacion es de cardinalidad "*" y se expone en VacunaDTODetail).
 */
@Data
public class VacunaDTO {

    private Long id;
    private String nombre;
    private String descripcion;
}
