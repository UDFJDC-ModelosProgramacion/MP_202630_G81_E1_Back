package co.edu.udistrital.mdp.pets.entities;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.co.jemos.podam.common.PodamExclude;

/**
 * Entidad que representa el registro de aplicación de una vacuna a una mascota.
 *
 * Relaciones implementadas: - RegistroVacunacionEntity "*" --> "1" VacunaEntity
 * - MascotaEntity "1" --> "*" RegistroVacunacionEntity - SeguimientoEntity
 * "0..1" --> "*" RegistroVacunacionEntity : origina
 *
 * MascotaEntity y SeguimientoEntity son placeholders temporales (ver esas
 * clases) mientras sus responsables suben la versión completa a Develop.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class RegistroVacunacionEntity extends BaseEntity {

	@Temporal(TemporalType.DATE)
	private Date fechaAplicacion;

	@Temporal(TemporalType.DATE)
	private Date proximaFecha;

	private String numeroLote;

	private String observacion;

	@PodamExclude
	@ManyToOne
	private VacunaEntity vacuna;

	@PodamExclude
	@ManyToOne
	private MascotaEntity mascota;

	@PodamExclude
	@ManyToOne
	private SeguimientoEntity seguimientoOrigen;

}
