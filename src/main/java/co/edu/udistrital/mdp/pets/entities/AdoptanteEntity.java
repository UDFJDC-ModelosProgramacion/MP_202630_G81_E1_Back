package co.edu.udistrital.mdp.pets.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.co.jemos.podam.common.PodamExclude;

/**
 * Entidad que representa a una persona interesada en adoptar una mascota.
 *
 * Relaciones implementadas:
 * - AdoptanteEntity "1" --> "*" SolicitudAdopcionEntity
 * - AdoptanteEntity "1" --> "*" MensajeEntity
 * (SolicitudAdopcionEntity y MensajeEntity son placeholders temporales, ver
 * esas clases, mientras sus responsables suben la versión completa a
 * Develop).
 *
 * Relaciones pendientes (entidades que todavía no existen en ninguna rama):
 * - AdoptanteEntity "1" --> "*" ResenaEntity
 * - AdoptanteEntity "1" --> "*" NotificacionEntity
 * - AdoptanteEntity "1" --> "*" ActualizacionEntity
 * Cuando esas entidades existan, agregar aquí los @OneToMany correspondientes.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class AdoptanteEntity extends BaseEntity {

	private String nombre;

	private String telefono;

	private String email;

	private String direccion;

	private String ciudad;

	@PodamExclude
	@OneToMany(mappedBy = "adoptante")
	private List<SolicitudAdopcionEntity> solicitudesAdopcion = new ArrayList<>();

	@PodamExclude
	@OneToMany(mappedBy = "adoptante")
	private List<MensajeEntity> mensajes = new ArrayList<>();

}
