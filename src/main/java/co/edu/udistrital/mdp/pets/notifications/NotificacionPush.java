package co.edu.udistrital.mdp.pets.notifications;

import org.springframework.stereotype.Component;

/**
 * Estrategia concreta para el envío de notificaciones push.
 */
@Component("notificacionPush")
public class NotificacionPush implements CanalNotificacion {

	@Override
	public boolean enviar(String destinatario, String mensaje) {
		if (destinatario == null || destinatario.isBlank()) {
			return false;
		}
		// TODO: integrar con un proveedor real de push (p.ej. Firebase Cloud Messaging)
		System.out.println("Enviando PUSH a " + destinatario + ": " + mensaje);
		return true;
	}

}
