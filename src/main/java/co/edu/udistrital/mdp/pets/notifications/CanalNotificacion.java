package co.edu.udistrital.mdp.pets.notifications;

/**
 * Interfaz Strategy que define cómo se envía una notificación.
 *
 * La entidad Notificacion (aún no implementada por el equipo) usará una
 * implementación de esta interfaz para delegar el envío según el canal
 * elegido, sin necesidad de conocer los detalles de cada medio.
 */
public interface CanalNotificacion {

	boolean enviar(String destinatario, String mensaje);

}
