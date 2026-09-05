package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.co.jemos.podam.common.PodamExclude;

/**
 * PLACEHOLDER TEMPORAL.
 *
 * Esta clase le corresponde implementar por completo al integrante
 * encargado de SolicitudAdopcion (rama Cristian_branch). Solo se agregó el
 * campo 'adoptante', porque es necesario para que compile la relación
 * AdoptanteEntity "1" --> "*" SolicitudAdopcionEntity (mappedBy) desde este
 * lado.
 *
 * Cuando esa rama se integre a Develop, este archivo debe reemplazarse por
 * la versión completa (con sus atributos y demás asociaciones), conservando
 * este campo 'adoptante' para que la relación no se rompa.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class SolicitudAdopcionEntity extends BaseEntity {

	@PodamExclude
	@ManyToOne
	private AdoptanteEntity adoptante;

}
