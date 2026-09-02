package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Entidad que representa a una persona interesada en adoptar una mascota.
 *
 * Relaciones pendientes (entidades aún no creadas por el equipo):
 * - Adoptante "1" --> "*" SolicitudAdopcion
 * - Adoptante "1" --> "*" Mensaje
 * - Adoptante "1" --> "*" Resena
 * - Adoptante "1" --> "*" Notificacion
 * - Adoptante "1" --> "*" Actualizacion
 * Cuando esas entidades existan, agregar aquí los @OneToMany correspondientes.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Adoptante extends BaseEntity {

	private String nombre;

	private String telefono;

	private String email;

	private String direccion;

	private String ciudad;

}
