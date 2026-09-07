package co.edu.udistrital.mdp.pets.notifications;

import org.springframework.stereotype.Component;

/**
 * Estrategia concreta para el envío de notificaciones por correo electrónico.
 */
@Component("notificacionEmail")
public class NotificacionEmail implements CanalNotificacion {

	@Override
	public boolean enviar(String destinatario, String mensaje) {
		if (destinatario == null || destinatario.isBlank()) {
			return false;
		}
		// TODO: integrar con un proveedor real de correo (p.ej. JavaMailSender)
		System.out.println("Enviando EMAIL a " + destinatario + ": " + mensaje);
		return true;
	}

}
