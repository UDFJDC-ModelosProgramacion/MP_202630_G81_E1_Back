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
 * Entidad que representa la calificación y el comentario realizados por
 * un adoptante sobre su experiencia con una mascota.
 *
 * @author Eddie Santiago Rondón Capera
 */
@Data
@Entity
public class ResenaEntity extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Calificación numérica dada por el adoptante.
	 */
	private Integer calificacion;

	/**
	 * Comentario escrito por el adoptante.
	 */
	private String comentario;

	/**
	 * Fecha en la que se realiza la reseña.
	 */
	@Temporal(TemporalType.DATE)
	private Date fecha;

	/**
	 * Mascota sobre la que trata la reseña.
	 * Relación Mascota (1) — Resena (*).
	 */
	@PodamExclude
	@ManyToOne
	private MascotaEntity mascota;

	/**
	 * Adoptante que escribió la reseña.
	 * Relación Adoptante (1) — Resena (*).
	 */
	@PodamExclude
	@ManyToOne
	private AdoptanteEntity adoptante;
}
