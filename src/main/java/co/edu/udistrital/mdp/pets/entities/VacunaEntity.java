package co.edu.udistrital.mdp.pets.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.co.jemos.podam.common.PodamExclude;

/**
 * Entidad que representa una vacuna que puede ser aplicada a una mascota.
 *
 * Relación: VacunaEntity "1" --> "*" RegistroVacunacionEntity
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class VacunaEntity extends BaseEntity {

	private String nombre;

	private String descripcion;

	@PodamExclude
	@OneToMany(mappedBy = "vacuna")
	private List<RegistroVacunacionEntity> registrosVacunacion = new ArrayList<>();

}
