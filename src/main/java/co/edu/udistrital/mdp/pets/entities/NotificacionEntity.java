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
 * Entidad que representa los avisos automáticos enviados por el sistema
 * sobre eventos como citas, vacunación o seguimientos. Puede notificarse
 * tanto respecto a una mascota como a un adoptante.
 *
 * Nota de diseño: en el diagrama UML el atributo "canal" es privado y de
 * tipo CanalNotificacion, que corresponde a una interfaz (patrón Strategy),
 * no a una entidad persistible. Por eso aquí se guarda como String el
 * identificador del canal (por ejemplo "EMAIL", "SMS", "PUSH"); la
 * resolución de cuál implementación de CanalNotificacion usar para
 * enviarla se hace en la capa de lógica de negocio, no en la entidad.
 * El método enviar() del diagrama tampoco vive aquí por la misma razón.
 *
 * @author Eddie Santiago Rondón Capera
 */
@Data
@Entity
public class NotificacionEntity extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Tipo de notificación (por ejemplo: cita, vacunación, seguimiento).
	 */
	private String tipo;

	/**
	 * Mensaje que se envía.
	 */
	private String mensaje;

	/**
	 * Fecha en la que se genera la notificación.
	 */
	@Temporal(TemporalType.DATE)
	private Date fecha;

	/**
	 * Identificador del canal por el cual se envía la notificación
	 * (EMAIL, SMS, PUSH, etc.). Ver nota de diseño de la clase.
	 */
	private String canal;

	/**
	 * Mascota sobre la que trata la notificación.
	 * Relación Mascota (1) — Notificacion (*).
	 */
	@PodamExclude
	@ManyToOne
	private MascotaEntity mascota;

	/**
	 * Adoptante que recibe la notificación.
	 * Relación Adoptante (1) — Notificacion (*).
	 */
	@PodamExclude
	@ManyToOne
	private AdoptanteEntity adoptante;
}
