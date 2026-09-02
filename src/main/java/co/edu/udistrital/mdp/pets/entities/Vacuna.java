package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Entidad que representa una vacuna que puede ser aplicada a una mascota.
 *
 * Relación: RegistroVacunacion "*" --> "1" Vacuna
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Vacuna extends BaseEntity {

	private String nombre;

	private String descripcion;

}
