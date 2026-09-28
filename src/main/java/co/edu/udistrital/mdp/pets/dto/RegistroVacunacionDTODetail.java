package co.edu.udistrital.mdp.pets.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DTO detallado de RegistroVacunacion. RegistroVacunacion no tiene ninguna
 * asociacion de cardinalidad "*" propia (sus tres asociaciones -vacuna,
 * mascota, seguimientoOrigen- ya son de cardinalidad 1 y quedaron en
 * RegistroVacunacionDTO), asi que este detalle no agrega atributos nuevos;
 * se conserva como clase separada para cumplir el contrato DTO/DTODetail
 * de la actividad y por si en el futuro se agrega alguna asociacion "*".
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RegistroVacunacionDTODetail extends RegistroVacunacionDTO {
}
