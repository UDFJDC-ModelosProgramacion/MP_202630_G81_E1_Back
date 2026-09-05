package co.edu.udistrital.mdp.pets.notifications;

import org.springframework.stereotype.Component;

/**
 * Estrategia concreta para el envío de notificaciones por SMS.
 */
@Component("notificacionSMS")
public class NotificacionSMS implements CanalNotificacion {

	@Override
	public boolean enviar(String destinatario, String mensaje) {
		if (destinatario == null || destinatario.isBlank()) {
			return false;
		}
		// TODO: integrar con un proveedor real de SMS (p.ej. Twilio)
		System.out.println("Enviando SMS a " + destinatario + ": " + mensaje);
		return true;
	}

}
