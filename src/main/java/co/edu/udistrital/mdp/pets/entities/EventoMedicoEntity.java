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
 * Entidad que representa un evento relacionado con la salud de una
 * mascota, como una enfermedad, cirugía u otra situación médica
 * relevante.
 *
 * @author Eddie Santiago Rondón Capera
 */
@Data
@Entity
public class EventoMedicoEntity extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Fecha en la que ocurre el evento médico.
	 */
	@Temporal(TemporalType.DATE)
	private Date fecha;

	/**
	 * Descripción general del evento médico.
	 */
	private String descripcion;

	/**
	 * Diagnóstico asociado al evento médico.
	 */
	private String diagnostico;

	/**
	 * Tratamiento indicado para el evento médico.
	 */
	private String tratamiento;

	/**
	 * Mascota a la que corresponde el evento médico.
	 * Relación Mascota (1) — EventoMedico (*).
	 */
	@PodamExclude
	@ManyToOne
	private MascotaEntity mascota;
}
