package co.edu.udistrital.mdp.pets.entities;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

/**
 * Entidad que representa una actualización relacionada con una mascota
 * después de su adopción, como información médica, cambio de dirección
 * o fotografías. Puede estar asociada tanto a la mascota como al
 * adoptante que la reporta.
 *
 * @author Eddie Santiago Rondón Capera
 */
@Data
@Entity
public class ActualizacionEntity extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Fecha en la que se realiza la actualización.
	 */
	@Temporal(TemporalType.DATE)
	private Date fecha;

	/**
	 * Tipo de actualización (por ejemplo: médica, dirección, fotografía).
	 */
	private String tipo;

	/**
	 * Descripción breve de la actualización.
	 */
	private String descripcion;

	/**
	 * URL del archivo (foto, documento, etc.) asociado a la actualización.
	 */
	private String archivoUrl;

	/**
	 * Mascota sobre la cual se realiza la actualización.
	 * Relación Mascota (1) — Actualizacion (*).
	 */
	@PodamExclude
	@ManyToOne
	private MascotaEntity mascota;

	/**
	 * Adoptante que reporta la actualización.
	 * Relación Adoptante (1) — Actualizacion (*).
	 */
	@PodamExclude
	@ManyToOne
	private AdoptanteEntity adoptante;
}
