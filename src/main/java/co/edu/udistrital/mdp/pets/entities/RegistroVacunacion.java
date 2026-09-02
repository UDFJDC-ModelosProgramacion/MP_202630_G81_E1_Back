package co.edu.udistrital.mdp.pets.entities;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Entidad que representa el registro de aplicación de una vacuna a una
 * mascota.
 *
 * Relaciones ya implementadas:
 * - RegistroVacunacion "*" --> "1" Vacuna
 *
 * Relaciones pendientes (entidades aún no creadas por el equipo):
 * - Mascota "1" --> "*" RegistroVacunacion
 * - Seguimiento "0..1" --> "*" RegistroVacunacion : origina
 * Cuando esas entidades existan, agregar aquí el @ManyToOne correspondiente.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class RegistroVacunacion extends BaseEntity {

	@Temporal(TemporalType.DATE)
	private Date fechaAplicacion;

	@Temporal(TemporalType.DATE)
	private Date proximaFecha;

	private String numeroLote;

	private String observacion;

	@ManyToOne
	private Vacuna vacuna;

}
